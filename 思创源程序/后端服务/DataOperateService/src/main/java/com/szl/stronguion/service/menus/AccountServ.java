package com.szl.stronguion.service.menus;

import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.menus.Account;
import com.szl.stronguion.service.customercharacter.ThresholdValueServ;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/6/25.
 */
public class AccountServ {
    private Account account = new Account();
    private ThresholdValueServ thresholdValueServ = new ThresholdValueServ();

    public Account login(String name, String pass) {
        return account.login(name, pass);
    }

    public boolean testAccount(String name){
        return account.testNameIs(name);
    }

    //add account and set role 
    public boolean addAccount(String name, String pass, int role_id) {
        int uid = account.addAccount(name, pass, role_id);

        if (uid > 0) {
            //生成对应的阀值，值为null
            for (int i = 1; i < 7; i++) {
                if (thresholdValueServ.initThresholdValue(uid, i)) continue;
                return false;
            }
            return true;
        }
        return false;
    }

    //update pwd
    public boolean updatePwd(int uid, String oldPwd, String newPwd, String confirmPwd) {
        return account.updatePwd(uid, oldPwd, newPwd, confirmPwd);
    }

    // delete a old account and roles
    public boolean deleteAccount(int uid) {
        return account.deleteAccount(uid);
    }

    public List<Record> searchAccount(int pageNumber,String name) {
        if (name == null || "".equals(name)) {
            long totol=account.getAllAccountTotol(name);
            List<Record> list1=account.getAllAccount(pageNumber,name);
            if (list1.size()>0){
                list1.get(0).set("total",Math.ceil(totol/(Account.pageSize*1.0)));
            }
            return list1;
        }
        long t2=account.searchAccountTotol(name);
        List<Record> list2=account.searchAccount(pageNumber,name);
        if (list2.size()>0){
            list2.get(0).set("total",Math.ceil(t2/(Account.pageSize*1.0)));
        }
        return list2;
    }

    public Account getAccountById(int uid) {
        return account.getAccountById(uid);
    }

    public boolean modifyAccount(int uid, String name, int role_id) {
        return account.modifyAccount(uid, name, role_id) == 1;
    }
}
