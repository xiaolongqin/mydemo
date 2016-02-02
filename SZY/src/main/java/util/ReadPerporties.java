package util;
import java.io.IOException;
import java.util.Properties;
/**
 * Created by Tyfunwang on 2015/1/4.
 */
public class ReadPerporties {
    public static Properties subSys = new Properties();
    static{
        try {
            subSys.load(ReadPerporties.class.getClassLoader().getResourceAsStream("product.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
