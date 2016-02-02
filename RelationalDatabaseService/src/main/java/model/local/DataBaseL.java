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
 * Created by liweiqi on 2014/10/16.
 */
public class DataBaseL extends Model<DataBaseL> {
    public static DataBaseL dao = new DataBaseL();
    public final static String TABLE = "db";
    public final static String ID = "id";
    public final static String NAME = "db_name";
    public final static String SPACEID = "space_id";
    public final static String CHARSET = "charset";
    public final static String STATE = "state";//0 normal,1 not in use
    public final static String TABLESPACE = "tablespace";
    public final static String STORAGE = "storage";
    public final static int NORMAL = 0;
    public final static int NOTUSE = 1;

    public DataBaseL add(Map<String, Object> attrs) {
        DataBaseL dataBaseL = new DataBaseL();
        boolean state = dataBaseL.setAttrs(attrs).save();
        return state ? dataBaseL : null;
    }

    public Page<DataBaseL> getBySpacePage(int sid, int number, int size) {
        Page<DataBaseL> bases = dao.paginate(number, size, "select *", "from " + TABLE + " where " + SPACEID + "=? order by " + ID + " desc", sid);
        return bases;
    }

    public List<DataBaseL> getBySpace(int sid) {
        List<DataBaseL> bases = dao.find("select * from " + TABLE + " where " + SPACEID + "=?", sid);
        return bases;
    }

    public boolean deleteBySapce(int spaceid) {
        return Db.use(DSBox.LOCAL).update(" delete from " + TABLE + " where " + spaceid + "=?", spaceid) >= 0;
    }

    /**
     * get all data_bases  in db_user's privilege
     */
    public List<Record> getInDBUserPri(int dbuid, int spaceid) {
        StringBuilder sb = new StringBuilder("select * from" + TABLE + " as A")
                .append(" join " + UserDbPriL.TABLE + " as B on")
                .append("A." + ID + "=B." + UserDbPriL.DBID)
                .append("where B." + UserDbPriL.USERID + "=? and A." + SPACEID + "=?");
        List<Record> list = Db.use("local").find(sb.toString(), dbuid, spaceid);
        return list;
    }

    /**
     * get all data_bases  without db_user's privilege
     */
    public List<DataBaseL> getOutDBUserPri(int dbuid, int spaceid) {
        StringBuilder sb = new StringBuilder("select * from " + TABLE)
                .append(" where " + SPACEID + " =? and " + ID + " not in (").append("select " + UserDbPriL.DBID)
                .append(" from " + UserDbPriL.TABLE + " where " + UserDbPriL.USERID + "=?")
                .append(")");
        List<DataBaseL> list = find(sb.toString(), spaceid, dbuid);
        return list;
    }

    /**
     * get all data_bases  by db_user's privilege page-form
     */
    public Page<Record> getByDBUserPri(int dbuid, int spaceid, int number, int size) {
        StringBuilder sb = new StringBuilder("from " + TABLE + " as A")
                .append(" join " + UserDbPriL.TABLE + " as B on ")
                .append("A." + ID + "=B." + UserDbPriL.DBID)
                .append(" where B." + UserDbPriL.USERID + "=? and A." + SPACEID + "=?");
        Page<Record> list = Db.use(DSBox.LOCAL).paginate(number, size, "select * ", sb.toString(), dbuid, spaceid);
        return list;
    }

    public static Map<String, Object> getAttrFromUrl(Map<String, String[]> paras) {
        Map<String, Object> attrs = new HashMap<>();
        TransmitUtil.add(attrs, paras, ID, TransmitUtil.toInteger);
        TransmitUtil.add(attrs, paras, NAME);
        TransmitUtil.add(attrs, paras, CHARSET);
        TransmitUtil.add(attrs, paras, SPACEID, TransmitUtil.toInteger);
        TransmitUtil.add(attrs, paras, STATE, TransmitUtil.toInteger);
        TransmitUtil.add(attrs, paras, TABLESPACE);
        TransmitUtil.add(attrs, paras, STORAGE);
        return attrs;
    }
}
