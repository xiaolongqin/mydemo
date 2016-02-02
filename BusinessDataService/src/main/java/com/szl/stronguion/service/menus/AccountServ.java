package com.szl.stronguion.service.menus;

import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.menus.Account;

import java.util.List;

/**
 * Created by 郭皓 on 2015/6/25.
 */
public class AccountServ {
    private Account account = new Account();

    public Account login(String name, String pass) {
        return account.login(name, pass);
    }

    public boolean testAccount(String name) {
        return account.testNameIs(name);
    }

    public boolean testEmail(String email) {
        return account.testEmail(email);
    }

    public int addAccount(String name, String pass, int cata, String email, String dep, String ctime, String etime, String phone, String cata1, int is_flag_apply) {
        return account.addAccount(name, pass, cata, email, dep, ctime, etime, phone, cata1, is_flag_apply);


    }


    // delete a old account and roles
    public boolean deleteAccount(int uid) {
        return account.deleteAccount(uid);
    }

    public List<Record> searchAccount(int pageNumber, String name) {
        long totol = account.getAllAccountTotol(name);
        List<Record> list1 = account.getAllAccount(pageNumber, name);
        if (list1.size() > 0) {
            list1.get(0).set("total", Math.ceil(totol / (Account.pageSize * 1.0)));
        }
        return list1;
    }

    public List<Record> searchAccountDR(int pageNumber, String name) {
        long totol = account.getAllAccountTotolDR(name);
        List<Record> list1 = account.getAllAccountDR(pageNumber, name);

//        for (Record list : list1) {
//            if (list.getInt("is_validate_email") == 0 && list.getInt("is_validate_apply") == 0) {
//                list1.remove(list);
//            }
//        }
        for (int i = 0; i < list1.size(); i++) {
            if ((list1.get(i).getInt("is_validate_email") == 0) && (list1.get(i).getInt("is_validate_apply") == 0)) {
                System.out.println(list1.get(i).getStr("name"));
                list1.remove(i);
            }
        }
        if (list1.size() > 0) {
            list1.get(0).set("total", Math.ceil(totol / (Account.pageSize * 1.0)));
        }
        return list1;
    }

    //update pwd
    public boolean updatePwd(int uid, String oldPwd, String newPwd, String confirmPwd) {
        return account.updatePwd(uid, oldPwd, newPwd, confirmPwd);
    }

    public Account getAccountById(long uid) {
        return account.getAccountById(uid);
    }

    public Account getAccountByEMail(String email) {
        return account.getAccountByEMail(email);
    }

    public int clearMail(String email) {
        return account.clearMail(email);
    }

    public boolean registMail(int uid, Long time, String validateCode) {
        return account.registMail(uid, time, validateCode);
    }
    public boolean passMail(long uid, Long time, String validateCode) {
        return account.passMail(uid, time, validateCode);
    }


    public List<Record> getCata() {
        return account.getCata();
    }

    public boolean modifyAccount(int uid, String pass, String phone, String company) {
        return account.modifyAccount(uid, pass, phone, company) == 1;
    }

    public boolean modifyPass(long uid, String pass) {
        return account.modifyPass(uid, pass) == 1;
    }

    public int modifyEx(int uid, int cata, String cata1) {
        return account.modifyEx(uid, cata, cata1);
    }

    public int modifyEmailValidate(String email) {
        return account.modifyEmailValidate(email);
    }

    public int modifyDR(int uid, int rights) {
        return account.modifyDR(uid, rights);
    }

    public Long checkRemainingTime(int uid) {

        Long x = account.getMaturityByID(uid);
        return (x - System.currentTimeMillis());

    }
}
