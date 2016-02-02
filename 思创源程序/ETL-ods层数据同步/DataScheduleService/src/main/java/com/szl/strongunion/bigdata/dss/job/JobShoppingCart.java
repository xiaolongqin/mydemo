package com.szl.strongunion.bigdata.dss.job;

import com.szl.strongunion.bigdata.dss.model.TableMode;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by 小龙
 * on 15-7-30
 * at 下午2:06.
 */

public class JobShoppingCart implements Job{
    public static String CRON = "0 05 2 * * ?";
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String str1="id,uid,shopsid,goodsid,num,is_special,special_num,my_special_num,longitude,latitude,province,city," +
                "district,street,address,exist,is_over,orderid,addtime,update_time,delete_time,over_time";
        TableMode tableMode=new TableMode("shopping_cart","sl_ods_shopping_cart","addtime","1",str1);
        tableMode.source_to_targert(true);
    }
}
