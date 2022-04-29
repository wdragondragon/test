package org.example.ftp;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    public UploadStatus startTransfer(FileRecord sourceFileRecord, FileRecord targetFileRecord) throws IOException {
        sourceFileRecord.mkParentDir();
        targetFileRecord.mkParentDir();
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

        if (skipSize == 0L) {
            if (startTransfer(sourceFileRecord.getInputStream(skipSize), targetFileRecord.getOutputStream(skipSize), skipSize)) {
                return UploadStatus.Upload_New_File_Success;
            } else {
                return UploadStatus.Upload_New_File_Failed;
            }
        } else {
            //开始断点续传
            if (startTransfer(sourceFileRecord.getInputStream(skipSize), targetFileRecord.getOutputStream(skipSize), skipSize)) {
                return UploadStatus.Upload_From_Break_Success;
            } else if (targetFileRecord.delete()) {
                if (startTransfer(sourceFileRecord.getInputStream(skipSize), targetFileRecord.getOutputStream(skipSize), skipSize)) {
                    return UploadStatus.Upload_New_File_Success;
                } else {
                    return UploadStatus.Upload_New_File_Failed;
                }
            } else {
                return UploadStatus.Delete_Remote_Faild;
            }
        }
    }

    public boolean startTransfer(InputStream inputStream, OutputStream outputStream, long skipSize) {
        try {
            byte[] bytes = new byte[10240];
            long count = skipSize;
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

    public String toUnit(long size) {
        List<Integer> numList = new LinkedList<>();
        while (size != 0) {
            long remainder = (size & 1023);
            size = size >> 10;
            numList.add((int) (remainder));
        }
        StringBuilder progress = new StringBuilder();
        for (int i = numList.size() - 1; i >= 0; i--) {
            progress.append(numList.get(i)).append(unitMap.get(i + 1)).append(" ");
        }
        return progress.toString();
    }

    public static void main(String[] args) throws Exception {
        FileTransfer fileTransfer = new FileTransfer();

        fileTransfer.localToLocal();
        fileTransfer.localToFtp();
        fileTransfer.localToSftp();

        fileTransfer.ftpToLocal();
        fileTransfer.ftpToFtp();
        fileTransfer.ftpToSftp();

        fileTransfer.sftpToLocal();
        fileTransfer.sftpToFtp();
        fileTransfer.sftpToSftp();
    }


    public void localToLocal() throws Exception {
        FileRecord sourceLocalFileRecord = new LocalFileRecord("C:\\Users\\10619\\Desktop\\test.jar");
        FileRecord targetLocalFileRecord = new LocalFileRecord("C:\\Users\\10619\\Desktop\\test-local-local.jar");
        LOG.info("上传结果：[{}]", startTransfer(sourceLocalFileRecord, targetLocalFileRecord));
    }


    public void localToFtp() throws Exception {
        FTPClient targetFtp = FTPClientCreator.initFTPClient("10.194.188.77", 21, "user_test", "EhnkvrX1V20=");

        FileRecord sourceLocalFileRecord = new LocalFileRecord("C:\\Users\\10619\\Desktop\\test.jar");
        FileRecord targetFTPFileRecord = new FTPFileRecord(targetFtp, "test-local-ftp.jar");
        LOG.info("上传结果：[{}]", startTransfer(sourceLocalFileRecord, targetFTPFileRecord));
        FTPClientCreator.disconnect(targetFtp);
    }

    public void localToSftp() throws Exception {
        SFTP sftp = new SFTP("10.194.188.77", 22, "root", "EhnkvrX1V20=");

        sftp.connect();
        FileRecord sourceLocalFileRecord = new LocalFileRecord("C:\\Users\\10619\\Desktop\\test.jar");
        FileRecord targetSFTPFileRecord = new SFTPFileRecord(sftp, "/data/ftp/user_test/test-local-sftp.jar");
        LOG.info("上传结果：[{}]", startTransfer(sourceLocalFileRecord, targetSFTPFileRecord));
        sftp.close();
    }


    public void ftpToLocal() throws Exception {

        FTPClient sourceFtp = FTPClientCreator.initFTPClient("10.194.188.77", 21, "user_test", "EhnkvrX1V20=");
        FileRecord sourceFTPFileRecord = new FTPFileRecord(sourceFtp, "test-local-ftp.jar");
        FileRecord targetLocalFileRecord = new LocalFileRecord("C:\\Users\\10619\\Desktop\\test-ftp-local.jar");

        LOG.info("上传结果：[{}]", startTransfer(sourceFTPFileRecord, targetLocalFileRecord));
        FTPClientCreator.disconnect(sourceFtp);
    }

    public void ftpToFtp() throws Exception {
        FTPClient sourceFtp = FTPClientCreator.initFTPClient("10.194.188.77", 21, "user_test", "EhnkvrX1V20=");
        FTPClient targetFtp = FTPClientCreator.initFTPClient("10.194.188.77", 21, "user_test", "EhnkvrX1V20=");
        FileRecord sourceFTPFileRecord = new FTPFileRecord(sourceFtp, "test-local-ftp.jar");
        FileRecord targetFTPFileRecord = new FTPFileRecord(targetFtp, "test-ftp-ftp.jar");

        LOG.info("上传结果：[{}]", startTransfer(sourceFTPFileRecord, targetFTPFileRecord));
        FTPClientCreator.disconnect(sourceFtp);
        FTPClientCreator.disconnect(targetFtp);
    }


    public void ftpToSftp() throws Exception {
        FTPClient sourceFtp = FTPClientCreator.initFTPClient("10.194.188.77", 21, "user_test", "EhnkvrX1V20=");
        SFTP sftp = new SFTP("10.194.188.77", 22, "root", "EhnkvrX1V20=");

        sftp.connect();
        FileRecord sourceFTPFileRecord = new FTPFileRecord(sourceFtp, "test-local-ftp.jar");
        FileRecord targetSFTPFileRecord = new SFTPFileRecord(sftp, "/data/ftp/user_test/test-ftp-sftp.jar");

        LOG.info("上传结果：[{}]", startTransfer(sourceFTPFileRecord, targetSFTPFileRecord));
        FTPClientCreator.disconnect(sourceFtp);
        sftp.close();
    }


    public void sftpToLocal() throws Exception {
        SFTP sftp = new SFTP("10.194.188.77", 22, "root", "EhnkvrX1V20=");

        sftp.connect();
        FileRecord sourceSFTPFileRecord = new SFTPFileRecord(sftp, "/data/ftp/user_test/test-local-sftp.jar");
        FileRecord targetLocalFileRecord = new LocalFileRecord("C:\\Users\\10619\\Desktop\\test-sftp-local.jar");
        LOG.info("上传结果：[{}]", startTransfer(sourceSFTPFileRecord, targetLocalFileRecord));
        sftp.close();
    }


    public void sftpToFtp() throws Exception {
        FTPClient targetFtp = FTPClientCreator.initFTPClient("10.194.188.77", 21, "user_test", "EhnkvrX1V20=");
        SFTP sftp = new SFTP("10.194.188.77", 22, "root", "EhnkvrX1V20=");

        sftp.connect();
        FileRecord sourceSFTPFileRecord = new SFTPFileRecord(sftp, "/data/ftp/user_test/test-local-sftp.jar");
        FileRecord targetFTPFileRecord = new FTPFileRecord(targetFtp, "test-sftp-ftp.jar");
        LOG.info("上传结果：[{}]", startTransfer(sourceSFTPFileRecord, targetFTPFileRecord));
        FTPClientCreator.disconnect(targetFtp);
        sftp.close();
    }


    public void sftpToSftp() throws Exception {
        SFTP sourceSftp = new SFTP("10.194.188.77", 22, "root", "EhnkvrX1V20=");
        SFTP targetSftp = new SFTP("10.194.188.77", 22, "root", "EhnkvrX1V20=");
        sourceSftp.connect();
        targetSftp.connect();
        FileRecord sourceSFTPFileRecord = new SFTPFileRecord(sourceSftp, "/data/ftp/user_test/test-local-sftp.jar");
        FileRecord targetSFTPFileRecord = new SFTPFileRecord(targetSftp, "/data/ftp/user_test/test-sftp-sftp.jar");
        LOG.info("上传结果：[{}]", startTransfer(sourceSFTPFileRecord, targetSFTPFileRecord));
        sourceSftp.close();
        targetSftp.close();
    }
}