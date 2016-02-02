package controller;

import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import interceptor.captcha.CaptchaIC;
import model.Admin;
import model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import plugin.shiro.annotation.RequiresRoles;
import service.AdminService;
import service.UserService;
import util.EncAndDecByDES;
import util.JsonHelp;

import java.util.List;

/**
 * Created by Administrator on 2014/11/6.
 */
@RequiresRoles("admin")
public class AdminController extends Controller {
    EncAndDecByDES encAndDecByDES = new EncAndDecByDES();
    AdminService adminService = new AdminService();
    UserService userService = new UserService();

    @ClearInterceptor(ClearLayer.ALL)
    public void index() {
        render("index.html");
    }

    /**
     * Admin Controller
     * <p/>
     * admin login and setSession
     */
    @ClearInterceptor(ClearLayer.ALL)
    public void login() {
        String email = getPara(Admin.ADMIN_EMAIL);
        String pwdMi = encAndDecByDES.getEncString(getPara(Admin.ADMIN_PWD));
        //shiro
        try {
            Subject currentUser = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(email, pwdMi);
            currentUser.login(token);
            Admin admin = adminService.login(email);
            //登录成功时存入session
            currentUser.getSession().setAttribute(Admin.ADMIN_SESSIONNAME, admin);
            //登录成功，写入cookie(cookiename,adminid-adminname),加密, 有效期1小时
            setCookie(Admin.ADMIN_COOKIENAME, encAndDecByDES.getEncString(String.valueOf(admin.get(Admin.ADMIN_ID) + "-" + admin.get(Admin.ADMIN_NAME))), 60 * 60);
            renderJson(JsonHelp.buildSuccess());
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed());
        }
    }

    /**
     * logout and invalidate
     */
    @ClearInterceptor(ClearLayer.ALL)
    public void logout() {
        Subject currentUser = SecurityUtils.getSubject();
        try {
            //登出时session
            currentUser.logout();
            setCookie(Admin.ADMIN_COOKIENAME, null, 0);
            renderJson(JsonHelp.buildSuccess());
        } catch (NullPointerException e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed());
        }
    }

    //add admin of superadmin
    @RequiresRoles("superadmin")
    public void register() {

        String realname = getPara(Admin.ADMIN_REALNAME);
        String email = getPara(Admin.ADMIN_EMAIL);
        String pwdMi = encAndDecByDES.getEncString(getPara(Admin.ADMIN_PWD));
        try {
            boolean flag = adminService.register(realname, pwdMi, email);
            renderJson(flag ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    //delete admin by adminid
    @Before(CaptchaIC.class)
    public void delAdmin() {
        int adminid = getParaToInt(Admin.ADMIN_ID);
        renderJson(adminService.delAdmin(adminid) ? JsonHelp.buildFailed() : JsonHelp.buildSuccess());
    }
    //admin modify
    public void modifyInfo() {
        Subject currentUser = SecurityUtils.getSubject();
        String name = getPara(Admin.ADMIN_NAME);
        String tel = getPara(Admin.ADMIN_TEL);
        String fixedline = getPara(Admin.ADMIN_FIXEDLINE);

        try {
            Admin admin = (Admin) currentUser.getSession().getAttribute(Admin.ADMIN_SESSIONNAME);
            int adminid = admin.getInt(Admin.ADMIN_ID);
            boolean flag = adminService.modifyInfo(adminid, name, tel, fixedline);

            renderJson(flag ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (NullPointerException e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed());
        }
    }

    //获取当前发送请求用户的信息
    public void getAdmin() {
        Subject currentUser = SecurityUtils.getSubject();

        try {
            int adminid = ((Admin) currentUser.getSession().getAttribute(Admin.ADMIN_SESSIONNAME)).getInt(Admin.ADMIN_ID);

            Admin admin = adminService.getAdminInfo(adminid);
            renderJson(admin.equals(null) ? JsonHelp.buildFailed() : JsonHelp.buildSuccess(JsonKit.toJson(admin)));
        } catch (NullPointerException e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed());
        }
    }

    //获取当前管理员所拥有的管理服务信息，
    public void getAdminServices() {
        Subject currentUser = SecurityUtils.getSubject();
        try {
            Object obj=currentUser.getSession().getAttribute(Admin.ADMIN_SESSIONNAME);
            int adminid = ((Admin)obj).getInt(Admin.ADMIN_ID);

            List<Record> record = adminService.getAdminService(adminid);
            renderJson(record == null ? JsonHelp.buildFailed() : JsonHelp.buildSuccess(JsonKit.toJson(record)));
        } catch (NullPointerException e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed());
        }
    }

    /**
     * admin updatePwd
     * 修改密码前需要进行判断，Session
     */
    @Before(CaptchaIC.class)
    public void updatePwd() {
        Subject currentUser = SecurityUtils.getSubject();
        String newPwdMi = encAndDecByDES.getEncString(getPara("newpwd"));
        try {
            Admin admin = (Admin) currentUser.getSession().getAttribute(Admin.ADMIN_SESSIONNAME);
            //session中获取adminid
            int adminid = admin.getInt(Admin.ADMIN_ID);
            boolean flag = adminService.updatePwd(adminid, newPwdMi);
            renderJson(flag ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (NullPointerException e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed());
        }

    }


    //get all admins for the superadmin

    @RequiresRoles("superadmin")
    public void getAllAdmins() {
        int currentPage = getParaToInt(User.USER_CURRENTPAGE);
        int pageSize = getParaToInt(User.USER_PAGESIZE);
        try {
            Page<Record> recordList = adminService.getAllAdmins(currentPage, pageSize);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(recordList)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed());
        }

    }

    /**
     * updateStatus for user
     */
    public void updateStatus() {
        int userid = getParaToInt(User.USER_ID);
        int status = getParaToInt(User.USER_STATUS);

        boolean flag = userService.updateStatus(userid, status);
        renderJson(flag ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
    }


    //get admin's service and service'url
}
