package com.szl.stronguion.model.baseoperate;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.utils.FormatUtils;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Tyfunwang on 2015/5/31.
 */
//app推广分渠道统计日报
public class AppChannelAim1 extends Model<AppChannelAim1> {
    public static AppChannelAim1 dao = new AppChannelAim1();
    public final static String TABLE = "sl_rpt_app_channel_aim1";
    public final static String APP_CHANNEL = "app_channel";

    String startTime = FormatUtils.getDay(-7);//当前日期的前7天
    String endTime = FormatUtils.getDay(-1);//当前日期的前一天
//    String startTime = "20140531";
//    String endTime = "20150803";




    public Map<String, List<Record>> getThreeTotal(){
        Map<String,List<Record>> maps = new TreeMap<String,List<Record>>();
        /*用户总数：*/
        List<Record> r1=Db.use("main2").find("select count(distinct t.uid) as count\n" +
                " from sl_rpt_life_cycle_users t\n" +
                "where (t.uid is not null or t.uid<>'')\n" +
                "  and (t.reg_time is not null or t.reg_time <> 0);");
        /*店铺总数：*/
        List<Record> r2=Db.use("main2").find("select count(distinct t.id) as count\n" +
                "  from sl_ods_shops t\n" +
                "where (t.id is not null or t.id<>'')\n" +
                "  and t.state in (1,2,6);");
        /*订单总数：*/
        List<Record> r3=Db.use("main2").find("select count(distinct t.id) as count\n" +
                "  from sl_ods_order t\n" +
                " where t.state in ('-1','1','2','3','4')\n" +
                "  and (t.id is not null or t.id<>'')\n" +
                "  and (t.addtime is not null or t.addtime <> 0);");
        maps.put("userCount",r1);
        maps.put("shopCount",r2);
        maps.put("orderCount",r3);

        return maps;
    }



    /**
     * 展示饼状图 *
     * *
     */
  //保存率
    //全部
    public List<Record> getSaveRate(String startTime,String endTime) {
        Record record=Db.use("main2").findFirst("select \n" +
                "       SUM(a1.channel_num1) as countNum4,\n" +
                "       (case when sum(a1.channel_num1) = 0 then 0\n" +
                "        else ROUND(SUM(a1.channel_num3)/SUM(a1.channel_num1)*100,2)\n" +
                "        end) as total_result /*保存率：下载用户数/到达用户*/\n" +
                "  from sl_rpt_app_channel_aim1 a1\n" +
                " where a1.day_no >= '"+startTime+"' and a1.day_no <= '"+endTime+"'\n" +
                "  and (a1.day_no is not null or a1.day_no <> 0);");
        List<Record> list2=Db.use("main2").find("select a1.prod_name, /*产品渠道：如用户端、商户端、管家端*/\n" +
                "       a1.app_channel, /*app推广渠道：指具体某个产品推广的渠道*/\n" +
                "        SUM(a1.channel_num3) as countNum3, /*下载用户数 保存用户*/\n" +
                "       (case when sum(a1.channel_num1) = 0 then 0\n" +
                "        else ROUND(SUM(a1.channel_num3)/SUM(a1.channel_num1)*100,2)\n" +
                "        end) as result /*保存率：下载用户数/到达用户*/\n" +
                "  from sl_rpt_app_channel_aim1 a1\n" +
                " where a1.day_no >= '"+startTime+"' and a1.day_no <= '"+endTime+"'\n" +
                "  and (a1.day_no is not null or a1.day_no <> 0)\n" +
                " GROUP BY a1.prod_name, a1.app_channel ORDER BY a1.prod_name DESC;");
        if (record!=null){
            list2.add(record);
        }
        return list2;
    }
    //某一个端
    public List<Record> getSaveRate(String prod_name,String startTime,String endTime) {
        Record record=Db.use("main2").findFirst("select \n" +
                "       SUM(a1.channel_num1) as countNum4,\n" +
                "       (case when sum(a1.channel_num1) = 0 then 0\n" +
                "        else ROUND(SUM(a1.channel_num3)/SUM(a1.channel_num1)*100,2)\n" +
                "        end) as total_result  /*保存率：下载用户数/到达用户*/\n" +
                "  from sl_rpt_app_channel_aim1 a1\n" +
                " where a1.day_no >= '"+startTime+"' and a1.day_no <= '"+endTime+"'\n" +
                " and  a1.prod_name='"+prod_name+"'\n" +
                "  and (a1.day_no is not null or a1.day_no <> 0);");
        List<Record> list2= Db.use("main2").find("select a1.prod_name, /*产品渠道：如用户端、商户端、管家端*/\n" +
                "       a1.app_channel, /*app推广渠道：指具体某个产品推广的渠道*/\n" +
                "        SUM(a1.channel_num3) as countNum3, /*下载用户数 保存用户*/\n" +
                "       (case when sum(a1.channel_num1) = 0 then 0\n" +
                "        else ROUND(SUM(a1.channel_num3)/SUM(a1.channel_num1)*100,2)\n" +
                "        end) as result /*保存率：下载用户数/到达用户*/\n" +
                "  from sl_rpt_app_channel_aim1 a1\n" +
                " where a1.day_no >= '"+startTime+"' and a1.day_no <= '"+endTime+"'\n" +
                "  and (a1.day_no is not null or a1.day_no <> 0)\n" +
                "  and  a1.prod_name='"+prod_name+"'\n" +
                " GROUP BY a1.prod_name, a1.app_channel ORDER BY a1.prod_name DESC;\n");
        if (record!=null){
            list2.add(record);
        }
        return list2;
    }

  //转化率
    //全部端
    public List<Record> getTransRate(String startTime,String endTime) {
        Record record=Db.use("main2").findFirst("select \n" +
                "       SUM(a1.channel_num0) as countNum0,\n" +
                "       (case when sum(a1.channel_num0) = 0 then 0\n" +
                "        else ROUND(SUM(a1.channel_num1)/SUM(a1.channel_num0)*100,2)\n" +
                "        end) as total_result /*保存率：到达用户/总的目标用户*/\n" +
                "  from sl_rpt_app_channel_aim1 a1\n" +
                " where a1.day_no >= '"+startTime+"' and a1.day_no <= '"+endTime+"'\n" +
                "  and (a1.day_no is not null or a1.day_no <> 0);");

        //-- countNum1 到达量
         List<Record> list2= Db.use("main2").find("select a1.prod_name, /*产品渠道：如用户端、商户端、管家端*/\n" +
                "       a1.app_channel, /*app推广渠道：指具体某个产品推广的渠道*/\n" +
                "       SUM(a1.channel_num1) as countNum1, /*到达用户 到达量*/\n" +
                "       (case when sum(a1.channel_num0) = 0 then 0\n" +
                "        else ROUND(SUM(a1.channel_num1)/SUM(a1.channel_num0)*100,2)\n" +
                "        end) as result /*保存率：到达用户/总的目标用户*/\n" +
                "  from sl_rpt_app_channel_aim1 a1\n" +
                " where a1.day_no >= '"+startTime+"' and a1.day_no <= '"+endTime+"'\n" +
                "  and (a1.day_no is not null or a1.day_no <> 0)\n" +
                " GROUP BY a1.prod_name, a1.app_channel ORDER BY a1.prod_name DESC;");
        if (record!=null){
            list2.add(record);
        }
        return list2;
    }
    //某一端
    public List<Record> getTransRate(String prod_name,String startTime,String endTime) {
        Record record=Db.use("main2").findFirst("select \n" +
                "       SUM(a1.channel_num0) as countNum0,\n" +
                "       (case when sum(a1.channel_num0) = 0 then 0\n" +
                "        else ROUND(SUM(a1.channel_num1)/SUM(a1.channel_num0)*100,2)\n" +
                "        end) as total_result /*保存率：到达用户/总的目标用户*/\n" +
                "  from sl_rpt_app_channel_aim1 a1\n" +
                "  where a1.day_no >= '"+startTime+"' and a1.day_no <= '"+endTime+"'\n" +
                "  and  a1.prod_name='"+prod_name+"'\n" +
                "  and (a1.day_no is not null or a1.day_no <> 0)");
        
         List<Record> list2=Db.use("main2").find("select a1.prod_name, /*产品渠道：如用户端、商户端、管家端*/\n" +
                "       a1.app_channel, /*app推广渠道：指具体某个产品推广的渠道*/\n" +
                "       SUM(a1.channel_num1) as countNum1, /*到达用户 到达量*/\n" +
                "       (case when a1.channel_num0 = 0 then 0\n" +
                "        else ROUND(SUM(a1.channel_num1)/SUM(a1.channel_num0)*100,2)\n" +
                "        end) as result /*保存率：到达用户/总的目标用户*/\n" +
                "  from sl_rpt_app_channel_aim1 a1\n" +
                " where a1.day_no >= '"+startTime+"' and a1.day_no <= '"+endTime+"'\n" +
                "  and (a1.day_no is not null or a1.day_no <> 0)\n" +
                "  and  a1.prod_name='"+prod_name+"'\n" +
                " GROUP BY a1.prod_name, a1.app_channel ORDER BY a1.prod_name DESC;");
        if (record!=null){
            list2.add(record);
        }
        return  list2;
    }


    //保存量，到达量
    public List<Record> getTotal() {
        return Db.use("main2").find("select SUM(a1.channel_num1) arrivedTotal , SUM(a1.channel_num3) saveTotal\n" +
                "from sl_rpt_app_channel_aim1 a1 \n" +
                "where a1.day_no >= '" + startTime + "' and a1.day_no <= '" + endTime + "' and (a1.day_no is not null or a1.day_no <> 0);");
    }

    /**
     * 展示折线图 *
     * *
     */
    //获取到达用户数
    public List<Record> getArrivedNum(String start_time,String end_time,String channel_name,String port_name) {
        return Db.use("main2").find("select  a1.day_no as time,\n" +
                "                           a1.app_channel,\n" +
                "                           SUM(a1.channel_num1) as data1 /*到达用户数*/\n" +
                "                           from sl_rpt_app_channel_aim1  a1 \n" +
                "                           where  a1.app_channel ='"+channel_name+"' and a1.day_no >= '"+start_time+"' and a1.day_no <= '"+end_time+"'\n" +
                "                           and a1.prod_name LIKE '%"+port_name+"%' and(a1.day_no is not null or a1.day_no <> 0)\n" +
                "                           group by a1.day_no,a1.app_channel ;");
    }

    //获取下载用户数
    public List<Record> getDownNum(String start_time,String end_time,String channel_name,String port_name) {
        return Db.use("main2").find("  select  a1.day_no as time,\n" +
                "                           a1.app_channel,\n" +
                "                           SUM(a1.channel_num3) as  data1 /*下载用户数*/\n" +
                "                           from sl_rpt_app_channel_aim1  a1 \n" +
                "                           where  a1.app_channel='"+channel_name+"' and a1.day_no >= '"+start_time+"' and a1.day_no <= '"+end_time+"'\n" +
                "                           and a1.prod_name LIKE '%"+port_name+"%' and(a1.day_no is not null or a1.day_no <> 0)\n" +
                "                           group by a1.day_no,a1.app_channel ;");
    }

    //获取注册用户数
    public List<Record> getRegisterNum(String start_time,String end_time,String channel_name,String port_name) {
        return Db.use("main2").find("  select  a1.day_no as time,\n" +
                "                           a1.app_channel,\n" +
                "                           SUM(a1.channel_num4) as data1 /*注册用户数*/\n" +
                "                           from sl_rpt_app_channel_aim1  a1 \n" +
                "                           where  a1.app_channel='"+channel_name+"' and a1.day_no >= '"+start_time+"' and a1.day_no <= '"+end_time+"'\n" +
                "                           and a1.prod_name LIKE '%"+port_name+"%' and(a1.day_no is not null or a1.day_no <> 0)\n" +
                "                           group by a1.day_no,a1.app_channel ;");
    }


    public List<Record> getAppChannelAim1(String startTime, String endTime) {
        /**
         * totalNum0,totalNum1,totalNum2,totalNum3,totalNum4
         * uavgNum0,uavgNum1,uavgNum2,uavgNum3
         * * * * */
        return Db.find("select a.app_channel,SUM(a.channel_num0) as  totalNum0 , SUM(a.channel_num1) as totalNum1 , " +
                "SUM(a.channel_num2) as totalNum2 ,SUM(a.channel_num3) as totalNum3 ,\n" +
                "SUM(a.channel_num4) as totalNum4 \n" +
                "from strongunion_online.sl_rpt_app_channel_aim1  a \n" +
                "    where  day_no >= '" + startTime + "' and day_no <= '" + endTime + "' and (a.day_no is not null or a.day_no <> 0)\n" +
                "  group by a.app_channel order by a.app_channel DESC;");
    }

    /**
     * * total0,total1，total2,total3,total4
     * uavg0,uavg1,uavg2,uavg3
     * * *
     */
    public List<Record> getCountChannelAim1(String startTime, String endTime) {
        return Db.find("select SUM(b.totalNum0) total0,SUM(b.totalNum1) total1,SUM(b.totalNum2) total2,SUM(b.totalNum3) total3," +
                "SUM(b.totalNum4) total4,TRUNCATE(AVG(b.avgNum0),4) uavg0,TRUNCATE(AVG(b.avgNum1),4) uavg1," +
                "TRUNCATE(AVG(b.avgNum2),4) uavg2,TRUNCATE(AVG(b.avgNum3),4) uavg3 " +
                "from (select a.app_channel,SUM(a.channel_num0) as totalNum0 , " +
                "SUM(a.channel_num1) as totalNum1 , SUM(a.channel_num2) as totalNum2 ," +
                "SUM(a.channel_num3) as totalNum3 ,SUM(a.channel_num4) as totalNum4 ,\n" +
                "round(SUM(a.channel_num1)/SUM(a.channel_num0),4) as avgNum0, round(SUM(a.channel_num2)/SUM(a.channel_num1),4) as  avgNum1,\n" +
                "round(SUM(a.channel_num3)/SUM(a.channel_num2),4) as avgNum2,round(SUM(a.channel_num4)/SUM(a.channel_num3),4) as  avgNum3\n" +
                "  from strongunion_online.sl_rpt_app_channel_aim1  a \n" +
                "    where  day_no >= '" + startTime + "' and day_no <= '" + endTime + "' and (a.day_no is not null or a.day_no <> 0)\n" +
                "  group by a.app_channel order by a.app_channel DESC) as b;");

    }


    //for get PreserveTable
    //NUM3 -->totalNum3
    public List<Record> getPreserveTable(String startTime, String endTime) {
        //获取每个渠道的总数
        return Db.find("select a.app_channel,SUM(a.channel_num3) as totalNum3 \n" +
                "  from strongunion_online.sl_rpt_app_channel_aim1  a \n" +
                "    where  day_no >= '" + startTime + "' and day_no <= '" + endTime + "' and (a.day_no is not null or a.day_no <> 0)\n" +
                "  group by a.app_channel order by a.app_channel DESC;");

    }

    //NUM-->totalNum
    public Record getTotalNum(String startTime, String endTime) {
        //获取渠道的总数
        return Db.find("select SUM(b.totalNum3) totalNum from (select a.app_channel,SUM(a.channel_num3) as totalNum3 \n" +
                "  from strongunion_online.sl_rpt_app_channel_aim1  a \n" +
                "    where  day_no >= '" + startTime + "' and day_no <= '" + endTime + "' and (a.day_no is not null or a.day_no <> 0)\n" +
                "  group by a.app_channel order by a.app_channel DESC) as b;").get(0);
    }

    //for get conversionTable
    public List<Record> getConversionTable(String startTime, String endTime) {
        //获取每个渠道的总数
        return Db.find("select a.app_channel,SUM(a.channel_num0) as totalNum0,SUM(a.channel_num3) as totalNum3 \n" +
                "  from strongunion_online.sl_rpt_app_channel_aim1  a \n" +
                "    where  day_no >= '" + startTime + "' and day_no <= '" + endTime + "' and (a.day_no is not null or a.day_no <> 0)\n" +
                "  group by a.app_channel order by a.app_channel DESC;");

    }

    //for index table 
    public List<Record> getNumByDay(String time) {
        //渠道名称 name
        // num0 到达用户数
        //num1 下载用户数
        //num2 注册用户数
        return Db.find("select a.day_no day,a.app_channel name,a.channel_num1 num0,a.channel_num3 num1,a.channel_num4 num2\n" +
                "  from strongunion_online.sl_rpt_app_channel_aim1  a \n" +
                "        where  a.day_no = '" + time + "' and (a.day_no is not null or a.day_no <> 0);");

    }

//    public List<Record> getCountNum0(String time) {
//        // num0 到达用户数
//        return Db.find("select a.day_no day1 , SUM(a.channel_num0) data1\n" +
//                "  from strongunion_online.sl_rpt_app_channel_aim1  a \n" +
//                "        where  a.day_no = '" + time + "' and (a.day_no is not null or a.day_no <> 0);");
//
//    }

    public List<Record> getCountNum0(String time,String port_name) {
        // num0 到达用户数
        return Db.find("\n" +
                "select '"+time+"' as day1 ,SUM(tt.countNum1) as data1 from (select a1.prod_name, /*产品渠道：如用户端、商户端、管家端*/\n" +
                "       a1.app_channel, /*app推广渠道：指具体某个产品推广的渠道*/\n" +
                "       round(SUM(a1.channel_num1)/count(*),0) as countNum1, /*到达用户数*/\n" +
                "       round(SUM(a1.channel_num3)/count(*),0) as countNum3, /*下载用户数*/\n" +
                "       round(SUM(a1.channel_num4)/count(*),0) as countNum4 /*注册用户数*/\n" +
                "  from strongunion_online.sl_rpt_app_channel_aim1 a1\n" +
                " where a1.day_no = '"+time+"'  and a1.prod_name  LIKE '%"+port_name+"%'\n" +
                "  and (a1.day_no is not null or a1.day_no <> 0)\n" +
                " GROUP BY a1.prod_name, a1.app_channel ORDER BY a1.prod_name DESC) tt;");

    }
    public List<Record> getCountNum1(String time,String port_name) {
        //num1 下载用户数
        return Db.find("select '"+time+"' as day1 ,SUM(tt.countNum3) as data1 from (select a1.prod_name, /*产品渠道：如用户端、商户端、管家端*/\n" +
                "       a1.app_channel, /*app推广渠道：指具体某个产品推广的渠道*/\n" +
                "       round(SUM(a1.channel_num1)/count(*),0) as countNum1, /*到达用户数*/\n" +
                "       round(SUM(a1.channel_num3)/count(*),0) as countNum3, /*下载用户数*/\n" +
                "       round(SUM(a1.channel_num4)/count(*),0) as countNum4 /*注册用户数*/\n" +
                "  from strongunion_online.sl_rpt_app_channel_aim1 a1\n" +
                " where a1.day_no = '"+time+"'  and a1.prod_name  LIKE '%"+port_name+"%'\n" +
                "  and (a1.day_no is not null or a1.day_no <> 0)\n" +
                " GROUP BY a1.prod_name, a1.app_channel ORDER BY a1.prod_name DESC) tt;");

    }
    public List<Record> getCountNum2(String time,String port_name) {
        //num2 注册用户数
        return Db.find("select '"+time+"' as day1 ,SUM(tt.countNum4) as data1 from (select a1.prod_name, /*产品渠道：如用户端、商户端、管家端*/\n" +
                "       a1.app_channel, /*app推广渠道：指具体某个产品推广的渠道*/\n" +
                "       round(SUM(a1.channel_num1)/count(*),0) as countNum1, /*到达用户数*/\n" +
                "       round(SUM(a1.channel_num3)/count(*),0) as countNum3, /*下载用户数*/\n" +
                "       round(SUM(a1.channel_num4)/count(*),0) as countNum4 /*注册用户数*/\n" +
                "  from strongunion_online.sl_rpt_app_channel_aim1 a1\n" +
                " where a1.day_no = '"+time+"'  and a1.prod_name  LIKE '%"+port_name+"%'\n" +
                "  and (a1.day_no is not null or a1.day_no <> 0)\n" +
                " GROUP BY a1.prod_name, a1.app_channel ORDER BY a1.prod_name DESC) tt;");

    }
}
