package com.szl.stronguion.service.channel;

import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.channel.SalesChannel;
import com.szl.stronguion.utils.FormatUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 小龙
 * on 15-10-15
 * at 下午4:28.
 */

public class ChannelService {
    private SalesChannel salesChannel=new SalesChannel();
    //线上渠道销售总额排行
    public Map<String,List<Record>> getOnlineChannelTop(String category,String startTime,String endTime){
        Map<String,List<Record>> map=new HashMap<String, List<Record>>();

        map.put("first",salesChannel.getOnlineChannelTop(category,startTime,endTime));
        map.put("second",salesChannel.getOnlineChannelPercent(category,startTime,endTime));
        return map;

    }
    public Map<String,List<Record>> getDetailChannelTop(String goodName,String startTime,String endTime){
        Map<String,List<Record>> map=new HashMap<String, List<Record>>();
        double record_total=0;
        double percert=0;
        try {
            record_total =salesChannel.getDetailChannelTopPercent(goodName,startTime, endTime).getBigDecimal("total_sum").doubleValue();
        }catch (NullPointerException e){

        }
        List<Record> list1=salesChannel.getDetailChannelTop(goodName,startTime,endTime);
        for(Record r:list1){
            if (record_total==0.0){
              r.set("percent",0.0);
            }else {
                     percert=(Double.valueOf(r.get("total").toString())/record_total)*100;
                     r.set("percent",String.format("%.2f",percert));
            }
        }

        map.put("first",list1);
        return map;

    }

    public List<Record>  getAllGoodName(){
        return salesChannel.getAllGoodName();
    }

    public List<Record>  getAllChannelName(){
        return salesChannel.getAllChannelName();
    }

    public List<Record>  getChannelName(int saleType){
        return salesChannel.getChannelName(saleType);
    }



    //线上渠道销售情况
    public List<Record> getOnlineChannelSale(String category_id,int saleType,String channelName,String startTime,String endTime){
        List<Record>  list2=salesChannel.getOnlineChannelSale1(category_id,saleType,channelName, startTime, endTime);
        int dayCount= FormatUtils.getDayCount(startTime, endTime);
            List<Record> list=new ArrayList<Record>();
            for (int i = dayCount; i > 0; i--) {
                String  time=FormatUtils.getDayTime(-i,endTime);
                boolean flag=false;
                if (list2!=null){
                    for(Record r:list2){
                        if (r.get("day").toString().equals(time)&&r.get("channel").equals(channelName)){
                            list.add(r);
                            flag=true;
//                        list2.remove(r);
                        }
                    }
                }

                if (!flag){
                    Record r2=new Record();
                    r2.set("day",time);
                    r2.set("channel",channelName);
                    if (saleType==1){
                        r2.set("total",0);
                    }else {
                        r2.set("total",0);
                    }
                    list.add(r2);
                }
            }
        return list;

    }
    //渠道细分市场
    public List<Record> getOnlineChannelDetailSale(int saleType,String channelName,String startTime,String endTime){
        List<Record> list=null;
        if (saleType==1){
            list=salesChannel.getChannelDetailSale1(channelName, startTime, endTime);
        }else {
            list=salesChannel.getChannelDetailSale2(channelName, startTime, endTime);
        }
        return list;
    }

    public List<Record> getChannelShopTop(String category_id,int saleType,String channelName,String startTime,String endTime){
        List<Record> list=null;
        if (saleType==1){
            list=salesChannel.getChannelShopTop1(category_id,channelName, startTime, endTime);
        }else {
            list=salesChannel.getChannelShopTop2(category_id,channelName, startTime, endTime);
        }
        return list;
    }

    public List<Record> getChannelGoodTop(String category_id,int saleType,String channelName,String startTime,String endTime){
        List<Record> list=null;
        if (saleType==1){
            list=salesChannel.getChannelGoodTop1(category_id,channelName, startTime, endTime);
        }else {
            list=salesChannel.getChannelGoodTop2(category_id,channelName, startTime, endTime);
        }
        return list;
    }

    //渠道对比
    public Map<String,List<Record>> getChannelduibi(String category_id,int saleType,String[] channelName,String startTime,String endTime){
        Map<String,List<Record>> map=new HashMap<String, List<Record>>();
        int dayCount= FormatUtils.getDayCount(startTime, endTime);
        List<Record> list2 = salesChannel.getChannelduibi(category_id,channelName,startTime,endTime);
        for(String channel:channelName){

                List<Record> list=new ArrayList<Record>();
                for (int i = dayCount; i > 0; i--) {
                    String  time=FormatUtils.getDayTime(-i,endTime);
                    boolean flag=false;
                    if (list2!=null){
                        for(Record r:list2){
                            if (r.get("day").toString().equals(time)&&r.get("channel").equals(channel)){
                                list.add(r);
                                flag=true;
//                        list2.remove(r);
                            }
                        }
                    }

                    if (!flag){
                        Record r2=new Record();
                        r2.set("day",time);
                        r2.set("channel",channel);
                        r2.set("total_sale",0);
                        r2.set("total_num",0);
                        list.add(r2);
                    }
                }
                map.put(channel,list);
        }
        return map;
    }

    //渠道对比（各细分市场销售对比）
    public Map<String,List<Record>> getChannelduibiByMonth(String category_id,int saleType,String startTime,String endTime){
        Map<String,List<Record>> map=new HashMap<String, List<Record>>();

        List<Record> list2 = salesChannel.getChannelduibiByMonth(category_id,startTime, endTime);
        List<Record> channel_Names=salesChannel.getAllChannelName();
        List<Record> good_Names=salesChannel.getAllGoodName();
        for(Record channel:channel_Names){

            String channel1=channel.getStr("channel");
            List<Record> list=new ArrayList<Record>();
            for(Record goodName:good_Names){
                String good=goodName.getStr("goodName");
                boolean flag=false;
                if (list2!=null){
                    for(Record r:list2){
                        if (r.get("channel").equals(channel1)&&r.get("goodName").equals(good)){
                            list.add(r);
                            flag=true;
                        }
                    }
                }

                if (!flag){
                    Record r2=new Record();
                    r2.set("channel",channel1);
                    r2.set("goodName",good);
                    r2.set("total_sale",0);
                    r2.set("total_num",0);
                    list.add(r2);
                }
            }

            map.put(channel1,list);
        }
        return map;
    }




}
