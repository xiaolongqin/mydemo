package com.szl.stronguion.model.channel;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * Created by 小龙
 * on 15-10-15
 * at 下午4:32.
 */

public class SalesChannel extends Model<SalesChannel> {
    public static final String DAYSALES = "daysales";
    public static final String QUDAO = "squdao";
    public static final String UPDATETIME = "DATE_FORMAT(updatetime,'%Y-%m-%d')";
    public static final String UPDATETIME1 = "updatetime";
    public static final String ID = "id";
    private static SalesChannel dao=new SalesChannel();
    public static final String MAIN2 = "main2";
    //渠道扫描--------
    //线上渠道销售总额排行
    public List<Record> getOnlineChannelTop(String category_id,String startTime,String endTime){
        return Db.use(MAIN2).find("select squdao as 'channel',SUM(daysales) as 'totalsale' from eb_mod_all_sales_goods  \n" +
                "WHERE  "+UPDATETIME1+"  >= '"+startTime+"' and  "+UPDATETIME1+" <='"+endTime+"' and category_id='"+category_id+"' GROUP BY squdao order by totalsale desc;");
    }

    public List<Record> getOnlineChannelPercent(String category_id,String startTime,String endTime){
        return Db.use(MAIN2).find("select squdao as 'channel',round(SUM(daysales)*100/(SELECT SUM(tt.totalsale) FROM (select squdao as 'channel',SUM(daysales)  as 'totalsale' from eb_mod_all_sales_goods \n" +
                "WHERE  "+UPDATETIME1+"  >= '"+startTime+"' and  "+UPDATETIME1+" <='"+endTime+"' and category_id='"+category_id+"'  GROUP BY squdao) tt),2) as 'percent' from eb_mod_all_sales_goods \n" +
                "WHERE  "+UPDATETIME1+"  >= '"+startTime+"' and   "+UPDATETIME1+" <='"+endTime+"' and category_id='"+category_id+"' GROUP BY squdao ;");
    }
    //线上电商细分行业交易总额排行
    public List<Record> getDetailChannelTop(String category_id,String startTime,String endTime){
        return Db.use(MAIN2).find("SELECT squdao as 'channel',sum(daynum) as 'total' from eb_mod_all_sales_goods\n" +
                " where category_id='"+category_id+"' \n" +
                " and  "+UPDATETIME1+" >='"+startTime+"' and  "+UPDATETIME1+" <='"+endTime+"'\n" +
                " GROUP BY squdao ORDER BY total desc LIMIT 10");
    }

    public Record getDetailChannelTopPercent(String category_id,String startTime,String endTime){
        return Db.use(MAIN2).findFirst("SELECT SUM(daynum) as total_sum from eb_mod_all_sales_goods\n" +
                "WHERE category_id='" + category_id + "' and  "+UPDATETIME1+" >='" + startTime + "' and  "+UPDATETIME1+" <='" + endTime + "'");
    }

    //获取所有goods分类
    public List<Record> getAllGoodName(){
        return Db.use(MAIN2).find("SELECT distinct category as 'goodName' from eb_mod_all_sales_goods ");
    }

    //渠道详情--------

    //获取所有渠道名
    public List<Record> getAllChannelName(){
//        String str=" ";
//        if (saleType==1||saleType==2){
//            str=" where daynum_flag=1 ";
//        }
//        if (saleType==3){
//            str=" where comment_flag=1 ";
//        }
        return Db.use(MAIN2).find("SELECT distinct squdao as 'channel' from eb_rpt_goods_attr_channel  ");
    }


    //根据条件获取渠道名
    public List<Record> getChannelName(int saleType){
        String str=" where daynum_flag=1 ";
        if (saleType==3){
            str=" where comment_flag=1 ";
        }
        return Db.use(MAIN2).find("SELECT distinct squdao as 'channel' from eb_rpt_goods_attr_channel "+str+" ");
    }

    //线上渠道销售情况
    //销售额
    public List<Record> getOnlineChannelSale1(String category_id,int saleType,String channelName,String startTime,String endTime){
        if (saleType==1){
            return Db.use(MAIN2).find("select  DATE_FORMAT(updatetime,'%Y-%m-%d') as 'day', squdao as 'channel',SUM(daysales) as 'total' \n" +
                    "    from eb_mod_all_sales_goods WHERE category_id='"+category_id+"' and  "+UPDATETIME1+"  >= '"+startTime+"' and   "+UPDATETIME1+" <='"+endTime+"' and squdao = '"+channelName+"' \n" +
                    "    GROUP BY  DATE_FORMAT(updatetime,'%Y-%m-%d') ORDER BY DATE_FORMAT(updatetime,'%Y-%m-%d')");
        }else {
            return Db.use(MAIN2).find("select  DATE_FORMAT(updatetime,'%Y-%m-%d') as 'day', squdao as 'channel',SUM(daynum) as 'total' \n" +
                    "    from eb_mod_all_sales_goods WHERE category_id='"+category_id+"' and "+UPDATETIME1+"  >= '"+startTime+"' and   "+UPDATETIME1+" <='"+endTime+"' and squdao = '"+channelName+"' \n" +
                    "    GROUP BY  DATE_FORMAT(updatetime,'%Y-%m-%d') ORDER BY DATE_FORMAT(updatetime,'%Y-%m-%d')");
        }

    }

    //渠道细分市场
    //销售额
    public List<Record> getChannelDetailSale1(String channelName,String startTime,String endTime){
        return Db.use(MAIN2).find("select category as 'name',ROUND(SUM(daysales),2) as 'totalsale' \n" +
                "    from eb_mod_all_sales_goods WHERE  "+UPDATETIME+"  >= '"+startTime+"' and   "+UPDATETIME+" <='"+endTime+"' \n" +
                "    and  squdao='"+channelName+"' \n" +
                "    GROUP BY category_id ORDER BY totalsale;");
    }
    //销售量
    public List<Record> getChannelDetailSale2(String channelName,String startTime,String endTime){
        return Db.use(MAIN2).find("select category as 'name',ROUND(SUM(daynum),2) as 'totalsale' \n" +
                "    from eb_mod_all_sales_goods WHERE  "+UPDATETIME+"  >= '"+startTime+"' and   "+UPDATETIME+" <='"+endTime+"'\n" +
                "    and  squdao='"+channelName+"'  \n" +
                "    GROUP BY category_id ORDER BY totalsale;");
    }

    /*渠道热门店铺TOP10*/
    //销售额
    public List<Record> getChannelShopTop1(String category_id,String channelName,String startTime,String endTime){
        return Db.use(MAIN2).find("SELECT store_id,store_name as shopName,SUM(daysales) as total\n" +
                " from eb_mod_all_sales_goods \n" +
                "WHERE category_id='"+category_id+"' and squdao='"+channelName+"' and "+UPDATETIME1+"  >= '"+startTime+"' and   "+UPDATETIME1+" <='"+endTime+"' \n" +
                "GROUP BY store_id ORDER BY total DESC LIMIT 10;");
    }
    //销售量
    public List<Record> getChannelShopTop2(String category_id,String channelName,String startTime,String endTime){
        return Db.use(MAIN2).find("SELECT store_id,store_name as shopName,SUM(daynum) as total\n" +
                "  from eb_mod_all_sales_goods \n" +
                "WHERE category_id='"+category_id+"' and  squdao='"+channelName+"' and "+UPDATETIME1+"  >= '"+startTime+"' and   "+UPDATETIME1+" <='"+endTime+"' \n" +
                "GROUP BY store_id ORDER BY total DESC LIMIT 10;");
    }

    /*渠道热销产品TOP10*/
    //销售额
    public List<Record> getChannelGoodTop1(String category_id,String channelName,String startTime,String endTime){
        return Db.use(MAIN2).find("SELECT goodsname as goodName ,squdao as 'channel',goodsid,goodsbrand, SUM(daysales) as total  \n" +
                "  FROM eb_mod_all_sales_goods \n" +
                "WHERE category_id='"+category_id+"' and squdao='"+channelName+"' and "+UPDATETIME1+"  >= '"+startTime+"' and   "+UPDATETIME1+" <='"+endTime+"' \n" +
                "GROUP BY goodsid ORDER BY total DESC LIMIT 10;");
    }

    //销售量
    public List<Record> getChannelGoodTop2(String category_id,String channelName,String startTime,String endTime){
        return Db.use(MAIN2).find("SELECT goodsname as goodName ,squdao as 'channel',goodsid,goodsbrand, SUM(daynum) as total  \n" +
                "  FROM eb_mod_all_sales_goods \n" +
                "WHERE category_id='"+category_id+"' and squdao='"+channelName+"' and "+UPDATETIME1+"  >= '"+startTime+"' and   "+UPDATETIME1+" <='"+endTime+"' \n" +
                "GROUP BY goodsid ORDER BY total DESC LIMIT 10;");
    }

    //渠道对比--------
    //渠道销量情况对比对比(按天)
    public List<Record> getChannelduibi(String category_id,String[] channelName,String startTime,String endTime){
        String str=" ";
        for (int i=0;i<channelName.length;i++){
            if (i!=0||i!=channelName.length-1){
                str+=" or squdao='"+channelName[i]+"'";
            }else {
                if(i==0){
                    str+=" and ( squdao='"+channelName[i]+"'";
                }
                if (i==channelName.length-1){
                        str+=" or squdao='"+channelName[i]+"')";
                }
            }


        }
        return Db.use(MAIN2).find("SELECT DATE_FORMAT(updatetime,'%Y-%m-%d') as 'day',squdao as 'channel',SUM(daysales) as 'total_sale',SUM(daynum) as 'total_num' from eb_mod_all_sales_goods\n" +
                "  WHERE category_id='"+category_id+"' and  "+UPDATETIME1+" >='"+startTime+"' and  "+UPDATETIME1+" <='"+endTime+"' "+str+"" +
                "  GROUP BY  "+UPDATETIME+" ,squdao");
    }
    //渠道对比（各细分市场销售对比）
    public List<Record> getChannelduibiByMonth(String category_id,String startTime,String endTime){

        return Db.use(MAIN2).find("SELECT category as 'goodName' ,squdao as 'channel',SUM(daysales) as 'total_sale',SUM(daynum) as 'total_num'\n" +
                "  from eb_mod_all_sales_goods\n" +
                "  WHERE category_id='"+category_id+"' and  "+UPDATETIME1+" >='"+startTime+"' and  "+UPDATETIME1+" <='"+endTime+"'\n" +
                "  GROUP BY squdao");
    }



}
