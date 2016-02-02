package com.szl.stronguion.model.menus;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/6/23.
 */
public class Account extends Model<Account> {
    public static final String ID = "id";
    public static final String ROLE_ID = "role_id";
    public static final String NAME = "name";
    public static final String PASS = "pass";
    public static final int pageSize = 5;
    private static Account dao = new Account();

    public Account login(String name, String pass) {
        return dao.findFirst("select * from account where " + NAME + " = '" + name + "' and " + PASS + " = '" + pass + "';");
    }

    //add account
    public int addAccount(String name, String pass, int role_id) {
        long time = System.currentTimeMillis();
        // Account account = new Account();
        Record account = new Record();
        account.set("name", name);
        account.set("pass", pass);
        account.set("ctime", time);
        account.set("role_id", role_id);
        //account.save();
        Db.use("main1").save("account", account);
        return account.getLong("id").intValue();
    }
    //查看是否有改用户名
    public boolean testNameIs(String name) {
        Record record=Db.use("main1").findFirst("select * from account WHERE name='"+name+"'");
        if(record==null){
            return false;
        }
        return record.get("name")!=null?true:false;
    }

    public boolean updatePwd(int id, String oldPwd, String newPwd, String confirmPwd) {
        return Db.use("main1").update("update account set pass = '" + newPwd + "' where id = '" + id + "' and pass = '" + oldPwd + "';") == 1;
    }

    //delete account
    public boolean deleteAccount(int uid) {
        return dao.deleteById(uid);
    }

    public long searchAccountTotol(String name) {
        return dao.find("SELECT COUNT(*) as totolpage \n" +
                "FROM `account` a,roles r \n" +
                "where  r.id = a.role_id and a.name like '%" + name + "%' and a.id <> 0;").get(0).get("totolpage");
    }

    public long getAllAccountTotol(String name) {
        return dao.find("SELECT COUNT(*) as totolpage \n" +
                "FROM `account` a,roles r \n" +
                "where  r.id = a.role_id and a.id <> 0;").get(0).get("totolpage");
    }


    public List<Record> searchAccount(int pageNumber,String name) {
        List<Record> list=Db.use("main1").paginate(pageNumber, pageSize, "SELECT a.id,a.`name`,a.email,a.`level`,a.ctime,a.role_id,a.`status`,r.`name` as role_name", "" +
                "FROM `account` a,roles r where  r.id = a.role_id and a.name like '%" + name + "%' and a.id <> 0").getList();
        return list;
    }

    public List<Record> getAllAccount(int pageNumber,String name) {
        return Db.use("main1").paginate(pageNumber,pageSize,"SELECT  a.id,a.`name`,a.email,a.`level`,a.ctime,a.role_id,a.`status`,r.`name` as role_name","" +
                " FROM `account` a,roles r where  r.id = a.role_id and a.id <> 0").getList();
    }

    public int modifyAccount(int uid, String name, int role_id) {
        return Db.use("main1").update("update account set name = '" + name + "',role_id = '" + role_id + "' where id = '" + uid + "'");
        // return dao.findById(uid).set("name", name).set("role_id", role_id).save();
    }

    public Account getAccountById(int uid) {
        return dao.findFirst("select a.`name`,a.`level`,a.role_id,r.`name` as role_name " +
                "from account a,roles r where a.role_id = r.id and a.id = '" + uid + "';");
    }
}
