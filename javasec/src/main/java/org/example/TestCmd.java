package org.example;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.12.25 10:08
 * @Description:
 */
@Slf4j
public class TestCmd {
    public static void main(String[] args) {
        String cmdarray = cmdarray("ps -ef |grep " + args[0] + "-|grep -v 'grep'|awk '{print $2}'");
        String[] split = cmdarray.trim().split("\\s+");
        for (String s : split) {
            String pid = s.trim();
            if (!pid.isEmpty()) {
                System.out.println("指令结果:" + pid);
            }
        }
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
                resultBuilder.append(line + "\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultBuilder.toString();
    }
}
