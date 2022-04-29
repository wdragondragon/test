package org.example.ftp;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author JDragon
 * @Date 2022.04.28 下午 3:31
 * @Email 1061917196@qq.com
 * @Des:
 */
public class FTPClientCreator {
    public static FTPClient initFTPClient(String hostname, int port, String username, String password) throws IOException {
        final FTPClient ftpClient = new FTPClient();
        ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        ftpClient.connect(hostname, port);
        if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            if (ftpClient.login(username, password)) {
                //设置PassiveMode传输
                ftpClient.enterLocalPassiveMode();
                //设置以二进制流的方式传输
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                return ftpClient;
            } else {
                throw new IOException("登录失败：" + ftpClient.getReplyString());
            }
        }
        disconnect(ftpClient);
        throw new IOException("登录失败：" + ftpClient.getReplyString());
    }

    /**
     * 断开与远程服务器的连接
     */
    public static void disconnect(FTPClient ftpClient) throws IOException {
        if (ftpClient.isConnected()) {
            ftpClient.disconnect();
        }
    }
}
