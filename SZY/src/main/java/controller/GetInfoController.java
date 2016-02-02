package controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import model.Admin;
import model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import service.UserService;
import util.JsonHelp;

/**
 * Created by Tyfunwang on 2014/12/10.
 */
//@RequiresRoles(value = {"admin", "user"}, logical = Logical.OR)
public class GetInfoController extends Controller {
    UserService userService = new UserService();

    /**
     * 获取当前发送请求的用户信息
     */
    public void getUserInfo() {
        Subject currentUser = SecurityUtils.getSubject();
        //从session中获取
        try {
            User user = (User) currentUser.getSession().getAttribute(User.USER_SESSIONNAME);

            User user1 = new User();
            user1.set(User.USER_EMAIL, user.get(User.USER_EMAIL));
            user1.set(User.USER_NAME, user.get(User.USER_NAME));
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(user1)));
        } catch (NullPointerException e) {
            renderJson(JsonHelp.buildFailed());
        }

    }

    public void getUser() {
        Subject currentUser = SecurityUtils.getSubject();
        //从session中获取
        try {
            User user = (User) currentUser.getSession().getAttribute(User.USER_SESSIONNAME);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(user)));
        } catch (NullPointerException e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    /**
     * 获取当前发送请求的管理员信息
     */
    public void getAdminInfo() {
        Subject currentUser = SecurityUtils.getSubject();
        try {
            //从session中获取
            Admin admin = (Admin) currentUser.getSession().getAttribute(Admin.ADMIN_SESSIONNAME);

            Admin admin1 = new Admin();
            admin1.set(Admin.ADMIN_EMAIL, admin.get(Admin.ADMIN_EMAIL));
            admin1.set(Admin.ADMIN_REALNAME, admin.get(Admin.ADMIN_REALNAME));
            admin1.set(Admin.ADMIN_TYPE, admin.get(Admin.ADMIN_TYPE));
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(admin1)));
        } catch (NullPointerException e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed());
        }
    }

    public void getAdmin() {
        Subject currentUser = SecurityUtils.getSubject();
        try {
            //从session中获取
            Admin admin = (Admin) currentUser.getSession().getAttribute(Admin.ADMIN_SESSIONNAME);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(admin)));
        } catch (NullPointerException e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed());
        }
    }
}
