package com.szl.stronguion.aop.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.szl.stronguion.model.menus.Account;

/**
 * Created by Tyfunwang on 2015/6/4.
 */
public class AddInter implements Interceptor {
    @Override
    public void intercept(ActionInvocation ai) {
        Account account = new Account();
        account.set("id",2L);
        account.set("name", "admin2");
        account.set("pass", "admin2");
        account.set("role_id", "0");
        ai.getController().setSessionAttr("accounts", account);
        ai.invoke();
    }
}
