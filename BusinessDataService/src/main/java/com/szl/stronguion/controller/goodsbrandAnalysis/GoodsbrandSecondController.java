package com.szl.stronguion.controller.goodsbrandAnalysis;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.service.goodsbrandAnalysis.GoodsbrandSecondService;
import com.szl.stronguion.utils.FormatUtils;
import com.szl.stronguion.utils.JsonHelp;

import java.util.List;

/**
 * Created by 小龙
 * on 15-11-30
 * at 下午3:38.
 */

public class GoodsbrandSecondController extends Controller {
    private GoodsbrandSecondService goodsbrandSecondService=new GoodsbrandSecondService();


    //品牌分析-》品牌特写》品牌各渠道销量占比》--------------------------------------------------------------
    //某渠道中品牌销售情况
    public void getGoodsbrandSaleBychannel(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        int  saleType=getParaToInt("saleType", 1);
        String goodsbrand=getPara("goodsbrand","茅台");
        String channel=getPara("channel","天猫");
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        time = FormatUtils.formatDetail(time);
        try {
            List<Record> list = goodsbrandSecondService.getGoodsbrandSaleBychannel(goodsbrand,channel,saleType,  time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }


    //某渠道品牌热销店铺排行
    public void getStoreTopByBrand(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        int  saleType=getParaToInt("saleType", 1);
        String goodsbrand=getPara("goodsbrand","茅台");
        String channel=getPara("channel","天猫");
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        time = FormatUtils.formatDetail(time);
        try {
            List<Record> list = goodsbrandSecondService.getStoreTopByBrand(goodsbrand,channel,saleType,  time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }


    ///某渠道热销茅台产品TOP10
    public void getGoodsTopByBrand(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        int  saleType=getParaToInt("saleType", 1);
        String goodsbrand=getPara("goodsbrand","茅台");
        String channel=getPara("channel","天猫");
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        time = FormatUtils.formatDetail(time);
        try {
            List<Record> list = goodsbrandSecondService.getGoodsTopByBrand(goodsbrand, channel, saleType, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }


    //品牌分析-》品牌特写》品牌价格分布》--------------------------------------------------------------
    //品牌价格区间销售变化情况

    public void getGoodsbrandSaleByPrice(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        int  saleType=getParaToInt("saleType", 1);
        String goodsbrand=getPara("goodsbrand","茅台");
        String price_attr=getPara("price_attr","A1");
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        time = FormatUtils.formatDetail(time);
        try {
            List<Record> list = goodsbrandSecondService.getGoodsbrandSaleByPrice(goodsbrand, price_attr, saleType, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    //品牌（茅台）500-1000元价格区间产品在各渠道的销量

    public void getBrandSaleByChannel(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        int  saleType=getParaToInt("saleType", 1);
        String goodsbrand=getPara("goodsbrand","茅台");
        String price_attr=getPara("price_attr","A1");
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        time = FormatUtils.formatDetail(time);
        try {
            List<Record> list = goodsbrandSecondService.getBrandSaleByChannel(goodsbrand, price_attr, saleType, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }


    //某品牌价格区间热销产品TOP10

    public void getGoodsSaleByBrand(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        int  saleType=getParaToInt("saleType", 1);
        String goodsbrand=getPara("goodsbrand","茅台");
        String price_attr=getPara("price_attr","A1");
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        time = FormatUtils.formatDetail(time);
        try {
            List<Record> list = goodsbrandSecondService.getGoodsSaleByBrand(goodsbrand, price_attr, saleType, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }



    /**
     * 品牌（茅台）500-1000元价格区间品牌印象
     */
    public void getbrandFaceByPrice(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        String goodsbrand=getPara("goodsbrand","茅台");//品牌名称
        String price_attr=getPara("String price_attr","A1");
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        time = FormatUtils.formatDetail(time);
        try {
            List<Record> list = goodsbrandSecondService.getbrandFaceByPrice(goodsbrand,price_attr,time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    //某品牌某价格区间产品地区覆盖

//    public void getGoodsSaleByArea(){
//        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
//        int  saleType=getParaToInt("saleType", 1);
//        String goodsbrand=getPara("goodsbrand","茅台");
//        String price_attr=getPara("price_attr","A1");
//        String area=getPara("area","北京");
//        String[] time = new String[2];
//        if (dateType == 0) {
//            time[0] = getPara("startTime");
//            time[1] = getPara("endTime");
//        } else {
//            time = FormatUtils.getDateTime(dateType);
//        }
//        try {
//            List<Record> list = goodsbrandSecondService.getGoodsSaleByArea(goodsbrand, price_attr,area, saleType, time[0], time[1]);
//            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
//        } catch (Exception e) {
//            e.printStackTrace();
//            renderJson(JsonHelp.buildFailed(e.getMessage()));
//        }
//    }
      
}
