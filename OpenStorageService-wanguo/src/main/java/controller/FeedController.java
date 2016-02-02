package controller;

import com.jfinal.core.Controller;
import model.Account;
import service.HttpServ;
import util.JsonHelp;
import util.PropertyUtil;

import java.util.*;

/**
 * Created by Tyfunwang on 2015/1/31.
 */
public class FeedController extends Controller {
    //反馈信息
    private String URL = PropertyUtil.urls.getProperty("backurl");

    public void getServices() {
        String url = URL + "getServices";
        try {
            String result = HttpServ.me().postMethod(url);
            String data = result != null ? result : JsonHelp.buildFailed();
            renderJson(data);
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    public void addBack() {
        String url = URL + "addFeed/";
        String serv_name = getPara("serv_name");
        String content = getPara("content");

        Account account = (Account) getSession().getAttribute("loginUser");
        Map<String, String[]> map = new HashMap<String, String[]>();
        map.putAll(getParaMap());
        map.put("email", new String[]{account.getAccountEmail() + ""});
        map.put("serv_name",new String[]{serv_name + ""});
        map.put("content",new String[]{content + ""});

        //Map<String, String[]> map = getParaMap();
        try {
           // String result = HttpServ.me().postMethod(url, map);
            String result = HttpServ.me().getMethod(url, map);
            String data = result != null ? result : JsonHelp.buildFailed();
            renderJson(data);
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed());
        }

    }
}
