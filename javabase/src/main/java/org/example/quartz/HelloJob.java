package org.example.quartz;

import lombok.Setter;
import org.quartz.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.15 16:51
 * @Description:
 */
public class HelloJob implements Job {
    @Setter
    private String name;

    public HelloJob() {
    }

    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        TriggerKey key = context.getTrigger().getKey();
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        JobKey key1 = context.getJobDetail().getKey();

        String doSomething = jobDataMap.getString("do");

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println("exec time:" + simpleDateFormat.format(date));
        System.err.println(name + " Hello! " + doSomething);
        System.out.println("triggerName:" + key.getName() + " jobName:" + key1.getName());
    }
}
