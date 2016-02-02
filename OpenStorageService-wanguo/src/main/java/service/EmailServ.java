package service;

import java.util.Map;

/**
 * Created by Tyfunwang on 2015/2/9.
 */
public class EmailServ {

    private static String URL = null;
    private static EmailServ loadsSrv = new EmailServ();

    private EmailServ() {
    }

    public static EmailServ me() {
        return loadsSrv;
    }

    public boolean check(String url, Map<String, String[]> map) {
        String res = HttpServ.me().postMethod(url, map);
        if ("true".equals(res)) {
            return true;
        }
        return false;
    }

    public boolean sendOss(String url, Map<String, String[]> map) {
        String res = HttpServ.me().postMethod(url, map);
        if ("true".equals(res)) {
            return true;
        }
        return false;
    }
}
