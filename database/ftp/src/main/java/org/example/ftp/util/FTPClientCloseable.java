package org.example.ftp.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.example.ftp.file.FTPFileRecord;
import org.example.ftp.file.FileRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @Author JDragon
 * @Date 2022.04.28 下午 3:31
 * @Email 1061917196@qq.com
 * @Des:
 */
@Slf4j
public class FTPClientCloseable extends FTPClient implements FileHelper {

    private String hostname;

    private int port;

    private String username;

    private String password;


    public FTPClientCloseable(String hostname, int port, String username, String password) throws Exception {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.connect();
    }


    @Override
    public boolean connect() throws Exception {
        addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        connect(hostname, port);
        if (FTPReply.isPositiveCompletion(getReplyCode())) {
            if (login(username, password)) {
                //设置PassiveMode传输
                enterLocalPassiveMode();
                //设置以二进制流的方式传输
                setFileType(FTP.BINARY_FILE_TYPE);
                return true;
            } else {
                throw new IOException("登录失败：" + getReplyString());
            }
        }
        disconnect();
        throw new IOException("登录失败：" + getReplyString());
    }

    @Override
    public void close() throws IOException {
        if (this.isConnected()) {
            this.disconnect();
        }
    }

    @Override
    public Set<String> listFile(String dir, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Set<String> fileList = new HashSet<>();
        try {
            final FTPFile[] ftpFiles = super.listFiles(dir);
            for (FTPFile ftpFile : ftpFiles) {
                if (ftpFile.isFile()) {
                    String name = ftpFile.getName();
                    if (pattern.matcher(name).find()) {
                        fileList.add(name);
                    }
                }
            }
        } catch (IOException e) {
            String message = String
                    .format("获取path:[%s] 下文件列表时发生I/O异常,请确认与ftp服务器的连接正常,拥有目录ls权限, errorMessage:%s",
                            dir, e.getMessage());
            log.error(message);
        }
        return fileList;
    }

    @Override
    public void mkdir(String filePath) throws IOException {
        // ftp server不支持递归创建目录,只能一级一级创建
        StringBuilder dirPath = new StringBuilder();
        String[] dirSplit = StringUtils.split(filePath, "/");
        for (String dirName : dirSplit) {
            dirPath.append("/").append(dirName);
            // 如果directoryPath目录不存在,则创建
            if (this.changeWorkingDirectory(dirPath.toString())) {
                continue;
            }
            int replayCode = this.mkd(dirPath.toString());
            if (replayCode != FTPReply.COMMAND_OK
                    && replayCode != FTPReply.PATHNAME_CREATED) {
                log.error("create path fail [{}]", dirPath.toString());
            }
        }
    }

    @Override
    public void rm(String path) throws IOException {
        this.deleteFile(path);
    }

    public InputStream getInputStream(String path, String name, long skip) throws Exception {
        if (isConnected() || reConnect()) {
            this.setFileType(FTPClient.BINARY_FILE_TYPE);
            this.enterLocalPassiveMode();
            if (skip > 0) {
                this.setRestartOffset(skip);
            }
            this.changeWorkingDirectory(path);
            return this.retrieveFileStream(name);
        }
        throw new IOException("连接不可用");
    }

    public OutputStream getOutputStream(String path, String name, long skip) throws Exception {
        if (isConnected() || isAvailable() || reConnect()) {
            this.setFileType(FTPClient.BINARY_FILE_TYPE);
            this.enterLocalPassiveMode();
            if (skip > 0) {
                this.setRestartOffset(skip);
            }
            this.changeWorkingDirectory(path);
            OutputStream outputStream = this.storeFileStream(name);
            if (outputStream == null) {
                String[] replyStrings = this.getReplyStrings();
                System.out.println(Arrays.toString(replyStrings));
            }
            return outputStream;
        }
        throw new IOException("连接不可用");
    }

    public FileRecord initFile(String path, String name) throws IOException {
        return new FTPFileRecord(this, path, name);
    }

    @Override
    public void fresh() throws Exception {
        this.completePendingCommand();
    }
}
