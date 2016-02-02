package com.szl.stronguion.controller.salesanalysis;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.service.salesanalysis.OrderAnalysisServ;
import com.szl.stronguion.utils.FormatUtils;
import com.szl.stronguion.utils.JsonHelp;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/7/28.
 */
public class RepeatOrderController extends Controller {
    private OrderAnalysisServ analysisServ = new OrderAnalysisServ();
    /**
     * 重复购买分析* 
     * dateType: 1:昨天，2：最近3天，3：最近7天，4：最近一个月，5：最近一季度 6:最近14天 *
     * paramType: 1:购买2次以上商品特征分析,2:购买2次以上商品店铺分析,3:购买2次以上商品用户分析*
     */
    public void repeatOrder(){
        int paramType = getParaToInt("paramType");// paramType: 1:购买2次以上商品特征分析,2:购买2次以上商品店铺分析,3:购买2次以上商品用户分析
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段

        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        try {
            List<Record> list = analysisServ.getRepeatOrder(paramType, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }
    
    //获取对应类别的地图列表
    public void getAreaByType(){
        String goodName = getPara("goodName");//商品名称
        String shopName = getPara("shopName");//店铺名称
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段

        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }

        try {
           List<Record> list = analysisServ.getAreaByType(goodName,shopName, time[0], time[1]);
           renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
        
    }
    
}
