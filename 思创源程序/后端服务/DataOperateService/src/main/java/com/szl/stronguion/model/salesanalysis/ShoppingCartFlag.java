package com.szl.stronguion.model.salesanalysis;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

/**
 * Created by Tyfunwang on 2015/7/31.
 * 购物车分析--总体分析*
 * 商品特征分析**
 */
public class ShoppingCartFlag extends Model<ShoppingCartFlag> {
    private static ShoppingCartFlag dao = new ShoppingCartFlag();

    /**
     * total1:加入购物车成功交易--> goodsid_shoppingcart_flag1 = 1*
     * total2: 加入购物车删除-->goodsid_shoppingcart_flag2 = 1*
     * total3:加入购物车免运费-->goodsid_freight_price = 0.00*
     * total4:加入购物车10元运费-->goodsid_freight_price = 10.00*
     * total5:加入购物车10元以上运费-->goodsid_freight_price > 10.00*
     * total6:加入购物车7天后无订单生成-->goodsid_shoppingcart_flag3=1*
     */
    public Record getTotal1(String startTime, String endTime, int start, int end) {
        //加入购物车成功交易--> goodsid_shoppingcart_flag1 = 1
        return Db.use("main2").findFirst("SELECT SUM(t1.buytimes) as 'buytimes' from \n" +
                "(select t.goods,\n" +
                "\t\tcount(t.goodsid) as 'buytimes'\n" +
                "  from sl_rpt_analysis_shoppingcart_flag t\n" +
                " where FROM_UNIXTIME(t.shoppingcart_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                "   and FROM_UNIXTIME(t.shoppingcart_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                "\t\tand t.goodsid_shoppingcart_flag1 = 1\n" +
                "   and (t.shoppingcart_addtime is not null or t.shoppingcart_addtime <> 0)\n" +
                "  group by t.province, t.city, t.town, t.village, t.address,t.goods\n" +
                " order by t.goods desc LIMIT " + start + "," + end + ") as t1;");
    }

    public Record getTotal2(String startTime, String endTime, int start, int end) {
        //加入购物车后删除-->goodsid_shoppingcart_flag2 = 1
        return Db.use("main2").findFirst(" select SUM(t2.buytimes) as 'buytimes' from\n" +
                "            (select t.goods,\n" +
                "             count(t.goodsid) as 'buytimes'\n" +
                "    from sl_rpt_analysis_shoppingcart_flag t\n" +
                "    where FROM_UNIXTIME(t.shoppingcart_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                "    and FROM_UNIXTIME(t.shoppingcart_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                "    and t.goodsid_shoppingcart_flag2 = 1\n" +
                "    and (t.shoppingcart_addtime is not null or t.shoppingcart_addtime <> 0)\n" +
                "    group by t.province, t.city, t.town, t.village, t.address,t.goods\n" +
                "    order by t.goods desc LIMIT " + start + "," + end + ") as t2;");
    }

    public Record getTotal3(String startTime, String endTime, int start, int end) {
        //加入购物车免运费-->goodsid_freight_price = 0.00
        return Db.use("main2").findFirst("select SUM(t3.buytimes) as 'buytimes' from \n" +
                "(select t.goods,\n" +
                "\t\tcount(t.goodsid) as 'buytimes'\n" +
                "  from sl_rpt_analysis_shoppingcart_flag t\n" +
                " where FROM_UNIXTIME(t.shoppingcart_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                "   and FROM_UNIXTIME(t.shoppingcart_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                "\t\tand t.goodsid_freight_price = 0.00\n" +
                "   and (t.shoppingcart_addtime is not null or t.shoppingcart_addtime <> 0)\n" +
                "  group by t.province, t.city, t.town, t.village, t.address,t.goods\n" +
                " order by t.goods desc LIMIT " + start + "," + end + ") as t3;");
    }

    public Record getTotal4(String startTime, String endTime, int start, int end) {
        //加入购物车10元运费-->goodsid_freight_price  = 10.00
        return Db.use("main2").findFirst("select SUM(t4.buytimes) as 'buytimes' from \n" +
                "(select t.goods,\n" +
                "\t\tcount(t.goodsid) as 'buytimes'\n" +
                "  from sl_rpt_analysis_shoppingcart_flag t\n" +
                " where FROM_UNIXTIME(t.shoppingcart_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                "   and FROM_UNIXTIME(t.shoppingcart_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                "\t\tand t.goodsid_freight_price = 10.00\n" +
                "   and (t.shoppingcart_addtime is not null or t.shoppingcart_addtime <> 0)\n" +
                "  group by t.province, t.city, t.town, t.village, t.address,t.goods\n" +
                " order by t.goods desc LIMIT " + start + "," + end + ") as t4;");
    }

    public Record getTotal5(String startTime, String endTime, int start, int end) {
        //加入购物车10元以上运费-->goodsid_freight_price  > 10.00
        return Db.use("main2").findFirst("select SUM(t5.buytimes) as 'buytimes' from \n" +
                "(select t.goods,\n" +
                "\t\tcount(t.goodsid) as 'buytimes'\n" +
                "  from sl_rpt_analysis_shoppingcart_flag t\n" +
                " where FROM_UNIXTIME(t.shoppingcart_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                "   and FROM_UNIXTIME(t.shoppingcart_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                "\t\tand t.goodsid_freight_price > 10.00\n" +
                "   and (t.shoppingcart_addtime is not null or t.shoppingcart_addtime <> 0)\n" +
                "  group by t.province, t.city, t.town, t.village, t.address,t.goods\n" +
                " order by t.goods desc LIMIT " + start + "," + end + ") as t5;");
    }

    public Record getTotal6(String startTime, String endTime, int start, int end) {
        //加入购物车7天后无订单生成-->goodsid_shoppingcart_flag3 = 1
        return Db.use("main2").findFirst("select SUM(t6.buytimes) as 'buytimes' from \n" +
                "(select t.goods,\n" +
                "\t\tcount(t.goodsid) as 'buytimes'\n" +
                "  from sl_rpt_analysis_shoppingcart_flag t\n" +
                " where FROM_UNIXTIME(t.shoppingcart_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                "   and FROM_UNIXTIME(t.shoppingcart_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                "\t\tand t.goodsid_shoppingcart_flag3 = 1\n" +
                "   and (t.shoppingcart_addtime is not null or t.shoppingcart_addtime <> 0)\n" +
                "  group by t.province, t.city, t.town, t.village, t.address,t.goods\n" +
                " order by t.goods desc LIMIT " + start + "," + end + ") as t6;");
    }

    public Record getCartTotal(String startTime, String endTime) {
        //加入购物车7天后无订单生成-->goodsid_shoppingcart_flag3 = 1
        return Db.use("main2").findFirst(" select COUNT(*) as total\n" +
                "      from sl_rpt_analysis_shoppingcart_flag t\n" +
                "                where FROM_UNIXTIME(t.shoppingcart_addtime, '%Y%m%d') >= '"+startTime+"'\n" +
                "                   and FROM_UNIXTIME(t.shoppingcart_addtime, '%Y%m%d') <= '"+endTime+"'\n" +
                "                   and (t.shoppingcart_addtime is not null or t.shoppingcart_addtime <> 0)");
    }


    /**
     * 店铺分析--明细分析--商品特征分析*
     * total1:加入购物车成功交易--> goodsid_shoppingcart_flag1 = 1*
     * total2: 加入购物车删除-->goodsid_shoppingcart_flag2 = 1*
     * total3:加入购物车免运费-->goodsid_freight_price = 0.00*
     * total4:加入购物车10元以上运费-->goodsid_freight_price >= 10.00*
     * total5:加入购物车7天后无订单生成-->goodsid_shoppingcart_flag3=1*
     */
    public Record getTotals1(String shopName, String startTime, String endTime, int start, int end) {
        //加入购物车成功交易--> goodsid_shoppingcart_flag1 = 1
        return Db.use("main2").findFirst("SELECT SUM(t1.buytimes) as 'buytimes' from \n" +
                "(select t.goods,count(t.goodsid) as 'buytimes'\n" +
                " from sl_rpt_analysis_shoppingcart_flag t\n" +
                "where FROM_UNIXTIME(t.shoppingcart_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                "  and FROM_UNIXTIME(t.shoppingcart_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                "\tand t.goodsid_shoppingcart_flag1 = 1\n" +
                "  and t.shopsname = '" + shopName + "'\n" +
                " and (t.shoppingcart_addtime is not null or t.shoppingcart_addtime <> 0)\n" +
                " group by t.province, t.city, t.town, t.village, t.address,t.goods\n" +
                "order by t.goods desc LIMIT " + start + "," + end + ") as t1;");
    }
    public Record getTotals2(String shopName,  String startTime, String endTime, int start, int end) {
        //加入购物车后删除--> goodsid_shoppingcart_flag2 = 1
        return Db.use("main2").findFirst("SELECT SUM(t1.buytimes) as 'buytimes' from \n" +
                "(select t.goods,count(t.goodsid) as 'buytimes'\n" +
                " from sl_rpt_analysis_shoppingcart_flag t\n" +
                "where FROM_UNIXTIME(t.shoppingcart_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                "  and FROM_UNIXTIME(t.shoppingcart_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                "\tand t.goodsid_shoppingcart_flag2 = 1\n" +
                "  and t.shopsname = '" + shopName + "'\n" +
                " and (t.shoppingcart_addtime is not null or t.shoppingcart_addtime <> 0)\n" +
                " group by t.province, t.city, t.town, t.village, t.address,t.goods\n" +
                "order by t.goods desc LIMIT " + start + "," + end + ") as t1;");
    }
    public Record getTotals3(String shopName, String startTime, String endTime, int start, int end) {
        //加入购物车免运费--> goodsid_freight_price = 0.00
        return Db.use("main2").findFirst("SELECT SUM(t1.buytimes) as 'buytimes' from \n" +
                "(select t.goods,count(t.goodsid) as 'buytimes'\n" +
                " from sl_rpt_analysis_shoppingcart_flag t\n" +
                "where FROM_UNIXTIME(t.shoppingcart_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                "  and FROM_UNIXTIME(t.shoppingcart_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                "\tand t.goodsid_freight_price = 0.00\n" +
                "  and t.shopsname = '" + shopName + "'\n" +
                " and (t.shoppingcart_addtime is not null or t.shoppingcart_addtime <> 0)\n" +
                " group by t.province, t.city, t.town, t.village, t.address,t.goods\n" +
                "order by t.goods desc LIMIT " + start + "," + end + ") as t1;");
    }
    public Record getTotals4(String shopName, String startTime, String endTime, int start, int end) {
        //加入购物车10元运费-->goodsid_freight_price  = 10.00
        return Db.use("main2").findFirst("SELECT SUM(t1.buytimes) as 'buytimes' from \n" +
                "(select t.goods,count(t.goodsid) as 'buytimes'\n" +
                " from sl_rpt_analysis_shoppingcart_flag t\n" +
                "where FROM_UNIXTIME(t.shoppingcart_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                "  and FROM_UNIXTIME(t.shoppingcart_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                "\tand t.goodsid_freight_price  = 10.00\n" +
                "  and t.shopsname = '" + shopName + "'\n" +
                " and (t.shoppingcart_addtime is not null or t.shoppingcart_addtime <> 0)\n" +
                " group by t.province, t.city, t.town, t.village, t.address,t.goods\n" +
                "order by t.goods desc LIMIT " + start + "," + end + ") as t1;");
    }
    public Record getTotals5(String shopName, String startTime, String endTime, int start, int end) {
        //加入购物车10元以上运费-->goodsid_freight_price  > 10.00
        return Db.use("main2").findFirst("SELECT SUM(t1.buytimes) as 'buytimes' from \n" +
                "(select t.goods,count(t.goodsid) as 'buytimes'\n" +
                " from sl_rpt_analysis_shoppingcart_flag t\n" +
                "where FROM_UNIXTIME(t.shoppingcart_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                "  and FROM_UNIXTIME(t.shoppingcart_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                "\tand t.goodsid_freight_price  > 10.00\n" +
                "  and t.shopsname = '" + shopName + "'\n" +
                " and (t.shoppingcart_addtime is not null or t.shoppingcart_addtime <> 0)\n" +
                " group by t.province, t.city, t.town, t.village, t.address,t.goods\n" +
                "order by t.goods desc LIMIT " + start + "," + end + ") as t1;");
    }
    public Record getTotals6(String shopName, String startTime, String endTime, int start, int end) {
        //加入购物车7天后无订单生成-->goodsid_shoppingcart_flag3 = 1
        return Db.use("main2").findFirst("SELECT SUM(t1.buytimes) as 'buytimes' from \n" +
                "(select t.goods,count(t.goodsid) as 'buytimes'\n" +
                " from sl_rpt_analysis_shoppingcart_flag t\n" +
                "where FROM_UNIXTIME(t.shoppingcart_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                "  and FROM_UNIXTIME(t.shoppingcart_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                "\tand t.goodsid_shoppingcart_flag3 = 1\n" +
                "  and t.shopsname = '" + shopName + "'\n" +
                " and (t.shoppingcart_addtime is not null or t.shoppingcart_addtime <> 0)\n" +
                " group by t.province, t.city, t.town, t.village, t.address,t.goods\n" +
                "order by t.goods desc LIMIT " + start + "," + end + ") as t1;");
    }


    public Record getCartTotalByName(String startTime, String endTime,String shopName) {
        //加入购物车7天后无订单生成-->goodsid_shoppingcart_flag3 = 1
        return Db.use("main2").findFirst(" select COUNT(*) as total\n" +
                "      from sl_rpt_analysis_shoppingcart_flag t\n" +
                "                where FROM_UNIXTIME(t.shoppingcart_addtime, '%Y%m%d') >= '"+startTime+"'\n" +
                "                   and FROM_UNIXTIME(t.shoppingcart_addtime, '%Y%m%d') <= '"+endTime+"'\n" +
                "                   and  t.shopsname='"+shopName+"'\n" +
                "                   and (t.shoppingcart_addtime is not null or t.shoppingcart_addtime <> 0)");
    }
}
