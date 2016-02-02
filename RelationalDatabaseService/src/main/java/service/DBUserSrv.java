package service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import model.cluster.DBUserR;
import model.local.DBUserL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.PassFactory;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2014/10/16.
 */
public class DBUserSrv {
    Logger logger = LoggerFactory.getLogger(DBUserSrv.class);
    private static DBUserSrv srv = new DBUserSrv();
    private DBUserL dbUserL = DBUserL.dao;
    private DBUserR dbUserR = DBUserR.me();
    private DBPriSrv dbPriSrv = DBPriSrv.me();

    public static DBUserSrv me() {
        return srv;
    }

    private DBUserSrv() {
    }


    public Page<DBUserL> allPage(int sid, int number, int size) {
        return dbUserL.getBySpacePage(sid, number, size);
    }

    public DBUserL getById(int id) {
        return dbUserL.findById(id);
    }

    public DBUserL getSpaceRootUser(int spaceId) {
        return dbUserL.getSpaceRoot(spaceId);
    }


    /**
     * return all the pri-record in this space ,about username,dbname,pri
     */
    public List<Record> getUserAndPriOfSpace(int spaceId) {
        return dbUserL.getUserAndPriOfSpace(spaceId);
    }

    /**
     * return all the pri-record in this db ,about username,dbname,pri
     */
    public List<Record> getUserAndPriOfDb(int dbid) {
        return dbUserL.getUserAndPriOfDb(dbid);
    }

    /**
     * add a user both at local mysql(meta-info) adn remote cluster with no privileges
     */
    public DBUserL add(final Map<String, Object> attrs) {
        final DBUserL newUser = new DBUserL();
        boolean state = Db.use(DSBox.LOCAL).tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                DBUserL tmp = dbUserL.add(attrs);
                if (tmp == null) return false;
                newUser.setAttrs(tmp);
                String name = newUser.getStr(DBUserL.NAME);
                String pass = newUser.getStr(DBUserL.PASS);
                return dbUserR.createUser(name, pass);
            }
        });
        return state ? newUser : null;
    }

    /**
     * add a space root user
     */
    public boolean addSpaceRoot(int spaceid) {
        final String user_name = buildRootUserName(spaceid);
        final String password = PassFactory.createRandomPass(7);
        final Map<String, Object> attrs = new HashMap<>();
        attrs.put(DBUserL.NAME, user_name);
        attrs.put(DBUserL.PASS, password);
        attrs.put(DBUserL.SPACEID, spaceid);
        attrs.put(DBUserL.DESC, "空间管理员");
        attrs.put(DBUserL.ROLE, 0);
        return Db.use(DSBox.LOCAL).tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                return dbUserL.add(attrs) != null && dbUserR.createUser(user_name, password);
            }
        });
    }

    /**
     * del the db-user both at local mysql(meta-info) and remote cluster(real-db)
     */
    public boolean del(final int id, final String name) {
        return Db.use(DSBox.LOCAL).tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                return dbUserL.deleteById(id) && dbPriSrv.delUserPri(id) && dbUserR.deleteUser(name);
            }
        });
    }


    /**
     * del db both at local and remote ,can't roll back if any operation failed
     */
    public boolean delBySpace(int spaceid) {
        List<DBUserL> list = dbUserL.getBySpace(spaceid);
        for (DBUserL user : list) {
            boolean state = del(user.getNumber(DBUserL.ID).intValue(), user.getStr(DBUserL.NAME));
            if (state) return false;
        }
        return true;
    }


    /**
     * update user-info at local mysql and user's password at remote cluster
     */
    public boolean up(final Map<String, Object> attrs) {
        return Db.use(DSBox.LOCAL).tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                int id = (int) attrs.get(DBUserL.ID);
                String oldPass = dbUserL.findById(id).getStr(DBUserL.PASS);
                DBUserL upUser = new DBUserL().setAttrs(attrs);
                if (!upUser.update()) return false;
                String name = upUser.getStr(DBUserL.NAME);
                String pass = upUser.getStr(DBUserL.PASS);
                return pass.equals(oldPass) || dbUserR.upPassword(name, pass);
            }
        });
    }


    public boolean upPassword(final Map<String, Object> attrs) {
        return Db.use(DSBox.LOCAL).tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                String name = attrs.get(DBUserL.NAME).toString();
                String pass = attrs.get(DBUserL.PASS).toString();
                return up(attrs) && dbUserR.upPassword(name, pass);
            }
        });

    }

    private String buildRootUserName(int spaceid) {
        return "su" + spaceid + "_root";
    }

}
