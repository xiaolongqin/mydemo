import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import model.*;
import org.junit.Before;
import org.junit.Test;
import service.ControllerService;
import service.UserService;
import util.EncAndDecByDES;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by 小龙
 * on 15-5-5
 * at 下午2:31.
 */

public class UserControllerTest {

    UserService userService = new UserService();
    EncAndDecByDES encAndDecByDES = new EncAndDecByDES();
    ControllerService controllerService = new ControllerService();
    @Before
    public void ini(){
        Properties properties=new Properties();
        try {
            properties.load(ResetPassTest.class.getClassLoader().getResourceAsStream("jdbc.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        DruidPlugin dp = new DruidPlugin(properties.getProperty("jdbcUrl"),properties.getProperty("user"), properties.getProperty("password"));
        dp.start();
        ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);

        arp.addMapping("user", "userid", User.class);
        arp.addMapping("admin", "adminid", Admin.class);
        arp.addMapping("t_address", "id", Address.class);
        arp.addMapping("msg", "msgid", Msg.class);
        arp.addMapping("role", "roleid", Role.class);
        arp.addMapping("rolerelation", "relationid", RoleRelation.class);
        arp.addMapping("servicetype", "typeid", ServiceType.class);
        arp.addMapping("service", "serviceid", Service.class);
        arp.addMapping("feedback","feed_id",Feedback.class);
        arp.start();
    }
    /**
     * check email
     */
    @Test
    public  void checkEmail() {
        String email ="676095579@qq.com";
        boolean user = userService.checkEmail(email);
        System.out.println("email是否唯一："+user);
    }

    /**
     * check username
     */
    @Test
    public void checkUsername() {
        String name = "xiaolong";
        boolean user = userService.checkUsername(name);
        System.out.println("name是否唯一："+user);
    }
    /**
     * check realname
     */
    @Test
    public void checkRealname() {
        String realname = "xiaolong";
        boolean user = userService.checkRealname(realname);
        System.out.println("用户真名是否唯一："+user);
    }

    /**
     * check tel
     */
    @Test
    public void checkTel() {
        String tel = "12345678901";
        boolean user = userService.checkTel(tel);
        System.out.println("用户电话号码是否唯一："+user);
    }

    /**
     * check identitycard
     */
    @Test
    public void checkCard() {
        String card = "510121199107295072";
        boolean user = userService.checkCard(card);
        System.out.println("用户身份证号是否唯一："+user);
    }
}
