package org.example.sftp.ssh2;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.04 23:54
 * @Description:
 */
@Slf4j
public class SCP {

    private String host;

    private int port;

    private String username;

    private String password;

    private Connection connection;

    @Builder
    public SCP(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public boolean connect() {
        log.info("开始连接！");
        connection = new Connection(host, port);
        try {
            connection.connect();
            connection.authenticateWithPassword(username, password);
            log.info("远端连接成功！");
            return true;
        } catch (IOException e) {
            log.error("远端连接失败!");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 字节数组的方式上传文件
     *
     * @param bytes      需要上传的数据
     * @param remotePath 远程文件路径
     * @param remoteName 远程文件名
     * @return 上传结果
     */
    public boolean uploadBytes(byte[] bytes, String remotePath, String remoteName) {
        int time = 0;
        while (time < 3) {
            time++;
            try {
                SCPClient scpClient = connection.createSCPClient();
                scpClient.put(bytes, remoteName, remotePath);
                return true;
            } catch (IOException e) {
                Throwable cause = e.getCause();
                if (cause != null && cause.toString().contains("No such file or directory")) {
                    log.info("No such file or directory-->try mkdir try [{}] times", time);
                    exec("mkdir -p " + remotePath);
                } else {
                    e.printStackTrace();
                    log.error(e.getMessage());
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 下载文件以字节数组方式获取
     *
     * @param remoteFile 远程全路径文件名
     * @return 下载的数据
     * @throws IOException
     */
    public byte[] downloadBytes(String remoteFile) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SCPClient scpClient = connection.createSCPClient();
        scpClient.get(remoteFile, outputStream);
        return outputStream.toByteArray();
    }

    public void exec(String cmd) {
        try {
            String line;
            Session session = connection.openSession();
            log.info("执行远端命令：{}", cmd);
            session.execCommand(cmd);
            StringBuilder sb = new StringBuilder();
            /** 打印出结果 **/
            InputStream is = new StreamGobbler(session.getStdout());
            BufferedReader brs = new BufferedReader(new InputStreamReader(is));
            log.info("命令执行结果：\r\n");
            while ((line = brs.readLine()) != null) {
                log.info("{}", line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (connection != null) {
            connection.close();
            connection = null;
            log.info("远端连接关闭！");
        }
    }
}
