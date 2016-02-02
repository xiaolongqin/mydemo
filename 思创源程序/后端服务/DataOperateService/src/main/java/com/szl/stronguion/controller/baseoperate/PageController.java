package com.szl.stronguion.controller.baseoperate;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.baseoperate.Page;
import com.szl.stronguion.service.baseoperate.PageServ;
import com.szl.stronguion.utils.JsonHelp;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/6/26.
 */
public class PageController extends Controller {
    private PageServ pageServ = new PageServ();

    /**
     * 为用户访问页面分析提供模糊搜索服务 *
     */
    public void searchByPageName(){
        String name = getPara();
        try {
            List<Page> list = pageServ.searchByPageName(name);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            JsonHelp.buildFailed(e.getMessage());
        }
    }
  

    //check page_id
    public void checkPageId() {
        String id = getPara(Page.PAGEID);
        try {
            renderJson(pageServ.checkPageId(id) ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (Exception e) {
            JsonHelp.buildFailed(e.getMessage());
        }
    }

    /**
     * *   添加页面 和 添加按钮
     * *  page_type: 1-用户端 2-商户端 3-管家端
     * *  page_state: 1-上架 0-下架 默认为:1
     */
    public void addPage() {

        String pageId = getPara(Page.PAGEID);
        String pageUrl = getPara(Page.PAGEURL);
        String pageType= getPara(Page.PAGETYPE);
        String pageState = getPara(Page.PAGESTATE,"1");
        String pageName = getPara(Page.PAGENAME);
        String pageVersion = getPara(Page.PAGEVERSION);
        String json = getPara("json");
//        String pageId = "201";
//        String pageUrl = "http://www.index.com/menus/";
//        String pageType = "2";
//        String pageState = "1";
//        String pageName = "菜单";
//        String pageVersion = "1.0.0";
//        String json = "[\n" +
//                "    {\n" +
//                "        \"actionId\": \"021\",\n" +
//                "        \"actionName\": \"购物\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "        \"actionId\": \"022\",\n" +
//                "        \"actionName\": \"注销\"\n" +
//                "    }\n" +
//                "]";
     
        try {
            boolean flag = pageServ.addPage(pageId, pageUrl, pageType, pageState, pageName, pageVersion, json);
            renderJson(flag ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (Exception e) {
            JsonHelp.buildFailed(e.getMessage());
        }
    }

    /**
     * 页面内容 和按钮修改  *
     */
    //getPage for edit page
    public void getPage() {
        String id = getPara(Page.PAGEID);
        try {
            List<Page> list = pageServ.getPage(id);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            JsonHelp.buildFailed(e.getMessage());
        }
    }
    public void editPage() {
        String pageId = getPara(Page.PAGEID);
        String pageUrl = getPara(Page.PAGEURL);
        String pageType= getPara(Page.PAGETYPE);
        String pageState = getPara(Page.PAGESTATE,"1");
        String pageName = getPara(Page.PAGENAME);
        String pageVersion = getPara(Page.PAGEVERSION);
        String json = getPara("json");
//        String pageId = "201";
//        String pageUrl = "http://www.index.com/menus/";
//        String pageType = "2";
//        String pageState = "1";
//        String pageName = "菜单";
//        String pageVersion = "1.0.0";
//        String json = "[\n" +
//                "    {\n" +
//                "        \"actionId\": \"021\",\n" +
//                "        \"actionName\": \"购物2\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "        \"actionId\": \"022\",\n" +
//                "        \"actionName\": \"注销2\"\n" +
//                "    }\n" +
//                "]";
       
        try {
            boolean flag = pageServ.updatePage(pageId, pageUrl, pageType, pageState, pageName, pageVersion, json);
            renderJson(flag ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (Exception e) {
            JsonHelp.buildFailed(e.getMessage());
        }
    }

    /**
     * * 页面状态修改 和 搜索
     * *
     */
    //change the page state
    public void changePage() {
        String id = getPara(Page.PAGEID);
        String state = getPara(Page.PAGESTATE);
        try {
            boolean flag = pageServ.changePage(id, state);
            renderJson(flag ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (Exception e) {
            JsonHelp.buildFailed(e.getMessage());
        }
    }
    //search
    public void searchPage() {
        int pageNumber=getParaToInt("pageNumber",1);
        String type = getPara(Page.PAGETYPE);
        String param = getPara("param",null);
        try {
            List<Record> list = pageServ.searchPage(type,param,pageNumber);
            renderJson(!list.isEmpty() ? JsonHelp.buildSuccess(JsonKit.toJson(list)) : JsonHelp.buildFailed());
        } catch (Exception e) {
            e.printStackTrace();
            JsonHelp.buildFailed(e.getMessage());

        }
    }

}
