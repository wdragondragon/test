package org.example.ftp.helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.example.ftp.file.FileRecord;
import org.example.ftp.file.SFTPFileRecord;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Pattern;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.04 17:26
 * @Description:
 */
@Slf4j
public class SFTPClientCloseable implements FileHelper {

    private String host;

    private int port;

    private String username;

    private String password;

    private Session session;//会话

    private Channel channel;//连接通道

    private ChannelSftp sftp;// sftp操作类

    @Builder
    public SFTPClientCloseable(String host, int port, String username, String password) throws Exception {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.connect();
    }

    public boolean connect() throws Exception {
        log.info("开始连接！");
        JSch jSch = new JSch();

        session = jSch.getSession(username, host, port);
        session.setPassword(password);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no"); // 不验证 HostKey
        session.setConfig(config);

        try {
            session.connect();
        } catch (Exception e) {
            if (session.isConnected())
                session.disconnect();
            log.error("连接服务器失败,请检查主机[" + host + "],端口[" + port
                    + "],用户名[" + username + "],端口[" + port
                    + "]是否正确,以上信息正确的情况下请检查网络连接是否正常或者请求被防火墙拒绝.");
            return false;
        }
        channel = session.openChannel("sftp");
        try {
            channel.connect();
        } catch (Exception e) {
            if (channel.isConnected())
                channel.disconnect();
            log.error("连接服务器失败,请检查主机[" + host + "],端口[" + port
                    + "],用户名[" + username + "],密码是否正确,以上信息正确的情况下请检查网络连接是否正常或者请求被防火墙拒绝.");
            return false;
        }
        sftp = (ChannelSftp) channel;
        log.info("远端连接成功！");
        return true;
    }

    @Override
    public boolean isConnected() {
        if (null != sftp) {
            return sftp.isConnected();
        } else {
            return false;
        }
    }

    public void close() {
        if (null != sftp) {
            sftp.disconnect();
            sftp.exit();
            sftp = null;
            log.info("远端连接关闭！");
        }
        if (null != channel) {
            channel.disconnect();
            channel = null;
        }
        if (null != session) {
            session.disconnect();
            session = null;
        }
    }

    /**
     * 下载文件到目录
     *
     * @param remoteDirectory 下载目录 根据SFTP设置的根目录来进行传入
     * @param remoteFilename  下载的文件
     * @param localPath       存在本地的路径
     */
    public void downloadFile(String remoteDirectory, String remoteFilename, String localPath) {
        try {
            sftp.cd(remoteDirectory); //进入目录
            File file = new File(localPath);
            boolean bFile;
            bFile = file.exists();
            if (!bFile) {
                bFile = file.mkdirs();//创建目录
            }
            OutputStream out = new FileOutputStream(new File(localPath, remoteFilename));

            sftp.get(remoteFilename, out);

            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件到byte数组中
     *
     * @param remoteDirectory 下载目录 根据SFTP设置的根目录来进行传入
     * @param remoteFilename  下载的文件
     */
    public byte[] downloadByte(String remoteDirectory, String remoteFilename) {
        try {
            sftp.cd(remoteDirectory); //进入目录

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            sftp.get(remoteFilename, out);

            byte[] bytes = out.toByteArray();

            out.flush();
            out.close();

            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * linux上传文件
     */
    public void uploadFile(String directory, File file) {
        try {
            upload(directory, file.getName(), new FileInputStream(file));
        } catch (FileNotFoundException e) {
            log.info("文件未找到：[{}]", file.getAbsoluteFile());
        }
    }

    public void uploadBytes(String directory, String filename, byte[] bytes) {
        upload(directory, filename, new ByteArrayInputStream(bytes));
    }

    public void upload(String directory, String filename, InputStream stream) {
        try {
            if (!isExistDir(directory))
                sftp.mkdir(directory);
            sftp.cd(directory);
            log.info("cd {}", directory);
            try {
                sftp.put(stream, filename);
            } catch (Exception e) {
                log.error("上传失败", e);
            } finally {
                stream.close();
            }
        } catch (Exception e) {
            log.error("sftp处理过程出错", e);
        }
    }

    public boolean isExistDir(String path) throws IOException {
        try {
            SftpATTRS sftpATTRS = sftp.lstat(path);
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().equalsIgnoreCase("no such file")) {
                return false;
            }
            throw new IOException(e);
        }
    }

    public boolean isExistFile(String path) throws IOException {
        try {
            sftp.lstat(path);
            return true;
        } catch (Exception e) {
            if (e.getMessage().equalsIgnoreCase("no such file")) {
                return false;
            }
            throw new IOException(e);
        }
    }

    public long getSize(String path) throws IOException {
        try {
            if (isExistFile(path)) {
                SftpATTRS lstat = sftp.lstat(path);
                return lstat.getSize();
            } else {
                return 0L;
            }
        } catch (SftpException e) {
            if (e.getMessage().equalsIgnoreCase("no such file")) {
                return 0;
            } else {
                throw new IOException(e);
            }
        }
    }

    public InputStream getInputStream(String path, String name, long skip) throws IOException {
        try {
            if (isConnected() || reConnect()) {
                return sftp.get(processingPath(path, name), null, skip);
            }
            throw new IOException("连接不可用");
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public OutputStream getOutputStream(String path, String name, int mode, long skip) throws IOException {
        try {
            if (isConnected() || reConnect()) {
                return sftp.put(processingPath(path, name), null, mode, skip);
            }
            throw new IOException("连接不可用");
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public void mkdir(String path) throws IOException {
        try {
            if (!isExistDir(path)) {
                sftp.mkdir(path);
            }
        } catch (SftpException e) {
            throw new IOException(e);
        }
    }

    public void rm(String path) throws IOException {
        try {
            if (isExistDir(path)) {
                sftp.rmdir(path);
            } else if (isExistFile(path)) {
                sftp.rm(path);
            }
        } catch (SftpException e) {
            throw new IOException(e);
        }
    }

    @Override
    public Set<String> listFile(String dir, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Set<String> fileList = new HashSet<>();
        try {
            @SuppressWarnings("rawtypes")
            Vector allFiles = this.sftp.ls(dir);
            log.debug(String.format("ls: %s", JSON.toJSONString(allFiles,
                    SerializerFeature.UseSingleQuotes)));
            for (Object allFile : allFiles) {
                ChannelSftp.LsEntry le = (ChannelSftp.LsEntry) allFile;
                if (le.getAttrs().isDir()) {
                    continue;
                }
                String name = le.getFilename();
                if (pattern.matcher(name).find()) {
                    fileList.add(name);
                }
            }
        } catch (SftpException e) {
            String message = String
                    .format("获取path:[%s] 下文件列表时发生I/O异常,请确认与ftp服务器的连接正常,拥有目录ls权限, errorMessage:%s",
                            dir, e.getMessage());
            log.error(message);
        }
        return fileList;
    }


    public FileRecord initFile(String path, String name) {
        return new SFTPFileRecord(this, path, name);
    }
}
