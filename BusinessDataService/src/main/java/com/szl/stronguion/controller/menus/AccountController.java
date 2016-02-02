package com.szl.stronguion.controller.menus;

import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.Controller;
import com.jfinal.ext.render.CaptchaRender;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PropKit;
import com.szl.stronguion.model.menus.Account;
import com.szl.stronguion.service.menus.AccountServ;
import com.szl.stronguion.utils.EncAndDecByDES;
import com.szl.stronguion.utils.JavaMail.SendMail;
import com.szl.stronguion.utils.JsonHelp;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

//import com.szl.stronguion.aop.interceptor.LoginInterceptor;

/**
 * Created by 郭皓 on 2015/6/23.
 */
@ClearInterceptor(ClearLayer.ALL)
public class AccountController extends Controller {
    private AccountServ accountServ = new AccountServ();
    public static final String ACCOUNTS = "accounts";
    private static Properties properties = new Properties();
    EncAndDecByDES encAndDecByDES=new EncAndDecByDES();


    static {
        try {
            properties.load(AccountController.class.getClassLoader().getResourceAsStream("javaMail.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void img() {
        String str = getPara("str");
        CaptchaRender img = new CaptchaRender(str);
        render(img);
    }

    @ClearInterceptor(ClearLayer.ALL)
    public void login() {
        String name = getPara("name");
        String pass = getPara("pass");
//        String pass = encAndDecByDES.getEncString(getPara("pass"));

        try {
            Account account = accountServ.login(name, pass);
            if (account != null) {
                if (account.getInt("is_validate_email") == 0 && account.getInt("is_validate_apply") == 1) {

                    renderJson(JsonHelp.buildFailed("无法登陆，邮箱未验证"));
                } else if (account.getInt("is_validate_email") == 1 && account.getInt("is_validate_apply") == 0) {

                    renderJson(JsonHelp.buildFailed("无法登陆，试用未通过审核"));
                } else if (account.getInt("is_validate_email") == 0 && account.getInt("is_validate_apply") == 0) {

                    renderJson(JsonHelp.buildFailed("无法登陆，未通过审核"));
                } else {


                    setSessionAttr(ACCOUNTS, account);
                    setCookie("uid", account.getStr("uid"), 30000000);
                    Map map = new HashMap();
                    renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
                }
            } else {
                renderJson(JsonHelp.buildFailed("用户名密码错误."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed());
        }
    }


    @ClearInterceptor(ClearLayer.ALL)
    public void getAccountMe() {
//        HttpSession session=getSession();
//         System.out.println(session.getId());
        try {


            Account account = getSessionAttr(ACCOUNTS);
            Map<String, Object> map = new HashMap<String, Object>();
            if (account == null) {
//                map.put("account", new Account().set("id",2).set("name","admin2"));
//                renderJson(JsonHelp.buildFailed(JsonKit.toJson(map)));
//                return;
                //session失效，应该带到登录界面
                renderJson(JsonHelp.buildFailed(JsonKit.toJson(map)));
                return;
            }
            map.put("account", account);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }

    }

    public void checkRole() {
        try {
            //session 获取用户的role_id，生成菜单
            Account account = getSessionAttr(AccountController.ACCOUNTS);
            Long id = account.getLong("id");//string --> int
            if (1 == id) {
                //管理员
                renderJson(JsonHelp.buildSuccess());
                return;
            }
            renderJson(JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    public void checkMail() {
        String email = getPara("email");
        if (accountServ.testEmail(email)) {
            Account account = accountServ.getAccountByEMail(email);
            if (Integer.parseInt(account.get("is_flag_apply").toString()) == 1) {
                renderJson(JsonHelp.buildFailed("此邮箱已被试用用户注册,请重新选择邮箱!"));
                return;
            } else {
                if (Integer.parseInt(account.get("is_validate_email").toString()) == 1) {

                    renderJson(JsonHelp.buildFailed("此邮箱已被正式用户注册,请重新选择邮箱!"));
                    return;
                } else {
//                    Long registerTime = (Long.parseLong(account.getLong("registertime")));
                    Long registerTime = account.getLong("registertime");
                    Long rinow = System.currentTimeMillis();
                    Long remained = 86400000 - (rinow - registerTime);
                    if (remained < 86400000 && remained > 0) {
                        int hour = (int) (remained / 1000 / 3600);
                        int fen = (int) (((remained / 1000) % 3600) / 60);

                        renderJson(JsonHelp.buildFailed("此邮箱已发送注册邮件，请前往邮箱激活!剩余激活时间： " + hour + " 小时" + fen + " 分。", "0"));
                        return;
                    } else {
                        accountServ.clearMail(email);
                        renderJson(JsonHelp.buildSuccess("恭喜，邮箱可用!", null));
                        return;
                    }
                }
            }
        } else {
            renderJson(JsonHelp.buildSuccess("恭喜，邮箱可用!", null));
            return;
        }
    }

    public void passMail() {
        System.out.println(PropKit.use("javaMail.properties").get("mailurl"));
        String email = getPara("email");
        if (accountServ.testEmail(email)) {
            Account account = accountServ.getAccountByEMail(email);
            if (Integer.parseInt(account.get("is_validate_email").toString()) == 0) {
                renderJson(JsonHelp.buildFailed("此邮箱未通过验证!"));
                return;
            } else {
//                Long registerTime = (Long.parseLong(account.getStr("resetpasstime")));
//                Long registerTime = 0L;
                long registerTime = Long.valueOf(account.getStr("resetpasstime"));
                long rinow = System.currentTimeMillis();
                long remained = 86400000 - (rinow - registerTime);
                if (remained < 86400000 && remained > 0) {
                    int hour = (int) (remained / 1000 / 3600);
                    int fen = (int) (((remained / 1000) % 3600) / 60);
                    renderJson(JsonHelp.buildFailed("此邮箱已发送密码重置邮件，请前往邮箱重置!剩余重置时间： " + hour + " 小时" + fen + " 分。", "0"));
                    return;
                } else {

                    long time = System.currentTimeMillis();
                    String vCode = encrypt(email) + Math.random() * Math.random();
                    long uid = account.getLong("id");
                    if (accountServ.passMail(uid, time, vCode)) {

                        StringBuffer sb = new StringBuffer("点击下面链接重置密码，24小时生效！</br>");
                        sb.append("<a href=\"" + PropKit.use("javaMail.properties").get("mailurl") + "?&email=");
                        sb.append(email);
                        sb.append("&validateCode=");
                        sb.append(vCode);
                        sb.append("\">" + PropKit.use("javaMail.properties").get("mailurl") + "?&email=");
                        sb.append(email);
                        sb.append("&validateCode=");
                        sb.append(vCode);
                        sb.append("</a>");
                        try {
                            renderJson(SendMail.sendMail(email, false, sb.toString()) == true ? JsonHelp.buildSuccess() : JsonHelp.buildFailed("写入注册信息成功，发送验证邮件失败"));
                        } catch (Exception e) {
                            renderJson(JsonHelp.buildFailed("写入pass信息成功，发送邮件失败"));
                        }
                    } else {
                        renderJson(JsonHelp.buildFailed("写入pass信息失败"));
                    }
                }
            }
        } else {
            renderJson(JsonHelp.buildSuccess("此邮箱未注册!", null));
            return;
        }
    }

    public void passMailFor() {
        String email = getPara("email");
        String ValidateCode = getPara("validateCode");
        Account account = accountServ.getAccountByEMail(email);
        if (account != null) {
            System.out.println(account.getInt("is_validate_email"));
            if (account.getInt("is_validate_email") == 1) {
                Long registerTime = (Long.parseLong(account.getStr("resetpasstime")));
                Long rinow = System.currentTimeMillis();

                if ((rinow - registerTime) < 86400000) {
                    if (ValidateCode.equals(account.getStr("validatecode"))) {
                        renderJson(JsonHelp.buildSuccess());
                    } else {
                        renderJson(JsonHelp.buildFailed("验证码不正确"));
                    }
                } else {
                    renderJson(JsonHelp.buildFailed("已过期！"));
                }
            } else {
                renderJson(JsonHelp.buildFailed("邮箱未激活！"));
            }
        } else {
            renderJson(JsonHelp.buildFailed("该邮箱未注册（邮箱地址不存在）！"));
        }
    }

    public void getNow() {


        renderJson(JsonHelp.buildSuccess(System.currentTimeMillis() + ""));


    }

    public void logout() {
        try {
            removeSessionAttr(ACCOUNTS);
            renderJson(JsonHelp.buildSuccess());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    //admin add a new account
    public void addAccount() {

        String inputRandomCode = getPara("inputRandomCode");
        String str = getPara("str");

        boolean loginSuccess = CaptchaRender.validate(this, inputRandomCode, str);
        if (loginSuccess) {
            String name = getPara("name");
            String pass = getPara("pass");
            String email = getPara("email");
            String dep = getPara("dep");
//        int role_id = getParaToInt("role_id");
            int cata = getParaToInt("cata");
            String cata1 = getPara("cata_name");
            String ctime = getPara("ctime");
            String etime = getPara("etime");
            String phone = getPara("phone");
            int is_flag_apply = getParaToInt("is_flag_apply");

            try {
                renderJson(JsonHelp.buildSuccess("数据库写入成功", JsonKit.toJson(accountServ.addAccount(name, pass, cata, email, dep, ctime, etime, phone, cata1, is_flag_apply))));
                return;
            } catch (Exception e) {
                renderJson(JsonHelp.buildFailed());
                e.printStackTrace();
                return;
            }
        } else {
            renderJson(JsonHelp.buildFailed("验证码错误"));
            return;
        }

    }

    public void addTestAccount() {

            String name = getPara("name");
            String pass = getPara("pass");
            String email = getPara("email");
            String dep = getPara("dep");
//        int role_id = getParaToInt("role_id");
            int cata = getParaToInt("cata");
            String cata1 = getPara("cata_name");
            String ctime = getPara("ctime");
            String etime = getPara("etime");
            String phone = getPara("phone");
            int is_flag_apply = getParaToInt("is_flag_apply");
            try {
                renderJson(JsonHelp.buildSuccess("数据库写入成功", JsonKit.toJson(accountServ.addAccount(name, pass, cata, email, dep, ctime, etime, phone, cata1, is_flag_apply))));
                return;
            } catch (Exception e) {
                renderJson(JsonHelp.buildFailed());
                e.printStackTrace();
                return;
            }
    }





    public void checkExist() {
        String name = getPara("name");
        if (accountServ.testAccount(name)) {
            renderJson(JsonHelp.buildFailed("此用户名已经存在,请重新输入!"));
            return;
        } else {
            renderJson(JsonHelp.buildSuccess("恭喜，用户名可用!", null));
            return;
        }
    }

    public void checkExistEmail() {
        String email = getPara("email");
        if (accountServ.testEmail(email)) {
            renderJson(JsonHelp.buildFailed("此邮箱已经存在,请重新输入!"));
            return;
        } else {
            renderJson(JsonHelp.buildSuccess("恭喜，邮箱可用!", null));
            return;
        }
    }

    //admin delete a old account
    public void deleteAccount() {

        int uid = getParaToInt("uid");
        Account a = accountServ.getAccountById(uid);
        if (a.getInt("status") == 1) {
            renderJson(JsonHelp.buildFailed("无法删除管理员"));
            return;
        } else {
            try {
                renderJson(accountServ.deleteAccount(uid) ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
            } catch (Exception e) {
                renderJson(JsonHelp.buildFailed());
            }
        }
    }

    //search a account by name
    public void searchAccount() {
        String name = getPara("name", "");
        int pageNumber = getParaToInt("pageNumber", 1);
        try {
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(accountServ.searchAccount(pageNumber, name))));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed());
        }
    }

    public void searchAccountDR() {
        String name = getPara("name", "");
        int pageNumber = getParaToInt("pageNumber", 1);
        try {
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(accountServ.searchAccountDR(pageNumber, name))));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed());
        }
    }

    //search a account by uid
    public void getAccountById() {
        int uid = getParaToInt("uid");
        try {
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(accountServ.getAccountById(uid))));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed());
        }
    }


    @ClearInterceptor(ClearLayer.ALL)
    public void getCata() {
        try {
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(accountServ.getCata())));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed());
        }

    }

    //edit account and role
    public void modifyAccount() {


        String x = getPara("uid");
        int uid = Integer.parseInt(x);
        Account mee = accountServ.getAccountById(uid);
        String oldpass = mee.getStr("pass");

//        Account account = accountServ.login(((Account)getSessionAttr(ACCOUNTS)).getStr("name"), oldpass);

        String pass0 = getPara("pass0");
        String pass1 = getPara("pass1");
        String phone = getPara("phone");
        String company = getPara("company");

//        if (account!=null) {
        if (oldpass.equals(pass0)) {
            try {
                renderJson(accountServ.modifyAccount(uid, pass1, phone, company) ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
            } catch (Exception e) {
                e.printStackTrace();
                renderJson(JsonHelp.buildFailed());
            }
        } else {
            renderJson(JsonHelp.buildFailed("原密码出错！"));
        }

    }

    public void modifyPass() {

        String email = getPara("email");
        Account mee = accountServ.getAccountByEMail(email);
        long uid = mee.getLong("id");
        String pass0 = getPara("pass0");

        try {

            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(accountServ.getAccountById(uid))));
            renderJson(accountServ.modifyPass(uid, pass0) ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }


    public void modifyEx() {
        int x = getParaToInt("uid");
        int cata = getParaToInt("cata");
        String cata1 = getPara("cata_name");
        try {
            renderJson(accountServ.modifyEx(x, cata, cata1) == 1 ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }

    }

    public void modifyDR() {
        int x = getParaToInt("uid");
        int right = getParaToInt("rights");
        try {
            renderJson(accountServ.modifyDR(x, right) == 1 ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }

    }

    public void registMail() {

//        String name = getPara("name");
//        String pass = getPara("pass");
        String email = getPara("email");
//        String dep = getPara("dep");
////        int role_id = getParaToInt("role_id");
//        int cata = getParaToInt("cata");
//        String cata1 = getPara("cata_name");
//        String ctime = getPara("ctime");
//        String etime = getPara("etime");
//        String phone = getPara("phone");
        int x = getParaToInt("uid");

        long time = System.currentTimeMillis();
        String vCode = encrypt(email) + Math.random() * Math.random();


        if (accountServ.registMail(x, time, vCode)) {


//            int is_flag_apply = getParaToInt("is_flag_apply");
            StringBuffer sb = new StringBuffer("点击下面链接激活账号，24小时生效，否则重新注册账号，链接只能使用一次，请尽快激活！</br>");
            sb.append("<a href=\""+properties.getProperty("url")+"?email=");
            sb.append(email);
            sb.append("&validateCode=");
            sb.append(vCode);
            sb.append("\">");
//            sb.append(email);
//            sb.append("&validateCode=");
//            sb.append(vCode);
//            sb.append("</a>");
            try {
                renderJson(SendMail.sendMail(email, false, sb.toString()) == true ? JsonHelp.buildSuccess() : JsonHelp.buildFailed("写入注册信息成功，发送验证邮件失败"));
            } catch (Exception e) {
                renderJson(JsonHelp.buildFailed("写入注册信息成功，发送验证邮件失败"));
            }
        } else {
            renderJson(JsonHelp.buildFailed("写入注册信息失败2"));
        }

    }

    public void register() {

        String email = getPara("email");
        String ValidateCode = getPara("validateCode");
        Account account = accountServ.getAccountByEMail(email);
        if (account != null) {
            //验证用户激活状态

            System.out.println(account.getInt("is_validate_email"));
            if (account.getInt("is_validate_email") == 0) {
                Long registerTime = (Long.parseLong(account.getStr("registertime")));
                Long rinow = System.currentTimeMillis();

                if ((rinow - registerTime) < 86400000) {
                    if (ValidateCode.equals(account.getStr("validatecode"))) {
                        //激活成功， //并更新用户的激活状态，为已激活
                        System.out.println("==sq===" + account.getInt("is_validate_email"));
                        modifyEmailValidate(email);
                        System.out.println("==sh===" + account.getInt("is_validate_email"));
                    } else {
                        renderJson(JsonHelp.buildFailed("激活码不正确"));
                    }
                } else {
                    renderJson(JsonHelp.buildFailed("激活码已过期！"));
                }
            } else {
                renderJson(JsonHelp.buildFailed("邮箱已激活，请登录！"));
            }
        } else {
            renderJson(JsonHelp.buildFailed("该邮箱未注册（邮箱地址不存在）！"));
        }

    }

    public void modifyEmailValidate(String email) {
        try {
            System.out.println("priljjfe");
            renderJson(accountServ.modifyEmailValidate(email) == 1 ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }

    }

    private static final String encrypt(String srcStr) {
        try {
            String result = "";
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(srcStr.getBytes("utf-8"));
            for (byte b : bytes) {
                String hex = Integer.toHexString(b & 0xFF).toUpperCase();
                result += ((hex.length() == 1) ? "0" : "") + hex;
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void checkRemainingTime() {
        int x = getParaToInt("uid");
        try {
//            renderJson(accountServ.checkRemainingTime(x) > 0 ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
            renderJson(JsonHelp.buildSuccess(accountServ.checkRemainingTime(x) + "", null));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());

            e.printStackTrace();
        }

    }

    //get account info
//    @Before(LoginInterceptor.class)
    public void getAccount() {
        try {
            Account account = getSessionAttr(ACCOUNTS);
            Map<String, Object> map = new HashMap<String, Object>();
            if (account == null) {
                //session失效，应该带到登录界面
                renderJson(JsonHelp.buildFailed(JsonKit.toJson(map)));
                return;
            }
            Account user = new Account();
            user.set(Account.NAME, account.getStr(Account.NAME));
            map.put("account", user);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    public void updatePwd() {
        String oldPwd = getPara("oldPwd");
        String newPwd = getPara("newPwd");
        String confirmPwd = getPara("confirmPwd");

        Account account = getSessionAttr(ACCOUNTS);
        int uid = account.getLong(Account.ID).intValue();

        try {
            boolean flag = accountServ.updatePwd(uid, oldPwd, newPwd, confirmPwd);
            if (flag) {
                removeSessionAttr(ACCOUNTS);
                renderJson(JsonHelp.buildSuccess());
            } else {
                renderJson(JsonHelp.buildFailed());
            }
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }

    }
}
