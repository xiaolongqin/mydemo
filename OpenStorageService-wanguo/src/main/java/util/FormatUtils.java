package util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tyfunwang on 2015/1/22.
 */
public class FormatUtils {

    static String s = "yyyy年MM月dd日 HH时mm分ss秒";
    private static DateFormat df2s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static DateFormat df2D = new SimpleDateFormat("yyyy年MM月dd日");

    private FormatUtils() {
    }


    public static String time2String(Timestamp ts) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        return dateFormat.format(ts);
    }

    public static String time2String(Long ts) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        return dateFormat.format(ts);
    }

    public static String time2StringMs(Timestamp ts) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:ms");
        return dateFormat.format(ts);
    }


    public static String format2S(long time) {
        if (time == 0) return "";
        Date date = new Date();
        date.setTime(time);
        return df2s.format(date);
    }
    public static float formatSpace(float space) {

        return space/(1000*1000*1000);
    }

    public static String format2D(long time) {
        if (time == 0) return "";
        Date date = new Date();
        date.setTime(time);
        return df2D.format(date);
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
}
