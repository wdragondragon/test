package org.example.javabase.sh;

import lombok.extern.slf4j.Slf4j;
import org.example.javabase.sh.jsch.ExecShell;

import java.io.*;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.18 15:36
 * @Description:
 */
@Slf4j
public class Test {
    public static void main(String[] args) throws IOException {
        while (true) {
            try {
                new Thread(() -> ExecShell.exec("tail -200f /bmdata/application/datax/logs/log_info.log")).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void test(String srcFilename) throws IOException {
//        String srcFilename = "/var/haofangproject/haofangServer/log/haofangerp.log";
//        String srcFilename = "C:\\Users\\10619\\Desktop\\项目\\read2.txt";
        String charset = "utf8";
        File file = new File(srcFilename);
        System.out.println(file.getAbsolutePath());
        InputStream fileInputStream;
        BufferedReader bufferedReader = null;
        String singleLine;
        long fileSize = file.length();
        while (true) {
            //自
            while (!file.exists()) {
                System.out.println("不存在");
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            if (bufferedReader == null) {
                fileInputStream = new FileInputStream(srcFilename);
                bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, charset));
            }

            //读取内容
            try {
                if ((singleLine = bufferedReader.readLine()) != null) {
                    System.out.println("日志内容:" + singleLine);
                    fileSize = Math.max(file.length(), fileSize);
                    continue;
                }
            } catch (IOException e) { // 文件被清空的时候FileInputStream会被close
                bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(srcFilename), charset));
                fileSize = file.length();
                bufferedReader.skip(fileSize);
            }
//            System.out.println("无新增");
            //判断删减
            try {
                if (file.length() < fileSize) { // 文件被清空了
                    bufferedReader.close();
                    bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(srcFilename), charset));
                    fileSize = file.length();
                    bufferedReader.skip(fileSize);
                }
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                bufferedReader.close();
                break;
            }
        }
    }
}
