package com.szl.strongunion.bigdata.dss.job;

import com.szl.strongunion.bigdata.dss.model.TableMode;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by 小龙
 * on 15-7-10
 * at 上午11:01.
 */

public class JobOrder implements Job {
    public static String CRON = "0 05 2 * * ?";
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String str1="id,ordernum,shopsid,uid,linkman,address,code,phone,total,goods_total_price,freight_total_price,coupon_total_price," +
                "user_coupon_ids,state,distri,payment,paystate,astocktime,sstocktime,bstocktime,aservicetime,sservicetime,bservicetime," +
                "receive_time,pay_overtime,courierid,content,addtime,suretime,gettime,overtime,revoketime,imeiid,revoke_type,revoke_reason,auth_state,longitude," +
                "latitude,province,city,district,street,current_address,disnum,goodsnum,hname,hphone,is_dis";
        TableMode t1=new TableMode("`order`","sl_ods_order","addtime","7",str1);
        t1.source_to_targert(false);
    }
}
