package interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import plugin.shiro.ShiroKit;
import plugin.shiro.handler.AuthHandler;
import util.JsonHelp;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by liweiqi on 2014/11/24.
 */
//实现Shiro拦截器
public class ShiroInterceptor implements Interceptor {

    @Override
    public void intercept(ActionInvocation ai) {
        String key=ai.getActionKey();
        String url = ai.getControllerKey().equals("/ad") ? "/ad/login" : ai.getControllerKey().equals("/view") ? "/view/login" : null;
        AuthHandler authHandler = ShiroKit.getHandle(ai.getActionKey());
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            //调转到登录界面
            if (url != null) {
                flushCache(ai.getController().getResponse());
                ai.getController().redirect(url);
                return;
            } else {
                ai.getController().renderJson(JsonHelp.buildFailed());
            }
        }

        //存在访问控制处理器
        if (authHandler != null) {
            try {
                //执行权限检查
                authHandler.assertAuthroized();
            } catch (UnauthorizedException lae) {
                //RequiresAuthentication，未满足时，抛出未经授权的异常。
                //如果没有进行身份验证，返回HTTP401状态码

                ai.getController().renderError(404);
                return;
            } catch (AuthenticationException aue) {
                ai.getController().renderError(404);
                return;
            } catch (AuthorizationException ae) {
                //RequiresRoles，RequiresPermissions授权异常
                //如果没有权限访问对应的资源，返回HTTP状态码403。
                ai.getController().renderError(404);
                return;
            } catch (Exception e) {
                ai.getController().renderJson(JsonHelp.buildFailed("no privileges"));
//                ai.getController().renderError(404);
                return;
            }
        }
        //执行正常逻辑
        ai.invoke();
    }

    private void flushCache(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
        response.setHeader("Pragma", "no-cache"); //HTTP 1.0
        response.setDateHeader("Expires", 0); //prevents caching at the proxy server
    }
}
