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
public class FTPClientCloseable extends FTPClient implements AutoCloseable {

    public FTPClientCloseable(String hostname, int port, String username, String password) throws IOException {
        addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        connect(hostname, port);
        if (FTPReply.isPositiveCompletion(getReplyCode())) {
            if (login(username, password)) {
                //设置PassiveMode传输
                enterLocalPassiveMode();
                //设置以二进制流的方式传输
                setFileType(FTP.BINARY_FILE_TYPE);
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
}
