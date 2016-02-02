package com.szl.strongunion.bigdata.dss.job;

import com.szl.strongunion.bigdata.dss.model.TableMode;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by 小龙
 * on 15-8-3
 * at 上午11:13.
 */

public class JobCretePageRecord implements Job {

    //每个月最后一天00：00
    public static String CRON = "0 0 1 L * ?";
//    public static String CRON = "0 50 14 * * ?";
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String TABLNEME = "sl_ods_page_visit_record";
        String TABLESCHEMA = "strongunion_online";
        String str1="id,uid,shopsid,addtime";
        TableMode tableMode=new TableMode("attention","sl_ods_attention","addtime","1",str1);
        tableMode.createTable(TABLNEME,TABLESCHEMA,1);
    }
}
