package com.szl.stronguion.service.customercharacter;

import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.customercharacter.LifeCycleUsers;

import java.util.*;

/**
 * Created by Tyfunwang on 2015/7/14.
 */
public class LifeCycleUsersServ {
    private LifeCycleUsers cycleUsers = new LifeCycleUsers();

    /**
     * 消费客户群体特征分析*
     * *
     * @Param startTime 前几天的时间*
     * @Param endTime 结束的时间*
     * *
     */
    public Map<String, Map<String, Object>> getCustmorGroup(String startTime, String endTime) {
        Map<String, Map<String, Object>> maps = new HashMap<String, Map<String, Object>>();

        //total  nan nv
        //总人数的比例
        Map<String, Object> leftMaps = new HashMap<String, Object>();
        Long man = cycleUsers.getTotalBySex(startTime, endTime, 1).getLong(LifeCycleUsers.YESTORDAY + "1");//男
        Long woman = cycleUsers.getTotalBySex(startTime, endTime, 0).getLong(LifeCycleUsers.YESTORDAY + "0");//女
        Long total = cycleUsers.getTotalByTime(startTime, endTime).getLong(LifeCycleUsers.YESTORDAY);
        leftMaps.put("total", total);
        leftMaps.put("1", man);
        leftMaps.put("0", woman);
        leftMaps.put("percent1", Double.valueOf(String.format("%.2f", ((double) man / (double) total) * 100)));
        leftMaps.put("percent0", Double.valueOf(String.format("%.2f", ((double) woman / (double) total) * 100)));


        //各个年龄段占比
        Map<String, Object> manMaps = cycleUsers.getTotalByAge(startTime, endTime, 1);//男
        Map<String, Object> womanMaps = cycleUsers.getTotalByAge(startTime, endTime, 0);//女
        maps.put("manMaps", manMaps);
        maps.put("womanMaps", womanMaps);

        //计算值
        maps.put("total", leftMaps);
        maps.put("man", modifyValues(manMaps, man));
        maps.put("woman", modifyValues(womanMaps, woman));


        return maps;

    }

    //遍历计算值
    protected Map<String, Object> modifyValues(Map<String, Object> maps, long man) {
        Map<String, Object> mapValues = new TreeMap<String, Object>();
        Set<Map.Entry<String, Object>> setMap = maps.entrySet();
        for (Map.Entry<String, Object> entry : setMap) {
            if (man == 0) {
                mapValues.put(entry.getKey(), String.format("%.2f", 0.0));
//                entry.setValue(String.format("%.2f", 0.0));
            } else {
                mapValues.put(entry.getKey(), String.format("%.2f", (Double.valueOf(entry.getValue().toString()) / (double) man) * 100));
//                entry.setValue(String.format("%.2f", (Double.valueOf(entry.getValue().toString()) / (double) man) * 100));
            }
        }
        return mapValues;
    }

    /**
     * 最近几天的生日祝福*
     * *
     */
    public Record getBirth(int type) {
        Record record = null;
        switch (type) {
            case 1:
                //明天过生
                record = cycleUsers.getCountUid(1, 0);
                break;
            case 2:
                //最近7天过生
                record = cycleUsers.getCountUid(1, 7);
                break;
            case 3:
                //最近30天过生
                record = cycleUsers.getCountUid(1, 30);
                break;
            default:
                break;
        }
        return record;
    }

    public List<Record> getPersonInfo(int type, String param) {
        List<Record> list = null;
        switch (type) {
            case 1:
                //明天过生
                list = cycleUsers.getPersonInfo(1, 0, param);
                break;
            case 2:
                //最近7天过生
                list = cycleUsers.getPersonInfo(1, 7, param);
                break;
            case 3:
                //最近30天过生
                list = cycleUsers.getPersonInfo(1, 30, param);
                break;
            default:
                break;
        }
        return list;


    }

    public Map<String, Object> getBirthBless(int type) {
        Record blessed = null;//明天已经过生
        Record willBless = null;//明天将要过生
        switch (type) {
            case 1:
                //明天过生
                blessed = cycleUsers.blessedBirth(-1, 0);
                willBless = cycleUsers.willBlessBirth(1, 0);
                break;
            case 2:
                //最近7天过生
                blessed = cycleUsers.blessedBirth(-7, -1);
                willBless = cycleUsers.willBlessBirth(1, 7);
                break;
            case 3:
                //最近30天过生
                blessed = cycleUsers.blessedBirth(-30, -1);
                willBless = cycleUsers.willBlessBirth(1, 30);
                break;
            default:
                break;
        }
        return countBirthBless(blessed, willBless);
    }

    public Map<String, Object> countBirthBless(Record blessed, Record willBless) {
        Map<String, Object> maps = new HashMap<String, Object>();//0:将要过生 ,1:已经过生
        Long param1 = willBless.get(LifeCycleUsers.COUNTUID);
        Long param2 = blessed.get(LifeCycleUsers.COUNTUID);
        maps.put("0", Double.valueOf(String.format("%.2f", (double) param1 / (double) (param1 + param2) * 100)));
        maps.put("1", Double.valueOf(String.format("%.2f", (double) param2 / (double) (param1 + param2) * 100)));
        return maps;
    }

    /**
     * 用户画像搜索*
     * *
     */
    public List<Record> searchUsers(String param, int sex) {
        return cycleUsers.searchUsers(param, sex);
    }
}
