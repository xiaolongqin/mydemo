package sl.bigdata.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Tyfunwang on 2015/5/29.
 */
public class GetMonth {
    public static String  getMonth(){
        DateFormat format = new SimpleDateFormat("yyyyMM");
        long time = new Date().getTime();
        return format.format(time);
    }

    public static String  getOrtherMonth(int i){

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, i);
        DateFormat format = new SimpleDateFormat("yyyyMM");
        long time = cal.getTime().getTime();
        return format.format(time);
    }

    public static Date  getNextDayTime(int minute){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return  cal.getTime();
    }

}
