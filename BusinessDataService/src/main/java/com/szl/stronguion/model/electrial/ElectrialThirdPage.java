package com.szl.stronguion.model.electrial;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * Created by 小龙
 * on 15-11-9
 * at 下午3:14.
 */

public class ElectrialThirdPage {
    public static final String MAIN2 = "main2";
    public static final int PAGESIZE=10;
    public static final String UPDATETIME = "DATE_FORMAT(updatetime,'%Y-%m-%d')";
    public static final String UPDATETIME1 = "updatetime";
    //产品销售情况
    public List<Record> getGoodsSaleInfo(String goodsId,int  saleType,String start_time,String end_time){
//        StringBuilder sb =FormatUtils.getChannelStr(channel);
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        if (saleType==3){
            sale_attr=" (goods_mod_sum1+goods_mod_sum2+goods_mod_sum3) ";
        }
        return Db.use(MAIN2).find("SELECT goodsid,DATE_FORMAT(updatetime,'%Y-%m-%d') as 'day',goods_unit_price as 'price',goodsname,squdao,ROUND(SUM("+sale_attr+")/10000,2) as 'total'\n" +
                "  from  eb_mod_all_sales_goods WHERE goodsid='"+goodsId+"' \n" +
                "    and "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"'  \n" +
                "  GROUP BY DATE_FORMAT(updatetime,'%Y-%m-%d')");
    }
    //获取所有渠道名
    public List<Record> getAllChannelName(){
        return Db.use(MAIN2).find("SELECT distinct squdao as 'channel' from eb_mod_all_sales_goods ");
    }

    //产品各渠道销量对比
    public List<Record> getChannelduibi(int saleType,String goodsId,String startTime,String endTime){


        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        if (saleType==3){
            sale_attr=" (goods_mod_sum1+goods_mod_sum2+goods_mod_sum3) ";
        }

        return Db.use(MAIN2).find("SELECT DATE_FORMAT(updatetime,'%Y-%m-%d') as 'day',squdao as 'channel'," +
                "  round(SUM("+sale_attr+")/10000,2) as 'total' from eb_mod_all_sales_goods\n" +
                "  WHERE goodsid='"+goodsId+"' and "+UPDATETIME1+">='"+startTime+"' and "+UPDATETIME1+"<='"+endTime+"' " +
                "  GROUP BY DATE_FORMAT(updatetime,'%Y-%m-%d'),squdao");
    }



    //产品价格走势
    public List<Record> getGoodsPrice(String goodsId,String start_time,String end_time){
        return Db.use(MAIN2).find("SELECT goodsid,DATE_FORMAT(updatetime,'%Y-%m-%d') as 'day',goodsname,goods_unit_price as 'price',squdao\n" +
                "                  from  eb_mod_all_sales_goods WHERE goodsid='"+goodsId+"' \n" +
                "                  and "+UPDATETIME+">='"+start_time+"' and "+UPDATETIME+"<='"+end_time+"' \n" +
                "                 GROUP BY DATE_FORMAT(updatetime,'%Y-%m-%d')");
    }
    //产品价格走势最高/最低 单价  第一条最低 第二条最高
    public List<Record> getGoodsHighLowPrice(String goodsId,String start_time,String end_time){
//        StringBuilder sb =FormatUtils.getChannelStr(channel);
        return Db.use(MAIN2).find("(SELECT goods_unit_price as 'price'\n" +
                " from  eb_mod_all_sales_goods WHERE goodsid='"+goodsId+"'  \n" +
                " and "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"'  ORDER BY price LIMIT 1)\n" +
                " union ALL\n" +
                "(SELECT goods_unit_price as 'price'\n" +
                " from  eb_mod_all_sales_goods WHERE goodsid='"+goodsId+"'  \n" +
                " and "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"'  ORDER BY price DESC LIMIT 1)");
    }


    //产品口碑
    public List<Record> getGoodsComment(String goodsid,String start_time,String end_time){



        return Db.use(MAIN2).find("SELECT goodsbrand,goodsname,goodsid,squdao as 'channel'," +
                "round(AVG(goods_mod_comment1),5) as 'A1',sum(goods_mod_sum1) as 'A2'," +
                "goods_mod_best_comment1 as 'best',goods_mod_worst_comment3 as 'worst'," +
                "round(AVG(goods_mod_comment3),5) as 'A5',sum(goods_mod_sum1) as 'A6'," +
                "round(AVG(goods_mod_quality_rate),5) as 'A7',round(sum(goods_mod_quality_comment_sum),5) as 'A8'  ,\n" +
                "round(AVG(goods_mod_pack_rate),5) as 'A9',round(AVG(goods_mod_cost_rate),5) as 'A10',\n" +
                "round(sum(goods_mod_pack_comment_sum),5) as 'A11',round(sum(goods_mod_cost_comment_sum),5) as 'A12',\n" +
                "round(AVG(goods_mod_flavor_rate),5) as 'A13',round(sum(goods_mod_flavor_comment_sum),5) as 'A14'\n" +
                " FROM  eb_mod_all_sales_goods WHERE goodsid='"+goodsid+"' and \n" +
                "   "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"' ");

    }

    /**
     * 取某段时间内某商品经典好评
     * @param goodsid
     * @param start_time
     * @param end_time
     * @return
     */
    public List<Record> getGoodsCommentBest(String goodsid,String start_time,String end_time){
        return Db.use(MAIN2).find("SELECT " +
                "goods_mod_best_comment1 as 'best',goods_mod_best_comment1_weight " +
                " FROM  eb_mod_all_sales_goods WHERE goodsid='"+goodsid+"'  and goods_mod_best_comment1 is not null  and \n" +
                "   "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"' GROUP BY updatetime  ORDER BY goods_mod_best_comment1_weight DESC LIMIT 1 ");
    }


    /**
     * 取某段时间内某商品经典差评
     * @param goodsid
     * @param start_time
     * @param end_time
     * @return
     */
    public List<Record> getGoodsCommentWorst(String goodsid,String start_time,String end_time){
        return Db.use(MAIN2).find("SELECT " +
                "goods_mod_worst_comment3 as 'worst',goods_mod_worst_comment3_weight " +
                " FROM  eb_mod_all_sales_goods WHERE goodsid='"+goodsid+"'  and goods_mod_worst_comment3 is not null  and \n" +
                "   "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"' GROUP BY updatetime  ORDER BY goods_mod_worst_comment3_weight DESC LIMIT 1 ");
    }

    //    return Db.use(MAIN2).find("SELECT goodsbrand,goodsname,goodsid,squdao as 'channel'," +
//                                      "round(AVG(goods_mod_comment1),5) as '好评率',sum(goods_mod_sum1) as '好评数'" +
//                                      "goods_mod_best_comment1 as 'best',goods_mod_worst_comment3 as 'worst'" +
//                                      "round(AVG(goods_mod_comment3),5) as '差评率',sum(goods_mod_sum1) as '差评数'" +
//                                      "round(AVG(goods_mod_quality_rate),5) as '品质关注度',round(sum(goods_mod_quality_comment_sum),5) as '品质评论数'  ,\n" +
//                                      "round(AVG(goods_mod_pack_rate),5) as '外观关注度',round(AVG(goods_mod_cost_rate),5) as '性价比关注度',\n" +
//                                      "round(sum(goods_mod_pack_comment_sum),5) as '外观评论数',round(sum(goods_mod_cost_comment_sum),5) as '性价比评论数',\n" +
//                                      "round(AVG(goods_mod_flavor_rate),5) as '口感关注度',round(sum(goods_mod_flavor_comment_sum),5) as '口感评论数'\n" +
//                                      " FROM  eb_mod_all_sales_goods WHERE  \n" +
//                                      "   "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"' "+sb+"\n" +
//                                      "GROUP BY goodsid");



    //产品地区覆盖情况
    public List<Record> getGoodsArea(int  saleType,String goodsbrand,String store_area,String start_time,String end_time){
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        return Db.use(MAIN2).find("SELECT goodsid,goodsname,goodsbrand,squdao,ROUND(SUM("+sale_attr+")/10000,2) as 'total'\n" +
                "  from  eb_mod_all_sales_goods WHERE goodsbrand='"+goodsbrand+"' \n" +
                "  and "+UPDATETIME+">='"+start_time+"' and "+UPDATETIME+"<='"+end_time+"' \n" +
                "  and store_area='"+store_area+"' GROUP BY goodsid ORDER BY total DESC LIMIT 5");
    }


    //同类型产品竞争排名top50
    public List<Record> getGoodsTopByCategory(int pageNumber,String category_id,String start_time,String end_time){
        return Db.use(MAIN2).paginate(pageNumber,PAGESIZE,"SELECT goodsid,SUM(daynum) as 'total',goodsname,goodsbrand,squdao as 'channel'," +
                "category,goods_unit_price as 'price',ROUND(avg(goods_sales_rank),0) as 'rank',ROUND(avg(goods_mod_compete_index),3) as 'compete_index' ","FROM eb_mod_all_sales_goods\n" +
                " WHERE category_id='"+category_id+"' and "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"'\n" +
                " GROUP BY goodsid ORDER BY compete_index desc ").getList();
    }
    //同类型产品竞争排名总页数
    public long getTotalPageSizeByCategory(String category_id,String start_time,String end_time){
        return Db.use(MAIN2).findFirst("SELECT COUNT(tt.goodsname) as 'total_page' FROM\n" +
                "(SELECT goodsname,goods_unit_price as 'price' FROM eb_mod_all_sales_goods\n" +
                " WHERE category_id='"+category_id+"' and "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"'\n" +
                " GROUP BY goodsid LIMIT 50) tt").getLong("total_page");
    }

    //品牌中内竞争排名top20
    public List<Record> getGoodsTopByBrand(String goodsbrand,int pageNumber,String category_id,String start_time,String end_time){
        return Db.use(MAIN2).paginate(pageNumber,PAGESIZE,"SELECT goodsid,SUM(daynum) as 'total',goodsname," +
                "ROUND(avg(goods_sales_rank),0) as 'rank',sum(goods_sales_flag) as 'goods_sales_flag',ROUND(avg(goods_mod_compete_index),3) as 'compete_index'","FROM eb_mod_all_sales_goods\n" +
                " WHERE category_id='"+category_id+"' and goodsbrand='"+goodsbrand+"' and "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"'\n" +
                " GROUP BY goodsid ORDER BY compete_index desc  ").getList();
    }
    //品牌中内竞争排名总页数
    public long getTotalPageSizeByBrand(String goodsbrand,String category_id,String start_time,String end_time){
        return Db.use(MAIN2).findFirst("SELECT COUNT(tt.goodsname) as 'total_page' FROM\n" +
                "(SELECT goodsname FROM eb_mod_all_sales_goods\n" +
                " WHERE category_id='"+category_id+"' and goodsbrand='"+goodsbrand+"' and "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"'\n" +
                " GROUP BY goodsid  LIMIT 20) tt").getLong("total_page");
    }

}
