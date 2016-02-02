package model;

import com.alibaba.fastjson.JSONObject;
import util.EncAndDecByDES;

/**
 * Created by Tyfunwang on 2015/1/27.
 */
public class Account {
    private static EncAndDecByDES encAndDecByDES = new EncAndDecByDES();
    private int accountId;
    private String accountName;
    private String accountRealName;
    private String accountEmail;
    private String accountPhone;


    private long time;

    public String getAccountRealName() {
        return accountRealName;
    }

    public void setAccountRealName(String accountRealName) {
        this.accountRealName = accountRealName;
    }

    public String getAccountPhone() {
        return accountPhone;
    }

    public void setAccountPhone(String accountPhone) {
        this.accountPhone = accountPhone;
    }


    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public static Account parse(String cipher) throws Exception {
        String json = encAndDecByDES.getDesString(cipher);
        JSONObject object = JSONObject.parseObject(json);

        Account account = new Account();
        account.setAccountEmail(object.getString("email"));
        account.setAccountId(object.getInteger("userid"));
        account.setAccountName(object.getString("name"));
        account.setAccountPhone(object.getString("tel"));
        account.setAccountRealName(object.getString("realname"));
        account.setTime(object.getLong("time"));
        long interval=( System.currentTimeMillis()-account.getTime());
        //if ( interval> 2 * 60*1000) throw new RuntimeException();
        return account;
    }
}
