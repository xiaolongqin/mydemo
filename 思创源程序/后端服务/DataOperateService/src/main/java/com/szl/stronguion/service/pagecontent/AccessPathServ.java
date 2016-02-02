package com.szl.stronguion.service.pagecontent;

import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.pagecontent.Temp0VisitRoute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tyfunwang on 2015/7/20.
 * 用户访问路径分析*
 */
public class AccessPathServ {
   private Temp0VisitRoute temp0VisitRoute = new Temp0VisitRoute();
    public Map<String, Object> getResultData(String startTime, String endTime, String pageName) throws Exception{
        Map<String, Object> maps = new HashMap<String, Object>();

//        List<Record> recordList = temp0VisitRoute.getPageRelation(startTime, endTime, pageName);

        List<Record> parentList = temp0VisitRoute.getParentPage(pageName,startTime,endTime);
        List<Record> pageList = temp0VisitRoute.getPage(pageName,startTime,endTime);
        List<Record> subPageList = temp0VisitRoute.getSubPage(pageName,startTime,endTime);
        maps.put("parent",parentList);
        maps.put("page",pageList);
        maps.put("subPageList",subPageList);
        return maps;
    }
}
