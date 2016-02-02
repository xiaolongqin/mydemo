package util;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by 小龙
 * on 15-5-7
 * at 上午10:11.
 */

public class MySessionContext {

    private static HashMap<String,HashSet<HttpSession>> mymap = new HashMap<String,HashSet<HttpSession>>();

    public static synchronized void AddSession(String loginuser,HttpSession session) {
        if (session != null) {
            if (mymap.containsKey(loginuser)){
                mymap.get(loginuser).add(session);
            }else {
                HashSet<HttpSession> hashSet=new HashSet<HttpSession>();
                hashSet.add(session);
                mymap.put(loginuser,hashSet);
            }
        }
    }

    public static synchronized void DelSession(HttpSession session) {
        if (session != null) {
            Iterator iterator=mymap.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry entry = (Map.Entry) iterator.next();

                HashSet<HttpSession> hashSet=( HashSet<HttpSession>)entry.getValue();
                if (hashSet.contains(session)){
                    ((HashSet<HttpSession>) entry.getValue()).remove(session);
                }
                //用户到服务器无连接，清空mymap中相应的记录
                if (hashSet.isEmpty()){
                    iterator.remove();
                }
            }

        }
    }

    public static synchronized HashSet<HttpSession> getSession(String loginuser) {
        if (loginuser == null)
            return null;
        return  mymap.get(loginuser);
    }
}
