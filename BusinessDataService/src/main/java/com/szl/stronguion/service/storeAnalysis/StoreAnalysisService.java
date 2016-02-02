package com.szl.stronguion.service.storeAnalysis;

import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.electrial.AllSaleGood;
import com.szl.stronguion.model.storeAnalysis.StoreAnalysis;
import com.szl.stronguion.utils.FormatUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 小龙
 * on 15-11-23
 * at 上午9:22.
 */

public class StoreAnalysisService {

    private StoreAnalysis storeAnalysis=new StoreAnalysis();

    public List<Record> getStoreSaleTotal(int seqType,String category_id,String store_name,String[] channel,int pageNumber,int  saleType,String start_time,String end_time){

            long record_total=storeAnalysis.getStoreTotalPageSize(category_id,store_name,channel,start_time,end_time);
            List<Record> list=storeAnalysis.getStoreSaleTotal(seqType,category_id,store_name,channel,pageNumber,saleType,start_time,end_time);
            if (list.size()>0){
                list.get(0).set("total_page",Math.ceil(record_total/(AllSaleGood.PAGESIZE*1.0)));
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


    public List<Record> getExportStoreTotal(int seqType,String category_id,String store_name,String[] channel,int  saleType,String start_time,String end_time){
        return storeAnalysis.getExportStoreTotal(seqType,category_id,store_name,channel,saleType,start_time,end_time);
    }


    public List<Record> getAllChannelByTime(String start_time,String end_time){
        return storeAnalysis.getAllChannelByTime(start_time,end_time);
    }

    public List<Record> getAllChannel(){
        return storeAnalysis.getAllChannel();
    }

    public List<Record> getAllCategory(){
        return storeAnalysis.getAllCategory();
    }


    public List<Record> getStoreTopBychannel(String category_id,String channel,int  saleType,String start_time,String end_time){
        return storeAnalysis.getStoreTopBychannel(category_id,channel, saleType, start_time, end_time);
      
    }

//    public List<Record> getStoreTopByCategory(String category,int  saleType,String start_time,String end_time){
//        return storeAnalysis.getStoreTopByCategory(category, saleType, start_time, end_time);
//
//    }


    public List<Record> getStoreDetaiInfo(String category_id,int  saleType,String storeId,String start_time,String end_time){
        int dayCount= FormatUtils.getDayCount(start_time, end_time);
        List<Record> list2 = storeAnalysis.getStoreDetaiInfo(category_id,storeId, saleType, start_time, end_time);
            List<Record> list=new ArrayList<Record>();
            for (int i = dayCount; i > 0; i--) {
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
                    r2.set("storeId",storeId);
                    r2.set("avg_price",0);
                    r2.set("total",0);
                    list.add(r2);
                }
            }
        return list;
    }


    public List<Record> getStoreGoodsAndBrandInfo(int type,String storeId,int  saleType,String start_time,String end_time){
        return  storeAnalysis.getStoreGoodsAndBrandInfo(type,storeId,saleType,start_time,end_time);
    }

    public List<Record> getStoreComment(String storeId,String start_time,String end_time){
        return  storeAnalysis.getStoreComment(storeId,start_time,end_time);
    }


    public List<Record> getStoreTop5(String category_id,String start_time,String end_time){
        return storeAnalysis.getStoreTop5(category_id,start_time,end_time);
    }

    //按店铺名搜索
    public List<Record> getStroreByName(String category_id,int pageNumber,String storeName,String start_time,String end_time){
        try {
            long record_total=storeAnalysis.getStorePageSize(category_id,storeName, start_time, end_time);
            List<Record> list=storeAnalysis.getStroreByName(category_id,pageNumber, storeName, start_time, end_time);
            if (list.size()>0){
                list.get(0).set("total_page",Math.ceil(record_total/(StoreAnalysis.PAGESIZE*1.0)));
                for (Record r:list){
                    double flag=r.getBigDecimal("store_industry_score").doubleValue();
                    if (flag>0){
                        r.set("state","up");
                    }else if (flag<0){
                        r.set("state","down");
                    }else {
                        r.set("state","equal");
                    }
                }
            }
              
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public Map<String,List<Record>> getStoreSaleduibi(int  saleType,String[] storeName,String[] storeIdList,String start_time,String end_time){
        Map<String,List<Record>> map=new HashMap<String, List<Record>>();
        int dayCount= FormatUtils.getDayCount(start_time, end_time);
        List<Record> list2 = storeAnalysis.getStoreSaleduibi(saleType,storeIdList,start_time,end_time);
        for(int j=0;j<storeIdList.length;j++){
            List<Record> list=new ArrayList<Record>();
            for (int i = dayCount; i > 0; i--) {
                String  time=FormatUtils.getDayTime(-i,end_time);
                boolean flag=false;
                if (list2!=null){
                    for(Record r:list2){
                        if (r.get("day").toString().equals(time)&&r.get("store_id").equals(storeIdList[j])){
                            list.add(r);
                            flag=true;
                        }
                    }
                }
                if (!flag){
                    Record r2=new Record();
                    r2.set("day",time);
                    r2.set("store_id",storeIdList[j]);
                    r2.set("store_name",storeName[j]);
                    r2.set("total",0);
                    list.add(r2);
                }
            }
            map.put(storeName[j]+"("+storeIdList[j]+")",list);
        }
        return map;
    }

    public List<Record> getStoreAttrduibi(String storeIdList[],String start_time,String end_time){
        return storeAnalysis.getStoreAttrduibi(storeIdList,start_time,end_time);
    }
      
}
