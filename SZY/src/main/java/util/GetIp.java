package util;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Created by Tyfunwang on 2014/12/24.
 */
public class GetIp extends HttpServletRequestWrapper {
    private HttpServletRequest request;
    public GetIp(HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    public String getIp(){
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unKnown".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Cient-IP");
        }
        if(ip == null || ip.length() == 0 || "unKnown".equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Proxy-Cient-IP");
        }
        if(ip == null || ip.length() == 0 || "unKnown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }
        System.out.println(ip);
        return ip;
    }

}
