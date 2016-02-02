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
 * Created by Tyfunwang on 2015/7/21.
 * dateType: 1:昨天，2：最近3天，3：最近7天，4：最近一个月，5：最近一季度 6:最近14天 *
 */
public class OrderAnalysisController extends Controller {
    private OrderAnalysisServ analysisServ = new OrderAnalysisServ();

    /**
     * 订单分析--总体分析*
     * 商品效果排行*
     */
    public void generalAnalysis() {
        int goodsType = getParaToInt("goodsType");//1:销量最好top10的商品，0：销量最差top10的商品
        int dateType = getParaToInt("dateType");//dateType=0 ：客户选择时间段
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        try {
            List<Record> list = analysisServ.getResultRank(time[0], time[1], goodsType);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    //获取选中商品的地域分布
    public  void getAreasByGood(){
        String goodName = getPara("goodName");//goodName 选中的商品名称
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        try {
            List<Record> list = analysisServ.getAreaByType(goodName,null,time[0],time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }
    /**
     * 订单分析--总体分析*
     * 商品特征分析*
     */
    public void featureAnalysis() {
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        try {
            List<Record> list = analysisServ.getFeatureRank(time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     * 订单分析--用户象限分析*
     */
    public void quadrantAnalysisFirst(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段

        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        try {
            Map<String, List<Record>> list = analysisServ.getQuadrant(time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
        
    }
    
    public void quadrantAnalysis() {
        int paramType = getParaToInt("paramType");// paramType: 1:购物冲动型,2:目标明确型,3:理想比较型,4:海淘犹豫型
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段

        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        try {
            List<Record> list = analysisServ.getQuadrant(paramType, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }
}
