package plugin.shiro.handler;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.Logical;
import plugin.shiro.annotation.RequiresRoles;

import java.lang.annotation.Annotation;

/**
 * Created by liweiqi on 2014/11/24.
 */

/**
 * 基于角色的访问控制处理器，非单例模式运行。
 *
 * @author Tyfunwang
 */
public class RoleAuthHandler extends AbstractAuthHandler {
    private final Annotation annotation;

    public RoleAuthHandler(Annotation annotation) {
        this.annotation = annotation;
    }

    @Override
    public void assertAuthroized() throws AuthorizationException {
        //断定角色
        if (annotation instanceof RequiresRoles) {

            Logical logical = ((RequiresRoles) annotation).logical();
            String[] values = ((RequiresRoles) annotation).value();

            if (logical.equals(Logical.AND)) {
                //and
                for (String value1 : values) {
                    if (!SecurityUtils.getSubject().hasRole(value1)) throw new AuthorizationException();;
                }
            } else if (logical.equals(Logical.OR)) {
                //or
                for (String value2 : values) {
                    if (SecurityUtils.getSubject().hasRole(value2)) return;
                }
                throw new AuthorizationException();
            }
        }
    }
}
