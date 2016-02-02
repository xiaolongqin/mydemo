package com.szl.stronguion.controller.baseoperate;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.service.baseoperate.ChannelAim1Serv;
import com.szl.stronguion.service.baseoperate.ChannelAim2Serv;
import com.szl.stronguion.service.baseoperate.PageAim1Serv;
import com.szl.stronguion.utils.JsonHelp;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
/**
 * Created by Tyfunwang on 2015/6/30.
 */

/**
 * 业务发展波动监控 *
 * 以下数据均是当前日期前7天的数据 *
 * *
 */
public class OperationMonitorController extends Controller {
    private ChannelAim1Serv channelAim1Serv = new ChannelAim1Serv();
    private ChannelAim2Serv channelAim2Serv = new ChannelAim2Serv();
    private PageAim1Serv pageAim1Serv = new PageAim1Serv();


    /**
     * 获取用户总数、订单总数、店铺总数
     * *
     */
    public void getThreeTotal() {

        try {
            Map<String,List<Record>> maps= channelAim1Serv.getThreeTotal();
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(maps)));
        } catch (Exception e) {
            JsonHelp.buildFailed(e.getMessage());
        }

    }

    /**
     * sl_rpt_app_channel_aim1  app推广分渠道统计日报*
     * *
     */
    public void getSaveRate() {
        //保存率
        String prod_name=getPara("prod_name");
        int type = getParaToInt("type",0);
        String startTime=getPara("startTime");
        String endTime=getPara("endTime");
        try {
            List<Record> list = channelAim1Serv.getSaveRate(prod_name,type,startTime,endTime);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            JsonHelp.buildFailed(e.getMessage());
        }

    }

    public void getTransRate() {
        //转化率
        String prod_name=getPara("prod_name");
        int type = getParaToInt("type",0);
        String startTime=getPara("startTime");
        String endTime=getPara("endTime");
        try {
            List<Record> list = channelAim1Serv.getTransRate(prod_name,type,startTime,endTime);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            JsonHelp.buildFailed(e.getMessage());
        }
    }

    public void getTotal() {
        //保存量，到达量
        try {
            List<Record> list = channelAim1Serv.getTotal();
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            JsonHelp.buildFailed(e.getMessage());
        }
    }


    /**
     * sl_rpt_app_page_aim1  app页面转换率分析*
     * *
     */
    public void getPuAndUv() {
        //获取柱状图
        String prod_name=getPara("prod_name");
        int type = getParaToInt("type",0);
        String startTime=getPara("startTime");
        String endTime=getPara("endTime");

        try {
            List<Record> list = channelAim2Serv.getPuAndUv(prod_name,type,startTime,endTime);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            e.printStackTrace();
            JsonHelp.buildFailed(e.getMessage());
        }
    }


    /**
     * sl_rpt_app_channel_aim1  app推广分渠道统计日报*
     * sl_rpt_app_page_aim1  app页面转换率分析*
     * 需要两张表一起完成*
     */
    public void getLineChart() {
        //获取波浪图
        int type=getParaToInt("type",0); //时间选取方式
        int pv_name = getParaToInt("pv_name", 1);//pv;uv到达用户
        String startTime=getPara("startTime");
        String endTime=getPara("endTime");
        String prod_name=getPara("prod_name");
        if(prod_name!=null&&prod_name.equals("全部")){
            prod_name="";
        }
        try {
            try {
                Map<String, Object> map = new TreeMap<String, Object>();
                Map<String,List<Record>> table1 = channelAim1Serv.getFirstTable(type,pv_name,startTime,endTime,prod_name);
                Map<String, Map<String, Object>> table2 = channelAim1Serv.getSecondTable(type,pv_name,startTime,endTime,prod_name);
                if (table1.isEmpty()||table1==null){
                    map.put("first", "null");
                }else {
                    map.put("first", table1);
                }
                map.put("second", table2);
                renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
            } catch (Exception e) {
                renderJson(JsonHelp.buildFailed(e.getMessage()));
            }
        } catch (Exception e) {
            JsonHelp.buildFailed(e.getMessage());
        }
    }


}
