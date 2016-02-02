package com.szl.stronguion.controller.goodsAnalysis;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.controller.menus.AccountController;
import com.szl.stronguion.model.menus.Account;
import com.szl.stronguion.service.electrial.ElectrialSecondService;
import com.szl.stronguion.service.electrial.ElectrialService;
import com.szl.stronguion.service.electrial.ElectrialThirdService;
import com.szl.stronguion.service.goodsAnalysis.GoodsAnalysisService;
import com.szl.stronguion.utils.FormatUtils;
import com.szl.stronguion.utils.JsonHelp;
import com.szl.stronguion.utils.JxlTest;
import jxl.write.WritableWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by 小龙
 * on 15-11-16
 * at 上午9:31.
 */

public class GoodsAnalysisController extends Controller {
    private GoodsAnalysisService goodsAnalysisService=new GoodsAnalysisService();
    private ElectrialService electrialService = new ElectrialService();
    private ElectrialThirdService electrialThirdService=new ElectrialThirdService();
    private ElectrialSecondService electrialSecondService=new ElectrialSecondService();
    /**
     *产品分析-》产品整体扫描---------------------------------------------------------
     *全网产品扫描
     * seqType:1--升序分页 2--降序排列
     * saleType:1--销售额  2--销售量
     */

    public void getGoodsScan(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        int  saleType=getParaToInt("saleType", 1);
        int pageNumber=getParaToInt("pageNumber",1);
        int seqType=getParaToInt("seqType", 1);  //1--升序分页    2--降序排列
        String[] channelList=getParaValues("channelList[]");
        String goodName=getPara("goodName","");

        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        String category_id= ((Account)getSessionAttr(AccountController.ACCOUNTS)).get(Account.CATEGORYID).toString();
        time = FormatUtils.formatDetail(time);

        try {
            List<Record> map = goodsAnalysisService.getGoodsScan(seqType,category_id,goodName,channelList, pageNumber, saleType, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }



    public void exportDataExcel(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        int  saleType=getParaToInt("saleType", 1);
        int seqType=getParaToInt("seqType", 1);  //1--升序分页    2--降序排列
//        String[] channelList=getParaValues("channelList[]");
        String goodName=getPara("goodName","");

        int channel_num=getParaToInt("channel_num",3);
        String[] channelList=new String[channel_num];
        for (int i=0;i<channel_num;i++){
            channelList[i]=getPara("channelList["+(i)+"]");
        }

        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        String category_id= ((Account)getSessionAttr(AccountController.ACCOUNTS)).get(Account.CATEGORYID).toString();
        time = FormatUtils.formatDetail(time);

        try {
            List<Record> list = goodsAnalysisService.getExportGoods(seqType, category_id, goodName, channelList, saleType, time[0], time[1]);
            sendFile(list,saleType);
            renderNull();
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }
    private void sendFile(List<Record> list,int  saleType) {
        try {
            HttpServletResponse response = getResponse();
            String filename = "全网商品扫描.xls";//设置下载时客户端Excel的名称
            filename = new String(filename.getBytes(),"iso-8859-1");
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            OutputStream ouputStream = response.getOutputStream();

            WritableWorkbook wb= JxlTest.GoodsToExcel(list, saleType, ouputStream);
            wb.write();
            wb.close();
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 全网热销类目
     */

//    public void getDetailGoodTop(){
//        int dateType = getParaToInt("dateType",1);//dateType=0 ：客户选择时间段
//        int  saleType=getParaToInt("saleType", 1);
//        String[] time = new String[2];
//        if (dateType == 0) {
//            time[0] = getPara("startTime");
//            time[1] = getPara("endTime");
//        } else {
//            time = FormatUtils.getDateTime(dateType);
//        }
//        try {
//            List<Record> list = electrialService.getDtailGoodTop(saleType, time[0], time[1]);
//            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
//        } catch (Exception e) {
//            renderJson(JsonHelp.buildFailed(e.getMessage()));
//        }
//    }

    /**
     * 产品分析-》产品飙升排行---------------------------------------------------------
     * 产品飙升总榜top50
     */
    public void getGoodsTop(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        int pageNumber=getParaToInt("pageNumber",1);
        int  saleType=getParaToInt("saleType",1);
        String brandName=getPara("brandName","全部");
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        String category_id= ((Account)getSessionAttr(AccountController.ACCOUNTS)).get(Account.CATEGORYID).toString();
        time = FormatUtils.formatDetail(time);
        try {
            List<Record> map = goodsAnalysisService.getGoodsTop(category_id,brandName,saleType,pageNumber,time[0],time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     *     //商品类目飙升排行top50
     */
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


    /**
     *品牌产品飙升排行top20
     */
//    public void getGoodsTopByBrand(){
//        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
//        int pageNumber=getParaToInt("pageNumber",1);
//        String goodsBrand=getPara("goodsBrand","茅台");
//        int  saleType=getParaToInt("saleType",1);
//        String[] time = new String[2];
//        if (dateType == 0) {
//            time[0] = getPara("startTime");
//            time[1] = getPara("endTime");
//        } else {
//            time = FormatUtils.getDateTime(dateType);
//        }
//        try {
//            List<Record> map = goodsAnalysisService.getGoodsTopByBrand(saleType,goodsBrand,pageNumber,time[0],time[1]);
//            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
//        } catch (Exception e) {
//            renderJson(JsonHelp.buildFailed(e.getMessage()));
//        }
//    }


//    //获取类目列表
//    public void getGoodsCategory(){
//        try {
//            List<Record> list =goodsAnalysisService.getGoodsCategory();
//            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
//        } catch (Exception e) {
//            renderJson(JsonHelp.buildFailed(e.getMessage()));
//        }
//    }

    /**
     *获取品牌列表
     */
    public void getGoodsBrand(){
        try {
            List<Record> list =goodsAnalysisService.getGoodsGoodBrand();
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }


    /**
     * 产品分析-->产品画像---------------------------------------------------
     * 白酒产品参数与销量
     */

    public void getDegreeSale(){
        int dateType = getParaToInt("dateType",1);//dateType=0 ：客户选择时间段
        int  saleType=getParaToInt("saleType",1);
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        String category_id= ((Account)getSessionAttr(AccountController.ACCOUNTS)).get(Account.CATEGORYID).toString();
        time = FormatUtils.formatDetail(time);
        try {
            List<Record> map = electrialService.getDegreeSale(saleType,category_id, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     * 白酒各香型酒销量
     */
    public void getXiangxinSale(){
        int dateType = getParaToInt("dateType",1);//dateType=0 ：客户选择时间段
        int  saleType=getParaToInt("saleType",1);
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        String category_id= ((Account)getSessionAttr(AccountController.ACCOUNTS)).get(Account.CATEGORYID).toString();
        time = FormatUtils.formatDetail(time);
        try {
            Map<String,List<Record>> map = electrialService.getXiangxinSale(saleType, category_id, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }


    /**
     * 产品分析-->产品画像  二层页面---------------------------------------------------
     *30-40%Vol白酒产品销量情况
     */

    public void getDetailGoodsInfo(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        int  saleType=getParaToInt("saleType", 1);
        String good_attr=getPara("good_attr", "B1");
//        String goodName=getPara("goodName","白酒");
        int good_attr_type=1; //1为白酒度数情况，2为白酒香型情况
        if (good_attr.startsWith("C")){
            good_attr_type=2;
        }
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        String category_id= ((Account)getSessionAttr(AccountController.ACCOUNTS)).get(Account.CATEGORYID).toString();
        time = FormatUtils.formatDetail(time);
        try {
            List<Record> map = electrialSecondService.getDetailGoodsInfo(good_attr_type,saleType, category_id, good_attr, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     *     //30-40%白酒热销品牌
     */
    public void getGoodsBrandInfo(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        int  saleType=getParaToInt("saleType", 1);
//        String goodName=getPara("goodName","白酒");
        String good_attr=getPara("good_attr","B1");
        int good_attr_type=1; //1为白酒度数情况，2为白酒香型情况
        if (good_attr.startsWith("C")){
            good_attr_type=2;
        }
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        String category_id= ((Account)getSessionAttr(AccountController.ACCOUNTS)).get(Account.CATEGORYID).toString();
        time = FormatUtils.formatDetail(time);
        try {
            List<Record> map = electrialSecondService.getGoodsBrandInfo(good_attr_type,good_attr,saleType, category_id, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     *     //30-40%白酒热销产品
     */
    public void getGoodsNameInfo(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
//        int  saleType=getParaToInt("saleType", 1);
//        String goodName=getPara("goodName","白酒");
        String good_attr=getPara("good_attr","B1");
        int good_attr_type=1; //1为白酒度数情况，2为白酒香型情况
        if (good_attr.startsWith("C")){
            good_attr_type=2;
        }
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        String category_id= ((Account)getSessionAttr(AccountController.ACCOUNTS)).get(Account.CATEGORYID).toString();
        time = FormatUtils.formatDetail(time);
        try {
            List<Record> map = electrialSecondService.getGoodsNameInfo(good_attr_type,good_attr, category_id, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }


    /**
     *  30-40%白酒产品消费者关注点
     */
    public void getGoodsFocus(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
//        int  saleType=getParaToInt("saleType", 1);
//        String goodName=getPara("goodName","白酒");
        String good_attr=getPara("good_attr","B1");
        int good_attr_type=1; //1为白酒度数情况，2为白酒香型情况
        if (good_attr.startsWith("C")){
            good_attr_type=2;
        }
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        String category_id= ((Account)getSessionAttr(AccountController.ACCOUNTS)).get(Account.CATEGORYID).toString();
        time = FormatUtils.formatDetail(time);
        try {
            List<Record> map = electrialSecondService.getGoodsFocus(good_attr_type,good_attr, category_id, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }


//    /**
//     *     //30-40%白酒区域热度
//     */
//    public void getGoodsArea(){
//        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
//        int  saleType=getParaToInt("saleType", 1);
//        String goodName=getPara("goodName","白酒");
//        String good_attr=getPara("good_attr","B1");
//        String[] time = new String[2];
//        if (dateType == 0) {
//            time[0] = getPara("startTime");
//            time[1] = getPara("endTime");
//        } else {
//            time = FormatUtils.getDateTime(dateType);
//        }
//        try {
//            List<Record> map = electrialSecondService.getGoodsArea(good_attr,saleType,goodName, time[0], time[1]);
//            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
//        } catch (Exception e) {
//            renderJson(JsonHelp.buildFailed(e.getMessage()));
//        }
//    }






    /**
     * 产品分析-->产品对比--------------------------------------------------------------
     * 线上产品对比TOP5
     */

    public void getOnlineGoodsduibi(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        String category_id= ((Account)getSessionAttr(AccountController.ACCOUNTS)).get(Account.CATEGORYID).toString();
        time = FormatUtils.formatDetail(time);
        try {
            List<Record> map= goodsAnalysisService.getOnlineGoodsduibi(category_id,time[0],time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }


    /**
     * 产品搜索分页
     */
    public void getGoodsByGoodsName(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        int pageNumber=getParaToInt("pageNumber",1);
        String goodsName=getPara("goodsName","");//产品名称
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        String category_id= ((Account)getSessionAttr(AccountController.ACCOUNTS)).get(Account.CATEGORYID).toString();
        time = FormatUtils.formatDetail(time);
        try {
            List<Record> map = goodsAnalysisService.getGoodsByGoodsName(category_id,pageNumber,goodsName,time[0],time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }


    /**
     * 产品分析-->产品对比 二层页面-------------------------------------------------------------
     * 产品销售情况对比
     */
    public void getGoodsSaleduibi(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        String[] goodsname=getParaValues("goodsNameList[]");
        String[] goodsidList=getParaValues("goodsIdList[]");


//        String[] channelList={"京东","天猫"};
//        String[] goodsname={"茅台52度’清香酒","茅台52度’礼品酒"};
        int  saleType=getParaToInt("saleType",1);
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        time = FormatUtils.formatDetail(time);
        try {
            Map<String,List<Record>> map= goodsAnalysisService.getGoodsSaleduibi(saleType,goodsname,goodsidList,time[0],time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }


    //产品各项参数对比
    public void getGoodsAttrduibi(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        String[] goodsname=getParaValues("goodsNameList[]");
        String[] goodsidList=getParaValues("goodsIdList[]");

        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        time = FormatUtils.formatDetail(time);
        try {
            Map<String,List<Record>> map= goodsAnalysisService.getGoodsAttrduibi(goodsname,goodsidList,time[0],time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }


//    //产品覆盖区域对比
//    public void getGoodsArea(){
//        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
//        String[] goodsname=getParaValues("goodsNameList[]");
//        String[] goodsIdList=getParaValues("goodsIdList[]");
//        String[] time = new String[2];
//        if (dateType == 0) {
//            time[0] = getPara("startTime");
//            time[1] = getPara("endTime");
//        } else {
//            time = FormatUtils.getDateTime(dateType);
//        }
//        time = FormatUtils.formatDetail(time);
//        try {
//            Map<String,List<Record>> map= goodsAnalysisService.getGoodsArea(goodsname,goodsIdList,time[0],time[1]);
//            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
//        } catch (Exception e) {
//            renderJson(JsonHelp.buildFailed(e.getMessage()));
//        }
//    }




}
