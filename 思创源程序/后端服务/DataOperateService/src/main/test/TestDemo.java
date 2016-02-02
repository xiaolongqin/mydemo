import com.alibaba.fastjson.JSONArray;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by Tyfunwang on 2015/6/26.
 */
public class TestDemo {

    public void getMonth() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -3);
        System.out.println(dateFormat.format(calendar.getTime()));
    }
    @Test
    public void returnWho() {
        System.out.println("SELECT SUM(t1.buytimes) as 'buytimes' from \n" +
                "\" +\n" +
                "                \"(select t.goods,count(t.goodsid) as 'buytimes'\\n\" +\n" +
                "                \" from sl_rpt_analysis_shoppingcart_flag t\\n\" +\n" +
                "                \"where FROM_UNIXTIME(t.shoppingcart_addtime, '%Y%m%d') >= '\" + startTime + \"'\\n\" +\n" +
                "                \"  and FROM_UNIXTIME(t.shoppingcart_addtime, '%Y%m%d') <= '\" + endTime + \"'\\n\" +\n" +
                "                \"\\tand t.goodsid_shoppingcart_flag1 = 1\\n\" +\n" +
                "                \"  and t.shopsname = '\" + shopName + \"'\\n\" +\n" +
                "                \" and (t.shoppingcart_addtime is not null or t.shoppingcart_addtime <> 0)\\n\" +\n" +
                "                \" group by t.province, t.city, t.town, t.village, t.address,t.goods\\n\" +\n" +
                "                \"order by t.goods desc LIMIT \" + start + \",\" + end + \") as t1;");

    }

    @Test
    public  void getStrCh(){
        StringBuilder stringBuilder = new StringBuilder();
        String str = "北京市";
        char[] ch = str.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            if (ch[i] == '市' || ch[i] == '省') continue;
            stringBuilder.append(ch[i]);
        }

        System.out.println(stringBuilder.toString());
    }
    public void getDate() {
        System.out.println(System.currentTimeMillis());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis());

    }

    public void getJsonArray() {
        //String json = "[{\"parentid\": \"00\",\"code\": \"12\"}, {\"parentid\": \"12\", \"code\": \"1201\" }]";
        String json = "[\n" +
                "    {\n" +
                "        \"node\": [\n" +
                "            {\n" +
                "                \"id\": \"11101\",\n" +
                "                \"name\": \"node1\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": \"11102\",\n" +
                "                \"name\": \"node2\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"route\": [\n" +
                "            {\n" +
                "                \"id\": \"111\",\n" +
                "                \"name\": \"first route\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "]";
        JSONArray jsonArray = JSONArray.parseArray(json);

        for (Object obj : jsonArray) {

            Map<String, Object> map = (Map<String, Object>) obj;
            Object s1 = map.get("node");
            Object s2 = map.get("route");
            if (s1 != null) {
                //node
                System.out.println(s1 + "node");
                continue;
            }
            //route
            System.out.println(s2 + "route");

        }

    }
}
