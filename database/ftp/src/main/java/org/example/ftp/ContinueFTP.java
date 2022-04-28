package org.example.ftp;

/**
 * @Author JDragon
 * @Date 2022.04.27 下午 2:12
 * @Email 1061917196@qq.com
 * @Des:
 */

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class ContinueFTP {
    private static final Logger LOG = LoggerFactory.getLogger(ContinueFTP.class);
    private final FTPClient ftpClient = new FTPClient();

    public ContinueFTP() throws IOException {
        //设置将过程中使用到的命令输出到控制台
        this.ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
    }

    /**
     * 连接到FTP服务器
     *
     * @param hostname 主机名
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @return 是否连接成功
     * @throws IOException
     */
    public boolean connect(String hostname, int port, String username, String password) throws IOException {
        ftpClient.connect(hostname, port);
        if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            if (ftpClient.login(username, password)) {
                //设置PassiveMode传输
                ftpClient.enterLocalPassiveMode();
                //设置以二进制流的方式传输
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                return true;
            }
        }
        disconnect();
        return false;
    }

    /**
     * 从FTP服务器上下载文件
     *
     * @param remote 远程文件路径
     * @param local  本地文件
     * @return 是否成功
     */
    public boolean download(String remote, String local) throws IOException {
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        File f = new File(local);
        FTPFile[] files = ftpClient.listFiles(remote);
        if (files.length != 1) {
            LOG.info("远程文件不唯一");
            return false;
        }
        long lRemoteSize = files[0].getSize();
        OutputStream out;
        if (f.exists()) {
            out = new FileOutputStream(f, true);
            LOG.info("source文件大小为:" + f.length());
            if (f.length() >= lRemoteSize) {
                LOG.info("source文件大小大于远程文件大小，下载中止");
                return false;
            }
            ftpClient.setRestartOffset(f.length());
        } else {
            out = new FileOutputStream(f);
        }
        InputStream inputStream = ftpClient.retrieveFileStream(remote);
        byte[] bytes = new byte[1024];
        long count = f.length();
        int readCount;
        while ((readCount = inputStream.read(bytes)) != -1) {
            out.write(bytes, 0, readCount);
            count += readCount;
            LOG.info("down " + count);
        }
        LOG.info("down finish");
        out.close();
        inputStream.close();
        return true;
    }

    /**
     * 上传文件到FTP服务器，支持断点续传
     *
     * @param local  本地文件
     * @param remote 远程文件路径，使用/home/directory1/subdirectory/file.ext 按照Linux上的路径指定方式，支持多级目录嵌套，支持递归创建不存在的目录结构
     * @return 上传结果
     * @throws IOException
     */
    public UploadStatus upload(String local, String remote) throws IOException {
        UploadStatus result;
        //对远程目录的处理
        String fileName = remote;
        if (remote.contains("/")) {
            String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
            fileName = remote.substring(remote.lastIndexOf("/") + 1);
            this.mkDirSingleHierarchy(ftpClient, directory);
            ftpClient.changeWorkingDirectory(directory);
        }

        long skipSize = 0L;
        //检查远程是否存在文件
        FTPFile[] files = ftpClient.listFiles(fileName);
        InputStream inputStream = new FileInputStream(local);
        if (files.length == 1) {
            skipSize = files[0].getSize();
            File f = new File(local);
            long localSize = f.length();
            if (skipSize == localSize) {
                return UploadStatus.File_Exits;
            } else if (skipSize > localSize) {
                return UploadStatus.Remote_Bigger_Local;
            }
            //尝试移动文件内读取指针,实现断点续传
            if (inputStream.skip(skipSize) == skipSize) {
                ftpClient.setRestartOffset(skipSize);
            }
        }
        if (skipSize == 0L && copy(inputStream, ftpClient.storeFileStream(remote), skipSize)) {
            result = UploadStatus.Upload_New_File_Success;
        } else if (skipSize == 0L) {
            result = UploadStatus.Upload_New_File_Failed;
        } else if (copy(inputStream, ftpClient.storeFileStream(remote), skipSize)) {
            result = UploadStatus.Upload_From_Break_Success;
        } else if (ftpClient.deleteFile(fileName)) {
            //如果断点续传没有成功，则删除服务器上文件，重新上传
            if (copy(new FileInputStream(local), ftpClient.storeFileStream(remote), 0L)) {
                result = UploadStatus.Upload_New_File_Success;
            } else {
                result = UploadStatus.Upload_New_File_Failed;
            }
        } else {
            result = UploadStatus.Delete_Remote_Faild;
        }
        return result;
    }

    public UploadStatus copy(String source, FTPClient sourceFtp, String target, FTPClient targetFtp) throws IOException {
        UploadStatus result;
        //对远程目录的处理
        String fileName = target;
        if (target.contains("/")) {
            String directory = target.substring(0, target.lastIndexOf("/") + 1);
            fileName = target.substring(target.lastIndexOf("/") + 1);
            this.mkDirSingleHierarchy(targetFtp, directory);
            targetFtp.changeWorkingDirectory(directory);
        }

        long skipSize = 0L;
        //检查远程是否存在文件
        FTPFile[] sourceFiles = sourceFtp.listFiles(source);
        FTPFile[] targetFiles = targetFtp.listFiles(fileName);
        if (sourceFiles.length == 1 && targetFiles.length == 1) {
            long sourceSize = sourceFiles[0].getSize();
            skipSize = targetFiles[0].getSize();
            if (skipSize == sourceSize) {
                return UploadStatus.File_Exits;
            } else if (skipSize > sourceSize) {
                return UploadStatus.Remote_Bigger_Local;
            }
            //尝试移动文件内读取指针,实现断点续传
            sourceFtp.setRestartOffset(skipSize);
            targetFtp.setRestartOffset(skipSize);
        }

        if (skipSize == 0L && copy(sourceFtp.retrieveFileStream(source), targetFtp.storeFileStream(target), skipSize)) {
            result = UploadStatus.Upload_New_File_Success;
        } else if (skipSize == 0L) {
            result = UploadStatus.Upload_New_File_Failed;
        } else if (copy(sourceFtp.retrieveFileStream(source), targetFtp.storeFileStream(target), skipSize)) {
            result = UploadStatus.Upload_From_Break_Success;
        } else if (targetFtp.deleteFile(fileName)) {
            //如果断点续传没有成功，则删除服务器上文件，重新上传
            if (copy(sourceFtp.retrieveFileStream(source), targetFtp.storeFileStream(target), 0L)) {
                result = UploadStatus.Upload_New_File_Success;
            } else {
                result = UploadStatus.Upload_New_File_Failed;
            }
        } else {
            result = UploadStatus.Delete_Remote_Faild;
        }
        return result;
    }

    public boolean copy(InputStream inputStream, OutputStream outputStream, long skipSize) {
        try {
            byte[] bytes = new byte[10240];
            long count = skipSize;
            int readCount;
            while ((readCount = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, readCount);
                count += readCount;
                LOG.info("进度 {}", count);
            }
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean mkDirSingleHierarchy(FTPClient ftpClient, String directoryPath) throws IOException {
        // ftp server不支持递归创建目录,只能一级一级创建
        StringBuilder dirPath = new StringBuilder();
        String[] dirSplit = StringUtils.split(directoryPath, "/");
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


    /**
     * 断开与远程服务器的连接
     */
    public void disconnect() throws IOException {
        if (ftpClient.isConnected()) {
            ftpClient.disconnect();
        }
    }

    public static void main(String[] args) throws IOException {
        ContinueFTP sourceFtp = new ContinueFTP();
        ContinueFTP targetFtp = new ContinueFTP();
        try {
            sourceFtp.connect("10.194.188.77", 21, "user_test", "EhnkvrX1V20=");
            targetFtp.connect("10.194.188.77", 21, "user_test", "EhnkvrX1V20=");
//            sourceFtp.download("/data/ftp/user_test/core-site.xml", "C:\\Users\\10619\\Desktop\\core-site6.xml");
//            LOG.info("上传结果：[{}]", sourceFtp.upload("C:\\Users\\10619\\Desktop\\引擎升级包\\汇聚黄埔升级.rar", "update.rar"));
            LOG.info("上传结果：[{}]", sourceFtp.copy("update.rar", sourceFtp.ftpClient, "update2.rar", targetFtp.ftpClient));
            sourceFtp.disconnect();
        } catch (IOException e) {
            LOG.error("连接FTP出错：{}" + e.getMessage(), e);
        }
    }
}