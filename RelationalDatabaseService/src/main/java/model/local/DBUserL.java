package model.local;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import service.DSBox;
import util.param.TransmitUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liweiqi on 2014/10/15.
 */
public class DBUserL extends Model<DBUserL> {
    public static DBUserL dao = new DBUserL();
    public final static String TABLE = "db_user";
    public final static String ID = "id";
    public final static String NAME = "name";
    public final static String PASS = "pass";
    public final static String SPACEID = "space_id";
    public final static String ROLE = "role";
    public final static String DESC = "desc";
    public final static String MAXCONN = "max_conn";

    public DBUserL add(Map<String, Object> attrs) {
        DBUserL u = new DBUserL();
        u.setAttrs(attrs);
        boolean state = u.save();
        return state ? u : null;
    }


    public DBUserL getByName(String name) {
        List<DBUserL> li = find("select * from " + TABLE + " where " + NAME + "=?", name);
        return li.size() > 0 ? li.get(0) : null;
    }

    public DBUserL getSpaceRoot(int spaceid) {
        List<DBUserL> li = find("select * from " + TABLE + " where " + SPACEID + "=? and " + ROLE + "=0", spaceid);
        return li.size() > 0 ? li.get(0) : null;
    }

    public Page<DBUserL> getBySpacePage(int space_id, int number, int size) {
        return paginate(number, size, "select * ", "from " + TABLE + " where " + SPACEID + "=? and role=1", space_id);
    }

    public List<DBUserL> getBySpace(int space_id) {
        return find("select * from " + TABLE + " where " + SPACEID + "=?", space_id);
    }

    public boolean deleteBySapce(int spaceid) {
        return Db.use(DSBox.LOCAL).update(" delete from " + TABLE + " where " + spaceid + "=?", spaceid) >= 0;
    }

    public List<Record> getUserAndPriOfDb(int dbid) {
        StringBuilder sql = new StringBuilder("select A." + UserDbPriL.PRI + " as " + UserDbPriL.PRI + ", B." + NAME + " as " + NAME + " from ")
                .append(UserDbPriL.TABLE).append(" as A").append(" join ").append(TABLE).append(" as B on A.")
                .append(UserDbPriL.USERID).append("=").append(" B.").append(ID).append(" where A.")
                .append(UserDbPriL.DBID).append("=").append(dbid);
        return Db.use(DSBox.LOCAL).find(sql.toString());
    }

    public List<Record> getUserAndPriOfSpace(int spaceId) {
        StringBuilder sql = new StringBuilder("select A." + UserDbPriL.PRI + " as " + UserDbPriL.PRI + ", B." + NAME + " as " + NAME)
                .append(",C.").append(DataBaseL.NAME).append(" AS ").append(DataBaseL.NAME).append(" from ")
                .append(UserDbPriL.TABLE).append(" as A").append(" join ").append(TABLE).append(" as B on A.")
                .append(UserDbPriL.USERID).append("=").append(" B.").append(ID)
                .append(" join ").append(DataBaseL.TABLE).append(" as C on A.").append(UserDbPriL.DBID).append("=").append(" B.").append(ID)
                .append(" where A.").append(UserDbPriL.SPACEID).append("=").append(spaceId);
        return Db.use(DSBox.LOCAL).find(sql.toString());
    }

    public static Map<String, Object> getAttrFromUrl(Map<String, String[]> paras) {
        Map<String, Object> attrs = new HashMap<>();
        TransmitUtil.add(attrs, paras, ID, TransmitUtil.toInteger);
        TransmitUtil.add(attrs, paras, NAME);
        TransmitUtil.add(attrs, paras, PASS);
        TransmitUtil.add(attrs, paras, DESC);
        TransmitUtil.add(attrs, paras, SPACEID, TransmitUtil.toInteger);
        TransmitUtil.add(attrs, paras, MAXCONN, TransmitUtil.toInteger);
        return attrs;
    }

}
