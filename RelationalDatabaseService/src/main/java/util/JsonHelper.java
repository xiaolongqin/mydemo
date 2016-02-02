package util;

import com.jfinal.kit.StrKit;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by liweiqi on 2014/10/27.
 */
public class JsonHelper {
    private static final String SUCCESS = "{\"state\":true,\"msg\":null,\"data\":null}";
    private static final String FAILED = "{\"state\":false,\"msg\":null,\"data\":null}";

    public static String buildSuccess() {
        return SUCCESS;
    }

    public static String buildSuccess(String data) {
        return new StringBuilder("{\"state\":true,\"msg\":null,")
                .append("\"data\":").append(data).append("}").toString();
    }

    public static String buildSuccess(String msg, String data) {
        return new StringBuilder("{\"state\":true,\"msg\":\"").append(msg).append("\",")
                .append("\"data\":").append(data).append("}").toString();
    }

    public static String buildFailed() {
        return FAILED;
    }

    public static String buildFailed(String msg) {
        return new StringBuilder("{\"state\":false,\"msg\":\"").append(msg).append("\",")
                .append("\"data\":null}").toString();
    }

    public static String buildFailed(String msg, String data) {
        return new StringBuilder("{\"state\":false,\"msg\":\"").append(msg).append("\",")
                .append("\"data\":").append(data).append("}").toString();
    }

    public static String build(boolean state) {
        return new StringBuilder("{\"state\":" + state + ",\"msg\":\"\",")
                .append("\"data\":").append("null").append("}").toString();
    }

    public static boolean isIE(HttpServletRequest request) {
        if (request == null) return false;
        String header = request.getHeader("USER-AGENT");
        return (!StrKit.isBlank(header)) && header.contains("MSIE");
    }
}
