package common;

import aop.handler.LoginHandler;
import com.jfinal.config.*;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import controller.*;
import model.local.DBUserL;
import model.local.DataBaseL;
import model.local.SpaceL;
import service.DSManager;

import java.io.File;

/**
 * Created by Administrator on 2014/10/15.
 */
public class MainConfig extends JFinalConfig {
    @Override
    public void configConstant(Constants me) {
        me.setDevMode(false);
        me.setViewType(ViewType.JSP);
        String path = PathKit.getRootClassPath();

        loadPropertyFile("classes" + File.separator + "jdbc.properties");
    }

    @Override
    public void configRoute(Routes me) {
        me.add("/", MainController.class);
        me.add("/space", SpaceController.class);
        me.add("/db", DataBaseController.class);
        me.add("/captcha", CaptchaController.class);
        me.add("/user", DBUserController.class);
    }

    @Override
    public void configPlugin(Plugins me) {
        DruidPlugin local = new DruidPlugin(getProperty("local_url"), getProperty("local_user"), getProperty("local_password"));
        ActiveRecordPlugin local_arp = new ActiveRecordPlugin(DSManager.LOCAL, local);
        DruidPlugin remote = new DruidPlugin(getProperty("remote_url"), getProperty("remote_user"), getProperty("remote_password"));
        ActiveRecordPlugin remote_arp = new ActiveRecordPlugin(DSManager.REMOTE, remote);

        local_arp.addMapping(DataBaseL.TABLE, DataBaseL.class);
        local_arp.addMapping(DBUserL.TABLE, DBUserL.class);
        local_arp.addMapping(SpaceL.TABLE, SpaceL.class);

        local.set(1, 1, 3);
        local.setValidationQuery("select 1");
        local.setTimeBetweenEvictionRunsMillis(3 * 1000L);
        remote.set(1, 1, 3);
        remote.setTimeBetweenEvictionRunsMillis(3 * 1000L);
        remote.setValidationQuery("select 1");

        me.add(local);
        me.add(local_arp);
        me.add(remote);
        me.add(remote_arp);
    }

    @Override
    public void configInterceptor(Interceptors me) {

    }

    @Override
    public void configHandler(Handlers me) {

        me.add(new LoginHandler());
        //me.add(new TestHandler());
    }

    @Override
    public void afterJFinalStart() {

    }

}
