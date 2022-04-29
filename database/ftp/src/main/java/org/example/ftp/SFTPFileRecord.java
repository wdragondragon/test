package org.example.ftp;

import com.jcraft.jsch.ChannelSftp;

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
    private final SFTP sftp;

    public SFTPFileRecord(SFTP sftp, String fileFullPath) {
        super(fileFullPath);
        this.sftp = sftp;
    }

    @Override
    public boolean exists() throws IOException {
        return getSize() > 0;
    }

    @Override
    public long getSize() throws IOException {
        return sftp.getSize(fileFullPath);
    }

    @Override
    public OutputStream getOutputStream(long skipSize) throws IOException {
        if (skipSize > 0) {
            return sftp.put(fileFullPath, ChannelSftp.RESUME, skipSize);
        } else {
            return sftp.put(fileFullPath, ChannelSftp.OVERWRITE, 0L);
        }
    }

    @Override
    public InputStream getInputStream(long skipSize) throws IOException {
        return sftp.get(fileFullPath, skipSize);
    }

    @Override
    public boolean mkParentDir() throws IOException {
        sftp.mkdir(filePath);
        return true;
    }

    @Override
    public boolean delete() throws IOException {
        if (exists()) {
            sftp.rm(fileFullPath);
        }
        return true;
    }
}
