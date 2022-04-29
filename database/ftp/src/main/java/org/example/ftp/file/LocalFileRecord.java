package org.example.ftp.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Author JDragon
 * @Date 2022.04.28 下午 3:02
 * @Email 1061917196@qq.com
 * @Des:
 */
public class LocalFileRecord extends FileRecord {

    private static final Logger LOG = LoggerFactory.getLogger(LocalFileRecord.class);

    private final File file;

    public LocalFileRecord(String fileFullPath) {
        super(fileFullPath);
        file = new File(fileFullPath);
    }

    @Override
    public boolean exists() {
        return file.exists();
    }

    @Override
    public long getSize() {
        return file.length();
    }

    @Override
    public OutputStream getOutputStream(long skipSize) throws FileNotFoundException {
        return new FileOutputStream(file, skipSize > 0);
    }

    @Override
    public InputStream getInputStream(long skipSize) throws IOException {
        final FileInputStream fileInputStream = new FileInputStream(file);
        final long skip = fileInputStream.skip(skipSize);
        return fileInputStream;
    }

    @Override
    public boolean mkParentDir() {
        File parentFile = file.getParentFile();
        if (parentFile == null) {
            return true;
        }
        if (!file.getParentFile().exists()) {
            boolean mkdirs = file.getParentFile().mkdirs();
            LOG.info("创建文件夹[{}]结果[{}]", file.getParentFile().getPath(), mkdirs);
            return mkdirs;
        }
        return true;
    }

    @Override
    public boolean delete() {
        if (exists()) {
            return file.delete();
        }
        return true;
    }

}
