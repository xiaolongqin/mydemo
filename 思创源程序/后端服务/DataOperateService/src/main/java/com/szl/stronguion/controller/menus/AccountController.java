package com.szl.stronguion.controller.menus;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.szl.stronguion.aop.interceptor.LoginInterceptor;
import com.szl.stronguion.model.menus.Account;
import com.szl.stronguion.service.menus.AccountServ;
import com.szl.stronguion.service.menus.RolesServ;
import com.szl.stronguion.utils.JsonHelp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tyfunwang on 2015/6/23.
 */
public class AccountController extends Controller {
    private AccountServ accountServ = new AccountServ();
    public static final String ACCOUNTS = "accounts";
    private RolesServ rolesServ = new RolesServ();

    public void login() {
        String name = getPara("name");
        String pass = getPara("pass");

        try {
            Account account = accountServ.login(name, pass);
            if (account != null) {
                setSessionAttr(ACCOUNTS, account);
                renderJson(JsonHelp.buildSuccess());
            } else {
                renderJson(JsonHelp.buildFailed());
            }
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    public void logout() {
        try {
            removeSessionAttr(ACCOUNTS);
            renderJson(JsonHelp.buildSuccess());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    //admin add a new account
    public void addAccount() {
        String name = getPara("name");
        String pass = getPara("pass", "123456");
        int role_id = getParaToInt("role_id");
        if (accountServ.testAccount(name)){
            renderJson(JsonHelp.buildFailed("此用户名已经存在,请重新输入!"));
            return;
        }
            try {
            renderJson(accountServ.addAccount(name, pass, role_id) ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    //admin delete a old account
    public void deleteAccount() {
        int uid = getParaToInt("uid");
        try {
            renderJson(accountServ.deleteAccount(uid) ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    //search a account by name
    public void searchAccount() {
        String name = getPara("name");
        int pageNumber=getParaToInt("pageNumber",1);
        try {
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(accountServ.searchAccount(pageNumber,name))));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }
    //search a account by uid
    public void getAccountById() {
        int uid = getParaToInt("uid");
        try {
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(accountServ.getAccountById(uid))));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }
    //edit account and role
    public void modifyAccount() {
        int uid = getParaToInt("uid");
        String name = getPara("name");
        int role_id = getParaToInt("role_id");
        try {
            renderJson(accountServ.modifyAccount(uid, name, role_id) ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }

    }

    //get account info
    @Before(LoginInterceptor.class)
    public void getAccount() {
        try {
            Account account = getSessionAttr(ACCOUNTS);
            Map<String, Object> map = new HashMap<String, Object>();
            if (account == null) {
                //session失效，应该带到登录界面
                renderJson(JsonHelp.buildFailed(JsonKit.toJson(map)));
                return;
            }
            Account user = new Account();
            user.set(Account.NAME, account.getStr(Account.NAME));
            map.put("account", user);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    public void updatePwd() {
        String oldPwd = getPara("oldPwd");
        String newPwd = getPara("newPwd");
        String confirmPwd = getPara("confirmPwd");

        Account account = getSessionAttr(ACCOUNTS);
        int uid = account.getLong(Account.ID).intValue();

        try {
            boolean flag = accountServ.updatePwd(uid, oldPwd, newPwd, confirmPwd);
            if (flag) {
                removeSessionAttr(ACCOUNTS);
                renderJson(JsonHelp.buildSuccess());
            } else {
                renderJson(JsonHelp.buildFailed());
            }
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }

    }
}
