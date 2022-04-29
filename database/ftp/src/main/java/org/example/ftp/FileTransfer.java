package org.example.ftp;

import org.example.ftp.file.FTPFileRecord;
import org.example.ftp.file.FileRecord;
import org.example.ftp.file.LocalFileRecord;
import org.example.ftp.file.SFTPFileRecord;
import org.example.ftp.util.FTPClientCloseable;
import org.example.ftp.util.SFTPClientCloseable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author JDragon
 * @Date 2022.04.27 下午 2:12
 * @Email 1061917196@qq.com
 * @Des:
 */
public class FileTransfer {

    private static final Logger LOG = LoggerFactory.getLogger(FileTransfer.class);

    private final static Map<Integer, String> unitMap = new HashMap<Integer, String>() {{
        put(1, "b");
        put(2, "kb");
        put(3, "mb");
        put(4, "gb");
    }};

    public UploadStatus startTransfer(FileRecord sourceFileRecord, FileRecord targetFileRecord, int concurrency) throws IOException {
        try {
            sourceFileRecord.mkParentDir();
            targetFileRecord.mkParentDir();
        } catch (IOException e) {
            return UploadStatus.Create_Directory_Fail;
        }
        long skipSize = 0L;
        //检查远程是否存在文件
        if (!sourceFileRecord.exists()) {
            throw new IOException("源文件不存在");
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

        ExecutorService exec = Executors.newFixedThreadPool(concurrency);
        CountDownLatch latch = new CountDownLatch(concurrency);
        List<FilePart> filePartList = split(sourceFileRecord.getSize(), skipSize, concurrency, latch);

        for (FilePart filePart : filePartList) {
            FileTransferThread fileTransferThread = new FileTransferThread(sourceFileRecord, targetFileRecord, filePart, new AtomicLong(skipSize));
            exec.execute(fileTransferThread);
        }
        try {
            latch.await();
            exec.shutdown();
        } catch (InterruptedException e) {
            LOG.error("latch等待异常", e);
        }
        return UploadStatus.Upload_New_File_Success;
    }

    public boolean startTransfer(FileRecord sourceFileRecord, FileRecord targetFileRecord, long start, long end) {
        try {
            InputStream inputStream = sourceFileRecord.getInputStream(start);
            OutputStream outputStream = targetFileRecord.getOutputStream(start);
            byte[] bytes = new byte[10240];
            long count = start;
            int readCount;
            while ((readCount = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, readCount);
                count += readCount;
                LOG.info("进度 {}", toUnit(count));
            }
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            LOG.error("文件传输失败", e);
            return false;
        }
        return true;
    }

    public List<FilePart> split(long size, long skip, int num, CountDownLatch countDownLatch) {
        if (num == 1) {
            return Collections.singletonList(new FilePart(1, skip, size, countDownLatch));
        }
        List<FilePart> filePartList = new LinkedList<>();
        long part = (size - skip) / num;
        long startTemp = skip;
        for (int i = 0; i < num - 1; i++) {
            long end = startTemp + part;
            filePartList.add(new FilePart(i + 1, startTemp, end, countDownLatch));
            startTemp = end;
        }
        filePartList.add(new FilePart(num, startTemp, size, countDownLatch));
        return filePartList;
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

    public static void main(String[] args) throws Exception {
        FileTransfer fileTransfer = new FileTransfer();

//        fileTransfer.localToLocal();
//        fileTransfer.localToFtp();
        fileTransfer.localToSftp();

//        fileTransfer.ftpToLocal();
//        fileTransfer.ftpToFtp();
//        fileTransfer.ftpToSftp();

//        fileTransfer.sftpToLocal();
//        fileTransfer.sftpToFtp();
//        fileTransfer.sftpToSftp();
    }


    int concurrency = 2;


    public void localToLocal() throws Exception {
        FileRecord sourceLocalFileRecord = new LocalFileRecord("C:\\Users\\10619\\Desktop\\test.jar");
        FileRecord targetLocalFileRecord = new LocalFileRecord("C:\\Users\\10619\\Desktop\\test-local-local.jar");
        LOG.info("上传结果：[{}]", startTransfer(sourceLocalFileRecord, targetLocalFileRecord, concurrency));
    }


    public void localToFtp() throws Exception {
        try (FTPClientCloseable targetFtp = new FTPClientCloseable("10.194.188.77", 21, "user_test", "EhnkvrX1V20=");) {
            FileRecord sourceLocalFileRecord = new LocalFileRecord("C:\\Users\\10619\\Desktop\\test.jar");
            FileRecord targetFTPFileRecord = new FTPFileRecord(targetFtp, "test-local-ftp.jar");
            LOG.info("上传结果：[{}]", startTransfer(sourceLocalFileRecord, targetFTPFileRecord, concurrency));
        }
    }

    public void localToSftp() throws Exception {
        try (SFTPClientCloseable sftpClientCloseable = new SFTPClientCloseable("10.194.188.77", 22, "root", "EhnkvrX1V20=");) {
            sftpClientCloseable.connect();
            FileRecord sourceLocalFileRecord = new LocalFileRecord("C:\\Users\\10619\\Desktop\\test.jar");
            FileRecord targetSFTPFileRecord = new SFTPFileRecord(sftpClientCloseable, "/data/ftp/user_test/test-local-sftp.jar");
            final OutputStream outputStream = targetSFTPFileRecord.getOutputStream(0L);
            final OutputStream outputStream2 = targetSFTPFileRecord.getOutputStream(4L);
            LOG.info("上传结果：[{}]", startTransfer(sourceLocalFileRecord, targetSFTPFileRecord, concurrency));
        }
    }


    public void ftpToLocal() throws Exception {
        try (FTPClientCloseable sourceFtp = new FTPClientCloseable("10.194.188.77", 21, "user_test", "EhnkvrX1V20=");) {
            FileRecord sourceFTPFileRecord = new FTPFileRecord(sourceFtp, "test-local-ftp.jar");
            FileRecord targetLocalFileRecord = new LocalFileRecord("C:\\Users\\10619\\Desktop\\test-ftp-local.jar");

            LOG.info("上传结果：[{}]", startTransfer(sourceFTPFileRecord, targetLocalFileRecord, concurrency));
        }
    }

    public void ftpToFtp() throws Exception {
        try (FTPClientCloseable sourceFtp = new FTPClientCloseable("10.194.188.77", 21, "user_test", "EhnkvrX1V20=");
             FTPClientCloseable targetFtp = new FTPClientCloseable("10.194.188.77", 21, "user_test", "EhnkvrX1V20=");) {
            FileRecord sourceFTPFileRecord = new FTPFileRecord(sourceFtp, "test-local-ftp.jar");
            FileRecord targetFTPFileRecord = new FTPFileRecord(targetFtp, "test-ftp-ftp.jar");
            LOG.info("上传结果：[{}]", startTransfer(sourceFTPFileRecord, targetFTPFileRecord, concurrency));
        }
    }


    public void ftpToSftp() throws Exception {
        try (FTPClientCloseable sourceFtp = new FTPClientCloseable("10.194.188.77", 21, "user_test", "EhnkvrX1V20=");
             SFTPClientCloseable sftpClientCloseable = new SFTPClientCloseable("10.194.188.77", 22, "root", "EhnkvrX1V20=");) {
            sftpClientCloseable.connect();
            FileRecord sourceFTPFileRecord = new FTPFileRecord(sourceFtp, "test-local-ftp.jar");
            FileRecord targetSFTPFileRecord = new SFTPFileRecord(sftpClientCloseable, "/data/ftp/user_test/test-ftp-sftp.jar");
            LOG.info("上传结果：[{}]", startTransfer(sourceFTPFileRecord, targetSFTPFileRecord, concurrency));
        }
    }


    public void sftpToLocal() throws Exception {
        try (SFTPClientCloseable sftpClientCloseable = new SFTPClientCloseable("10.194.188.77", 22, "root", "EhnkvrX1V20=");) {
            sftpClientCloseable.connect();
            FileRecord sourceSFTPFileRecord = new SFTPFileRecord(sftpClientCloseable, "/data/ftp/user_test/test-local-sftp.jar");
            FileRecord targetLocalFileRecord = new LocalFileRecord("C:\\Users\\10619\\Desktop\\test-sftp-local.jar");
            LOG.info("上传结果：[{}]", startTransfer(sourceSFTPFileRecord, targetLocalFileRecord, concurrency));
        }
    }


    public void sftpToFtp() throws Exception {
        try (FTPClientCloseable targetFtp = new FTPClientCloseable("10.194.188.77", 21, "user_test", "EhnkvrX1V20=");
             SFTPClientCloseable sftpClientCloseable = new SFTPClientCloseable("10.194.188.77", 22, "root", "EhnkvrX1V20=");) {
            sftpClientCloseable.connect();
            FileRecord sourceSFTPFileRecord = new SFTPFileRecord(sftpClientCloseable, "/data/ftp/user_test/test-local-sftp.jar");
            FileRecord targetFTPFileRecord = new FTPFileRecord(targetFtp, "test-sftp-ftp.jar");
            LOG.info("上传结果：[{}]", startTransfer(sourceSFTPFileRecord, targetFTPFileRecord, concurrency));
        }
    }


    public void sftpToSftp() throws Exception {
        try (SFTPClientCloseable sourceSftpClientCloseable = new SFTPClientCloseable("10.194.188.77", 22, "root", "EhnkvrX1V20=");
             SFTPClientCloseable targetSftpClientCloseable = new SFTPClientCloseable("10.194.188.77", 22, "root", "EhnkvrX1V20=");) {
            sourceSftpClientCloseable.connect();
            targetSftpClientCloseable.connect();
            FileRecord sourceSFTPFileRecord = new SFTPFileRecord(sourceSftpClientCloseable, "/data/ftp/user_test/test-local-sftp.jar");
            FileRecord targetSFTPFileRecord = new SFTPFileRecord(targetSftpClientCloseable, "/data/ftp/user_test/test-sftp-sftp.jar");
            LOG.info("上传结果：[{}]", startTransfer(sourceSFTPFileRecord, targetSFTPFileRecord, concurrency));
        }
    }
}