package plugin.shiro;

import com.jfinal.config.Routes;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.plugin.IPlugin;
import plugin.shiro.annotation.ClearAuthHandler;
import plugin.shiro.annotation.RequireAuthentication;
import plugin.shiro.annotation.RequiresPermissions;
import plugin.shiro.annotation.RequiresRoles;
import plugin.shiro.handler.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;


/**
 * Shiro插件，启动时加载所有Shiro访问控制注解。
 * @author Tyfunwang
 *
 */
public class ShiroPlugin  implements IPlugin {
    private String SLASH = "/";
    //路由设定
    private Routes routes;
    /**Shiro的几种访问控制注解
     * */
    private static Class<? extends Annotation>[] SHIROPRI = new Class[]{RequiresRoles.class, RequireAuthentication.class, RequiresPermissions.class};
    /**
     * 构造函数
     * @param routes 路由设定
     */
    public ShiroPlugin(Routes routes) {
        this.routes = routes;
    }

    @Override
    public boolean start() {
        //获取所有路由设定。这里修改了JFinal类，增加了一个getRoutes方法。
        //Routes routes = JFinal.me().getRoutes();
        Set<Map.Entry<String, Class<? extends Controller>>> sets = routes.getEntrySet();
        Set<String> excludedMethods = getExcludedMethod();
        //逐个访问所有注册的Controller，解析Controller及action上的所有Shiro注解。
        //并依据这些注解，actionKey提前构建好权限检查处理器。
        for (Map.Entry<String, Class<? extends Controller>> entry : sets) {

            Class<? extends Controller> controllerClass = entry.getValue();


            // 获取Controller的所有Shiro注解。
            List<Annotation> cAnnotation = getCAuthAnnotation(controllerClass);
            String controllerKey = entry.getKey();
            //遍历
            Method[] methods = controllerClass.getMethods();
            for (Method method : methods) {

                //排除掉Controller基类的所有方法，并且只关注没有参数的Action方法。
                if ((!excludedMethods.contains(method.getName())) && method.getParameterTypes().length == 0) {
                    //若该方法上存在ClearShiro注解，则对该action不进行访问控制检查。
                    if (isAuthClearMethod(method)) continue;
                    //获取方法的所有Shiro注解。
                    List<Annotation> mAnnotation = getMAuthAnnotation(method);
                    //依据Controller的注解和方法的注解来生成访问控制处理器。
                    AuthHandler handler = createAuthHandler(cAnnotation, mAnnotation);
                    //生成访问控制处理器成功。
                    if (handler != null) {
                        //构建ActionKey，参考ActionMapping中实现
                        String actionKey = createActionKey(controllerClass, method, controllerKey);
                        //添加映射
                        ShiroKit.addHandler(actionKey, handler);
                    }
                }
            }
        }
        //注入到ShiroKit类中。ShiroKit类以单例模式运行。
       // ShiroKit.init(authzMaps);
        return true;
    }

    /**
     * create AuthHandler by controller's  and methods's annotation
     */
    private String createActionKey(Class<? extends Controller> controllerClass,
                                   Method method, String controllerKey) {
        String methodName = method.getName();
        String actionKey = "";
        ActionKey ak = method.getAnnotation(ActionKey.class);
        if (ak != null) {
            actionKey = ak.value().trim();
            if ("".equals(actionKey))
                throw new IllegalArgumentException(controllerClass.getName() + "." + methodName + "(): The argument of ActionKey can not be blank.");
            if (!actionKey.startsWith(SLASH))
                actionKey = SLASH + actionKey;
        } else if (methodName.equals("index")) {
            actionKey = controllerKey;
        } else {
            actionKey = controllerKey.equals(SLASH) ? SLASH + methodName : controllerKey + SLASH + methodName;
        }
        return actionKey;

    }

    /**
     * create AuthHandler by controller's  and methods's annotation
     */
    /**
     * 依据Controller的注解和方法的注解来生成访问控制处理器。
     * @param cAnnotation  Controller的注解
     * @param mAnnotation 方法的注解
     * @return 访问控制处理器
     */
    private AuthHandler createAuthHandler(List<Annotation> cAnnotation, List<Annotation> mAnnotation) {
        //没有注解
        if (cAnnotation.size() == 0 && mAnnotation.size() == 0) return null;
        //至少有一个注解
        List<AuthHandler> handlers = new ArrayList<AuthHandler>();
        for (int index = 0; index < 3; index++) {
            handlers.add(null);
        }
        // 逐个扫描注解，若是相应的注解则在相应的位置赋值。
        getHandlerFromAnnotation(cAnnotation, handlers);
        // 逐个扫描注解，若是相应的注解则在相应的位置赋值。函数的注解优先级高于Controller
        getHandlerFromAnnotation(mAnnotation, handlers);
        // 去除空值
        List<AuthHandler> finalAuthzHandlers = new ArrayList<AuthHandler>();
        for (AuthHandler a : handlers) {
            if (a != null) {
                finalAuthzHandlers.add(a);
            }
        }
        if (finalAuthzHandlers.size() < 1) {
            return null;
        }
        if (finalAuthzHandlers.size() > 1) {
            return new CompositeHandler(finalAuthzHandlers);
        }
        // 一个的话直接返回
        return finalAuthzHandlers.get(0);
    }
    /**
     * 逐个扫描注解，若是相应的注解则在相应的位置赋值。
     * 注解的处理是有顺序的，依次为RequiresRoles,RequiresPermissions, RequiresAuthentication
     *
     * @param cAnnotation
     * @param handlers
     */
    private void getHandlerFromAnnotation(List<Annotation> cAnnotation, List<AuthHandler> handlers) {
        if (null == cAnnotation || 0 == cAnnotation.size()) {
            return;
        }
        for (Annotation annotation : cAnnotation) {
            if (annotation instanceof RequiresRoles) {
                handlers.set(0, new RoleAuthHandler(annotation));
            } else if (annotation instanceof RequiresPermissions) {
                handlers.set(1, new PermissionAuthHandler(annotation));
            } else if (annotation instanceof RequireAuthentication) {
                handlers.set(2, AuthenticatedHandler.me());
            }
        }
    }

    /**
     * get annotation from controller
     */

    private List<Annotation> getCAuthAnnotation(Class<? extends Controller> controllerClass) {
        List<Annotation> annotations = new ArrayList<Annotation>();
        for (Class<? extends Annotation> authAnnotation : SHIROPRI) {
            Annotation a = controllerClass.getAnnotation(authAnnotation);
            if (a != null) annotations.add(a);
        }
        return annotations;
    }


    /**
     * get annotation from method
     */
    private List<Annotation> getMAuthAnnotation(Method method) {
        List<Annotation> annotations = new ArrayList<Annotation>();
        for (Class<? extends Annotation> authAnnotation : SHIROPRI) {
            Annotation a = method.getAnnotation(authAnnotation);
            if (a != null) annotations.add(a);
        }
        return annotations;
    }
    /**
     * 该方法上是否有ClearShiro注解
     * @param method
     * @return boolean
     */
    private boolean isAuthClearMethod(Method method) {
        return method.getAnnotation(ClearAuthHandler.class) != null;
    }

    /**
     * exclude the methods from base class which  conflict with sub_class's controller method
     */
    /**
     * 从Controller方法中构建出需要排除的方法列表
     * @return Set
     */
    private Set<String> getExcludedMethod() {
        Set<String> excludedMethods = new HashSet<String>();
        Method[] methods = Controller.class.getMethods();
        for (Method method : methods) {
            if (method.getParameterTypes().length == 0) {
                excludedMethods.add(method.getName());
            }
        }
        return excludedMethods;
    }

    @Override
    public boolean stop() {
        return true;
    }
}
