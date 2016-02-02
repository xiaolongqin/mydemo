package com.szl.stronguion.service.electrial;

import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.electrial.SalesElectrial;
import com.szl.stronguion.model.electrial.ShopsDetail;
import com.szl.stronguion.utils.FormatUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 小龙
 * on 15-11-2
 * at 下午3:56.
 */

public class ElectrialService {
    private SalesElectrial salesElectrial=new SalesElectrial();
    private ShopsDetail shopsDetail=new ShopsDetail();

    /*线上电商总览*/
    public  List<Record> getOverView(String[] channel,String category_id,int saleType,String start_time,String end_time){
        StringBuilder sb =new StringBuilder(" ");
        if (channel!=null&&channel.length!=0){
            sb.append(" and (squdao=");
            for (int i=0;i<channel.length;i++){
                if (i<channel.length-1){
                    sb.append("'"+channel[i]+"'").append(" or squdao= ");
                }else {
                    sb.append("'"+channel[i]+"'").append(") ");
                }
            }
        }

        List<Record> list=new ArrayList<Record>();
            list.add(shopsDetail.getTop1Channel(sb,category_id,saleType,start_time,end_time));
            list.add(shopsDetail.getTop1Shop(sb,category_id,saleType,start_time,end_time));
            list.add(shopsDetail.getTop1Brand(sb,category_id,saleType,start_time,end_time));
            list.add(shopsDetail.getTop1Good(sb,category_id,saleType,start_time,end_time));
            String total=shopsDetail.getTotal(sb,category_id, saleType,start_time,end_time).get("total").toString();
            String str= BigDecimal.valueOf(Double.valueOf(total)).toPlainString();
            Record record= new Record().set("total_str",FormatUtils.dataChange(str)).set("total",total);
            list.add(record);

        return list;
    }

    //线上渠道销售总额排行
    public List<Record> getDtailGoodTop(int  saleType,String startTime,String endTime){
            return salesElectrial.getDtailGoodTop(saleType,startTime,endTime);
    }

    //细分市场概览
    public List<Record> getDtailMarket(String[] channelList,int saleType,String category_id,String startTime,String endTime){
        int dayCount= FormatUtils.getDayCount(startTime, endTime);
        List<Record> list2 = salesElectrial.getDtailMarket(channelList,saleType,category_id, startTime, endTime);
        List<Record> list=new ArrayList<Record>();
            for (int i = dayCount; i > 0; i--) {
                String  time= FormatUtils.getDayTime(-i, endTime);
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
    //行业价格分布
    public List<Record> getDtailPrice(String[] channelList,String category_id,String startTime,String endTime){
        List<Record> records=salesElectrial.getDtailPrice(channelList,category_id, startTime, endTime);
        return records;
    }

    //行业价格分布
    public List<Record> getUserFocus(String[] channelList,String  category_id,String startTime,String endTime){
        List<Record> list=salesElectrial.getUserFocus(channelList,category_id,startTime,endTime);
        Record record=list.get(0);
        if (record.get("好评")==null){
            record.set("好评",0);
        }
        if (record.get("中评")==null){
            record.set("中评",0);
        }
        if (record.get("差评")==null){
            record.set("差评",0);
        }
        if (record.get("品质")==null){
            record.set("品质",0);
        }
        if (record.get("外观")==null){
            record.set("外观",0);
        }
        if (record.get("性价比")==null){
            record.set("性价比",0);
        }
        if (record.get("口感")==null){
            record.set("口感",0);
        }
        return list;
    }

    //白酒产品参数与销量
    public List<Record> getDegreeSale(int saleType,String category_id,String startTime,String endTime){
        List<Record> records=salesElectrial.getDegreeSale(saleType,category_id, startTime, endTime);
        return records;
    }

    //白酒各香型酒销量(按天)
    public Map<String,List<Record>> getXiangxinSale(int saleType,String category_id,String startTime,String endTime){
        Map<String,List<Record>> map=new HashMap<String, List<Record>>();
        int dayCount= FormatUtils.getDayCount(startTime, endTime);
        List<Record> list2 = salesElectrial.getXiangxinSale(saleType,category_id,startTime,endTime);
        List<Record> list1 = salesElectrial.getXiangxinName();
        for(Record xiangxin:list1){
            String name=xiangxin.getStr("name");
            List<Record> list=new ArrayList<Record>();
            for (int i = dayCount; i > 0; i--) {
                String  time=FormatUtils.getDayTime(-i,endTime);
                boolean flag=false;
                if (list2!=null){
                    for(Record r:list2){
                        if (r.get("day").toString().equals(time)&&r.get("xiangxin").equals(name)){
                            r.set("A",xiangxin.getStr("attr_id"));
                            list.add(r);
                            flag=true;
                        }
                    }
                }
                if (!flag){
                    Record r2=new Record();
                    r2.set("A",xiangxin.getStr("attr_id"));
                    r2.set("day",time);
                    r2.set("xiangxin",xiangxin);
                    r2.set("total",0);
                    list.add(r2);
                }
            }
            map.put(name,list);
        }
        return map;
    }


    public List<Record> getAllGoodName(){
        return salesElectrial.getAllGoodName();
    }

}
