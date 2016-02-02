package com.szl.stronguion.service.baseoperate;

import com.alibaba.fastjson.JSONArray;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.baseoperate.Page;
import com.szl.stronguion.model.baseoperate.PageAction;
import com.szl.stronguion.model.menus.Roles;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Tyfunwang on 2015/6/26.
 */
public class PageServ {
    private Page page = new Page();
    private PageAction pageAction = new PageAction();

    public boolean checkPageId(String id) {
        return page.checkPageId(id).isEmpty();
    }

    //add page and add page_actions
    public boolean addPage(final String pageId, final String pageUrl, final String pageType,
                           final String pageState, final String pageName, final String pageVersion, final String json) {
        return Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                //save page 
                boolean flag = page.addPage(pageId, pageUrl, pageType, pageState, pageName, pageVersion);
                if (flag) {
                    //save pageActions
                    for (Object o : JSONArray.parseArray(json)) {
                        Map<String, Object> map = (Map<String, Object>) o;
                        String pageActionId = String.valueOf(map.get("actionId"));
                        String pageActionName = String.valueOf(map.get("actionName"));
                        if (pageAction.addPageAction(pageId, pageActionId, pageActionName)) continue;
                        return false;
                    }
                    return true;
                }
                return false;
            }
        });
    }

    //update Page
    public boolean updatePage(final String pageId, final String pageUrl, final String pageType,
                              final String pageState, final String pageName, final String pageVersion, final String json) {
        return Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                //save page
                int i = page.updatePageById(pageId, pageUrl, pageType, pageState, pageName, pageVersion);
                if (i == 1) {
                    //delete pageActions 
                      if (pageAction.deletePageActions(pageId) > 0){
                          //save pageActions
                          for (Object o : JSONArray.parseArray(json)) {
                              Map<String, Object> map = (Map<String, Object>) o;
                              String pageActionId = String.valueOf(map.get("actionId"));
                              String pageActionName = String.valueOf(map.get("actionName"));
                              if (pageAction.addPageAction(pageId, pageActionId, pageActionName)) continue;
                              return false;
                          }
                          return true;
                      }
                    return true;
                }
                return false;
            }
        });

    }

    //search page by type and name
    public List<Record> searchPage(String type, String name,int pageNumber) {
        if(name == null){
            long totol=page.getTotalPageByType(type);
            List<Record> list1=page.searchPageByType(type, pageNumber);
            if (list1.size()>0){
                list1.get(0).set("total",Math.ceil(totol/(Page.pageSize*1.0)));
            }
            return list1;
        }

        long t2=page.getTotalPage(type,name);
        List<Record> list2=page.searchPage(type,name,pageNumber);
        if (list2.size()>0){
            list2.get(0).set("total",Math.ceil(t2/(Roles.pageSize*1.0)));
        }
        return list2;
    }

    //changepage state by page_id
    public boolean changePage(String id, String state) {
        return page.changePage(id, state);
    }

    public List<Page> getPage(String id) {
        return page.getPage(id);
    }

    //模糊搜索 by page name
    public List<Page> searchByPageName(String name) {
        return page.searchByPageName(name);
    }
}
