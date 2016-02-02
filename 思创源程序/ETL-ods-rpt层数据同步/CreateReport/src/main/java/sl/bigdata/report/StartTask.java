package sl.bigdata.report;

import sl.bigdata.util.GetMonth;

import java.util.Timer;

/**
 * Created by Tyfunwang on 2015/5/29.
 */
public class StartTask {
    Timer timer = new Timer();
    //设置执行时间

    //检测ods日志是否成功(每20分钟检测一次)，成功才执行rpt抽取
    public void testOds() {
        timer.schedule(TimeTask.testOds,  GetMonth.getNextDayTime(20),20*60*1000L);
    }
    //检测ods日志是否成功，失败就发送邮件
    public void mailOds() {
        timer.schedule(TimeTask.mailOds,  GetMonth.getNextDayTime(420),1000*60*60*24L);
    }
    //每天凌晨12：10点重置jar包的运行次数
    public void resstRptCount() {
        timer.schedule(TimeTask.resetRptCount, GetMonth.getNextDayTime(10),1000*60*60*24L);
    }

    public static void main(String[] args) {
        if (args.length==0){
            StartTask task = new StartTask();
            task.resstRptCount();
            task.testOds();
            task.mailOds();

        }else if(args[0].equals("sl_rpt_app_channel_aim1")){
            TimeTask.task1.run();
            TimeTask.task1.cancel();
        }else if (args[0].equals("sl_rpt_app_channel_aim2")){
            TimeTask.task2.run();
            TimeTask.task2.cancel();
        }else if (args[0].equals("sl_rpt_app_page_aim1")){
            TimeTask.task3.run();
            TimeTask.task3.cancel();
        }else if (args[0].equals("sl_rpt_life_cycle_users")){
            TimeTask.task4.run();
            TimeTask.task4.cancel();
        }else if (args[0].equals("sl_rpt_oder_any_aim1")){
            TimeTask.task5.run();
            TimeTask.task5.cancel();
        }else if (args[0].equals("sl_rpt_life_cycle_users_flag")){
            TimeTask.task6.run();
            TimeTask.task6.cancel();
        }else if (args[0].equals("sl_rpt_life_cycle_all_users_flag")){
            TimeTask.task7.run();
            TimeTask.task7.cancel();
        }else if(args[0].equals("sl_rpt_analysis_order")){
            TimeTask.task8.run();
            TimeTask.task8.cancel();
        }else if(args[0].equals("sl_rpt_app_page_visit")){
            TimeTask.task10.run();
            TimeTask.task10.cancel();
        }

    }
}
