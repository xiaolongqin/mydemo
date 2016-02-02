package common;

import com.jfinal.config.*;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import controller.*;
import controller.forall.FeedbackController;
import controller.oss.OssController;
import controller.oss.OssViewCon;
import handler.AddHandler;
import interceptor.ShiroInterceptor;
import model.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;
import plugin.shiro.ShiroPlugin;

import java.io.File;

/**
 * Created by Administrator on 2014/11/6.
 */
public class WebConfig extends JFinalConfig {
    /**
     * 供Shrio插件使用
     */
    private Routes route;

    @Override
    public void configConstant(Constants constants) {
        loadPropertyFile("classes" + File.separator + "jdbc.properties");
        constants.setViewType(ViewType.JSP);
        constants.setEncoding("UTF-8");
        constants.setDevMode(true);
    }

    @Override
    public void configRoute(Routes routes) {
        this.route = routes;
//        routes.add("/", AdminController.class);
        routes.add("/user", UserController.class);
        routes.add("/admin", AdminController.class);
        routes.add("/msg", MsgController.class);
        routes.add("/role", RoleController.class);

        /**
         *    others
         */
        //getInfo
        routes.add("/getInfo", GetInfoController.class);
        //get randompwd
        routes.add("/util", UtilController.class);
        //验证码
        routes.add("/captcha", CaptchaController.class);
        //get address
        routes.add("/address", AddressController.class);

        //for services
        routes.add("/rds", RdsController.class);
        routes.add("/oss", OssController.class);
        //view
        routes.add("/view", ViewController.class);
        routes.add("/ossView", OssViewCon.class);
        routes.add("/ad", AdController.class);
        //feedback
        routes.add("/back", FeedbackController.class);
    }

    @Override
    public void configPlugin(Plugins plugins) {
        //add ehcache
        //plugins.add(new EhCachePlugin(ClassLoader.getSystemResourceAsStream("ehcache.xml")));

        //数据源插件Druid
        DruidPlugin dp = new DruidPlugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password"));
        plugins.add(dp);

        //ActiveRecord插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
        plugins.add(arp);

        //加载Shrio插件
        plugins.add(new ShiroPlugin(route));

        //添加映射
        arp.addMapping("user", "userid", User.class);
        arp.addMapping("admin", "adminid", Admin.class);
        arp.addMapping("t_address", "id", Address.class);
        arp.addMapping("msg", "msgid", Msg.class);
        arp.addMapping("role", "roleid", Role.class);
        arp.addMapping("rolerelation", "relationid", RoleRelation.class);
        arp.addMapping("servicetype", "typeid", ServiceType.class);
        arp.addMapping("service", "serviceid", Service.class);
        arp.addMapping("feedback","feed_id",Feedback.class);

    }

    @Override
    public void configInterceptor(Interceptors interceptors) {
        interceptors.add(new ShiroInterceptor());
    }

    @Override
    public void configHandler(Handlers handlers) {

        handlers.add(new AddHandler());
//       handlers.add(new TestHandler());
    }

    @Override
    public void beforeJFinalStop() {


    }

    @Override
    public void afterJFinalStart() {
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityUtils.setSecurityManager(factory.getInstance());

    }
}
