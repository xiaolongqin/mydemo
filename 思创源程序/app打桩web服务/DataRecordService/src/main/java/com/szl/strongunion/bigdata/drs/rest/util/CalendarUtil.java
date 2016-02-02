package com.szl.strongunion.bigdata.drs.rest.util;

import java.util.Calendar;

/**
 * Created by liweiqi on 2015/5/27.
 */
public class CalendarUtil {
    private CalendarUtil() {
    }

    public static String getMonthPostFix() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
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
