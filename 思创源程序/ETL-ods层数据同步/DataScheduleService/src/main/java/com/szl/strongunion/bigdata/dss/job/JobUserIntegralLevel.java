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

public class JobUserIntegralLevel implements Job {
    public static String CRON = "0 05 2 * * ?";
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String str1="id,levelname,leveltype,level_value,level_add_max_value,level_cut_max_value,level_img,`desc`,state";
        TableMode tableMode=new TableMode("user_integral_level","sl_ods_user_integral_level"," ","0",str1);
        tableMode.source_to_targert(true);
    }
}
