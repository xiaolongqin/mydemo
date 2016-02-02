package com.szl.stronguion.model.goodsbrandAnalysis;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.utils.FormatUtils;

import java.util.List;

/**
 * Created by 小龙
 * on 15-11-24
 * at 下午2:58.
 */

public class GoodsbrandAnalysis {

    public static final String MAIN2 = "main2";
    public static final int PAGESIZE=10;
    public static final String UPDATETIME = "DATE_FORMAT(updatetime,'%Y-%m-%d')";
    public static final String UPDATETIME1 = "updatetime";

    //品牌分析-》品牌整体扫描--------------------------------------------------------------
    //品牌线上销售排行--分页
    public List<Record> getGoodsbrandSaleTotal(int seqType,String brandName,String category_id,String[] channel,int pageNumber,int  saleType,String start_time,String end_time){
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
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        if (saleType==3){
            sale_attr=" (goods_mod_sum1+goods_mod_sum2+goods_mod_sum3) ";
        }
        String seq_attr=seqType==1?"desc":"asc";
        return Db.use(MAIN2).paginate(pageNumber, PAGESIZE, "select goodsbrand,category,GROUP_CONCAT(DISTINCT  squdao ) as 'channel',SUM(" + sale_attr + ") as 'total', " +
                "  sum(daynum) as 'total_num',round(sum(daysales),0) as 'total_sale',ROUND(avg(goods_sales_rank),0) as 'rank',sum(goods_sales_flag) as 'goods_sales_flag'\n" +
                "  ", "from eb_mod_all_sales_goods WHERE category_id='" + category_id + "' and goodsbrand like '%"+brandName+"%' and " + UPDATETIME1 + ">='" + start_time + "' and " + UPDATETIME1 + "<='" + end_time + "' " + sb + "\n" +
                "     GROUP BY goodsbrand ORDER BY total "+seq_attr+" ").getList();
    }

    //获取品牌线上销售排行总页数
    public long getGoodsbrandTotalPageSize(String brandName,String category_id,String[] channel,String start_time,String end_time){
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
        return Db.use(MAIN2).findFirst("SELECT COUNT(tt.goodsbrand) as 'total' from \n" +
                " (select goodsbrand,category,squdao as 'channel'\n" +
                "     from eb_mod_all_sales_goods WHERE category_id='" + category_id + "' and goodsbrand like '%" + brandName + "%'  and " + UPDATETIME1 + ">='" + start_time + "' and " + UPDATETIME1 + "<='" + end_time + "' " + sb + "\n" +
                "     GROUP BY goodsbrand  limit 1000)  tt").get("total");
    }

    public List<Record> getExportGoodsbrand(int seqType,String brandName,String category_id,String[] channel,int  saleType,String start_time,String end_time){
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
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        String seq_attr=seqType==1?"desc":"asc";
        return Db.use(MAIN2).find( "select goodsbrand,category,GROUP_CONCAT(DISTINCT  squdao ) as 'channel',SUM(" + sale_attr + ") as 'total', " +
                "  sum(daynum) as 'total_num',round(sum(daysales),0) as 'total_sale',ROUND(avg(goods_sales_rank),0) as 'rank',sum(goods_sales_flag) as 'goods_sales_flag'\n" +
                "  from eb_mod_all_sales_goods WHERE category_id='" + category_id + "' and goodsbrand like '%"+brandName+"%' and " + UPDATETIME1 + ">='" + start_time + "' and " + UPDATETIME1 + "<='" + end_time + "' " + sb + "\n" +
                "     GROUP BY goodsbrand ORDER BY total "+seq_attr+" ");
    }


    //细分行业品牌销售排行top10
    public List<Record> getGoodsbrandTopBycategory(String[] channel,String category_id,int  saleType,String start_time,String end_time){
        StringBuilder sb = FormatUtils.getChannelStr(channel);
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        if (saleType==3){
            sale_attr=" (goods_mod_sum1+goods_mod_sum2+goods_mod_sum3) ";
        }
        return Db.use(MAIN2).find("select goodsbrand,round(SUM(" + sale_attr + ")/10000,2) as 'total'\n" +
                "     from eb_mod_all_sales_goods WHERE category_id='"+category_id+"'   AND " + UPDATETIME1 + ">='" + start_time + "' and " + UPDATETIME1 + "<='" + end_time + "' \n" +
                "  "+sb+"   GROUP BY goodsbrand ORDER BY total DESC LIMIT 10");
    }

    //细分行业品牌飙升排行
    public List<Record> getGoodsbrandTop(int pageNumber,String category,int  saleType,String start_time,String end_time){
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        return Db.use(MAIN2).paginate(pageNumber, PAGESIZE, "SELECT goodsbrand,SUM(" + sale_attr + ") as 'total',sum(goods_sales_flag) as 'goodsbrand_top'  \n" +
                "  ", "FROM eb_mod_all_sales_goods\n" +
                " WHERE " + UPDATETIME + ">='" + start_time + "'  and  " + UPDATETIME + "<='" + end_time + "'\n" +
                "  AND category='" + category + "'\n" +
                "  GROUP BY goodsbrand ORDER BY goodsbrand_top DESC ").getList();
    }

    //细分行业品牌飙升排行总页数
    public long getGoodsbrandTopPageSize(String category,String start_time,String end_time){

        return Db.use(MAIN2).findFirst("SELECT COUNT(tt.goodsbrand) as 'total' from \n" +
                " (SELECT goodsbrand  FROM eb_mod_all_sales_goods\n" +
                " WHERE " + UPDATETIME + ">='" + start_time + "'  and  " + UPDATETIME + "<='" + end_time + "'\n" +
                "  AND category='" + category + "'  GROUP BY goodsbrand )  tt").get("total");
    }

    //品牌分析-》品牌特写--------------------------------------------------------------
    //品牌销售情况
    public List<Record> getGoodsbrandSaleInfo(String goodsbrand,int  saleType,String start_time,String end_time){
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        return Db.use(MAIN2).find("SELECT "+UPDATETIME+" as 'day' ,goodsbrand,round(SUM("+sale_attr+")/10000,2) as 'total'  FROM eb_mod_all_sales_goods\n" +
                " WHERE "+UPDATETIME1+">=DATE_SUB('"+start_time+"',INTERVAL 1 day)  and  "+UPDATETIME1+"<='"+end_time+"'\n" +
                " and goodsbrand='"+goodsbrand+"'  GROUP BY "+UPDATETIME+" ");
    }

    //品牌各渠道销量占比
    public List<Record> getGoodsbrandSaleBysqudao(String goodsbrand,int  saleType,String start_time,String end_time){
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        return Db.use(MAIN2).find("SELECT goodsbrand,squdao as 'channel',round(SUM("+sale_attr+")/10000,2) as 'total' from eb_mod_all_sales_goods\n" +
                " WHERE goodsbrand='"+goodsbrand+"' AND  "+UPDATETIME1+">='"+start_time+"' AND\n" +
                " "+UPDATETIME1+"<='"+end_time+"' GROUP BY squdao ");
    }

    //品牌热销产品排行TOP5
    public List<Record> getGoodsSaleByGoodsbrand(String goodsbrand,int  saleType,String start_time,String end_time){
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        return Db.use(MAIN2).find("SELECT goodsbrand,goodsname,goodsid,category,squdao as 'channel',round(sum("+sale_attr+")/10000,2) as 'total' from eb_mod_all_sales_goods\n" +
                " WHERE goodsbrand='"+goodsbrand+"' AND  "+UPDATETIME1+">='"+start_time+"' AND\n" +
                " "+UPDATETIME1+"<='"+end_time+"' GROUP BY goodsid ORDER BY total  DESC LIMIT 5 ");
    }

    //品牌市场覆盖
    public List<Record> getGoodsbrandAreaduibi(String goodsbrandList[],int saleType,String start_time,String end_time){
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        StringBuilder sb =new StringBuilder(" ");
        if (goodsbrandList!=null&&goodsbrandList.length!=0){
            sb.append(" and (goodsbrand=");
            for (int i=0;i<goodsbrandList.length;i++){
                if (i<goodsbrandList.length-1){
                    sb.append("'"+goodsbrandList[i]+"'").append(" or goodsbrand= ");
                }else {
                    sb.append("'"+goodsbrandList[i]+"'").append(") ");
                }
            }
        }
        return Db.use(MAIN2).find("SELECT goodsbrand,store_area,SUM("+sale_attr+") as 'total'  FROM eb_mod_all_sales_goods\n" +
                "  WHERE "+UPDATETIME+">='"+start_time+"'  and  "+UPDATETIME+"<='"+end_time+"'\n" +
                "  GROUP BY goodsbrand,store_area");
    }

    //品牌市场覆盖-点击事件top10
    public List<Record> getGoodsbrandTopByBrand(String area,String goodsbrand,int saleType,String start_time,String end_time){
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        return Db.use(MAIN2).find("SELECT goodsid,goodsname,store_area,round(SUM("+sale_attr+")/10000,2) as 'total'  FROM eb_mod_all_sales_goods\n" +
                "  WHERE goodsbrand='"+goodsbrand+"' and store_area='"+area+"' and "+UPDATETIME+">='"+start_time+"'  and  "+UPDATETIME+"<='"+end_time+"'\n" +
                "  GROUP BY goodsid order by total desc limit 10 ");
    }

    //品牌价格分布
    public List<Record> getGoodsbrandPriceInfo(String goodsbrand,int saleType,String start_time,String end_time){
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        return Db.use(MAIN2).find("SELECT t2.attr_name as 'name',goodsbrand,t1.goods_attr_price as 'A',round(SUM(t1."+sale_attr+")/10000,2) 'total_num' from eb_mod_all_sales_goods t1, eb_rpt_goods_attr_price t2\n" +
                "       WHERE t1.goods_attr_price=t2.attr_id and t1.goodsbrand='"+goodsbrand+"' and t1."+UPDATETIME1+">='"+start_time+"'\n" +
                "       and t1."+UPDATETIME1+"<='"+end_time+"' GROUP BY t1.goods_attr_price ");
    }


    //品牌印象
    public List<Record> getGoodsbrandFace(String goodsbrand,String start_time,String end_time){

        return Db.use(MAIN2).find("SELECT word,goodsbrand,round(AVG(weight),2) as 'weight' from  eb_mod_wordcloud_weekly \n" +
                " WHERE goodsbrand='"+goodsbrand+"'  AND "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"'\n" +
                " GROUP BY word ORDER BY weight desc LIMIT 20 ");
    }
    //品牌口碑变化
    public List<Record> getGoodsbrandFaceChange(String goodsbrand,String start_time,String end_time){

        return Db.use(MAIN2).find("SELECT DATE_FORMAT(updatetime,'%Y-%m-%d') as 'day',goodsbrand,SUM(goods_mod_sum1+goods_mod_sum2+goods_mod_sum3) as 'goods_mod_sum1',\n" +
                " round(AVG(goods_mod_comment1),4) as 'goods_mod_comment1' from  eb_mod_all_sales_goods \n" +
                " WHERE goodsbrand='"+goodsbrand+"'  AND "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"'\n" +
                " GROUP BY DATE_FORMAT(updatetime,'%Y-%m-%d')");
    }







    //品牌分析-》品牌对比--------------------------------------------------------------
    //品牌top5
    public List<Record> getGoodsbrandTop5(String category_id,String start_time,String end_time){
        return Db.use(MAIN2).find("SELECT  '"+start_time+"至"+end_time+"' as 'time',goodsbrand,round(AVG(goods_mod_comment1),5)  as 'total'\n" +
                "  FROM eb_mod_all_sales_goods WHERE category_id='"+category_id+"' and "+UPDATETIME1+">='"+start_time+"'  AND "+UPDATETIME1+"<='"+end_time+"'\n" +
                "  GROUP BY goodsbrand ORDER BY  total DESC,goodsbrand asc LIMIT 5");
    }



    //线上品牌搜索
    public List<Record> getGoodsbrandByName(String category_id,int pageNumber,String goodsbrand,String start_time,String end_time){
        return Db.use(MAIN2).paginate(pageNumber,PAGESIZE,"SELECT goodsbrand,round(AVG(goods_mod_comment1),5) as 'goods_mod_comment1',\n" +
                "round(AVG(goods_mod_comment2),5) as 'goods_mod_comment2',round(AVG(goods_mod_comment3),5) as 'goods_mod_comment3'","from eb_mod_all_sales_goods\n" +
                " WHERE category_id='"+category_id+"' and goodsbrand like '%"+goodsbrand+"%' and "+UPDATETIME1+">='"+start_time+"'  and  "+UPDATETIME1+"<='"+end_time+"'\n" +
                " GROUP BY goodsbrand ORDER BY  goods_mod_comment1 DESC,goodsbrand asc ").getList();
    }
    //线上品牌搜索总页数
    public long getGoodsbrandPageSize(String category_id,String goodsbrand,String start_time,String end_time){
        return Db.use(MAIN2).findFirst("SELECT COUNT(tt.goodsbrand) as 'total_page'  FROM (SELECT goodsbrand from eb_mod_all_sales_goods\n" +
                " WHERE category_id='"+category_id+"' and goodsbrand like '%"+goodsbrand+"%' and "+UPDATETIME1+">='"+start_time+"'  \n" +
                " and  "+UPDATETIME1+"<='"+end_time+"'\n" +
                " GROUP BY goodsbrand limit 1000 ) tt").getLong("total_page");
    }


    //品牌分析-》品牌对比二层页面--------------------------------------------------------------
    //品牌各项参数对比
    public List<Record> getGoodsbrandAttrduibi(String goodsbrandList[],String start_time,String end_time){

        StringBuilder sb =new StringBuilder(" ");
        if (goodsbrandList!=null&&goodsbrandList.length!=0){
            sb.append(" and (goodsbrand=");
            for (int i=0;i<goodsbrandList.length;i++){
                if (i<goodsbrandList.length-1){
                    sb.append("'"+goodsbrandList[i]+"'").append(" or goodsbrand= ");
                }else {
                    sb.append("'"+goodsbrandList[i]+"'").append(") ");
                }
            }
        }
        return Db.use(MAIN2).find("SELECT goodsbrand," +
                "round(AVG(goods_mod_comment1),5) as 'A1'," +
                "round(AVG(goods_mod_comment2),5) as 'A2',round(AVG(goods_mod_comment3),5) as 'A3', " +
                "round(AVG(goods_mod_quality_rate),5) as 'A4',round(AVG(goods_mod_flavor_rate),5) as 'A5' ,\n" +
                "round(AVG(goods_mod_pack_rate),5) as 'A6',round(AVG(goods_mod_cost_rate),5) as 'A7',\n" +
                "round(sum(goods_mod_pack_comment_sum),5) as 'A8',round(sum(goods_mod_cost_comment_sum),5) as 'A9',\n" +
                "round(sum(goods_mod_quality_comment_sum),5) as 'A10',round(sum(goods_mod_flavor_comment_sum),5) as 'A11'\n" +
                " FROM  eb_mod_all_sales_goods WHERE  \n" +
                "   "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"' "+sb+"\n" +
                "  GROUP BY goodsbrand");

//        return Db.use(MAIN2).find("SELECT goodsbrand," +
//                "round(AVG(goods_mod_comment1),5) as '好评率'," +
//                "round(AVG(goods_mod_comment2),5) as '中评率',round(AVG(goods_mod_comment3),5) as '差评率', " +
//                "round(AVG(goods_mod_quality_rate),5) as '品质关注度',round(AVG(goods_mod_flavor_rate),5) as '口感关注度' ,\n" +
//                "round(AVG(goods_mod_pack_rate),5) as '外观关注度',round(AVG(goods_mod_cost_rate),5) as '性价比关注度',\n" +
//                "round(sum(goods_mod_pack_comment_sum),5) as '外观评论数',round(sum(goods_mod_cost_comment_sum),5) as '性价比评论数',\n" +
//                "round(sum(goods_mod_quality_comment_sum),5) as '品质评论数',round(sum(goods_mod_flavor_comment_sum),5) as '口感评论数'\n" +
//                " FROM  eb_mod_all_sales_goods WHERE  \n" +
//                "   "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"' "+sb+"\n" +
//                "  GROUP BY goodsbrand");
    }

}
