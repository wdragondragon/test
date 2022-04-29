package org.example.ftp;

import lombok.extern.slf4j.Slf4j;
import org.example.ftp.file.FileRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author JDragon
 * @Date 2022.04.29 下午 2:46
 * @Email 1061917196@qq.com
 * @Des:
 */
@Slf4j
public class FileTransferThread extends Thread {
    private final static Map<Integer, String> unitMap = new HashMap<Integer, String>() {{
        put(1, "b");
        put(2, "kb");
        put(3, "mb");
        put(4, "gb");
    }};
    private final AtomicLong fileProgress;

    private final FileRecord sourceFileRecord;

    private final FileRecord targetFileRecord;

    private final FilePart filePart;

    public FileTransferThread(FileRecord sourceFileRecord, FileRecord targetFileRecord, FilePart filePart, AtomicLong fileProgress) {
        this.setName("文件采集线程-" + filePart.getPartId());
        this.sourceFileRecord = sourceFileRecord;
        this.targetFileRecord = targetFileRecord;
        this.filePart = filePart;
        this.fileProgress = fileProgress;
    }

    @Override
    public void run() {
        try {
            UploadStatus uploadStatus = startTransfer(sourceFileRecord, targetFileRecord, filePart);
            log.info(getName() + " 传输完成：" + uploadStatus);
            filePart.finish(uploadStatus);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UploadStatus startTransfer(FileRecord sourceFileRecord, FileRecord targetFileRecord, FilePart filePart) throws IOException {
        if (filePart.getStart() == 0L) {
            if (transfer(sourceFileRecord, targetFileRecord, filePart)) {
                return UploadStatus.Upload_New_File_Success;
            } else {
                return UploadStatus.Upload_New_File_Failed;
            }
        } else {
            //开始断点续传
            if (transfer(sourceFileRecord, targetFileRecord, filePart)) {
                return UploadStatus.Upload_From_Break_Success;
            }
//            else if (targetFileRecord.delete()) {
//                if (transfer(sourceFileRecord, targetFileRecord, filePart)) {
//                    return UploadStatus.Upload_New_File_Success;
//                } else {
//                    return UploadStatus.Upload_New_File_Failed;
//                }
//            }
            else {
                return UploadStatus.Delete_Remote_Faild;
            }
        }
    }

    public boolean transfer(FileRecord sourceFileRecord, FileRecord targetFileRecord, FilePart filePart) {
        try {
            long start = filePart.getStart();
            long end = filePart.getEnd();
            long transferSize = filePart.getSize();
            long fileSize = sourceFileRecord.getSize();

            byte[] bytes = new byte[10240];
            long count = start;
            int readCount;
            while ((count - start) != transferSize) {
                try (InputStream inputStream = sourceFileRecord.getInputStream(count);
                     OutputStream outputStream = targetFileRecord.getOutputStream(count)) {
                    while ((readCount = inputStream.read(bytes)) != -1) {
                        synchronized (this.targetFileRecord) {
                            count += readCount;
                            long fileProgressSize = this.fileProgress.addAndGet(readCount);
                            if (count > end) {
                                count = end;
                            }
                            outputStream.write(bytes, 0, readCount);
                            log.info(getName() + " 进度{}%,文件进度{}%, {}", (count - start) * 100 / transferSize, fileProgressSize * 100 / fileSize, toUnit(fileProgressSize));
                            if (count == end) {
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("{} 采集异常", getName(), e);
                    Thread.sleep(3000L);
                }
            }
        } catch (Exception e) {
            log.error(getName() + " 文件传输失败", e);
            return false;
        }
        return true;
    }

    public String toUnit(long size) {
        List<Integer> numList = new LinkedList<>();
        while (size != 0) {
            numList.add((int) (size & 1023));
            size = size >> 10;
        }
        StringBuilder progress = new StringBuilder();
        for (int i = numList.size() - 1; i >= 0; i--) {
            progress.append(numList.get(i)).append(unitMap.get(i + 1)).append(" ");
        }
        return progress.toString();
    }
}
