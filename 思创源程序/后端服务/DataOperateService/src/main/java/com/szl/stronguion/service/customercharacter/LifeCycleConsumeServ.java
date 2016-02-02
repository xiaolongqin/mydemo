package com.szl.stronguion.service.customercharacter;

import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.customercharacter.LifeCycleConsume;
import com.szl.stronguion.service.salesanalysis.OrderAnalysisServ;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/7/15.
 */
public class LifeCycleConsumeServ {
    private LifeCycleConsume cycleConsume = new LifeCycleConsume();
    private OrderAnalysisServ orderAnalysisServ = new OrderAnalysisServ();

    public List<Record> getTop5(String startTime, String endTime) {
        return cycleConsume.getTop5(startTime, endTime);
    }

    //消费客户地域分布分*
    public List<Record> getCustDistri(String startTime, String endTime, int type) {
        List<Record> list = null;
        switch (type) {
            case 1:
                list = cycleConsume.getTotalPrice(startTime, endTime);
                break; //成交金额
            case 2:
                list = cycleConsume.getCountUid(startTime, endTime);
                break;//购买人数
            case 3:
                list = cycleConsume.getTotalOrders(startTime, endTime);
                break; //成交笔数
            case 4:
                list = cycleConsume.getPricePer(startTime, endTime);
                break;  //客单价
            default:
                break;
        }
        orderAnalysisServ.modifyValue(list);
        return list;
    }
}
