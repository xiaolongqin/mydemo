package plugin.shiro.annotation;

import java.lang.annotation.*;

/**
 * Created by liweiqi on 2014/11/24.
 */

/**
 * 用来清除所有的Shiro访问控制注解，适合于Controller绝大部分方法都需要做访问控制，个别不需要做访问控制的场合。
 * 仅能用在方法上。
 * @author Tyfunwang
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ClearAuthHandler {
}
