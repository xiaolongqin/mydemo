package controller;

import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import interceptor.ShiroInterceptor;
import model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.subject.Subject;
import plugin.shiro.annotation.ClearAuthHandler;
import plugin.shiro.annotation.RequiresRoles;
import service.ControllerService;
import service.UserService;
import util.EncAndDecByDES;

/**
 * Created by Tyfunwang on 2014/12/28.
 */

@Before(ShiroInterceptor.class)
@RequiresRoles(value = {"user","admin"},logical = Logical.OR)
public class ViewController extends Controller {
    UserService userService = new UserService();
    EncAndDecByDES encAndDecByDES = new EncAndDecByDES();
    ControllerService service = new ControllerService();

    @ClearInterceptor(ClearLayer.ALL)
    public void login() {
        if (getPara("logout") != null) {
            //通知子系统退出
            Subject currentUser = SecurityUtils.getSubject();
            currentUser.logout();
            setCookie(User.USER_COOKIENAME, null, 0);
            render("/view/html/login.html");
            return;
        }

        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated()) {
            try {
                User user = (User) currentUser.getSession().getAttribute(User.USER_SESSIONNAME);
                String emailFalse =  RdsController.REGISTER_VF + "?email=" + user.getStr(User.USER_EMAIL);
                String verifyFalse = RdsController.REGISTER_CK;
                String url = user.getInt(User.USER_STATUS) == 0 ? user.getInt(User.USER_AUTHENTICATION) == 0 ? emailFalse : verifyFalse : null;
                if (url != null) {
                    redirect(url);
                    return;
                }
            } catch (Exception ex) {
                render("/view/html/login.html");
            }

        }
        String url = getPara("url");
        if (url == null) {
            //普通登录请求
            mainLogin();
        } else {
            //子系统登录请求
            subLogin(url);
        }
    }

    /**
     * mainLogin
     */
    private void mainLogin() {
        Subject currentUser = SecurityUtils.getSubject();
        User user = (User) currentUser.getSession().getAttribute(User.USER_SESSIONNAME);
        String cookie = getCookie(User.USER_COOKIENAME);
        if (user != null) {
            if (currentUser.isAuthenticated()) {
                //登录用户信息已登录，重新设置cookie
                setCookie(User.USER_COOKIENAME, cookie, 60 * 60);
                redirect(RdsController.INDEX_URL);
                return;
            }
        } else if (cookie != null) {
            String userid = (encAndDecByDES.getDesString((cookie))).split("-")[0];
            User user2 = userService.getUserByUserId(userid);
            if (user2 == null) {
                render("/view/html/login.html");
                return;
            }
            currentUser.getSession().setAttribute(User.USER_SESSIONNAME, user2);
            //登录用户信息已登录，重新设置cookie,session
            setCookie(User.USER_COOKIENAME, cookie, 60 * 60);
            redirect(RdsController.INDEX_URL);
            return;
        }
        render("/view/html/login.html");
    }

    /**
     * subLogin
     */
    private void subLogin(String url) {
        if (url.startsWith("http")) {

            Subject currentUser = SecurityUtils.getSubject();
            User user = (User) currentUser.getSession().getAttribute(User.USER_SESSIONNAME);
            String cookie = getCookie(User.USER_COOKIENAME);
            if (user != null) {
                //已经登陆过
                if (currentUser.isAuthenticated()) {
                    //登录用户信息已认证，重新设置cookie
                    setCookie(User.USER_COOKIENAME, cookie, 60 * 60);
                    User user1 = service.formatUser(user);
                    user1.put("time", System.currentTimeMillis());

                    String userMi = encAndDecByDES.getEncString(JsonKit.toJson(user1));
                    redirect(url + "/?" + RdsController.RDS_PARAM + "=" + userMi);
                    return;
                }
                user.put("time", System.currentTimeMillis());
            } else if (cookie != null) {
                String userid = encAndDecByDES.getDesString(cookie).split("-")[0];
                User user2 = service.formatUser(userService.getUserByUserId(userid));

                currentUser.getSession().setAttribute(User.USER_SESSIONNAME, user2);
                user2.set("time", System.currentTimeMillis());
                //encryptByPrivateKey
                String userMi = encAndDecByDES.getEncString(JsonKit.toJson(user2));
                setCookie(User.USER_COOKIENAME, cookie, 60 * 60);
                redirect(url + "/?" + RdsController.RDS_PARAM + "=" + userMi);
                return;
            }
            render("/view/html/login.html");
        } else {
            render("/view/html/index.html");
        }
    }

    //all
    @ClearInterceptor(ClearLayer.ALL)
    public void all() {
        render("/view/html/all.html");
    }

    //info
    public void info() {
        render("/view/html/info.html");
    }


    //index
    @ClearInterceptor(ClearLayer.ALL)
    public void index() {
        render("/view/html/index.html");
    }

    //rds
    @ClearInterceptor(ClearLayer.ALL)
    public void rds() {
        render("/view/html/rds.html");
    }

    //rdsorder
    public void rdsorder() {
        render("/view/html/rdsorder.html");
    }

    //rdsorderDetail
    public void rdsorderdetail() {
        render("/view/html/rdsorderdetail.html");
    }

    //regist
    @ClearInterceptor(ClearLayer.ALL)
    public void regist() {
        render("/view/html/regist.html");
    }
    //regist_ck
    @ClearInterceptor(ClearLayer.ALL)
    public void regist_ck() {
        render("/view/html/regist_ck.html");
    }

    //regist_rlt
    @ClearInterceptor(ClearLayer.ALL)
    public void regist_rlt() {
        render("/view/html/regist_rlt.html");
    }
    //regist_vf
    @ClearInterceptor(ClearLayer.ALL)
    public void regist_vf() {
        render("/view/html/regist_vf.html");
    }
    //regist_vf_fail
    @ClearInterceptor(ClearLayer.ALL)
    public void regist_vf_fail() {
        render("/view/html/regist_vf_fail.html");
    }
    //updatepwd
    public void updatepwd() {
        render("/view/html/updatepwd.html");
    }

    //updatepwdrlt
    public void updatepwdrlt() {
        render("/view/html/updatepwdrlt.html");
    }

    //xieyi
    public void agreement() {
        render("/view/html/xieyi.html");
    }



    //for oss
    @ClearInterceptor(ClearLayer.ALL)
    @ClearAuthHandler
    public void oss(){
        render("/view/html/oss.html");
    }
    public void ossorder(){
        render("/view/html/ossorder.html");
    }
    public void ossorderdetail(){
        render("/view/html/ossorderdetail.html");
    }
}
