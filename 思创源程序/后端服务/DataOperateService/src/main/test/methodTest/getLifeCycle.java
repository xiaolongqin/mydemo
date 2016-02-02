package methodTest;

import com.jfinal.config.Plugins;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.szl.stronguion.model.baseoperate.*;
import com.szl.stronguion.model.customercharacter.LifeCycleUsers;
import com.szl.stronguion.model.customercharacter.ThresholdValue;
import com.szl.stronguion.model.menus.*;
import com.szl.stronguion.service.salesanalysis.ShoppingCartServ;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Tyfunwang on 2015/7/14.
 */
public class getLifeCycle {
    Properties properties = new Properties();

    {
        try {
            properties.load(getLifeCycle.class.getClassLoader().getResourceAsStream("jdbc.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Before
    public void start() {
        Plugins me = new Plugins();
        /**
         * * strongunion_web
         * */
        DruidPlugin dp1 = new DruidPlugin(properties.getProperty("jdbcUrl1"), properties.getProperty("name1"), properties.getProperty("password1"));
        ActiveRecordPlugin arp1 = new ActiveRecordPlugin("main1", dp1);
        dp1.start();
        arp1.addMapping("account", Account.class);
       // arp1.addMapping("report", Report.class);
        arp1.addMapping("notice", Notice.class);
        arp1.addMapping("roles", Roles.class);
        arp1.addMapping("function_module", Module.class);
        arp1.addMapping("role_function_module", RoleModule.class);
        //阀值配置
        arp1.addMapping("threshold_value", ThresholdValue.class);
        arp1.start();
        /**
         * strongunion_online 
         * */
        DruidPlugin dp2 = new DruidPlugin(properties.getProperty("jdbcUrl2"), properties.getProperty("name2"), properties.getProperty("password2"));
        dp2.start();
        ActiveRecordPlugin arp2 = new ActiveRecordPlugin("main2", dp2);
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
        arp2.start();
    }

    @Test
    public void testAddCountForModule(){
        new Module().addCount(13);
        
    }
    public void testShoppingCart(){
        ShoppingCartServ serv =  new ShoppingCartServ();
       // List<Record> list =  serv.getGeneralAnalysis("20140101", "20150909", "0");
      //  System.out.println(JsonKit.toJson(list));
        
    }
//    public void testFlagServ() {
//        CustomerPortraitServ portraitServ = new CustomerPortraitServ();
//        Map<String, Object> maps= portraitServ.getCustPortrait("沧海一粟", "1");
//        System.out.println(JsonKit.toJson(maps));
//    }

    public void getTotalByTime() {
        /**
         * @Param startTime 前几天的时间*
         *  @Param endTime 结束的时间*
         */
        LifeCycleUsers cycleUsers = new LifeCycleUsers();
        String startTime = "1436544000";
        String endTime = "1436716800";
    }

    public void getTotalByAge() {
        LifeCycleUsers cycleUsers = new LifeCycleUsers();
        String startTime = "1436544000";
        String endTime = "1436716800";
    }
}
