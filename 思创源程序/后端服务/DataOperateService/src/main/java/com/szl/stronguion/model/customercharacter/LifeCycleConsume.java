package com.szl.stronguion.model.customercharacter;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/7/15.
 */
public class LifeCycleConsume extends Model<LifeCycleConsume> {
    private static LifeCycleConsume dao = new LifeCycleConsume();

    /**
     * 客户消费top5分析*
     * *
     */
    public List<Record> getTop5(String startTime, String endTime) {
        return Db.use("main2").find("select COUNT(t.type_name) as 'COUNT(type_name)',t.type_name as type_name\n" +
                "\tfrom sl_rpt_life_cycle_users_consume t\n" +
                "\twhere FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '"+startTime+"'\n" +
                "\tand FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '"+endTime+"'\n" +
                "\tGROUP BY t.type_name ORDER BY COUNT(t.type_name) DESC LIMIT 0,5;");
    }

    /**
     * 消费客户地域分布分析*
     * *
     */
    //购买人数
    public List<Record> getCountUid(String startTime, String endTime) {
        return Db.use("main2").find("select count(uid) data1,province,city from sl_rpt_life_cycle_users_consume \n" +
                "\t\twhere FROM_UNIXTIME(order_addtime,'%Y%m%d') >= '" + startTime + "' and FROM_UNIXTIME(order_addtime,'%Y%m%d') <= '" + endTime + "' and province <>''\n" +
                "\t\t GROUP BY province ORDER BY count(uid) desc limit 5;");
    }

    //成交金额
    public List<Record> getTotalPrice(String startTime, String endTime) {
        return Db.use("main2").find("select SUM(total) data1,province,city from sl_rpt_life_cycle_users_consume \n" +
                "\t\twhere FROM_UNIXTIME(order_addtime,'%Y%m%d') >= '" + startTime + "' and FROM_UNIXTIME(order_addtime,'%Y%m%d') <= '" + endTime + "' and province <>''\n" +
                "\t\t GROUP BY province ORDER BY SUM(total) desc limit 5;-- 成交金额");
    }

    //成交笔数
    public List<Record> getTotalOrders(String startTime, String endTime) {
        return Db.use("main2").find("select count(order_id) data1,province,city from sl_rpt_life_cycle_users_consume \n" +
                "\t\twhere FROM_UNIXTIME(order_addtime,'%Y%m%d') >= '" + startTime + "' and FROM_UNIXTIME(order_addtime,'%Y%m%d') <= '" + endTime + "' and province <>''\n" +
                "\t\t GROUP BY province ORDER BY count(order_id)  desc limit 5;");
    }

    //客单价
    public List<Record> getPricePer(String startTime, String endTime) {
        return Db.use("main2").find("select ROUND(SUM(total) / count(uid),2) data1,province,city from sl_rpt_life_cycle_users_consume \n" +
                "\t\twhere FROM_UNIXTIME(order_addtime,'%Y%m%d') >= '" + startTime + "' and FROM_UNIXTIME(order_addtime,'%Y%m%d') <= '" + endTime + "' and province <>''\n" +
                "\t\t GROUP BY province ORDER BY ROUND(SUM(total) / count(uid),2)  desc limit 5;");
    }
}
