package com.szl.strongunion.bigdata.dss.job;

import com.szl.strongunion.bigdata.dss.model.TableMode;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by 小龙
 * on 15-7-10
 * at 上午11:05.
 */

public class JobComshop implements Job {
    public static String CRON = "0 05 2 * * ?";
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String str1="id,shopsid,communityid,addtime";
        TableMode tableMode=new TableMode("comshop","sl_ods_comshop"," ","0",str1);
        tableMode.source_to_targert(true);
    }
}
