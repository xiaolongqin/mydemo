package com.szl.stronguion.service.baseoperate;

import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.baseoperate.AppChannelAim1;
import com.szl.stronguion.model.baseoperate.AppPageAim1;
import com.szl.stronguion.utils.FormatUtils;

import java.util.*;

/**
 * Created by Tyfunwang on 2015/6/30.
 */
public class ChannelAim1Serv {
    private AppChannelAim1 aim1 = new AppChannelAim1();
    private AppPageAim1 pageAim1 = new AppPageAim1();


    public Map<String, List<Record>> getThreeTotal(){
        return aim1.getThreeTotal();
    }


    //保存率
    public List<Record> getSaveRate(String prod_name,int type,String startTime,String endTime) {
        if (prod_name.equals("全部")){
            if (type>0){
                String[] str=FormatUtils.getDateTime(type);
                return aim1.getSaveRate(str[0],str[1]);
            }
            return aim1.getSaveRate(startTime,endTime);
        }else {
            if (type>0){
                String[] str=FormatUtils.getDateTime(type);
                return aim1.getSaveRate(prod_name,str[0],str[1]);
            }
            return aim1.getSaveRate(prod_name,startTime,endTime);
        }
    }

    //转化率
    public List<Record> getTransRate(String prod_name,int type,String startTime,String endTime) {
        if (prod_name.equals("全部")){
            if (type>0){
                String[] str=FormatUtils.getDateTime(type);
                return aim1.getTransRate(str[0],str[1]);
            }
            return aim1.getTransRate(startTime,endTime);
        }else {
            if (type>0){
                String[] str=FormatUtils.getDateTime(type);
                return aim1.getTransRate(prod_name,str[0],str[1]);
            }
            return aim1.getTransRate(prod_name,startTime,endTime);
        }
    }

    //   //保存量，到达量
    public List<Record> getTotal() {
        return aim1.getTotal();
    }


    /**
     * 获取主页的表信息
     * * line折线图
     */
    public Map<String,List<Record>> getFirstTable(int type,int pro_name,String startTime,String endTime,String port_name ) {
        String start_time=startTime;
        String end_time=endTime;
        String channel_name=null;
        Map<String,List<Record>> maps = new TreeMap<String,List<Record>>();
        if (type>0){
            String[] time=FormatUtils.getDateTime(type);
            start_time=time[0];
            end_time= time[1];
        }
        int dayCount=FormatUtils.getDayCount(start_time,end_time);
        List<Record>  channel_list=pageAim1.getChannelName(start_time,end_time,port_name);
        if (pro_name == 1) {
            //pageAim1 1 默认情况下也是1 ：pv
            for(Record record:channel_list){
                channel_name=record.getStr("channel_name");
                String app_channel=record.get("app_channel");
                String prod_name=record.getStr("prod_name");
                List<Record> list=new ArrayList<Record>();
                List<Record> list2 = pageAim1.getPv(start_time,end_time,app_channel,prod_name);
                for (int i = dayCount; i > 0; i--) {
                    String time = FormatUtils.getDay(-i);
                    if (type==0){
                        time=FormatUtils.getDayTime(-i,end_time);
                    }
                    boolean flag=false;
                    for(Record r:list2){
                        if (r.get("time").toString().equals(time)){
                            list.add(r);
                            flag=true;
                        }
                    }
                    if (!flag){
                        Record r2=new Record();
                        r2.set("time",time);
                        r2.set("data1",0);
                        list.add(r2);
                    }

                }

                maps.put(channel_name,list);
            }

            return maps;
        }
        if (pro_name == 2) {
            //pageAim1 2 :uv
//            for (int i = dayCount; i > 0; i--) {
//                String time = FormatUtils.getDay(-i);
//                if (type==0){
//                    time=FormatUtils.getDayTime(-i,end_time);
//                }
//                List<Record> list = pageAim1.getUv(time,channel_name,port_name);
//                maps.put(time, list);
//            }

            for(Record record:channel_list){
                channel_name=record.getStr("channel_name");
                String app_channel=record.get("app_channel");
                String prod_name=record.getStr("prod_name");
                List<Record> list=new ArrayList<Record>();
                List<Record> list2 = pageAim1.getUv(start_time, end_time, app_channel, prod_name);
                for (int i = dayCount; i > 0; i--) {
                    String time = FormatUtils.getDay(-i);
                    if (type==0){
                        time=FormatUtils.getDayTime(-i,end_time);
                    }
                    boolean flag=false;
                    for(Record r:list2){
                        if (r.get("time").toString().equals(time)){
                            list.add(r);
                            flag=true;
                        }
                    }
                    if (!flag){
                        Record r2=new Record();
                        r2.set("time",time);
                        r2.set("data1",0);
                        list.add(r2);
                    }

                }

                maps.put(channel_name,list);
            }

            return maps;
        }
        if (pro_name == 3) {
            //AppChannelAim1 3:到达用户数
//            for (int i = dayCount; i > 0; i--) {
//                String time = FormatUtils.getDay(-i);
//                if (type==0){
//                    time=FormatUtils.getDayTime(-i,end_time);
//                }
//                List<Record> list = aim1.getArrivedNum(time);
//                maps.put(time, list);
//            }

            for(Record record:channel_list){
                channel_name=record.getStr("channel_name");
                String app_channel=record.get("app_channel");
                String prod_name=record.getStr("prod_name");
                List<Record> list=new ArrayList<Record>();
                List<Record> list2 = aim1.getArrivedNum(start_time, end_time, app_channel, prod_name);
                for (int i = dayCount; i > 0; i--) {
                    String time = FormatUtils.getDay(-i);
                    if (type==0){
                        time=FormatUtils.getDayTime(-i,end_time);
                    }
                    boolean flag=false;
                    for(Record r:list2){
                        if (r.get("time").toString().equals(time)){
                            list.add(r);
                            flag=true;
                        }
                    }
                    if (!flag){
                        Record r2=new Record();
                        r2.set("time",time);
                        r2.set("data1",0);
                        list.add(r2);
                    }

                }

                maps.put(channel_name,list);
            }
            return maps;
        }
        if (pro_name == 4) {
            //AppChannelAim1 4:下载用户数
//            for (int i = dayCount; i > 0; i--) {
//                String time = FormatUtils.getDay(-i);
//                if (type==0){
//                    time=FormatUtils.getDayTime(-i,end_time);
//                }
//                List<Record> list = aim1.getDownNum(time);
//                maps.put(time, list);
//            }

            for(Record record:channel_list){
                channel_name=record.getStr("channel_name");
                String app_channel=record.get("app_channel");
                String prod_name=record.getStr("prod_name");
                List<Record> list=new ArrayList<Record>();
                List<Record> list2 = aim1.getDownNum(start_time, end_time, app_channel, prod_name);
                for (int i = dayCount; i > 0; i--) {
                    String time = FormatUtils.getDay(-i);
                    if (type==0){
                        time=FormatUtils.getDayTime(-i,end_time);
                    }
                    boolean flag=false;
                    for(Record r:list2){
                        if (r.get("time").toString().equals(time)){
                            list.add(r);
                            flag=true;
                        }
                    }
                    if (!flag){
                        Record r2=new Record();
                        r2.set("time",time);
                        r2.set("data1",0);
                        list.add(r2);
                    }

                }

                maps.put(channel_name,list);
            }

            return maps;
        }
        if (pro_name == 5) {
            //AppChannelAim1 5:注册用户数
//            for (int i = dayCount; i > 0; i--) {
//                String time = FormatUtils.getDay(-i);
//                if (type==0){
//                    time=FormatUtils.getDayTime(-i,end_time);
//                }
//                List<Record> list = aim1.getRegisterNum(time);
//                maps.put(time,list);
//            }
            for(Record record:channel_list){
                channel_name=record.getStr("channel_name");
                String app_channel=record.get("app_channel");
                String prod_name=record.getStr("prod_name");
                List<Record> list=new ArrayList<Record>();
                List<Record> list2 = aim1.getRegisterNum(start_time, end_time, app_channel, prod_name);
                for (int i = dayCount; i > 0; i--) {
                    String time = FormatUtils.getDay(-i);
                    if (type==0){
                        time=FormatUtils.getDayTime(-i,end_time);
                    }
                    boolean flag=false;
                    for(Record r:list2){
                        if (r.get("time").toString().equals(time)){
                            list.add(r);
                            flag=true;
                        }
                    }
                    if (!flag){
                        Record r2=new Record();
                        r2.set("time",time);
                        r2.set("data1",0);
                        list.add(r2);
                    }

                }

                maps.put(channel_name,list);
            }


            return maps;
        }

        return maps;
    }

    /**
     * 获取主页的表信息
     * * table 表
     */
    public Map<String, Map<String, Object>> getSecondTable(int type,int pro_name,String startTime,String endTime,String port_name) {
        String start_time=startTime;
        String end_time=endTime;
        if (type>0){
            String[] time=FormatUtils.getDateTime(type);
            start_time=time[0];
            end_time= time[1];
        }
        int dayCount=FormatUtils.getDayCount(start_time,end_time);
        Map<String, Map<String, Object>> maps = new HashMap<String, Map<String, Object>>();
        if (pro_name == 1) {
            //pageAim1 1
            for (int i = dayCount; i > 0; i--) {
                Map<String, Object> map;
                //获取最后日期，前一天的数据
                String time1 = FormatUtils.getDayTime(-i - 1,end_time);
                List<Record> yesRecord = pageAim1.getCountPv(time1,port_name);

                //当日数据
                String time2 = FormatUtils.getDayTime(-i,end_time);
                List<Record> todayRecord = pageAim1.getCountPv(time2,port_name);

                // 获取上一周同期的数据
                String time3 = FormatUtils.getDayTime(-i - 7,end_time);
                List<Record> lastWeekRecord = pageAim1.getCountPv(time3,port_name);

                //计算数据
                map = countData(yesRecord, todayRecord, lastWeekRecord);
                maps.put(time2, map);
            }
            //修改数据，获取日环比增幅
            maps = modidyCountData( dayCount,maps,end_time);
            return maps;
        }
        if (pro_name == 2) {
            //pageAim1 2
            for (int i = dayCount; i > 0; i--) {
                Map<String, Object> map;
                //获取最后日期，前一天的数据
                String time1 = FormatUtils.getDayTime(-i - 1,end_time);
                List<Record> recordYes = pageAim1.getCountUv(time1,port_name);

                //当日数据
                String time2 = FormatUtils.getDayTime(-i,end_time);
                List<Record> list = pageAim1.getCountUv(time2,port_name);

                // 获取上一周同期的数据
                String time3 = FormatUtils.getDayTime(-i - 7,end_time);
                List<Record> recordWeek = pageAim1.getCountUv(time3,port_name);

                //计算数据
                map = countData(recordYes, list, recordWeek);
                maps.put(time2, map);
            }
            //修改数据，获取日环比增幅
            maps = modidyCountData(dayCount,maps,end_time);
            return maps;
        }
        if (pro_name == 3 ) {
            //channelAim1 3,channel_num0 到达用户数
            for (int i = dayCount; i > 0; i--) {
                Map<String, Object> map;
                //获取最后日期，昨日的数据
                String time1 = FormatUtils.getDayTime(-i - 1,end_time);
                List<Record> recordYes = aim1.getCountNum0(time1,port_name);

                //当日数据
                String time2 = FormatUtils.getDayTime(-i,end_time);
                List<Record> list = aim1.getCountNum0(time2,port_name);

                // 获取上一周同期的数据
                String time3 = FormatUtils.getDayTime(-i - 7,end_time);
                List<Record> recordWeek = aim1.getCountNum0(time3,port_name);

                //计算数据
                map = countData(recordYes, list, recordWeek);
                maps.put(time2, map);
            }
            //修改数据，获取日环比增幅
            maps = modidyCountData(dayCount,maps,end_time);
            return maps;
        }
        if (pro_name == 4 ) {
            //channelAim1 4,channel_num1 下载用户数
            for (int i = dayCount; i > 0; i--) {
                Map<String, Object> map;
                //获取最后日期，昨日的数据
                String time1 = FormatUtils.getDayTime(-i - 1,end_time);
                List<Record> recordYes = aim1.getCountNum1(time1,port_name);

                //当日数据
                String time2 = FormatUtils.getDayTime(-i,end_time);
                List<Record> list = aim1.getCountNum1(time2,port_name);

                // 获取上一周同期的数据
                String time3 = FormatUtils.getDayTime(-i - 7,end_time);
                List<Record> recordWeek = aim1.getCountNum1(time3,port_name);

                //计算数据
                map = countData(recordYes, list, recordWeek);
                maps.put(time2, map);
            }
            //修改数据，获取日环比增幅
            maps = modidyCountData(dayCount,maps,end_time);
            return maps;
        }
        if ( pro_name == 5) {
            //channelAim1 5,channel_num2 注册用户数
            for (int i = dayCount; i > 0; i--) {
                Map<String, Object> map = new HashMap<String, Object>();
                //获取最后日期，昨日的数据
                String time1 = FormatUtils.getDayTime(-i - 1,end_time);
                List<Record> recordYes = aim1.getCountNum2(time1,port_name);

                //当日数据
                String time2 = FormatUtils.getDayTime(-i,end_time);
                List<Record> list = aim1.getCountNum2(time2,port_name);

                // 获取上一周同期的数据
                String time3 = FormatUtils.getDayTime(-i - 7,end_time);
                List<Record> recordWeek = aim1.getCountNum2(time3,port_name);

                //计算数据
                map = countData(recordYes, list, recordWeek);
                maps.put(time2, map);
            }
            //修改数据，获取日环比增幅
            maps = modidyCountData(dayCount,maps,end_time);
            return maps;
        }
        return maps;
    }

    //修改数据，获取日环比增幅
    private Map<String, Map<String, Object>> modidyCountData(int dayCount,Map<String, Map<String, Object>> map,String end_time) {
        for (int i = dayCount; i > 0; i--) {
            //获取最后日期，昨日的数据
            String time1 = FormatUtils.getDayTime(-i - 1,end_time);
            Map<String, Object> map1 = map.get(time1);
            //当日数据
            String time2 = FormatUtils.getDayTime(-i,end_time);
            Map<String, Object> map2 = map.get(time2);
            double c1 ;
            double c2 ;
            try {
                String str1=map1.get("dayChanges").toString();
                c1 = Double.valueOf(str1.substring(0,str1.length()-1));
            } catch (Exception e) {
                c1 = 0.0;
            }
            try {
                c2 =Double.valueOf(map2.get("dayChanges").toString());
            } catch (Exception e) {
                c2 = 0.0;
            }
            //计算日环比增幅
            double add = c2 - c1;
            map2.put("dayOnDayGrowth", String.format("%.2f",add) + "%");
            map2.put("dayChanges", c2 + "%");
        }
        map.remove(FormatUtils.getDay(-(dayCount+1)));
        return map;
    }

    //计算需要的数据
    private Map<String, Object> countData(List<Record> recordYes, List<Record> list, List<Record> recordWeek) {
        String data = "data1";
        String day_no = "day1";
        Map<String, Object> maps = new TreeMap<String, Object>();
        //昨日数据
        double dataYes;
        try {
            Record yes = recordYes.get(0);
            dataYes = yes.getDouble(data);
        } catch (Exception e) {
            dataYes = 0.00;
        }

        //当日数据
        double dataToday;
        String day;
        Record today ;
        try {

            today = list.get(0);
            day = today.getStr(day_no);
            dataToday = today.getDouble(data);
        } catch (Exception e) {
            dataToday = 0.00;
            day = FormatUtils.getDay(-1);
        }

        //上周同日数据
        double dataWeek;
        try {
            Record week = recordWeek.get(0);
            dataWeek = week.getDouble(data);
        } catch (Exception e) {
            dataWeek = 0.00;
        }


        maps.put("day", day);
        maps.put("todayData", dataToday);
        maps.put("yesData", dataYes);

        maps.put("dayGrowth", dataToday - dataYes);//日增长质量
        maps.put("dayOnDayGrowth",0.0);//日环比增幅
        maps.put("weedData", dataWeek);//上周同期

//        日环比变化= （当日-昨日）/昨日 * 100%
//        日增长质量= 当日-昨日
//        日环比增幅= 当日（日环比变化）-昨日（日环比变化）
//        上周同期= 当前时间（当日）- 上周同期（当日）（减7天数据）如：今天是2015-9-25 则，上周同期为2015-9-18
//        周同比变化= （当日-上周同期）/当日 * 100%
//        //dataYes = 0.00
//        if (dataYes == 0.00) {
//            maps.put("dayChanges", 0.00);//日环比变化
//            maps.put("weekChanges", String.format("%.2f", divisionData(dataToday, dataWeek)) + "%");//周同比变化 ((dataToday - dataWeek) / dataWeek) * 100)
//            return maps;
//        }
//
//        //dataToday = 0.00
//        if (dataToday == 0.00) {
//            maps.put("dayChanges", String.format("%.2f", divisionData(dataToday, dataYes)));//日环比变化 ((dataToday - dataYes) / dataYes) * 100)
//            maps.put("weekChanges", String.format("%.2f", divisionData(dataToday, dataWeek)) + "%");//周同比变化
//            return maps;
//        }

//
//        //dataWeek == 0.00
//        if (dataWeek == 0.00) {
//            maps.put("dayChanges", String.format("%.2f", ((dataToday - dataYes) / dataYes) * 100));//日环比变化 ((dataToday - dataYes) / dataYes) * 100)
//            maps.put("weekChanges ", 0.00);//周同比变化
//            return maps;
//        }

        maps.put("dayChanges", String.format("%.2f", divisionData1(dataToday, dataYes)));//日环比变化 ((dataToday - dataYes) / dataYes) * 100)
        maps.put("weekChanges", String.format("%.2f", divisionData(dataToday, dataWeek)) + "%");//周同比变化
        return maps;
    }

    private double divisionData1(double time1, double time2) {
        double result = 0.0;
        if (time2 == 0.0) {
            return result;
        }

        if (time1 > time2) {
            return ((time1 - time2) / time2) * 100;
        }
        return (0 - ((time2 - time1) / time2)) * 100;
    }

    private double divisionData(double time1, double time2) {
        double result = 0.0;
        if (time1 == 0.0) {
            return result;
        }

        if (time1 > time2) {
            return ((time1 - time2) / time1) * 100;
        }
        return (0 - ((time2 - time1) / time1)) * 100;
    }
}
