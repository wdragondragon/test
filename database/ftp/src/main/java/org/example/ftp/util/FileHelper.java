package org.example.ftp.util;

import org.example.ftp.file.FileRecord;

import java.io.IOException;
import java.util.Set;

/**
 * @Author JDragon
 * @Date 2022.05.05 上午 11:39
 * @Email 1061917196@qq.com
 * @Des:
 */
public interface FileHelper extends AutoCloseable {
    /**
     * 格式路径
     */
    default String processingPath(String path, String fileName) {
        boolean end = path.endsWith("/");
        boolean start = fileName.startsWith("/");
        if (start && end) {
            return path + fileName.substring(1);
        } else if (start || end) {
            return path + fileName;
        } else {
            return path + "/" + fileName;
        }
    }

    default String processingPath(String path, String... fileNames) {
        for (String fileName : fileNames) {
            path = processingPath(path, fileName);
        }
        return path;
    }

    Set<String> listFile(String dir, String regex);

    void mkdir(String filePath) throws IOException;

    void rm(String path) throws IOException;

    boolean connect() throws Exception;

    boolean isConnected();

    default boolean reConnect() throws Exception {
        close();
        return connect();
    }

    FileRecord initFile(String path, String name) throws IOException;

    default void fresh() throws Exception {}
}
