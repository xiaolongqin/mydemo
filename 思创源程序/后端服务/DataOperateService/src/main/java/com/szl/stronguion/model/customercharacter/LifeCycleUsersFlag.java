package com.szl.stronguion.model.customercharacter;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Tyfunwang on 2015/7/15.
 */
public class LifeCycleUsersFlag extends Model<LifeCycleUsersFlag> {
    private final static String[] hourArray = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};

    /**
     * 客户习惯分析*
     */
    public List<Record> getHabitDao(String typeName, String startTime, String endTime) {
        return Db.use("main2").find("select u1.times, (case when u2.times is null then 0 else u2.num1 end) as num1\n" +
                " from \n" +
                "(select '00' as 'times'\n" +
                "union all\n" +
                "select '01' as 'times'\n" +
                "union all\n" +
                "select '02' as 'times'\n" +
                "union all\n" +
                "select '03' as 'times'\n" +
                "union all\n" +
                "select '04' as 'times'\n" +
                "union all \n" +
                "select '05' as 'times'\n" +
                "union all\n" +
                "select '06' as 'times'\n" +
                "union all \n" +
                "select '07' as 'times'\n" +
                "union all\n" +
                "select '08' as 'times'\n" +
                "union all\n" +
                "select '09' as 'times'\n" +
                "union all\n" +
                "select '10' as 'times'\n" +
                "union all\n" +
                "select '11' as 'times'\n" +
                "union all\n" +
                "select '12' as 'times'\n" +
                "union all\n" +
                "select '13' as 'times'\n" +
                "union all\n" +
                "select '14' as 'times'\n" +
                "union all\n" +
                "select '15' as 'times'\n" +
                "union all\n" +
                "select '16' as 'times'\n" +
                "union all\n" +
                "select '17' as 'times'\n" +
                "union all\n" +
                "select '18' as 'times'\n" +
                "union all\n" +
                "select '19' as 'times'\n" +
                "union all\n" +
                "select '20' as 'times'\n" +
                "union all\n" +
                "select '21' as 'times'\n" +
                "union all\n" +
                "select '22' as 'times'\n" +
                "union all\n" +
                "select '23' as 'times') u1\n" +
                "LEFT JOIN \n" +
                " (select FROM_UNIXTIME(t.pagestart_time, '%H') as 'times',\n" +
                "        sum(t." + typeName + ") as 'num1'\t\t\n" +
                "   from sl_rpt_life_cycle_users_flag t\n" +
                "  where t." + typeName + " in (1)\n" +
                "    and FROM_UNIXTIME(t.pagestart_time, '%Y%m%d') >= '" + startTime + "'\n" +
                "    and FROM_UNIXTIME(t.pagestart_time, '%Y%m%d') <= '" + endTime + "'\n" +
                "    and (t.pagestart_time is not null or t.pagestart_time <> 0)\n" +
                "\t\tGROUP BY times ) u2 on u1.times = u2.times;");

    }

    public List<Record> getHabitTop5(String typeName, String startTime, String endTime) {
        return Db.use("main2").find("select u1.times, (case when u2.times is null then 0 else u2.num1 end) as num1\n" +
                " from \n" +
                "(select '00' as 'times'\n" +
                "union all\n" +
                "select '01' as 'times'\n" +
                "union all\n" +
                "select '02' as 'times'\n" +
                "union all\n" +
                "select '03' as 'times'\n" +
                "union all\n" +
                "select '04' as 'times'\n" +
                "union all \n" +
                "select '05' as 'times'\n" +
                "union all\n" +
                "select '06' as 'times'\n" +
                "union all \n" +
                "select '07' as 'times'\n" +
                "union all\n" +
                "select '08' as 'times'\n" +
                "union all\n" +
                "select '09' as 'times'\n" +
                "union all\n" +
                "select '10' as 'times'\n" +
                "union all\n" +
                "select '11' as 'times'\n" +
                "union all\n" +
                "select '12' as 'times'\n" +
                "union all\n" +
                "select '13' as 'times'\n" +
                "union all\n" +
                "select '14' as 'times'\n" +
                "union all\n" +
                "select '15' as 'times'\n" +
                "union all\n" +
                "select '16' as 'times'\n" +
                "union all\n" +
                "select '17' as 'times'\n" +
                "union all\n" +
                "select '18' as 'times'\n" +
                "union all\n" +
                "select '19' as 'times'\n" +
                "union all\n" +
                "select '20' as 'times'\n" +
                "union all\n" +
                "select '21' as 'times'\n" +
                "union all\n" +
                "select '22' as 'times'\n" +
                "union all\n" +
                "select '23' as 'times') u1\n" +
                "LEFT JOIN \n" +
                " (select FROM_UNIXTIME(t.pagestart_time, '%H') as 'times',\n" +
                "        sum(t." + typeName + ") as 'num1'\t\t\n" +
                "   from sl_rpt_life_cycle_users_flag t\n" +
                "  where t." + typeName + " in (1)\n" +
                "    and FROM_UNIXTIME(t.pagestart_time, '%Y%m%d') >= '" + startTime + "'\n" +
                "    and FROM_UNIXTIME(t.pagestart_time, '%Y%m%d') <= '" + endTime + "'\n" +
                "    and (t.pagestart_time is not null or t.pagestart_time <> 0)\n" +
                "\t\tGROUP BY times ) u2 on u1.times = u2.times\n" +
                "\t\tORDER BY num1 DESC LIMIT 0,5;");

    }

    public List<Record> getHabitAllDao(String startTime, String endTime) {
        return Db.use("main2").find("select u1.times, (case when u2.times is null then 0 else u2.num1 end) as num1\n" +
                " from \n" +
                "(select '00' as 'times'\n" +
                "union all\n" +
                "select '01' as 'times'\n" +
                "union all\n" +
                "select '02' as 'times'\n" +
                "union all\n" +
                "select '03' as 'times'\n" +
                "union all\n" +
                "select '04' as 'times'\n" +
                "union all \n" +
                "select '05' as 'times'\n" +
                "union all\n" +
                "select '06' as 'times'\n" +
                "union all \n" +
                "select '07' as 'times'\n" +
                "union all\n" +
                "select '08' as 'times'\n" +
                "union all\n" +
                "select '09' as 'times'\n" +
                "union all\n" +
                "select '10' as 'times'\n" +
                "union all\n" +
                "select '11' as 'times'\n" +
                "union all\n" +
                "select '12' as 'times'\n" +
                "union all\n" +
                "select '13' as 'times'\n" +
                "union all\n" +
                "select '14' as 'times'\n" +
                "union all\n" +
                "select '15' as 'times'\n" +
                "union all\n" +
                "select '16' as 'times'\n" +
                "union all\n" +
                "select '17' as 'times'\n" +
                "union all\n" +
                "select '18' as 'times'\n" +
                "union all\n" +
                "select '19' as 'times'\n" +
                "union all\n" +
                "select '20' as 'times'\n" +
                "union all\n" +
                "select '21' as 'times'\n" +
                "union all\n" +
                "select '22' as 'times'\n" +
                "union all\n" +
                "select '23' as 'times') u1\n" +
                "LEFT JOIN \n" +
                " (select FROM_UNIXTIME(t.pagestart_time, '%H') as 'times',\n" +
                "        sum(t.uid_consume_flag+t.uid_active_flag+t.uid_silence_flag) as 'num1'\t\t\n" +
                "   from sl_rpt_life_cycle_users_flag t\n" +
                "  where (t.uid_consume_flag in (1)) or (t.uid_active_flag in (1)) or (t.uid_silence_flag in (1))\n" +
                "    and FROM_UNIXTIME(t.pagestart_time, '%Y%m%d') >= '" + startTime + "'\n" +
                "    and FROM_UNIXTIME(t.pagestart_time, '%Y%m%d') <= '" + endTime + "'\n" +
                "    and (t.pagestart_time is not null or t.pagestart_time <> 0)\n" +
                "\t\tGROUP BY times ) u2 on u1.times = u2.times;");

    }

    public List<Record> getHabitAllTop5(String startTime, String endTime) {
        return Db.use("main2").find("select u1.times, (case when u2.times is null then 0 else u2.num1 end) as num1\n" +
                " from \n" +
                "(select '00' as 'times'\n" +
                "union all\n" +
                "select '01' as 'times'\n" +
                "union all\n" +
                "select '02' as 'times'\n" +
                "union all\n" +
                "select '03' as 'times'\n" +
                "union all\n" +
                "select '04' as 'times'\n" +
                "union all \n" +
                "select '05' as 'times'\n" +
                "union all\n" +
                "select '06' as 'times'\n" +
                "union all \n" +
                "select '07' as 'times'\n" +
                "union all\n" +
                "select '08' as 'times'\n" +
                "union all\n" +
                "select '09' as 'times'\n" +
                "union all\n" +
                "select '10' as 'times'\n" +
                "union all\n" +
                "select '11' as 'times'\n" +
                "union all\n" +
                "select '12' as 'times'\n" +
                "union all\n" +
                "select '13' as 'times'\n" +
                "union all\n" +
                "select '14' as 'times'\n" +
                "union all\n" +
                "select '15' as 'times'\n" +
                "union all\n" +
                "select '16' as 'times'\n" +
                "union all\n" +
                "select '17' as 'times'\n" +
                "union all\n" +
                "select '18' as 'times'\n" +
                "union all\n" +
                "select '19' as 'times'\n" +
                "union all\n" +
                "select '20' as 'times'\n" +
                "union all\n" +
                "select '21' as 'times'\n" +
                "union all\n" +
                "select '22' as 'times'\n" +
                "union all\n" +
                "select '23' as 'times') u1\n" +
                "LEFT JOIN \n" +
                " (select FROM_UNIXTIME(t.pagestart_time, '%H') as 'times',\n" +
                "        sum(t.uid_consume_flag+t.uid_active_flag+t.uid_silence_flag) as 'num1'\t\t\n" +
                "   from sl_rpt_life_cycle_users_flag t\n" +
                "  where (t.uid_consume_flag in (1)) or (t.uid_active_flag in (1)) or (t.uid_silence_flag in (1))\n" +
                "    and FROM_UNIXTIME(t.pagestart_time, '%Y%m%d') >= '" + startTime + "'\n" +
                "    and FROM_UNIXTIME(t.pagestart_time, '%Y%m%d') <= '" + endTime + "'\n" +
                "    and (t.pagestart_time is not null or t.pagestart_time <> 0)\n" +
                "\t\tGROUP BY times ) u2 on u1.times = u2.times\n" +
                "GROUP BY times ORDER BY num1 DESC LIMIT 0,5;");

    }

    public List<Map<Integer, Object>> getConsume(String startTime, String endTime) {
        List<Map<Integer, Object>> list = new ArrayList<Map<Integer, Object>>();
        //消费客户
        for (int i = 0; i < hourArray.length; i++) {
            Map<Integer, Object> maps = new TreeMap<Integer, Object>();
            Record record = Db.use("main2").findFirst(" select sum(case when substr(FROM_UNIXTIME(t.pagestart_time, '%Y-%m-%d %H:%i:%S'),12,2) = '" + hourArray[i] + "' then 1 else 0 end) as num" + i + "\n" +
                    "   from sl_rpt_life_cycle_users_flag t\n" +
                    "  where t.uid_consume_flag in (1)\n" +
                    "    and FROM_UNIXTIME(t.pagestart_time, '%Y%m%d') >= '" + startTime + "'\n" +
                    "    and FROM_UNIXTIME(t.pagestart_time, '%Y%m%d') <= '" + endTime + "'\n" +
                    "    and (t.pagestart_time is not null or t.pagestart_time <> 0);");
            maps.put(i, record.get("num" + i + ""));
            list.add(maps);
        }
        return list;
    }

    public List<Map<Integer, Object>> getActived(String startTime, String endTime) {
        List<Map<Integer, Object>> list = new ArrayList<Map<Integer, Object>>();
        //活跃客户
        for (int i = 0; i < hourArray.length; i++) {
            Map<Integer, Object> maps = new TreeMap<Integer, Object>();
            Record record = Db.use("main2").findFirst(" select sum(case when substr(FROM_UNIXTIME(t.pagestart_time, '%Y-%m-%d %H:%i:%S'),12,2) = '" + hourArray[i] + "' then 1 else 0 end) as num" + i + "\n" +
                    "   from sl_rpt_life_cycle_users_flag t\n" +
                    "  where t.uid_active_flag in (1)\n" +
                    "    and FROM_UNIXTIME(t.pagestart_time, '%Y%m%d') >= '" + startTime + "'\n" +
                    "    and FROM_UNIXTIME(t.pagestart_time, '%Y%m%d') <= '" + endTime + "'\n" +
                    "    and (t.pagestart_time is not null or t.pagestart_time <> 0);");
            maps.put(i, record.get("num" + i + ""));
            list.add(maps);
        }
        return list;
    }

    public List<Map<Integer, Object>> getSilence(String startTime, String endTime) {
        List<Map<Integer, Object>> list = new ArrayList<Map<Integer, Object>>();
        //沉默客户
        for (int i = 0; i < hourArray.length; i++) {
            Map<Integer, Object> maps = new TreeMap<Integer, Object>();
            Record record = Db.use("main2").findFirst(" select sum(case when substr(FROM_UNIXTIME(t.pagestart_time, '%Y-%m-%d %H:%i:%S'),12,2) = '" + hourArray[i] + "' then 1 else 0 end) as num" + i + "\n" +
                    "   from sl_rpt_life_cycle_users_flag t\n" +
                    "  where t.uid_silence_flag in (1)\n" +
                    "    and FROM_UNIXTIME(t.pagestart_time, '%Y%m%d') >= '" + startTime + "'\n" +
                    "    and FROM_UNIXTIME(t.pagestart_time, '%Y%m%d') <= '" + endTime + "'\n" +
                    "    and (t.pagestart_time is not null or t.pagestart_time <> 0); ");
            maps.put(i, record.get("num" + i + ""));
            list.add(maps);
        }
        return list;
    }

    public List<Map<Integer, Object>> getAll(String startTime, String endTime) {
        List<Map<Integer, Object>> list = new ArrayList<Map<Integer, Object>>();
        //全部客户
        for (int i = 0; i < hourArray.length; i++) {
            Map<Integer, Object> maps = new TreeMap<Integer, Object>();
            Record record = Db.use("main2").findFirst(" select sum(case when substr(FROM_UNIXTIME(t.pagestart_time, '%Y-%m-%d %H:%i:%S'),12,2) = '" + hourArray[i] + "' then 1 else 0 end) as num" + i + "\n" +
                    "   from sl_rpt_life_cycle_users_flag t\n" +
                    "  where FROM_UNIXTIME(t.pagestart_time, '%Y%m%d') >= '" + startTime + "'\n" +
                    "    and FROM_UNIXTIME(t.pagestart_time, '%Y%m%d') <= '" + endTime + "'\n" +
                    "    and (t.pagestart_time is not null or t.pagestart_time <> 0);");
            maps.put(i, record.get("num" + i + ""));
            list.add(maps);
        }
        return list;
    }
}
