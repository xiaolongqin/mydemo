package controller;

import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import interceptor.AccessInterceptor;
import interceptor.captcha.CaptchaIC;
import util.MySessionContext;
import model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.AuthService;
import service.ControllerService;
import service.UserService;
import util.EncAndDecByDES;
import util.JsonHelp;
import util.javaMail.SendMail;
import util.javaMail.SetParameter;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Administrator on 2014/11/6.
 */

public class UserController extends Controller {
    public static final Logger log = LoggerFactory.getLogger(User.class);

    /**
     * UserController
     */
    UserService userService = new UserService();
    EncAndDecByDES encAndDecByDES = new EncAndDecByDES();
    ControllerService controllerService = new ControllerService();

    public void index() {
        renderText("User login");
        renderJson("msg", "User Login");
    }

    /**
     * check user's email and send email
     */
    @ClearInterceptor(ClearLayer.ALL)
    public void sendResetPassMail() {
        String identitycard=getPara("identitycard");
        String email=getPara("email");
        if(userService.checkEmailAndCard(email,identitycard)){
            System.out.println("userService.checkEmailAndCard(email,identitycard)="+userService.checkEmailAndCard(email,identitycard));
            String emailmi=encAndDecByDES.getEncString(email);
            String timemi=encAndDecByDES.getEncString(String.valueOf(System.currentTimeMillis()));
            String url=new SetParameter().getProperties().getProperty("url");
            try {
                SendMail.sendresetPassMail(email, true, "尊敬的"+email+"用户！\n你好！\n数之云找回密码邮件！本邮件24小时内有效，找回密码请点击:\n"+url+"resetPass?email=" + emailmi + "&time=" + timemi);
                renderJson(JsonHelp.buildSuccess());
            } catch (MessagingException e) {
                e.printStackTrace();
                renderJson(JsonHelp.buildFailed());
            }
        }else{
            System.out.println("userService.checkEmailAndCard(email,identitycard)="+userService.checkEmailAndCard(email,identitycard));
            renderJson(JsonHelp.buildFailed());
        }
    }

    /**
     * reset user's password
     */
    @ClearInterceptor(ClearLayer.ALL)
    public void resetPass() {
        String time=encAndDecByDES.getDesString(getPara("time"));
        String email = encAndDecByDES.getDesString(getPara("email"));
        if ((System.currentTimeMillis()-Long.valueOf(time).longValue()<24*60*60*1000)&&(!userService.checkEmail(email))){
                getSession().setAttribute("reset_email", email);

                String sessionid=getSession().getId();
                System.out.println("session1=" + getSession());
                System.out.println("sessionid111=" + sessionid);
                System.out.println("session2="+MySessionContext.getSession(sessionid));

            render("/view/html/reset.html?email="+email);
        }else {
                renderJson(JsonHelp.buildFailed());
        }
    }

    /**
     * reset user's password
     */
    @ClearInterceptor(ClearLayer.ALL)
    public void sureResetPass() {
        String sessionid=getSession().getId();
        System.out.println("sessionid222="+sessionid);
        String email =getSession().getAttribute("reset_email").toString();
        System.out.println("email="+email);
        if (email==null){
            renderJson(JsonHelp.buildFailed());
            return;
        }
        String newPwd=getPara("newPwd");
        String newPwdmi=encAndDecByDES.getEncString(newPwd);
        boolean user=userService.resetPwd(email,newPwdmi);
        renderJson(user ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());

    }





    /**
     * check email
     */
    public void checkEmail() {
        String email = getPara(User.USER_EMAIL);
        boolean user = userService.checkEmail(email);
        renderJson(user ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
    }

    /**
     * check username
     */
    public void checkUsername() {
        String name = getPara(User.USER_NAME);
        boolean user = userService.checkUsername(name);
        renderJson(JsonHelp.buildSuccess());
    }

    /**
     * check realname
     */
    public void checkRealname() {
        String realname = getPara(User.USER_REALNAME);
        boolean user = userService.checkRealname(realname);
        renderJson(JsonHelp.buildSuccess());
    }

    /**
     * check tel
     */
    public void checkTel() {
        String tel = getPara(User.USER_TEL);
        boolean user = userService.checkTel(tel);
        renderJson(user ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
    }

    /**
     * check identitycard
     */

    public void checkCard() {
        String card = getPara(User.USER_CARD);
        boolean user = userService.checkCard(card);
        renderJson(user ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
    }

    /**
     * register
     */
    @ClearInterceptor(ClearLayer.ALL)
    @Before(CaptchaIC.class)
    public void register() {
        String username = getPara(User.USER_NAME);
        String realname = getPara(User.USER_REALNAME);
        String email = getPara(User.USER_EMAIL);
        String tel = getPara(User.USER_TEL);
        String comname = getPara(User.USER_COMNAME);
        String identitycard = getPara(User.USER_CARD);
        String pwdMi = encAndDecByDES.getEncString(getPara(User.USER_PWD));
        try {
            boolean user = userService.register(username, pwdMi, email, realname, tel, comname, identitycard);
            renderJson(user ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    /**
     * 邮箱认证，更改authentication
     */
    public void authValidate() {
        String idMi = getPara("id");
        String emailMi = getPara(User.USER_EMAIL);
        boolean flag = false;
        try {
            flag = new AuthService().authValidate(idMi, emailMi);
            //验证成功后，调转
            if (flag) {
                redirect("/view/regist_ck");
            } else {
                redirect("/view/regist_vf_fail?email=" + encAndDecByDES.getDesString(emailMi));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("authValidate:" + e.getMessage());
            renderJson(JsonHelp.buildFailed());
        }

    }

    //重发邮箱验证
    @ClearInterceptor(ClearLayer.ALL)
    public void resendMail() {
        String email = getPara(User.USER_EMAIL);
        try {
            renderJson(new AuthService().resendValidate(email) ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (MessagingException e) {
            renderJson(JsonHelp.buildFailed());
        }
    }


    /**
     * login and setAttribute ---loginSession
     * login and setCookie ---loginCookie
     * <p/>
     * include mainLogin(String email,String pwdMi) and subLogin(String email,String pwdMi)
     * <p/>
     * 0--登录失败
     * 1--普通登录成功
     * 2--子系统请求登录成功
     */
    @ClearInterceptor(ClearLayer.ALL)
    public void login() {
        String url = getPara("url");
        String email = getPara(User.USER_EMAIL);
        String pwdMi = encAndDecByDES.getEncString(getPara(User.USER_PWD));
        if (url == null) {
            //普通登录请求
            mainLogin(email, pwdMi);
        } else {
            //子系统登录请求
            subLogin(email, pwdMi, url);
        }
    }

    /**
     * mainLogin
     */
    private void mainLogin(String email, String pwdMi) {
        String url = "{\"json\":\"" + "/szy" + RdsController.INDEX_URL + "\"}";
        try {
            //未登录
            User user = daoLogin(email, pwdMi);
            if (user == null) {
                renderJson(JsonHelp.buildFailed("0"));
                return;
            }
            //邮箱或管理员未审核
            if (user.getInt("status") != 1 && user.getInt("authentication") != 1) {
                renderJson(JsonHelp.buildSuccess("{\"json\":\"" + "/szy" + RdsController.REGISTER_VF + "?email="
                                + user.getStr(User.USER_EMAIL) + "\"}"
                ));
                return;
            } else if (user.getInt("status") != 1 && user.getInt("authentication") == 1) {
                renderJson(JsonHelp.buildSuccess("{\"json\":\"" + "/szy" + RdsController.REGISTER_CK + "?email="
                                + user.getStr(User.USER_EMAIL) + "\"}"
                ));
                return;
            }

            renderJson(JsonHelp.buildSuccess(url));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed("0"));
        }
    }

    /**
     * subLogin
     */
    private void subLogin(String email, String pwdMi, String url) {
        try {
            User user = controllerService.formatUser(daoLogin(email, pwdMi));
            user.set("time", System.currentTimeMillis());
            //邮箱或管理员未审核
            if (user.getInt("status") != 1 && user.getInt("authentication") != 1) {
                renderJson(JsonHelp.buildSuccess("{\"json\":\"" + "/szy" + RdsController.REGISTER_VF + "?email="
                                + user.getStr(User.USER_EMAIL) + "\"}"
                ));
                return;
            } else if (user.getInt("status") != 1 && user.getInt("authentication") == 1) {
                renderJson(JsonHelp.buildSuccess("{\"json\":\"" + "/szy" + RdsController.REGISTER_CK + "?email="
                                + user.getStr(User.USER_EMAIL) + "\"}"
                ));
                return;
            }
            //encrypt
            String userMi = encAndDecByDES.getEncString(JsonKit.toJson(user));

            //rds-oss -->sessionname = loginUser
            String str = url + "/?" + RdsController.RDS_PARAM + "=" + userMi;
            StringBuilder sb = new StringBuilder();
            sb.append("{\"json\":\"");
            sb.append(str).append("\"}");

            renderJson(JsonHelp.buildSuccess(sb.toString()));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed("0"));
        }
    }

    /**
     * login utils
     */
    //dao login
    private User daoLogin(String email, String pwdMi) {
        Subject currentUser = SecurityUtils.getSubject();
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(email, pwdMi);
            currentUser.login(token);
            User user = userService.login(email);
            if (user.getInt(User.USER_STATUS) == 1) {
                //登录成功时存入session
                currentUser.getSession().setAttribute(User.USER_SESSIONNAME, user);
                //登录成功，写入cookie(cookiename,userid-username),加密userid, 有效期1小时
                setCookie(User.USER_COOKIENAME, encAndDecByDES.getEncString(String.valueOf(user.get(User.USER_ID) + "-" + user.get(User.USER_NAME))), 60 * 60);
                userService.addLoginUserSet(user.getInt(User.USER_ID));
            } else if (user.getInt(User.USER_AUTHENTICATION) == 1) {
                //登录成功时存入session
                currentUser.getSession().setAttribute(User.USER_SESSIONNAME, user);
            }
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * logout and invalidate
     */
    public void logout() {
        Subject currentUser = SecurityUtils.getSubject();
        try {
            //登出时session
            UserService.simpleRemLoginUser(((User) currentUser.getSession().getAttribute(User.USER_SESSIONNAME)).getInt(User.USER_ID));
            currentUser.logout();
            setCookie(User.USER_COOKIENAME, null, 0);
            renderJson(JsonHelp.buildSuccess());
        } catch (NullPointerException e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed());
        }
    }

    /**
     * 修改密码前需要对session中的id进行对比
     */
    @Before(CaptchaIC.class)
    public void updatePwd() {
        Subject currentUser = SecurityUtils.getSubject();
        String newPwdMi = encAndDecByDES.getEncString(getPara("newpwd"));

        try {
            User user = (User) currentUser.getSession().getAttribute(User.USER_SESSIONNAME);
            //session中获取id
            int id = user.getInt(User.USER_ID);
            boolean flag = userService.updatePwd(id, newPwdMi);
            renderJson(flag ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (NullPointerException e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    //修改个人资料
    public void modifyInfo() {
        Subject currentUser = SecurityUtils.getSubject();
        String tel = getPara(User.USER_TEL);
        String fixedline = getPara(User.USER_FIXEDLINE);
        String realname = getPara(User.USER_REALNAME);
        String comname = getPara(User.USER_COMNAME);
        //classofindustry行业分类
        String classofindustry = getPara(User.USER_CLASSOFINDUSTRY);
        //primarybusiness主营业务
        String primarybusiness = getPara(User.USER_PRIMARYBUSINESS);
        String websiteurl = getPara(User.USER_WEBSITEURL);

        //拼接address（prov-city-area-street）
        String prov = getPara("prov");
        String city = getPara("city");
        String area = getPara("area");
        String street = getPara("street");
        String address = prov + "-" + city + "-" + area + "-" + street;

        try {
            User user = (User) currentUser.getSession().getAttribute(User.USER_SESSIONNAME);
            //session中获取id
            int id = user.getInt(User.USER_ID);
            boolean flag = userService.modifyInfo(id, tel, realname, comname,
                    classofindustry, primarybusiness, websiteurl, address, fixedline);

            renderJson(flag ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (NullPointerException e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed());
        }

    }

    /**
     * 获取当前发送请求用户的信息
     */
    public void getUser() {
        Subject currentUser = SecurityUtils.getSubject();
        try {
            User user2 = (User) currentUser.getSession().getAttribute(User.USER_SESSIONNAME);
            int userid = user2.getInt(User.USER_ID);

            User user = userService.getUserInfo(userid);
            renderJson(user.equals(null) ? JsonHelp.buildFailed() : JsonHelp.buildSuccess(JsonKit.toJson(user)));
        } catch (NullPointerException e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed());
        }
    }

    /**
     * 管理员操作,管理用户部分
     * 以下为对普通用户展示的操作：列表展现
     */
    //获取所有用户 get all users byPage
    public void byPage() {
        int currentPage = getParaToInt(User.USER_CURRENTPAGE);
        int pageSize = getParaToInt(User.USER_PAGESIZE);
        int totalPage = userService.getTotal(pageSize, 3);
        List<User> userList = userService.getUsersBypage(currentPage, pageSize);
        renderJson(userList == null ? JsonHelp.buildFailed() : JsonHelp.buildSuccessByPage(totalPage, JsonKit.toJson(userList)));

    }

    //get  users byStatus
    public void byStatus() {
        int currentPage = getParaToInt(User.USER_CURRENTPAGE);
        int pageSize = getParaToInt(User.USER_PAGESIZE);
        int status = getParaToInt(User.USER_STATUS);
        int totalPage = userService.getTotal(pageSize, status);

        List<User> userList = userService.getUsersByStatus(currentPage, pageSize, status);
        renderJson(userList == null ? JsonHelp.buildFailed() : JsonHelp.buildSuccessByPage(totalPage, JsonKit.toJson(userList)));
    }

    //get user byAttr
    public void byAttr() throws UnsupportedEncodingException {
        String attr = getPara("attr");
        List<User> users = userService.fuzzyFindUser(attr);

        renderJson(users.size() <= 0 ? JsonHelp.buildFailed() : JsonHelp.buildSuccess(JsonKit.toJson(users)));
    }

    /**
     * 实现子系统和本系统的同事登出
     */

    @ClearInterceptor(ClearLayer.ALL)
    @Before(AccessInterceptor.class)
    public void isUserLogin() {
        int id = getParaToInt(User.USER_ID);

        boolean state = UserService.isUserLogin(id);
        renderJson(JsonHelp.buildSuccess("{\"state\":" + state + "}"));
    }

}
