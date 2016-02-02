package plugin.shiro.handler;

import org.apache.shiro.authz.AuthorizationException;

import java.util.List;

/**
 * Created by liweiqi on 2014/11/24.
 */
public class CompositeHandler implements AuthHandler {
    private final List<AuthHandler> authHandlers;

    public CompositeHandler(List<AuthHandler> authHandlers) {
        this.authHandlers = authHandlers;
    }

    @Override
    public void assertAuthroized() throws AuthorizationException {
        for (AuthHandler authHandler : authHandlers) {
            authHandler.assertAuthroized();
        }
    }

}
