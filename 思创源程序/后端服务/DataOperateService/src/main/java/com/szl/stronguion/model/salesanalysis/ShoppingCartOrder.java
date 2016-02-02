package com.szl.stronguion.model.salesanalysis;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/7/31.
 */
public class ShoppingCartOrder extends Model<ShoppingCartOrder> {
    private static ShoppingCartOrder dao = new ShoppingCartOrder();

    /**
     * 购物车分析--总体分析*
     * 商品效果排行*
     */
    //销量top10的商品
    public List<Record> getTop10(int goodsType, String startTime, String endTime) {
        switch (goodsType) {
            case 1: //销量最好top10的商品
                return Db.use("main2").find("select t.province as 'province',\n" +
                        "       t.city as 'city',\n" +
                        "       t.town as 'county', -- 区县\n" +
                        "       t.village as 'plot', -- 小区\n" +
                        "       t.address as 'address',\n" +
                        "       t.goods_name as 'goodsname', \n" +
                        "       count(t.goodsid) as 'buytimes'\n" +
                        "  from sl_rpt_analysis_shoppingcart_order t\n" +
                        " where FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                        "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                        "   and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                        "  group by t.province, t.city, t.town, t.village, t.address, t.goods_name \n" +
                        " order by count(t.goodsid) desc LIMIT 0,10;");
            case 0://销量最差top10的商品
                return Db.use("main2").find("select t.province as 'province',\n" +
                        "       t.city as 'city',\n" +
                        "       t.town as 'county',\n" +
                        "       t.village as 'plot',\n" +
                        "       t.address as 'address',\n" +
                        "       t.goods_name as 'goodsname', \n" +
                        "       count(t.goodsid) as 'buytimes'\n" +
                        "  from sl_rpt_analysis_shoppingcart_order t\n" +
                        " where FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                        "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                        "   and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                        "  group by t.province, t.city, t.town, t.village, t.address, t.goods_name\n" +
                        " order by count(t.goodsid) asc LIMIT 0,10");
            default:
                return null;
        }

    }

    //销量top10的商品 --总数
    public Record getTotalTop10(int goodsType, String startTime, String endTime) {
        switch (goodsType) {
            case 1: //销量最好top10的商品
                return Db.use("main2").findFirst("select SUM(tt.buytimes) as total from \n" +
                        "(select t.province as 'province',\n" +
                        "       t.city as 'city',\n" +
                        "       t.town as 'county', -- 区县\n" +
                        "       t.village as 'plot', -- 小区\n" +
                        "       t.address as 'address',\n" +
                        "       t.goods_name as 'goodsname', \n" +
                        "       count(t.goodsid) as 'buytimes'\n" +
                        "  from sl_rpt_analysis_shoppingcart_order t\n" +
                        " where FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                        "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                        "   and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                        "  group by t.province, t.city, t.town, t.village, t.address, t.goods_name \n" +
                        " order by count(t.goodsid) desc LIMIT 0,10) as tt;");
            case 0://销量最差top10的商品
                return Db.use("main2").findFirst("select SUM(tt.buytimes) as total from \n" +
                        "(select t.province as 'province',\n" +
                        "       t.city as 'city',\n" +
                        "       t.town as 'county',\n" +
                        "       t.village as 'plot',\n" +
                        "       t.address as 'address',\n" +
                        "       t.goods_name as 'goodsname', \n" +
                        "       count(t.goodsid) as 'buytimes'\n" +
                        "  from sl_rpt_analysis_shoppingcart_order t\n" +
                        " where FROM_UNIXTIME(t.order_addtime, '%Y%m%d') >= '" + startTime + "'\n" +
                        "   and FROM_UNIXTIME(t.order_addtime, '%Y%m%d') <= '" + endTime + "'\n" +
                        "   and (t.order_addtime is not null or t.order_addtime <> 0)\n" +
                        "  group by t.province, t.city, t.town, t.village, t.address, t.goods_name\n" +
                        " order by count(t.goodsid) asc LIMIT 0,10) as tt;");
            default:
                return null;
        }
    }
}
