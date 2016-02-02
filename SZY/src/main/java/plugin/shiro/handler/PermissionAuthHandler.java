package plugin.shiro.handler;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;

import java.lang.annotation.Annotation;

/**
 * Created by liweiqi on 2014/11/24.
 */
public class PermissionAuthHandler extends AbstractAuthHandler {
    private final Annotation annotation;
    public PermissionAuthHandler(Annotation annotation) {
        this.annotation = annotation;
    }

    @Override
    public void assertAuthroized() throws AuthorizationException {
        SecurityUtils.getSubject().checkPermission("system:user:del");
    }
}
