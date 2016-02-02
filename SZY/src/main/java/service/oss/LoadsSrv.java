package service.oss;

import service.AuthService;
import util.javaMail.SendMail;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Tyfunwang on 2015/2/3.
 */
public class LoadsSrv {
    private static String URL = null;
    private static LoadsSrv loadsSrv = new LoadsSrv();

    private LoadsSrv() {
    }

    public static LoadsSrv me() {
        return loadsSrv;
    }

    static {
        try {
            Properties p = new Properties();
            p.load(AuthService.class.getClassLoader().getResourceAsStream("product.properties"));
            URL = p.getProperty("oss");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //send email for oss download resourse

    public boolean sendEmailOss(String src_email,String src_e, String src_f, String dst_email,String time) throws MessagingException {
        String con = build(src_email, src_e,src_f,time);
        return SendMail.sendMsgs2Oss(dst_email, true, con);
    }

    //build the URL
    private String build(String src_email, String src_e ,String src_f,String time) {
        String url = new StringBuilder().append(src_e).append(":share with you."+"\n")
                .append("Click the below url  to download you resources:\n").append(URL+"/hdfs").
                        append("/downloadLink?" + "email" + "=").append(src_email).append("&" + "dirUrl" + "=").append(src_f).append("&time=").append(time).toString();
        return url;
    }
}
