package plugin.shiro.handler;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * Created by liweiqi on 2014/11/24.
 */
public abstract class AbstractAuthHandler implements AuthHandler {
    protected Subject getSubject() {
        return SecurityUtils.getSubject();
    }
}
