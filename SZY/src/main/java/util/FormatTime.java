package util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Tyfunwang on 2014/12/4.
 */
public class FormatTime {

    public static String time2String(Timestamp ts){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(ts);
    }
    public static String time2StringMs(Timestamp ts){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:ms");
        return dateFormat.format(ts);
    }
}
