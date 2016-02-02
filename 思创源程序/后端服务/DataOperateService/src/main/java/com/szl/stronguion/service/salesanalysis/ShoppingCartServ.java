package com.szl.stronguion.service.salesanalysis;

import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.customercharacter.LifeCycleConsume;
import com.szl.stronguion.model.salesanalysis.ShoppingCartFlag;
import com.szl.stronguion.model.salesanalysis.ShoppingCartOrder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Tyfunwang on 2015/7/21.
 */
public class ShoppingCartServ {
    private LifeCycleConsume cycleConsume = new LifeCycleConsume();
    private ShoppingCartOrder shoppingCartOrder = new ShoppingCartOrder();
    private ShoppingCartFlag shoppingCartFlag = new ShoppingCartFlag();

    /**
     * 购物车分析--总体分析*
     */
    public List<Record> getResultRank(int goodsType, String startTime, String endTime) {
        //商品效果排行
        List<Record> top10;
        Record total;
        top10 = shoppingCartOrder.getTop10(goodsType, startTime, endTime);
        total = shoppingCartOrder.getTotalTop10(goodsType, startTime, endTime);
        //计算
        modifyValue(top10, total);
        return top10;
    }

    /**
     * 购物车分析--商品特征分析*
     */
    public Map<Integer, Object> getFeatureRank(int goodsType, String startTime, String endTime) {
        Map<Integer, Object> map = new TreeMap<Integer, Object>();
        Record record1 = null;
        Record record2 = null;
        Record record3 = null;
        Record record4 = null;
        Record record5 = null;
        Record record6 = null;
        double totals = 0.0;
        /*1高流量高销售 --> top10, 2高流量低销售 --> top10-top20, 3低流量高销售 --> top20-top50, 4低流量低销售 --> top50以后*/
        switch (goodsType) {
            case 1://高流量高销售 --> top5
                record1 = shoppingCartFlag.getTotal1(startTime, endTime, 0, 5);
                record2 = shoppingCartFlag.getTotal2(startTime, endTime, 0, 5);
                record3 = shoppingCartFlag.getTotal3(startTime, endTime, 0, 5);
                record4 = shoppingCartFlag.getTotal4(startTime, endTime, 0, 5);
                record5 = shoppingCartFlag.getTotal5(startTime, endTime, 0, 5);
                record6 = shoppingCartFlag.getTotal6(startTime, endTime, 0, 5);
                break;
            case 2://高流量低销售 --> top5-top10
                record1 = shoppingCartFlag.getTotal1(startTime, endTime, 5, 10);
                record2 = shoppingCartFlag.getTotal2(startTime, endTime, 5, 10);
                record3 = shoppingCartFlag.getTotal3(startTime, endTime, 5, 10);
                record4 = shoppingCartFlag.getTotal4(startTime, endTime, 5, 10);
                record5 = shoppingCartFlag.getTotal5(startTime, endTime, 5, 10);
                record6 = shoppingCartFlag.getTotal6(startTime, endTime, 5, 10);
                break;
            case 3://低流量高销售 --> top10-top20
                record1 = shoppingCartFlag.getTotal1(startTime, endTime, 10, 20);
                record2 = shoppingCartFlag.getTotal2(startTime, endTime, 10, 20);
                record3 = shoppingCartFlag.getTotal3(startTime, endTime, 10, 20);
                record4 = shoppingCartFlag.getTotal4(startTime, endTime, 10, 20);
                record5 = shoppingCartFlag.getTotal5(startTime, endTime, 10, 20);
                record6 = shoppingCartFlag.getTotal6(startTime, endTime, 10, 20);
                break;
            case 4://低流量低销售 --> top20以后
                record1 = shoppingCartFlag.getTotal1(startTime, endTime, 20, 50000);
                record2 = shoppingCartFlag.getTotal2(startTime, endTime, 20, 50000);
                record3 = shoppingCartFlag.getTotal3(startTime, endTime, 20, 50000);
                record4 = shoppingCartFlag.getTotal4(startTime, endTime, 20, 50000);
                record5 = shoppingCartFlag.getTotal5(startTime, endTime, 20, 50000);
                record6 = shoppingCartFlag.getTotal6(startTime, endTime, 20, 50000);
                break;
        }
        map.put(1, record1);
        map.put(2, record2);
        map.put(3, record3);
        map.put(4, record4);
        map.put(5, record5);
        map.put(6, record6);
        //计算 先求总和，再计算
//        totals = getTotals(map);
        totals = Double.valueOf(shoppingCartFlag.getCartTotal(startTime,endTime).get("total").toString());
        map = modifyValue(map, totals);
        return map;
    }

    private void modifyValue(List<Record> top10, Record total) {

        double totals = 0.0;
        try {

            totals = total.getBigDecimal("total").doubleValue();
        } catch (NullPointerException e) {
            totals = 0.0;
        }
        
        for (Record record : top10) {
            double times = record.getLong("buytimes").doubleValue();
            record.set("percent", String.format("%.2f", (times / totals) * 100));
        }
    }

    protected Map<Integer, Object> modifyValue(Map<Integer, Object> map, double totals) {
        if (totals == 0.0) {
            for (int i = 1; i < 7; i++) {
                map.put(i, 0.0);
            }
            return map;
        }
        for (int i = 1; i < 7; i++) {
            double times = 0.0;
            try {
                times = ((Record) map.get(i)).getBigDecimal("buytimes").doubleValue();
            } catch (NullPointerException e) {
                times = 0.0;
            }
            map.put(i, String.format("%.2f", (times / totals) * 100));
        }
        return map;
    }

    protected double getTotals(Map<Integer, Object> top) {
        double totals = 0.0;
        for (int i = 1; i < 7; i++) {
            BigDecimal decimal = ((Record) top.get(i)).getBigDecimal("buytimes");
            if (decimal == null) continue;
            totals += decimal.doubleValue();
        }
        return totals;
    }
}
