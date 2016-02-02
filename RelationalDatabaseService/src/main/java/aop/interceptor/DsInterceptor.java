package aop.interceptor;


import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import service.DSBox;

import javax.servlet.http.HttpSession;

/**
 * Created by liweiqi on 2014/12/4.
 */
public class DsInterceptor implements Interceptor {
    @Override
    public void intercept(ActionInvocation ai) {
        HttpSession session = ai.getController().getSession();
        if (session.getAttribute(DSBox.NAMEINSESSION) == null) {
            session.setAttribute(DSBox.NAMEINSESSION, new DSBox());
        }
    }
}
