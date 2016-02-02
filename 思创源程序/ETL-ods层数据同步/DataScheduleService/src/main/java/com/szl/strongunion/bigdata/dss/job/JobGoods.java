package com.szl.strongunion.bigdata.dss.job;

import com.szl.strongunion.bigdata.dss.model.TableMode;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by 小龙
 * on 15-7-10
 * at 上午11:02.
 */

public class JobGoods implements Job {
    public static String CRON = "0 05 2 * * ?";
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String str1="id,goodsnum,shopsid,typeid,goods,picname,marketprice,goodsprice,other,fullprice,minusprice,hits,buynum,num,unit,content," +
                "sale,isgroom,sort,state,endtime,revnum,level,addtime,groutime,edittime,attnum,exist,is_special";
        TableMode t1=new TableMode("goods","sl_ods_goods","addtime","1",str1);
        t1.source_to_targert(true);
    }
}
