package org.example.quartz;

import lombok.Setter;
import org.quartz.*;

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


        System.err.println(name + " Hello! " + doSomething);
        System.out.println("triggerName:"+key.getName()+" jobName:"+key1.getName());
    }
}
