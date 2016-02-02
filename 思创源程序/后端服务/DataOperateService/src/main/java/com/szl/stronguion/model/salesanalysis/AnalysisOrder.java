package com.szl.stronguion.model.salesanalysis;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/7/24.
 */
public class AnalysisOrder extends Model<AnalysisOrder> {
    /**
     * 订单分析--总体分析*
     * 商品效果排行*
     */
    //销量最好top10的商品
    public List<Record> getTop10(String startTime, String endTime) {
        return Db.use("main2").find("select t.province as 'province',\n" +
                "       t.city as 'city',\n" +
                "       t.town as 'town', -- 区县\n" +
                "       t.village as 'village', -- 小区\n" +
                "       t.address as 'address',\n" +
                "       t.goods_name as 'goodsname', \n" +
                "       count(*) as 'buytimes'\n" +
                "  from sl_rpt_analysis_order t\n" +
                " where t.state not in (-1) \n" +
                "   and t.uid is not null\n" +
                "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                "   and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                "  group by  t.goods_name \n" +
                " order by count(*) desc LIMIT 0,10;");
    }

    public Record getTop10Total(String startTime, String endTime) {
        return Db.use("main2").findFirst("select SUM(tt.buytimes) as total from \n" +
                "\t(select t.province as 'province',\n" +
                "       t.city as 'city',\n" +
                "       t.town as 'town', -- 区县\n" +
                "       t.village as 'village', -- 小区\n" +
                "       t.address as 'address',\n" +
                "       t.goods_name as 'goodsname', \n" +
                "       count(*) as 'buytimes'\n" +
                "  from sl_rpt_analysis_order t\n" +
                " where t.state not in (-1) \n" +
                "   and t.uid is not null\n" +
                "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                "   and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                "  group by  t.goods_name \n" +
                " order by count(*) desc LIMIT 0,10) as tt ;");
    }

    //销量最差top10的商品
    public List<Record> getLast10(String startTime, String endTime) {
        return Db.use("main2").find("select t.province as 'province',\n" +
                "       t.city as 'city',\n" +
                "       t.town as 'town',\n" +
                "       t.village as 'village',\n" +
                "       t.address as 'address',\n" +
                "       t.goods_name as 'goodsname', \n" +
                "       count(*) as 'buytimes'\n" +
                "  from sl_rpt_analysis_order t\n" +
                " where t.state not in (-1) \n" +
                "   and t.uid is not null\n" +
                "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                "   and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                "  group by  t.goods_name\n" +
                " order by count(*) asc LIMIT 0,10");
    }

    public Record getLast10Total(String startTime, String endTime) {
        return Db.use("main2").findFirst("select SUM(tt.buytimes) as total from \n" +
                "(select t.province as 'province',\n" +
                "       t.city as 'city',\n" +
                "       t.town as 'town',\n" +
                "       t.village as 'village',\n" +
                "       t.address as 'address',\n" +
                "       t.goods_name as 'goodsname', \n" +
                "       count(*) as 'buytimes'\n" +
                "  from sl_rpt_analysis_order t\n" +
                " where t.state not in (-1) \n" +
                "   and t.uid is not null\n" +
                "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                "   and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                "  group by  t.goods_name\n" +
                " order by count(*) asc LIMIT 0,10) as tt ;");
    }

    /**
     * 重复购买分析*
     */
    public List<Record> getGoodsAnalysis(String startTime, String endTime) {
//购买2次以上商品特征分析
        return Db.use("main2").find("select t.province ,\n" +
                "       t.city ,\n" +
                "       t.town ,\n" +
                "       t.village ,\n" +
                "       t.address ,\n" +
                "       t.goods_name , \n" +
                "       t.goodsid ,\n" +
                "       count(*) as 'buytimes'\n" +
                "  from  sl_rpt_analysis_order t\n" +
                "  where t.state not in (-1) \n" +
                "    and t.uid is not null\n" +
                "    and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '"+startTime+"'\n" +
                "    and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '"+endTime+"'\n" +
                "    and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                "        group by t.goods_name HAVING COUNT(1) >= 2 order by count(*) desc LIMIT 0,10 ;");
    }

    public Record getTotalGoodsAnalysis(String startTime, String endTime) {
        //购买2次以上商品特征 总数
        return Db.use("main2").findFirst("SELECT SUM(tt.buytimes) as total from (select t.province ,\n" +
                "       t.city ,\n" +
                "       t.town ,\n" +
                "       t.village ,\n" +
                "       t.address ,\n" +
                "       t.goods_name , \n" +
                "       t.goodsid ,\n" +
                "       count(*) as 'buytimes'\n" +
                "  from  sl_rpt_analysis_order t\n" +
                "  where t.state not in (-1) \n" +
                "    and t.uid is not null\n" +
                "    and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '"+startTime+"'\n" +
                "    and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '"+endTime+"'\n" +
                "    and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                "        group by t.goods_name   HAVING COUNT(1) >= 2 order by count(*) desc LIMIT 0,10) tt");
    }

    public List<Record> getShopAnalysis(String startTime, String endTime) {
//购买2次以上商品店铺分析
        return Db.use("main2").find("  select  t.province ,\n" +
                "             t.city ,\n" +
                "             t.town ,\n" +
                "             t.village ,\n" +
                "             t.address ,\n" +
                "             t.shopsname ,\n" +
                "             t.shopsid , \n" +
                "             count(*) as 'buytimes'\n" +
                "          from sl_rpt_analysis_order t\n" +
                "         where t.state not in (-1) \n" +
                "           and t.uid is not null\n" +
                "           and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '"+startTime+"'\n" +
                "           and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '"+endTime+"'\n" +
                "           and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                "        group by t.shopsname HAVING COUNT(1) >= 2 order by count(*) desc LIMIT 0,10;");

    }

    public Record getTotalShopAnalysis(String startTime, String endTime) {
//购买2次以上商品店铺分析 总数
        return Db.use("main2").findFirst("SELECT SUM(tt.buytimes) as total from (   select  t.province ,\n" +
                "             t.city ,\n" +
                "             t.town ,\n" +
                "             t.village ,\n" +
                "             t.address ,\n" +
                "             t.shopsname ,\n" +
                "             t.shopsid , \n" +
                "             count(*) as 'buytimes'\n" +
                "          from sl_rpt_analysis_order t\n" +
                "         where t.state not in (-1) \n" +
                "           and t.uid is not null\n" +
                "           and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '"+startTime+"'\n" +
                "           and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '"+endTime+"'\n" +
                "           and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                "        group by t.shopsname  HAVING COUNT(1) >= 2 order by count(*) desc LIMIT 0,10) tt");

    }

    public List<Record> getUserAnalysis(String startTime, String endTime) {
        //购买2次以上商品用户分析
        return Db.use("main2").find("select t2.province ,\n" +
                "       t2.city ,\n" +
                "       t2.town ,\n" +
                "       t2.village ,\n" +
                "       t2.address ,\n" +
                "       t.phone ,\n" +
                "       sum(t2.u_cfjl) as 'buytimes'\n" +
                "  from sl_ods_users t join (\n" +
                "        select t.province,\n" +
                "               t.city,\n" +
                "               t.town,\n" +
                "               t.village,\n" +
                "               t.address,\n" +
                "               t.uid, \n" +
                "               count(*) as u_cfjl\n" +
                "          from sl_rpt_analysis_order t\n" +
                "         where t.state not in (-1) \n" +
                "           and t.uid is not null\n" +
                "           and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '"+startTime+"'\n" +
                "           and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '"+endTime+"'\n" +
                "           and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                "        group by t.province,\n" +
                "               t.city,\n" +
                "               t.town,\n" +
                "               t.village,\n" +
                "               t.address,\n" +
                "               t.uid \n" +
                "        HAVING COUNT(1) >= 2) t2 on t.id = t2.uid\n" +
                " where t.state not in (-1) \n" +
                "   and t.id is not null\n" +
                " group by \n" +
                "       t2.address ,\n" +
                "       t.phone \n" +
                " order by sum(t2.u_cfjl) desc LIMIT 0,10");
    }

    public Record getTotalUserAnalysis(String startTime, String endTime) {
        //购买2次以上商品用户分析 总数
        return Db.use("main2").findFirst("SELECT SUM(tt.buytimes) as 'total' from (select t2.province ,\n" +
                "       t2.city ,\n" +
                "       t2.town ,\n" +
                "       t2.village ,\n" +
                "       t2.address ,\n" +
                "       t.phone ,\n" +
                "       sum(t2.u_cfjl) as 'buytimes'\n" +
                "  from sl_ods_users t join (\n" +
                "        select t.province,\n" +
                "               t.city,\n" +
                "               t.town,\n" +
                "               t.village,\n" +
                "               t.address,\n" +
                "               t.uid, \n" +
                "               count(*) as u_cfjl\n" +
                "          from sl_rpt_analysis_order t\n" +
                "         where t.state not in (-1) \n" +
                "           and t.uid is not null\n" +
                "           and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '"+startTime+"'\n" +
                "           and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '"+endTime+"'\n" +
                "           and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                "        group by t.province,\n" +
                "               t.city,\n" +
                "               t.town,\n" +
                "               t.village,\n" +
                "               t.address,\n" +
                "               t.uid \n" +
                "        HAVING COUNT(1) >= 2) t2 on t.id = t2.uid\n" +
                " where t.state not in (-1) \n" +
                "   and t.id is not null\n" +
                " group by \n" +
                "       t2.address ,\n" +
                "       t.phone \n" +
                " order by sum(t2.u_cfjl) desc LIMIT 0,10) tt");
    }

    //重复购买分析的地图列表
    public List<Record> getGoodArea(String goodName, String startTime, String endTime) {
        //商品的地图列表
        return Db.use("main2").find("select t.province,count(*) as buytimes from  sl_rpt_analysis_order  t \n" +
                "\t\twhere t.goods_name = '" + goodName + "'\n" +
                "\t\t\tand\tFROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                "\t\t\tand FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                "      and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                "\t\t\tGROUP BY t.province\n" +
                "\t\t\tORDER BY count(*) desc;");
    }

    public List<Record> getShopArea(String shopName, String startTime, String endTime) {
        //店铺的地图列表
        return Db.use("main2").find("select t.province,count(*) as buytimes from  sl_rpt_analysis_order  t \n" +
                "\t\twhere t.shopsname = '" + shopName + "'\n" +
                "\t\t\tand\tFROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                "\t\t\tand FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                "      and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                "\t\t\tGROUP BY t.province\n" +
                "\t\t\tORDER BY count(*) desc;");
    }

    /**
     * 店铺分析-总体分析*
     */
    // 店铺地域分析
    public List<Record> getShopAreasTop10(int selectType, String startTime, String endTime) {
        switch (selectType) {
            case 1: // 销量最好店铺top10
                return Db.use("main2").find("select t.province,\n" +
                        "       t.city ,\n" +
                        "       t.town,\n" +
                        "       t.village,\n" +
                        "       t.address,\n" +
                        "       t1.shopsname,\n" +
                        "       count(*) as 'buytimes'\n" +
                        "  from sl_rpt_analysis_order t join sl_ods_shops t1 on t.shopsid = t1.id\n" +
                        " where t.state not in (-1) \n" +
                        "   and t.uid is not null\n" +
                        "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                        "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                        "   and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                        "  group by  t.address, t1.shopsname \n" +
                        " order by count(*) desc LIMIT 0,10;");
            case 0: // 销量最差店铺top10
                return Db.use("main2").find("select t.province,\n" +
                        "       t.city ,\n" +
                        "       t.town ,\n" +
                        "       t.village,\n" +
                        "       t.address ,\n" +
                        "       t1.shopsname ,\n" +
                        "       count(*) as 'buytimes'\n" +
                        "  from sl_rpt_analysis_order t join sl_ods_shops t1 on t.shopsid = t1.id\n" +
                        " where t.state not in (-1) \n" +
                        "   and t.uid is not null\n" +
                        "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                        "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                        "   and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                        "  group by  t.address, t1.shopsname \n" +
                        " order by count(*) asc  LIMIT 0,10;");
            default:
                return null;
        }

    }

    // 店铺地域分析 -总量
    public Record getTotalShopAreas(int selectType, String startTime, String endTime) {
        switch (selectType) {
            case 1: // 销量最好店铺top10
                return Db.use("main2").findFirst("select SUM(tt.buytimes) as total from \n" +
                        "(select t.province,\n" +
                        "       t.city ,\n" +
                        "       t.town,\n" +
                        "       t.village,\n" +
                        "       t.address,\n" +
                        "       t1.shopsname,\n" +
                        "       count(*) as 'buytimes'\n" +
                        "  from sl_rpt_analysis_order t join sl_ods_shops t1 on t.shopsid = t1.id\n" +
                        " where t.state not in (-1) \n" +
                        "   and t.uid is not null\n" +
                        "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                        "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                        "   and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                        "  group by  t.address, t1.shopsname \n" +
                        " order by count(*) desc LIMIT 0,10) as tt;");
            case 0: // 销量最差店铺top10
                return Db.use("main2").findFirst("select SUM(tt.buytimes) as total from \n" +
                        "(select t.province,\n" +
                        "       t.city ,\n" +
                        "       t.town ,\n" +
                        "       t.village,\n" +
                        "       t.address ,\n" +
                        "       t1.shopsname ,\n" +
                        "       count(*) as 'buytimes'\n" +
                        "  from sl_rpt_analysis_order t join sl_ods_shops t1 on t.shopsid = t1.id\n" +
                        " where t.state not in (-1) \n" +
                        "   and t.uid is not null\n" +
                        "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                        "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                        "   and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                        "  group by  t.address, t1.shopsname \n" +
                        " order by count(*) asc  LIMIT 0,10) as tt;");
            default:
                return null;
        }
    }

    // 店铺销量分析
    public List<Record> getShopSalesTop10(int selectType, String startTime, String endTime) {
        switch (selectType) {
            case 1: // 销量最好店铺top10
                return Db.use("main2").find("select t.province,\n" +
                        "       t.city,\n" +
                        "       t.town ,\n" +
                        "       t.village ,\n" +
                        "       t.address ,\n" +
                        "       t1.shopsname ,\n" +
                        "       sum(goods_total_price) as 'buytimes'\n" +
                        "  from sl_rpt_analysis_order t join sl_ods_shops t1 on t.shopsid = t1.id\n" +
                        " where t.state not in (-1) \n" +
                        "   and t.uid is not null\n" +
                        "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                        "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                        "   and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                        "  group by  t.address, t1.shopsname \n" +
                        " order by sum(goods_total_price) desc LIMIT 0,10;");
            case 0: // 销量最差店铺top10
                return Db.use("main2").find("select t.province,\n" +
                        "       t.city,\n" +
                        "       t.town ,\n" +
                        "       t.village ,\n" +
                        "       t.address ,\n" +
                        "       t1.shopsname ,\n" +
                        "       sum(goods_total_price) as 'buytimes'\n" +
                        "  from sl_rpt_analysis_order t join sl_ods_shops t1 on t.shopsid = t1.id\n" +
                        " where t.state not in (-1) \n" +
                        "   and t.uid is not null\n" +
                        "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                        "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                        "   and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                        "  group by  t.address, t1.shopsname \n" +
                        " order by sum(goods_total_price) asc LIMIT 0,10;");
            default:
                return null;
        }
    }

    //店铺销量分析--总量
    public Record getTotalShopSales(int selectType, String startTime, String endTime) {
        switch (selectType) {
            case 1: // 销量最好店铺top10
                return Db.use("main2").findFirst("select SUM(tt.buytimes) as total from \n" +
                        "(select t.province,\n" +
                        "       t.city,\n" +
                        "       t.town ,\n" +
                        "       t.village ,\n" +
                        "       t.address ,\n" +
                        "       t1.shopsname ,\n" +
                        "       sum(goods_total_price) as 'buytimes'\n" +
                        "  from sl_rpt_analysis_order t join sl_ods_shops t1 on t.shopsid = t1.id\n" +
                        " where t.state not in (-1) \n" +
                        "   and t.uid is not null\n" +
                        "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                        "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                        "   and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                        "  group by  t.address, t1.shopsname \n" +
                        " order by sum(goods_total_price) desc LIMIT 0,10) as tt;");
            case 0: // 销量最差店铺top10
                return Db.use("main2").findFirst("select SUM(tt.buytimes) as total from \n" +
                        "(select t.province,\n" +
                        "       t.city,\n" +
                        "       t.town ,\n" +
                        "       t.village ,\n" +
                        "       t.address ,\n" +
                        "       t1.shopsname ,\n" +
                        "       sum(goods_total_price) as 'buytimes'\n" +
                        "  from sl_rpt_analysis_order t join sl_ods_shops t1 on t.shopsid = t1.id\n" +
                        " where t.state not in (-1) \n" +
                        "   and t.uid is not null\n" +
                        "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                        "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                        "   and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                        "  group by  t.address, t1.shopsname \n" +
                        " order by sum(goods_total_price) asc LIMIT 0,10) as tt;");
            default:
                return null;
        }
    }

    /**
     * 店铺销量分析的下一级数据 *
     * 该店铺销量top10 *
     */
    public List<Record> getShopGoodsTop10(int selectType, String shopName, String startTime, String endTime) {
        switch (selectType) {
            case 1://一个店铺销量最好商品top10
                return Db.use("main2").find("select t.goods_name, \n" +
                        "       count(*) as 'buytimes',\n" +
                        "       round(sum(t.total),2) as 'totalPrice'\n" +
                        "  from sl_rpt_analysis_order t join sl_ods_shops t1 on t.shopsid = t1.id\n" +
                        " where t1.shopsname = '" + shopName + "'\n" +
                        "   and t.state not in (-1) \n" +
                        "   and t.uid is not null\n" +
                        "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                        "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                        "   and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                        "  group by t.province, t.city, t.town, t.village, t.address, t.goods_name \n" +
                        " order by count(*) desc LIMIT 0,10;");
            case 0:  //一个店铺销量最差商品top10
                return Db.use("main2").find("select t.goods_name, \n" +
                        "       count(*) as 'buttimes',\n" +
                        "       round(sum(t.total),2) as 'totalPrice'\n" +
                        "  from sl_rpt_analysis_order t join sl_ods_shops t1 on t.shopsid = t1.id\n" +
                        " where t1.shopsname = '" + shopName + "'\n" +
                        "   and t.state not in (-1) \n" +
                        "   and t.uid is not null\n" +
                        "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                        "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                        "   and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                        "  group by t.province, t.city, t.town, t.village, t.address, t.goods_name \n" +
                        " order by count(*) asc LIMIT 0,10;");
            default:
                return null;
        }
    }

    //店铺用户分析
    public List<Record> getShopUsersTop10(int selectType, String startTime, String endTime) {
        switch (selectType) {
            case 1: //销量最好店铺top10
                return Db.use("main2").find("select t2.province ,t2.city ,t2.town ,t2.village ,\n" +
                        "t2.address ,t.phone ,sum(t2.u_cfjl) as 'buytimes'\n" +
                        "from sl_ods_users t join (\n" +
                        "select t.province,t.city,t.town,t.village,t.address,\n" +
                        "t.uid, count(*) as u_cfjl\n" +
                        "from sl_rpt_analysis_order t\n" +
                        "where t.state not in (-1) \n" +
                        "and t.uid is not null\n" +
                        "and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '"+startTime+"'\n" +
                        "and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '"+endTime+"'\n" +
                        "and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                        "group by t.province,t.city,t.town,t.village,t.address,\n" +
                        "t.uid ) t2 \n" +
                        "on t.id = t2.uid\n" +
                        "where t.state not in (-1) \n" +
                        "and t.phone is not null \n" +
                        "and t.id is not null\n" +
                        "group by \n" +
                        "t2.address,\n" +
                        "t.phone \n" +
                        "order by sum(t2.u_cfjl) desc limit 0,10");
            case 0://销量最差店铺top10
                return Db.use("main2").find("select t2.province ,t2.city ,t2.town ,t2.village ,\n" +
                        "t2.address ,t.phone ,sum(t2.u_cfjl) as 'buytimes'\n" +
                        "from sl_ods_users t join (\n" +
                        "select t.province,t.city,t.town,t.village,t.address,\n" +
                        "t.uid, count(*) as u_cfjl\n" +
                        "from sl_rpt_analysis_order t\n" +
                        "where t.state not in (-1) \n" +
                        "and t.uid is not null\n" +
                        "and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '"+startTime+"'\n" +
                        "and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '"+endTime+"'\n" +
                        "and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                        "group by t.province,t.city,t.town,t.village,t.address,\n" +
                        "t.uid ) t2 \n" +
                        "on t.id = t2.uid\n" +
                        "where t.state not in (-1) \n" +
                        "and t.phone is not null \n" +
                        "and t.id is not null\n" +
                        "group by \n" +
                        "t2.address,\n" +
                        "t.phone \n" +
                        "order by sum(t2.u_cfjl) asc limit 0,10");
            default:
                return null;
        }

    }

    //店铺用户分析 -- 总量
    public Record getTotalShopUsers(int selectType, String startTime, String endTime) {
        switch (selectType) {
            case 1: //销量最好店铺top10
//                return Db.use("main2").findFirst("select SUM(tt.buytimes) as total from \n" +
//                        "(select t.province ,\n" +
//                        "       t.city ,\n" +
//                        "       t.town ,\n" +
//                        "       t.village,\n" +
//                        "       t.address ,\n" +
//                        "       t1.shopsname as 'shopsname',\n" +
//                        "       count(*) as 'buytimes'\n" +
//                        "  from sl_rpt_analysis_order t join sl_ods_shops t1 on t.shopsid = t1.id\n" +
//                        " where t.state not in (-1) \n" +
//                        "   and t.uid is not null\n" +
//                        "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
//                        "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
//                        "   and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
//                        "  group by t.province, t.city, t.town, t.village, t.address, t1.shopsname \n" +
//                        " order by count(*) desc LIMIT 0,10) as tt;");
                return Db.use("main2").findFirst("select SUM(tt.buytimes) as total from \n" +
                        "(select t2.province ,t2.city ,t2.town ,t2.village ,\n" +
                        "t2.address ,t.phone ,sum(t2.u_cfjl) as 'buytimes'\n" +
                        "from sl_ods_users t join (\n" +
                        "select t.province,t.city,t.town,t.village,t.address,\n" +
                        "t.uid, count(*) as u_cfjl\n" +
                        "from sl_rpt_analysis_order t\n" +
                        "where t.state not in (-1) \n" +
                        "and t.uid is not null\n" +
                        "and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '"+startTime+"'\n" +
                        "and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '"+endTime+"'\n" +
                        "and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                        "group by t.province,t.city,t.town,t.village,t.address,\n" +
                        "t.uid ) t2 \n" +
                        "on t.id = t2.uid\n" +
                        "where t.state not in (-1) \n" +
                        "and t.phone is not null \n" +
                        "and t.id is not null\n" +
                        "group by \n" +
                        "t2.address,\n" +
                        "t.phone \n" +
                        "order by sum(t2.u_cfjl) desc limit 0,10) as tt;");
            case 0://销量最差店铺top10
//                return Db.use("main2").findFirst("select SUM(tt.buytimes) as total from \n" +
//                        "(select t.province,\n" +
//                        "       t.city,\n" +
//                        "       t.town ,\n" +
//                        "       t.village ,\n" +
//                        "       t.address ,\n" +
//                        "       t1.shopsname as 'shopsname',\n" +
//                        "       count(*) as 'buytimes'\n" +
//                        "  from sl_rpt_analysis_order t join sl_ods_shops t1 on t.shopsid = t1.id\n" +
//                        " where t.state not in (-1) \n" +
//                        "   and t.uid is not null\n" +
//                        "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
//                        "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
//                        "   and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
//                        "  group by t.province, t.city, t.town, t.village, t.address, t1.shopsname \n" +
//                        " order by count(*) asc LIMIT 0,10) as tt;");
                return Db.use("main2").findFirst("select SUM(tt.buytimes) as total from \n" +
                        "(select t2.province ,t2.city ,t2.town ,t2.village ,\n" +
                        "t2.address ,t.phone ,sum(t2.u_cfjl) as 'buytimes'\n" +
                        "from sl_ods_users t join (\n" +
                        "select t.province,t.city,t.town,t.village,t.address,\n" +
                        "t.uid, count(*) as u_cfjl\n" +
                        "from sl_rpt_analysis_order t\n" +
                        "where t.state not in (-1) \n" +
                        "and t.uid is not null\n" +
                        "and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '"+startTime+"'\n" +
                        "and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '"+endTime+"'\n" +
                        "and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                        "group by t.province,t.city,t.town,t.village,t.address,\n" +
                        "t.uid ) t2 \n" +
                        "on t.id = t2.uid\n" +
                        "where t.state not in (-1) \n" +
                        "and t.phone is not null \n" +
                        "and t.id is not null\n" +
                        "group by \n" +
                        "t2.address,\n" +
                        "t.phone \n" +
                        "order by sum(t2.u_cfjl) asc limit 0,10) as tt;");
            default:
                return null;
        }

    }

    /**
     * 店铺分析--明细分析*
     */
    //商品效果排行分析
    public List<Record> getDetailResultRank(int selectType, String shopName, String startTime, String endTime) {
        switch (selectType) {
            case 1://销量最好商品top10
                return Db.use("main2").find("select t.goods_name as 'goodsname',t.address, \n" +
                        "                               count(*) as 'buytimes'\n" +
                        "                          from sl_rpt_analysis_order t   join sl_ods_shops t1 on t.shopsid = t1.id\n" +
                        "                         where t.state not in (-1) \n" +
                        "                           and t.uid is not null \n" +
                        "                       and t1.shopsname ='"+shopName+"'\n" +
                        "                       and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '"+startTime+"'\n" +
                        "                           and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '"+endTime+"'\n" +
                        "                          and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                        "                          group by t.address,t.goods_name \n" +
                        "                        order by count(*) desc LIMIT 0,10;");
            case 0://销量最差商品top10
                return Db.use("main2").find("select t.goods_name as 'goodsname',t.address, \n" +
                        "                               count(*) as 'buytimes'\n" +
                        "                          from sl_rpt_analysis_order t   join sl_ods_shops t1 on t.shopsid = t1.id\n" +
                        "                         where t.state not in (-1) \n" +
                        "                           and t.uid is not null \n" +
                        "                       and t1.shopsname ='"+shopName+"'\n" +
                        "                       and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '"+startTime+"'\n" +
                        "                           and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '"+endTime+"'\n" +
                        "                          and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                        "                          group by t.address, t.goods_name \n" +
                        "                        order by count(*) asc LIMIT 0,10");
            default:
                return null;
        }
    }

    public Record totalDetailResultRank(int selectType, String shopName, String startTime, String endTime) {
        switch (selectType) {
            case 1://销量最好商品总量
                return Db.use("main2").findFirst("select SUM(tt.buytimes) as total from \n" +
                        "(select t.goods_name as 'goodsname',t.address, \n" +
                        "                               count(*) as 'buytimes'\n" +
                        "                          from sl_rpt_analysis_order t   join sl_ods_shops t1 on t.shopsid = t1.id\n" +
                        "                         where t.state not in (-1) \n" +
                        "                           and t.uid is not null \n" +
                        "                       and t1.shopsname ='"+shopName+"'\n" +
                        "                       and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '"+startTime+"'\n" +
                        "                           and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '"+endTime+"'\n" +
                        "                          and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                        "                          group by t.address, t.goods_name \n" +
                        "                        order by count(*) desc LIMIT 0,10) as tt;");//销量最好商品top10
            case 0://销量最差商品总量
                return Db.use("main2").findFirst("select SUM(tt.buytimes) as total from \n" +
                        "(select t.goods_name as 'goodsname',t.address, \n" +
                        "                               count(*) as 'buytimes'\n" +
                        "                          from sl_rpt_analysis_order t   join sl_ods_shops t1 on t.shopsid = t1.id\n" +
                        "                         where t.state not in (-1) \n" +
                        "                           and t.uid is not null \n" +
                        "                       and t1.shopsname ='"+shopName+"'\n" +
                        "                       and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '"+startTime+"'\n" +
                        "                           and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '"+endTime+"'\n" +
                        "                          and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                        "                          group by t.address, t.goods_name \n" +
                        "                        order by count(*) asc LIMIT 0,10) as tt;");
            default:
                return null;
        }
    }


    // 商品订单分析
    public List<Record> getDetailOrders(String shopName, String startTime, String endTime) {
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
                "          t.shopsid as shopsid,\n" +
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
                " group by t.order_id, t.shopsid) u join sl_ods_shops u1 on u.shopsid = u1.id\n" +
                " where u1.shopsname like '%"+shopName+"%';");
    }

    //为输入店铺名联想
    public List<Record> searchShopName(String shopName) {
        return Db.use("main2").find("select shopsname from sl_rpt_analysis_order " +
                "where shopsname like '%" + shopName + "%'" +
                " \tGROUP BY shopsname limit 0,10; ;");
    }


    /**
     * 用户画像分析--消费行为  +-1*
     */
    public Record getConBehavior(int uid) {
        return Db.use("main2").findFirst("select t.uid,\n" +
                "      (case when sum(t.total) >=0 and sum(t.total) <500 then '新手'\n" +
                "            when sum(t.total) >=500 and sum(t.total)<1000 then '初级'\n" +
                "            when sum(t.total) >=1000 and sum(t.total)<2000 then '中级'\n" +
                "            when sum(t.total) >=2000 and sum(t.total)<5000 then '资深'\n" +
                "            when sum(t.total) >=5000 then '骨灰级' end) as '买家等级',\n" +
                "      sum(case when (t.order_addtime >= UNIX_TIMESTAMP(date_sub(curdate(),interval 31 day)) and t.order_addtime < UNIX_TIMESTAMP(CURDATE())) then t.total else 0 end) as '近1个月消费',\n" +
                "      sum(case when (t.order_addtime >= UNIX_TIMESTAMP(date_sub(curdate(),interval 91 day)) and t.order_addtime < UNIX_TIMESTAMP(CURDATE())) then t.total else 0 end) as '近2个月消费',\n" +
                "      (case when sum(t.total) >=0 and sum(t.total) <300 then '蓝领型'\n" +
                "            when sum(t.total) >=300 and sum(t.total)<500 then '灰领型'\n" +
                "            when sum(t.total) >=500 and sum(t.total)<1500 then '粉领型'\n" +
                "            when sum(t.total) >=1500 and sum(t.total)<3000 then '白领型'\n" +
                "            when sum(t.total) >=3000 and sum(t.total)<5000 then '富帅型'\n" +
                "            when sum(t.total) >=5000 and sum(t.total)<8000 then '富豪型'\n" +
                "            when sum(t.total) >=8000 and sum(t.total)<10000 then '大富豪型'\n" +
                "            when sum(t.total) >=10000 then '土豪型' end) as '消费等级',\n" +
                "      (case when t.payment in (1) then '货到付款' when t.payment in (2) then '电子支付' else '未知' end) as '支付偏好'\n" +
                "  from sl_rpt_analysis_order t where  t.state not in ('-1') and t.uid = '" + uid + "';");

    }
}
