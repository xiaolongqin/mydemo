package com.szl.stronguion.controller.menus;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.menus.Notice;
import com.szl.stronguion.service.menus.NoticeServ;
import com.szl.stronguion.utils.JsonHelp;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/8/20.
 */
public class NoticeController extends Controller {
    private NoticeServ noticeServ = new NoticeServ();

    /**
     * 管理公告*
     */
    public void getAllNotices() {
        try {
            List<Notice> list = noticeServ.getAllNotices();
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    public void addNotice() {
        String title = getPara("title");
        String content = getPara("content");
        try {
            renderJson(noticeServ.addNotice(title, content) ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    public void modifyNotice() {
        int id = getParaToInt("id");
        String title = getPara("title");
        String content = getPara("content");
        try {
            renderJson(noticeServ.modifyNotice(id, title, content) ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    public void searchNotice() {
        String content = getPara("content", "");
        int pageNumber=getParaToInt("pageNumber",1);
        try {
            List<Record> list = noticeServ.searchNotice(content,pageNumber);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    public void deleteNotice() {
        int id = getParaToInt("id");
        try {

            renderJson(noticeServ.deleteNotice(id) ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }
}
