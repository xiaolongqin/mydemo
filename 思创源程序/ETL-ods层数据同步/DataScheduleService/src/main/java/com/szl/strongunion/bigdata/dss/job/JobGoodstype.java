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

public class JobGoodstype implements Job {
    public static String CRON = "0 05 2 * * ?";
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String str1="id,shopsid,name,pid,path";
        TableMode t1=new TableMode("goodstype","sl_ods_goodstype","addtime","0",str1);
        t1.source_to_targert(true);
    }
}
