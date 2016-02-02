package com.szl.stronguion.service.customercharacter;

import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.customercharacter.LifeCycleUsersFlag;

import java.util.*;

/**
 * Created by Tyfunwang on 2015/7/15.
 */
public class LifeCycleFlagServ {
    private LifeCycleUsersFlag usersFlag = new LifeCycleUsersFlag();


    //获取users_flag的每个小时的数据，然后计算出前五个
    public Map<String, List<Map<Integer, Object>>> getCoustHabit(String type, String startTime, String endTime) throws InterruptedException {
        Map<String, List<Map<Integer, Object>>> maps = new HashMap<String, List<Map<Integer, Object>>>();
        //获取24个小时的数据
        List<Map<Integer, Object>> listByHours = getBy24Hour(type, startTime, endTime);
        maps.put("hours", listByHours);
        //排序
        List<Map<Integer, Object>> listShuffle = getShuffleTop5(type, startTime, endTime);
        maps.put("top5", listShuffle);
        return maps;
    }

    //对获取的24小时数据进行排序
    private List<Map<Integer, Object>> getShuffleTop5(String type, String startTime, String endTime) {
        List<Map<Integer, Object>> shuffledTop5 = new ArrayList<Map<Integer, Object>>();
        List<Map<Integer, Object>> shuffled = getBy24Hour(type, startTime, endTime);
        listSort(shuffled);
        //top5
        for (int j = 0; j < 24; j++) {
            if (j <= 4) {
                shuffledTop5.add(shuffled.get(j));
            }
        }
        return shuffledTop5;
    }


    //获取24个小时段的数据
    private List<Map<Integer, Object>> getBy24Hour(String type, String startTime, String endTime) {
        List<Map<Integer, Object>> listMap;
        switch (Integer.valueOf(type)) {
            case 1: //1:消费客户
                listMap = usersFlag.getConsume(startTime, endTime);
                break;
            case 2: //2:活跃客户
                listMap = usersFlag.getActived(startTime, endTime);
                break;
            case 3:  //3:沉默客户
                listMap = usersFlag.getSilence(startTime, endTime);
                break;
            case 4:   //4:全部客户
                listMap = usersFlag.getAll(startTime, endTime);
                break;
            default://1:消费客户
                listMap = usersFlag.getConsume(startTime, endTime);
                break;
        }
        return listMap;
    }

    //计算24小时的前5
    private void listSort(List<Map<Integer, Object>> listByHours) {
        Collections.sort(listByHours, new Comparator<Map<Integer, Object>>() {
            @Override
            public int compare(Map<Integer, Object> o1, Map<Integer, Object> o2) {
                //o1，o2是list中的Map，可以在其内取得值，按其排序，此例为升序，s1和s2是排序字段值
                Integer s1 = 0;
                Integer s2 = 0;
                for (Integer obj1 : o1.keySet()) {
                    s1 = Integer.valueOf(o1.get(obj1).toString());
                }
                for (Integer obj2 : o2.keySet()) {
                    s2 = Integer.valueOf(o2.get(obj2).toString());
                }
                if (s2 > s1) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }


    /**
     * test method getByhour getShuffle
     *
     * @param type
     * @param startTime
     * @param endTime
     * @return
     */
    public Map<String, List<Record>> getCustHabit(int type, String startTime, String endTime) throws InterruptedException {
        Map<String, List<Record>> maps = new HashMap<String, List<Record>>();
        //获取24个小时的数据
        List<Record> listByHours = getHabitByHour(type, startTime, endTime);
        maps.put("hours", listByHours);
        //排序
        List<Record> listTop5 = getHabitTop5(type, startTime, endTime);
        maps.put("top5", listTop5);
        return maps;
    }

    private List<Record> getHabitByHour(int type, String startTime, String endTime) {
        switch (type) {
            case 1: //1:消费客户
                return usersFlag.getHabitDao("uid_consume_flag", startTime, endTime);
            case 2: //2:活跃客户
                return usersFlag.getHabitDao("uid_active_flag", startTime, endTime);
            case 3:  //3:沉默客户
                return usersFlag.getHabitDao("uid_silence_flag", startTime, endTime);
            case 4:   //4:全部客户
                return usersFlag.getHabitAllDao(startTime, endTime);
            default:return usersFlag.getHabitDao("uid_consume_flag", startTime, endTime);
        }
    }

    private List<Record> getHabitTop5(int type, String startTime, String endTime) {
        switch (type) {
            case 1: //1:消费客户top5
                return usersFlag.getHabitTop5("uid_consume_flag", startTime, endTime);
            case 2: //2:活跃客户top5
                return usersFlag.getHabitTop5("uid_active_flag", startTime, endTime);
            case 3:  //3:沉默客户top5
                return usersFlag.getHabitTop5("uid_silence_flag", startTime, endTime);
            case 4:   //4:全部客户top5
                return usersFlag.getHabitAllTop5( startTime, endTime);
            default:return usersFlag.getHabitTop5("uid_consume_flag", startTime, endTime);
        }
    }

}
