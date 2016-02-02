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

public class JobMobileDevice implements Job {
    public static String CRON = "0 05 2 * * ?";
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String str1="id,app_type,type,imei,model_name,model,system_version,kernel_version,processor,phone,ip,longitude," +
                "latitude,province,city,district,street,network_type,screen_resolution,memory,channel_no,addtime";
        TableMode tableMode=new TableMode("mobile_device","sl_ods_mobile_device","addtime","1",str1);
        tableMode.source_to_targert(true);
    }
}
