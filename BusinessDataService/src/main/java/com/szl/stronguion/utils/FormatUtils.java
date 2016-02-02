package com.szl.stronguion.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 小龙
 * on 15-10-15
 * at 下午4:28.
 */
public class FormatUtils {

    private static DateFormat df2D = new SimpleDateFormat("yyyy年MM月dd日");

    private FormatUtils() {
    }


    /**
     * 获取当前时间yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getDate() {
        Date date=new Date();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    /**
     * @param day 天数（如果day数为负数,说明是此日期前的天数）
     *            * *
     */
    public static String getDay(int day) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, day);
        return dateFormat.format(calendar.getTime());
    }
    //获取日期的天数
    public static int getDayCount(String strstart,String strend){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date_start = dateFormat.parse(strstart);
            Date date_end = dateFormat.parse(strend);
            long count=(date_end.getTime()-date_start.getTime())/(1000 * 60 * 60 * 24)+1;
            return  (int)count;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    //获取自选日期某一天的时间
    public static String getDayTime(int i,String strend){

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date_end = dateFormat.parse(strend);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date_end);
            calendar.add(Calendar.DAY_OF_YEAR, i+1);
            String theday=dateFormat.format(calendar.getTime());
            return theday;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 日转timestamp
     * * *
     */
    public static Long getDayDate(int day) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, day);
        Date dataTime = null;
        try {
            dataTime = dateFormat.parse(dateFormat.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dataTime == null ? null : dataTime.getTime() / 1000;
    }

    /**
     * @param month 月份（如果day数为负数,说明是此日期前的月份）
     */
    public static String getMonth(int month) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, month);
        return dateFormat.format(calendar.getTime());
    }

//    /**
//     * 通过dateType 返回开始结束时间数组*
//     *
//     * @param dateType dateType 1：昨天，2：最近三天，3：最近7天，4：最近一个月，5：最近一季度,6:最近14天*
//     * @return
//     */
//    public static String[] getDateTime(int dateType) {
//        String[] str = new String[2];
//        switch (dateType) {
//            case 1://昨天
//                str[0] = getDay(-1);//startTime
//              //tf  str[1] = getDay(0);//endTime
//                str[1] = getDay(-1);//endTime
//                break;
//            case 2:   //最近三天
//                str[0] = getDay(-3);
//                str[1] = getDay(-1);
//                break;
//            case 3: //最近7天
//                str[0] = getDay(-7);
//                str[1] = getDay(-1);
//                break;
//            case 4: //最近一个月
//                str[0] = getDay(-30);
//                str[1] = getDay(-1);
//                break;
//            case 5: //最近一季度
//                str[0] = getDay(-90);
//                str[1] = getDay(-1);
//                break;
//            case 6:  //最近14天
//                str[0] = getDay(-14);
//                str[1] = getDay(-1);
//                break;
//        }
//        return str;
//    }

    /**
     * 通过dateType 返回开始结束时间数组*
     *
     * @param dateType dateType 1：最近7天，2：最近一个月，
     * @return
     */
    public static String[] getDateTime(int dateType) {
        String[] str = new String[2];
        switch (dateType) {
            case 3: //最近7天
                str[0] = getDay(-7);
                str[1] = getDay(-1);
                break;
            case 2: //最近一个月
                str[0] = getDay(-30);
                str[1] = getDay(-1);
                break;
            case 1: //最近一季度
                str[0] = getDay(-90);
                str[1] = getDay(-1);
                break;
        }
        return str;
    }
    //时间戳转年月日
    public static String time2String(long ts) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(new Date(ts*1000));
    }
    /**
     * @param ts 小时
     */
    public static String hour2String(long ts) {
        DateFormat dateFormat = new SimpleDateFormat("HH");
        return dateFormat.format(ts);
    }

    public static String format2D(long time) {
        if (time == 0) return "";
        Date date = new Date();
        date.setTime(time);
        return df2D.format(date);
    }

    /**
     * 16进制字符串转字符串
     * @param src
     * @return
     */
    public static String encodeString16(String src) {
        try {
            String temp = "";
            for (int i = 0; i < src.length() / 2; i++) {
                temp = temp
                        + (char) Integer.valueOf(src.substring(i * 2, i * 2 + 2),
                        16).byteValue();
            }
            return temp;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }



    public static String buildDurationStr(long time) {
        StringBuilder sb = new StringBuilder();
        long yeartime = 365 * 24 * 60 * 60 * 1000L;
        long daytime = 24 * 60 * 60 * 1000L;
        long hourtime = 60 * 60 * 1000L;
        long minutetime = 60 * 60 * 1000L;
        long secondtime = 60 * 1000L;

        long year = time / yeartime;
        time = time % yeartime;
        if (year > 0) sb.append(year).append("年");

        long day = time / daytime;
        time = time % daytime;
        if (day > 0) sb.append(day).append("天");

        long hour = time / hourtime;
        time = time % hourtime;
        if (hour > 0) sb.append(hour).append("小时");

        long minutes = time / minutetime;
        time = time % minutetime;
        if (minutes > 0) sb.append(minutes).append("分钟");

        long seconds = time / secondtime;
        if (year > 0) sb.append(seconds).append("秒");
        return sb.toString();
    }

    /**
     * 对获取到的 XX年-XX月-XX日 格式日期数组转换成 XX年-月-日 00:00:00 至 XX年-月-日 23:59:59
      */

    public static String[] formatDetail(String[] time) {
        String[] time1=time;
        if (time1!=null){
            time[0]=time[0]+" 00:00:00";
            time[1]=time[1]+" 23:59:59";
        }
        return time1;
    }

    /**
     * 转化‘1234567891‘的数字为'1,234,567,891'格式
     * @param str1
     * @return
     */
    public  static String dataChange(String str1){
        String total_str=str1;
        int  total_lenth=total_str.length();
        String[]  t=new String[total_lenth/3+1];
        String str="";

        if (total_lenth>3){
            for(int i=0;i<total_lenth/3+1;i++){
                int index=(total_lenth-3*(i+1))>=0?(total_lenth-3*(i+1)):0;
                int end=(total_lenth-i*3)>=0?total_lenth-i*3:0;
                t[i]=total_str.substring(index,end);
            }
            for (int i=t.length-1;i>=0;i--){
                if (i>0){
                    str+=t[i]+",";
                }else {
                    str+=t[i];
                }
            }
        }else {
            str=total_str;
        }

        if (total_lenth/3.0==total_lenth/3&&total_lenth>3){
            str=str.substring(1,str.length());
        }
        return  str;
    }


    /**
     * 项目中渠道拼接
     * @param channel
     * @return
     */

    public static StringBuilder getChannelStr(String[] channel){
        StringBuilder sb =new StringBuilder(" ");
        if (channel!=null&&channel.length!=0){
            sb.append(" and (squdao=");
            for (int i=0;i<channel.length;i++){
                if (i<channel.length-1){
                    sb.append("'"+channel[i]+"'").append(" or squdao= ");
                }else {
                    sb.append("'"+channel[i]+"'").append(") ");
                }
            }
        }
        return sb;
    }

}
