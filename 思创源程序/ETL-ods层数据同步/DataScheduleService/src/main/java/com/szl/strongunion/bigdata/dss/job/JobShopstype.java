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

public class JobShopstype implements Job {
    public static String CRON = "0 05 2 * * ?";
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String str1="id,name,pid,path,picname,picname_old,sort,state,logo,type,is_index,url,is_login,exist";
        TableMode tableMode=new TableMode("shopstype","sl_ods_shopstype"," ","0",str1);
        tableMode.source_to_targert(true);
    }
}
