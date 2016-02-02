package com.szl.strongunion.bigdata.dss.job;

import com.szl.strongunion.bigdata.dss.model.TableMode;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by 小龙
 * on 15-7-10
 * at 上午11:07.
 */

public class JobAttention implements Job {
    public static String CRON = "0 05 2 * * ?";
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String str1="id,uid,shopsid,addtime";
        TableMode tableMode=new TableMode("attention","sl_ods_attention","addtime","1",str1);
        tableMode.source_to_targert(true);
    }
}
