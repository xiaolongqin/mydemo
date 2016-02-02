import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.ControllerService;
import service.UserService;
import util.EncAndDecByDES;
import util.JsonHelp;
import util.javaMail.SendMail;
import util.javaMail.SetParameter;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by 小龙
 * on 15-5-4
 * at 上午9:45.
 */

public class ResetPassTest {
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
    @After
    public void endM(){


    }
    @Test
    public void sendResetPassMail() {
        String identitycard="510121199107295071";
        String email="676095579@qq.com";
        if(userService.checkEmailAndCard(email,identitycard)){
            String emailmi=encAndDecByDES.getEncString(email);
            String timemi=encAndDecByDES.getEncString(String.valueOf(System.currentTimeMillis()));
            String url=new SetParameter().getProperties().getProperty("url");
            System.out.println("邮箱正确,发送邮件。。。");
            try {
                SendMail.sendresetPassMail(email, true, "数之云找回密码邮件！本邮件24小时内有效，找回密码请点击:" + url + "resetPass?email=" + emailmi + "&time=" + timemi);
            System.out.println("发送邮件成功");
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }else{

            System.out.println("邮箱和身份证号匹配有误");
        }
    }
    @Test
    public void checkEmailAndCard() {
        String identitycard="510121199107295071";
        String email="676095579@qq.com";
        System.out.println(User.dao.checkEmailAndCard(email, identitycard));

    }
    @Test
    public void resetPass() {
        String time=encAndDecByDES.getDesString("39018bdf201d650f0b5323be58ae8082");
        String email = encAndDecByDES.getDesString("f40c5a4edb2fe527fe145bd486726904dcff873245a8ae6e");

        if ((System.currentTimeMillis()-Long.valueOf(time).longValue()<24*60*60*1000)&&(!userService.checkEmail(email))){
            System.out.println("邮箱和时间有效");
         //   getSession().setAttribute("reset_email",email);
           // render("/view/html/reset.html?email="+email);
        }else {
            System.out.println("邮箱和时间无效");
            //renderJson(JsonHelp.buildFailed());
        }
    }
    @Test
    public void sureResetPass() {
        String email ="676095579@qq.com";
        String newPwd="123456";
        System.out.println("email="+email);
        if (email==null||"".equals(email)){
            System.out.println("email=null");
            return;
        }
        String newPwdmi=encAndDecByDES.getEncString(newPwd);
        boolean user=userService.resetPwd(email,newPwdmi);
        System.out.println(user ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());

    }


}
