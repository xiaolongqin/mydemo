package com.szl.strongunion.bigdata.dss.job;

import com.szl.strongunion.bigdata.dss.model.TableMode;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by 小龙
 * on 15-7-10
 * at 上午11:06.
 */

public class JobUsers implements Job {
    public static String CRON = "0 05 2 * * ?";
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //birth,
        String str1="id,username,pass,name,nickname,avatar,sex,address,code,phone,email,points,pointslevel,pointstime," +
                "communityid,tip,answer,isonline,state,role_type,addtime,recode,rid,UserId,BoundNO,headimage,image_code," +
                "token,appkey,imei,longitude,latitude,province,city,district,street,user_type,channel_apk_id";
        TableMode tableMode=new TableMode("users","sl_ods_users","addtime","1",str1);
        tableMode.source_to_targert(true);
    }
}
