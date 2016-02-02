package com.szl.stronguion.model.electrial;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * Created by 小龙
 * on 15-10-28
 * at 下午3:12.
 */

public class ShopsDetail {
    public static final String UPDATETIME1 = "updatetime";
    /*top1的销售渠道：
    * saleType:2 为销售量,1 为销售额
    * */
    public  Record getTop1Channel(StringBuilder sb,String category_id,int saleType,String start_time,String end_time){
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        if (saleType==3){
            sale_attr=" (goods_mod_sum1+goods_mod_sum2+goods_mod_sum3) ";
        }
            return Db.use("main2").findFirst("select 'TOP1销售渠道' as 'title',squdao as 'name',round(SUM("+sale_attr+"),0) as 'total' \n" +
                    "from eb_mod_all_sales_goods where category_id='"+category_id+"' " +
                    " and "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"' "+sb+"\n" +
                    "GROUP BY squdao ORDER BY total DESC LIMIT 1;");
    }

    /*top1的销售店铺：*/
    public  Record getTop1Shop(StringBuilder sb,String category_id,int saleType,String start_time,String end_time){
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        if (saleType==3){
            sale_attr=" (goods_mod_sum1+goods_mod_sum2+goods_mod_sum3) ";
        }
            return Db.use("main2").findFirst("select 'TOP1销售店铺' as 'title',store_name as 'name',round(SUM("+sale_attr+"),0) as 'total' \n" +
                    "from eb_mod_all_sales_goods where category_id='"+category_id+"' " +
                    " and "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"'  "+sb+"\n" +
                    "GROUP BY store_id ORDER BY total DESC LIMIT 1");
    }

    /*top1的销售品牌：*/
    public  Record getTop1Brand(StringBuilder sb,String category_id,int saleType,String start_time,String end_time){
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        if (saleType==3){
            sale_attr=" (goods_mod_sum1+goods_mod_sum2+goods_mod_sum3) ";
        }
            return Db.use("main2").findFirst("select 'TOP1品牌' as 'title',goodsbrand as 'name',round(SUM("+sale_attr+"),0) as 'total' \n" +
                    "from eb_mod_all_sales_goods where category_id='"+category_id+"' " +
                    " and "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"' "+sb+"\n" +
                    "GROUP BY goodsbrand ORDER BY total DESC LIMIT 1;");
    }

    /*top1的销售商品：*/
    public  Record getTop1Good(StringBuilder sb,String category_id,int saleType,String start_time,String end_time){
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        if (saleType==3){
            sale_attr=" (goods_mod_sum1+goods_mod_sum2+goods_mod_sum3) ";
        }
            return Db.use("main2").findFirst("select 'TOP1商品' as 'title',goodsname as 'name',round(SUM("+sale_attr+"),0) as 'total' \n" +
                    "from eb_mod_all_sales_goods where category_id='"+category_id+"'  " +
                    " and "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"' "+sb+"\n" +
                    "GROUP BY goodsid ORDER BY total DESC LIMIT 1;");
    }

    public Record getTotal(StringBuilder sb,String category_id,int saleType,String start_time,String end_time){
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        if (saleType==3){
            sale_attr=" (goods_mod_sum1+goods_mod_sum2+goods_mod_sum3) ";
        }
           return Db.use("main2").findFirst("select  '"+start_time+"至"+end_time+"' as 'time','线上电商交易金额合计' as 'title',round(SUM("+sale_attr+"),0) as 'total' \n" +
                    "from eb_mod_all_sales_goods where category_id='"+category_id+"' " +
                   " and "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"' "+sb+"");
    }
}
