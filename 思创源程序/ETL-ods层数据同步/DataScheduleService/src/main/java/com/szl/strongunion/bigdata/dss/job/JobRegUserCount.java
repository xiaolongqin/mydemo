package com.szl.strongunion.bigdata.dss.job;

import com.szl.strongunion.bigdata.dss.model.TableMode;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by 小龙
 * on 15-7-10
 * at 上午11:00.
 */

public class JobRegUserCount implements Job {
    public static String CRON = "0 05 2 * * ?";
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String str1="id,channel_apk_id,reg_num,regtime,aim_num,arrive_num,addtime";
        TableMode t1=new TableMode("reg_user_count","sl_ods_reg_user_count","regtime","1",str1);
        t1.source_to_targert(true);
    }
}
