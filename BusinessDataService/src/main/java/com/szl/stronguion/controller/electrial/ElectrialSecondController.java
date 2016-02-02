package com.szl.stronguion.controller.electrial;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.controller.menus.AccountController;
import com.szl.stronguion.model.menus.Account;
import com.szl.stronguion.service.electrial.ElectrialSecondService;
import com.szl.stronguion.utils.FormatUtils;
import com.szl.stronguion.utils.JsonHelp;
import com.szl.stronguion.utils.JxlTest;
import jxl.write.WritableWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by 小龙
 * on 15-11-2
 * at 下午3:51.
 */

public class ElectrialSecondController extends Controller{

    private ElectrialSecondService electrialSecondService=new ElectrialSecondService();


    /**
     * //电商交易概览-》市场细分行业-》二层页面----------------------------------
     *获取细分市场价格区间热销产品扫描  saleType:1---销售额  ，2---销售量
     * seqType:1--升序分页    2--降序排列
     */


    public void getDetailPriceInfo(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        int  saleType=getParaToInt("saleType", 1);
        int pageNumber=getParaToInt("pageNumber", 1);
        int seqType=getParaToInt("seqType", 1);  //1--升序分页    2--降序排列
        String price=getPara("price", "A1");
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
            List<Record> map = electrialSecondService.getChannelTotal(seqType,channelList,pageNumber,saleType,category_id, price, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     *     //细分市场销量变化
     */
    public void getDetailMarketSale(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        int  saleType=getParaToInt("saleType", 1);
        String price=getPara("price", "A1");
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
            List<Record> map = electrialSecondService.getDetailMarketSale(channelList,saleType, category_id, price, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     * 细分市场品牌占比
     */
    public void getGoodsBrandPercent(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        int  saleType=getParaToInt("saleType", 1);
        String price=getPara("price", "A1");
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
            List<Record> map = electrialSecondService.getGoodsBrandPercent(channelList,saleType, category_id, price, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     *获取时间段内所有渠道
     */
    public void getAllChannelByTime(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        String price=getPara("price", "A1");
        String goodName=getPara("goodName","白酒");
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
            List<Record> map = electrialSecondService.getAllChannelByTime(price,category_id,time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }




    /**
     * 导出获取细分市场价格区间热销产品扫描数据
     */

    public void exportDataExcel(){
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        int  saleType=getParaToInt("saleType", 1);
        int pageNumber=getParaToInt("pageNumber", 1);
        int seqType=getParaToInt("seqType", 1);  //1--升序分页    2--降序排列
        String price=getPara("price", "A1");
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
            List<Record> list = electrialSecondService.getAllChannelTotal(seqType,channelList,pageNumber,saleType,category_id, price, time[0], time[1]);
            sendFile(list,saleType);
            renderNull();
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    private void sendFile(List<Record> list,int  saleType) {
        try {
            HttpServletResponse response = getResponse();
            HttpServletRequest request = getRequest();
            String filename = "热销产品排名.xls";//设置下载时客户端Excel的名称
            filename = new String(filename.getBytes(),"iso-8859-1");
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            OutputStream ouputStream = response.getOutputStream();

            WritableWorkbook wb= JxlTest.toExcel(list,saleType,ouputStream);
            wb.write();
            wb.close();
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
