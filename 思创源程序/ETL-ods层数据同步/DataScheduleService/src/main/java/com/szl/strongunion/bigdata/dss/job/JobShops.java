package com.szl.strongunion.bigdata.dss.job;

import com.szl.strongunion.bigdata.dss.model.TableMode;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by 小龙
 * on 15-7-10
 * at 上午11:04.
 */

public class JobShops implements Job {
    public static String CRON = "0 05 2 * * ?";
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String str1="id,typeid,childtypeid,username,pass,name,identity_number,address,code,phone,tip,answer,logo,license,slogan,content,shopsname," +
                "is_operate,vip,sort,homeshop,homesort,email,attnum,connum,level,distribution,starttime,endtime,state,addtime,longitude,latitude," +
                "recode,rid,contactway,pd_img,exist,MercCode,buynum,fixphone,BoundNo,accountstate,AcNo,onlinetime,business_time,is_business,claim," +
                "is_goods,is_send,is_goods_payment,send_distance,sendprice,freight_price,AcNotype";
        TableMode tableMode=new TableMode("shops","sl_ods_shops","addtime","1",str1);
        tableMode.source_to_targert(true);
    }
}
