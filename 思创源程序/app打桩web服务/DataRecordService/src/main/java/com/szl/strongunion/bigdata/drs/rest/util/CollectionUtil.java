package com.szl.strongunion.bigdata.drs.rest.util;

import javax.ws.rs.core.MultivaluedMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by liweiqi on 2015/5/28.
 */
public class CollectionUtil {
    private CollectionUtil() {
    }

    public static Map<String, Object> multiValueMapToMap(MultivaluedMap<String, String> formParas, Set<String> excludeKeys) {
        Map<String, Object> attrs = new HashMap<String, Object>();
        for (String key : formParas.keySet()) {
            if (!excludeKeys.contains(key))
                attrs.put(key, formParas.getFirst(key));
        }
        return attrs;
    }

    public static Map<String, Object> multiValueMapToMap(MultivaluedMap<String, String> formParas) {
        Map<String, Object> attrs = new HashMap<String, Object>();
        for (String key : formParas.keySet()) {
            attrs.put(key, formParas.getFirst(key));
        }
        return attrs;
    }

}
