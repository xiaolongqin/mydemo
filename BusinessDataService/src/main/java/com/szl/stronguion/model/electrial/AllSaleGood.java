package com.szl.stronguion.model.electrial;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * Created by 小龙
 * on 15-11-2
 * at 下午4:52.
 */

public class AllSaleGood extends Model<AllSaleGood> {
    private static AllSaleGood dao = new AllSaleGood();
    public static final String TABLE = "eb_mod_all_sales_goods";
    public static final String MAIN2 = "main2";
    public static final int PAGESIZE=10;
    public static final String UPDATETIME = "DATE_FORMAT(updatetime,'%Y-%m-%d')";
    public static final String UPDATETIME1 = "updatetime";
    //电商交易概览-》市场细分行业-》二层页面----------------------------------
    //获取细分市场价格区间热销产品扫描--分页
    public List<Record> getChannelTotal(int seqType,String[] channel,int pageNumber,int  saleType,String category_id,String price,String start_time,String end_time){
        StringBuilder sb =getChannelStr(channel);

        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        if (saleType==3){
            sale_attr=" (goods_mod_sum1+goods_mod_sum2+goods_mod_sum3) ";
        }
        String seq_attr=seqType==1?"desc":"asc";

            return Db.use(MAIN2).paginate(pageNumber, PAGESIZE, "select goods_url,goodsid,goodsname,goodsbrand,category,squdao as 'channel'," +
                    "ROUND(avg(goods_sales_rank),0) as 'rank',sum(goods_sales_flag) as 'goods_sales_flag',sum(daynum) as 'total_num',sum(daysales) as 'total_sale'," +
                    " round(avg(goods_unit_price),1) as 'price', SUM(" + sale_attr + ") as 'total',sum(goods_mod_sum1+goods_mod_sum2+goods_mod_sum3) as 'total_comment' \n" +
                    "  ", "from eb_mod_all_sales_goods WHERE category_id='" + category_id + "'\n" +
                    "  and goods_attr_price='" + price + "' and " + UPDATETIME1 + ">='" + start_time + "' and " + UPDATETIME1 + "<='" + end_time +
                    "' " + sb + " GROUP BY goodsid ORDER BY total "+seq_attr+" ").getList();
    }

    //获取总页数
    public long getTotalPageSize(String[] channel,int  saleType,String category_id,String price,String start_time,String end_time){
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
        return Db.use(MAIN2).findFirst("SELECT COUNT(tt.goodsname) as 'total' from (select goodsname \n" +
                "  from eb_mod_all_sales_goods WHERE category_id='"+category_id+"'\n" +
                "  and goods_attr_price='"+price+"' and "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"' "+sb+" GROUP BY goodsid limit 1000) tt").get("total");
    }
    //获取细分市场价格区间热销产品扫描--全部
    public List<Record> getAllChannelTotal(int seqType,String[] channel,int pageNumber,int  saleType,String category_id,String price,String start_time,String end_time){
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



        return Db.use(MAIN2).find("select goods_url,goodsid,goodsname,goodsbrand,category,squdao as 'channel'," +
                "sum(goods_sales_flag) as 'goods_sales_flag',sum(daynum) as 'total_num',sum(daysales) as 'total_sale'," +
                " round(avg(goods_unit_price),1) as 'price', SUM(" + sale_attr + ") as 'total' \n" +
                " from eb_mod_all_sales_goods WHERE category_id='" + category_id + "'\n" +
                "  and goods_attr_price='" + price + "' and " + UPDATETIME1 + ">='" + start_time + "' " +
                "and " + UPDATETIME1 + "<='" + end_time + "' " + sb + " GROUP BY goodsid ORDER BY total "+seq_attr+" ");
    }

    //获取时间段内所有渠道
    public List<Record> getAllChannelByTime(String price,String category_id,String start_time,String end_time){
        return Db.use(MAIN2).find("SELECT squdao as 'channel' from eb_mod_all_sales_goods \n" +
                " WHERE goods_attr_price='" + price + "' and category_id='" + category_id + "' and "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"' GROUP BY squdao");
    }

    //细分市场销量变化
    public List<Record> getDetailMarketSale(String[] channel,int  saleType,String category_id,String price,String start_time,String end_time){
        StringBuilder sb =getChannelStr(channel);
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        if (saleType==3){
            sale_attr=" (goods_mod_sum1+goods_mod_sum2+goods_mod_sum3) ";
        }
            return Db.use(MAIN2).find("select DATE_FORMAT(updatetime,'%Y-%m-%d') as 'day',round(SUM("+sale_attr+"),2) as 'total' \n" +
                    "from eb_mod_all_sales_goods WHERE category_id='"+category_id+"'\n" +
                    "and goods_attr_price='"+price+"' and "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"' "+sb+" GROUP BY DATE_FORMAT(updatetime,'%Y-%m-%d') ;");
    }

    //细分市场品牌占比
    public List<Record> getGoodsBrandPercent(String[] channel,int  saleType,String category_id,String price,String start_time,String end_time){
        StringBuilder sb =getChannelStr(channel);
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        if (saleType==3){
            sale_attr=" (goods_mod_sum1+goods_mod_sum2+goods_mod_sum3) ";
        }
            return Db.use(MAIN2).find("select goodsbrand,ROUND(SUM("+sale_attr+")*100/(select SUM("+sale_attr+") as 'total'\n" +
                    "from eb_mod_all_sales_goods WHERE category_id='"+category_id+"'\n" +
                    "and goods_attr_price='"+price+"' and "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"' "+sb+"),2) as 'percent' \n" +
                    "from eb_mod_all_sales_goods WHERE category_id='"+category_id+"'\n" +
                    "and goods_attr_price='"+price+"' and "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"' "+sb+" GROUP BY goodsbrand order by percent desc ;");
    }

    //电商交易概览-》细分行业产品参数偏好-》二层页面----------------------------------
    //30-40%Vol白酒产品销量情况
    public List<Record> getDetailGoodsInfo(int  saleType,String category_id,int good_attr_type,String good_attr,String start_time,String end_time){
        String sale_attr="daysales";
        String goods_attr_name="goods_attr_degree";
        if (saleType==2){
            sale_attr="daynum";
        }
        if (good_attr_type==2){
            goods_attr_name="goods_attr_flavor";
        }
        return Db.use(MAIN2).find("select DATE_FORMAT(updatetime,'%Y-%m-%d') as 'day',round(SUM("+sale_attr+"),2) as 'total' \n" +
                "  from eb_mod_all_sales_goods WHERE category_id='"+category_id+"'\n" +
                "  and "+goods_attr_name+"='"+good_attr+"' and "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"' GROUP BY DATE_FORMAT(updatetime,'%Y-%m-%d') ");
    }


    //30-40%白酒热销品牌
    public List<Record> getGoodsBrandInfo(int good_attr_type,String good_attr,int  saleType,String category_id,String start_time,String end_time){
        String sale_attr="daysales";
        String goods_attr_name="goods_attr_degree";
        if (saleType==2){
            sale_attr="daynum";
        }
        if (good_attr_type==2){
            goods_attr_name="goods_attr_flavor";
        }
            return Db.use(MAIN2).find("select goodsbrand,round(SUM("+sale_attr+")/10000,2) as 'total' \n" +
                    "  from eb_mod_all_sales_goods WHERE category_id='"+category_id+"'\n" +
                    "  and "+goods_attr_name+"='"+good_attr+"' and "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"' GROUP BY goodsbrand ORDER BY total DESC;");

    }


    //30-40%白酒热销产品TOP5
    public List<Record> getGoodsNameInfo(int good_attr_type,String good_attr,String category_id,String start_time,String end_time){
        String goods_attr_name="goods_attr_degree";
        if (good_attr_type==2){
            goods_attr_name="goods_attr_flavor";
        }
            return Db.use(MAIN2).find("SELECT goodsname,goodsid,goodsbrand,sum(goods_mod_sum1) as 'goods_mod_sum1',sum(goods_mod_sum2) as 'goods_mod_sum2',\n" +
                    " sum(goods_mod_sum3) as 'goods_mod_sum3'  from  eb_mod_all_sales_goods WHERE category_id='"+category_id+"'  AND\n" +
                    " "+goods_attr_name+"='"+good_attr+"'  and  "+UPDATETIME1+">='"+start_time+"'  AND\n" +
                    " "+UPDATETIME1+"<='"+end_time+"'  GROUP BY goodsid ORDER BY goods_mod_sum1 desc  LIMIT 5");
    }

    //30-40%白酒产品消费者关注点

    public List<Record> getUserFocus(int good_attr_type,String good_attr,String category_id,String start_time,String end_time){
        String goods_attr_name="goods_attr_degree";
        if (good_attr_type==2){
            goods_attr_name="goods_attr_flavor";
        }
        return Db.use(MAIN2).find("SELECT " +
                "sum(goods_mod_sum1) as 'A1'," +
                "sum(goods_mod_sum2) as 'A2'," +
                "sum(goods_mod_sum3) as 'A3'," +
                "sum(goods_mod_quality_comment_sum) as 'A4'  ,\n" +
                "sum(goods_mod_pack_comment_sum) as 'A5'," +
                "sum(goods_mod_cost_comment_sum) as 'A6',\n" +
                "sum(goods_mod_flavor_comment_sum) as 'A7'\n" +
                " FROM  eb_mod_all_sales_goods WHERE category_id='"+category_id+"' and "+goods_attr_name+"='"+good_attr+"' and\n" +
                "   "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"' ");
//            return Db.use(MAIN2).find("SELECT goodsbrand,goodsname,goodsid,squdao as 'channel'," +
//                                      "sum(goods_mod_sum1) as '好评数'" +
//                                      "sum(goods_mod_sum2) as '中评数'" +
//                                      "sum(goods_mod_sum3) as '差评数'" +
//                                      "sum(goods_mod_quality_comment_sum) as '品质评论数'  ,\n" +
//                                      "sum(goods_mod_pack_comment_sum) as '外观评论数'," +
//                                      "sum(goods_mod_cost_comment_sum) as '性价比评论数',\n" +
//                                      "sum(goods_mod_flavor_comment_sum) as '口感评论数'\n" +
//                                      " FROM  eb_mod_all_sales_goods WHERE category_id='"+category_id+"' and "+goods_attr_name+"='"+good_attr+"' and\n" +
//                                      "   "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"' \n" +
//                                      "GROUP BY goodsid");

    }



    //30-40%白酒区域热度
    public List<Record> getGoodsArea(String good_attr,int  saleType,String category,String start_time,String end_time){
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        return Db.use(MAIN2).find("select store_area,round(SUM("+sale_attr+")/1000,2) as 'total',round(SUM("+sale_attr+")*100/(select SUM("+sale_attr+") \n" +
                "  from eb_mod_all_sales_goods WHERE category='"+category+"'\n" +
                "  and goods_attr_degree='"+good_attr+"' and "+UPDATETIME+">='"+start_time+"' and "+UPDATETIME+"<='"+end_time+"' ),2) as 'percent' \n" +
                "  from eb_mod_all_sales_goods WHERE category='"+category+"'\n" +
                "  and goods_attr_degree='"+good_attr+"' and "+UPDATETIME+">='"+start_time+"' and "+UPDATETIME+"<='"+end_time+"' GROUP BY store_area ORDER BY percent DESC");
    }

    private StringBuilder getChannelStr(String[] channel){
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
        return sb;
    }


}
