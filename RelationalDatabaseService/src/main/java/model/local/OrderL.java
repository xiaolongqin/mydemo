package model.local;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import util.param.TransmitUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liweiqi on 2014/12/3.
 */
public class OrderL extends Model<OrderL> {
    public static final OrderL dao = new OrderL();
    public static final String TABLE = "order";
    public static final String ID = "id";
    public static final String ORDERNUM = "order_num";
    public final static String TYPE = "type";//0 new service;1 renew service
    public static final String SPACEID = "space_id";
    public final static String ACCOUNTID = "account_id";
    public final static String ACCOUNTNAME = "account_name";
    public final static String ACCOUNTEMAIL = "account_email";
    public static final String CTIME = "ctime";
    public static final String STATE = "state";//0 not pay ,1 have payed,2 refused
    public final static String ADDSTORE = "add_store";
    public final static String ADDCONN = "add_conn";
    public final static String ADDMONTH = "add_month";
    public static class Type {
        public static int NEW = 0;
        public static int UP = 1;
    }
    public static class State {
        public static int NOTPAY = 0;
        public static int PAYED = 1;
        public static int REFUSED = 2;
    }

    public static Map<String, Object> getAttrFromUrl(Map<String, String[]> paras) {
        Map<String, Object> attrs = new HashMap<>();
        TransmitUtil.add(attrs, paras, ID, TransmitUtil.toInteger);
        TransmitUtil.add(attrs, paras, ORDERNUM);
        TransmitUtil.add(attrs, paras, TYPE, TransmitUtil.toInteger);
        TransmitUtil.add(attrs, paras, ACCOUNTID, TransmitUtil.toInteger);
        TransmitUtil.add(attrs, paras, ACCOUNTNAME);
        TransmitUtil.add(attrs, paras, ACCOUNTEMAIL);
        TransmitUtil.add(attrs, paras, SPACEID, TransmitUtil.toInteger);
        TransmitUtil.add(attrs, paras, CTIME, TransmitUtil.toLong);
        TransmitUtil.add(attrs, paras, STATE, TransmitUtil.toInteger);
        TransmitUtil.add(attrs, paras, ADDSTORE, TransmitUtil.toInteger);
        TransmitUtil.add(attrs, paras, ADDCONN, TransmitUtil.toInteger);
        TransmitUtil.add(attrs, paras, ADDMONTH, TransmitUtil.toLong);
        return attrs;
    }

    public OrderL getByOrderNum(String orderNum) {
        return findFirst("select * from " + TABLE + " where " + ORDERNUM + "=?", orderNum);
    }

    public List<OrderL> getByAccount(String accountId) {
        return find("select * from " + TABLE + " where " + ACCOUNTID + "=?", accountId);
    }

    public OrderL getLastPayedBySpace(int spaceId) {
        return findFirst("select * from " + TABLE + " where " + SPACEID + "=? and " + STATE + "=? order by " + CTIME + " desc limit 1", spaceId, 1);
    }


    public Page<OrderL> getAllPageByCondition(int uid, int state, int type, int pageNumber, int pageSize) {
        StringBuilder sqlBUilder = new StringBuilder(" from " + TABLE + " where ");
        List<String> conditionStack = new ArrayList<>(3);
        //user condition
        String byUserCondition = uid > 0 ? ACCOUNTID + "=" + uid : "";
        if (!StrKit.isBlank(byUserCondition)) conditionStack.add(byUserCondition);
        //state condition
        String byStateCondition = state > 0 ? STATE + "=" + state : "";
        if (!StrKit.isBlank(byStateCondition)) conditionStack.add(byStateCondition);
        //type condition
        String byTypeCondition = TYPE + "=" + type;
        conditionStack.add(byTypeCondition);
        //build sql
        for (String condition : conditionStack) {
            sqlBUilder.append(" ").append(condition).append(" AND");
        }
        if (conditionStack.size() > 0) sqlBUilder.setLength(sqlBUilder.length() - 3);
        sqlBUilder.append(" order by " + CTIME + " desc");
        return paginate(pageNumber, pageSize, " select * ", sqlBUilder.toString());
    }

    public boolean up(Map<String, Object> attrs) {
        return new OrderL().setAttrs(attrs).update();
    }

    public OrderL add(Map<String, Object> attrs) {
        OrderL newOrder = new OrderL().setAttrs(attrs);
        return newOrder.save() ? newOrder : null;
    }
}
