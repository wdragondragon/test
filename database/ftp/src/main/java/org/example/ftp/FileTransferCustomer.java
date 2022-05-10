package org.example.ftp;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.example.ftp.communication.Communication;
import org.example.ftp.file.FileRecord;
import org.example.ftp.key.Key;
import org.example.ftp.helper.FileHelper;
import org.example.ftp.key.UploadStatus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

/**
 * @Author JDragon
 * @Date 2022.05.05 下午 7:05
 * @Email 1061917196@qq.com
 * @Des:
 */
@Slf4j
public class FileTransferCustomer implements Runnable {

    private final int transferBuffer;

    private final int retryTimes;

    private final Queue<String> fileRecordConcurrentLinkedQueue;

    private final List<FileLink> result = new LinkedList<>();

    private final FileHelper sourceFileHelper;

    private final String sourcePath;

    private final String targetPath;

    private final FileHelper targetFileHelper;

    private final CountDownLatch countDownLatch;

    private final Communication communication;

    private final String name;

    @Builder
    public FileTransferCustomer(int num,
                                FileHelper sourceFileHelper, String sourcePath,
                                FileHelper targetFileHelper, String targetPath,
                                Queue<String> fileRecordConcurrentLinkedQueue, CountDownLatch countDownLatch,
                                int transferBuffer, int retryTimes) {
        this.name = "文件传输线程-" + num;
        this.fileRecordConcurrentLinkedQueue = fileRecordConcurrentLinkedQueue;
        this.countDownLatch = countDownLatch;
        this.sourceFileHelper = sourceFileHelper;
        this.targetFileHelper = targetFileHelper;
        this.sourcePath = sourcePath;
        this.targetPath = targetPath;
        this.communication = new Communication(getName());
        this.transferBuffer = transferBuffer;
        this.retryTimes = retryTimes;
    }

    @Override
    public void run() {
        try {
            communication.start();
            String fileName;
            while ((fileName = fileRecordConcurrentLinkedQueue.poll()) != null) {
                FileRecord sourceFileRecord = sourceFileHelper.initFile(sourcePath, fileName);
                FileRecord targetFileRecord = targetFileHelper.initFile(targetPath, fileName);
                final UploadStatus uploadStatus = startTransfer(sourceFileRecord, targetFileRecord);
                if (UploadStatus.needFreshStatus.contains(uploadStatus)) {
                    sourceFileRecord.refresh();
                    targetFileRecord.refresh();
                }
                result.add(new FileLink(sourceFileRecord.getFileFullPath(), targetFileRecord.getFileFullPath(), uploadStatus));
                log.info("上传结果：[{}]", uploadStatus);
            }
            communication.finish();
            sourceFileHelper.close();
            targetFileHelper.close();
        } catch (Exception e) {
            log.error("[{}] 采集异常", getName(), e);
        }
        countDownLatch.countDown();
    }

    public UploadStatus startTransfer(FileRecord sourceFileRecord, FileRecord targetFileRecord) throws IOException {
        try {
            synchronized (targetFileRecord.getFilePath().intern()) {
                targetFileRecord.mkParentDir();
            }
        } catch (IOException e) {
            log.error("[{}] 创建目标文件夹失败", getName(), e);
            return UploadStatus.Create_Directory_Fail;
        }
        long skipSize = 0L;
        //检查远程是否存在文件
        if (!sourceFileRecord.exists()) {
            log.error("[{}] 源文件不存在,{}", getName(), sourceFileRecord.getFileFullPath());
            return UploadStatus.Source_not_exist;
        }
        if (sourceFileRecord.exists() && targetFileRecord.exists()) {
            skipSize = targetFileRecord.getSize();
            long sourceSize = sourceFileRecord.getSize();
            if (skipSize == sourceSize) {
                return UploadStatus.File_Exits;
            } else if (skipSize > sourceSize) {
                return UploadStatus.Remote_Bigger_Local;
            }
        }

        FilePart filePart = new FilePart(skipSize, sourceFileRecord.getSize());

        communication.setString("sourcePath", sourceFileRecord.getFileFullPath());
        communication.setString("targetPath", targetFileRecord.getFileFullPath());
        communication.setNumber("start", filePart.getStart());
        communication.setNumber("fileSize", filePart.getEnd());
        communication.setNumber("transferSize", filePart.getSize());
        communication.setNumber("transferCount", 0L);
        communication.setNumber("count", filePart.getStart());
        communication.setNumber("startTime", System.currentTimeMillis());

        this.updateConcurrentTransfer(sourceFileRecord.getFileFullPath(), targetFileRecord.getFileFullPath(), filePart.getStart());
        return this.startTransfer(sourceFileRecord, targetFileRecord, filePart);
    }

    public void updateConcurrentTransfer(String sourcePath, String targetPath, long skipSize) {
        log.info("[{}] 文件[{}] 开始传输 [{}]", getName(), sourcePath, targetPath);
        communication.setString(Key.CONCURRENT_SOURCE_PATH, sourcePath);
        communication.setString(Key.CONCURRENT_TARGET_PATH, targetPath);
        communication.setNumber(Key.SEQUEL_INDEX, skipSize);
    }

    public UploadStatus startTransfer(FileRecord sourceFileRecord, FileRecord targetFileRecord, FilePart filePart) {
        if (filePart.getStart() == 0L) {
            if (transfer(sourceFileRecord, targetFileRecord, filePart)) {
                return UploadStatus.Upload_New_File_Success;
            } else {
                return UploadStatus.Upload_New_File_Failed;
            }
        } else {
            log.info("[{}] 启动续传", getName());
            //开始断点续传
            if (transfer(sourceFileRecord, targetFileRecord, filePart)) {
                return UploadStatus.Upload_From_Break_Success;
            } else {
                return UploadStatus.Delete_Remote_Faild;
            }
        }
    }

    public boolean transfer(FileRecord sourceFileRecord, FileRecord targetFileRecord, FilePart filePart) {
        try {
            long start = filePart.getStart();
            long transferSize = filePart.getSize();

            byte[] bytes = new byte[transferBuffer];
            long count = start;
            int readCount;
            int retry = 0;
            while ((count - start) != transferSize) {
                try (InputStream inputStream = sourceFileRecord.getInputStream(count);
                     OutputStream outputStream = targetFileRecord.getOutputStream(count)) {
                    while ((readCount = inputStream.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, readCount);
                        count += readCount;
                        communication.setNumber(Key.COUNT, count);
                        communication.increaseLong(Key.TRANSFER_COUNT, readCount);
                        communication.increaseLong(Key.ALL_TRANSFER_COUNT, readCount);
                        retry = 0;
                    }
                } catch (Exception e) {
                    retry++;
                    if (retry == retryTimes) {
                        break;
                    }
                    log.error("[{}] 采集异常，剩余重试次数[{}]", getName(), retryTimes - retry, e);
                    Thread.sleep(3000L * retry);
                }
            }
        } catch (Exception e) {
            log.error("[{}] 文件传输失败", getName(), e);
            return false;
        }
        return true;
    }

    public List<FileLink> getResult() {
        return result;
    }

    public String getName() {
        return name;
    }
}
