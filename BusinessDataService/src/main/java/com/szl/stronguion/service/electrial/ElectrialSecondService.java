package com.szl.stronguion.service.electrial;

import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.electrial.AllSaleGood;
import com.szl.stronguion.utils.FormatUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小龙
 * on 15-11-2
 * at 下午3:56.
 */

public class ElectrialSecondService {
  private AllSaleGood  allSaleGood=new AllSaleGood();

    public List<Record> getChannelTotal(int seqType,String[] channel,int pageNumber,int  saleType,String category_id,String price,String start_time,String end_time){

            long record_total=allSaleGood.getTotalPageSize(channel,saleType,category_id,price,start_time,end_time);
            List<Record> list=allSaleGood.getChannelTotal(seqType,channel,pageNumber,saleType,category_id,price,start_time,end_time);
            if (list.size()>0){
                list.get(0).set("total_page",Math.ceil(record_total/(AllSaleGood.PAGESIZE*1.0)));
                for (Record record:list){
                    int sale_flag=Double.valueOf(record.get("goods_sales_flag").toString()).intValue();
//                    int sale_flag=record.getInt("goods_sales_flag");
                    if (sale_flag>=0){
                        record.set("icon1","up");
                    }else {
                        record.set("goods_sales_flag",-sale_flag);
                        record.set("icon1","down");
                    }
                }
            }
            return list;

    }

    public List<Record> getAllChannelTotal(int seqType,String[] channel,int pageNumber,int  saleType,String category_id,String price,String start_time,String end_time){

        try {
            List<Record> list=allSaleGood.getAllChannelTotal(seqType,channel,pageNumber,saleType,category_id,price,start_time,end_time);
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<Record> getDetailMarketSale(String[] channelList,int  saleType,String category_id,String price,String start_time,String end_time){

        List<Record> list2 = allSaleGood.getDetailMarketSale(channelList,saleType,category_id,price,start_time,end_time);
        int dayCount= FormatUtils.getDayCount(start_time, end_time);
        List<Record> list=new ArrayList<Record>();
        for (int i = dayCount; i > 0; i--) {
            String  time= FormatUtils.getDayTime(-i, end_time);
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
                r2.set("total",0);
                list.add(r2);
            }
        }
        return list;
    }


    public List<Record> getGoodsBrandPercent(String[] channelList,int  saleType,String category_id,String price,String start_time,String end_time){
        try {
            List<Record> list=new ArrayList<Record>();
            double other_percent=0;
            List<Record> list1=allSaleGood.getGoodsBrandPercent(channelList,saleType,category_id,price,start_time,end_time);
            if (list1.size()>10){
                for (int i=0;i<list1.size();i++){
                    if (i<10){
                        list.add(list1.get(i));
                    }else {
                        other_percent+=Double.valueOf(list1.get(i).get("percent").toString());
                    }
                }
                list.add(new Record().set("percent",other_percent).set("goodsbrand","其它"));
            }else {
                list=list1;
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //获取时间段内所有渠道
    public List<Record> getAllChannelByTime(String price,String category_id,String start_time,String end_time){
        return allSaleGood.getAllChannelByTime(price,category_id,start_time,end_time);
    }
    //电商交易概览-》细分行业产品参数偏好-》二层页面----------------------------------

    public List<Record> getDetailGoodsInfo(int good_attr_type,int  saleType,String category_id,String good_attr,String start_time,String end_time){

        List<Record> list2 = allSaleGood.getDetailGoodsInfo(saleType,category_id,good_attr_type,good_attr,start_time,end_time);
        int dayCount= FormatUtils.getDayCount(start_time, end_time);
        List<Record> list=new ArrayList<Record>();
        for (int i = dayCount; i > 0; i--) {
            String  time= FormatUtils.getDayTime(-i, end_time);
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
                r2.set("total",0);
                list.add(r2);
            }
        }
        return list;
    }

    public List<Record> getGoodsBrandInfo(int good_attr_type,String good_attr,int  saleType,String category_id,String start_time,String end_time){
        return allSaleGood.getGoodsBrandInfo(good_attr_type,good_attr,saleType,category_id,start_time,end_time);
    }

    public List<Record> getGoodsNameInfo(int good_attr_type,String good_attr,String category_id,String start_time,String end_time){
        return allSaleGood.getGoodsNameInfo(good_attr_type,good_attr,category_id,start_time,end_time);
    }

    public List<Record> getGoodsFocus(int good_attr_type,String good_attr,String category_id,String start_time,String end_time){
        return allSaleGood.getUserFocus(good_attr_type,good_attr,category_id,start_time,end_time);
    }

    public List<Record> getGoodsArea(String good_attr,int  saleType,String category,String start_time,String end_time){
        return allSaleGood.getGoodsArea(good_attr,saleType,category,start_time,end_time);
    }
}
