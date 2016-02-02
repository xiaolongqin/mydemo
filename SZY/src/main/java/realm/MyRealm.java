package realm;

import com.jfinal.plugin.activerecord.Record;
import model.Admin;
import model.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import service.AdminService;
import service.UserService;

import java.util.List;

/**
 * Created by liweiqi on 2014/11/20.
 */
public class MyRealm extends AuthorizingRealm {
    UserService userService = new UserService();
    AdminService adminService = new AdminService();

    //认证回调函数,登录时调用.
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken loginToken = (UsernamePasswordToken) token;

        //通过表单接收的邮箱，密码
        String email = loginToken.getUsername();
        String password = String.valueOf(loginToken.getPassword());

        User user = null;
        Admin admin = null;

        try {
            if ((user = userService.realmUser(email, password)) != null) {
                //user
                if (email.equals(user.getStr(User.USER_EMAIL)) && password.equals(user.getStr(User.USER_PWD))) {

                    return new SimpleAuthenticationInfo(email, password, getName());
                } else {
                    throw new AuthenticationException("user login failed");
                }
            } else if ((admin = adminService.realmAdmin(email, password)) != null) {
                //admin
                if (email.equals(admin.getStr(Admin.ADMIN_EMAIL)) && password.equals(admin.getStr(Admin.ADMIN_PWD))) {
                    return new SimpleAuthenticationInfo(email, password, getName());
                } else {
                    throw new AuthenticationException("admin login failed");
                }
            } else {
                throw new AuthenticationException("login failed");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        /**
         * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
         */
        String email = (String) principals.fromRealm(getName()).iterator().next();
        SimpleAuthorizationInfo info = null;
        //查询用户授权信息
        User user = userService.getUserByEmial(email);
        if (user == null) {
            //visit
            info = new SimpleAuthorizationInfo();
            info.addRole("visit");
        }
        if (user != null) {
            //user
            info = new SimpleAuthorizationInfo();
            info.addRole("user");
        } else if (adminService.getAdminByEmail(email) != null) {
            //admin
            List<Record> list = adminService.getRolesByEmail(email);
            info = new SimpleAuthorizationInfo();
            info.addRole("admin");
            for (int i = 0; i < list.size(); i++) {
                Record record = list.get(i);
                info.addRole(record.getStr("rolename"));
            }
        }
        return info;
    }

}
