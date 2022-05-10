package org.example.ftp.helper;

import org.example.ftp.file.FileRecord;
import org.example.ftp.file.LocalFileRecord;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @Author JDragon
 * @Date 2022.05.09 下午 4:06
 * @Email 1061917196@qq.com
 * @Des:
 */
public class LocalFileHelper implements FileHelper {
    @Override
    public Set<String> listFile(String dir, String regex) {
        final File directory = new File(dir);
        if (!directory.isDirectory()) {
            throw new RuntimeException(dir + "不是文件夹");
        }
        final File[] files = directory.listFiles();
        if (files == null) {
            return new HashSet<>();
        }

        Set<String> fileList = new HashSet<>();
        Pattern pattern = Pattern.compile(regex);
        for (File file : files) {
            if (file.isDirectory()) {
                continue;
            }
            String name = file.getName();
            if (pattern.matcher(name).find()) {
                fileList.add(name);
            }
        }
        return fileList;
    }

    @Override
    public void mkdir(String filePath) throws IOException {
        final File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @Override
    public void rm(String path) throws IOException {
        final File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            rmPath(file);
        } else {
            file.delete();
        }
    }

    public void rmPath(File path) {
        final File[] files = path.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    rmPath(path);
                } else {
                    file.delete();
                }
            }
        }
        path.delete();
    }

    @Override
    public boolean connect() throws Exception {
        return true;
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public FileRecord initFile(String path, String name) throws IOException {
        return new LocalFileRecord(processingPath(path, name));
    }

    @Override
    public void close() throws Exception {

    }
}
