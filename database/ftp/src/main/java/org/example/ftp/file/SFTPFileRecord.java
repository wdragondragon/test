package org.example.ftp.file;

import com.jcraft.jsch.ChannelSftp;
import org.example.ftp.util.SFTPClientCloseable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Author JDragon
 * @Date 2022.04.28 下午 4:48
 * @Email 1061917196@qq.com
 * @Des:
 */
public class SFTPFileRecord extends FileRecord {
    private SFTPClientCloseable sftpClientCloseable;

    public SFTPFileRecord(SFTPClientCloseable sftpClientCloseable, String fileFullPath) {
        super(fileFullPath);
        this.sftpClientCloseable = sftpClientCloseable;
    }

    @Override
    public boolean exists() throws IOException {
        synchronized (this) {
            return getSize() > 0;
        }
    }

    @Override
    public long getSize() throws IOException {
        synchronized (this) {
            return sftpClientCloseable.getSize(fileFullPath);
        }
    }

    @Override
    public OutputStream getOutputStream(long skipSize) throws IOException {
        synchronized (this) {
            if (skipSize > 0) {
                return sftpClientCloseable.put(fileFullPath, ChannelSftp.OVERWRITE, skipSize);
            } else {
                return sftpClientCloseable.put(fileFullPath, ChannelSftp.OVERWRITE, 0L);
            }
        }
    }

    @Override
    public InputStream getInputStream(long skipSize) throws IOException {
        synchronized (this) {
            return sftpClientCloseable.get(fileFullPath, skipSize);
        }
    }

    @Override
    public boolean mkParentDir() throws IOException {
        synchronized (this) {
            sftpClientCloseable.mkdir(filePath);
            return true;
        }
    }

    @Override
    public boolean delete() throws IOException {
        synchronized (this) {
            if (exists()) {
                sftpClientCloseable.rm(fileFullPath);
            }
            return true;
        }
    }
}
