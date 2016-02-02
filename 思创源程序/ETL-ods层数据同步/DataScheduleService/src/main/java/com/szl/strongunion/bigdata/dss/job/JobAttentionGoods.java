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

public class JobAttentionGoods implements Job{
    public static String CRON = "0 05 2 * * ?";
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String str1="id,uid,shopsid,goodsid,addtime";
        TableMode tableMode=new TableMode("attention_goods","sl_ods_attention_goods","addtime","1",str1);
        tableMode.source_to_targert(true);

    }
}
