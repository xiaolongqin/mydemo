package plugin.shiro.annotation;

import org.apache.shiro.authz.annotation.Logical;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by liweiqi on 2014/11/24.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresRoles {
    String[] value();
    Logical logical() default Logical.AND;
}
