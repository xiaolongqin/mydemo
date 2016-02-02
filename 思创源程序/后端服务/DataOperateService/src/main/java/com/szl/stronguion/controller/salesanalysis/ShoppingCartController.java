package com.szl.stronguion.controller.salesanalysis;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.service.salesanalysis.ShoppingCartServ;
import com.szl.stronguion.utils.FormatUtils;
import com.szl.stronguion.utils.JsonHelp;

import java.util.List;
import java.util.Map;

/**
 * Created by Tyfunwang on 2015/7/21.
 */
public class ShoppingCartController extends Controller {
    private ShoppingCartServ cartServ = new ShoppingCartServ();

    /**
     * 购物车分析--总体分析*
     * dateType: 1:昨天，2：最近3天，3：最近7天，4：最近一个月，5：最近一季度 6:最近14天 *
     */
    public void resultRanking() {
        //商品效果排行
        int goodsType = getParaToInt("goodsType");//1:销量最好top10的商品，0：销量最差top10的商品，

        int dateType = getParaToInt("dateType");//dateType=0 ：客户选择时间段
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }

        try {
            List<Record> list = cartServ.getResultRank(goodsType, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));

        }
    }
    
    /**
     * 购物车分析--商品特征分析*
     */
    public void featureRanking() {
        //商品特征分析
        int goodsType = getParaToInt("goodsType");// 1：高流量高销售，2：高流量低销售，3：低流量高销售，4：低流量低销售
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }

        try {
            Map<Integer, Object> list = cartServ.getFeatureRank(goodsType, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));

        }
    }

}
