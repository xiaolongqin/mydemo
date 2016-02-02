package com.szl.stronguion.service.salesanalysis;

import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.salesanalysis.AnalysisOrder;
import com.szl.stronguion.model.salesanalysis.AnalysisOrderFlag;
import com.szl.stronguion.model.salesanalysis.ShoppingCartFlag;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Tyfunwang on 2015/7/21.
 */
public class OrderAnalysisServ {
    private AnalysisOrder analysis = new AnalysisOrder();
    private AnalysisOrderFlag orderFlag = new AnalysisOrderFlag();
    private ShoppingCartFlag shoppingCartFlag = new ShoppingCartFlag();
    private ShoppingCartServ shoppingCartServ = new ShoppingCartServ();

    /**
     * 订单分析--总体分析*
     * 商品效果排行*
     */
    public List<Record> getResultRank(String startTime, String endTime, int type) {
        List<Record> top10 = null;
        Record total = null;
        if (type == 1) {
            //销量最好top10的商品
            top10 = analysis.getTop10(startTime, endTime);
            total = analysis.getTop10Total(startTime, endTime);
        }
        if (type == 0) {
            //销量最差top10的商品
            top10 = analysis.getLast10(startTime, endTime);
            total = analysis.getLast10Total(startTime, endTime);
        }
        //计算
        modifyValue(top10, total);
        return top10;
    }

    /**
     * 订单分析--总体分析*
     * 商品特征排行*
     */
    public List<Record> getFeatureRank(String startTime, String endTime) {
        return orderFlag.getFeature(startTime, endTime);
    }


    /**
     * 订单分析--用户象限分析*
     * paramType: 1:购物冲动型,2:目标明确型,3:理想比较型,4:海淘犹豫型*
     */
    public Map<String, List<Record>> getQuadrant(String startTime, String endTime) {
        Map<String, List<Record>> map = new TreeMap<String, List<Record>>();
        map.put("namea", orderFlag.getQuadrant("uid_order_flag1", startTime, endTime));//购物冲动型
        map.put("nameb", orderFlag.getQuadrant("uid_order_flag2", startTime, endTime));//目标明确型
        map.put("namec", orderFlag.getQuadrant(startTime, endTime));//理想比较型 uid_order_flag3+uid_order_flag4
        map.put("named", orderFlag.getQuadrant("uid_order_flag6", startTime, endTime));//海淘犹豫型
        return map;
    }

    public List<Record> getQuadrant(int paramType, String startTime, String endTime) {
        switch (paramType) {
            case 1:
                return orderFlag.getQuadrant("uid_order_flag1", startTime, endTime);//购物冲动型
            case 2:
                return orderFlag.getQuadrant("uid_order_flag2", startTime, endTime);//目标明确型
            case 3:
                return orderFlag.getQuadrant(startTime, endTime);//理想比较型 uid_order_flag3+uid_order_flag4
            case 4:
                return orderFlag.getQuadrant("uid_order_flag6", startTime, endTime);//海淘犹豫型
            default:
                return null;//购物冲动型
        }
    }

    /**
     * 重复购买分析*
     * paramType: 1:购买2次以上商品特征分析,2:购买2次以上商品店铺分析,3:购买2次以上商品用户分析*
     */
    public List<Record> getRepeatOrder(int paramType, String startTime, String endTime) {
        List<Record> list = null;
        Record total;
        if (paramType == 1) {
            //购买2次以上商品特征分析
            list = analysis.getGoodsAnalysis(startTime, endTime);
            total = analysis.getTotalGoodsAnalysis(startTime, endTime);

            //计算占比
            modifyValue(list, total);
        }
        if (paramType == 2) {
            //购买2次以上商品店铺分析
            list = analysis.getShopAnalysis(startTime, endTime);
            total = analysis.getTotalShopAnalysis(startTime, endTime);

            //计算占比
            modifyValue(list, total);
        }
        if (paramType == 3) {
            //购买2次以上商品用户分析
            list = analysis.getUserAnalysis(startTime, endTime);
            total = analysis.getTotalUserAnalysis(startTime, endTime);

            //计算占比
            modifyValue(list, total);
        }
        return list;
    }

    /**
     * 订单分析，重复购买分析，店铺分析公用该方法 *
     */
    public List<Record> getAreaByType(String goodName, String shopName, String startTime, String endTime) {
        //获取对应类别的地图列表--几个模块公用这个方法
        List<Record> list = null;
        if (goodName != null) {
            //商品的地图列表
            list = analysis.getGoodArea(goodName, startTime, endTime);
        }
        if (shopName != null) {
            //店铺的地图列表
            list = analysis.getShopArea(shopName, startTime, endTime);
        }
        modifyValue(list);
        return list;
    }

    /**
     * 店铺分析-总体分析*
     * paramType: 1:店铺地域分析,2:店铺销量分析,3:店铺订单分析,4:店铺用户分析*
     * selectType: 1:销量最好店铺top10, 0:销量最差店铺top10*
     */
    public List<Record> getGeneralAnalysis(int paramType, int selectType, String startTime, String endTime) {
        List<Record> list = null;
        Record total;
        if (paramType == 1) {
            //店铺地域分析
            list = analysis.getShopAreasTop10(selectType, startTime, endTime);
            total = analysis.getTotalShopAreas(selectType, startTime, endTime);

            //计算占比
            modifyValue(list, total);
        }
        if (paramType == 2) {
            //店铺销量分析
            list = analysis.getShopSalesTop10(selectType, startTime, endTime);
            total = analysis.getTotalShopSales(selectType, startTime, endTime);

            //计算占比
            modifyValue(list, total);
        }
        if (paramType == 3) {
            //店铺订单分析
            list = orderFlag.getOrderAnalysis(startTime, endTime);
        }
        if (paramType == 4) {
            //店铺用户分析
            list = analysis.getShopUsersTop10(selectType, startTime, endTime);
            total = analysis.getTotalShopUsers(selectType, startTime, endTime);
            //计算占比
            modifyValue(list, total);
        }
        return list;
    }

    //一个店铺销售最好和最差top10商品分析
    public List<Record> getGoodsSales(int selectType, String shopName, String startTime, String endTime) {
        return analysis.getShopGoodsTop10(selectType, shopName, startTime, endTime);
    }


    /**
     * 店铺分析-明细分析*
     * paramType: 1:商品效果排行分析,2:商品购物车分析,3:商品订单分析*
     * selectType: 1:销量最好商品top10, 0:销量最差商品top10*
     */

    public List<Record> detailResultsRanking(int selectType, String shopName, String startTime, String endTime) {
        //商品效果排行分析 
        List<Record> list = analysis.getDetailResultRank(selectType, shopName, startTime, endTime);

        modifyValue(list, analysis.totalDetailResultRank(selectType, shopName, startTime, endTime));
        return list;
    }

    public Map<Integer, Object> detailShoppingCart(int selectType, String shopName, String startTime, String endTime) {
        //商品购物车分析
        Map<Integer, Object> map = new TreeMap<Integer, Object>();
        Record record1 = null;
        Record record2 = null;
        Record record3 = null;
        Record record4 = null;
        Record record5 = null;
        Record record6 = null;
        double totals = 0.0;
        /*selectType 
        1高流量高销售 --> top10, 2高流量低销售 --> top10-top20, 3低流量高销售 --> top20-top50, 4低流量低销售 --> top50以后*/
        switch (selectType) {
            case 1://高流量高销售 --> top10
                record1 = shoppingCartFlag.getTotals1(shopName, startTime, endTime, 0, 5);
                record2 = shoppingCartFlag.getTotals2(shopName, startTime, endTime, 0, 5);
                record3 = shoppingCartFlag.getTotals3(shopName, startTime, endTime, 0, 5);
                record4 = shoppingCartFlag.getTotals4(shopName, startTime, endTime, 0, 5);
                record5 = shoppingCartFlag.getTotals5(shopName, startTime, endTime, 0, 5);
                record6 = shoppingCartFlag.getTotals6(shopName, startTime, endTime, 0, 5);
                break;
            case 2://高流量低销售 --> top10-top20
                record1 = shoppingCartFlag.getTotals1(shopName, startTime, endTime, 5, 10);
                record2 = shoppingCartFlag.getTotals2(shopName, startTime, endTime, 5, 10);
                record3 = shoppingCartFlag.getTotals3(shopName, startTime, endTime, 5, 10);
                record4 = shoppingCartFlag.getTotals4(shopName, startTime, endTime, 5, 10);
                record5 = shoppingCartFlag.getTotals5(shopName, startTime, endTime, 5, 10);
                record6 = shoppingCartFlag.getTotals6(shopName, startTime, endTime, 5, 10);
                break;
            case 3://低流量高销售 --> top20-top50
                record1 = shoppingCartFlag.getTotals1(shopName, startTime, endTime, 10, 20);
                record2 = shoppingCartFlag.getTotals2(shopName, startTime, endTime, 10, 20);
                record3 = shoppingCartFlag.getTotals3(shopName, startTime, endTime, 10, 20);
                record4 = shoppingCartFlag.getTotals4(shopName, startTime, endTime, 10, 20);
                record5 = shoppingCartFlag.getTotals5(shopName, startTime, endTime, 10, 20);
                record6 = shoppingCartFlag.getTotals6(shopName, startTime, endTime, 10, 20);
                break;
            case 4://低流量低销售 --> top50以后
                record1 = shoppingCartFlag.getTotals1(shopName, startTime, endTime, 20, 50000);
                record2 = shoppingCartFlag.getTotals2(shopName, startTime, endTime, 20, 50000);
                record3 = shoppingCartFlag.getTotals3(shopName, startTime, endTime, 20, 50000);
                record4 = shoppingCartFlag.getTotals4(shopName, startTime, endTime, 20, 50000);
                record5 = shoppingCartFlag.getTotals5(shopName, startTime, endTime, 20, 50000);
                record6 = shoppingCartFlag.getTotals6(shopName, startTime, endTime, 20, 50000);
                break;
        }
        map.put(1, record1);
        map.put(2, record2);
        map.put(3, record3);
        map.put(4, record4);
        map.put(5, record5);
        map.put(6, record6);
        //计算 先求总和，再计算
//        totals = shoppingCartServ.getTotals(map);
        totals = Double.valueOf(shoppingCartFlag.getCartTotalByName(startTime,endTime,shopName).get("total").toString());
        map = shoppingCartServ.modifyValue(map, totals);
        return map;
    }

    public List<Record> detailOrders(String shopName, String startTime, String endTime) {
        //商品订单分析
        return analysis.getDetailOrders(shopName, startTime, endTime);
    }

    public List<Record> searchShopName(String shopName) {
        return analysis.searchShopName(shopName);

    }

    //tool methods
    private void modifyValue(List<Record> top10, Record total) {
        double totals = 0.0;
        try {
            totals = total.getBigDecimal("total").doubleValue();
        } catch (NullPointerException e) {
            totals = 0.0;
        }

        for (Record record : top10) {
            try{
                double times = Double.valueOf(record.get("buytimes").toString());
                record.set("percent", String.format("%.2f", (times / totals) * 100));
            }catch (Exception e){
                record.set("percent", 0.0);
            }
        }
    }

    /**
     * 去除省份中的 ‘市’ 和 ‘省’ 字*
     *
     * @param records
     */
    public  void modifyValue(List<Record> records) {
        for (Record record : records) {
            StringBuilder stringBuilder = new StringBuilder();
            String str = record.getStr("province");
            char[] ch = str.toCharArray();
            for (int i = 0; i < ch.length; i++) {
                if (ch[i] == '市' || ch[i] == '省') continue;
                stringBuilder.append(ch[i]);
            }
            record.set("province", stringBuilder.toString());
        }
    }
}
