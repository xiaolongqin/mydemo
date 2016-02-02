package utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Tyfunwang on 2015/1/22.
 */
public class FormatUtils {

    private static DateFormat df2D = new SimpleDateFormat("yyyy年MM月dd日");

    private FormatUtils() {
    }

    /**
     * @param day 天数（如果day数为负数,说明是此日期前的天数）
     *            * *
     */
    public static String getDay(int day) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, day);
        return dateFormat.format(calendar.getTime());
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

    /**
     * 通过dateType 返回开始结束时间数组*
     *
     * @param dateType dateType 1：昨天，2：最近三天，3：最近7天，4：最近一个月，5：最近一季度,6:最近14天*
     * @return
     */
    public static String[] getDateTime(int dateType) {
        String[] str = new String[2];
        switch (dateType) {
            case 1://昨天
                str[0] = getDay(-1);//startTime
                str[1] = getDay(0);//endTime
                break;
            case 2:   //最近三天
                str[0] = getDay(-3);
                str[1] = getDay(-1);
                break;
            case 3: //最近7天
                str[0] = getDay(-7);
                str[1] = getDay(-1);
                break;
            case 4: //最近一个月
                str[0] = getDay(-30);
                str[1] = getDay(-1);
                break;
            case 5: //最近一季度
                str[0] = getDay(-90);
                str[1] = getDay(-1);
                break;
            case 6:  //最近14天
                str[0] = getDay(-14);
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
