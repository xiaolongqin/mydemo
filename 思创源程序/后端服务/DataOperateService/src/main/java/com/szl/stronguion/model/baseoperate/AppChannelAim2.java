package com.szl.stronguion.model.baseoperate;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.utils.FormatUtils;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/5/31.
 */
//app存量用户分渠道统计日报
public class AppChannelAim2 extends Model<AppChannelAim2> {
    String startTime = FormatUtils.getDay(-7);//当前日期的前7天
    String endTime = FormatUtils.getDay(-1);//当前日期的前一天
//        String startTime = "20150631";
//    String endTime = "20150803";
    public final static String TABLE = "sl_rpt_app_channel_aim2";
    public final static String DAY = "day_no";

    private static AppChannelAim2 dao = new AppChannelAim2();

    //获取柱状图
      //全部端
    public List<Record> getPuAndUv(String startTime,String endTime) {

        return Db.use("main2").find("select a1.prod_name, /*产品渠道：如用户端、商户端、管家端*/\n" +
                "       a1.app_channel, /*app推广渠道：指具体某个产品推广的渠道*/\n" +
                "\t     SUM(a1.channel_num2) as channel_sum_pv, /*总的PV*/\n" +
                "       round(SUM(a1.channel_num2)/count(*),2) as channel_avg_pv, /*平均PV*/\n" +
                "\t     SUM(a1.channel_num1) as channel_sum_uv, /*总的UV*/\n" +
                "       round(SUM(a1.channel_num1)/count(*),2) as channel_avg_uv, /*平均UV*/\n" +
                "       '"+startTime+"' as startTime,\n" +
                "       '"+endTime+"' as  endTime \n" +
                "  from sl_rpt_app_channel_aim2 a1\n" +
                " where a1.day_no >= '"+startTime+"' and a1.day_no <= '"+endTime+"'\n" +
                "  and (a1.day_no is not null or a1.day_no <> 0)\n" +
                " GROUP BY a1.prod_name, a1.app_channel ORDER BY a1.prod_name DESC;");
    }
      //某一端
    public List<Record> getPuAndUv(String prod_name,String startTime,String endTime) {

        return Db.use("main2").find("select a1.prod_name, /*产品渠道：如用户端、商户端、管家端*/\n" +
                "       a1.app_channel, /*app推广渠道：指具体某个产品推广的渠道*/\n" +
                "\t     SUM(a1.channel_num2) as channel_sum_pv, /*总的PV*/\n" +
                "       round(SUM(a1.channel_num2)/count(*),2) as channel_avg_pv, /*平均PV*/\n" +
                "\t     SUM(a1.channel_num1) as channel_sum_uv, /*总的UV*/\n" +
                "       round(SUM(a1.channel_num1)/count(*),2) as channel_avg_uv, /*平均UV*/\n" +
                "       '"+startTime+"' as startTime,\n" +
                "       '"+endTime+"' as  endTime \n" +
                "  from sl_rpt_app_channel_aim2 a1\n" +
                " where a1.day_no >= '"+startTime+"' and a1.day_no <= '"+endTime+"'\n" +
                "  and (a1.day_no is not null or a1.day_no <> 0)\n" +
                "  and  a1.prod_name='"+prod_name+"'\n" +
                " GROUP BY a1.prod_name, a1.app_channel ORDER BY a1.prod_name DESC;");
    }


    public List<Record> getAppChannelAim2(String startTime, String endTime) {
        return Db.find("select a.app_channel, SUM(a.channel_num0) as aregate0, SUM(a.channel_num1) as aregate1" +
                " from strongunion_online.sl_rpt_app_channel_aim2  a \n" +
                "      where  " + DAY + " >= '" + startTime + "' and " + DAY + " <= '" + endTime + "' and (a.day_no is not null or a.day_no <> 0) " +
                "     group by a.app_channel order by a.app_channel DESC; ");
    }

    public List<Record> getChannelAim2(String startTime, String endTime) {
        /**
         *  下载用户数：totalNum0
         *  活跃用户数：totalNum1
         *  活跃率：avgNum0
         *  沉默率：avgNum1
         * * */
        return Db.find("select a.app_channel, SUM(a.channel_num0) as aregate0, SUM(a.channel_num1) as aregate1, round(SUM(a.channel_num1) / SUM(a.channel_num0), 4) as avgNum0, 1 - round(SUM(a.channel_num1) / SUM(a.channel_num0), 4) as avgNum1\n" +
                " from strongunion_online.sl_rpt_app_channel_aim2  a \n" +
                "    where  " + DAY + " >= '" + startTime + "' and " + DAY + " <= '" + endTime + "' and (a.day_no is not null or a.day_no <> 0) " +
                "  group by a.app_channel order by a.app_channel DESC; ");
    }

    //get counts
    public List<AppChannelAim2> getCountChannelAim1() {
        String startTime = "20150530";
        String endTime = "20150601";
        /**
         * *下载用户总数：total1
         * *活跃用户总数：total2
         * * 平均活跃率：avg1
         * * 平均沉默率：avg2
         * * * */
        return dao.find("select SUM(b.aregate0) total0,SUM(b.aregate1) total1,TRUNCATE(AVG(b.avgNum0),4) avg0,TRUNCATE(AVG( b.avgNum1),4) avg1 from (select a.app_channel,SUM(a.channel_num0) as  aregate0 , SUM(a.channel_num1) as aregate1 ,  round(SUM(a.channel_num1)/SUM(a.channel_num0),4) as avgNum0, 1-round(SUM(a.channel_num1)/SUM(a.channel_num0),4) as  avgNum1\n" +
                "  from sl_rpt_app_channel_aim2  a \n" +
                "    where  " + DAY + " >= '" + startTime + "' and " + DAY + " <= '" + endTime + "' and (a.day_no is not null or a.day_no <> 0) " +
                "  group by a.app_channel order by a.app_channel DESC) as b;");
    }

    //get counts
    public List<Record> getCountChanAim1(String startTime, String endTime) {
        /**
         * *下载用户总数：total1
         * *活跃用户总数：total2
         * * 平均活跃率：avg1
         * * 平均沉默率：avg2
         * * * */
        return Db.find("select SUM(b.aregate0) total0,SUM(b.aregate1) total1,TRUNCATE(AVG(b.avgNum0),4) avg0,TRUNCATE(AVG( b.avgNum1),4) avg1 from (select a.app_channel,SUM(a.channel_num0) as  aregate0 , SUM(a.channel_num1) as aregate1 ,  round(SUM(a.channel_num1)/SUM(a.channel_num0),4) as avgNum0, 1-round(SUM(a.channel_num1)/SUM(a.channel_num0),4) as  avgNum1\n" +
                "  from strongunion_online.sl_rpt_app_channel_aim2  a \n" +
                "    where  " + DAY + " >= '" + startTime + "' and " + DAY + " <= '" + endTime + "' and (a.day_no is not null or a.day_no <> 0)" +
                "  group by a.app_channel order by a.app_channel DESC) as b;");
    }

}
