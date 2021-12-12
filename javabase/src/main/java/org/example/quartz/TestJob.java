package org.example.quartz;

import org.quartz.CalendarIntervalScheduleBuilder;
import org.quartz.CalendarIntervalTrigger;
import org.quartz.DateBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author JDragon
 * @Date 2021.11.22 上午 11:30
 * @Email 1061917196@qq.com
 * @Des:
 */
public class TestJob implements Job {

    private String test = null;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println(this.hashCode());
        test = (String) context.get("test");
        if (this.test == null) {
            this.test = "ssss";
            System.out.println(this.test);
            context.put("test", this.test);
        } else {
            System.out.println("已初始化");
        }
    }

    public static void main(String[] args) throws SchedulerException, InterruptedException {
        SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

        Scheduler sched = schedFact.getScheduler();

        sched.start();

        JobKey jobKey = JobKey.jobKey("myJob", "group1");
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("do", "work!");
        // define the job and tie it to our HelloJob class
        JobDetail job = JobBuilder.newJob(TestJob.class)
                .withIdentity(jobKey) // name "myJob", group "group1"
                .usingJobData("name", "admin")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();

        TriggerKey triggerKey = TriggerKey.triggerKey("myTrigger", "group1");

        CalendarIntervalScheduleBuilder calendarIntervalScheduleBuilder =
                CalendarIntervalScheduleBuilder.calendarIntervalSchedule();
        calendarIntervalScheduleBuilder.withInterval(1, DateBuilder.IntervalUnit.SECOND);

        //触发建立
        CalendarIntervalTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey).withSchedule(calendarIntervalScheduleBuilder.withMisfireHandlingInstructionDoNothing())
                .build();

        sched.scheduleJob(job, trigger);

//        Thread.sleep(90 * 1000L);
    }
}
