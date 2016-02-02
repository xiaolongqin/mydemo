package com.szl.stronguion.controller.storeAnalysis;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.controller.menus.AccountController;
import com.szl.stronguion.model.menus.Account;
import com.szl.stronguion.service.storeAnalysis.StoreAnalysisService;
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
 * on 15-11-23
 * at 上午9:21.
 */

public class StoreAnalysisController extends Controller {
    private StoreAnalysisService storeAnalysisService=new StoreAnalysisService();

    //店铺跟踪-》店铺整体扫描--------------------------------------
    //线上店铺销售排行--分页  seqType:1--升序分页    2--降序排列
    public void getStoreSaleTotal(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        int  saleType=getParaToInt("saleType", 1);
        int pageNumber=getParaToInt("pageNumber",1);
        int seqType=getParaToInt("seqType", 1);  //1--升序分页    2--降序排列
        String store_name=getPara("storeName","");
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
            List<Record> list = storeAnalysisService.getStoreSaleTotal(seqType,category_id,store_name,channelList, pageNumber, saleType, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     * 导出店铺扫描数据
     */
    public void exportDataExcel(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        int  saleType=getParaToInt("saleType", 1);
        int seqType=getParaToInt("seqType", 1);  //1--升序分页    2--降序排列
        String store_name=getPara("storeName","");
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
            List<Record> list = storeAnalysisService.getExportStoreTotal(seqType, category_id, store_name, channelList, saleType, time[0], time[1]);
            sendFile(list,saleType);
            renderNull();
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }
    private void sendFile(List<Record> list,int  saleType) {
        try {
            HttpServletResponse response = getResponse();
            String filename = "线上店铺销售排行.xls";//设置下载时客户端Excel的名称
            filename = new String(filename.getBytes(),"iso-8859-1");
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            OutputStream ouputStream = response.getOutputStream();

            WritableWorkbook wb= JxlTest.StoreToExcel(list, saleType, ouputStream);
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

    //获取所有渠道
    public void getAllChannel(){
        try {
            List<Record> list = storeAnalysisService.getAllChannel();
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

   //线上电商店铺销售排行top10
    public void getStoreTopBychannel(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        int  saleType=getParaToInt("saleType", 1);
        String channel=getPara("channel","全部");
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
            List<Record> list = storeAnalysisService.getStoreTopBychannel(category_id,channel, saleType, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

//    //获取所有类目
//    public void getAllCategory(){
//        try {
//            List<Record> list = storeAnalysisService.getAllCategory();
//            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
//        } catch (Exception e) {
//            renderJson(JsonHelp.buildFailed(e.getMessage()));
//        }
//    }

//    //线上细分行业店铺排行top10
//    public void getStoreTopByCategory(){
//        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
//        int  saleType=getParaToInt("saleType", 1);
//        String category=getPara("category", storeAnalysisService.getAllCategory().get(0).get("category").toString());
//        String[] time = new String[2];
//        if (dateType == 0) {
//            time[0] = getPara("startTime");
//            time[1] = getPara("endTime");
//        } else {
//            time = FormatUtils.getDateTime(dateType);
//        }
//        try {
//            List<Record> list = storeAnalysisService.getStoreTopByCategory(category, saleType, time[0], time[1]);
//            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
//        } catch (Exception e) {
//            e.printStackTrace();
//            renderJson(JsonHelp.buildFailed(e.getMessage()));
//        }
//    }

    //店铺跟踪-》店铺详细---------------------------------------------------------------
    //店铺销售情况

    public void getStoreDetaiInfo(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        int  saleType=getParaToInt("saleType", 1);
        String storeId=getPara("storeId");
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
            List<Record> list = storeAnalysisService.getStoreDetaiInfo(category_id,saleType, storeId, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

//    //店铺产品销售排行(品牌
//
//    public void getStoreGoodsAndBrandInfo(){
//        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
//        int  saleType=getParaToInt("saleType", 1); //1--销售额   2--销售量
//        int  attrType=getParaToInt("attrType", 1); //attrType:1--品牌  2--产品
//        String storeId=getPara("storeId");
//        String[] time = new String[2];
//        if (dateType == 0) {
//            time[0] = getPara("startTime");
//            time[1] = getPara("endTime");
//        } else {
//            time = FormatUtils.getDateTime(dateType);
//        }
//        try {
//            List<Record> list = storeAnalysisService.getStoreGoodsAndBrandInfo(attrType, storeId, saleType, time[0], time[1]);
//            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
//        } catch (Exception e) {
//            e.printStackTrace();
//            renderJson(JsonHelp.buildFailed(e.getMessage()));
//        }
//    }

    //店铺评价情况
    public void getStoreComment(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        String storeId=getPara("storeId");
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        time = FormatUtils.formatDetail(time);
        try {
            List<Record> list = storeAnalysisService.getStoreComment(storeId, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    //店铺跟踪-》店铺对比--------------------------------------------------------------
    //线上店铺对比
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
            List<Record> list = storeAnalysisService.getStoreTop5(category_id, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    //按店铺名搜索
    public void getStroreByName(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        int pageNumber=getParaToInt("pageNumber",1);
        String storeName=getPara("storeName","");//店铺名称
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
            List<Record> list = storeAnalysisService.getStroreByName(category_id,pageNumber,storeName,time[0],time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    //店铺跟踪-》店铺对比---二层页面--------------------------------------------------------------
    //线上店铺销售对比
    public void getStoreSaleduibi(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        int  saleType=getParaToInt("saleType", 1); //1--销售额   2--销售量
        String[] storeNameList=getParaValues("storeNameList[]");//店铺名称
        String[] storeIdList=getParaValues("storeIdList[]");//店铺ID
//        String[] storeNameList={"郎酒一号旗舰店","郎酒一号折扣店"};//店铺名称
//        String[] goodsIdList={"48153","48798"};//店铺ID
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        time = FormatUtils.formatDetail(time);

        try {
            Map<String,List<Record>> map = storeAnalysisService.getStoreSaleduibi(saleType,storeNameList, storeIdList, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    //店铺各项参数对比
    public void getStoreAttrduibi(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        String[] storeIdList=getParaValues("storeIdList[]");//店铺ID
//        String[] storeIdList={"48153","48798"};//店铺ID
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        time = FormatUtils.formatDetail(time);
        try {
            List<Record> list = storeAnalysisService.getStoreAttrduibi(storeIdList, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }



}
