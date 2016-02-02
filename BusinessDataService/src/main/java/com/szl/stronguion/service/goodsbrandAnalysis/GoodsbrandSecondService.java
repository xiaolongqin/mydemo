package com.szl.stronguion.service.goodsbrandAnalysis;

import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.goodsbrandAnalysis.GoodsbrandSecond;
import com.szl.stronguion.utils.FormatUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小龙
 * on 15-11-30
 * at 下午3:39.
 */

public class GoodsbrandSecondService {
    private GoodsbrandSecond goodsbrandSecond=new GoodsbrandSecond();

    //品牌分析-》品牌特写》品牌各渠道销量占比》--------------------------------------------------------------
    //某渠道中品牌销售情况
    public List<Record> getGoodsbrandSaleBychannel(String goodsbrand,String channel,int  saleType,String start_time,String end_time){
        int dayCount= FormatUtils.getDayCount(start_time, end_time);
        List<Record> list2 = goodsbrandSecond.getGoodsbrandSaleBychannel(goodsbrand,channel, saleType, start_time, end_time);
        List<Record> list=new ArrayList<Record>();
        for (int i = dayCount+1; i > 0; i--) {
            String  time=FormatUtils.getDayTime(-i,end_time);
            boolean flag=false;
            if (list2!=null){
                for(Record r:list2){
                    if (r.get("day").toString().equals(time)){
                        list.add(r);
                        flag=true;
                    }
                }
            }
            if (!flag){
                Record r2=new Record();
                r2.set("day",time);
                r2.set("goodsbrand",goodsbrand);
                r2.set("channel",channel);
                r2.set("total",0);
                list.add(r2);
            }
        }

        for(int i=0;i<list.size();i++){
            if (i==0){
                list.get(i).set("speed",0);
            }else {
                double today=Double.valueOf(list.get(i).get("total").toString());
                double yesterday=Double.valueOf(list.get(i-1).get("total").toString());
                if (yesterday==0.0){
                    list.get(i).set("speed",100);
                }else {
                    list.get(i).set("speed",(double)Math.round(((today-yesterday)*100/yesterday)*100)/100);
                }
            }
        }
        list.remove(0);
        return list;
    }


    //某渠道品牌热销店铺排行
    public List<Record> getStoreTopByBrand(String goodsbrand,String channel,int  saleType,String start_time,String end_time){
        return goodsbrandSecond.getStoreTopByBrand(goodsbrand,channel,saleType,start_time,end_time);
    }

    //某渠道热销茅台产品TOP10
    public List<Record> getGoodsTopByBrand(String goodsbrand,String channel,int  saleType,String start_time,String end_time){
        return goodsbrandSecond.getGoodsTopByBrand(goodsbrand, channel, saleType, start_time, end_time);
    }


    //品牌分析-》品牌特写》品牌价格分布》--------------------------------------------------------------
    //品牌价格区间销售变化情况
    public List<Record> getGoodsbrandSaleByPrice(String goodsbrand,String price_attr,int  saleType,String start_time,String end_time){
        int dayCount= FormatUtils.getDayCount(start_time, end_time);
        List<Record> list2 = goodsbrandSecond.getGoodsbrandSaleByPrice(goodsbrand, price_attr, saleType, start_time, end_time);
        List<Record> list=new ArrayList<Record>();
        for (int i = dayCount+1; i > 0; i--) {
            String  time=FormatUtils.getDayTime(-i,end_time);
            boolean flag=false;
            if (list2!=null){
                for(Record r:list2){
                    if (r.get("day").toString().equals(time)){
                        list.add(r);
                        flag=true;
                    }
                }
            }
            if (!flag){
                Record r2=new Record();
                r2.set("day",time);
                r2.set("goodsbrand",goodsbrand);
                r2.set("price_attr",price_attr);
                r2.set("total",0);
                list.add(r2);
            }
        }

        for(int i=0;i<list.size();i++){
            if (i==0){
                list.get(i).set("speed",0);
            }else {
                double today=Double.valueOf(list.get(i).get("total").toString());
                double yesterday=Double.valueOf(list.get(i-1).get("total").toString());
                if (yesterday==0.0){
                    list.get(i).set("speed",100);
                }else {
                    list.get(i).set("speed",(double)Math.round(((today-yesterday)*100/yesterday)*100)/100);
                }
            }
        }
        list.remove(0);
        return list;
    }

    //品牌（茅台）500-1000元价格区间产品在各渠道的销量
    public List<Record> getBrandSaleByChannel(String goodsbrand,String price_attr,int  saleType,String start_time,String end_time){
        return goodsbrandSecond.getBrandSaleByChannel(goodsbrand,price_attr,saleType,start_time,end_time);
    }

    //品牌（茅台）500-1000元价格区间热销产品TOP10
    public List<Record> getGoodsSaleByBrand(String goodsbrand,String price_attr,int  saleType,String start_time,String end_time){
        return goodsbrandSecond.getGoodsSaleByBrand(goodsbrand,price_attr,saleType,start_time,end_time);
    }
    //品牌（茅台）500-1000元价格区间热销产品TOP10
    public List<Record> getbrandFaceByPrice(String goodsbrand,String price_attr,String start_time,String end_time){
        return goodsbrandSecond.getbrandFaceByPrice(goodsbrand,price_attr,start_time,end_time);
    }

    //品牌（茅台）500-1000元价格区间产品地区覆盖

    public List<Record> getGoodsSaleByArea(String goodsbrand,String price_attr,String area,int  saleType,String start_time,String end_time){
        return  goodsbrandSecond.getGoodsSaleByArea(goodsbrand,price_attr,area,saleType,start_time,end_time);
    }
 
    
}
