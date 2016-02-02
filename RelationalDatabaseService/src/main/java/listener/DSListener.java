package listener;

import service.DSBox;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by liweiqi on 2014/12/4.
 */
public class DSListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        Object ds = session.getAttribute(DSBox.NAMEINSESSION);
        if (ds != null) {
            DSBox box = (DSBox) ds;
            box.stop();
        }
    }
}
