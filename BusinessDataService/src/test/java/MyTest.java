import com.szl.stronguion.utils.EncAndDecByDES;
import com.szl.stronguion.utils.FormatUtils;
import org.junit.Test;

/**
 * Created by 小龙
 * on 15-11-4
 * at 下午2:14.
 */

public class MyTest {
    private  String aaa="123";

    @Test
    public void test01(){
        String total_str="46456456456";
        int  total_lenth=total_str.length();
        String[]  t=new String[total_lenth/3+1];
        String str="";

        if (total_lenth>3){
            for(int i=0;i<total_lenth/3+1;i++){
                int index=(total_lenth-3*(i+1))>=0?(total_lenth-3*(i+1)):0;
                int end=(total_lenth-i*3)>=0?total_lenth-i*3:0;
                t[i]=total_str.substring(index,end);
            }
            for (int i=t.length-1;i>=0;i--){
                if (i>0){
                    str+=t[i]+",";
                }else {
                    str+=t[i];
                }
            }
        }else {
            str=total_str;
        }
        if (total_lenth/3.0==total_lenth/3&&total_lenth>3){
            str=str.substring(1,str.length());
        }
        System.out.println(total_str);
        System.out.println(str);
    }

    @Test
    public void test02(){
        String[] channel={"京东","淘宝","淘宝"};
        String[] goodname={"茅台21","茅台45","郎酒53"};
        StringBuilder sb =new StringBuilder(" ");
        if (channel!=null&&channel.length!=0){
            sb.append(" and (");
            for (int i=0;i<channel.length;i++){
                if (i<channel.length-1){
                    sb.append("(squdao='"+channel[i]+"' and goodsname='"+goodname[i]+"')").append(" or ");
                }else {
                    sb.append(" (squdao='"+channel[i]+"' and goodsname='"+goodname[i]+"' )) ");
                }
            }
        }
        System.out.println("sb="+sb);
    }


    @Test
    public void test03(){
        Double dou = 3000.5333333333;
        dou = (double)Math.round(dou*1000)/1000;
        System.out.println( new EncAndDecByDES().getEncString("admin2"));


        String[] time = new String[2];
        time = FormatUtils.getDateTime(1);
        time = FormatUtils.formatDetail(time);

    }
}
