package com.szl.stronguion.service.customercharacter;

import com.alibaba.fastjson.JSONArray;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.szl.stronguion.model.customercharacter.ThresholdValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tyfunwang on 2015/7/13.
 */
@Before(Tx.class)
public class ThresholdValueServ {
    private ThresholdValue thresholdValue = new ThresholdValue();

    //添加阀值
    public boolean initThresholdValue(int id, int thId) {
        //add threshold value
        Map<String, Object> thresvalue = new HashMap<String, Object>();
        thresvalue.put(ThresholdValue.UID, id);
        thresvalue.put(ThresholdValue.THRESID, thId);
        thresvalue.put(ThresholdValue.STARTTIME, null);
        thresvalue.put(ThresholdValue.ENDTIME, null);
        thresvalue.put(ThresholdValue.STARTLOGINNUM, null);
        thresvalue.put(ThresholdValue.ENDLOGINNUM, null);
        thresvalue.put(ThresholdValue.STARTORDERNUM, null);
        thresvalue.put(ThresholdValue.ENDORDERNUM, null);
        thresvalue.put(ThresholdValue.STARTORDERPRICE, null);
        thresvalue.put(ThresholdValue.ENDORDERPRICE, null);
        if (thresholdValue.addThresholdValue(thresvalue)) return true;
        return false;
    }

    //添加阀值
    public boolean addThresholdValue(int id, String json) {
        for (Object object : JSONArray.parseArray(json)) {
            Map<String, Object> obj = (Map<String, Object>) object;
            //add threshold value
            Map<String, Object> thresvalue = new HashMap<String, Object>();
            thresvalue.put(ThresholdValue.UID, id);
            thresvalue.put(ThresholdValue.THRESID, obj.get(ThresholdValue.THRESID));
            thresvalue.put(ThresholdValue.STARTTIME, obj.get(ThresholdValue.STARTTIME));
            thresvalue.put(ThresholdValue.ENDTIME, obj.get(ThresholdValue.ENDTIME));
            thresvalue.put(ThresholdValue.STARTLOGINNUM, obj.get(ThresholdValue.STARTLOGINNUM));
            thresvalue.put(ThresholdValue.ENDLOGINNUM, obj.get(ThresholdValue.ENDLOGINNUM));
            thresvalue.put(ThresholdValue.STARTORDERNUM, obj.get(ThresholdValue.STARTORDERNUM));
            thresvalue.put(ThresholdValue.ENDORDERNUM, obj.get(ThresholdValue.ENDORDERNUM));
            thresvalue.put(ThresholdValue.STARTORDERPRICE, obj.get(ThresholdValue.STARTORDERPRICE));
            thresvalue.put(ThresholdValue.ENDORDERPRICE, obj.get(ThresholdValue.ENDORDERPRICE));
            if (thresholdValue.addThresholdValue(thresvalue)) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    //修改阀值
    public boolean updateThresholdValue(int id, String json) {
        for (Object object : JSONArray.parseArray(json)) {
            Map<String, Object> obj = (Map<String, Object>) object;
            //add threshold value
            Object threshold = obj.get(ThresholdValue.THRESID);
            Object startTime = obj.get(ThresholdValue.STARTTIME);
            Object endTime = obj.get(ThresholdValue.ENDTIME);
            Object startLoginNum = obj.get(ThresholdValue.STARTLOGINNUM);
            Object endLoginNum = obj.get(ThresholdValue.ENDLOGINNUM);
            Object startOrderNum = obj.get(ThresholdValue.STARTORDERNUM);
            Object endOrderNum = obj.get(ThresholdValue.ENDORDERNUM);
            Object startOrderPrice = obj.get(ThresholdValue.STARTORDERPRICE);
            Object endOrderPrice = obj.get(ThresholdValue.ENDORDERPRICE);
            if (thresholdValue.updateThresholdValue(id, threshold, startTime, endTime, startLoginNum, endLoginNum, startOrderNum, endOrderNum, startOrderPrice, endOrderPrice) == 1) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    //get thresholds
    public List<Record> getThreshold(int id) {
        return thresholdValue.getThreshold(id);
    }


    //get indicator percent
    public Map<String, Object> getIndicator(String uid) {
        List<Record> list=getThreshold(Integer.valueOf(uid));
        Map<String, Object> maps = new HashMap<String, Object>();
        String str1=" or (slu.login_num >=tv.start_login_num and slu.login_num <=tv.end_login_num)";
        String str2=" or (slu.orders_num>=tv.start_order_num and slu.orders_num<=tv.end_order_num)";
        String str3=" or (slu.orders_total_price>=tv.start_order_price and slu.orders_total_price<=tv.end_order_price)";
        double total=Double.valueOf(thresholdValue.getTotalUser().get("total").toString());
        double percent=0.0;
        for (int i = 1; i < 7; i++) {
            String start_time=list.get(i - 1).get("start_time");
            String end_time=list.get(i-1).get("end_time");
            List<Record> list1=null;
            if(!list.get(i - 1).get("start_login_num").equals("null")&&list.get(i - 1).get("start_order_num").equals("null")&&list.get(i - 1).get("start_order_price").equals("null")){
                String str=str1;
                list1 =thresholdValue.getIndicator(uid, i,str);
            }else if (list.get(i - 1).get("start_login_num").equals("null")&&!list.get(i - 1).get("start_order_num").equals("null")&&list.get(i - 1).get("start_order_price").equals("null")){
                String str=str2;
                list1 =thresholdValue.getIndicator(uid, i,str);
            }else if (list.get(i - 1).get("start_login_num").equals("null")&&list.get(i - 1).get("start_order_num").equals("null")&&!list.get(i - 1).get("start_order_price").equals("null")){
                String str=str3;
                list1 =thresholdValue.getIndicator(uid, i,str);
            }else if (!list.get(i - 1).get("start_login_num").equals("null")&&!list.get(i - 1).get("start_order_num").equals("null")&&list.get(i - 1).get("start_order_price").equals("null")){
                String str=str1+str2;
                list1 =thresholdValue.getIndicator(uid, i,str);
            }else if (!list.get(i - 1).get("start_login_num").equals("null")&&list.get(i - 1).get("start_order_num").equals("null")&&!list.get(i - 1).get("start_order_price").equals("null")){
                String str=str1+str3;
                list1 =thresholdValue.getIndicator(uid, i,str);
            }else if (list.get(i - 1).get("start_login_num").equals("null")&&!list.get(i - 1).get("start_order_num").equals("null")&&!list.get(i - 1).get("start_order_price").equals("null")){
                String str=str2+str3;
                list1 =thresholdValue.getIndicator(uid, i,str);
            }else if (!list.get(i - 1).get("start_login_num").equals("null")&&!list.get(i - 1).get("start_order_num").equals("null")&&!list.get(i - 1).get("start_order_price").equals("null")){
                 String str=str1+str2+str3;
                 list1 =thresholdValue.getIndicator(uid, i, str);
            }else if (list.get(i - 1).get("start_login_num").equals("null")&&list.get(i - 1).get("start_order_num").equals("null")&&list.get(i - 1).get("start_order_price").equals("null")){
                 String str="";
                 list1 =thresholdValue.getIndicator(uid, i, str);
            }
            double  fenzi=Double.valueOf(list1.get(0).get("fenzi").toString());
            if (total!=0.0){
                percent=fenzi/total;
//                String percent=String.format("%.2f",fenzi/total);
            }
            Record record=new Record();
            record.set("percent",String.format("%.2f",percent*100));
            Record newRecord=new Record();
            newRecord.set("start_time",start_time);
            newRecord.set("end_time",end_time);
            list1.clear();
            list1.add(record);
            list1.add(newRecord);
            maps.put(i + "", list1);
        }
        return maps;
    }
}
