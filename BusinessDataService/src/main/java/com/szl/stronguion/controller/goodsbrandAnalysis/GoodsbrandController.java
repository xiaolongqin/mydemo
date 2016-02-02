package com.szl.stronguion.controller.goodsbrandAnalysis;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.controller.menus.AccountController;
import com.szl.stronguion.model.menus.Account;
import com.szl.stronguion.service.goodsbrandAnalysis.GoodsbrandService;
import com.szl.stronguion.service.storeAnalysis.StoreAnalysisService;
import com.szl.stronguion.utils.FormatUtils;
import com.szl.stronguion.utils.JsonHelp;
import com.szl.stronguion.utils.JxlTest;
import jxl.write.WritableWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by 小龙
 * on 15-11-24
 * at 下午2:56.
 */

public class GoodsbrandController extends Controller {
    private StoreAnalysisService storeAnalysisService=new StoreAnalysisService();
    private GoodsbrandService goodsbrandService=new GoodsbrandService();

    /**品牌聚焦-》品牌扫描------------------------------------------
     * -》线上品牌销售排行
     * seqType:1--升序分页 2--降序排列
     * saleType:1--销售额  2--销售量
     */

    public void getGoodsbrandSaleTotal(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        int  saleType=getParaToInt("saleType", 1);
        int pageNumber=getParaToInt("pageNumber",1);
        int seqType=getParaToInt("seqType", 1);  //1--升序分页    2--降序排列
        String brandName=getPara("brandName","");
        String[] channelList=getParaValues("channelList[]");
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
            List<Record> list = goodsbrandService.getGoodsbrandSaleTotal(seqType,brandName,category_id,channelList, pageNumber, saleType, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }


    /**
     * 导出品牌扫描数据
     */
    public void exportDataExcel(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        int  saleType=getParaToInt("saleType", 1);
        int seqType=getParaToInt("seqType", 1);  //1--升序分页    2--降序排列
        String brandName=getPara("brandName","");
//        String[] channelList=getParaValues("channelList[]");
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
            List<Record> list = goodsbrandService.getExportGoodsbrand(seqType,brandName,category_id,channelList,saleType, time[0], time[1]);
            sendFile(list,saleType);
            renderNull();
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }
    private void sendFile(List<Record> list,int  saleType) {
        try {
            HttpServletResponse response = getResponse();
            String filename = "线上品牌销售排行.xls";//设置下载时客户端Excel的名称
            filename = new String(filename.getBytes(),"iso-8859-1");
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            OutputStream ouputStream = response.getOutputStream();

            WritableWorkbook wb= JxlTest.GoodBrandToExcel(list, saleType, ouputStream);
            wb.write();
            wb.close();
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



//    //获取时间段内所有渠道
//    public void getAllChannelByTime(){
//        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
//        String[] time = new String[2];
//        if (dateType == 0) {
//            time[0] = getPara("startTime");
//            time[1] = getPara("endTime");
//        } else {
//            time = FormatUtils.getDateTime(dateType);
//        }
//        try {
//            List<Record> list = storeAnalysisService.getAllChannelByTime(time[0], time[1]);
//            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
//        } catch (Exception e) {
//            renderJson(JsonHelp.buildFailed(e.getMessage()));
//        }
//    }

//    //获取所有类目
//    public void getAllCategory(){
//        try {
//            List<Record> list = storeAnalysisService.getAllCategory();
//            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
//        } catch (Exception e) {
//            renderJson(JsonHelp.buildFailed(e.getMessage()));
//        }
//    }


    /**
     * 获取所有渠道
     */
    public void getAllChannel(){
        try {
            List<Record> list = storeAnalysisService.getAllChannel();
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }


    /**
     * 细分行业品牌销售排行top10
     */
    public void getGoodsbrandTopBycategory(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        int  saleType=getParaToInt("saleType", 1);
        String[] channelList=getParaValues("channelList[]");
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
            List<Record> list = goodsbrandService.getGoodsbrandTopBycategory(channelList,category_id, saleType, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }




    /**品牌聚焦-》品牌扫描-》品牌详情------------------------------------------
     * 品牌销售情况
     */
    public void getStoreDetaiInfo(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        int  saleType=getParaToInt("saleType", 1);
        String goodsbrand=getPara("goodsbrand");
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        time = FormatUtils.formatDetail(time);
        try {
            List<Record> list = goodsbrandService.getGoodsbrandSaleInfo(saleType, goodsbrand, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     * 品牌各渠道销量占比
     */
    public void getGoodsbrandSaleBysqudao(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        int  saleType=getParaToInt("saleType", 1);
        String goodsbrand=getPara("goodsbrand","茅台");
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        time = FormatUtils.formatDetail(time);
        try {
            List<Record> list = goodsbrandService.getGoodsbrandSaleBysqudao(goodsbrand, saleType, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     * 品牌热销产品排行TOP5
     */
    public void getGoodsSaleByGoodsbrand(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        int  saleType=getParaToInt("saleType", 1);
        String goodsbrand=getPara("goodsbrand","茅台");
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        time = FormatUtils.formatDetail(time);
        try {
            List<Record> list = goodsbrandService.getGoodsSaleByGoodsbrand(goodsbrand, saleType, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }



    /**
     * 品牌价格分布
     */
    public void getGoodsbrandPriceInfo(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        String goodsbrand=getPara("goodsbrand","茅台");//品牌名称
        int  saleType=getParaToInt("saleType", 1);
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
//        time = FormatUtils.formatDetail(time);
        try {
            List<Record> list = goodsbrandService.getGoodsbrandPriceInfo(goodsbrand, saleType, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     * 品牌印象
     */
    public void getGoodsbrandFace(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        String goodsbrand=getPara("goodsbrand","茅台");//品牌名称
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        time = FormatUtils.formatDetail(time);
        try {
            List<Record> list = goodsbrandService.getGoodsbrandFace(goodsbrand,time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     * 品牌口碑变化
     */
    public void getGoodsbrandFaceChange(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        String goodsbrand=getPara("goodsbrand","茅台");//品牌名称
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        time = FormatUtils.formatDetail(time);
        try {
            List<Record> list = goodsbrandService.getGoodsbrandFaceChange(goodsbrand,time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }





    /**品牌聚焦-》品牌对比-》品牌详情------------------------------------------
     * 线上品牌对比-品牌top5
     */
    public void getStoreTop5(){
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
            List<Record> list = goodsbrandService.getGoodsbrandTop5(category_id,time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     * 按品牌名搜索-分页模糊查询
     */
    public void getGoodsbrandByName(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        int pageNumber=getParaToInt("pageNumber",1);
        String brandName=getPara("brandName","");//品牌名称
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
            List<Record> list = goodsbrandService.getGoodsbrandByName(category_id,pageNumber, brandName, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }


    /** 品牌分析-》品牌对比 二层页面--------------------------------------------------------------
     * 线上品牌销售对比
     */
    public void getGoodsbrandAttrduibi(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        String[] goodsbrandList=getParaValues("goodsbrandList[]");//品牌名称
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        time = FormatUtils.formatDetail(time);
        try {
            List<Record> list = goodsbrandService.getGoodsbrandAttrduibi(goodsbrandList, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }








}
