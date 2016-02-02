package util;

import java.io.IOException;
import java.util.Properties;
/**
 * Created by Tyfunwang on 2015/1/27.
 */
public class PropertyUtil {
    public static Properties urls = new Properties();
    public static Properties port = new Properties();
    static {
        try {
            urls.load(PropertyUtil.class.getClassLoader().getResourceAsStream("szy.properties"));
            port.load(PropertyUtil.class.getClassLoader().getResourceAsStream("rpc.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
