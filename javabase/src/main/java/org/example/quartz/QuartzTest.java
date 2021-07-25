package org.example.quartz;

import org.quartz.*;


/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.15 16:55
 * @Description:
 */
public class QuartzTest {
    public static void main(String[] args) throws SchedulerException, InterruptedException {
        SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

        Scheduler sched = schedFact.getScheduler();

        sched.start();


        JobKey jobKey = JobKey.jobKey("myJob", "group1");
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("do", "work!");
        // define the job and tie it to our HelloJob class
        JobDetail job = JobBuilder.newJob(HelloJob.class)
                .withIdentity(jobKey) // name "myJob", group "group1"
                .usingJobData("name", "admin")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();

        TriggerKey triggerKey = TriggerKey.triggerKey("myTrigger", "group1");
        // Trigger the job to run now, and then every 40 seconds
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .startNow()
//                .forJob("myJob","group1")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(2)
                        .repeatForever())
                .build();

        // Tell quartz to schedule the job using our trigger
        sched.scheduleJob(job, trigger);
//        sched.addJob(job,true);
//        sched.scheduleJob(trigger);
        Thread.sleep(10*1000);

        sched.deleteJob(jobKey);
    }
}
