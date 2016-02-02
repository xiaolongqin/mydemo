package com.szl.stronguion.model.customercharacter;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

/**
 * Created by Tyfunwang on 2015/7/16.
 */
public class AllCycleUsersFlag extends Model<AllCycleUsersFlag> {
    private static AllCycleUsersFlag dao = new AllCycleUsersFlag();

    //消费习惯
    public Record getCustHabit() {
        return Db.use("main2").findFirst("select  SUM(CASE  WHEN  uid_consume_flag3 in (1) THEN 1 ELSE 0 END ) as '购物忠实型',\n" +
                "\t\t\t\tSUM(CASE  WHEN  uid_consume_flag2 in (1) THEN 1 ELSE 0 END ) as '购物粉丝型',\n" +
                "\t\t\t\tSUM(CASE  WHEN  uid_consume_flag1 in (1) THEN 1 ELSE 0 END ) as '购物热衷型',\n" +
                "\t\t\t\tSUM(CASE  WHEN  uid_consume_flag0 in (1) THEN 1 ELSE 0 END ) as '购物关注型'\n" +
                "\t\t\tFROM sl_rpt_life_cycle_all_users_flag ;");
    }

    //消费金额
    public Record getCustPrice() {
        return Db.use("main2").findFirst("select  SUM(CASE  WHEN uid_consume_flag3a0 >= '0'   and uid_consume_flag3a0 <= '300'   THEN 1 ELSE 0 END ) as ' 蓝领型',\n" +
                "\t\t\t\tSUM(CASE  WHEN uid_consume_flag3a0 > '300'  and uid_consume_flag3a0 <= '500'   THEN 1 ELSE 0 END ) as '灰领型',\n" +
                "\t\t\t\tSUM(CASE  WHEN uid_consume_flag3a0 > '500'  and uid_consume_flag3a0 <= '1500'  THEN 1 ELSE 0 END ) as '粉领型',\n" +
                "\t\t\t\tSUM(CASE  WHEN uid_consume_flag3a0 > '1500'  and uid_consume_flag3a0 <= '3000' THEN 1 ELSE 0 END ) as '白领型',\n" +
                "\t\t\t\tSUM(CASE  WHEN uid_consume_flag3a0 > '3000'  and uid_consume_flag3a0 <= '5000' THEN 1 ELSE 0 END ) as '富帅型',\n" +
                "\t\t\t\tSUM(CASE  WHEN uid_consume_flag3a0 > '5000'  and uid_consume_flag3a0 <= '8000' THEN 1 ELSE 0 END ) as '富豪型',\n" +
                "\t\t\t\tSUM(CASE  WHEN uid_consume_flag3a0 > '8000'  and uid_consume_flag3a0 <= '10000' THEN 1 ELSE 0 END ) as '大富豪型',\n" +
                "\t\t\t\tSUM(CASE  WHEN uid_consume_flag3a0 > '10000'   THEN 1 ELSE 0 END ) as '土豪型'\t\n" +
                "\t\t\tFROM sl_rpt_life_cycle_all_users_flag;\n");

    }

    //余额习惯
    public Record getBalance() {
        return Db.use("main2").findFirst("select  SUM(CASE  WHEN  uid_balance_flag3 in (1) THEN 1 ELSE 0 END ) as '半面不忘型',\n" +
                "\t\t\t\tSUM(CASE  WHEN  uid_balance_flag2 in (1) THEN 1 ELSE 0 END ) as '触目成诵型',\n" +
                "\t\t\t\tSUM(CASE  WHEN  uid_balance_flag1 in (1) THEN 1 ELSE 0 END ) as '多闻强记型'\n" +
                "\t\t\tFROM sl_rpt_life_cycle_all_users_flag;");
    }

    //活跃度
    public Record getActive() {
        return Db.use("main2").findFirst("select  SUM(CASE  WHEN  uid_active_flag3 in (1) THEN 1 ELSE 0 END ) as '狂热忠诚型',\n" +
                "\t\t\t\tSUM(CASE  WHEN  uid_active_flag2 in (1) THEN 1 ELSE 0 END ) as '粉丝忠诚型',\n" +
                "\t\t\t\tSUM(CASE  WHEN  uid_active_flag1 in (1) THEN 1 ELSE 0 END ) as '爱好兴趣型',\n" +
                "\t\t\t\tSUM(CASE  WHEN  uid_active_flag0 in (1) THEN 1 ELSE 0 END ) as '关注使用型'\n" +
                "\t\t\tFROM sl_rpt_life_cycle_all_users_flag;");

    }

    //沉默度
    public Record getSilence() {
        return Db.use("main2").findFirst("select  SUM(CASE  WHEN  uid_silent_flag3 in (1) THEN 1 ELSE 0 END ) as '离网死亡型',\n" +
                "\t\t\t\tSUM(CASE  WHEN  uid_silent_flag2 in (1) THEN 1 ELSE 0 END ) as '沉默离网趋向型',\n" +
                "\t\t\t\tSUM(CASE  WHEN  uid_silent_flag1 in (1) THEN 1 ELSE 0 END ) as '活跃突减型',\n" +
                "\t\t\t\tSUM(CASE  WHEN  uid_silent_flag0 in (1) THEN 1 ELSE 0 END ) as '沉默使用型'\n" +
                "\t\t\tFROM sl_rpt_life_cycle_all_users_flag;");

    }

    /**
     * 用户活跃度*
     */
    public Record getActivation(int uid) {
        return Db.use("main2").findFirst("select t.uid as uid, /*用户ID*/\n" +
                "       (case when t.uid_active_flag3 in (1) then '狂热忠诚型'\n" +
                "             when t.uid_active_flag2 in (1) then '粉丝忠诚型'\n" +
                "             when t.uid_active_flag1 in (1) then '爱好兴趣型'\n" +
                "             when t.uid_active_flag0 in (1) then '关注使用型'\n" +
                "             when t.uid_active_flag3 in (0) and t.uid_active_flag2 in (0) and t.uid_active_flag1 in (0) and t.uid_active_flag0 in (0) then '离网趋向型'\n" +
                "        else '未知' end) as '活跃度',\n" +
                "      (case when t.uid_silent_flag0 in (1) then '沉默使用型'\n" +
                "            when t.uid_silent_flag1 in (1) then '活跃突减型'\n" +
                "            when t.uid_silent_flag2 in (1) then '沉默离网趋向型'\n" +
                "            when t.uid_silent_flag3 in (1) then '离网死亡型'  \n" +
                "            when t.uid_silent_flag0 in (0) and t.uid_silent_flag1 in (0) and t.uid_silent_flag2 in (0) and t.uid_silent_flag3 in (0) then '已经离网型'\n" +
                "        else '未知' end) as '沉默度'\n" +
                "  from sl_rpt_life_cycle_all_users_flag t where t.uid = '" + uid + "';");

    }
}
