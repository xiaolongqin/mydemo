package controller.oss;

import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import controller.RdsController;
import model.User;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.subject.Subject;
import plugin.shiro.ShiroKit;
import plugin.shiro.annotation.ClearAuthHandler;
import plugin.shiro.annotation.RequiresRoles;
import service.ControllerService;
import service.HttpClientService;
import service.UserService;
import service.oss.LoadsSrv;
import util.EncAndDecByDES;
import util.JsonHelp;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Tyfunwang on 2015/1/30.
 */
@RequiresRoles(value = {"rdsadmin", "superadmin", "user"}, logical = Logical.OR)
public class OssController extends Controller {
    private HttpClientService httpserv = new HttpClientService();
    UserService userService = new UserService();
    EncAndDecByDES encAndDecByDES = new EncAndDecByDES();
    ControllerService service = new ControllerService();
    public static final String OSS_PARAM = "loginUser";
    public static final String OSS_URL = "/view/main";
    public static final String OSS_OPEN = "/view/open";


    public static String ip = null;
    public static String domain = null;

    static {
        try {
            Properties p = new Properties();
            p.load(RdsController.class.getClassLoader().getResourceAsStream("product.properties"));
            ip = p.getProperty("oss");
            domain = p.getProperty("osso");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    //立即使用

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
                    String str = null;
                    if (isHasOpen(user1.getStr(User.USER_EMAIL))) {
                        str = domain + OSS_URL + "?" + OSS_PARAM + "=" + userMi;
                    } else {
                        str = domain + OSS_OPEN + "?" + OSS_PARAM + "=" + userMi;
                    }

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
                String str = null;
                if (isHasOpen(user2.getStr(User.USER_EMAIL))) {
                    str = domain + OSS_URL + "?" + OSS_PARAM + "=" + userMi;
                } else {
                    str = domain + OSS_OPEN + "?" + OSS_PARAM + "=" + userMi;
                }
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

    private boolean isHasOpen(String email) {
        Map<String, String[]> map = new HashMap<String, String[]>();
        map.put("email", new String[]{email});
        String url = ip + "/hdfs/isHas";
        try {
            String result = httpserv.postMethod(url, map);
            if ("true".equals(result)) return true;
           // if ("false".equals(result)) return false;
           // throw new RuntimeException("运行错误，请重试！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @ClearAuthHandler
    @ClearInterceptor
    public void check() {
        String email = getPara("dst_email");
        try {
            boolean flag = userService.checkEmail(email);
            renderText(flag ? "true" : "false");
        } catch (Exception e) {
            renderText("false");
        }
    }

    /**
     * user manage hdfs
     */

//    //openService
//    public void openService() {
//        Map<String, String[]> map = getParaMap();
//        String url = ip + "/hdfs/openService";
//        try {
//            String result = httpserv.postMethod(url, map);
//            renderJson(result);
//        } catch (Exception e) {
//            renderJson(JsonHelp.buildFailed());
//        }
//    }
//
//    //mkdir
//    public void mkdir() {
//        Map<String, String[]> map = getParaMap();
//        String url = ip + "/hdfs/mkdir";
//        try {
//            String result = httpserv.postMethod(url, map);
//            renderJson(result);
//        } catch (Exception e) {
//            renderJson(JsonHelp.buildFailed());
//        }
//    }

//    //delete
//    public void delete() {
//        Map<String, String[]> map = getParaMap();
//        String url = ip + "/hdfs/delete";
//        try {
//            String result = httpserv.postMethod(url, map);
//            renderJson(result);
//        } catch (Exception e) {
//            renderJson(JsonHelp.buildFailed());
//        }
//    }

//    //rename directory or file
//    public void rename() {
//        Map<String, String[]> map = getParaMap();
//        String url = ip + "/hdfs/rename";
//        try {
//            String result = httpserv.postMethod(url, map);
//            renderJson(result);
//        } catch (Exception e) {
//            renderJson(JsonHelp.buildFailed());
//        }
//    }
//
//    //move file and directory
//    public void mvFile() {
//        Map<String, String[]> map = getParaMap();
//        String url = ip + "/hdfs/mvFile";
//        try {
//            String result = httpserv.postMethod(url, map);
//            renderJson(result);
//        } catch (Exception e) {
//            renderJson(JsonHelp.buildFailed());
//        }
//    }

//    //get the user's file
//    public void getUserFile() {
//        String url = ip + "/account/getUserFile";
//        try {
//            String result = httpserv.postMethod(url);
//            renderJson(JsonHelp.buildSuccess(result));
//        } catch (Exception e) {
//            renderJson(JsonHelp.buildFailed());
//        }
//    }

//    @ClearAuthHandler
//    public void getUser() {
//        String url = ip + "/account/get";
//        try {
//            String result = httpserv.postMethod(url);
//            render(result);
//        } catch (Exception e) {
//            renderJson(JsonHelp.buildFailed());
//        }
//    }

    //getNextFile获取给定目录下全部的子目录:包括根目录
    public void getNextFile() {
        Map<String, String[]> map = getParaMap();
        String url = ip + "/hdfs/getNextFile";
        try {
            String result = httpserv.postMethod(url, map);
            renderJson(result);
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    //getDir获取给定目录下的子文件夹
//    public void getDir() {
//        Map<String, String[]> map = getParaMap();
//        String url = ip + "/hdfs/getDir";
//        try {
//            String result = httpserv.postMethod(url, map);
//            renderJson(result);
//        } catch (Exception e) {
//            renderJson(JsonHelp.buildFailed());
//        }
//    }

//    //shared file
//    public void sharedFile() {
//        String email = getPara("email");
//        String src_f = getPara("src_f");
//        String dst_email = getPara("dst_email");
//        Map<String, String[]> map = getParaMap();
//        //check email
//        if (userService.checkEmail(email)) {
//            String url = ip + "/hdfs/sharedFile";
//            String result = httpserv.postMethod(url, map);
//            renderJson(result);
//        } else {
//            //发送邮件链接加密
//            try {
//                String e = encAndDecByDES.getEncString(email);
//                String s = encAndDecByDES.getEncString(src_f);
//                LoadsSrv.me().sendEmailOss(e, s, dst_email);
//                renderJson(JsonHelp.buildFailed("下载链接已经发送至" + dst_email));
//            } catch (MessagingException e) {
//                e.printStackTrace();
//                renderJson(JsonHelp.buildFailed());
//            }
//        }
//    }

    //upload
//    public void uploadFile() {
//        Map<String, String[]> map = getParaMap();
//        String url = ip + "/hdfs/uploadFile";
//        try {
//            String result = httpserv.postMethod(url, map);
//            renderJson(result);
//        } catch (Exception e) {
//            renderJson(JsonHelp.buildFailed());
//        }
//    }

    //链接下载 download
//    public void downloadLink() {
//        String emailMi = getPara("email");
//        String src_fMi = getPara("src_f");
//        String url = ip + "/hdfs/downloadLink";
//        String email = encAndDecByDES.getDesString(emailMi);
//        String src_f = encAndDecByDES.getDesString(src_fMi);
//
//        Map<String, String[]> map = new HashMap<String, String[]>();
//        map.put("email", new String[]{email});
//        map.put("dirUrl", new String[]{src_f});
//        try {
//            String result = httpserv.postMethod(url, map);
//            renderJson(JsonHelp.buildSuccess(result));
//        } catch (Exception e) {
//            renderJson(JsonHelp.buildFailed());
//
//        }
//
//    }

    //下载
//    public void downloads() {
//        Map<String, String[]> map = getParaMap();
//        String url = ip + "/hdfs/downloadLocals";
//        try {
//            String result = httpserv.postMethod(url, map);
//            renderJson(result);
//        } catch (Exception e) {
//            renderJson(JsonHelp.buildFailed());
//        }
//    }

    /**
     * order for admin
     */

// 查询已审核的订单
    public void getChecked() {
        Map<String, String[]> map = getParaMap();
        String url = ip + "/order/getChecked";
        try {
            String result = httpserv.postMethod(url, map);
            renderJson(result);
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    //按订单号查询订单详情//getDetailsByNum
    public void getDetails() {
        Map<String, String[]> map = getParaMap();
        String url = ip + "/order/getDetailsByNum";
        try {
            String result = httpserv.postMethod(url, map);
            renderJson(result);
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    // 添加订单备注
    public void addOrderRemarksByNum() {
        Map<String, String[]> map = getParaMap();
        String url = ip + "/order/addOrderRemarksByNum";
        try {
            String result = httpserv.postMethod(url, map);
            renderJson(result);
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    // 按条件查询    0:邮箱  1:用户真实姓名 2：订单
    public void getOrderByConditions() {
        Map<String, String[]> map = getParaMap();
        String url = ip + "/order/getOrderByConditions";
        try {
            String result = httpserv.postMethod(url, map);
            renderJson(result);
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    //query by CheckState and Pay_state
    public void queryByCstateAndPstate() {
        Map<String, String[]> map = getParaMap();
        String url = ip + "/order/queryByCstateAndPstate";
        try {
            String result = httpserv.postMethod(url, map);
            renderJson(result);
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    //拒绝订单
    public void refusedById() {
        Map<String, String[]> map = getParaMap();
        String url = ip + "/order/refusedById";
        try {
            String result = httpserv.postMethod(url, map);
            renderJson(result);
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    //已付款并通过
    public void passByid() {
        Map<String, String[]> map = getParaMap();
        String url = ip + "/order/passByid";
        try {
            String result = httpserv.postMethod(url, map);
            renderJson(result);
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    //扩容信息修改
    public void addSpaceUpdate() {
        Map<String, String[]> map = getParaMap();
        String url = ip + "/order/addSpaceUpdate";
        try {
            String result = httpserv.postMethod(url, map);
            renderJson(result);
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    //增加订单
    public void addOrder() {
        Map<String, String[]> map = getParaMap();
        String url = ip + "/order/addOrder";
        try {
            String result = httpserv.postMethod(url, map);
            renderJson(result);
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    /**
     * order for user
     */
    //query by userOperate and Pay_state
    public void queryByState() {
        Map<String, String[]> map = getParaMap();
        Subject subject = SecurityUtils.getSubject();
        User currentUser = (User) subject.getSession().getAttribute(User.USER_SESSIONNAME);
        String email = currentUser.getStr(User.USER_EMAIL);
        String url = ip + "/user/queryByState";
        try {
            String result = httpserv.postMethod(url, modifyPara(map, email));
            renderJson(result);
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    //取消订单
    public void cancelByid() {
        Map<String, String[]> map = getParaMap();
        String url = ip + "/user/cancelByid";
        try {
            String result = httpserv.postMethod(url, map);
            renderJson(result);
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    //用户查看订单详情
    public void getDetailsByNum() {
        Map<String, String[]> map = getParaMap();
        String url = ip + "/user/getDetailsByNum";
        try {
            String result = httpserv.postMethod(url, map);
            renderJson(result);
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }


    //util method
    private Map<String, String[]> modifyPara(Map<String, String[]> map, String email) {
        Map<String, String[]> attrs = new HashedMap();
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            attrs.put(entry.getKey(), entry.getValue());
        }
        attrs.put("account_email", new String[]{email + ""});
        return attrs;
    }
}
