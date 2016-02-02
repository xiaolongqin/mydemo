package service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import model.cluster.DBUserR;
import model.local.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2014/10/16.
 */
public class SpaceSrv {
    static Logger logger = LoggerFactory.getLogger(SpaceSrv.class);
    private static SpaceSrv spaceSrv = new SpaceSrv();
    private SpaceL spaceL = SpaceL.dao;
    private DBSrv dbsrv = DBSrv.me();
    private DBUserSrv usrv = DBUserSrv.me();
    private DBPriSrv dbPriSrv = DBPriSrv.me();
    private OrderSrv orderSrv = OrderSrv.me();

    private SpaceSrv() {
    }

    public static SpaceSrv me() {
        return spaceSrv;
    }

    public Page<SpaceL> all(int uid, int number, int size) {
        return spaceL.all(uid, number, size);
    }

    public List<SpaceL> allInUse() {
        return spaceL.allInUse();
    }

    public SpaceL getById(int id) {
        return spaceL.findById(id);
    }

    /**
     * add a out-date space;
     * dd a order
     * add a space-root-user;
     */
    public SpaceL addSpace(final Map<String, Object> spAttrs, final Map<String, Object> odAttrs) {
        final SpaceL newSpace = new SpaceL();
        boolean state = Db.use(DSBox.LOCAL).tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                SpaceL tmp = spaceL.add(spAttrs);
                if (tmp == null) return false;
                newSpace.setAttrs(tmp);
                if (orderSrv.addOrder(odAttrs) == null) return false;
                int newId = newSpace.getNumber(SpaceL.ID).intValue();
                return usrv.addSpaceRoot(newId);
            }
        });
        return state ? newSpace : null;
    }

    /**
     * del the space and it's user and db both in local and remote
     */
    public boolean del(int spaceid, int account_id) {
        if (!dbPriSrv.delSpacePri(spaceid)) return false;
        boolean state1 = dbsrv.delBySpace(spaceid);
        boolean state2 = usrv.delBySpace(spaceid);
        boolean state3 = DBUserR.me().deleteUser("s" + spaceid + "_root");
        if (state1 && state2 && state3) {
            return spaceL.deleteById(spaceid);
        }
        return false;
    }

    /**
     * simply del the space record ,only used in the condition that create-new-space order is refused
     */
    public boolean simpleDel(int spaceid) {
       return spaceL.deleteById(spaceid);
    }

    public boolean up(Map<String, Object> attrs) {
        return new SpaceL().setAttrs(attrs).update();
    }

    /**
     * stupid operation to limit space's use
     * if space has already limited or has out of use,return true soon
     */
    public boolean forbidSpace(final int spaceId) {
        boolean state = Db.use(DSBox.LOCAL).tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                SpaceL tmp = spaceL.findById(spaceId);
                if (tmp.getNumber(SpaceL.STATE).intValue() > 0) return true;
                boolean state = tmp.set(SpaceL.STATE, 2).update();
                final List<DataBaseL> dbs = dbsrv.getBySpace(spaceId);
                for (DataBaseL dbl : dbs) {
                    state = state && dbl.set(DataBaseL.STATE, 1).update();
                    if (!state) return false;
                }
                state = Db.use(DSBox.REMOTE).tx(new IAtom() {
                    @Override
                    public boolean run() throws SQLException {
                        for (DataBaseL dbl : dbs) {
                            String dbName = dbl.getStr(DataBaseL.NAME);
                            boolean state = dbPriSrv.revokeRootPriFromDB(dbName);
                            if (!state) return false;
                        }
                        return true;
                    }
                });
                return state;
            }
        });
        dbPriSrv.flushAndLog(state, logger);
        return state;
    }

    /**
     * stupid operation to open space's use
     */
    public boolean recoverySpace(final int spaceId) {
        boolean state = Db.use(DSBox.LOCAL).tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                SpaceL tmp = spaceL.findById(spaceId);
                if (tmp.getNumber(SpaceL.STATE).intValue() == 1) return true;
                boolean state = spaceL.findById(spaceId).set(SpaceL.STATE, 0).update();
                if (!state) return false;
                final List<DataBaseL> dbs = dbsrv.getBySpace(spaceId);
                for (DataBaseL dbl : dbs) {
                    state = state && dbl.set(DataBaseL.STATE, 0).update();
                }
                if (!state) return false;
                final List<Record> records = usrv.getUserAndPriOfSpace(spaceId);
                state = Db.use(DSBox.REMOTE).tx(new IAtom() {
                    @Override
                    public boolean run() throws SQLException {
                        for (Record record : records) {
                            String dbName = record.getStr(DataBaseL.NAME);
                            int pri = record.getNumber(UserDbPriL.PRI).intValue();
                            String userName = record.getStr(DBUserL.NAME);
                            boolean state = dbPriSrv.addUserPriToDB(userName, dbName, pri);
                            if (!state) return false;
                        }
                        return true;
                    }
                });
                return state;
            }
        });
        dbPriSrv.flushAndLog(state, logger);
        return state;
    }
}
