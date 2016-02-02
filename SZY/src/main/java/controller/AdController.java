package controller;

import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.Controller;
import model.Admin;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import service.AdminService;
import util.EncAndDecByDES;

/**
 * Created by Tyfunwang on 2014/12/30.
 */
@RequiresRoles("admin")
public class AdController extends Controller {
    public static final String ADMIN_INDEX = "/ad/user";
    EncAndDecByDES encAndDecByDES = new EncAndDecByDES();
    AdminService adminService = new AdminService();

    /**
     * manage admin's view
     */
    @ClearInterceptor(ClearLayer.ALL)
    public void index() {
        Subject currentUser = SecurityUtils.getSubject();
        Admin admin = (Admin) currentUser.getSession().getAttribute(Admin.ADMIN_SESSIONNAME);
        String cookie = getCookie(Admin.ADMIN_COOKIENAME);
        if (admin != null) {
            render("/ad/html/user.html");
            return;
        }
        render("/ad/html/login.html");
    }

    public void admin() {
        render("/ad/html/admin.html");
    }

    public void info() {
        render("/ad/html/info.html");
    }

    @ClearInterceptor(ClearLayer.ALL)
    public void login() {
        Subject currentUser = SecurityUtils.getSubject();
        Admin admin = (Admin) currentUser.getSession().getAttribute(Admin.ADMIN_SESSIONNAME);
        String cookie = getCookie(Admin.ADMIN_COOKIENAME);
        if (admin != null) {
            if (currentUser.isAuthenticated()) {
                //登录用户信息已登录，重新设置cookie
                setCookie(Admin.ADMIN_COOKIENAME, cookie, 60 * 60);
                redirect(ADMIN_INDEX);
                return;
            }
        } else if (cookie != null) {
            String adminid = (encAndDecByDES.getDesString((cookie))).split("-")[0];
            Admin admin2 = adminService.getAdminByAdminId(adminid);
            currentUser.getSession().setAttribute(Admin.ADMIN_SESSIONNAME, admin2);
            //登录用户信息已登录，重新设置cookie,session
            setCookie(Admin.ADMIN_COOKIENAME, cookie, 60 * 60);
            redirect(ADMIN_INDEX);
            return;
        }
        render("/ad/html/login.html");
    }

    public void message() {
        render("/ad/html/message.html");
    }

    public void rds() {
        render("/ad/html/rds.html");
    }

    public void rdsdetail() {
        render("/ad/html/rdsdetail.html");
    }

    public void rdsed() {
        render("/ad/html/rdsed.html");
    }

    public void rdseddetail() {
        render("/ad/html/rdseddetail.html");
    }

    public void updatepwd() {
        render("/ad/html/updatepwd.html");
    }

    public void updatepwdrlt() {
        render("/ad/html/updatepwdrlt.html");
    }

    public void user() {
        render("/ad/html/user.html");
    }

    //for oss
    public void oss() {
        render("/ad/html/oss.html");
    }

    public void ossdetail() {
        render("/ad/html/ossdetail.html");
    }

    public void ossdirectory() {
        render("/ad/html/ossdirectory.html");
    }

    public void ossed() {
        render("/ad/html/ossed.html");
    }

    public void osseddetail() {
        render("/ad/html/osseddetail.html");
    }

}
