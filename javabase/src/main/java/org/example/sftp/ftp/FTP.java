package org.example.sftp.ftp;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.poi.util.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.05 15:07
 * @Description:
 */
@Slf4j
public class FTP {
    private FTPClient ftpClient = null;
    private final String host;
    private final int port;
    private final String username;
    private final String password;

    @Builder
    public FTP(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }
    /**
     * 链接到服务器
     *
     * @return
     * @throws Exception
     */
    public boolean open() {
        if (ftpClient != null && ftpClient.isConnected()) {
            return true;
        }
        try {
            ftpClient = new FTPClient();
            // 连接
            ftpClient.connect(this.host, this.port);
            ftpClient.login(this.username, this.password);
            // 检测连接是否成功
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                this.close();
                System.exit(1);
            }
            // 设置上传模式binally or ascii
            ftpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
            log.info("连接成功");
            return true;
        } catch (Exception ex) {
            // 关闭
            this.close();
            ex.printStackTrace();
            return false;
        }

    }

    private boolean cd(String dir) throws IOException {
        return ftpClient.changeWorkingDirectory(dir);
    }

    /**
     * 获取目录下所有的文件名称
     *
     * @param filePath
     * @return
     * @throws IOException
     */

    private FTPFile[] getFileList(String filePath) throws IOException {
        FTPFile[] list = ftpClient.listFiles();
        return list;
    }

    /**
     * 循环将设置工作目录
     */
    public boolean changeDir(String ftpPath) {
        if (!ftpClient.isConnected()) {
            return false;
        }
        try {
            ftpPath = fileSeparateReplace(ftpPath);
            if (ftpPath.indexOf('/') == -1) {
                // 只有一层目录
                ftpClient.changeWorkingDirectory(new String(ftpPath.getBytes(), StandardCharsets.ISO_8859_1));
            } else {
                // 多层目录循环创建
                String[] paths = ftpPath.split("/");
                for (int i = 0; i < paths.length; i++) {
                    ftpClient.changeWorkingDirectory(new String(paths[i].getBytes(), StandardCharsets.ISO_8859_1));
                }
            }
            log.info("进入目录[{}]", ftpPath);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 循环创建目录，并且创建完目录后，设置工作目录为当前创建的目录下
     */
    public boolean mkDir(String ftpPath) {
        if (!ftpClient.isConnected()) {
            return false;
        }
        try {
            ftpPath = fileSeparateReplace(ftpPath);
            if (ftpPath.indexOf('/') == -1) {
                // 只有一层目录
                ftpClient.makeDirectory(new String(ftpPath.getBytes(), StandardCharsets.ISO_8859_1));
                ftpClient.changeWorkingDirectory(new String(ftpPath.getBytes(), StandardCharsets.ISO_8859_1));
            } else {
                // 多层目录循环创建
                String[] paths = ftpPath.split("/");
                for (int i = 0; i < paths.length; i++) {
                    ftpClient.makeDirectory(new String(paths[i].getBytes(), StandardCharsets.ISO_8859_1));
                    ftpClient.changeWorkingDirectory(new String(paths[i].getBytes(), StandardCharsets.ISO_8859_1));
                }
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 上传文件到FTP服务器
     *
     * @param localDirectoryAndFileName 本地文件目录和文件名
     * @param ftpFileName               上传后的文件名
     * @param ftpDirectory              FTP目录如:/path1/pathb2/,如果目录不存在回自动创建目录
     * @throws Exception
     */
    public boolean uploadFile(String localDirectoryAndFileName, String ftpDirectory, String ftpFileName) {
        File srcFile = new File(localDirectoryAndFileName);
        FileInputStream fis;
        try {
            fis = new FileInputStream(srcFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        // 上传
        boolean flag = upload(fis, ftpFileName, ftpDirectory);
        log.info("上传结果[{}]", ftpClient.getReplyString());
        IOUtils.closeQuietly(fis);
        return flag;
    }
    public boolean uploadBytes(byte[] bytes, String ftpFileName, String ftpDirectory) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        // 上传
        boolean flag = upload(inputStream, ftpFileName, ftpDirectory);
        log.info("上传结果[{}]", ftpClient.getReplyString());
        IOUtils.closeQuietly(inputStream);
        return flag;
    }

    public boolean upload(InputStream inputStream, String ftpDirectory , String ftpFileName) {
        if (ftpClient == null)
            return false;
        if (!ftpClient.isConnected())
            return false;
        // 创建目录
        if (this.mkDir(ftpDirectory)) {
            log.info("创建目录[{}]", ftpDirectory);
        }

        try {
            ftpClient.setBufferSize(1024);
            ftpClient.setControlEncoding("UTF-8");
            // 设置文件类型（二进制）
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 上传
            return ftpClient.storeFile(ftpDirectory + ftpFileName, inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从FTP服务器上下载文件并返回下载文件长度
     *
     * @param ftpDirectoryAndFileName
     * @param localDirectoryAndFileName
     * @return
     * @throws Exception
     */
    public void downloadFile(String ftpDirectoryAndFileName, String localDirectoryAndFileName) {
        try {
            ftpDirectoryAndFileName = fileSeparateReplace(ftpDirectoryAndFileName);
            FileOutputStream outputStream = new FileOutputStream(localDirectoryAndFileName);
            download(ftpDirectoryAndFileName, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] downloadBytes(String ftpDirectoryAndFileName) {
        try {
            ftpDirectoryAndFileName = fileSeparateReplace(ftpDirectoryAndFileName);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            download(ftpDirectoryAndFileName, outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void download(String ftpDirectoryAndFileName, OutputStream outputStream) throws Exception {
        if (!ftpClient.isConnected()) {
            throw new Exception("ftp未连接");
        }
        ftpClient.enterLocalPassiveMode();
        log.info("下载文件[{}]", ftpDirectoryAndFileName);
        ftpClient.retrieveFile(ftpDirectoryAndFileName, outputStream); // download
        log.info(ftpClient.getReplyString()); // check result
    }

    /**
     * 返回FTP目录下的文件列表
     *
     * @param ftpDirectory
     * @return
     */
    public List getFileNameList(String ftpDirectory) {
        List list = new ArrayList();
        if (!open())
            return list;
        try {
            DataInputStream dis = new DataInputStream(ftpClient.retrieveFileStream(ftpDirectory));
            String filename = "";
            while ((filename = dis.readLine()) != null) {
                list.add(filename);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 删除FTP上的文件
     *
     * @param ftpDirAndFileName
     */
    public boolean deleteFile(String ftpDirAndFileName) {
        return ftpClient.isConnected();
    }

    /**
     * 删除FTP目录
     *
     * @param ftpDirectory
     */
    public boolean deleteDirectory(String ftpDirectory) {
        return ftpClient.isConnected();
    }

    public String fileSeparateReplace(String filePath) {
        // 将路径中的斜杠统一
        char[] chars = filePath.toCharArray();
        StringBuffer sbStr = new StringBuffer(256);
        for (int i = 0; i < chars.length; i++) {
            if ('\\' == chars[i]) {
                sbStr.append('/');
            } else {
                sbStr.append(chars[i]);
            }
        }
        filePath = sbStr.toString();
        return filePath;
    }

    /**
     * 关闭链接
     */
    public void close() {
        try {
            if (ftpClient != null && ftpClient.isConnected())
                ftpClient.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
