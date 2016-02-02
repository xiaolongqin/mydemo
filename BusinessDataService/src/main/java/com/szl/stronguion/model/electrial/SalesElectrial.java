package com.szl.stronguion.model.electrial;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * Created by 小龙
 * on 15-10-15
 * at 下午4:32.
 */

public class SalesElectrial{
    private static SalesElectrial dao=new SalesElectrial();
    public static final String UPDATETIME = "DATE_FORMAT(updatetime,'%Y-%m-%d')";
    public static final String UPDATETIME1 = "updatetime";
    //市场细分行业--------
    //细分市场销售排名
    public List<Record> getDtailGoodTop(int  saleType,String startTime,String endTime){
        if (saleType==1){
            return Db.use("main2").find("select category as 'goodName',SUM(daysales) as 'total' \n" +
                    "from eb_mod_all_sales_goods WHERE "+UPDATETIME+">='"+startTime+"' and "+UPDATETIME+"<='"+endTime+"'\n" +
                    " GROUP BY category_id ORDER BY total DESC");
        }else {
            return Db.use("main2").find("select category as 'goodName',SUM(daynum) as 'total' \n" +
                    "from eb_mod_all_sales_goods WHERE "+UPDATETIME+">='"+startTime+"' and "+UPDATETIME+"<='"+endTime+"'\n" +
                    " GROUP BY category_id ORDER BY total DESC");
        }

    }
    //细分市场概览
    public List<Record> getDtailMarket(String[] channel,int saleType,String  category_id,String startTime,String endTime){
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        if (saleType==3){
            sale_attr=" (goods_mod_sum1+goods_mod_sum2+goods_mod_sum3) ";
        }

        StringBuilder sb =getChannelStr(channel);
            return Db.use("main2").find("select category as 'goodName',SUM("+sale_attr+") as 'total',DATE_FORMAT(updatetime,'%Y-%m-%d') as 'day' \n" +
                    "from eb_mod_all_sales_goods \n" +
                    "WHERE  category_id='"+category_id+"' and "+UPDATETIME1+">='"+startTime+"' and "+UPDATETIME1+"<='"+endTime+"' "+sb+" GROUP BY "+UPDATETIME+";");

    }

    //行业用户关注点透视
    public List<Record> getUserFocus(String[] channel,String  category_id,String startTime,String endTime){
//        StringBuilder sb =getChannelStr(channel);

        return Db.use("main2").find("select good_comment_sum/(good_comment_sum+neutral_comment_sum+bad_comment_sum) as '好评'," +
                "neutral_comment_sum/(good_comment_sum+neutral_comment_sum+bad_comment_sum) as '中评',\n" +
                "bad_comment_sum/(good_comment_sum+neutral_comment_sum+bad_comment_sum) as '差评',avg(quality_rate) as '品质'," +
                "avg(pack_rate) as '外观',avg(cost_rate) as '性价比',avg(flavor_rate) as '口感' from  eb_mod_market_analy\n" +
                " WHERE category_id='"+category_id+"' and "+UPDATETIME1+">='"+startTime+"' and "+UPDATETIME1+"<='"+endTime+"'  ");
    }


    //行业价格分布
    public List<Record> getDtailPrice(String[] channel,String  category_id,String startTime,String endTime){
        StringBuilder sb =new StringBuilder(" ");
        if (channel!=null&&channel.length!=0){
            sb.append(" and (t1.squdao=");
            for (int i=0;i<channel.length;i++){
                if (i<channel.length-1){
                    sb.append("'"+channel[i]+"'").append(" or t1.squdao= ");
                }else {
                    sb.append("'"+channel[i]+"'").append(") ");
                }
            }
        }
        return Db.use("main2").find("SELECT t2.attr_name as 'name',t1.goods_attr_price as 'A',SUM(t1.daynum) 'total_num' from eb_mod_all_sales_goods t1, eb_rpt_goods_attr_price t2\n" +
                "WHERE t1.goods_attr_price=t2.attr_id and t1.category_id='"+category_id+"' and t1."+UPDATETIME1+">='"+startTime+"' \n" +
                "and t1."+UPDATETIME1+"<='"+endTime+"' "+sb+" GROUP BY t1.goods_attr_price ");
    }


    //白酒产品参数与销量
    public List<Record> getDegreeSale(int saleType,String  category_id,String startTime,String endTime){
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        return Db.use("main2").find("SELECT t2.attr_name as 'name',t1.goods_attr_degree as 'A',SUM(t1."+sale_attr+") 'total_num' \n" +
                " from eb_mod_all_sales_goods t1, eb_rpt_goods_attr_degree t2\n" +
                " WHERE t1.goods_attr_degree=t2.attr_id and t1.category_id='"+category_id+"' and t1."+UPDATETIME1+">='"+startTime+"' \n" +
                " and t1."+UPDATETIME1+"<='"+endTime+"' GROUP BY t1.goods_attr_degree");
    }

    //白酒的香型分类
    public List<Record> getXiangxinName(){
        return Db.use("main2").find("SELECT attr_name as 'name',attr_id from eb_rpt_goods_attr_flavor");
    }

    //白酒各香型酒销量
    public List<Record> getXiangxinSale(int saleType,String  category_id,String startTime,String endTime){
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        return Db.use("main2").find("SELECT t1.category as 'goodName' ,DATE_FORMAT(t1.updatetime,'%Y-%m-%d') as 'day',t2.attr_name as 'xiangxin',SUM(t1."+sale_attr+") as 'total'\n" +
                "           from eb_mod_all_sales_goods t1,eb_rpt_goods_attr_flavor t2\n" +
                "           WHERE category_id='"+category_id+"' and t2.attr_id=t1.goods_attr_flavor and t1."+UPDATETIME1+">='"+startTime+"' and t1."+UPDATETIME1+"<='"+endTime+"'\n" +
                "           GROUP BY DATE_FORMAT(t1.updatetime,'%Y-%m-%d'),t2.attr_name");
    }
    //获取所有产品分类名
    public List<Record> getAllGoodName(){
        return Db.use("main2").find("SELECT category as 'goodName' FROM eb_mod_all_sales_goods \n" +
                " GROUP BY category_id ");
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
