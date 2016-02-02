package com.szl.stronguion.controller.menus;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.menus.FeedBack;
import com.szl.stronguion.service.menus.FeedServ;
import com.szl.stronguion.utils.JsonHelp;

import java.util.List;

/**
 * Created by  on 2015/8/20.
 */
public class FeedController extends Controller {
    private FeedServ feedServ = new FeedServ();

    /**
     * 管理公告*
     */
    public void getAllFeedBack() {
        try {
            List<FeedBack> list = feedServ.getAllFeedBack();
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }


    public void getFeedBackByID() {
        long x = getParaToLong("id");
        try {
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(feedServ.getFeedBackByID(x))));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    public void addFeedBack() {
        String title = getPara("title");
        String content = getPara("content");
        String contact = getPara("contact");
        long feedback_time = getParaToLong("feedback_time");
        try {
            renderJson(feedServ.addFeedBack(title, content, contact, feedback_time) ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    public void modifyFeedBack() {
        int id = getParaToInt("id");
        String title = getPara("title");
        String content = getPara("content");
        String contact = getPara("contact");
        try {
            renderJson(feedServ.modifyFeedBack(id, title, content, contact) ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    public void searchFeedBack() {
        String content = getPara("content", "");
        int pageNumber=getParaToInt("pageNumber",1);
        try {
            List<Record> list = feedServ.searchFeedBack(content, pageNumber);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    public void deleteFeedBack() {
        int id = getParaToInt("id");
        try {

            renderJson(feedServ.deleteFeedBack(id) ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }
}
