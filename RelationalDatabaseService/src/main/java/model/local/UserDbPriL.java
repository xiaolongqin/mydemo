package model.local;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import service.DSBox;

import java.util.List;

/**
 * Created by liweiqi on 2014/10/23.
 */
public class UserDbPriL {
    private static UserDbPriL pri = new UserDbPriL();

    public final static String TABLE = "user_db_pri";
    public final static String DBID = "db_id";
    public final static String USERID = "db_user_id";
    public final static String SPACEID = "space_id";
    public final static String PRI = "pri";//0 read only ;1 read write

    public final static int READONLY = 0;
    public final static int READWRITE = 1;

    private UserDbPriL() {
    }

    public static UserDbPriL me() {
        return pri;
    }

    public List<Record> getByUser(int uid, int spaceid) {
        return Db.use(DSBox.LOCAL).find("select * from " + TABLE +
                " where " + USERID + "=? and " + SPACEID + " =?", uid, spaceid);
    }

    public boolean addDbPriToUser(int uid, int dbid, int pri, int spaceid) {
        return Db.use(DSBox.LOCAL).update("insert into " + TABLE + "(" + DBID + "," + USERID + "," + SPACEID + "," + PRI + ")"
                + " values(?,?,?,?)", dbid, uid, spaceid, pri) >= 0;
    }

    public boolean delDbPriFromUser(int uid, int dbid, int spaceid) {
        return Db.use(DSBox.LOCAL).update("delete from " + TABLE +
                " where " + USERID + "=? and " + DBID + "=? and " + SPACEID + "=?", uid, dbid, spaceid) >= 0;

    }

    public boolean upUserPriToDB(int uid, int dbid, int pri, int spaceid) {
        String sql = "update " + TABLE + " set " + PRI + "=?" +
                " where " + USERID + "=? and " + DBID + "=? and " + SPACEID + "=?";
        return Db.use(DSBox.LOCAL).update(sql, pri, uid, dbid, spaceid) >= 0;
    }

    public boolean delUserPri(int uid) {
        return Db.use(DSBox.LOCAL).update("delete from " + TABLE + " where " + USERID + "=?", uid) >= 0;
    }

    public boolean delDbPri(int dbid) {
        return Db.use(DSBox.LOCAL).update("delete from " + TABLE + " where " + DBID + "=?", dbid) >= 0;
    }

    public boolean delSpacePri(int spaceid) {
        return Db.use(DSBox.LOCAL).update("delete from " + TABLE + " where " + SPACEID + "=?", spaceid) >= 0;
    }
}
