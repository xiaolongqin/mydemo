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
public class LoginInterceptor implements Interceptor {

        @Override
    public void intercept(ActionInvocation ai) {
        Account account = ai.getController().getSessionAttr(AccountController.ACCOUNTS);
        if (account == null) {
            //没有登录，带到登录页面
//            ai.getController().renderJson(JsonHelp.buildFailed());
            ai.getController().redirect("/view/html/login.html");
        } else {
            ai.invoke();
        }
    }

}
