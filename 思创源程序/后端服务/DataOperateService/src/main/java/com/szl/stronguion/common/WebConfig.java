package com.szl.stronguion.common;

import com.jfinal.config.*;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.szl.stronguion.aop.handler.AddHandler;
import com.szl.stronguion.controller.baseoperate.*;
import com.szl.stronguion.controller.customercharacter.CustomerPortraitController;
import com.szl.stronguion.controller.customercharacter.LifeCycleController;
import com.szl.stronguion.controller.customercharacter.ThresholdController;
import com.szl.stronguion.controller.menus.*;
import com.szl.stronguion.controller.pagecontent.AccessPathController;
import com.szl.stronguion.controller.pagecontent.KeyWordsController;
import com.szl.stronguion.controller.salesanalysis.OrderAnalysisController;
import com.szl.stronguion.controller.salesanalysis.RepeatOrderController;
import com.szl.stronguion.controller.salesanalysis.ShoppingCartController;
import com.szl.stronguion.controller.salesanalysis.StoreAnalysisController;
import com.szl.stronguion.model.baseoperate.*;
import com.szl.stronguion.model.customercharacter.*;
import com.szl.stronguion.model.customercharacter.customerportrait.*;
import com.szl.stronguion.model.menus.*;
import com.szl.stronguion.model.pagecontent.KeyWordsSearch;
import com.szl.stronguion.model.pagecontent.Temp0VisitRoute;
import com.szl.stronguion.model.salesanalysis.AnalysisOrder;
import com.szl.stronguion.model.salesanalysis.AnalysisOrderFlag;
import com.szl.stronguion.model.salesanalysis.ShoppingCartFlag;
import com.szl.stronguion.model.salesanalysis.ShoppingCartOrder;


/**
 * Created by Tyfunwang on 2015/5/22.
 */
public class WebConfig extends JFinalConfig {
    @Override
    public void configConstant(Constants me) {
        loadPropertyFile("jdbc.properties");
//        me.setDevMode(true);
        me.setDevMode(false);
        me.setViewType(ViewType.JSP);
    }

    @Override
    public void configRoute(Routes me) {
        //route for menus
        me.add("/account", AccountController.class);
        me.add("/notice", NoticeController.class);
        me.add("/module", ModuleController.class);
        me.add("/roles", RolesController.class);
        me.add("/rightMenus", RightMenusController.class);

        //route for base operate report
        me.add("/page", PageController.class);
        me.add("/pageaction", PageActionController.class);
        me.add("/monitor", OperationMonitorController.class);
        me.add("/shopsales", ShopSalesController.class);
        me.add("/keyroute", KeyRouteController.class);

        //route for threshold value 
        me.add("/threshold", ThresholdController.class);

        //route for customer portrait 
        me.add("/customerportrait", CustomerPortraitController.class);
        //route for life cycle
        me.add("/lifecycle", LifeCycleController.class);

        //route for page content
        me.add("/accesspath", AccessPathController.class);
        me.add("/keyword", KeyWordsController.class);

        //route for sales analysis
        me.add("/orderanalysis", OrderAnalysisController.class);
        me.add("/shoppingcart", ShoppingCartController.class);
        me.add("/repeatorder", RepeatOrderController.class);
        me.add("/storeanalysis", StoreAnalysisController.class);

    }

    @Override
    public void configPlugin(Plugins me) {
        /**
         * * strongunion_web
         * */
        DruidPlugin dp1 = new DruidPlugin(getProperty("jdbcUrl1"), getProperty("name1"), getProperty("password1"));
        me.add(dp1);
        ActiveRecordPlugin arp1 = new ActiveRecordPlugin("main1", dp1);
        me.add(arp1);
        arp1.addMapping("account", Account.class);
        arp1.addMapping("notice", Notice.class);
        arp1.addMapping("roles", Roles.class);
        arp1.addMapping("function_module", Module.class);
        arp1.addMapping("role_function_module", RoleModule.class);
        //阀值配置
        arp1.addMapping("threshold_value", ThresholdValue.class);

        /**
         * strongunion_online 
         * */
        DruidPlugin dp2 = new DruidPlugin(getProperty("jdbcUrl2"), getProperty("name2"), getProperty("password2"));
        me.add(dp2);
        ActiveRecordPlugin arp2 = new ActiveRecordPlugin("main2", dp2);
        me.add(arp2);
        //渠道
        arp2.addMapping("sl_rpt_app_channel_aim1", AppChannelAim1.class);
        arp2.addMapping("sl_rpt_app_channel_aim2", AppChannelAim2.class);
        arp2.addMapping("sl_rpt_app_page_aim1", AppPageAim1.class);
        //页面
        arp2.addMapping("sl_ods_page", "page_id", Page.class);
        arp2.addMapping("sl_ods_page_action", "page_action_id", PageAction.class);
        arp2.addMapping("sl_rpt_oder_any_aim1", OrderAim1.class);
        //关键路径
        arp2.addMapping("sl_rpt_key_route", KeyRoute.class);
        arp2.addMapping("sl_rpt_key_node", KeyNode.class);
        arp2.addMapping("sl_rpt_rela_route_node", RelaRouteNode.class);
        //生命周期分析计算
        arp2.addMapping("sl_rpt_life_cycle_users", LifeCycleUsers.class);
        arp2.addMapping("sl_rpt_life_cycle_users_consume", LifeCycleConsume.class);
        arp2.addMapping("sl_rpt_life_cycle_users_flag", LifeCycleUsersFlag.class);
        arp2.addMapping("sl_rpt_life_cycle_all_users_flag", AllCycleUsersFlag.class);
        //页面内容分析
        arp2.addMapping("sl_rpt_app_page_visit", Temp0VisitRoute.class);
        arp2.addMapping("sl_ods_keywords_search", KeyWordsSearch.class);
        arp2.addMapping("sl_rpt_user_portrait_tfidf", UserPortraitTfidf.class);

        //销售分析
        arp2.addMapping("sl_rpt_analysis_order_flag", AnalysisOrderFlag.class);
        arp2.addMapping("sl_rpt_analysis_order", AnalysisOrder.class);
        arp2.addMapping("sl_rpt_analysis_shoppingcart_order", ShoppingCartOrder.class);
        arp2.addMapping("sl_rpt_analysis_shoppingcart_flag", ShoppingCartFlag.class);

        arp2.addMapping("sl_ods_attention_goods", AttentionGoods.class);
        arp2.addMapping("sl_ods_attention", Attention.class);
        arp2.addMapping("sl_ods_shops", Shops.class);
        arp2.addMapping("sl_ods_goods", Goods.class);
    }

    @Override
    public void configInterceptor(Interceptors me) {
//        me.add(new AddInter());
//        me.add(new CheckIndexInter());
    }

    @Override
    public void configHandler(Handlers me) {
        me.add(new AddHandler());
    }
}
