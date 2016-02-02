package aop.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import model.Account;
import util.PropertyUtil;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by Tyfunwang on 2015/1/27.
 */
public class LoginInterceptor implements Interceptor {

    @Override
    public void intercept(ActionInvocation ai) {
        HttpServletResponse response = ai.getController().getResponse();
        String ossMainUrl = PropertyUtil.urls.getProperty("meurl");
        String szyLoginUrl = PropertyUtil.urls.getProperty("loginurl");
        HttpSession session = ai.getController().getSession();
       String json = ai.getController().getPara("loginUser");

        if (json != null) {
            try {
                Account account = Account.parse(json);
                session.setAttribute("loginUser", account);

//                MySessionContext.AddSession(json,session);

                flushCache(response);
                //ai.getController().redirect(ossMainUrl);
               // return;
            } catch (Exception e) {
                flushCache(response);
               // ai.getController().redirect(ossMainUrl);
               // return;
            }
        } else {
            if (session.getAttribute("loginUser") == null) {
                String url = szyLoginUrl + "?url=" + ossMainUrl;
                flushCache(response);
                ai.getController().redirect(url);
                return;
            }
        }
        ai.invoke();
    }

    public static void flushCache(HttpServletResponse response) {
        response.setHeader("cache-control", "no-cache");
        response.setHeader("expires", "0");
    }
}
