package controller;

import com.jfinal.core.Controller;
import model.Account;
import service.HttpServ;
import util.JsonHelp;
import util.PropertyUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liweiqi on 2014/12/29.
 */
public class MsgController extends Controller {
    private String URL = PropertyUtil.urls.getProperty("msgurl");

    public void all() {
        String url = URL + "getMsg";
        Account account = (Account) getSession().getAttribute("loginUser");
        Map<String, String[]> map = new HashMap<String, String[]>();
        map.putAll(getParaMap());
        map.put("userid", new String[]{account.getAccountId() + ""});
        try {
            String result = HttpServ.me().postMethod(url, map);
            String data = result != null ? result : JsonHelp.buildFailed();
            renderJson(data);
        } catch (Exception ex) {
            ex.printStackTrace();
            renderJson(JsonHelp.buildFailed(ex.getMessage()));
        }
    }

    public void getById() {
        String url = URL + "getMsgById";
        Account account = (Account) getSession().getAttribute("loginUser");
        Map<String, String[]> map = new HashMap<String, String[]>();
        map.putAll(getParaMap());
        map.put("userid", new String[]{account.getAccountId() + ""});
        try {
            String result = HttpServ.me().postMethod(url, map);
            String data = result != null ? result : JsonHelp.buildFailed();
            renderJson(data);
        } catch (Exception ex) {
            renderJson(JsonHelp.buildFailed(ex.getMessage()));
        }
    }
}
