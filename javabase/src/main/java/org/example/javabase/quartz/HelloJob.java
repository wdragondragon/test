package org.example.javabase.quartz;

import lombok.Setter;
import lombok.SneakyThrows;
import org.quartz.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.15 16:51
 * @Description:
 */
@DisallowConcurrentExecution
public class HelloJob implements Job {
    @Setter
    private String name;

    public HelloJob() {
    }

    @SneakyThrows
    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        TriggerKey key = context.getTrigger().getKey();
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        JobKey key1 = context.getJobDetail().getKey();

        String doSomething = jobDataMap.getString("do");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println(Thread.currentThread().getId() + " exec start time:" + simpleDateFormat.format(new Date()));
//        System.err.println(name + " Hello! " + doSomething);
//        System.out.println("triggerName:" + key.getName() + " jobName:" + key1.getName());
        Thread.sleep(150 * 1000L);
        System.out.println(Thread.currentThread().getId() + " exec end time:" + simpleDateFormat.format(new Date()));
    }
}
