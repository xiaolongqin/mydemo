package com.szl.stronguion.model.salesanalysis;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/7/21.
 */
public class AnalysisOrderFlag extends Model<AnalysisOrderFlag> {
    /**
     * 订单分析--总体分析*
     * 商品特征分析*
     */
    public List<Record> getFeature(String startTime, String endTime) {
        return Db.use("main2").find("select (case when count(*) = 0 then 0 else \n" +
                "        round(sum(u.uid_order_flag5)/count(*)*100,2)\n" +
                "        end) as 'percent1',\n" +
                "       (case when count(*) = 0 then 0 else \n" +
                "        round(sum(u.uid_order_flag6)/count(*)*100,2)\n" +
                "        end) as 'percent2',\n" +
                "       (case when count(*) = 0 then 0 else\n" +
                "        round(sum(u.uid_order_flag7)/count(*)*100,2)\n" +
                "        end) as 'percent3',\n" +
                "       (case when count(*) = 0 then 0 else\n" +
                "       round(sum(u.uid_order_flag8)/count(*)*100,2)\n" +
                "       end) as 'percent4',\n" +
                "       (case when count(*) = 0 then 0 else\n" +
                "       round(sum(u.uid_order_flag9)/count(*)*100,2)\n" +
                "       end) as 'percent5'\n" +
                "  from \n" +
                "  (select t.order_id as order_id,\n" +
                "       sum(t.uid_order_flag5) as uid_order_flag5,\n" +
                "       sum(t.uid_order_flag6) as uid_order_flag6,\n" +
                "       sum(t.uid_order_flag7) as uid_order_flag7,\n" +
                "       sum(t.uid_order_flag8) as uid_order_flag8,\n" +
                "       sum(t.uid_order_flag9) as uid_order_flag9\n" +
                "  from sl_rpt_analysis_order_flag t\n" +
                " where t.uid is not null\n" +
                "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '"+startTime+"'\n" +
                "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '"+endTime+"'\n" +
                "   and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                " group by t.order_id) u ;");
    }

    public List<Record> getTop(String startTime, String endTime, int start, int end) {
        return Db.use("main2").find("select t.province as 'province',\n" +
                "       t.city as 'city',\n" +
                "       t.town as 'town', -- 区县\n" +
                "       t.village as 'village', -- 小区\n" +
                "       t.address as 'address',\n" +
                "       count(*) as 'buytimes'\n" +
                "  from sl_rpt_analysis_order_flag t\n" +
                " where t.uid is not null \n" +
                "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                "   and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                "  group by t.province, t.city, t.town, t.village, t.address \n" +
                " order by count(*) desc LIMIT " + start + "," + end + ";");
    }

    public Record getTopTotal(String startTime, String endTime, int start, int end) {
        return Db.use("main2").findFirst("select SUM(tt.buytimes) as total from \n" +
                "\t(select t.province as 'province',\n" +
                "       t.city as 'city',\n" +
                "       t.town as 'town', -- 区县\n" +
                "       t.village as 'village', -- 小区\n" +
                "       t.address as 'address',\n" +
                "       count(*) as 'buytimes'\n" +
                "  from sl_rpt_analysis_order_flag t\n" +
                " where t.uid is not null \n " +
                "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                "   and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                "  group by t.province, t.city, t.town, t.village, t.address \n" +
                " order by count(*) desc LIMIT " + start + "," + end + ") as tt ;");
    }

    /**
     * 订单分析--用户象限分析*
     */
    public List<Record> getQuadrant(String param, String startTime, String endTime) {
        return Db.use("main2").find("select  FROM_UNIXTIME(t.order_addtime, '%H') as 'times',\n" +
                "\t\t\t\tsum(t." + param + ") as 'buytimes'  -- 购物数量\n" +
                "   from sl_rpt_analysis_order_flag t\n" +
                "\t\t\tWHERE t.uid is not null\n" +
                "\t\t\t\tand FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                "\t\t\t\tand FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                "\t\t\t\tand (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                "\t\t\tgroup by times ORDER BY times;");
    }

    //理想比较型
    public List<Record> getQuadrant(String startTime, String endTime) {
        return Db.use("main2").find("select FROM_UNIXTIME(t.order_addtime, '%H') as 'times',-- 理想比较型\n" +
                "\t\t\tsum(t.uid_order_flag3)+sum(t.uid_order_flag4) as 'buytimes' -- 购物数量\n" +
                "\tfrom sl_rpt_analysis_order_flag t\n" +
                "\t\t\tWHERE\tt.uid is not null\n" +
                "\t\t\t\tand FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                "\t\t\t\tand FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                "\t\t\t\tand (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                "\t\t\tgroup by times ORDER BY times;");

    }

    /**
     * 店铺分析-总体分析*
     * 店铺订单分析**
     */
    public List<Record> getOrderAnalysis(String startTime, String endTime) {
        return Db.use("main2").find("select (case when count(*) = 0 then 0 else \n" +
                "        round(sum(u.uid_order_flag5)/count(*)*100,2)\n" +
                "        end) as 'percent1',\n" +
                "       (case when count(*) = 0 then 0 else \n" +
                "        round(sum(u.uid_order_flag6)/count(*)*100,2)\n" +
                "        end) as 'percent2',\n" +
                "       (case when count(*) = 0 then 0 else\n" +
                "        round(sum(u.uid_order_flag7)/count(*)*100,2)\n" +
                "        end) as 'percent3',\n" +
                "       (case when count(*) = 0 then 0 else\n" +
                "       round(sum(u.uid_order_flag8)/count(*)*100,2)\n" +
                "       end) as 'percent4',\n" +
                "       (case when count(*) = 0 then 0 else\n" +
                "       round(sum(u.uid_order_flag9)/count(*)*100,2)\n" +
                "       end) as 'percent5'\n" +
                "  from \n" +
                "  (select t.order_id as order_id,\n" +
                "       sum(t.uid_order_flag5) as uid_order_flag5,\n" +
                "       sum(t.uid_order_flag6) as uid_order_flag6,\n" +
                "       sum(t.uid_order_flag7) as uid_order_flag7,\n" +
                "       sum(t.uid_order_flag8) as uid_order_flag8,\n" +
                "       sum(t.uid_order_flag9) as uid_order_flag9\n" +
                "  from sl_rpt_analysis_order_flag t\n" +
                " where t.uid is not null\n" +
                "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '"+startTime+"'\n" +
                "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '"+endTime+"'\n" +
                "   and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                " group by t.order_id) u ;");
    }
}
