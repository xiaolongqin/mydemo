package util.param;

import com.google.common.base.Optional;

import java.util.Map;

/**
 * Created by Administrator on 2014/9/24.
 */
public class TransmitUtil {
    public static TransmitFactory toInteger = new TransmitFactory() {
        @Override
        public Optional<Object> toObject(String[] strings) {
            try {
                return Optional.of((Object) Integer.valueOf(strings[0]));
            } catch (Exception ex) {
                ex.printStackTrace();
                return Optional.absent();
            }
        }
    };
    public static TransmitFactory toLong = new TransmitFactory() {
        @Override
        public Optional<Object> toObject(String[] strings) {
            try {
                return Optional.of((Object) Long.valueOf(strings[0]));
            } catch (Exception ex) {
                ex.printStackTrace();
                return Optional.absent();
            }
        }
    };

    public static void add(Map<String, Object> attrs, Map<String, String[]> paras, String name) {
        if (paras.containsKey(name)) {
            try {
                String[] params = paras.get(name);
                if (params != null && params.length > 0) {
                    attrs.put(name, paras.get(name)[0]);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void add(Map<String, Object> attrs, Map<String, String[]> paras, String name, TransmitFactory factory) {
        if (paras.containsKey(name)) {
            try {
                String[] params = paras.get(name);
                if (params != null && params.length > 0) {
                    Optional<Object> op = factory.toObject(paras.get(name));
                    if (!op.isPresent()) return;
                    Object obj = op.get();
                    if (obj != null) attrs.put(name, obj);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
