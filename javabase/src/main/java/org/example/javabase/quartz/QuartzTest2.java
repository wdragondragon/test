package org.example.javabase.quartz;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author JDragon
 * @Date 2021.10.19 下午 5:39
 * @Email 1061917196@qq.com
 * @Des:
 */
public class QuartzTest2 {
    public static void main(String[] args) throws SchedulerException, InterruptedException, ParseException {
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

        CalendarIntervalScheduleBuilder calendarIntervalScheduleBuilder =
                CalendarIntervalScheduleBuilder.calendarIntervalSchedule();
        calendarIntervalScheduleBuilder.withInterval(1, DateBuilder.IntervalUnit.MINUTE);

//        DailyTimeIntervalScheduleBuilder dailyTimeIntervalScheduleBuilder = DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule();
//        dailyTimeIntervalScheduleBuilder.withInterval(2, DateBuilder.IntervalUnit.SECOND);


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        System.out.println("now:" + simpleDateFormat.format(now));
        Calendar instance = Calendar.getInstance();
        instance.setTime(now);
        instance.add(Calendar.SECOND, -10);
        Date startTime = instance.getTime();
        instance.add(Calendar.MINUTE, 10);
        Date endTime = instance.getTime();
        System.out.println("startTime:" + simpleDateFormat.format(startTime));
        System.out.println("endTime:" + simpleDateFormat.format(endTime));
        //触发建立
        CalendarIntervalTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey).withSchedule(calendarIntervalScheduleBuilder.withMisfireHandlingInstructionDoNothing())
                .startAt(startTime)
                .endAt(endTime)
                .build();

        sched.getListenerManager().addSchedulerListener(new TestListener());

        sched.scheduleJob(job, trigger);

//        getAllJobs(sched);
//
//        Thread.sleep(90 * 1000L);
//
//        getAllJobs(sched);
//
//        sched.deleteJob(jobKey);
//
//        sched.shutdown();
    }

    public static void getAllJobs(Scheduler scheduler) {
        try {
            for (String groupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    String jobName = jobKey.getName();
                    String jobGroup = jobKey.getGroup();
                    //get job's trigger
                    List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                    System.out.println("[jobName] : " + jobName + " [groupName] : "
                            + jobGroup);
                    System.out.println("triggers num: " + triggers.size());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
