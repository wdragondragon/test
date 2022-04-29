package org.example.ftp;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
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

    protected FTPClient ftpClient;

    protected FTPFile file;

    public FTPFileRecord(FTPClient ftpClient, String fileFullPath) throws IOException {
        super(fileFullPath);
        this.ftpClient = ftpClient;
        ftpClient.changeWorkingDirectory("/");
        FTPFile[] files = ftpClient.listFiles(fileFullPath);
        if (files.length == 1) {
            file = files[0];
        } else {
            file = null;
        }
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
    public OutputStream getOutputStream(long skipSize) throws IOException {
        if (skipSize > 0) {
            long size = getSize();
            ftpClient.setRestartOffset(size);
        }
        ftpClient.changeWorkingDirectory(filePath);
        return ftpClient.storeFileStream(fileName);
    }

    @Override
    public InputStream getInputStream(long skipSize) throws IOException {
        if (skipSize > 0) {
            long size = getSize();
            ftpClient.setRestartOffset(size);
        }
        ftpClient.changeWorkingDirectory(filePath);
        return ftpClient.retrieveFileStream(fileName);
    }

    @Override
    public boolean mkParentDir() throws IOException {
        // ftp server不支持递归创建目录,只能一级一级创建
        StringBuilder dirPath = new StringBuilder();
        String[] dirSplit = StringUtils.split(filePath, "/");
        for (String dirName : dirSplit) {
            dirPath.append("/").append(dirName);
            // 如果directoryPath目录不存在,则创建
            if (ftpClient.changeWorkingDirectory(dirPath.toString())) {
                continue;
            }
            int replayCode = ftpClient.mkd(dirPath.toString());
            if (replayCode != FTPReply.COMMAND_OK
                    && replayCode != FTPReply.PATHNAME_CREATED) {
                LOG.error("create path fail [{}]", dirPath.toString());
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean delete() throws IOException {
        if (exists()) {
            ftpClient.deleteFile(fileFullPath);
        }
        return true;
    }
}
