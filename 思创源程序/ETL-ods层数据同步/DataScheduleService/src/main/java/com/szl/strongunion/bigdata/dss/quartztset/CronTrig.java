package com.szl.strongunion.bigdata.dss.quartztset;

import com.szl.strongunion.bigdata.dss.job.*;
import org.quartz.*;
import org.quartz.JobDetail;
import org.quartz.impl.StdSchedulerFactory;

import java.text.ParseException;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by 小龙
 * on 15-6-25
 * at 上午10:17.
 */

public class CronTrig {

    public void start() throws ParseException, SchedulerException {
        //每个月最后一天00：00检测sl_ods_interface_errorlog_month、sl_ods_page_visit_record_moth是否存在，不存在则建表
        JobDetail job_page_visit =newJob(JobCretePageRecord.class).withIdentity("job_page_visit", "group_job").build();
        Trigger trigger_page_visit = newTrigger().withIdentity("trigger_page_visit", "group_trigger").withSchedule(cronSchedule(JobCretePageRecord.CRON)).build();

        JobDetail job_errorlog_month =newJob(JobCreteErrorLog.class).withIdentity("job_errorlog_month", "group_job").build();
        Trigger trigger_errorlog_month = newTrigger().withIdentity("trigger_errorlog_month", "group_trigger").withSchedule(cronSchedule(JobCreteErrorLog.CRON)).build();


        //同步表:channel_record
        JobDetail job_channel_record =newJob(JobChannelRecord.class).withIdentity("job_channel_record", "group_job").build();
        Trigger trigger_channel_record = newTrigger().withIdentity("trigger_channel_record", "group_trigger").withSchedule(cronSchedule(JobChannelRecord.CRON)).build();
        //同步表:channel_type
        JobDetail job_channel_type =newJob(JobChannelType.class).withIdentity("job_channel_type", "group_job").build();
        Trigger trigger_channel_type = newTrigger().withIdentity("trigger_channel_type", "group_trigger").withSchedule(cronSchedule(JobChannelType.CRON)).build();
        //同步表:channel_apk
        JobDetail job_channel_apk =newJob(JobChannelApk.class).withIdentity("job_channel_apk", "group_job").build();
        Trigger trigger_channel_apk = newTrigger().withIdentity("trigger_channel_apk", "group_trigger").withSchedule(cronSchedule(JobChannelApk.CRON)).build();

        //同步表:reg_user_count
        JobDetail job_reg_user_count =newJob(JobRegUserCount.class).withIdentity("job_reg_user_count", "group_job").build();
        Trigger trigger_reg_user_count = newTrigger().withIdentity("trigger_reg_user_count", "group_trigger").withSchedule(cronSchedule(JobRegUserCount.CRON)).build();
        //同步表:order
        JobDetail job_order =newJob(JobOrder.class).withIdentity("job_order", "group_job").build();
        Trigger trigger_order = newTrigger().withIdentity("trigger_order", "group_trigger").withSchedule(cronSchedule(JobOrder.CRON)).build();
        //同步表:detail
        JobDetail job_detail =newJob(com.szl.strongunion.bigdata.dss.job.JobDetail.class).withIdentity("job_detail", "group_job").build();
        Trigger trigger_detail = newTrigger().withIdentity("trigger_detail", "group_trigger").withSchedule(cronSchedule(com.szl.strongunion.bigdata.dss.job.JobDetail.CRON)).build();
        //同步表:goods
        JobDetail job_goods =newJob(JobGoods.class).withIdentity("job_goods", "group_job").build();
        Trigger trigger_goods = newTrigger().withIdentity("trigger_goods", "group_trigger").withSchedule(cronSchedule(JobGoods.CRON)).build();
        //同步表:goodstype
        JobDetail job_goodstype =newJob(JobGoodstype.class).withIdentity("job_goodstype", "group_job").build();
        Trigger trigger_goodstype = newTrigger().withIdentity("trigger_goodstype", "group_trigger").withSchedule(cronSchedule(JobGoodstype.CRON)).build();
        //同步表:goodsimg
        JobDetail job_goodsimg =newJob(JobGoodsimg.class).withIdentity("job_goodsimg", "group_job").build();
        Trigger trigger_goodsimg = newTrigger().withIdentity("trigger_goodsimg", "group_trigger").withSchedule(cronSchedule(JobGoodsimg.CRON)).build();
        //同步表:shops
        JobDetail job_shops =newJob(JobShops.class).withIdentity("job_shops", "group_job").build();
        Trigger trigger_shops = newTrigger().withIdentity("trigger_shops", "group_trigger").withSchedule(cronSchedule(JobShops.CRON)).build();
        //同步表:shopstype
        JobDetail job_shopstype =newJob(JobShopstype.class).withIdentity("job_shopstype", "group_job").build();
        Trigger trigger_shopstype = newTrigger().withIdentity("trigger_shopstype", "group_trigger").withSchedule(cronSchedule(JobShopstype.CRON)).build();
        //同步表:comshop
        JobDetail job_comshop =newJob(JobComshop.class).withIdentity("job_comshop", "group_job").build();
        Trigger trigger_comshop = newTrigger().withIdentity("trigger_comshop", "group_trigger").withSchedule(cronSchedule(JobComshop.CRON)).build();
        //同步表:community
        JobDetail job_community =newJob(JobCommunity.class).withIdentity("job_community", "group_job").build();
        Trigger trigger_community = newTrigger().withIdentity("trigger_community", "group_trigger").withSchedule(cronSchedule(JobCommunity.CRON)).build();
        //同步表:receipt
        JobDetail job_receipt =newJob(JobReceipt.class).withIdentity("job_receipt", "group_job").build();
        Trigger trigger_receipt = newTrigger().withIdentity("trigger_receipt", "group_trigger").withSchedule(cronSchedule(JobReceipt.CRON)).build();
        //同步表:users
        JobDetail job_users =newJob(JobUsers.class).withIdentity("job_users", "group_job").build();
        Trigger trigger_users = newTrigger().withIdentity("trigger_users", "group_trigger").withSchedule(cronSchedule(JobUsers.CRON)).build();
        //同步表:mobile_device
        JobDetail job_mobile_device =newJob(JobMobileDevice.class).withIdentity("job_mobile_device", "group_job").build();
        Trigger trigger_mobile_device = newTrigger().withIdentity("trigger_mobile_device", "group_trigger").withSchedule(cronSchedule(JobMobileDevice.CRON)).build();
        //同步表:user_integral_level
        JobDetail job_user_integral_level =newJob(JobUserIntegralLevel.class).withIdentity("job_user_integral_level", "group_job").build();
        Trigger trigger_user_integral_level = newTrigger().withIdentity("trigger_user_integral_level", "group_trigger").withSchedule(cronSchedule(JobUserIntegralLevel.CRON)).build();
        //同步表:attention
        JobDetail job_attention =newJob(JobAttention.class).withIdentity("job_attention", "group_job").build();
        Trigger trigger_attention = newTrigger().withIdentity("trigger_attention", "group_trigger").withSchedule(cronSchedule(JobAttention.CRON)).build();
        //同步表:attention_goods
        JobDetail job_attention_goods =newJob(JobAttentionGoods.class).withIdentity("job_attention_goods", "group_job").build();
        Trigger trigger_attention_goods = newTrigger().withIdentity("trigger_attention_goods", "group_trigger").withSchedule(cronSchedule(JobAttentionGoods.CRON)).build();
        //同步表:goods_special
        JobDetail job_goods_special =newJob(JobGoodsSpecial.class).withIdentity("job_goods_special", "group_job").build();
        Trigger trigger_goods_special = newTrigger().withIdentity("trigger_goods_special", "group_trigger").withSchedule(cronSchedule(JobGoodsSpecial.CRON)).build();
        //同步表:shopping_cart
        JobDetail job_shopping_cart =newJob(JobShoppingCart.class).withIdentity("job_shopping_cart", "group_job").build();
        Trigger trigger_shopping_cart = newTrigger().withIdentity("trigger_shopping_cart", "group_trigger").withSchedule(cronSchedule(JobShoppingCart.CRON)).build();


        /**
         * 获取scheduler
         */
        SchedulerFactory schdlFactory = new StdSchedulerFactory();
        Scheduler scheduler = schdlFactory.getScheduler();

        /**
         * 向scheduler加入任务
         */
        scheduler.scheduleJob(job_channel_record, trigger_channel_record);
        scheduler.scheduleJob(job_channel_type, trigger_channel_type);
        scheduler.scheduleJob(job_channel_apk, trigger_channel_apk);

        scheduler.scheduleJob(job_reg_user_count, trigger_reg_user_count);
        scheduler.scheduleJob(job_order, trigger_order);
        scheduler.scheduleJob(job_detail, trigger_detail);
        scheduler.scheduleJob(job_goods, trigger_goods);
        scheduler.scheduleJob(job_goodstype, trigger_goodstype);
        scheduler.scheduleJob(job_goodsimg, trigger_goodsimg);
        scheduler.scheduleJob(job_shops, trigger_shops);
        scheduler.scheduleJob(job_shopstype, trigger_shopstype);
        scheduler.scheduleJob(job_comshop, trigger_comshop);
        scheduler.scheduleJob(job_community, trigger_community);
        scheduler.scheduleJob(job_receipt, trigger_receipt);
        scheduler.scheduleJob(job_users, trigger_users);
        scheduler.scheduleJob(job_mobile_device, trigger_mobile_device);
        scheduler.scheduleJob(job_user_integral_level, trigger_user_integral_level);
        scheduler.scheduleJob(job_attention, trigger_attention);
        scheduler.scheduleJob(job_attention_goods, trigger_attention_goods);
        scheduler.scheduleJob(job_goods_special, trigger_goods_special);
        scheduler.scheduleJob(job_shopping_cart, trigger_shopping_cart);

        //加入两个归档表的创建任务
        scheduler.scheduleJob(job_page_visit, trigger_page_visit);
        scheduler.scheduleJob(job_errorlog_month, trigger_errorlog_month);

        scheduler.start();



         System.out.println("------- 任务调度已经启动 -----------------");
         try {
             // 开启一年，这里注意，如果主线程停止，任务是不会执行的
             Thread.sleep(10*365*24*60*60L * 1000L);
         } catch (Exception e) {

         }

         // 关闭 scheduler
         scheduler.shutdown(true);
         System.out.println("------- 调度已关闭 ---------------------");

    }

    public void startByTableiName(String name) throws SchedulerException {
         Trigger trigger = newTrigger().withIdentity("triggerByName", "triggerByNameGroup").startNow().build();
        //同步表:channel_record
        JobDetail job_channel_record =newJob(JobChannelRecord.class).withIdentity("job_channel_record", "group_job").build();
        //同步表:channel_type
        JobDetail job_channel_type =newJob(JobChannelType.class).withIdentity("job_channel_type", "group_job").build();
        //同步表:channel_apk
        JobDetail job_channel_apk =newJob(JobChannelApk.class).withIdentity("job_channel_apk", "group_job").build();
        //同步表:reg_user_count
        JobDetail job_reg_user_count =newJob(JobRegUserCount.class).withIdentity("job_reg_user_count", "group_job").build();
        //同步表:order
        JobDetail job_order =newJob(JobOrder.class).withIdentity("job_order", "group_job").build();
        //同步表:detail
        JobDetail job_detail =newJob(com.szl.strongunion.bigdata.dss.job.JobDetail.class).withIdentity("job_detail", "group_job").build();
        //同步表:goods
        JobDetail job_goods =newJob(JobGoods.class).withIdentity("job_goods", "group_job").build();
        //同步表:goodstype
        JobDetail job_goodstype =newJob(JobGoodstype.class).withIdentity("job_goodstype", "group_job").build();
        //同步表:goodsimg
        JobDetail job_goodsimg =newJob(JobGoodsimg.class).withIdentity("job_goodsimg", "group_job").build();
        //同步表:shops
        JobDetail job_shops =newJob(JobShops.class).withIdentity("job_shops", "group_job").build();
        //同步表:shopstype
        JobDetail job_shopstype =newJob(JobShopstype.class).withIdentity("job_shopstype", "group_job").build();
        //同步表:comshop
        JobDetail job_comshop =newJob(JobComshop.class).withIdentity("job_comshop", "group_job").build();
        //同步表:community
        JobDetail job_community =newJob(JobCommunity.class).withIdentity("job_community", "group_job").build();
        //同步表:receipt
        JobDetail job_receipt =newJob(JobReceipt.class).withIdentity("job_receipt", "group_job").build();
        //同步表:users
        JobDetail job_users =newJob(JobUsers.class).withIdentity("job_users", "group_job").build();
        //同步表:mobile_device
        JobDetail job_mobile_device =newJob(JobMobileDevice.class).withIdentity("job_mobile_device", "group_job").build();
        //同步表:user_integral_level
        JobDetail job_user_integral_level =newJob(JobUserIntegralLevel.class).withIdentity("job_user_integral_level", "group_job").build();
        //同步表:attention
        JobDetail job_attention =newJob(JobAttention.class).withIdentity("job_attention", "group_job").build();
        //同步表:attention_goods
        JobDetail job_attention_goods =newJob(JobAttentionGoods.class).withIdentity("job_attention_goods", "group_job").build();
        //同步表:goods_special
        JobDetail job_goods_special =newJob(JobGoodsSpecial.class).withIdentity("job_goods_special", "group_job").build();
        //同步表:shopping_cart
        JobDetail job_shopping_cart =newJob(JobShoppingCart.class).withIdentity("job_shopping_cart", "group_job").build();


        /**
         * 获取scheduler
         */
        SchedulerFactory schdlFactory = new StdSchedulerFactory();
        Scheduler scheduler = schdlFactory.getScheduler();

        /**
         * 向scheduler加入任务
         */
        if (name.equals("channel_record")){
            scheduler.scheduleJob(job_channel_record, trigger);
        }else if (name.equals("channel_type")){
            scheduler.scheduleJob(job_channel_type, trigger);
        }else if (name.equals("channel_apk")){
            scheduler.scheduleJob(job_channel_apk, trigger);
        }else if (name.equals("reg_user_count")){
            scheduler.scheduleJob(job_reg_user_count, trigger);
        }else if (name.equals("order")){
            scheduler.scheduleJob(job_order, trigger);
        }else if (name.equals("detail")){
            scheduler.scheduleJob(job_detail, trigger);
        }else if (name.equals("goods")){
            scheduler.scheduleJob(job_goods, trigger);
        }else if (name.equals("goodstype")){
            scheduler.scheduleJob(job_goodstype, trigger);
        }else if (name.equals("goodsimg")){
            scheduler.scheduleJob(job_goodsimg, trigger);
        }else if (name.equals("shops")){
            scheduler.scheduleJob(job_shops, trigger);
        }else if (name.equals("shopstype")){
            scheduler.scheduleJob(job_shopstype, trigger);
        }else if (name.equals("comshop")){
            scheduler.scheduleJob(job_comshop, trigger);
        }else if (name.equals("community")){
            scheduler.scheduleJob(job_community, trigger);
        }else if (name.equals("receipt")){
            scheduler.scheduleJob(job_receipt, trigger);
        }else if (name.equals("users")){
            scheduler.scheduleJob(job_users, trigger);
        }else if (name.equals("mobile_device")){
            scheduler.scheduleJob(job_mobile_device, trigger);
        }else if (name.equals("user_integral_level")){
            scheduler.scheduleJob(job_user_integral_level, trigger);
        }else if (name.equals("attention")){
            scheduler.scheduleJob(job_attention, trigger);
        }else if (name.equals("attention_goods")){
            scheduler.scheduleJob(job_attention_goods, trigger);
        }else if (name.equals("goods_special")){
            scheduler.scheduleJob(job_goods_special, trigger);
        }else if (name.equals("shopping_cart")){
            scheduler.scheduleJob(job_shopping_cart, trigger);
        }

        scheduler.start();

        System.out.println("------- 任务调度已经启动 -----------------");
        try {
            // 开启一小时，这里注意，如果主线程停止，任务是不会执行的
            Thread.sleep(60*60L * 1000L);
        } catch (Exception e) {

        }
        scheduler.shutdown(true);
        System.out.println("------- 调度已关闭 ---------------------");

    }

    public static void main(String[] args) {
        if (args.length==0){
            try {
                new CronTrig().start();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }else {
            try {
                new CronTrig().startByTableiName(args[0]);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }

    }
}
