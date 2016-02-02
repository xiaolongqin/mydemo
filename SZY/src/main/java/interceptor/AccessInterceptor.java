package interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.HashSet;

/**
 * Created by Tyfunwang on 2015/1/4.
 */
public class AccessInterceptor implements Interceptor {
    private static Set<String> ips = new HashSet<String>();

   static {
        ips.add("192.168.1.250");
        ips.add("192.168.99.30");
        ips.add("127.0.0.1");
        ips.add("192.168.0.32");
    }

    @Override
    public void intercept(ActionInvocation ai) {
        if (ips.contains(getIpAddr(ai.getController().getRequest()))) {
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
