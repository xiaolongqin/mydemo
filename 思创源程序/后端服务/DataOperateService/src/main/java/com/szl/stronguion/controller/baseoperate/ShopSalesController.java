package com.szl.stronguion.controller.baseoperate;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.szl.stronguion.service.baseoperate.OrderAim1Serv;
import com.szl.stronguion.utils.FormatUtils;
import com.szl.stronguion.utils.JsonHelp;

/**
 * Created by Tyfunwang on 2015/7/2.
 */
public class ShopSalesController extends Controller {
    private OrderAim1Serv orderAim1Serv = new OrderAim1Serv();

    //获取店铺销售分析结果
    public void getShopSales() {
// dateType 0:客户选择时间段,1：昨天，2：最近三天，3：最近7天，4：最近一个月，5：最近一季度,6:最近14天
        int dateType = getParaToInt("dateType");//dateType=0 ：客户选择时间段
        String[] time = new String[2];
        String address=getPara("address");
        if(address!=null&&address.equals("全部")){
            address="";
        }

        
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        
        try {
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(orderAim1Serv.getOrderAim1(time[0], time[1],address))));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }
}
