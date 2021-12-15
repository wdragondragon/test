package org.example;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.18 15:36
 * @Description:
 */
@Slf4j
public class TestSh {
    public static void main(String[] args) throws IOException {
        String echo_sdf = exec("echo sdf");
    }

    /**
     * java 利用 process 执行本地命令
     *
     * @param
     * @return 返回执行命令結果集
     */
    public static String cmdarray(String cmd) {
        StringBuilder resultBuilder = new StringBuilder();
        String ip = "";
        String line = null;
        Process process = null;
        try {
            String[] cmdArray = new String[]{"bash", "-c", cmd};
            log.info("ip：{} 执行命令：{}", ip, cmdArray);
            process = Runtime.getRuntime().exec(cmdArray);
            InputStream inputStream = process.getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = bf.readLine()) != null) {
                System.out.println(line);
                resultBuilder.append(line).append("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultBuilder.toString();
    }

    /**
     *
     */
    public static String exec(String cmdarray) throws IOException{
        ProcessBuilder processBuilder = new ProcessBuilder("bash","-c",cmdarray);
        ProcessBuilder.Redirect redirect = processBuilder.redirectError();
        Process start = processBuilder.start();
        InputStream inputStream = start.getInputStream();
        return "";
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
            while (!file.exists()){
                System.out.println("不存在");
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            if(bufferedReader==null){
                fileInputStream = new FileInputStream(srcFilename);
                bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, charset));
            }

            //读取内容
            try {
                if ((singleLine = bufferedReader.readLine()) != null) {
                    System.out.println("日志内容:"+singleLine);
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
