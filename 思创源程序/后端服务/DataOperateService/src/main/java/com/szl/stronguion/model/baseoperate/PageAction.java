package com.szl.stronguion.model.baseoperate;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/6/26.
 */
public class PageAction extends Model<PageAction> {
    public static final String PAGEACTIONID = "page_action_id";
    public static final String PAGEID = "page_id";//int
    public static final String PAGEACTIONNAME = "page_action_name";//按钮描述
    private static PageAction dao = new PageAction();

    //
    public List<PageAction> checkActionId(String id) {
        return dao.find("select * from sl_ods_page_action where page_action_id = '" + id + "';");
    }

    //update page
    public int updatePageActionById(String actionId, String actionName) {
        return Db.use("main2").update("update sl_ods_page_action set page_action_name= '" + actionName + "' " +
                "where page_action_id = '" + actionId + "';");
    }

    //delete page actions for update page
    public int deletePageActions(String pageid) {
        return Db.use("main2").update("delete from sl_ods_page_action where page_id = '" + pageid + "'");
    }

    //addPageAction for add page
    public boolean addPageAction(String pageId, String actionId, String actionName) {
        return new PageAction().set(PAGEACTIONID, actionId).set(PAGEID, pageId).set(PAGEACTIONNAME, actionName).save();
    }

    //getPageAction
    public List<PageAction> getPageAction(String id) {
        return dao.find("SELECT * from sl_ods_page_action where page_id = '" + id + "' ORDER BY page_action_id;");
    }
}
