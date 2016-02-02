package service;

import model.User;
import util.EncAndDecByDES;
import util.javaMail.SendMail;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2014/11/18.
 * 对id,email用DES进行加密
 * sendMail中的true表示登录邮箱时是否需要验证
 */
public class AuthService {
    private static Map<String, String> infos = new ConcurrentHashMap<String, String>();
    private static String URL = null;
    EncAndDecByDES encAndDecByDES = new EncAndDecByDES();
    User user = new User();

    static {
        try {
            Properties p = new Properties();
            p.load(AuthService.class.getClassLoader().getResourceAsStream("javaMail.properties"));
            URL = p.getProperty("url");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * sendAuthMail
     */
    public boolean sendAuthMail(int userid, String email) throws MessagingException {
        Long timestemp = System.currentTimeMillis();

        /**
         * encrypt id by DES
         * put into map<idMi,timestemp>
         */
        String idMi = encAndDecByDES.getEncString(String.valueOf(userid));
        String emailMi = encAndDecByDES.getEncString(email);
        infos.put(idMi, timestemp + "");
        //send mail
        String url = buildUrl(idMi, emailMi);
        return SendMail.sendMail(email, true, url);
    }

    /**
     * 邮箱验证
     * 过期时间一天
     */
    public boolean authValidate(String idMi, String emailMi) throws MessagingException {

        if (infos.get(idMi) == null || (System.currentTimeMillis() - Long.valueOf(infos.get(idMi))) > 7 * 24 * 60 * 60 * 1000) {
            //error -->sendMail again
            String email = encAndDecByDES.getDesString(emailMi);
            String url = buildUrl(idMi, emailMi);
            SendMail.sendMail(email, true, url);
        } else {
            //update authentication of user;
            int realId = Integer.valueOf(encAndDecByDES.getDesString(idMi));
            if (new User().authValidate(realId)) {
                infos.remove(idMi);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }


    /**
     * sendMsgMail
     */
    public boolean sendMsgMail(String email, String title, String content, String time) throws MessagingException {
        //send msg mail
        String con = buildContent(title, content, time);
        return SendMail.sendMsgs2User(email, true, con);
    }

   //build the URL
    public String buildUrl(String idMi, String email) {
        String url = new StringBuilder().append("Click the url below to complete registration:\n").append(URL).append("authValidate?" + "id" + "=").append(idMi).append("&" + "email" + "=").append(email).toString();
        return url;
    }

    //build the msg mail
    public String buildContent(String title, String content, String time) {
        String con = new StringBuilder().append("\n").append("title : ").append(title).append("\n")
                .append("content : ").append(content).append("\n").append("time : ").append(time).toString();
        return con;
    }

    public boolean resendValidate(String email) throws MessagingException {
        //user
        User user1 = user.getUser2Resend(email);
        if (user1 != null) {
            int userid = user1.getInt(User.USER_ID);
            Long timestemp = System.currentTimeMillis();

            String idMi = encAndDecByDES.getEncString(String.valueOf(userid));
            String emailMi = encAndDecByDES.getEncString(email);
            infos.put(idMi, timestemp + "");
            //send mail
            String url = buildUrl(idMi, emailMi);
            return SendMail.sendMail(email, true, url);
            //
        }
        return true;

    }

}
