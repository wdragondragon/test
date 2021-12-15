package org.example.javabase.sftp.jcsh;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.04 17:25
 * @Description:
 */
@Slf4j
public class SftpTest {
    public static void main(String[] args) throws Exception {
        long time = System.currentTimeMillis();
        SFTP sftp = SFTP.builder()
                .host("10.194.188.77")
                .port(22)
                .username("root")
                .password("123456aB")
                .build();

        if(!sftp.connect())return;
//        sftp.uploadFile("/ope/java/test", new File("D:/test.txt"));
//        byte[] bytes = sftp.downloadByte("/opt/java/fileTest", "test.txt");
//        System.out.println(System.currentTimeMillis()-time);
//        System.out.println(new String(bytes));

        String str = "test->input sftp22";
        byte[] bytesTest = str.getBytes();
        sftp.uploadBytes("/bmdata/software/fileTest","sftpTest.txt",bytesTest);
        byte[] bytesTest2 = sftp.downloadByte("/bmdata/software/fileTest", "sftpTest.txt");
        System.out.println(new String(bytesTest2));

        sftp.close();


    }
}
