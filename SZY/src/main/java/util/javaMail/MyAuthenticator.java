package util.javaMail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created by Administrator on 2014/11/14.
 * 认证
 */

public class MyAuthenticator extends Authenticator {
    String username = null;
    String password = null;

    public MyAuthenticator() {
    }

    protected MyAuthenticator(String username, String password) {
        this.username = username;
        this.password = password;
    }
    protected PasswordAuthentication getPasswordAuthentication(){
        return new PasswordAuthentication(username,password);
    }
}
