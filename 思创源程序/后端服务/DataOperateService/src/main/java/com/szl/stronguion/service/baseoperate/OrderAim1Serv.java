package com.szl.stronguion.service.baseoperate;

import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.baseoperate.OrderAim1;

import java.util.*;

/**
 * Created by Tyfunwang on 2015/7/2.
 */
public class OrderAim1Serv {
    private OrderAim1 orderAim1 = new OrderAim1();
    //获取店铺销售分析
    public List<Record> getOrderAim1(String startTime,String endTime,String address){
       return orderAim1.getOrderAim1(startTime,endTime,address);
    }
}
