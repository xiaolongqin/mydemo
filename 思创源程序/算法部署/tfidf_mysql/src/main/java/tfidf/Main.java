package tfidf;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.text.ParseException;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by Administrator on 2015/6/23.
 */

public class Main {
    public void start() throws ParseException, SchedulerException {
    //每个月最后一天00：00检测sl_ods_interface_errorlog_month、sl_ods_page_visit_record_moth是否存在，不存在则建表
    JobDetail job_page_visit =newJob(TfidfJob.class).withIdentity("job_page_visit", "group_job").build();
    Trigger trigger_page_visit = newTrigger().withIdentity("trigger_page_visit", "group_trigger").withSchedule(cronSchedule(TfidfJob.CRON)).build();

    /**
     * 获取scheduler
     */
    SchedulerFactory schdlFactory = new StdSchedulerFactory();
    Scheduler scheduler = schdlFactory.getScheduler();

    /**
     * 向scheduler加入任务
     */
    scheduler.scheduleJob(job_page_visit, trigger_page_visit);
    scheduler.start();

        System.out.println("------- 任务调度已经启动 -----------------");
        try {
            // 开启10年，这里注意，如果主线程停止，任务是不会执行的
            Thread.sleep(10*365*24*60*60L * 1000L);
        } catch (Exception e) {

        }

        // 关闭 scheduler
        scheduler.shutdown(true);
        System.out.println("------- 调度已关闭 ---------------------");


    }





    public static void main(String[] args)  {

        try {
            new Main().start();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }
}