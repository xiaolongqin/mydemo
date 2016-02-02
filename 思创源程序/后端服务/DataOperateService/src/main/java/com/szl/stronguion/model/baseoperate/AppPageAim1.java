package com.szl.stronguion.model.baseoperate;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/5/31.
 */
//app页面转换率分析数
public class AppPageAim1 extends Model<AppPageAim1> {
    private static AppPageAim1 dao = new AppPageAim1();
    public final static String TABLE = "sl_rpt_app_page_aim1";
    public final static String DAY = "day_no";
    public final static String PROD_NAME = "aprod_name";


    public List<Record> getPageAim1(String startTime, String endTime) {
        return Db.find("select a.prod_name aprod_name,a.url_link burl_link,SUM(a.avg_residence_time) davg_time," +
                "SUM(a.page_pv) as  pvNum0 , SUM(a.page_uv) as uvNum0\n" +
                "  from strongunion_online.sl_rpt_app_page_aim1  a \n" +
                "    where  day_no >= '" + startTime + "' and day_no <= '" + endTime + "' and (a.day_no is not null or a.day_no <> 0)\n" +
                "  group by a.prod_name order by a.prod_name DESC;");

    }

    //for index table
    public List<Record> getPvUv(String startTime, String endTime) {
        return Db.find("select a.prod_name aprod_name,SUM(a.page_pv) as  pvNum0 , SUM(a.page_uv) as uvNum0\n" +
                "  from strongunion_online.sl_rpt_app_page_aim1  a \n" +
                "    where  day_no >= '" + startTime + "' and day_no <= '" + endTime + "'  and (a.day_no is not null or a.day_no <> 0)\n" +
                "  group by a.prod_name order by a.prod_name DESC;");

    }

    //for index table
    public List<Record> getPvUv(String time) {
        return Db.use("main2").find("select a.prod_name name,a.page_pv data1,a.page_uv data2\n" +
                " from sl_rpt_app_page_aim1  a \n" +
                "    where  a.day_no = '" + time + "' and (a.day_no is not null or a.day_no <> 0);");

    }


    /**
     * 展示折线图 *
     * *
     */
//    //获取时间段的渠道
//    public List<Record> getChannelName(String star_time,String end_time,String port_name ) {
//        return Db.use("main2").find("select DISTINCT(app_channel) as channel_name from sl_rpt_app_channel_aim2 a1\n" +
//                "                                   WHERE prod_name like '%"+port_name+"%'  AND a1.day_no >= '"+star_time+"' and a1.day_no <= '"+end_time+"'\n" +
//                "                                   AND  (a1.day_no is not null or a1.day_no <> 0);");
//    }
    //获取时间段的渠道(某一端)
    public List<Record> getChannelName(String star_time,String end_time,String port_name ) {
        return Db.use("main2").find("select app_channel,prod_name,CONCAT(app_channel,'(',prod_name,')') as channel_name from sl_rpt_app_channel_aim2 a1\n" +
                "                                             WHERE prod_name like '%"+port_name+"%'  AND a1.day_no >= '"+star_time+"' and a1.day_no <= '"+end_time+"'\n" +
                "                                             AND  (a1.day_no is not null or a1.day_no <> 0)");
    }

    //get pv
    public  List<Record> getPv(String start_time,String end_time,String channel_name,String port_name ) {
        return Db.use("main2").find(" select  a1.day_no as time,\n" +
                "                                  a1.app_channel,\n" +
                "                                  SUM(a1.channel_num2) as data1/*平均PV*/\n" +
                "                            from sl_rpt_app_channel_aim2  a1 \n" +
                "                           where  a1.app_channel='"+channel_name+"' and a1.day_no >= '"+start_time+"' and a1.day_no <= '"+end_time+"'\n" +
                "                             and a1.prod_name LIKE '%"+port_name+"%' and(a1.day_no is not null or a1.day_no <> 0)\n" +
                "                            group by a1.day_no,a1.app_channel ;");
    }

//    //get uv
//    public List<Record> getUv(String time,String channel_name,String port_name) {
//        return Db.use("main2").find("select a1.app_channel name,\n" +
//                "                  round(SUM(a1.channel_num1)/count(*),2) as data1 /*平均UV*/\n" +
//                "                 from sl_rpt_app_channel_aim2  a1 \n" +
//                "                   where  a1.day_no = '"+time+"' and a1.prod_name='"+channel_name+"' and (a1.day_no is not null or a1.day_no <> 0) GROUP BY name;");
//    }

    //get uv
    public List<Record> getUv(String start_time,String end_time,String channel_name,String port_name) {
        return Db.use("main2").find(" select  a1.day_no as time,\n" +
                "                                  a1.app_channel,\n" +
                "                  round(SUM(a1.channel_num1)/count(*),2) as data1 /*平均UV*/\n" +
                "                            from sl_rpt_app_channel_aim2  a1 \n" +
                "                           where  a1.app_channel='"+channel_name+"' and a1.day_no >= '"+start_time+"' and a1.day_no <= '"+end_time+"'\n" +
                "                             and a1.prod_name LIKE '%"+port_name+"%' and(a1.day_no is not null or a1.day_no <> 0)\n" +
                "                            group by a1.day_no,a1.app_channel ;");
    }

    /**
     * 展示表格
     * * *
     */
//    public List<Record> getCountPv(String time) {
//        return Db.use("main2").find("select a.day_no day1 , SUM(a.page_pv) data1\n" +
//                "from sl_rpt_app_page_aim1  a \n" +
//                "   where  a.day_no = '" + time + "' and (a.day_no is not null or a.day_no <> 0);");
//    }

    public List<Record> getCountPv(String time,String port_name) {
        return Db.use("main2").find("select '"+time+"' as day1,(case when SUM(a.page_pv) is null then 0 else SUM(a.page_pv) end) as data1\n" +
                "                from sl_rpt_app_page_aim1  a \n" +
                "                 where  a.day_no = '"+time+"' and  a.prod_name  LIKE '%"+port_name+"%' and (a.day_no is not null or a.day_no <> 0);");
    }



    public List<Record> getCountUv(String time,String port_name) {
        return Db.use("main2").find("\n" +
                "select '"+time+"' as day1,SUM(tt.data1) as data1 FROM (select  a1.day_no as time,\n" +
                "                          a1.app_channel,\n" +
                "                           round(SUM(a1.channel_num1)/count(*),2) as data1 /*平均UV*/\n" +
                "                           from sl_rpt_app_channel_aim2  a1 \n" +
                "                           where  a1.day_no = '"+time+"' \n" +
                "                           and a1.prod_name LIKE '%"+port_name+"%' and(a1.day_no is not null or a1.day_no <> 0)\n" +
                "                           group by a1.day_no,a1.app_channel) tt");
    }
}
