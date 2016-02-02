package aop.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by liweiqi on 2015/1/4.
 */
public class PermitInter implements Interceptor {
    private static Set<String> ips = new HashSet<String>();

    {
        ips.add("118.123.173.67");
        ips.add("192.168.99.30");
        ips.add("192.168.1.250");
        ips.add("127.0.0.1");
    }


    @Override
    public void intercept(ActionInvocation ai) {
        HttpServletRequest request = ai.getController().getRequest();
        if (request.getRequestURI().contains("\\./") || request.getRequestURI().contains("\\../"))
            ai.getController().renderError(404);
        if (ips.contains(getIpAddr(request))) {
            ai.invoke();
        } else {
            ai.getController().renderError(404);
        }
    }

    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
