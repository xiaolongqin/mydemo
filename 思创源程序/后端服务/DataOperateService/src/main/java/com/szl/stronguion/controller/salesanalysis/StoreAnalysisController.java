package com.szl.stronguion.controller.salesanalysis;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.service.salesanalysis.OrderAnalysisServ;
import com.szl.stronguion.utils.FormatUtils;
import com.szl.stronguion.utils.JsonHelp;

import java.util.List;
import java.util.Map;

/**
 * Created by Tyfunwang on 2015/7/28.
 */
public class StoreAnalysisController extends Controller {
    private OrderAnalysisServ orderAnalysisServ = new OrderAnalysisServ();

    /**
     * 店铺分析-总体分析*
     * dateType: 1:昨天，2：最近3天，3：最近7天，4：最近一个月，5：最近一季度 6:最近14天 *
     * paramType: 1:店铺地域分析,2:店铺销量分析,3:店铺订单分析,4:店铺用户分析*
     * selectType: 1:销量最好店铺top10, 0:销量最差店铺top10*
     */
    public void getGeneralAnalysis() {
        int paramType = getParaToInt("paramType");// paramType: 1:店铺地域分析,2:店铺销量分析,3:店铺订单分析,4:店铺用户分析
        int selectType = getParaToInt("selectType");// selectType: 1:销量最好店铺top10, 0:销量最差店铺top10
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段

        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        try {
            List<Record> list = orderAnalysisServ.getGeneralAnalysis(paramType, selectType, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     * 店铺地域分析，店铺用户分析的地域分布 *
     */
    public void getAreaByType() {
        //分为商品和店铺的地域分布，和重复购买分析的共用一个方法
        String goodName = getPara("goodName");//商品名称
        String shopName = getPara("shopName");//店铺名称
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段

        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }

        try {
            List<Record> list = orderAnalysisServ.getAreaByType(goodName, shopName, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }

    }


    /**
     * 该店铺销售分析--选中店铺的商品分析*
     */
    public void storeSaleAnalysis() {
        // selectType: 1:销量最好商品top10, 0:销量最差商品top10*
        int selectType = getParaToInt("selectType");// selectType: 1:销量最好店铺top10, 0:销量最差店铺top10
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        String shopName = getPara("shopName");

        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        try {
            List<Record> list = orderAnalysisServ.getGoodsSales(selectType, shopName, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     * 店铺分析-明细分析*
     * paramType: 1:商品效果排行分析,2:商品购物车分析,3:商品订单分析*
     * selectType: 1:销量最好商品top10, 0:销量最差商品top10*
     */
    public void detailAnalysis() {
        int paramType = getParaToInt("paramType");
        int selectType = getParaToInt("selectType",-1);
        int dateType = getParaToInt("dateType");//dateType=0 ：客户选择时间段
        String shopName = getPara("shopName");

        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }

        try {
            List<Record> list = null;
            Map<Integer, Object> map = null;
            switch (paramType) {
                case 1://商品效果排行分析
                    list = orderAnalysisServ.detailResultsRanking(selectType, shopName, time[0], time[1]);
                    break;
                case 2://商品购物车分析
                    map = orderAnalysisServ.detailShoppingCart(selectType, shopName, time[0], time[1]);
                    break;
                case 3://商品订单分析
                    list = orderAnalysisServ.detailOrders(shopName, time[0], time[1]);
                    break;
            }
            
            if (paramType == 2) {
                renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
                return;
            }
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     * 明细分析提供店铺名称搜索*
     */
    public void searchShopName() {
        String shopName = getPara("shopName");
        try {
            List<Record> list = orderAnalysisServ.searchShopName(shopName);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }

    }
}
