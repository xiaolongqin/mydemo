package com.szl.stronguion.common;

import com.jfinal.config.*;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.szl.stronguion.aop.handler.AddHandler;
import com.szl.stronguion.aop.interceptor.AddInter;
import com.szl.stronguion.controller.channel.ChannleController;
import com.szl.stronguion.controller.electrial.ElectrialController;
import com.szl.stronguion.controller.electrial.ElectrialSecondController;
import com.szl.stronguion.controller.goodsAnalysis.GoodsAnalysisController;
import com.szl.stronguion.controller.goodsAnalysis.GoodsDetailController;
import com.szl.stronguion.controller.goodsbrandAnalysis.GoodsbrandController;
import com.szl.stronguion.controller.goodsbrandAnalysis.GoodsbrandSecondController;
import com.szl.stronguion.controller.menus.AccountController;
import com.szl.stronguion.controller.menus.FeedController;
import com.szl.stronguion.controller.menus.IndexController;
import com.szl.stronguion.controller.menus.NoticeController;
import com.szl.stronguion.controller.storeAnalysis.StoreAnalysisController;
import com.szl.stronguion.model.electrial.AllSaleGood;
import com.szl.stronguion.model.menus.Account;
import com.szl.stronguion.model.menus.FeedBack;
import com.szl.stronguion.model.menus.Module;
import com.szl.stronguion.model.menus.Notice;


/**
 * Created by 小龙
 * on 15-10-15
 * at 下午4:28.
 */
public class WebConfig extends JFinalConfig {
    @Override
    public void configConstant(Constants me) {
        loadPropertyFile("jdbc.properties");
        me.setDevMode(true);
//        me.setDevMode(false);
        me.setViewType(ViewType.JSP);
    }

    @Override
    public void configRoute(Routes me) {
        //route for path Interceptor
        me.add("/", IndexController.class);
        me.add("/view", IndexController.class);
        me.add("/view/html", IndexController.class);
        //route for menus
        me.add("/account", AccountController.class);
        me.add("/notice", NoticeController.class);
        me.add("/feed", FeedController.class);
        //route for electrial
        me.add("/electrial", ElectrialController.class);
        me.add("/electrialSecond", ElectrialSecondController.class);
//        me.add("/electrialThird", ElectrialThirdController.class);
        //route for channel
        me.add("/channel", ChannleController.class);
        //route for goodsAnalysis
        me.add("/goodsAnalysis", GoodsAnalysisController.class);
        me.add("/goodsDetail", GoodsDetailController.class);

        //route for StoreAnalysis
        me.add("/storeAnalysis", StoreAnalysisController.class);
        //route for goodsbrand
        me.add("/goodsbrand", GoodsbrandController.class);
        me.add("/goodsbrandSecond", GoodsbrandSecondController.class);


        //test for me
//        me.add("/yanz", CaptchaController.class);

    }

    @Override
    public void configPlugin(Plugins me) {
        /**
         * * Electricb_web
         * */
        DruidPlugin dp1 = new DruidPlugin(getProperty("jdbcUrl1"), getProperty("name1"), getProperty("password1"));
        me.add(dp1);
        ActiveRecordPlugin arp1 = new ActiveRecordPlugin("main1", dp1);
        me.add(arp1);
        arp1.addMapping("eb_web_account", Account.class);
        arp1.addMapping("eb_web_feedback", FeedBack.class);
        arp1.addMapping("eb_web_function_module", Module.class);
        arp1.addMapping("eb_web_notice", Notice.class);

        /**
         * Electricb_service
         * */
        DruidPlugin dp2 = new DruidPlugin(getProperty("jdbcUrl2"), getProperty("name2"), getProperty("password2"));
        me.add(dp2);
        ActiveRecordPlugin arp2 = new ActiveRecordPlugin("main2", dp2);
        me.add(arp2);
        //电商概览
        arp2.addMapping("eb_mod_all_sales_goods", AllSaleGood.class);
    }

    @Override
    public void configInterceptor(Interceptors me) {
        me.add(new AddInter());
//        me.add(new LoginInterceptor());

    }
    @Override
    public void configHandler(Handlers me) {
        me.add(new AddHandler());
    }
}
