package com.szl.stronguion.model.goodsbrandAnalysis;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * Created by 小龙
 * on 15-11-30
 * at 下午3:39.
 */

public class GoodsbrandSecond {

    public static final String MAIN2 = "main2";
    public static final int PAGESIZE=10;
    public static final String UPDATETIME = "DATE_FORMAT(updatetime,'%Y-%m-%d')";
    public static final String UPDATETIME1 = "updatetime";

    //品牌分析-》品牌特写》品牌各渠道销量占比》--------------------------------------------------------------
    //某渠道中品牌销售情况
    public List<Record> getGoodsbrandSaleBychannel(String goodsbrand,String channel,int  saleType,String start_time,String end_time){
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        return Db.use(MAIN2).find("SELECT "+UPDATETIME+" as 'day' ,squdao,goodsbrand,round(SUM("+sale_attr+")/10000,2) as 'total'  FROM eb_mod_all_sales_goods\n" +
                "   WHERE "+UPDATETIME1+">=DATE_SUB('"+start_time+"',INTERVAL 1 day)  and  "+UPDATETIME1+"<='"+end_time+"'\n" +
                "   and goodsbrand='"+goodsbrand+"' and squdao='"+channel+"'  GROUP BY DATE_FORMAT(updatetime,'%Y-%m-%d')");
    }

    //某渠道品牌热销店铺排行top10
    public List<Record> getStoreTopByBrand(String goodsbrand,String channel,int  saleType,String start_time,String end_time){
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        return Db.use(MAIN2).find("SELECT store_id,store_name,round(SUM("+sale_attr+")/10000,2) as 'total' FROM eb_mod_all_sales_goods\n" +
                " WHERE "+UPDATETIME1+">='"+start_time+"'  and  "+UPDATETIME1+"<='"+end_time+"'\n" +
                " and goodsbrand='"+goodsbrand+"' and squdao='"+channel+"' GROUP BY store_id ORDER BY total desc limit 10 ");
    }
    //某渠道热销茅台产品TOP10

    public List<Record> getGoodsTopByBrand(String goodsbrand,String channel,int  saleType,String start_time,String end_time){
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        return Db.use(MAIN2).find("SELECT  goodsid,goodsname,squdao as 'channel',category,goodsbrand,round(SUM("+sale_attr+")/10000,2) as 'total' FROM eb_mod_all_sales_goods\n" +
                " WHERE "+UPDATETIME1+">='"+start_time+"'  and  "+UPDATETIME1+"<='"+end_time+"'\n" +
                " and goodsbrand='"+goodsbrand+"' and squdao='"+channel+"' GROUP BY goodsid ORDER BY total desc limit 10 ");
    }


    //品牌分析-》品牌特写》品牌价格分布》--------------------------------------------------------------
    //品牌价格区间销售变化情况
    public List<Record> getGoodsbrandSaleByPrice(String goodsbrand,String price_attr,int  saleType,String start_time,String end_time){
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        return Db.use(MAIN2).find("SELECT "+UPDATETIME+" as 'day' ,goods_attr_price,goodsbrand,round(SUM("+sale_attr+")/10000,2) as 'total'  FROM eb_mod_all_sales_goods\n" +
                "    WHERE "+UPDATETIME1+">=DATE_SUB('"+start_time+"',INTERVAL 1 day)  and  "+UPDATETIME1+"<='"+end_time+"'\n" +
                "    and goodsbrand='"+goodsbrand+"' and goods_attr_price='"+price_attr+"'  GROUP BY "+UPDATETIME+"");
    }

    //品牌（茅台）500-1000元价格区间产品在各渠道的销量

    public List<Record> getBrandSaleByChannel(String goodsbrand,String price_attr,int  saleType,String start_time,String end_time){
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        return Db.use(MAIN2).find("SELECT squdao as 'channel',round(SUM("+sale_attr+")/10000,2) as 'total'  FROM eb_mod_all_sales_goods\n" +
                "  WHERE "+UPDATETIME1+">='"+start_time+"'  and  "+UPDATETIME1+"<='"+end_time+"'\n" +
                "  and goodsbrand='"+goodsbrand+"' and goods_attr_price='"+price_attr+"' GROUP BY squdao  ORDER BY total DESC");
    }


    //品牌（茅台）500-1000元价格区间热销产品TOP10

    public List<Record> getGoodsSaleByBrand(String goodsbrand,String price_attr,int  saleType,String start_time,String end_time){
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        return Db.use(MAIN2).find("SELECT goodsid,goodsname,category,squdao as 'channel',goodsbrand,round(SUM("+sale_attr+")/10000,2) as 'total'  FROM eb_mod_all_sales_goods\n" +
                "  WHERE "+UPDATETIME1+">='"+start_time+"'  and  "+UPDATETIME1+"<='"+end_time+"'\n" +
                "  and goodsbrand='"+goodsbrand+"' and goods_attr_price='"+price_attr+"' GROUP BY goodsid  ORDER BY total DESC limit 10");
    }

    //品牌（茅台）500-1000元价格区间品牌印象

    public List<Record> getbrandFaceByPrice(String goodsbrand,String price_attr,String start_time,String end_time){

//        return Db.use(MAIN2).find("SELECT word,goodsbrand,round(AVG(weight),2) as 'weight' from  eb_mod_wordcloud_weekly \n" +
//                " WHERE goodsbrand='"+goodsbrand+"'  AND "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"'\n" +
//                " GROUP BY word ORDER BY weight desc LIMIT 20 ");

        return Db.use(MAIN2).find("SELECT t1.word,t1.goodsbrand,round(AVG(t1.weight),2) as 'weight' from  eb_mod_wordcloud_weekly t1," +
                "(SELECT goodsid FROM eb_mod_all_sales_goods WHERE goods_attr_price='"+price_attr+"' and goodsbrand='"+goodsbrand+"' and " +
                ""+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"') t2\n" +
                " WHERE t1.goodsid=t2.goodsid and t1."+UPDATETIME1+">='"+start_time+"' and t1."+UPDATETIME1+"<='"+end_time+"'\n" +
                " GROUP BY t1.word ORDER BY weight desc LIMIT 20 ");

    }

    //品牌（茅台）500-1000元价格区间产品地区覆盖

    public List<Record> getGoodsSaleByArea(String goodsbrand,String price_attr,String area,int  saleType,String start_time,String end_time){
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        return Db.use(MAIN2).find("SELECT goodsname,goodsid,store_area,round(SUM("+sale_attr+")/10000,2) as 'total'  FROM eb_mod_all_sales_goods\n" +
                "  WHERE DATE_FORMAT(updatetime,'%Y-%m-%d')>='"+start_time+"'  and  DATE_FORMAT(updatetime,'%Y-%m-%d')<='"+end_time+"'\n" +
                "  and goodsbrand='"+goodsbrand+"' and goods_attr_price='"+price_attr+"' AND store_area='"+area+"' GROUP BY goodsid  ORDER BY total desc limit 10");
    }


}
