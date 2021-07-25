package org.example.sftp.ftp;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.04 14:59
 * @Description:
 */
@Slf4j
public class FTPTest {
    public static void main(String[] args) {
        FTP ftp = FTP.builder()
                .host("172.29.215.219")
                .port(21)
                .username("root")
                .password("951753")
                .build();
        try {
            if (ftp.open()) {
                log.info("开始下载");
                ftp.uploadFile("D:/test.txt", "/opt/java/test/", "test.txt");
                // 远程路径为相对路径
                byte[] bytes = ftp.downloadBytes("/opt/java/test/test.txt");
                System.out.println(new String(bytes));

                ftp.uploadBytes(bytes, "/opt/java/test/", "test.txt");
                ftp.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}