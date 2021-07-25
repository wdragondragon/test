package org.example.sh.jsch;

import com.jcraft.jsch.*;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.04 17:26
 * @Description:
 */
@Slf4j
public class Jsch implements Closeable {

    private String host;

    private int port;

    private String username;

    private String password;

    private Session session;//会话

    private Channel channel;//连接通道

    private ChannelSftp sftp;// sftp操作类

    private final static String DEFAULT_NEW_LINE = System.getProperty("line.separator");

    @Builder
    public Jsch(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public boolean connect() {
        log.info("开始连接！");
        try {
            JSch jSch = new JSch();
            session = jSch.getSession(username, host, port);
            session.setPassword(password);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no"); // 不验证 HostKey
            session.setConfig(config);
            session.connect();
        } catch (Exception e) {
            if (session.isConnected())
                session.disconnect();
            log.error("连接服务器失败,请检查主机[" + host + "],端口[" + port
                    + "],用户名[" + username + "],端口[" + port
                    + "]是否正确,以上信息正确的情况下请检查网络连接是否正常或者请求被防火墙拒绝.", e);
            return false;
        }
        try {
            channel = session.openChannel("sftp");
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

    public void mkdir(String directory) {
        try {
            if (!isExistDir(directory)) {
                log.info("创建目录：{}", directory);
                sftp.mkdir(directory);
            }
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }

    public boolean mkdirs(String directory) {
        try {
            if (!isExistDir(directory)) {
                log.info("创建目录：{}", directory);
                execShell("mkdir -p " + directory);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
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
    public boolean uploadFile(String directory, File file) {
        try {
            return upload(directory, file.getName(), new FileInputStream(file));
        } catch (FileNotFoundException e) {
            log.info("文件未找到：[{}]", file.getAbsoluteFile());
            return false;
        }
    }

    public boolean uploadBytes(String directory, String filename, byte[] bytes) {
        return upload(directory, filename, new ByteArrayInputStream(bytes));
    }

    public boolean upload(String filePath, InputStream stream) {
        return upload(filePath, stream, true);
    }

    public boolean upload(String filePath, InputStream stream, boolean closed) {
        File file = new File(filePath);
        return upload(file.getParent(), file.getName(), stream, closed);
    }

    public boolean upload(String directory, String filename, InputStream stream) {
        return upload(directory, filename, stream, true);
    }

    public boolean upload(String directory, String filename, InputStream stream, boolean closed) {
        try {
            this.mkdirs(directory);
            sftp.cd(directory);
            try {
                sftp.put(stream, filename);
                log.info("上传成功 {}/{}", directory, filename);
            } catch (Exception e) {
                log.info("上传失败 {}/{}", directory, filename);
            } finally {
                if (closed)
                    stream.close();
            }
            return true;
        } catch (Exception e) {
            log.error("sftp处理过程出错", e);
            return false;
        }
    }


    public boolean isExistDir(String path) {
        boolean isExist = false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(path);
            isExist = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")) {
                isExist = false;
            }
        }
        return isExist;
    }

    public List<String> rm(String path) {
        List<String> fileList = new ArrayList<>();
        try {
            SftpATTRS lstat = sftp.lstat(path);
            if (lstat.isDir()) {
                List<String> rmdir = rmdir(path);
                fileList.addAll(rmdir);
            } else {
                sftp.rm(path);
                fileList.add(path);
            }
            return fileList;
        } catch (SftpException e) {
            e.printStackTrace();
        }
        return fileList;
    }

    private List<String> rmdir(String path) throws SftpException {
        List<String> fileList = new ArrayList<>();
        Vector dirs = sftp.ls(path);
        for (Object o : dirs) {
            ChannelSftp.LsEntry dir = (ChannelSftp.LsEntry) o;
            if (dir.getFilename().equals(".") || dir.getFilename().equals(".."))
                continue;
            String newPath = path + "/" + dir.getFilename();
            if (dir.getAttrs().isDir()) {
                List<String> rmdir = rmdir(newPath);
                fileList.addAll(rmdir);
            } else {
                sftp.rm(newPath);
                fileList.add(newPath);
            }
        }
        sftp.rmdir(path);
        fileList.add(path);
        return fileList;
    }

    public String execShell(String command) throws JSchException, ExecShellException, IOException {
        return execShell(command, DEFAULT_NEW_LINE, null);
    }

    public String execShell(String command, ThrowableFunction function) throws JSchException, ExecShellException, IOException {
        return execShell(command, DEFAULT_NEW_LINE, function);
    }

    public String execShell(String command, String newLine) throws Exception {
        return execShell(command, newLine, null);
    }

    public String execShell(String command, String newLine, ThrowableFunction function) throws IOException, JSchException, ExecShellException {
        ChannelExec exec = (ChannelExec) session.openChannel("exec");
        InputStream in = exec.getInputStream();
        exec.setCommand(command);
        exec.connect();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringBuilder buffer = new StringBuilder();
        String temp;
        while ((temp = bufferedReader.readLine()) != null) {
            buffer.append(temp).append(newLine);
            if (function != null && !function.apply(temp)) break;
        }
        in.close();
        exec.disconnect();
        return buffer.toString();
    }

    public static void main(String[] args) {
        Jsch jsch = Jsch.builder()
                .host("10.194.188.79")
                .port(22)
                .username("root")
                .password("m")
                .build();
        jsch.connect();
        try {
            String s = jsch.execShell("cd /bmdata/software;./dxengine.sh status", e -> {
                if (e.contains("NOT running")) {
                    throw new ExecShellException("引擎未启动");
                } else {
                    return true;
                }
            });
            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        jsch.close();
    }
}
