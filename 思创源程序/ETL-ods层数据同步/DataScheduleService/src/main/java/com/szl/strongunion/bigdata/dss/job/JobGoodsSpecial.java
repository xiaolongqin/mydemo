package com.szl.strongunion.bigdata.dss.job;

import com.szl.strongunion.bigdata.dss.model.TableMode;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by 小龙
 * on 15-7-30
 * at 下午2:05.
 */

public class JobGoodsSpecial implements Job{
    public static String CRON = "0 05 2 * * ?";
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String str1="id,goodsid,special_price,special_num,cycle,cycle_limit,start_time,end_time,addtime";
        TableMode tableMode=new TableMode("goods_special","sl_ods_goods_special","addtime","1",str1);
        tableMode.source_to_targert(true);

    }
}
