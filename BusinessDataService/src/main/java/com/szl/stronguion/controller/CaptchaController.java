package com.szl.stronguion.controller;

import com.jfinal.core.Controller;
import com.jfinal.ext.render.CaptchaRender;
import com.jfinal.kit.JsonKit;
import com.szl.stronguion.service.menus.AccountServ;
import com.szl.stronguion.utils.JsonHelp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liweiqi on 2014/10/27.
 * 前段从这里请求验证码
 */
public class CaptchaController extends Controller {
    public static final String ERROR_KEY = "error_msg";
    public static final String CAPTCHA_KEY = "captcha";


    private AccountServ accountServ = new AccountServ();
    public static final String ACCOUNTS = "accounts";


    public void index(){
        redirect("/view/html/index.html");
    }
    public void get() {
        //生成随机的验证码，并保存到当前session
        String caKey = getPara(CAPTCHA_KEY,CAPTCHA_KEY);
        CaptchaRender render = new CaptchaRender(caKey);
        render(render);
    }

    public void sure() {
        String input = getPara("input").toUpperCase();
        String caKey = getPara("captcha",CAPTCHA_KEY);
        if (!CaptchaRender.validate(this, input, caKey)) {
            renderJson(JsonHelp.buildFailed(" 验证码错误"));
        }else {
            renderJson(JsonHelp.buildSuccess(" 验证码正确"));
        }
    }


    public void test1() {
            renderJson(JsonHelp.buildSuccess());
    }


    public void login() {
        String name = getPara("name","admin2");
        String pass = getPara("pass","admin2");
        setSessionAttr("name1", name);


        Map map=new HashMap();
                map.put("id",getSession().getId());
        System.out.println("Sessionid1=" + getSession().getId());
                renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
//        renderJson(JsonHelp.buildSuccess());

//        try {
//            Account account = accountServ.login(name, pass);
//            if (account != null) {
//                setSessionAttr(ACCOUNTS, account);
//                setCookie("uid", account.getStr("uid"), 30000000);
//                Account account1 = getSessionAttr(ACCOUNTS);
//                Map map=new HashMap();
//                map.put("uid",account.get("id"));
//
//                renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
//            } else {
//                renderJson(JsonHelp.buildFailed());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            renderJson(JsonHelp.buildFailed());
//        }
    }


    public void getAccountMe() {
//        String sessionid=getPara("sessionid");
//        HttpSession mysession=SessionContext.getSession(sessionid);
//        System.out.println("mysession=" + mysession.getId());


        String  name=getSessionAttr("name1");
//        System.out.println("Sessionid2=" + getSession().getId());
        renderJson(JsonHelp.buildSuccess(JsonKit.toJson(name)));
//        Account account = getSessionAttr(ACCOUNTS);
////
//        int uid11 = account.getLong(Account.ID).intValue();
//        int uid =getParaToInt("uid");
////        if (nedi == null || "".equals(nedi)) {
////            renderJson(JsonHelp.buildFailed("请登录."));
////        } else {
//        try {
//            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(accountServ.getAccountById(uid))));
//        } catch (Exception e) {
//            e.printStackTrace();
//            renderJson(JsonHelp.buildFailed());
//        }
////        }
    }





}
