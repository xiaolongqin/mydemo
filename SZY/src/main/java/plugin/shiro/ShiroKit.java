package plugin.shiro;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plugin.shiro.handler.AuthHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liweiqi on 2014/11/24.
 */

/**
 * 构建一个ShiroKit辅助类
 */
public class ShiroKit {
    //log
    private static final Logger logger = LoggerFactory.getLogger(ShiroKit.class);
    /**
     * 用来记录那个action或者actionpath中是否有shiro认证注解。
     */
    private static final Map<String, AuthHandler> HANDLERS = new HashMap<String, AuthHandler>();
    /**
     * 禁止初始化
     */
    private ShiroKit() {

    }

    public static AuthHandler getHandle(String key) {
        return HANDLERS.get(key);
    }

    public static boolean addHandler(String key, AuthHandler handler) {
        if (HANDLERS.containsKey(key) && handler == null && key == null) {
            logger.error("error while init ShiroPlugin when add handlers from aop");
            return false;
        }
        HANDLERS.put(key, handler);
        return true;
    }
    /**
     * 获取 Subject
     *
     * @return Subject
     */
    protected static org.apache.shiro.subject.Subject getSubject() {
        return SecurityUtils.getSubject();
    }








    /**
     * 验证当前用户是否属于该角色？,使用时与lacksRole 搭配使用
     *
     * @param roleName  角色名
     * @return 属于该角色：true，否则false
     */
    public static boolean hasRole(String roleName) {
        return getSubject() != null && roleName != null
                && roleName.length() > 0 && getSubject().hasRole(roleName);
    }
    /**
     * 与hasRole标签逻辑相反，当用户不属于该角色时验证通过。
     *
     * @param roleName  角色名
     * @return 不属于该角色：true，否则false
     */
    public static boolean lacksRole(String roleName) {
        return !hasRole(roleName);
    }



    /**
     * 验证当前用户是否拥有指定权限,使用时与lacksPermission 搭配使用
     *
     * @param permission  权限名
     * @return 拥有权限：true，否则false
     */
    public static boolean hasPermission(String permission) {
        return getSubject() != null && permission != null
                && permission.length() > 0
                && getSubject().isPermitted(permission);
    }

    /**
     * 与hasPermission标签逻辑相反，当前用户没有制定权限时，验证通过。
     *
     * @param permission 权限名
     * @return 拥有权限：true，否则false
     */
    public static boolean lacksPermission(String permission) {
        return !hasPermission(permission);
    }



}
