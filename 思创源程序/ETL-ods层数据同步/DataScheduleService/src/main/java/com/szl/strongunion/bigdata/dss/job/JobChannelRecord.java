package com.szl.strongunion.bigdata.dss.job;

import com.szl.strongunion.bigdata.dss.model.TableMode;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by 小龙
 * on 15-7-10
 * at 上午10:13.
 */

public class JobChannelRecord implements Job {
    public static String CRON = "0 05 2 * * ?";
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String str1="id,channel_apk_id,imei,ip_adress,downloadstart_time,downloadend_time,download_url,addtime";
        TableMode t1=new TableMode("channel_record","sl_ods_channel_record","downloadstart_time","1",str1);
        t1.source_to_targert(true);
    }
}
