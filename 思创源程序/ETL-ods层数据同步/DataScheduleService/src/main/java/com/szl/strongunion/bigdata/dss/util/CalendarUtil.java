package com.szl.strongunion.bigdata.dss.util;

import java.util.Calendar;

/**
 * Created by 小龙
 * on 15-7-17
 * at 上午10:39.
 */
public class CalendarUtil {
    private CalendarUtil() {
    }

    public static String getMonthPostFix() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 2;
        if (month==13){
            return "_"+((year+1) * 100 + 1);
        }
        return "_"+(year * 100 + month);
    }

    public static String getPrevMonthPostFix() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        if (month == 0) {
            year -= 1;
            month = 12;
        }
        return "_"+(year * 100 + month);
    }
}
