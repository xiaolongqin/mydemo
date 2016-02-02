package com.szl.stronguion.controller.menus;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.szl.stronguion.service.menus.ModuleServ;
import com.szl.stronguion.service.menus.NoticeServ;
import com.szl.stronguion.utils.JsonHelp;

/**
 * Created by Tyfunwang on 2015/6/29.
 */
public class RightMenusController extends Controller {
    private NoticeServ noticeServ = new NoticeServ();
    private ModuleServ moduleServ = new ModuleServ();

    //get notice
    public void getNotice() {
        try {
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(noticeServ.getLastedNotice())));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }
    //get report order
    public void getReportOrder() {
        try {
            //get pages order
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(moduleServ.getPagesOrder())));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());

        }
    }

}
