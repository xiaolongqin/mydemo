package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.StrKit;
import model.User;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.subject.Subject;
import plugin.shiro.ShiroKit;
import plugin.shiro.annotation.RequiresRoles;
import service.ControllerService;
import service.HttpClientService;
import service.UserService;
import util.EncAndDecByDES;
import util.JsonHelp;

import java.util.Map;
import java.util.Properties;

/**
 * Created by Tyfunwang on 2014/12/23.
 */
@RequiresRoles(value = {"rdsadmin", "superadmin"}, logical = Logical.OR)
public class RdsController extends Controller {
    public static final String INDEX_URL = "/view/index";
    public static final String REGISTER_CK = "/view/regist_ck";
    public static final String REGISTER_VF = "/view/regist_vf";
    public static final String RDS_PARAM = "loginUser";
    public static final String RDS_URL = "/view/space";
    HttpClientService httpClientService = new HttpClientService();
    UserService userService = new UserService();
    EncAndDecByDES encAndDecByDES = new EncAndDecByDES();
    ControllerService service = new ControllerService();
    public static String ip = null;
    public static String domain = null;

    static {
        try {
            Properties p = new Properties();
            p.load(RdsController.class.getClassLoader().getResourceAsStream("product.properties"));
            ip = p.getProperty("rds");
            domain = p.getProperty("rdso");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    //立即使用

    @RequiresRoles(value = {"user"})
    public void toUsing() {
        ShiroKit.getHandle("");
        Subject currentUser = SecurityUtils.getSubject();
        try {
            User user = (User) currentUser.getSession().getAttribute(User.USER_SESSIONNAME);
            String cookie = getCookie(User.USER_COOKIENAME);
            if (user != null) {
                //已经登陆过
                if (currentUser.isAuthenticated()) {
                    User user1 = service.formatUser(user);
                    user1.put("time", System.currentTimeMillis());
                    String userMi = encAndDecByDES.getEncString(JsonKit.toJson(user1));
                    String str = domain + RDS_URL + "/?" + RDS_PARAM + "=" + userMi;

                    StringBuilder sb = new StringBuilder();
                    sb.append("{\"json\":\"");
                    sb.append(str).append("\"}");
                    renderJson(JsonHelp.buildSuccess(sb.toString()));
                    return;
                }
            } else if (cookie != null) {
                String userid = encAndDecByDES.getDesString(cookie).split("-")[0];
                User user2 = service.formatUser(userService.getUserByUserId(userid));
                user2.set("time", System.currentTimeMillis());
                currentUser.getSession().setAttribute(User.USER_SESSIONNAME, user2);

                String userMi = encAndDecByDES.getEncString(JsonKit.toJson(user2));
                String str = domain + RDS_URL + "/?" + RDS_PARAM + "=" + userMi;
                StringBuilder sb = new StringBuilder();
                sb.append("{\"json\":\"");
                sb.append(str).append("\"}");

                renderJson(JsonHelp.buildSuccess(sb.toString()));
                return;
            }
            renderJson(JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    /**
     * manage order for admin
     */
    //get all orders
    public void getAllOrders() {
//        renderJson(JsonHelp.buildFailed());
        String order_num = getPara("order_num");
        Map<String, String[]> map = getParaMap();
        String re = null;
        try {
            if (!StrKit.isBlank(order_num)) {
                String url = ip + "/order/getById";
                re = httpClientService.postMethod(url, modifyPara(map));
            } else {
                String url = ip + "/order/all";
                re = httpClientService.postMethod(url, modifyPara(map));
            }
        } catch (Exception ex) {
            re = JsonHelp.buildFailed("inner error");
        }
        if (re == null) re = JsonHelp.buildFailed();
        renderJson(re);
    }


    //付款并通过
    public void payAndAllow() {
        String url = ip + "/order/payAndAllow";
        String re = null;
        try {
            re = httpClientService.postMethod(url, modifyPara(getParaMap()));
        } catch (Exception e) {
            re = JsonHelp.buildFailed("inner error");
        }
        renderJson(re);
    }

    //拒绝
    public void refuse() {
        Map<String, String[]> map = getParaMap();
        String url = ip + "/order/refuse";
        String str = null;
        try {
            str = httpClientService.postMethod(url, getParaMap());
        } catch (Exception ex) {
            str = JsonHelp.buildFailed("inner error");
        }
        renderJson(str);
    }

    //获取订单详情
    public void getAndSp() {
        String url = ip + "/order/getAndSP";
        String str = null;
        try {
            str = httpClientService.postMethod(url, modifyPara(getParaMap()));
        } catch (Exception e) {
            str = JsonHelp.buildFailed("inner error");
        }
        renderJson(str);
    }

    //编辑订单
    public void upOrder() {
        String url = ip + "/order/up";
        String str = null;
        try {
            str = httpClientService.postMethod(url, getParaMap());
        } catch (Exception e) {
            str = JsonHelp.buildFailed("inner error");
        }
        renderJson(str);
    }


    /**
     * manage db for admin
     */

    //编辑数据库备注
    public void upDb() {
        Map<String, String[]> map = getParaMap();
        String url = ip + "/db/up";
        String re = null;
        try {
            String str = httpClientService.postMethod(url, map);
            re = parseJson(str);
        } catch (Exception e) {
            re = JsonHelp.buildFailed("inner error");
        }
        renderJson(re);
    }

    //恢复
    public void dbRecovery() {
        Map<String, String[]> map = getParaMap();
        String url = ip + "/db/recovery";
        String re = null;
        try {
            String str = httpClientService.postMethod(url, map);
            re = parseJson(str);
        } catch (Exception e) {
            re = JsonHelp.buildFailed("inner error");
        }
        renderJson(re);
    }

    //拒绝
    public void dbForbid() {
        Map<String, String[]> map = getParaMap();
        String url = ip + "/db/forbid";
        try {
            String str = httpClientService.postMethod(url, map);
            String re = parseJson(str);
            renderJson(re);
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    /**
     * manage space for admin
     */

    //空间信息
    public void allByCondition() {
        Map<String, String[]> map = getParaMap();
        String url = ip + "/space/allByCondition";
        try {
            String re = httpClientService.postMethod(url, modifyPara(map));
            String str = parseJson(re);
            renderJson(str);
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    //获得已审核服务订单信息
    public void getDetail() {
        Map<String, String[]> map = getParaMap();
        String url = ip + "/space/getDetail";
        try {
            String str = httpClientService.postMethod(url, map);
            renderJson(str);
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    //编辑备注
    public void spaceUp() {
        Map<String, String[]> map = getParaMap();
        String url = ip + "/space/up";
        try {
            String str = httpClientService.postMethod(url, map);
            String re = parseJson(str);
            renderJson(re);
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    //恢复
    public void spaceRecovery() {
        Map<String, String[]> map = getParaMap();
        String url = ip + "/space/recovery";
        try {
            String str = httpClientService.postMethod(url, map);
            String re = parseJson(str);
            renderJson(re);
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    //拒绝
    public void spaceForbid() {
        Map<String, String[]> map = getParaMap();
        String url = ip + "/space/forbid";
        try {
            String str = httpClientService.postMethod(url, map);
            String re = parseJson(str);
            renderJson(re);
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    /**
     * manage order for user
     */

    //get user's orders
    @RequiresRoles(value = {"rdsadmin", "user"}, logical = Logical.OR)
    public void getUserOrders() {
//        renderJson(JsonHelp.buildFailed());
        Map<String, String[]> map = getParaMap();
        String url = ip + "/order/all";
        Subject currentuser = SecurityUtils.getSubject();
        String re = null;
        try {
            int userid = ((User) currentuser.getSession().getAttribute(User.USER_SESSIONNAME)).getInt(User.USER_ID);

            re = httpClientService.postMethod(url, modifyPara(map, userid));

            renderJson(re);
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    //拒绝
    @RequiresRoles(value = {"rdsadmin", "user"}, logical = Logical.OR)
    public void cancel() {
        Map<String, String[]> map = getParaMap();
        String url = ip + "/order/refuse";
        String str = null;
        Subject currentuser = SecurityUtils.getSubject();
        try {
            int userid = ((User) currentuser.getSession().getAttribute(User.USER_SESSIONNAME)).getInt(User.USER_ID);
            str = httpClientService.postMethod(url, modifyPara(map, userid));
        } catch (Exception ex) {
            str = JsonHelp.buildFailed("inner error");
        }
        renderJson(str);
    }

    //获得订单详情
    @RequiresRoles(value = {"rdsadmin", "user"}, logical = Logical.OR)
    public void getOrderDetail() {
        Map<String, String[]> map = getParaMap();
        String url = ip + "/order/getAndSP";
        String str = null;
        Subject currentuser = SecurityUtils.getSubject();
        try {
            int userid = ((User) currentuser.getSession().getAttribute(User.USER_SESSIONNAME)).getInt(User.USER_ID);
            str = httpClientService.postMethod(url, modifyPara(map, userid));
        } catch (Exception e) {
            str = JsonHelp.buildFailed("inner error");
        }
        renderJson(str);
    }


    private Map<String, String[]> modifyPara(Map<String, String[]> map) {
        Map<String, String[]> attrs = new HashedMap();
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            attrs.put(entry.getKey(), entry.getValue());
        }
        String[] account_email = attrs.get("account_email");
        String[] account_name = attrs.get("account_name");
        if (account_email != null && !StrKit.isBlank(account_email[0].toString())) {
            int id = userService.getUserId(account_email[0].toString(), 0);
            attrs.put("account_id", new String[]{id + ""});
        } else if (account_name != null && !StrKit.isBlank(account_name[0].toString())) {
            int id = userService.getUserId(account_name[0].toString(), 1);
            attrs.put("account_id", new String[]{id + ""});
        } else {
            attrs.put("account_id", new String[]{-2 + ""});
        }
        return attrs;
    }

    private Map<String, String[]> modifyPara(Map<String, String[]> map, int userid) {
        Map<String, String[]> attrs = new HashedMap();
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            attrs.put(entry.getKey(), entry.getValue());
        }
        attrs.put("account_id", new String[]{userid + ""});
        return attrs;
    }

    //解析json
    private String parseJson(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject list = (JSONObject) jsonObject.get("data");
        if (list != null) {
            JSONArray jsonArray = list.getJSONArray("list");
            for (int i = 0; i < jsonArray.size(); i++) {
                String id = jsonArray.getJSONObject(i).getString("account_id");
                User user = userService.getUserByUserId(id);
                jsonArray.getJSONObject(i).put(User.USER_EMAIL, user.getStr(User.USER_EMAIL));
                jsonArray.getJSONObject(i).put(User.USER_REALNAME, user.getStr(User.USER_REALNAME));
            }
            return jsonObject.toJSONString();
        }
        return jsonObject.toJSONString();
    }

}
