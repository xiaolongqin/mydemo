package listener;

import model.User;
import service.UserService;
import util.MySessionContext;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by Tyfunwang on 2015/1/8.
 */
public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

        Object user = se.getSession().getAttribute(User.USER_SESSIONNAME);
        if(user != null){
            int userid = ((User)user).getInt(User.USER_ID);
            UserService.remLoginUser(userid);
        }

    }
}
