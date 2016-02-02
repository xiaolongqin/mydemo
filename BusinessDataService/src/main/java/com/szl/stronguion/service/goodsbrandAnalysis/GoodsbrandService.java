package com.szl.stronguion.service.goodsbrandAnalysis;

import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.goodsbrandAnalysis.GoodsbrandAnalysis;
import com.szl.stronguion.utils.FormatUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 小龙
 * on 15-11-24
 * at 下午2:57.
 */

public class GoodsbrandService {


    private GoodsbrandAnalysis goodsbrandAnalysis=new GoodsbrandAnalysis();

    public List<Record> getGoodsbrandSaleTotal(int seqType,String brandName,String category_id,String[] channel,int pageNumber,int  saleType,String start_time,String end_time){

        long record_total=goodsbrandAnalysis.getGoodsbrandTotalPageSize(brandName,category_id,channel, start_time, end_time);
        List<Record> list=goodsbrandAnalysis.getGoodsbrandSaleTotal(seqType,brandName,category_id,channel, pageNumber, saleType, start_time, end_time);
        if (list.size()>0){
            list.get(0).set("total_page",Math.ceil(record_total/(GoodsbrandAnalysis.PAGESIZE*1.0)));
            for (Record record:list){
                int sale_flag=Double.valueOf(record.get("goods_sales_flag").toString()).intValue();
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

    public List<Record> getExportGoodsbrand(int seqType,String brandName,String category_id,String[] channel,int  saleType,String start_time,String end_time){
        return  goodsbrandAnalysis.getExportGoodsbrand(seqType,brandName,category_id,channel,saleType, start_time, end_time);
    }

    public List<Record> getGoodsbrandTopBycategory(String[] channelList,String category_id,int  saleType,String start_time,String end_time){
        return  goodsbrandAnalysis.getGoodsbrandTopBycategory(channelList,category_id,saleType,start_time,end_time);
    }

    public List<Record> getGoodsbrandTop5(String category_id,String start_time,String end_time){
        return  goodsbrandAnalysis.getGoodsbrandTop5(category_id,start_time,end_time);
    }



    public List<Record> getGoodsbrandTop(int pageNumber,String category,int  saleType,String start_time,String end_time){

        long record_total=goodsbrandAnalysis.getGoodsbrandTopPageSize(category, start_time, end_time);
        List<Record> list=goodsbrandAnalysis.getGoodsbrandTop(pageNumber, category, saleType, start_time, end_time);
        if (list.size()>0){
            list.get(0).set("total_page",Math.ceil(record_total/(GoodsbrandAnalysis.PAGESIZE*1.0)));
        }
        return list;
    }

    //按店铺名搜索
    public List<Record> getGoodsbrandByName(String category_id,int pageNumber,String goodsbrand,String start_time,String end_time){
        try {
            long record_total=goodsbrandAnalysis.getGoodsbrandPageSize(category_id,goodsbrand, start_time, end_time);
            List<Record> list=goodsbrandAnalysis.getGoodsbrandByName(category_id,pageNumber, goodsbrand, start_time, end_time);
            if (list.size()>0){
                list.get(0).set("total_page",Math.ceil(record_total/(GoodsbrandAnalysis.PAGESIZE*1.0)));
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<Record> getGoodsbrandAttrduibi(String goodsbrandList[],String start_time,String end_time){
        return  goodsbrandAnalysis.getGoodsbrandAttrduibi(goodsbrandList,start_time,end_time);
    }

    //品牌市场覆盖-点击事件
    public List<Record> getGoodsbrandTopByBrand(String area,String goodsbrand,int saleType,String start_time,String end_time){
        return goodsbrandAnalysis.getGoodsbrandTopByBrand(area,goodsbrand,saleType,start_time,end_time);
    }

    //品牌价格分布
    public List<Record> getGoodsbrandPriceInfo(String goodsbrand,int saleType,String start_time,String end_time){
        return goodsbrandAnalysis.getGoodsbrandPriceInfo(goodsbrand,saleType,start_time,end_time);
    }

    //品牌印象
    public List<Record> getGoodsbrandFace(String goodsbrand,String start_time,String end_time){
        return  goodsbrandAnalysis.getGoodsbrandFace(goodsbrand,start_time,end_time);
    }
    //品牌口碑变化
    public List<Record> getGoodsbrandFaceChange(String goodsbrand,String start_time,String end_time){
       return goodsbrandAnalysis.getGoodsbrandFaceChange(goodsbrand,start_time,end_time);
    }



    public Map<String,List<Record>> getGoodsbrandAreaduibi(String goodsbrandList[],int saleType,String start_time,String end_time){
        List<Record> list=goodsbrandAnalysis.getGoodsbrandAreaduibi(goodsbrandList,saleType,start_time,end_time);
        Map<String,List<Record>>  map=new HashMap<String, List<Record>>();
        for(String goodsbrand:goodsbrandList){
            List<Record> list1=new ArrayList<Record>();
            for(Record record:list){
                if (record.get("goodsbrand").equals(goodsbrand)){
                    list1.add(record);
                }
            }

            map.put(goodsbrand,list1);
        }
        return map;
    }

    //品牌销售情况
    public List<Record> getGoodsbrandSaleInfo(int  saleType,String goodsbrand,String start_time,String end_time){
        int dayCount= FormatUtils.getDayCount(start_time, end_time);
        List<Record> list2 = goodsbrandAnalysis.getGoodsbrandSaleInfo(goodsbrand, saleType, start_time, end_time);
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
    //品牌各渠道销量占比
    public List<Record> getGoodsbrandSaleBysqudao(String goodsbrand,int  saleType,String start_time,String end_time){
        return goodsbrandAnalysis.getGoodsbrandSaleBysqudao(goodsbrand,saleType,start_time,end_time);
    }

    //品牌热销产品排行TOP5
    public List<Record> getGoodsSaleByGoodsbrand(String goodsbrand,int  saleType,String start_time,String end_time){
        return goodsbrandAnalysis.getGoodsSaleByGoodsbrand(goodsbrand,saleType,start_time,end_time);
    }



}
