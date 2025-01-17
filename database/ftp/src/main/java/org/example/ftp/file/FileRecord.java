package org.example.ftp.file;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Author JDragon
 * @Date 2022.04.28 下午 2:55
 * @Email 1061917196@qq.com
 * @Des:
 */
public abstract class FileRecord {

    protected String fileName;

    protected final String filePath;

    protected String fileFullPath;

    public FileRecord(String fileFullPath) {
        fileFullPath = fileFullPath.replaceAll("\\\\", "/");
        this.fileFullPath = fileFullPath;
        this.fileName = fileFullPath.substring(fileFullPath.lastIndexOf("/") + 1);
        this.filePath = fileFullPath.substring(0, fileFullPath.lastIndexOf("/") + 1);
    }

    public abstract boolean exists() throws IOException;

    public abstract long getSize() throws IOException;

    public abstract OutputStream getOutputStream(long skipSize) throws Exception;

    public abstract InputStream getInputStream(long skipSize) throws Exception;

    public abstract boolean mkParentDir() throws IOException;

    public abstract boolean delete() throws IOException;

    public String getFileName() {
        return fileName;
    }

    public String getFileFullPath() {
        return fileFullPath;
    }

    public String getFilePath() {
        return filePath;
    }

    public String md5() throws Exception {
        return DigestUtils.md5Hex(getInputStream(0L));
    }

    public void refresh() throws Exception {};
}
