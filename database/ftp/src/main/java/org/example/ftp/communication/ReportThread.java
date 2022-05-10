package org.example.ftp.communication;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author JDragon
 * @Date 2022.05.06 上午 10:51
 * @Email 1061917196@qq.com
 * @Des:
 */
@Slf4j
public class ReportThread extends Thread{

    private static final long PRINT_TIME = 3 * 1000L;

    private final List<Reporter> reporters = new LinkedList<>();

    public void regReporter(Reporter reporter) {
        reporters.add(reporter);
    }

    @Override
    public void run() {
        log.info("传输信息上报启动");
        do {
            try {
                Thread.sleep(PRINT_TIME);
            } catch (InterruptedException e) {
                log.error("线程睡眠失败", e);
            }
            reporters.forEach(Reporter::reportAll);
        } while (!reporters.stream().allMatch(Reporter::isFinish));
    }


    public static void reg(String name, Communication communication) {
        Reporter.reg(name, communication);
    }
}
