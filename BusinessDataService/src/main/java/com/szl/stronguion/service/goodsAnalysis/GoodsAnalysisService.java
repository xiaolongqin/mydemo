package com.szl.stronguion.service.goodsAnalysis;

import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.goodsAnalysis.GoodsAnalysis;
import com.szl.stronguion.utils.FormatUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 小龙
 * on 15-11-16
 * at 上午9:31.
 */

public class GoodsAnalysisService {
    private GoodsAnalysis goodsAnalysis=new GoodsAnalysis();

    public List<Record> getGoodsScan(int seqType,String category_id,String goodName,String[] channel,int pageNumber,int  saleType,String start_time,String end_time){

        try {
            long record_total=goodsAnalysis.getTotalPageSize(category_id,goodName,channel,start_time,end_time);
            List<Record> list=goodsAnalysis.getChannelTotal(seqType,category_id,goodName,channel,pageNumber,saleType,start_time,end_time);
            if (list.size()>0){
                list.get(0).set("total_page",Math.ceil(record_total/(GoodsAnalysis.PAGESIZE*1.0)));
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
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public List<Record> getExportGoods(int seqType,String category_id,String goodName,String[] channel,int  saleType,String start_time,String end_time){
        return goodsAnalysis.getExportGoods(seqType,category_id,goodName,channel,saleType,start_time,end_time);
    }

    public List<Record> getGoodsTop(String category_id,String brandName,int  saleType,int pageNumber,String start_time,String end_time){
        try {
            long record_total=goodsAnalysis.getTotalPageSizeByCategory(category_id,brandName,start_time,end_time);
            List<Record> list=goodsAnalysis.getGoodsTopByCategory(brandName,category_id,saleType,pageNumber,start_time,end_time);
            if (list.size()>0){
                list.get(0).set("total_page",Math.ceil(record_total/(GoodsAnalysis.PAGESIZE*1.0)));
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
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public List<Record> getGoodsCategory(){
        return goodsAnalysis.getGoodsCategory();
    }

    public List<Record> getGoodsGoodBrand(){
        return goodsAnalysis.getGoodsGoodBrand();
    }

    //品牌中内竞争排名top20
    public List<Record> getGoodsTopByBrand(int  saleType,String goodsbrand,int pageNumber,String start_time,String end_time){
        try {
            long record_total=goodsAnalysis.getTotalPageSizeByBrand(goodsbrand,start_time,end_time);
            List<Record> list=goodsAnalysis.getGoodsTopByBrand(saleType,goodsbrand,pageNumber,start_time,end_time);
            if (list.size()>0){
                list.get(0).set("total_page",Math.ceil(record_total/(GoodsAnalysis.PAGESIZE*1.0)));
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //产品对比-产品销售情况对比

    public Map<String,List<Record>> getGoodsSaleduibi(int  saleType,String[] goodsname,String[] goodsidList,String start_time,String end_time){
        Map<String,List<Record>> map=new HashMap<String, List<Record>>();
        int dayCount= FormatUtils.getDayCount(start_time, end_time);
        List<Record> list2 = goodsAnalysis.getGoodsSaleduibi(saleType,goodsidList,start_time,end_time);
        for(int j=0;j<goodsidList.length;j++){
            List<Record> list=new ArrayList<Record>();
            for (int i = dayCount; i > 0; i--) {
                String  time=FormatUtils.getDayTime(-i,end_time);
                boolean flag=false;
                if (list2!=null){
                    for(Record r:list2){
                        if (r.get("day").toString().equals(time)&&r.get("goodsid").equals(goodsidList[j])){
                            list.add(r);
                            flag=true;
                        }
                    }
                }
                if (!flag){
                    Record r2=new Record();
                    r2.set("day",time);
                    r2.set("goodsid",goodsidList[j]);
                    r2.set("goodsname",goodsname[j]);
                    r2.set("total",0);
                    list.add(r2);
                }
            }
            map.put(goodsname[j]+"("+goodsidList[j]+")",list);
        }
        return map;
    }



    public Map<String,List<Record>> getGoodsAttrduibi(String goodsname[],String goodsidList[],String start_time,String end_time){
        Map<String,List<Record>> map=new HashMap<String, List<Record>>();
        List<Record> list2 = goodsAnalysis.getGoodsAttrduibi(goodsname, goodsidList, start_time, end_time);
        for(int j=0;j<goodsidList.length;j++){
            List<Record> list=new ArrayList<Record>();
                if (list2!=null){
                    for(Record r:list2){
                        if (r.get("goodsid").equals(goodsidList[j])){
                            list.add(r);
                        }
                    }
                }
            map.put(goodsname[j]+"("+goodsidList[j]+")",list);
        }
        return map;
    }
      

    //产品覆盖区域对比
    public Map<String,List<Record>> getGoodsArea(String[] goodsname,String[] goodsIdList,String start_time,String end_time){
        Map<String,List<Record>> map=new HashMap<String, List<Record>>();
        List<Record> list2 = goodsAnalysis.getGoodsArea(goodsIdList, start_time, end_time);
        for(int j=0;j<goodsIdList.length;j++){
            List<Record> list=new ArrayList<Record>();
            if (list2!=null){
                for(Record r:list2){
                    if (r.get("goodsid").equals(goodsIdList[j])){
                        list.add(r);
                    }
                }
            }
            map.put(goodsname[j]+"("+goodsIdList[j]+")",list);
        }
        return map;
    }


    //产品对比--

    public List<Record> getOnlineGoodsduibi(String category_id,String start_time,String end_time){
        return goodsAnalysis.getOnlineGoodsduibi(category_id,start_time,end_time);
    }


    //按产品名搜索
    public List<Record> getGoodsByGoodsName(String category_id,int pageNumber,String goodsName,String start_time,String end_time){
        try {
            long record_total=goodsAnalysis.getGoodsPageSize(category_id,goodsName,start_time,end_time);
            List<Record> list=goodsAnalysis.getGoodsByGoodsName(category_id,pageNumber,goodsName,start_time,end_time);
            if (list.size()>0){
                list.get(0).set("total_page",Math.ceil(record_total/(GoodsAnalysis.PAGESIZE*1.0)));
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
        }catch (Exception e){
            return null;
        }
    }


}
