package com.szl.stronguion.service.baseoperate;

import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.baseoperate.AppChannelAim2;
import com.szl.stronguion.utils.FormatUtils;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/6/30.
 */
public class ChannelAim2Serv {
    private AppChannelAim2 aim2 = new AppChannelAim2();
    //获取柱状图
    public List<Record> getPuAndUv(String prod_name,int type,String startTime,String endTime) {
        if (prod_name.equals("全部")){
            if (type>0){
                String[] str=FormatUtils.getDateTime(type);
                return aim2.getPuAndUv(str[0],str[1]);
            }
            return aim2.getPuAndUv(startTime,endTime);
        }else {
            if (type>0){
                String[] str=FormatUtils.getDateTime(type);
                return aim2.getPuAndUv(prod_name,str[0],str[1]);
            }
            return aim2.getPuAndUv(prod_name,startTime,endTime);
        }

    }

    public List<Record> getLineChart(String type) {
        return null;
    }
}
