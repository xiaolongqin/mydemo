package com.szl.stronguion.controller.electrial;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.controller.menus.AccountController;
import com.szl.stronguion.model.menus.Account;
import com.szl.stronguion.service.electrial.ElectrialService;
import com.szl.stronguion.utils.FormatUtils;
import com.szl.stronguion.utils.JsonHelp;

import java.util.List;

/**
 * Created by 小龙
 * on 15-10-15
 * at 下午4:20.
 */

public class ElectrialController extends Controller {
    private ElectrialService electrialService = new ElectrialService();
//    private String category="白酒";


    /**
     * 市场总体情况-------------
     * 线上电商总览
     * saleType:1---销售额  ，2---销售量 ，3---评论数
     */


    public void getOverView(){
        int  saleType=getParaToInt("saleType", 1);
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        String[] channelList=getParaValues("channelList[]");

        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        time = FormatUtils.formatDetail(time);
        String category_id= ((Account)getSessionAttr(AccountController.ACCOUNTS)).get(Account.CATEGORYID).toString();
        try {
            List<Record> list = electrialService.getOverView(channelList,category_id,saleType,time[0],time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }


    //市场细分行业---------------------------
    //细分市场销售排名 saleType:1---销售额  ，2---销售量

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
     *     //细分市场概览
     */

    public void getDetailMarket(){
        int  saleType=getParaToInt("saleType", 1);//1---销售额  ，2---销售量 ，3---评论数
        int dateType = getParaToInt("dateType",1);//dateType=0 ：客户选择时间段
        String category_id= ((Account)getSessionAttr(AccountController.ACCOUNTS)).get(Account.CATEGORYID).toString();

        String[] channelList=getParaValues("channelList[]");
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        time = FormatUtils.formatDetail(time);
        try {
            List<Record> map = electrialService.getDtailMarket(channelList,saleType,category_id,time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     *     //行业价格分布
     */
    public void getDetailPrice(){
        int dateType = getParaToInt("dateType",1);//dateType=0 ：客户选择时间段
        String category_id= ((Account)getSessionAttr(AccountController.ACCOUNTS)).get(Account.CATEGORYID).toString();

        String[] channelList=getParaValues("channelList[]");
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        time = FormatUtils.formatDetail(time);

        try {
            List<Record> map = electrialService.getDtailPrice(channelList,category_id,time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     *     //行业用户关注点透视
     */
    public void getUserFocus(){
        int dateType = getParaToInt("dateType",1);//dateType=0 ：客户选择时间段
        String category_id= ((Account)getSessionAttr(AccountController.ACCOUNTS)).get(Account.CATEGORYID).toString();
        String[] channelList=getParaValues("channelList[]");
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        time = FormatUtils.formatDetail(time);
        try {
            List<Record> map = electrialService.getUserFocus(channelList,category_id,time[0],time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }



    /**
     *     //获取所有产品分类名
     */
//    public void getElectrialAllGoodName(){
//        try {
//            List<Record> list = electrialService.getAllGoodName();
//            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
//        } catch (Exception e) {
//            renderJson(JsonHelp.buildFailed(e.getMessage()));
//        }
//    }
}
