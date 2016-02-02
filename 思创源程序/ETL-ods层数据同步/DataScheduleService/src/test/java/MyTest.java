import com.szl.strongunion.bigdata.dss.util.CalendarUtil;
import com.szl.strongunion.bigdata.dss.util.EncAndDecByDES;
import org.junit.Test;

import java.util.Calendar;

/**
 * Created by 小龙
 * on 15-7-30
 * at 下午2:49.
 */

public class MyTest {
    @Test
    public void test01(){
        EncAndDecByDES encAndDecByDES=new EncAndDecByDES();
        String str=encAndDecByDES.getEncString("jdbc:mysql://115.28.104.17:3306/strongunion?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&&autoReconnect=true");
        String str2=encAndDecByDES.getDesString("9358b7b2702ed77f278d38aaf97d49661a52bf8f093594af9c93e2114ab1717e151603b2f6b4b4ec055bb54d5835f154934e5627b897d5e8167e3c16f2bd399330c81a6bd245ad3b4c04ce2dbecaf40d743f85c41a3f33e0b8c29db8783c3b553712106f81e572fda6b844ceb7c1d7ea3546837cf52172f358996a54720542a4");
//        System.out.print("str="+str);
        System.out.print("str="+str2);

    }


    @Test
    public void test02(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        String TABLNEME = "sl_ods_page_visit_record";
        String realTableName = TABLNEME + CalendarUtil.getMonthPostFix();
        System.out.println("str="+realTableName+"----"+month);
        System.out.println("yerar="+(year*100+11));


    }
    @Test
    public void test03(){
        String str1="id,ordernum,shopsid,uid,linkman,address,code,phone,total,goods_total_price,freight_total_price,coupon_total_price," +
                "user_coupon_ids,state,distri,payment,paystate,astocktime,sstocktime,bstocktime,aservicetime,sservicetime,bservicetime," +
                "receive_time,pay_overtime,courierid,content,addtime,suretime,gettime,overtime,revoketime,imeiid,revoke_type,revoke_reason,auth_state,longitude," +
                "latitude,province,city,district,street,current_address,disnum,goodsnum,hname,hphone,is_dis";


        String table_value = "?";
        char c = ',';
        char[] chars = str1.toCharArray();
        for(int i = 0; i < chars.length; i++)
        {
            if(c == chars[i])
            {
                table_value+=",?";
            }
        }

        System.out.println("str="+table_value);


    }
}
