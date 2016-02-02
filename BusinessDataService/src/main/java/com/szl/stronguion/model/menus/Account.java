package com.szl.stronguion.model.menus;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import java.security.MessageDigest;
import java.util.List;

/**
 * Created by 郭皓 on 2015/6/23.
 */
public class Account extends Model<Account> {
    public static final String ID = "id";
    public static final String ROLE_ID = "role_id";
    public static final String NAME = "name";
    public static final String PASS = "pass";
    public static final int pageSize = 8;
    public static final String CATEGORYID = "category_id";
    public static final String CATEGORYNAME = "category_name";
    private static Account dao = new Account();

    public Account login(String name, String pass) {
        return dao.findFirst("select * from eb_web_account where " + NAME + " = '" + name + "' and " + PASS + " = '" + pass + "';");
    }

    //add account
    public int addAccount(String name, String pass, int cata, String email, String dep, String ctime, String etime, String phone, String cata1, int is_flag_apply) {
        long time = System.currentTimeMillis();
        // Account account = new Account();
        Record account = new Record();
        account.set("name", name);
        account.set("is_flag_apply", is_flag_apply);
        account.set("pass", pass);
        account.set("email", email);
        account.set("company", dep);
        account.set("ctime", ctime);
        account.set("edtime", etime);
        account.set("category_id", cata);
        account.set("category_name", cata1);
        account.set("creatime", time);
        if (is_flag_apply == 1) {
            account.set("is_validate_email", 1);
            account.set("is_validate_apply", 0);
        } else {
            account.set("is_validate_email", 0);
            account.set("is_validate_apply", 1);
        }
        account.set("phone", phone);
        //account.save();
        String vCode = encrypt(email) + Math.random() * Math.random();

        Db.use("main1").save("eb_web_account", account);
        return account.getLong("id").intValue();
    }

    public boolean registMail(int uid, Long time, String validateCode) {
        return Db.use("main1").update("update eb_web_account set registertime = '" + time + "' ,validatecode = '" + validateCode + "' where id = '" + uid + "'") == 1;

    }

    public boolean passMail(long uid, Long time, String validateCode) {
        return Db.use("main1").update("update eb_web_account set resetpasstime = '" + time + "' ,validatecode = '" + validateCode + "' where id = '" + uid + "'") == 1;

    }

    private static final String encrypt(String srcStr) {
        try {
            String result = "";
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(srcStr.getBytes("utf-8"));
            for (byte b : bytes) {
                String hex = Integer.toHexString(b & 0xFF).toUpperCase();
                result += ((hex.length() == 1) ? "0" : "") + hex;
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //查看是否有改用户名
    public boolean testNameIs(String name) {
        Record record = Db.use("main1").findFirst("select * from eb_web_account WHERE name='" + name + "'");
        if (record == null) {
            return false;
        }
        return record.get("name") != null ? true : false;
    }

    //查看邮箱是否重复
    public boolean testEmail(String email) {
        Record record = Db.use("main1").findFirst("select * from eb_web_account WHERE email='" + email + "'");
        if (record == null) {
            return false;
        }
        return record.get("email") != null ? true : false;
    }

    public boolean updatePwd(int id, String oldPwd, String newPwd, String confirmPwd) {
        return Db.use("main1").update("update eb_web_account set pass = '" + newPwd + "' where id = '" + id + "' and pass = '" + oldPwd + "';") == 1;
    }

    //delete account
    public boolean deleteAccount(int uid) {
//        return dao.deleteById(uid);

        return Db.use("main1").update("update eb_web_account set is_validate_apply = '" + 0 + "' ,is_validate_email = '" + 0 + "' where id = '" + uid + "'") == 1;

    }


    public long searchAccountTotol(String name) {
        return dao.find("SELECT COUNT(*) as totol \n" +
                "FROM `eb_web_account` a \n" +
                "where  a.name like '%" + name + "%' and a.status=0;").get(0).get("totolpage");
    }

    public long getAllAccountTotol(String name) {
        return dao.find("SELECT COUNT(*) as totol \n" +
                "FROM `eb_web_account` a \n" +
                "where a.name like '%" + name + "%' and a.status=0 ;").get(0).get("totol");
    }

    public long getAllAccountTotolDR(String name) {
        return dao.find("SELECT COUNT(*) as totol \n" +
                "FROM `eb_web_account` a \n" +
                "where a.name like '%" + name + "%' and a.status=0 ;").get(0).get("totol");
    }

    public long getMaturityByID(int uid) {
        return dao.find("SELECT a.edtime\n" +
                "FROM `eb_web_account` a \n" +
                "where a.id = '" + uid + "' ;").get(0).get("edtime");
    }


    public List<Record> searchAccount(int pageNumber, String name) {
        List<Record> list = Db.use("main1").paginate(pageNumber, pageSize, "SELECT a.id,a.name,a.category_id,a.category_name",
                " FROM `eb_web_account` a where (a.name like '%" + name + "%' or a.category_name like '%" + name + "%' ) and a.status=0 ").getList();
        return list;
    }

    public List<Record> getAllAccount(int pageNumber, String name) {
        return Db.use("main1").paginate(pageNumber, pageSize, "SELECT  a.id,a.name,a.category_name,a.category_id,a.is_validate_apply",
                " FROM `eb_web_account` a where (a.name like '%" + name + "%' or a.is_validate_apply like '%" + name + "%' ) and a.status=0 \n" +
                        "   and (a.is_validate_apply<>0 or a.is_validate_email<>0)\n" +
                        "   and a.is_flag_apply=1").getList();
    }

    public List<Record> getAllAccountDR(int pageNumber, String name) {
        return Db.use("main1").paginate(pageNumber, pageSize, "SELECT *",
                " FROM `eb_web_account` a where (a.name like '%" + name + "%' or a.is_validate_apply like '%" + name + "%' ) and a.status=0 \n" +
                        "   and (a.is_validate_apply<>0 or a.is_validate_email<>0)\n" +
                        "   and a.is_flag_apply=1").getList();
    }

//    public List<Record> getAllAccountDR(int pageNumber, String name) {
//        return Db.use("main1").paginate(pageNumber, pageSize, "SELECT *",
//                " FROM `eb_web_account` a where (a.name like '%" + name + "%' or a.is_validate_apply like '%" + name + "%' ) and a.status=0 \n" +
//                        "   and (a.is_validate_apply<>0 or a.is_validate_email<>0)\n" +
//                        "   and a.is_flag_apply=1").getList();
//    }

//    public List<Record> getAllAccountDR(int pageNumber, String name) {
//        return Db.use("main1").paginate(pageNumber, pageSize, "SELECT *",
//                " FROM (SELECT a.`phone`, a.`email`, a.id,a.name,a.category_name,a.category_id,a.is_validate_apply FROM `eb_web_account` a where (a.name like '%" + name + "%' or a.is_validate_apply like '%" + name + "%' ) and a.status=0 and a.is_flag_apply=1) where a.is_validate_apply<>0 or is_validate_email<>0").getList();
//    }

    public int modifyAccount(int uid, String pass, String phone, String company) {
        return Db.use("main1").update("update eb_web_account set pass = '" + pass + "',company ='" + company + "',phone = '" + phone + "' where id = '" + uid + "'");
        // return dao.findById(uid).set("name", name).set("role_id", role_id).save();
    }

    public int modifyPass(long uid, String pass) {
        return Db.use("main1").update("update eb_web_account set pass = '" + pass + "' where id = '" + uid + "'");
        // return dao.findById(uid).set("name", name).set("role_id", role_id).save();
    }

    public int modifyEx(int uid, int cata, String cata1) {
        return Db.use("main1").update("update eb_web_account set category_id = '" + cata + "' ,category_name = '" + cata1 + "' where id = '" + uid + "'");
        // return dao.findById(uid).set("name", name).set("role_id", role_id).save();
    }

    public int modifyEmailValidate(String email) {
        return Db.use("main1").update("update eb_web_account set is_validate_email = '" + 1 + "' where email = '" + email + "'");
        // return dao.findById(uid).set("name", name).set("role_id", role_id).save();
    }

    public int modifyDR(int uid, int right) {
        return Db.use("main1").update("update eb_web_account set is_validate_apply = '" + right + "' where id = '" + uid + "'");
        // return dao.findById(uid).set("name", name).set("role_id", role_id).save();
    }

    public Account getAccountById(long uid) {
        return dao.findFirst("select a.`phone`,a.`name`,a.`id`,a.`category_name`,a.`category_id`,a.`status`,a.is_validate_apply,a.company from eb_web_account a where a.id = '" + uid + "'; ");
    }

    public Account getAccountByEMail(String email) {
        return dao.findFirst("select * from eb_web_account a where a.email = '" + email + "'; ");
    }

    public int clearMail(String email) {
        return Db.use("main1").update("update eb_web_account set email = '" + "" + "'  where email = '" + email + "'");
    }

    public List<Record> getCata() {
//        return dao.findFirst("select a.`phone`,a.`name`,a.`pass`,a.`category_name`,a.`category_id`,a.`status`,a.is_validate_apply,a.company from eb_web_account a where a.id = '" + uid + "'; ");
        return Db.use("main1").find("SELECT DISTINCT a.category_id, a.category_name FROM eb_web_account a;");
    }
}
