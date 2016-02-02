//package com.szl.stronguion.controller.electrial;
//
//import com.jfinal.core.Controller;
//import com.jfinal.kit.JsonKit;
//import com.jfinal.plugin.activerecord.Record;
//import com.szl.stronguion.service.electrial.ElectrialThirdService;
//import com.szl.stronguion.utils.FormatUtils;
//import com.szl.stronguion.utils.JsonHelp;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by 小龙
// * on 15-11-9
// * at 下午3:13.
// */
//
//public class ElectrialThirdController extends Controller {
//    private ElectrialThirdService electrialThirdService=new ElectrialThirdService();
//
//    /**
//     * //电商概览->市场细分行业->行业价格分布->价格区间热销产品扫描->--------------------------------------
//     //产品销售情况
//     */
//
//    public void getGoodsSaleInfo(){
//        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
//        int  saleType=getParaToInt("saleType", 1);
////        String goodName=getPara("goodName","茅台48度’养生酒");
////        String channel=getPara("channel","亚马逊中国");
//        String goodsId=getPara("goodsId");
//        String[] time = new String[2];
//        if (dateType == 0) {
//            time[0] = getPara("startTime");
//            time[1] = getPara("endTime");
//        } else {
//            time = FormatUtils.getDateTime(dateType);
//        }
//        try {
//            List<Record> map = electrialThirdService.getGoodsSaleInfo(goodsId,saleType,time[0],time[1]);
//            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
//        } catch (Exception e) {
//            renderJson(JsonHelp.buildFailed(e.getMessage()));
//        }
//    }
//
//
//    /**
//     * 产品价格走势
//     */
//    public void getGoodsPrice(){
//        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
////        String goodName=getPara("goodName","茅台48度’养生酒");
////        String channel=getPara("channel","亚马逊中国");
//        String goodsId=getPara("goodsId");
//
//        String[] time = new String[2];
//        if (dateType == 0) {
//            time[0] = getPara("startTime");
//            time[1] = getPara("endTime");
//        } else {
//            time = FormatUtils.getDateTime(dateType);
//        }
//        try {
//            Map<String,List<Record>> map = electrialThirdService.getGoodsPrice(goodsId,time[0],time[1]);
//            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
//        } catch (Exception e) {
//            renderJson(JsonHelp.buildFailed(e.getMessage()));
//        }
//    }
//
//
//    /**
//     * 获取所有渠道名
//     */
//    public void getAllChannelName(){
//        try {
//            List<Record> map = electrialThirdService.getAllChannelName();
//            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
//        } catch (Exception e) {
//            renderJson(JsonHelp.buildFailed(e.getMessage()));
//        }
//    }
//
//
//    //产品各渠道销量占比
//
//    public void getChannelGoodDuibi(){
//        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
//        int  saleType=getParaToInt("saleType", 1);
////        String goodName=getPara("goodName", "茅台48度’养生酒");
//        String goodsId=getPara("goodsId");
//
////        String[] channelList={"天猫","京东"};
//        String[] channelList=getParaValues("channelList[]");//渠道列表
//
//        String[] time = new String[2];
//        if (dateType == 0) {
//            time[0] = getPara("startTime");
//            time[1] = getPara("endTime");
//        } else {
//            time = FormatUtils.getDateTime(dateType);
//        }
//        try {
//            Map<String,List<Record>> map = electrialThirdService.getChannelduibi(saleType,channelList,goodsId, time[0], time[1]);
//            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
//        } catch (Exception e) {
//            renderJson(JsonHelp.buildFailed(e.getMessage()));
//        }
//    }
//
//
//    //产品地区覆盖情况
//    public void getGoodsArea(){
//        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
//        int  saleType=getParaToInt("saleType", 1);
//        String goodsbrand=getPara("goodsbrand","茅台");
//        String area=getPara("area","北京");
//        String[] time = new String[2];
//        if (dateType == 0) {
//            time[0] = getPara("startTime");
//            time[1] = getPara("endTime");
//        } else {
//            time = FormatUtils.getDateTime(dateType);
//        }
//        try {
//            List<Record> map = electrialThirdService.getGoodsArea(saleType,goodsbrand,area,time[0], time[1]);
//            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
//        } catch (Exception e) {
//            renderJson(JsonHelp.buildFailed(e.getMessage()));
//        }
//    }
//
//
//    //同类型产品竞争排名top50
//    public void getGoodsTopByCategory(){
//        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
//        int pageNumber=getParaToInt("pageNumber",1);
//        String category=getPara("category","白酒");//商品类型
//        String[] time = new String[2];
//        if (dateType == 0) {
//            time[0] = getPara("startTime");
//            time[1] = getPara("endTime");
//        } else {
//            time = FormatUtils.getDateTime(dateType);
//        }
//        try {
//            List<Record> map = electrialThirdService.getGoodsTopByCategory(pageNumber,category,time[0],time[1]);
//            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
//        } catch (Exception e) {
//            renderJson(JsonHelp.buildFailed(e.getMessage()));
//        }
//    }
//
//
//    //品牌中内竞争排名top20
//    public void getGoodsTopByBrand(){
//        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
//        int pageNumber=getParaToInt("pageNumber",1);
//        String category=getPara("category","白酒");//商品类型
//        String goodsBrand=getPara("goodsBrand","茅台");
//        String[] time = new String[2];
//        if (dateType == 0) {
//            time[0] = getPara("startTime");
//            time[1] = getPara("endTime");
//        } else {
//            time = FormatUtils.getDateTime(dateType);
//        }
//        try {
//            List<Record> map = electrialThirdService.getGoodsTopByBrand(goodsBrand,pageNumber,category,time[0],time[1]);
//            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
//        } catch (Exception e) {
//            renderJson(JsonHelp.buildFailed(e.getMessage()));
//        }
//    }
//}
