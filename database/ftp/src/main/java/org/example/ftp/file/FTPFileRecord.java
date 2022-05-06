package org.example.ftp.file;

import org.apache.commons.net.ftp.FTPFile;
import org.example.ftp.util.FTPClientCloseable;
import org.example.ftp.util.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Author JDragon
 * @Date 2022.04.28 下午 3:11
 * @Email 1061917196@qq.com
 * @Des:
 */
public class FTPFileRecord extends FileRecord {

    private static final Logger LOG = LoggerFactory.getLogger(FTPFileRecord.class);

    protected FTPClientCloseable ftpClient;

    protected FTPFile file;

    public FTPFileRecord(FileHelper ftpClient, String fileFullPath) throws IOException {
        super(fileFullPath);
        this.ftpClient = (FTPClientCloseable) ftpClient;
        this.ftpClient.changeWorkingDirectory("/");
        FTPFile[] files = this.ftpClient.listFiles(fileFullPath);
        if (files.length == 1) {
            file = files[0];
        } else {
            file = null;
        }
    }

    public FTPFileRecord(FileHelper ftpClient, String path, String name) throws IOException {
        this(ftpClient, ftpClient.processingPath(path, name));
    }

    @Override
    public boolean exists() throws IOException {
        return file != null;
    }

    @Override
    public long getSize() {
        if (file == null) {
            return 0L;
        }
        return file.getSize();
    }

    @Override
    public OutputStream getOutputStream(long skipSize) throws Exception {
        return ftpClient.getOutputStream(filePath, fileName, skipSize);
    }

    @Override
    public InputStream getInputStream(long skipSize) throws Exception {
        return ftpClient.getInputStream(filePath, fileName, skipSize);
    }

    @Override
    public boolean mkParentDir() throws IOException {
        // ftp server不支持递归创建目录,只能一级一级创建
        ftpClient.mkdir(filePath);
        return true;
    }

    @Override
    public boolean delete() throws IOException {
        if (exists()) {
            ftpClient.rm(fileFullPath);
        }
        return true;
    }

    @Override
    public void refresh() throws Exception {
        ftpClient.fresh();
    }
}
