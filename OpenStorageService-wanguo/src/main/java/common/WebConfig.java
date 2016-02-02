package common;

import aop.handler.AddHandler;
import aop.interceptor.LoginInterceptor;
import com.jfinal.config.*;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import controller.*;
import model.FileDesc;
import model.Orders;
import model.Space;

import java.io.File;

/**
 * Created by Tyfunwang on 2015/1/16.
 */
public class WebConfig extends JFinalConfig {
    @Override
    public void configConstant(Constants me) {
        loadPropertyFile("classes" + File.separator + "jdbc.properties");
        me.setViewType(ViewType.JSP);
        me.setDevMode(false);
    }

    @Override
    public void configRoute(Routes me) {
        me.add("/user", UserController.class);
        me.add("/order", OrderController.class);
        me.add("/hdfs", HdfsController.class);
        me.add("/back", FeedController.class);

        me.add("/view", ViewController.class);
        me.add("/account", AccountController.class);
        
        me.add("/filedesc", FileDescController.class);
    }

    @Override
    public void configPlugin(Plugins me) {
        //数据源插件Druid
        DruidPlugin dp = new DruidPlugin(getProperty("jdbcUrl"), getProperty("name"), getProperty("password"));
        me.add(dp);
        //ActiveRecord插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
        me.add(arp);

        //添加映射
        arp.addMapping("space", "id", Space.class);
        arp.addMapping("orders", "order_num", Orders.class);
        arp.addMapping("filedesc", "id", FileDesc.class);
    }

    @Override
    public void configInterceptor(Interceptors me) {
       me.add(new LoginInterceptor());
    }
    @Override
    public void configHandler(Handlers me)
    {
        me.add(new AddHandler());
//        me.add(new TestHandler());
    }

}
