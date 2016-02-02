package methodTest;

import com.szl.stronguion.utils.FormatUtils;
import org.junit.Test;

/**
 * Created by Tyfunwang on 2015/7/14.
 */
public class dataTest {
    @Test
    public void subString() {
        StringBuilder ssb = new StringBuilder();
        String str = "For input String : \"page_my\"";
        for (int i = 0; i < str.length(); i++) {

        }
        for (Character character : str.toCharArray()) {
            if (character.toString().equals("\"") || character.toString().equals("\'")) continue;
            ssb.append(character);
        }

        System.out.println(ssb.toString());
    }

    public void testTimeToString() {
        System.out.println(FormatUtils.time2String(1438584665));
//        System.out.println(System.currentTimeMillis());

    }

    public void testArray() {
        String[][] str = {{"18"}, {"18", "24"}, {"25", "29"}, {"30", "34"}, {"35", "39"}, {"40", "49"}, {"50", "59"}, {"60"}};
        for (int i = 0; i < str.length; i++) {
            String[] s1 = str[i];
            if (s1.length == 1) {
                String old = s1[0];
                if (old.equals("18")) {
                    //18以下

                }
                //60 及以上
            }
            if (s1.length == 2) {
                String old1 = s1[0];
                String old2 = s1[1];
                System.out.println(old1 + " old1 and old2 " + old2);
            }


        }

    }
}
