package plugin.shiro.handler;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;

/**
 * Created by liweiqi on 2014/11/24.
 */

/**
 * 已认证通过访问控制处理器
 * 单例运行
 * @author Tyfunwang
 * */
public class AuthenticatedHandler extends AbstractAuthHandler {
    private static AuthenticatedHandler ah = new AuthenticatedHandler();

    private AuthenticatedHandler() {
    }
    public static AuthenticatedHandler me() {
        return ah;
    }

    @Override
    public void assertAuthroized() throws AuthorizationException {
        if (!SecurityUtils.getSubject().isAuthenticated())
           throw new UnauthenticatedException("The current Subject is not authenticated.");
    }
}
