package com.szl.stronguion.controller.pagecontent;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.szl.stronguion.model.baseoperate.Page;
import com.szl.stronguion.service.baseoperate.PageServ;
import com.szl.stronguion.service.pagecontent.AccessPathServ;
import com.szl.stronguion.utils.FormatUtils;
import com.szl.stronguion.utils.JsonHelp;

import java.util.List;
import java.util.Map;

/**
 * Created by Tyfunwang on 2015/7/20.
 * 用户访问路径分析*
 * dateType: 1:昨天，2：最近3天，3：最近7天，4：最近一个月，5：最近一季度*
 */
public class AccessPathController extends Controller {
    private PageServ pageServ = new PageServ();
    private AccessPathServ pathServ = new AccessPathServ();

    //页面名称搜索（页面描述）
    public void searchByPageName() {
        String name = getPara("pageName");
        try {
            List<Page> list = pageServ.searchByPageName(name);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            JsonHelp.buildFailed();
        }
    }

    /**
     * 用户访问路径分析*
     */
    public void accessPath() {
        String pageName = getPara("pageName");
        int dateType = getParaToInt("dateType");//dateType=0 ：客户选择时间段
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        
        try {
            Map<String, Object> list = pathServ.getResultData(time[0], time[1], pageName);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            JsonHelp.buildFailed(e.getMessage());
        }
    }
}
