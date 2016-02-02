package interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;

/**
 * Created by Tyfunwang on 2014/12/17.
 */
public class LoginInterceptor implements Interceptor {
    @Override
    public void intercept(ActionInvocation ai) {

        Subject subject = SecurityUtils.getSubject();
        try {
            if (!subject.isAuthenticated()) {
                //调转到登录界面
                ai.getController().redirect("/view/login");
            } else {
                ai.invoke();
            }

        } catch (AuthenticationException ex) {
            ex.printStackTrace();
        }
    }

}
