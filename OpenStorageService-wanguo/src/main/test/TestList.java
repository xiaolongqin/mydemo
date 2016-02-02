import java.util.*;
/**
 * Created by Tyfunwang on 2015/3/24.
 */
public class TestList {
    
    public static void main(String[] args) {
        List<Map<String,Integer>> list  = new ArrayList<Map<String,Integer>>();
        Map<String, Integer> map = new HashMap<String, Integer>();

        Map<String, Integer> map1 = new HashMap<String, Integer>();
        Map<String, Integer> map2 = new HashMap<String, Integer>();
        Map<String, Integer> map3 = new HashMap<String, Integer>();

        map.put("number", 1);
        map1.put("number", 3);
        map2.put("number", 4);
        map3.put("number", 2);
        list.add(map);
        list.add(map1);
        list.add(map2);
        list.add(map3);

        System.out.println(list);
      
            for (int i = 0; i < list.size(); i++) {
                Map<String, Integer> tmp0 = list.get(i);
                int number0 =  tmp0.get("number");

                Map<String, Integer> tmp = null;
                for (int j = i; j < list.size(); j++) {
                    Map<String, Integer> tmp1 = list.get(j);
                    int number1 =  tmp1.get("number");

                    if (number0 > number1) {
                        tmp = tmp0;
                        list.set(i, list.get(j));
                        list.set(j, tmp);
                    }
                }

        }
        System.out.println(list);
    }
    
}
