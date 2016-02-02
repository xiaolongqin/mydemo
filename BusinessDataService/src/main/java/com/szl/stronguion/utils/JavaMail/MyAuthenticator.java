package com.szl.stronguion.utils.JavaMail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created by 郭皓
 * on 15-04-9
 * at 下午3:13.
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
