package com.szl.stronguion.model.customercharacter;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Tyfunwang on 2015/7/14.
 */
public class LifeCycleUsers extends Model<LifeCycleUsers> {
    public static final String YESTORDAY = "yestorday";
    public static final String COUNTUID = "countUid";
    private static LifeCycleUsers dao = new LifeCycleUsers();
    private final static int[][] ageArray = {{18}, {18, 24}, {25, 29}, {30, 34}, {35, 39}, {40, 49}, {50, 59}, {60}};

    /**
     * 最近几天的生日祝福*
     * *
     */
    public Record getCountUid(int start, int end) {
        if (end == 0) {
            //昨天
            return Db.use("main2").findFirst("select COUNT(uid) countUid from sl_rpt_life_cycle_users \n" +
                    "\t\twhere  birth_away_from_flag = '" + start + "' ;");
        }
        return Db.use("main2").findFirst("select COUNT(uid) countUid from sl_rpt_life_cycle_users \n" +
                "\t\twhere  birth_away_from_flag > '" + start + "' and birth_away_from_flag <= '" + end + "'; ");
    }

    public List<Record> getPersonInfo(int start, int end, String param) {
        if (end == 0) {
            return Db.use("main2").find("select uid,name,phone,province,city,town,village,address,birth_away_from_flag \n" +
                    "from sl_rpt_life_cycle_users \n" +
                    "where birth_away_from_flag = '1'  and (city like '%" + param + "%' or province like '%" + param + "%' or phone like '%" + param + "%' " +
                    " or town like '%" + param + "%' or village like '%" + param + "%' or address like '%" + param + "%') ORDER BY uid;");
        }
        return Db.use("main2").find("select uid,name,phone,province,city,town,village,address,birth_away_from_flag \n" +
                "from sl_rpt_life_cycle_users \n" +
                "where (birth_away_from_flag > '" + start + "' and birth_away_from_flag <='" + end + "') " +
                "and (city like '%" + param + "%' or province like '%" + param + "%' or phone like '%" + param + "%' " +
                "or town like '%" + param + "%' or village like '%" + param + "%' or address like '%" + param + "%') ORDER BY uid;");
    }

    public Record blessedBirth(int start, int end) {
        //已经过生
        if (end == 0) {
            return Db.use("main2").findFirst("select COUNT(uid) " + COUNTUID + " from sl_rpt_life_cycle_users \n" +
                    "\t\twhere birth_flag = '1' and birth_away_from_flag = '" + start + "'; ");
        }
        return Db.use("main2").findFirst("select COUNT(uid)  " + COUNTUID + " from sl_rpt_life_cycle_users \n" +
                "\t\twhere birth_flag = '1' and birth_away_from_flag >= '" + start + "' and birth_away_from_flag <='" + end + "'; ");
    }

    public Record willBlessBirth(int start, int end) {
        //将要过生
        if (end == 0) {
            return Db.use("main2").findFirst("select COUNT(uid)  " + COUNTUID + " from sl_rpt_life_cycle_users \n" +
                    "\t\twhere birth_flag = '0' and birth_away_from_flag = '" + start + "'; ");
        }
        return Db.use("main2").findFirst("select COUNT(uid) " + COUNTUID + " from sl_rpt_life_cycle_users \n" +
                "\t\twhere birth_flag = '0' and birth_away_from_flag >= '" + start + "' and birth_away_from_flag <='" + end + "'; ");
    }

    /**
     * tools methods
     *
     * @param startTime
     * @param endTime
     * @param sex
     * @return Record
     */
    public Record getTotalBySex(String startTime, String endTime, int sex) {
        if (sex == 1) {
            //男
            return Db.use("main2").findFirst("select COUNT(*) yestorday1 from sl_rpt_life_cycle_users where sex = '1' " +
                    "and FROM_UNIXTIME(reg_time,'%Y%m%d') >='" + startTime + "' and FROM_UNIXTIME(reg_time,'%Y%m%d') <='" + endTime + "';");
        }
        //女
        return Db.use("main2").findFirst("select COUNT(*) yestorday0 from sl_rpt_life_cycle_users where sex = '0' " +
                "and FROM_UNIXTIME(reg_time,'%Y%m%d') >='" + startTime + "' and FROM_UNIXTIME(reg_time,'%Y%m%d') <='" + endTime + "';");
    }

    public Record getTotalByTime(String startTime, String endTime) {
        return Db.use("main2").findFirst("select COUNT(*) yestorday from sl_rpt_life_cycle_users  " +
                "where  FROM_UNIXTIME(reg_time,'%Y%m%d') >='" + startTime + "' and FROM_UNIXTIME(reg_time,'%Y%m%d') <='" + endTime + "';");
    }

    public Map<String, Object> getTotalByAge(String startTime, String endTime, int sex) {
        Map<String, Object> rightMaps = new TreeMap<String, Object>();
        for (int i = 0; i < ageArray.length; i++) {
            int[] s1 = ageArray[i];
            if (s1.length == 1) {
                int old = s1[0];
                if (old == 18) {
                    //18以下
                    Record record = getCountByAge(startTime, endTime, i + 1, sex, 18, 0);
                    rightMaps.put("total" + (i + 1), record.get("total" + (i + 1)));
                }
                if (old == 60) {
                    //60 及以上
                    Record record = getCountByAge(startTime, endTime, i + 1, sex, 60, 0);
                    rightMaps.put("total" + (i + 1), record.get("total" + (i + 1)));
                }
            }

            //其他年龄段
            if (s1.length == 2) {
                int old1 = s1[0];
                int old2 = s1[1];
                Record record = getCountByAge(startTime, endTime, i + 1, sex, old1, old2);
                rightMaps.put("total" + (i + 1), record.get("total" + (i + 1)));
            }
        }
        return rightMaps;

    }

    public Record getCountByAge(String startTime, String endTime, int i, int sex, int age1, int age2) {
        String totals = "total" + i;

        if (age2 == 0 && age1 == 18) {
            //18岁
            return Db.use("main2").findFirst("select COUNT(*) " + totals + "  from sl_rpt_life_cycle_users " +
                    "where sex = '" + sex + "'  and FROM_UNIXTIME(reg_time,'%Y%m%d') >='" + startTime + "'  and FROM_UNIXTIME(reg_time,'%Y%m%d') <='" + endTime + "' and age < '" + age1 + "';");
        }
        if (age2 == 0 && age1 == 60) {
            //60岁及以上
            return Db.use("main2").findFirst("select COUNT(*) " + totals + " from sl_rpt_life_cycle_users " +
                    "where sex = '" + sex + "'  and FROM_UNIXTIME(reg_time,'%Y%m%d') >='" + startTime + "' and FROM_UNIXTIME(reg_time,'%Y%m%d') <='" + endTime + "' and age >= '" + age1 + "';");
        }

        //其他年龄段
        return Db.use("main2").findFirst("select COUNT(*) " + totals + " from sl_rpt_life_cycle_users  " +
                "where sex = '" + sex + "' and FROM_UNIXTIME(reg_time,'%Y%m%d') >='" + startTime + "' and FROM_UNIXTIME(reg_time,'%Y%m%d') <='" + endTime + "' and age >= '" + age1 + "' and age <= '" + age2 + "';");
    }

    /**
     * 用户画像搜索*
     * * *
     */
    public List<Record> searchUsers(String param, int sex) {
        return Db.use("main2").find("select t.uid,t.phone,t.name ,t.sex\n" +
                "\t\tfrom sl_rpt_life_cycle_users t where (t.sex = '" + sex + "' and  t.phone LIKE '%" + param + "%')  " +
                "or (t.name like '%" + param + "%' and t.sex = '" + sex + "' )\n" +
                "\t\tGROUP BY t.name,t.phone  \n" +
                "\tORDER BY t.orders_total_price desc LIMIT 10;");
    }

    //用户基本信息
    public Record getBaseInfo(int uid) {
        return Db.use("main2").findFirst("select t.phone as phone, /*手机号码*/\n" +
                "       t.name as name, /*姓名*/\n" +
                "       (case when t.sex in (1) then '男' when t.sex in (0) then '女' else '未知' end) as sex, /*性别*/\n" +
                "       t.age as age, /*年龄*/\n" +
                "       t.birth as birth, /*出生年月*/\n" +
                "       t.address as address, /*注册地址*/\n" +
                "       t.birth_zeros_flag as birth_zeros_flag, /*出生几零后标示。如：60,70,80,90,00*/\n" +
                "       FROM_UNIXTIME(t.reg_time, '%Y%m%d') as reg_time, /*注册时间*/\n" +
                "       t.email as email, /*注册邮箱*/\n" +
                "       t.levelname as levelname, /*会员等级（称号）*/ \n" +
                "       t.points as points, /*会员积分*/\n" +
                "       (case when t.intelligence_flag in (1) then '是' when t.intelligence_flag in (0) then '否' else '未知' end) as intelligence_flag, /*是否智能机用户标示*/\n" +
                "       (case when t.mobile_device_model_name not in ('-1') then t.mobile_device_model_name else '未知' end) as mobile_device_model_name, /*手机型号*/\n" +
                "       CONCAT((case when t.mobile_device_type in (1) then '安卓' when t.mobile_device_type in (2) then 'ios' else '未知' end),\n" +
                "       (case when t.mobile_device_system_version not in ('-1') then t.mobile_device_system_version else '未知' end)) as mobile_device_type /*终端类型*/\n" +
                "  from sl_rpt_life_cycle_users t where t.uid = '" + uid + "';");

    }

    //用户关注 +-1
    public Record getUserGroup(int uid) {
        return Db.use("main2").findFirst("select u1.uid as uid, /*用户id*/\n" +
                "       u1.levelname as levelname, /*会员等级（称号）*/\n" +
                "       (case when u2.uid is not null then u2.user_group else '屌丝' end) as user_group, /*用户群*/\n" +
                "       (case when u2.uid is not null then u2.user_caste else '蓝领型'end) as user_caste /*职业等级*/\n" +
                "  from\n" +
                "(select t.uid as uid, /*用户id*/\n" +
                "        t.levelname as levelname /*会员等级（称号）*/ \n" +
                "   from sl_rpt_life_cycle_users t) u1 left join \n" +
                "(select t.uid,\n" +
                "      (case when sum(t.total) >=0 and sum(t.total) <500 then '屌丝'\n" +
                "            when sum(t.total) >=500 and sum(t.total)<1000 then '中产者'\n" +
                "            when sum(t.total) >=1000 and sum(t.total)<2000 then '高产者'\n" +
                "            when sum(t.total) >=2000 and sum(t.total)<5000 then '富豪'\n" +
                "            when sum(t.total) >=5000 then '大富豪' end) as user_group,\n" +
                "      (case when sum(t.total) >=0 and sum(t.total) <300 then '蓝领型'\n" +
                "            when sum(t.total) >=300 and sum(t.total)<500 then '灰领型'\n" +
                "            when sum(t.total) >=500 and sum(t.total)<1500 then '粉领型'\n" +
                "            when sum(t.total) >=1500 and sum(t.total)<3000 then '白领型'\n" +
                "            when sum(t.total) >=3000 and sum(t.total)<5000 then '富帅型'\n" +
                "            when sum(t.total) >=5000 and sum(t.total)<8000 then '富豪型'\n" +
                "            when sum(t.total) >=8000 and sum(t.total)<10000 then '大富豪型'\n" +
                "            when sum(t.total) >=10000 then '土豪型' end) as user_caste\n" +
                "  from sl_rpt_analysis_order t where t.state not in ('-1')\n" +
                "group by t.uid) u2 on u1.uid = u2.uid where u1.uid = '" + uid + "';");
    }
}
