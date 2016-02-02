package plugin.shiro.handler;

import org.apache.shiro.authz.AuthorizationException;

/**
 * Created by liweiqi on 2014/11/24.
 */
public interface AuthHandler {
    public void assertAuthroized() throws AuthorizationException;
}
