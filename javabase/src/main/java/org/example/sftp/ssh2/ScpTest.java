package org.example.sftp.ssh2;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import com.sun.corba.se.spi.orb.StringPair;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.04 12:20
 * @Description:
 */
@Slf4j
public class ScpTest {
    public static void main(String[] args) throws IOException {
        long time = System.currentTimeMillis();
        SCP scp = SCP.builder()
                .host("39.96.83.89")
                .port(22)
                .username("root")
                .password("zhjl951753")
                .build();
        if(!scp.connect())return;
        String str = "test->input scp";
        scp.uploadBytes(str.getBytes(),"/opt/java/fileTest/","scpTest.txt");
        byte[] bytes = scp.downloadBytes("/opt/java/fileTest/scpTest.txt");
        scp.close();
        System.out.println(System.currentTimeMillis()-time);
        System.out.println(new String(bytes));
    }
}
