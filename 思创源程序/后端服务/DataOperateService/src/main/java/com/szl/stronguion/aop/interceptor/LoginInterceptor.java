package com.szl.stronguion.aop.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.szl.stronguion.controller.menus.AccountController;
import com.szl.stronguion.model.menus.Account;
import com.szl.stronguion.utils.JsonHelp;

/**
 * Created by Tyfunwang on 2015/7/9.
 */
public class LoginInterceptor implements Interceptor {

    @Override
    public void intercept(ActionInvocation ai) {
        Account account = ai.getController().getSessionAttr(AccountController.ACCOUNTS);
        if (account == null) {
            //没有登录，带到登录页面
            ai.getController().renderJson(JsonHelp.buildFailed());
        } else {
            ai.invoke();
        }
    }
}
