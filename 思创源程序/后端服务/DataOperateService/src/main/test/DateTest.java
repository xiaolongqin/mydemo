import com.szl.stronguion.utils.FormatUtils;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Tyfunwang on 2015/7/13.
 */
public class DateTest {
    @Test
    public void string2Date() {
        int i = 9;
        String num = "num";
        System.out.println("num" + i + "");
        System.out.println("当天" + System.currentTimeMillis());
        System.out.println("昨天" + FormatUtils.getDayDate(-1));
        System.out.println("前天" + FormatUtils.getDayDate(-2));
        System.out.println("最近三天" + FormatUtils.getDayDate(-5));
        System.out.println("开始(系统前一天)" + FormatUtils.getDay(-1));
        System.out.println("前4天" + FormatUtils.getDay(-4));
        System.out.println("系统时间" + FormatUtils.getDay(-5));
        System.out.println("sql to java timestamp:" + FormatUtils.hour2String(1436803200));
//        System.out.println("最近7天" + FormatUtils.string2Long(FormatUtils.getDay(-7)));
//        System.out.println("最近30天" + FormatUtils.string2Long(FormatUtils.getDay(-30)));
//        System.out.println("上季度" + FormatUtils.string2Long(FormatUtils.getDay(-90)));
    }


    @Test
    public void test1(){

        DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        String a =dateFormat.format(calendar.getTime());

        System.out.println("a=" + a);

    }
    @Test
    public void test2(){
        String strstart="20150907";
        String strend="20150907";
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            Date date_start = dateFormat.parse(strstart);
            Date date_end = dateFormat.parse(strend);
            int i=(int)((date_end.getTime()-date_start.getTime())/(1000 * 60 * 60 * 24)+1 );
            System.out.println("dayCount="+(int)((date_end.getTime()-date_start.getTime())/(1000 * 60 * 60 * 24)+1 ));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void test3(){

        List a=new ArrayList<String>();
        a.add("haha");
        a.add("wawa");
        a.add("xixi");
        a.add("gg");
        System.out.println("a=" + a);
        Collections.reverse(a);
        System.out.println("a=" + a);
       String str= "{\"商品名称\":\"优加防辐射服 孕妇防辐射马甲F006粉 ...\"," +
               "\"商品编号\":\"1000023400\"," +
               "\"店铺\":\"优加旗舰店\"," +
               "\"上架时间：2013-01-18 15\":\"34:47\"," +
               "\"商品毛重\":\"500.00g\",\"商品产地\":\"中国大陆\"," +
               "\"尺码\":\"L，XL\",\"色系\":\"粉色\",\"材质\":\"金属纤维\"," +
               "\"季节\":\"四季通用\",\"分类\":\"马甲\",\"经营模式\":\"第三方运营\"}";
//        Map<String,Objects> map=new Map<String,Objects>();
//        Record record=new Record().setColumns(map);
    }

    @Test
    public void test4(){
        String strstart="20150907";
        String strend="20150908";
        int dayCount=FormatUtils.getDayCount(strstart,strend);
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        int i=-2+1;
        try {
            Date date_start = dateFormat.parse(strstart);
            Date date_end = dateFormat.parse(strend);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date_end);
            calendar.add(Calendar.DAY_OF_YEAR, i);
            String theday=dateFormat.format(calendar.getTime());
            System.out.println("theday="+theday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
