package com.szl.stronguion.controller.pagecontent;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.service.pagecontent.KeyWordsSearchServ;
import com.szl.stronguion.utils.FormatUtils;
import com.szl.stronguion.utils.JsonHelp;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/7/20.*
 * 站内搜索关键词分析*
 * dateType: 1:昨天，2：最近3天，3：最近7天，4：最近一个月，5：最近一季度**
 */
public class KeyWordsController extends Controller{
    private KeyWordsSearchServ searchServ = new KeyWordsSearchServ();
    //站内搜索关键词分析
    public void getKeyWords(){
        int dateType = getParaToInt("dateType");//dateType=0 ：客户选择时间段
        String[] time = new String[2];
        if (dateType==0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        
        try {
            List<Record> list = searchServ.getKeyWords(time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            JsonHelp.buildFailed();
        }
        
    }
}
