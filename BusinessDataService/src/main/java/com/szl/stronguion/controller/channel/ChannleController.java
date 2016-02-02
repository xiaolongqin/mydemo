package com.szl.stronguion.controller.channel;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.controller.menus.AccountController;
import com.szl.stronguion.model.menus.Account;
import com.szl.stronguion.service.channel.ChannelService;
import com.szl.stronguion.utils.FormatUtils;
import com.szl.stronguion.utils.JsonHelp;

import java.util.List;
import java.util.Map;

/**
 * Created by 小龙
 * on 15-10-15
 * at 下午4:20.
 */

public class ChannleController extends Controller {
    private ChannelService channelService = new ChannelService();

    /**
     * 渠道扫描模块---------
     *线上渠道销售总额排行
     */

    public void getOnlineChannelTop(){
        int dateType = getParaToInt("dateType",1);//dateType=0 ：客户选择时间段
        String category_id= ((Account)getSessionAttr(AccountController.ACCOUNTS)).get(Account.CATEGORYID).toString();
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }

        time = FormatUtils.formatDetail(time);

        try {
            Map<String,List<Record>> map = channelService.getOnlineChannelTop(category_id,time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     *线上电商细分行业交易总额排行
     **/

         public void getDetailChannelTop(){
        int dateType = getParaToInt("dateType", 1);//dateType=0  ：客户选择时间段
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
            Map<String,List<Record>> map = channelService.getDetailChannelTop(category_id, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     *     //获取所以good分类名称
     */
    public void getAllGoodName(){
        try {
            List<Record> list = channelService.getAllGoodName();
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     *获取所有渠道名称
     */
    public void getAllChannelName(){
        try {
            List<Record> list = channelService.getAllChannelName();
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     *按条件获取渠道名称
     */
    public void getChannelName(){
        int saleType=getParaToInt("saleType",1);//1--销售额  2--销售量 3--评论数
        try {
            List<Record> list = channelService.getChannelName(saleType);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     *     //线上渠道销售情况   saleType:1---销售额  ，2---销售量
     */
    public void getOnlineChannelSale(){
        int dateType = getParaToInt("dateType",1);//dateType=0 ：客户选择时间段
        int  saleType=getParaToInt("saleType",1);
        String channelName=getPara("channelName");
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
            List<Record> list = channelService.getOnlineChannelSale(category_id,saleType,channelName,time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     *     //渠道热门店铺TOP10  saleType:1---销售额  ，2---销售量
     */
    public void getChannelShopTop(){
        int dateType = getParaToInt("dateType",1);//dateType=0 ：客户选择时间段
        int saleType=getParaToInt("saleType",1);
        String channelName=getPara("channelName");
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
            List<Record> list = channelService.getChannelShopTop(category_id,saleType,channelName,time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }


    /**
     *     //天猫渠道热销产品TOP10  saleType:1---销售额  ，2---销售量
     */
    public void getChannelGoodTop(){
        int dateType = getParaToInt("dateType",1);//dateType=0 ：客户选择时间段
        int saleType=getParaToInt("saleType",1);
        String channelName=getPara("channelName");
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
            List<Record> list = channelService.getChannelGoodTop(category_id,saleType,channelName,time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     * //渠道对比--------
     *渠道销量情况对比对比
     */

    public void getChannelduibi(){
        int dateType = getParaToInt("dateType",1);//dateType=0 ：客户选择时间段
        int saleType=getParaToInt("saleType",1);
        String[] channelList=getParaValues("channelList[]");
        if (channelList==null){

            List<Record> list= channelService.getAllChannelName();
            channelList=new String[2];
            channelList[0]=list.get(0).get("channel").toString();
            channelList[1]=list.get(1).get("channel").toString();

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
            Map<String,List<Record>> map = channelService.getChannelduibi(category_id,saleType,channelList, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     *      //渠道对比（各细分市场销售对比）
     */
    public void getChannelGoodDuibi(){
        int dateType = getParaToInt("dateType",1);//dateType=0 ：客户选择时间段
        int saleType=getParaToInt("saleType",1); 
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
            Map<String,List<Record>> map = channelService.getChannelduibiByMonth(category_id,saleType,time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }
}
