package com.szl.stronguion.service.customercharacter;

import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.customercharacter.AllCycleUsersFlag;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Tyfunwang on 2015/7/16.
 */
public class AllCycleUsersFlagServ {
    private AllCycleUsersFlag usersFlag = new AllCycleUsersFlag();

    public Map<String,Object> getCustPerdict(){
        Map<String,Object> maps = new TreeMap<String, Object>();
        maps.put("0消费习惯",usersFlag.getCustHabit());
        maps.put("1消费金额",usersFlag.getCustPrice());
        maps.put("2余额习惯",usersFlag.getBalance());
        maps.put("3活跃度",usersFlag.getActive());
        maps.put("4沉默度",usersFlag.getSilence());
        return maps;
    }
    //消费习惯
    private void getCustHabit() {
        Record habit = usersFlag.getCustHabit();
    }
    //消费金额
    private void getCustPrice() {
        Record price = usersFlag.getCustPrice();
    }
    //余额习惯
    private void getBalance() {
        Record balance = usersFlag.getBalance();
    }
    //活跃度
    private void getActive() {
        Record active = usersFlag.getActive();
    }
    //沉默度
    private void getSilence() {
        Record silence = usersFlag.getSilence();
    }
}
