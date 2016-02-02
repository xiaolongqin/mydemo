package aop.interceptor;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import model.Account;
import service.HttpServ;
import util.PropertyUtil;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liweiqi on 2015/1/4.
 */
public class LoginStateInterceptor implements Interceptor {
    private static String logstateUrl = PropertyUtil.urls.getProperty("logstate");
    private static String szyUrl = PropertyUtil.urls.getProperty("loginurl");

    @Override
    public void intercept(ActionInvocation ai) {
        Account account = (Account) ai.getController().getSession().getAttribute("loginUser");
        int accountId = account.getAccountId();
        Map<String, String[]> attrs = new HashMap<String, String[]>();
        attrs.put("userid", new String[]{accountId + ""});
        try {
            String res = HttpServ.me().postMethod(logstateUrl, attrs);
            JSONObject obj = JSON.parseObject(res);
            Boolean isLogin = obj.getJSONObject("data").getBoolean("state");
            if (!isLogin) {
                ai.getController().getSession().removeAttribute("loginUser");
                HttpServletResponse response = ai.getController().getResponse();
                LoginInterceptor.flushCache(response);
                ai.getController().redirect(szyUrl);
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ai.getController().redirect(szyUrl);
            return;
        }
        ai.invoke();
    }
}
