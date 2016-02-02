package com.szl.stronguion.service.baseoperate;

import com.szl.stronguion.model.baseoperate.PageAction;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/6/26.
 */
public class PageActionServ {
    
    private PageAction pageAction = new PageAction();

    // checkid : to avoid the id repeat
    public boolean checkActionId(String id) {
        return pageAction.checkActionId(id).isEmpty();
    }

    //getPageAction by actionId
    public List<PageAction> getPageAction(String id) {
        return pageAction.getPageAction(id);
    }
}
