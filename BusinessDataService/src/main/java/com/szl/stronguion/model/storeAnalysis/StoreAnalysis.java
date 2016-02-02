package com.szl.stronguion.model.storeAnalysis;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * Created by 小龙
 * on 15-11-23
 * at 上午9:21.
 */

public class StoreAnalysis {

    public static final String MAIN2 = "main2";
    public static final int PAGESIZE=10;
    public static final String UPDATETIME = "DATE_FORMAT(updatetime,'%Y-%m-%d')";
    public static final String UPDATETIME1 = "updatetime";

    //店铺跟踪-》店铺整体扫描--------------------------------------------------------------
    //线上店铺销售排行--分页
    public List<Record> getStoreSaleTotal(int seqType,String category_id,String store_name,String[] channel,int pageNumber,int  saleType,String start_time,String end_time){
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

            return Db.use(MAIN2).paginate(pageNumber, PAGESIZE, "select store_url,store_id,store_name,category,squdao as 'channel',SUM(" + sale_attr + ") as 'total'," +
                    "sum(daynum) as 'total_num',round(sum(daysales),0) as 'total_sale',ROUND(avg(goods_sales_rank),0) as 'rank',sum(goods_sales_flag) as 'goods_sales_flag' \n" +
                    "  ", "from eb_mod_all_sales_goods WHERE store_name like '%"+store_name+"%' and" +
                    " category_id='" + category_id + "' and " + UPDATETIME1 + ">='" + start_time + "' and " + UPDATETIME1 + "<='" + end_time + "' " + sb + "\n" +
                    "     GROUP BY store_id ORDER BY total "+seq_attr+" ").getList();
    }

    //获取线上店铺销售排行总页数
    public long getStoreTotalPageSize(String category_id,String store_name,String[] channel,String start_time,String end_time){
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
        return Db.use(MAIN2).findFirst("SELECT COUNT(tt.store_id) as 'total' from \n" +
                " (select store_id\n" +
                "     from eb_mod_all_sales_goods WHERE store_name like '%"+store_name+"%' and" +
                " category_id='" + category_id + "' and " + UPDATETIME1 + ">='" + start_time + "' and " + UPDATETIME1 + "<='" + end_time + "' " + sb + "\n" +
                "     GROUP BY store_id limit 1000 )  tt").get("total");
    }

    /**
     * 获取导出数据
     * @param seqType
     * @param category_id
     * @param store_name
     * @param channel
     * @param saleType
     * @param start_time
     * @param end_time
     * @return
     */
    public List<Record> getExportStoreTotal(int seqType,String category_id,String store_name,String[] channel,int  saleType,String start_time,String end_time){
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

        return Db.use(MAIN2).find( "select store_url,store_id,store_name,category,squdao as 'channel',SUM(" + sale_attr + ") as 'total'," +
                "sum(daynum) as 'total_num',round(sum(daysales),0) as 'total_sale',ROUND(avg(goods_sales_rank),0) as 'rank',sum(goods_sales_flag) as 'goods_sales_flag' \n" +
                " from eb_mod_all_sales_goods WHERE store_name like '%"+store_name+"%' and" +
                " category_id='" + category_id + "' and " + UPDATETIME1 + ">='" + start_time + "' and " + UPDATETIME1 + "<='" + end_time + "' " + sb + "\n" +
                "     GROUP BY store_id ORDER BY total "+seq_attr+" ");
    }


    //获取时间段内所有渠道
    public List<Record> getAllChannelByTime(String start_time,String end_time){
        return Db.use(MAIN2).find("SELECT squdao as 'channel' from eb_mod_all_sales_goods \n" +
                " WHERE  "+UPDATETIME+">='"+start_time+"' and "+UPDATETIME+"<='"+end_time+"' GROUP BY squdao");
    }

    //获取所有渠道
    public List<Record> getAllChannel(){
        return Db.use(MAIN2).find("SELECT distinct squdao as 'channel' from eb_mod_all_sales_goods ");
    }

    //获取所有类目
    public List<Record> getAllCategory(){
        return Db.use(MAIN2).find("SELECT distinct category  from eb_mod_all_sales_goods ");
    }

    //线上电商店铺销售排行top10
    public List<Record> getStoreTopBychannel(String category_id,String channel,int  saleType,String start_time,String end_time){
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        StringBuilder sb =new StringBuilder(" ");
        if (!channel.equals("全部")){
            sb.append(" and squdao='"+channel+"' ");
        }
        return Db.use(MAIN2).find("select store_id,store_name,category,squdao as 'channel',round(SUM(" + sale_attr + ")/10000,2) as 'total'\n" +
                "     from eb_mod_all_sales_goods WHERE category_id='" + category_id + "' AND " + UPDATETIME1 + ">='" + start_time + "' and " + UPDATETIME1 + "<='" + end_time + "' "+sb+" \n" +
                "     GROUP BY store_id ORDER BY total DESC LIMIT 10");
    }


//    //线上细分行业店铺排行top10
//    public List<Record> getStoreTopByCategory(String category,int  saleType,String start_time,String end_time){
//        String sale_attr="daysales";
//        if (saleType==2){
//            sale_attr="daynum";
//        }
//
//
//        return Db.use(MAIN2).find("select store_id,store_name,category,squdao as 'channel',round(SUM("+sale_attr+")/10000,2) as 'total'\n" +
//                "     from eb_mod_all_sales_goods WHERE category='"+category+"' AND "+UPDATETIME+">='"+start_time+"' and "+UPDATETIME+"<='"+end_time+"' \n" +
//                "     GROUP BY store_id ORDER BY total DESC LIMIT 10");
//    }


    //店铺跟踪-》店铺详情---------------------------------------------------------------
    //店铺销售情况 平均贡献值:SUM(daysales)/SUM(daynum) as 'avg_price' (人数爬不到)
    public List<Record> getStoreDetaiInfo(String category_id,String storeId,int  saleType,String start_time,String end_time){
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        return Db.use(MAIN2).find("select "+UPDATETIME+" as 'day',store_name,round(SUM("+sale_attr+")/10000,2) as 'total'," +
                "(CASE WHEN SUM(daynum)=0 THEN 0 ELSE SUM(daysales)/SUM(daynum) end) as 'avg_price'\n" +
                "     from eb_mod_all_sales_goods WHERE category_id='"+category_id+"' and store_id='"+storeId+"' AND "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"' \n" +
                "     GROUP BY "+UPDATETIME+"");
    }


    //店铺产品销售排行(品牌) type:1--品牌  2--产品

    public List<Record> getStoreGoodsAndBrandInfo(int type,String storeId,int  saleType,String start_time,String end_time){
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        String type_attr="goodsbrand";
        if (type==2){
            type_attr="goodsid";
        }
        return Db.use(MAIN2).find("select "+type_attr+",goodsname,store_name,round(SUM("+sale_attr+")/10000,2) as 'total'\n" +
                "     from eb_mod_all_sales_goods WHERE store_id='"+storeId+"' AND "+UPDATETIME+">='"+start_time+"' and "+UPDATETIME+"<='"+end_time+"' \n" +
                "     GROUP BY "+type_attr+" order by total desc");
    }

    //店铺评价情况
    public List<Record> getStoreComment(String storeId,String start_time,String end_time){
        return Db.use(MAIN2).find("select store_name,ROUND(AVG(store_score),2) as 'store_score',round(AVG(store_industry_score),2) as 'store_industry_score',\n" +
                " round(avg(store_delivery_score),2) as 'store_delivery_score'\n" +
                " from eb_mod_all_sales_goods WHERE  store_id='"+storeId+"' AND "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"' ");
    }

    //店铺跟踪-》店铺对比--------------------------------------------------------------
    //线上店铺对比

    public List<Record> getStoreTop5(String category_id,String start_time,String end_time){
        return Db.use(MAIN2).find("SELECT  '"+start_time+"至"+end_time+"' as 'time',store_id,store_name,AVG(store_score) as 'store_score'\n" +
                "  FROM eb_mod_all_sales_goods WHERE category_id='"+category_id+"' and "+UPDATETIME1+">='"+start_time+"'  AND "+UPDATETIME1+"<='"+end_time+"'\n" +
                "  GROUP BY store_id ORDER BY  AVG(store_score) DESC LIMIT 5");
    }

    //线上店铺搜索
    public List<Record> getStroreByName(String category_id,int pageNumber,String storeName,String start_time,String end_time){
        return Db.use(MAIN2).paginate(pageNumber,PAGESIZE,"SELECT  store_id,store_name,round(AVG(store_score),2) as 'store_score'," +
                "ROUND(AVG(store_industry_score ),2) as 'store_industry_score'","FROM eb_mod_all_sales_goods WHERE category_id='"+category_id+"' and store_name like '%"+storeName+"%' and " +
                " "+UPDATETIME1+">='"+start_time+"'  AND "+UPDATETIME1+"<='"+end_time+"'\n" +
                "  GROUP BY store_id ORDER BY  AVG(store_score) DESC ").getList();
    }
    //线上店铺搜索总页数
    public long getStorePageSize(String category_id,String storeName,String start_time,String end_time){
        return Db.use(MAIN2).findFirst("SELECT COUNT(tt.store_id) as 'total_page'  FROM (SELECT  store_id,store_name FROM eb_mod_all_sales_goods " +
                "WHERE  store_name like '%"+storeName+"%' and category_id='"+category_id+"' and \n" +
                "  "+UPDATETIME1+">='"+start_time+"'  AND "+UPDATETIME1+"<='"+end_time+"'\n" +
                "  GROUP BY store_id limit 1000  ) tt").getLong("total_page");
    }

     //店铺跟踪-》店铺对比二层页面--------------------------------------------------------------
      //线上店铺销售对比


    public List<Record> getStoreSaleduibi(int  saleType,String storeIdList[],String start_time,String end_time){
        String sale_attr="daysales";
        if (saleType==2){
            sale_attr="daynum";
        }
        StringBuilder sb =new StringBuilder(" ");
        if (storeIdList!=null&&storeIdList.length!=0){
            sb.append(" and (store_id=");
            for (int i=0;i<storeIdList.length;i++){
                if (i<storeIdList.length-1){
                    sb.append("'"+storeIdList[i]+"'").append(" or store_id= ");
                }else {
                    sb.append("'"+storeIdList[i]+"'").append(") ");
                }
            }
        }
        return Db.use(MAIN2).find("SELECT DATE_FORMAT(updatetime,'%Y-%m-%d') as 'day',store_id,store_name,round(SUM("+sale_attr+")/10000,2) as 'total' \n" +
                "  FROM  eb_mod_all_sales_goods WHERE \n" +
                "   "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"' "+sb+"\n" +
                "  GROUP BY DATE_FORMAT(updatetime,'%Y-%m-%d'),store_id");
    }

    //店铺各项参数对比
    public List<Record> getStoreAttrduibi(String storeIdList[],String start_time,String end_time){

        StringBuilder sb =new StringBuilder(" ");
        if (storeIdList!=null&&storeIdList.length!=0){
            sb.append(" and (store_id=");
            for (int i=0;i<storeIdList.length;i++){
                if (i<storeIdList.length-1){
                    sb.append("'"+storeIdList[i]+"'").append(" or store_id= ");
                }else {
                    sb.append("'"+storeIdList[i]+"'").append(") ");
                }
            }
        }
        return Db.use(MAIN2).find("SELECT store_id,store_name,round(AVG(store_score),2) as 'store_score',round(AVG(goods_score),2) as 'goods_score'," +
                "round(AVG(store_delivery_score),2) as 'store_delivery_score',round(AVG(store_industry_score),2) as 'store_industry_score',round(AVG(goods_mod_comment1),2) as 'goods_mod_comment1'," +
                "round(AVG(goods_mod_comment2),2) as 'goods_mod_comment2',round(AVG(goods_mod_comment3),2) as 'goods_mod_comment3' \n" +
                "  FROM  eb_mod_all_sales_goods WHERE  \n" +
                "   "+UPDATETIME1+">='"+start_time+"' and "+UPDATETIME1+"<='"+end_time+"' "+sb+"\n" +
                "  GROUP BY store_id");
    }




}
