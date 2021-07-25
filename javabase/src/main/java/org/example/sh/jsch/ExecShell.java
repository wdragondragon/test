package org.example.sh.jsch;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @Author JDragon
 * @Date 2021.06.01 下午 2:31
 * @Email 1061917196@qq.com
 * @Des:
 */
@Slf4j
public class ExecShell {

    public final static String DEFAULT_NEW_LINE = System.getProperty("line.separator");

    public final static String SUCCESS = "success";

    /**
     * 返回结果的重定向指令执行
     *
     * @param cmd
     * @return
     */
    public static String exec(String cmd, ThrowableFunction... functions) {
        log.info("执行命令：{}", cmd);
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", String.format("%s 2>&1", cmd));
        Process process;
        BufferedReader bf;
        try {
            process = processBuilder.start();
            InputStream inputStream = process.getInputStream();
            bf = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bf.readLine()) != null) {
                if (handleExec(line, functions)) break;
            }
            process.waitFor();
            close(process, bf);
            return SUCCESS;
        } catch (ExecShellException e) {
            return e.getMessage();
        } catch (Exception e) {
            log.error("", e);
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            return stringWriter.toString();
        }
    }


    public static String execRetResult(String cmd) {
        return execRetResult(cmd, DEFAULT_NEW_LINE);
    }

    public static String[] execRetResultArray(String cmd) {
        return execRetResult(cmd, DEFAULT_NEW_LINE).split(DEFAULT_NEW_LINE);
    }

    public static String[] execRetResultArray(String cmd, String newLine) {
        return execRetResult(cmd, newLine).split(newLine);
    }

    public static String execRetResult(String cmd, String newLine) {
        StringBuilder resultBuilder = new StringBuilder();
        String exec = exec(cmd, line -> {
            resultBuilder.append(line).append(newLine);
            return true;
        });
        if (exec.equals(SUCCESS)) {
            return resultBuilder.toString();
        } else {
            return exec;
        }
    }

    private static boolean handleExec(String result, ThrowableFunction... functions) throws ExecShellException {
        if (functions == null) {
            return false;
        }
        for (ThrowableFunction function : functions) {
            if (function != null && !function.apply(result)) {
                return true;
            }
        }
        return false;
    }

    private static void close(Process process, BufferedReader bufferedReader) {
        try {
            process.destroy();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
