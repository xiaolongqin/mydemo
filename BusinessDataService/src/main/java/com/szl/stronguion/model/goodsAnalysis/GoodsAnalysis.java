package com.szl.stronguion.model.goodsAnalysis;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * Created by 小龙
 * on 15-11-16
 * at 上午9:32.
 */

public class GoodsAnalysis {

    public static final String MAIN2 = "main2";
    public static final int PAGESIZE=10;
    public static final String UPDATETIME = "DATE_FORMAT(updatetime,'%Y-%m-%d')";
    public static final String UPDATETIME1 = "updatetime";


    //产品分析-》产品整体扫描----------------
    //全网产品扫描--分页
    public List<Record> getChannelTotal(int seqType,String category_id,String brandName,String[] channel,int pageNumber,int  saleType,String start_time,String end_time){
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
        StringBuilder sb1 =new StringBuilder(" ");
        if (brandName!=null||!brandName.equals("")){
            sb.append(" and goodsname like '%"+brandName+"%' ");
        }

        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        String seq_attr=seqType==1?"desc":"asc";
            return Db.use(MAIN2).paginate(pageNumber, PAGESIZE, "select goods_url,goodsname,goodsid,category,goodsbrand,squdao as 'channel',SUM(" + sale_attr + ") as 'total'," +
                    "  round(avg(goods_unit_price),0) as 'price',sum(daynum) as 'total_num',round(sum(daysales),0) as 'total_sale',ROUND(avg(goods_sales_rank),0) as 'rank',sum(goods_sales_flag) as 'goods_sales_flag'  \n" +
                    "  ", "from eb_mod_all_sales_goods WHERE  category_id='" + category_id + "' and " + UPDATETIME1 + ">='" + start_time + "' and " + UPDATETIME1 + "<='" + end_time + "' " + sb + " " + sb1 + " group by goodsid ORDER BY total "+seq_attr+" ").getList();
        

    }

    //获取总页数
    public long getTotalPageSize(String category_id,String brandName,String[] channel,String start_time,String end_time){
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
        StringBuilder sb1 =new StringBuilder(" ");
        if (brandName!=null||!brandName.equals("")){
            sb.append(" and goodsname like '%"+brandName+"%' ");
        }
        return Db.use(MAIN2).findFirst("SELECT COUNT(tt.goodsname) as 'total' from (select goodsname \n" +
                "  from eb_mod_all_sales_goods WHERE category_id='" + category_id + "' and  " + UPDATETIME1 + ">='" + start_time + "'" +
                " and " + UPDATETIME1 + "<='" + end_time + "' " + sb + " " + sb1 + " GROUP BY goodsid limit 1000) tt").get("total");
    }

    /**
     * 获取导出数据
     * @param seqType
     * @param category_id
     * @param brandName
     * @param channel
     * @param saleType
     * @param start_time
     * @param end_time
     * @return
     */
    public List<Record> getExportGoods(int seqType,String category_id,String brandName,String[] channel,int  saleType,String start_time,String end_time){
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
        StringBuilder sb1 =new StringBuilder(" ");
        if (brandName!=null||!brandName.equals("")){
            sb.append(" and goodsname like '%"+brandName+"%' ");
        }

        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        String seq_attr=seqType==1?"desc":"asc";
        return Db.use(MAIN2).find("select goods_url,goodsname,goodsid,category,goodsbrand,squdao as 'channel',SUM(" + sale_attr + ") as 'total'," +
                "  round(avg(goods_unit_price),0) as 'price',sum(daynum) as 'total_num',round(sum(daysales),0) as 'total_sale',ROUND(avg(goods_sales_rank),0) as 'rank',sum(goods_sales_flag) as 'goods_sales_flag'  \n" +
                "  from eb_mod_all_sales_goods WHERE category_id='" + category_id + "' and " + UPDATETIME1 + ">='" + start_time + "' and " + UPDATETIME1 + "<='" + end_time + "' " + sb + " " + sb1 + " " +
                "group by goodsid ORDER BY total " + seq_attr + " ");
    }

      
    

    //产品分析-》产品飙升排行-----------------------------------------------------------------
    //获取类目列表
    public List<Record> getGoodsCategory(){
        return Db.use(MAIN2).find("select category from  eb_mod_all_sales_goods GROUP BY category");
    }

    //获取品牌列表
    public List<Record> getGoodsGoodBrand(){
        return Db.use(MAIN2).find("select distinct goodsbrand from  eb_mod_all_sales_goods ");
    }


    //产品飙升总榜top50
    public List<Record> getGoodsTopByCategory(String brandName,String category_id,int  saleType,int pageNumber,String start_time,String end_time){
        StringBuilder sb =new StringBuilder(" ");
        if (brandName!=null&&!brandName.equals("全部")){
            sb.append(" and goodsbrand='"+brandName+"' ");
        }
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        return Db.use(MAIN2).paginate(pageNumber, PAGESIZE, "SELECT goodsid,SUM(" + sale_attr + ") as 'total',goodsname,goodsbrand,squdao as 'channel',category,goods_unit_price as 'price'," +
                "ROUND(avg(goods_sales_rank),0) as 'rank',sum(goods_sales_flag) as 'goods_sales_flag',ROUND(avg(goods_mod_compete_index),3) as 'compete_index'  ", "FROM eb_mod_all_sales_goods\n" +
                " WHERE category_id='"+category_id+"' and "+UPDATETIME1+">='" + start_time + "' and "+UPDATETIME1+"<='" + end_time + "' "+sb+"\n" +
                " GROUP BY goodsid ORDER BY goods_sales_flag desc ").getList();
    }
    //产品飙升总榜top50总页数
    public long getTotalPageSizeByCategory(String category_id,String brandName,String start_time,String end_time){
        StringBuilder sb =new StringBuilder(" ");
        if (brandName!=null&&!brandName.equals("全部")){
            sb.append(" and goodsbrand='"+brandName+"' ");
        }
        return Db.use(MAIN2).findFirst("SELECT COUNT(tt.goodsname) as 'total_page' FROM\n" +
                "(SELECT goodsname FROM eb_mod_all_sales_goods\n" +
                " WHERE category_id='"+category_id+"' and "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"'  "+sb+"\n" +
                " GROUP BY goodsid LIMIT 50) tt").getLong("total_page");
    }



    //品牌中内竞争排名top20
    public List<Record> getGoodsTopByBrand(int  saleType,String goodsbrand,int pageNumber,String start_time,String end_time){
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        return Db.use(MAIN2).paginate(pageNumber, PAGESIZE, "SELECT goodsid,SUM(" + sale_attr + ") as 'total',goodsname,goodsbrand,squdao as 'channel',category", "FROM eb_mod_all_sales_goods\n" +
                " WHERE  goodsbrand='" + goodsbrand + "' and "+UPDATETIME+">='" + start_time + "' and "+UPDATETIME+"<='" + end_time + "'\n" +
                " GROUP BY goodsid ORDER BY total desc  ").getList();
    }
    //品牌中内竞争排名总页数
    public long getTotalPageSizeByBrand(String goodsbrand,String start_time,String end_time){
        return Db.use(MAIN2).findFirst("SELECT COUNT(tt.goodsname) as 'total_page' FROM\n" +
                "(SELECT goodsname FROM eb_mod_all_sales_goods\n" +
                " WHERE  goodsbrand='"+goodsbrand+"' and "+UPDATETIME+">='"+start_time+"' and "+UPDATETIME+"<='"+end_time+"'\n" +
                " GROUP BY goodsid  LIMIT 20) tt").getLong("total_page");
    }
    //产品分析-->产品对比--------------------------------------------------------------


    //线上产品对比

    public List<Record> getOnlineGoodsduibi(String category_id,String start_time,String end_time){

        return Db.use(MAIN2).find("SELECT goodsname,goodsid,squdao as 'channel','"+start_time+"至"+end_time+"' as 'time',SUM(daynum) as 'total'," +
                "round(AVG(goods_score),2) as 'goods_score' from eb_mod_all_sales_goods \n" +
                "  WHERE category_id='"+category_id+"' and   "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"' \n" +
                "  GROUP BY goodsid   ORDER BY goods_score desc  LIMIT 5");
    }

    //线上产品搜索
    public List<Record> getGoodsByGoodsName(String category_id,int pageNumber,String goodsName,String start_time,String end_time){
        return Db.use(MAIN2).paginate(pageNumber,PAGESIZE,"SELECT goodsname,goodsid,SUM(daynum) as 'total',round(AVG(goods_score),2) as 'goods_score',round(AVG(goods_mod_comment1),2) as 'goods_mod_comment1',\n" +
                " round(AVG(goods_mod_comment2),2) as 'goods_mod_comment2',round(AVG(goods_mod_comment3),2) as 'goods_mod_comment3', " +
                "ROUND(avg(goods_sales_rank),0) as 'rank',sum(goods_sales_flag) as 'goods_sales_flag'","from eb_mod_all_sales_goods \n" +
                "  WHERE  category_id='"+category_id+"' and "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"' \n" +
                "  AND goodsname LIKE '%"+goodsName+"%' GROUP BY goodsid   ORDER BY goods_score desc ").getList();
    }
    //线上产品搜索总页数
    public long getGoodsPageSize(String category_id,String goodsName,String start_time,String end_time){
        return Db.use(MAIN2).findFirst("SELECT COUNT(tt.goodsid) as 'total_page'  FROM (SELECT goodsname,goodsid from eb_mod_all_sales_goods \n" +
                "  WHERE category_id='"+category_id+"' and  "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"' \n" +
                "  AND goodsname LIKE '%"+goodsName+"%' GROUP BY goodsid limit 1000 ) tt").getLong("total_page");
    }



    //产品对比-产品销售情况对比

    public List<Record> getGoodsSaleduibi(int  saleType,String goodsidList[],String start_time,String end_time){
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        StringBuilder sb =new StringBuilder(" ");
        if (goodsidList!=null&&goodsidList.length!=0){
            sb.append(" and (goodsid=");
            for (int i=0;i<goodsidList.length;i++){
                if (i<goodsidList.length-1){
                    sb.append("'"+goodsidList[i]+"'").append(" or goodsid= ");
                }else {
                    sb.append("'"+goodsidList[i]+"'").append(") ");
                }
            }
        }
        return Db.use(MAIN2).find("SELECT DATE_FORMAT(updatetime,'%Y-%m-%d') as 'day',goodsid,goodsname,squdao as 'channel',round(SUM("+sale_attr+")/10000,2) as 'total' \n" +
                "  FROM  eb_mod_all_sales_goods WHERE  \n" +
                "   "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"' "+sb+"\n" +
                "  GROUP BY DATE_FORMAT(updatetime,'%Y-%m-%d'),goodsid");
    }

    //产品各项参数对比
    public List<Record> getGoodsAttrduibi(String goodsname[],String goodsidList[],String start_time,String end_time){

        StringBuilder sb =new StringBuilder(" ");
        if (goodsidList!=null&&goodsidList.length!=0){
            sb.append(" and (goodsid=");
            for (int i=0;i<goodsidList.length;i++){
                if (i<goodsidList.length-1){
                    sb.append("'"+goodsidList[i]+"'").append(" or goodsid= ");
                }else {
                    sb.append("'"+goodsidList[i]+"'").append(") ");
                }
            }
        }

                return Db.use(MAIN2).find("SELECT goodsbrand,goodsname,goodsid,squdao as 'channel'," +
                "round(AVG(goods_mod_comment1),5) as 'A1',sum(goods_mod_sum1) as 'A2'," +
                "round(AVG(goods_mod_comment2),5) as 'A3',sum(goods_mod_sum1) as 'A4'," +
                "round(AVG(goods_mod_comment3),5) as 'A5',sum(goods_mod_sum1) as 'A6'," +
                "round(AVG(goods_mod_quality_rate),5) as 'A7',round(sum(goods_mod_quality_comment_sum),5) as 'A8'  ,\n" +
                "round(AVG(goods_mod_pack_rate),5) as 'A9',round(AVG(goods_mod_cost_rate),5) as 'A10',\n" +
                "round(sum(goods_mod_pack_comment_sum),5) as 'A11',round(sum(goods_mod_cost_comment_sum),5) as 'A12',\n" +
                "round(AVG(goods_mod_flavor_rate),5) as 'A13',round(sum(goods_mod_flavor_comment_sum),5) as 'A14'\n" +
                " FROM  eb_mod_all_sales_goods WHERE  \n" +
                "   "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"' "+sb+"\n" +
                "GROUP BY goodsid");

//        return Db.use(MAIN2).find("SELECT goodsname,goodsid,squdao as 'channel',round(AVG(goods_unit_price),2) as 'price',round(AVG(goods_score),2) as 'goods_score',goods_unit_degree ,round(AVG(goods_mod_comment1),2) as 'goods_mod_comment1'," +
//                "round(AVG(goods_mod_comment2),2) as 'goods_mod_comment2',round(AVG(goods_mod_comment3),2) as 'goods_mod_comment3' \n" +
//                "  FROM  eb_mod_all_sales_goods WHERE  \n" +
//                "   "+UPDATETIME+">='"+start_time+"' and "+UPDATETIME+"<='"+end_time+"' "+sb+"\n" +
//                "  GROUP BY goodsid");
    }

//    return Db.use(MAIN2).find("SELECT goodsbrand,goodsname,goodsid,squdao as 'channel'," +
//                                      "round(AVG(goods_mod_comment1),5) as '好评率',sum(goods_mod_sum1) as '好评数'" +
//                                      "round(AVG(goods_mod_comment2),5) as '中评率',sum(goods_mod_sum1) as '中评数'" +
//                                      "round(AVG(goods_mod_comment3),5) as '差评率',sum(goods_mod_sum1) as '差评数'" +
//                                      "round(AVG(goods_mod_quality_rate),5) as '品质关注度',round(sum(goods_mod_quality_comment_sum),5) as '品质评论数'  ,\n" +
//                                      "round(AVG(goods_mod_pack_rate),5) as '外观关注度',round(AVG(goods_mod_cost_rate),5) as '性价比关注度',\n" +
//                                      "round(sum(goods_mod_pack_comment_sum),5) as '外观评论数',round(sum(goods_mod_cost_comment_sum),5) as '性价比评论数',\n" +
//                                      "round(AVG(goods_mod_flavor_rate),5) as '口感关注度',round(sum(goods_mod_flavor_comment_sum),5) as '口感评论数'\n" +
//                                      " FROM  eb_mod_all_sales_goods WHERE  \n" +
//                                      "   "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"' "+sb+"\n" +
//                                      "GROUP BY goodsid");
    





    //产品覆盖区域对比
    public List<Record> getGoodsArea(String[] goodsidList,String start_time,String end_time){
        StringBuilder sb =new StringBuilder(" ");
        if (goodsidList!=null&&goodsidList.length!=0){
            sb.append(" and (goodsid=");
            for (int i=0;i<goodsidList.length;i++){
                if (i<goodsidList.length-1){
                    sb.append("'"+goodsidList[i]+"'").append(" or goodsid= ");
                }else {
                    sb.append("'"+goodsidList[i]+"'").append(") ");
                }
            }
        }
        return Db.use(MAIN2).find("SELECT goodsname,goodsid,store_area,SUM(daynum) as 'total' FROM eb_mod_all_sales_goods\n" +
                " WHERE   "+UPDATETIME+">='"+start_time+"' and "+UPDATETIME+"<='"+end_time+"'  "+sb+"\n" +
                " GROUP BY goodsid,store_area order by total desc") ;
    }
    
}


