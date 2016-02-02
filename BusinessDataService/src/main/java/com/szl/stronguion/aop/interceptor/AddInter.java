package com.szl.stronguion.aop.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.szl.stronguion.controller.menus.AccountController;
import com.szl.stronguion.model.menus.Account;

/**
 * Created by 小龙
 * on 15-10-16
 * at 上午9:51.
 */
public class AddInter implements Interceptor {
    @Override
    public void intercept(ActionInvocation ai) {
        Account account = new Account();
        account.set("name", "admin2");
        account.set("pass", "admin2");
        account.set("id", 2);
        account.set("level", 1);
        account.set("category_name", "白酒");
        account.set("category_id", 1);
        ai.getController().setSessionAttr(AccountController.ACCOUNTS, account);
        ai.invoke();
    }
}
