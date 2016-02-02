package handler;

import com.jfinal.handler.Handler;
import model.Admin;
import model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import service.AdminService;
import service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Tyfunwang on 2014/12/8.
 */
public class TestHandler extends Handler {


    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        response.setHeader("access-control-allow-origin", "*");
        //通过userid查询内容
        Subject currentUser = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("810880747@qq.com", "8fd8e87af777e80c");
        currentUser.login(token);
//        Admin admin = new AdminService().login("443296059@qq.com");
        User user = new UserService().login("810880747@qq.com");
        //登录成功时存入session
        currentUser.getSession().setAttribute(User.USER_SESSIONNAME, user);
//        currentUser.getSession().setAttribute(Admin.ADMIN_SESSIONNAME, admin);
        nextHandler.handle(target, request, response, isHandled);
    }
}
