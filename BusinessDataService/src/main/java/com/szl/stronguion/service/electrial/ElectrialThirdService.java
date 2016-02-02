package com.szl.stronguion.service.electrial;

import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.electrial.ElectrialThirdPage;
import com.szl.stronguion.utils.FormatUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 小龙
 * on 15-11-9
 * at 下午3:14.
 */

public class ElectrialThirdService {
    private ElectrialThirdPage electrialThirdPage=new ElectrialThirdPage();

    public Map<String,List<Record>> getGoodsSaleInfo(String goodsId,int  saleType,String start_time,String end_time){
        Map<String,List<Record>> map=new HashMap<String, List<Record>>();
        List<Record> list1=electrialThirdPage.getGoodsHighLowPrice(goodsId,start_time,end_time);
        List<Record> list2 = electrialThirdPage.getGoodsSaleInfo(goodsId, saleType, start_time, end_time);

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
                r2.set("price",0);
                r2.set("total",0);
                list.add(r2);
            }
        }
        map.put("list1",list1);
        map.put("list2",list);
        return map;
    }


    //产品价格走势
    public Map<String,List<Record>> getGoodsPrice(String goodsId,String start_time,String end_time){
        Map<String,List<Record>> map=new HashMap<String, List<Record>>();
//        int dayCount= FormatUtils.getDayCount(start_time, end_time);
//        List<Record> list=electrialThirdPage.getGoodsPrice(goodsId, start_time, end_time);
//        List<Record> list1=electrialThirdPage.getGoodsHighLowPrice(goodsId,start_time,end_time);
//
//
//        List<Record> list2=new ArrayList<Record>();
//        for (int i = dayCount; i > 0; i--) {
//            String  time=FormatUtils.getDayTime(-i,end_time);
//            boolean flag=false;
//            if (list!=null){
//                for(Record r:list){
//                    if (r.get("day").toString().equals(time)){
//                        list2.add(r);
//                        flag=true;
//                    }
//                }
//            }
//
//            if (!flag){
//                Record r2=new Record();
//                r2.set("day",time);
//                r2.set("price",0);
//                list2.add(r2);
//            }
//
//        }
//
//        if (list1.size()==0||list1==null){
//            list1.add(new Record().set("price",0));
//            list1.add(new Record().set("price",0));
//        }
//        map.put("list1",list1);
//        map.put("list2",list2);
        return map;
    }

    //渠道对比
    public Map<String,List<Record>> getChannelduibi(int saleType,String[] channelName,String goodsId,String startTime,String endTime){
        Map<String,List<Record>> map=new HashMap<String, List<Record>>();
        int dayCount= FormatUtils.getDayCount(startTime, endTime);
//        List<Record> list2 = salesChannel.getChannelduibi(channelName,startTime,endTime);
        List<Record> list2 = electrialThirdPage.getChannelduibi(saleType,goodsId,startTime,endTime);
        for(String channel:channelName){

            List<Record> list=new ArrayList<Record>();
            for (int i = dayCount; i > 0; i--) {
                String  time=FormatUtils.getDayTime(-i,endTime);
                boolean flag=false;
                if (list2!=null){
                    for(Record r:list2){
                        if (r.get("day").toString().equals(time)&&r.get("channel").equals(channel.trim())){
                            list.add(r);
                            flag=true;
                        }
                    }
                }

                if (!flag){
                    Record r2=new Record();
                    r2.set("day",time);
                    r2.set("channel",channel);
                    r2.set("total",0);
                    list.add(r2);
                }
            }
            map.put(channel,list);
        }
        return map;
    }

    public List<Record> getGoodsArea(int  saleType,String goodsbrand,String store_area,String start_time,String end_time){
        return electrialThirdPage.getGoodsArea(saleType,goodsbrand,store_area,start_time,end_time);
    }

    public List<Record> getGoodsComment(String goodsid,String start_time,String end_time){
        List<Record> list=electrialThirdPage.getGoodsComment(goodsid,start_time,end_time);
        List<Record> list1=electrialThirdPage.getGoodsCommentBest(goodsid,start_time,end_time);
        List<Record> list2=electrialThirdPage.getGoodsCommentWorst(goodsid,start_time,end_time);
        if (list.size()>0&&list1.size()>0){
            list.get(0).set("best",list1.get(0).get("best"));
            list.get(0).set("worst",list2.get(0).get("worst"));
        }
        return list;
    }

    //获取所有渠道名
    public List<Record> getAllChannelName(){
        return electrialThirdPage.getAllChannelName();
    }
    //同类型产品竞争排名top50
    public List<Record> getGoodsTopByCategory(int pageNumber,String category_id,String start_time,String end_time){
        try {
            long record_total=electrialThirdPage.getTotalPageSizeByCategory(category_id,start_time,end_time);
            List<Record> list=electrialThirdPage.getGoodsTopByCategory(pageNumber,category_id,start_time,end_time);
            if (list.size()>0){
                list.get(0).set("total_page",Math.ceil(record_total/(ElectrialThirdPage.PAGESIZE*1.0)));
            }
            return list;
        }catch (Exception e){
            return null;
        }
    }

    //品牌中内竞争排名top20
    public List<Record> getGoodsTopByBrand(String goodsbrand,int pageNumber,String category_id,String start_time,String end_time){
        try {
            long record_total=electrialThirdPage.getTotalPageSizeByBrand(goodsbrand,category_id,start_time,end_time);
            List<Record> list=electrialThirdPage.getGoodsTopByBrand(goodsbrand,pageNumber,category_id,start_time,end_time);
            if (list.size()>0){
                list.get(0).set("total_page",Math.ceil(record_total/(ElectrialThirdPage.PAGESIZE*1.0)));
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
}
