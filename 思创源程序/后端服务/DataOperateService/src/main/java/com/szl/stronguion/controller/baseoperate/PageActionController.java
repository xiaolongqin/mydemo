package com.szl.stronguion.controller.baseoperate;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.szl.stronguion.model.baseoperate.PageAction;
import com.szl.stronguion.service.baseoperate.PageActionServ;
import com.szl.stronguion.utils.JsonHelp;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/6/26.
 */
public class PageActionController extends Controller {
    private PageActionServ pageActionServ = new PageActionServ();


    //getPageAction for edit page
    public void getPageAction() {
        String id = getPara(PageAction.PAGEID);
        try {
            List<PageAction> list = pageActionServ.getPageAction(id);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            JsonHelp.buildFailed();
        }

    }

    //check page_action_id
    public void checkActionId() {
        String id = getPara(PageAction.PAGEACTIONID);
        try {
            renderJson(pageActionServ.checkActionId(id) ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (Exception e) {
            JsonHelp.buildFailed();
        }
    }

}
