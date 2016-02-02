import org.junit.Test;
import sl.bigdata.report.TimeTask;
import sl.bigdata.util.GetMonth;
import sl.bigdata.util.javaMail.SendMail;

import javax.mail.MessagingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tyfunwang on 2015/5/29.
 */
public class TestMethod {
    //@Test
    public void test1() {
        String hello = "hello";
        String sql = "select * from user_" + hello + "";
        System.out.println(sql);

    }
    @Test
    public  void  test2(){
        String moth= GetMonth.getOrtherMonth(-2);
        System.out.println(moth);
    }
    @Test
    public  void  test3(){
        TimeTask.testOds.run();
    }
    @Test
    public  void  test4(){
        Date d=GetMonth.getNextDayTime(420);
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(format.format(d));
        System.out.println(System.currentTimeMillis());
        System.out.println(d.getTime());
    }

    @Test
    public  void  test5(){
        try {
            SendMail.sendMail("qinxiaolong@unionbigdata.com",false,"思创数据同步(ods)出现异常,请维护人员查看服务器错误日志文件,找出问题,并维护!");
//            SendMail.sendMail("274263348@qq.com",false,"数据同步ods层出现问题!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
