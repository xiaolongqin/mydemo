package service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import model.cluster.DataBaseR;
import model.local.DBUserL;
import model.local.DataBaseL;
import model.local.UserDbPriL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2014/10/16.
 */
public class DBSrv {
    static Logger logger = LoggerFactory.getLogger(DBSrv.class);
    private static DBSrv srv = new DBSrv();
    private DataBaseL dataBaseL = DataBaseL.dao;
    private DataBaseR dataBaseR = DataBaseR.me();
    private DBUserSrv dbuSrv = DBUserSrv.me();
    private DBPriSrv dbpriSrv = DBPriSrv.me();

    public static DBSrv me() {
        return srv;
    }

    private DBSrv() {
    }

    public Page<DataBaseL> allPage(int sid, int number, int size) {
        return dataBaseL.getBySpacePage(sid, number, size);
    }

    /**
     * get db by user's pri
     *
     * @param number page number start with 1
     * @param size   page size
     */
    public Page<Record> getByUserPage(int uid, int spaceid, int number, int size) {
        return dataBaseL.getByDBUserPri(uid, spaceid, number, size);
    }

    /**
     * get dbs which user can access
     *
     * @param spaceid to validate safe,but it looks not needed
     */
    public List<DataBaseL> getByUserPri(int uid, int spaceid) {
        return dataBaseL.getOutDBUserPri(uid, spaceid);
    }

    /**
     * get all db-record's of a space
     */
    public List<DataBaseL> getBySpace(int spaceId) {
        return dataBaseL.getBySpace(spaceId);
    }

    /**
     * add a db both at local mysql(meta-info) and remote-cluster(real db)
     * damn it!
     * before all the works you should create a tablespace,we use tablespace to control db's disk storage
     * first you should add a db record at local mysql
     * then you should really add a db at remote mysql cluster
     * finally you should add space_root user's pri to this db（just at remote cluster,we don't need to record root user's
     * pri at local mysql）
     * every step is simple ,but you can't put these steps into a transaction even a distributed transaction
     * because there are three DDL sentences that cause implicit commit.So,you should  rollback by hands in some condition
     */
    public DataBaseL add(final Map<String, Object> attrs) {
        final DataBaseL newDB = new DataBaseL();
        boolean state = Db.use(DSBox.LOCAL).tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                //add to local
                String dbNmae = attrs.get(DataBaseL.NAME).toString();
                String tsName = buildTSName(dbNmae);
                attrs.put(DataBaseL.TABLESPACE, tsName);
                DataBaseL tmpDB = dataBaseL.add(attrs);
                if (tmpDB == null) return false;
                newDB.setAttrs(tmpDB);
                //add pri to root
                String dbName = newDB.get(DataBaseL.NAME);
                String dbCharset = newDB.get(DataBaseL.CHARSET);
                int fileSize = newDB.getNumber(DataBaseL.STORAGE).intValue() / 5;
                int spaceId = newDB.getNumber(DataBaseL.SPACEID).intValue();
                DBUserL root = dbuSrv.getSpaceRootUser(spaceId);
                String userName = root.getStr(DBUserL.NAME);
                // create table space
                if (!createTableSpace(buildTSName(dbName), fileSize)) return false;
                boolean creteState = false;
                boolean addRootPriState = false;
                try {
                    //add to remote
                    creteState = dataBaseR.createDataBase(dbName, dbCharset);
                    //add pri to root user
                    addRootPriState = dbpriSrv.addPriToRootUser(userName, dbName);
                    dbpriSrv.flushAndLog(addRootPriState, logger);
                } catch (Exception ex) {
                    logger.warn("add db=" + dbName + " failed :" + ex.getMessage());
                }
                if (creteState && addRootPriState) return true;
                //roll back by hands
                if (!dropTableSpace(tsName)) {
                    logger.error("[tx]can't roll back create tablespace=" + dbName);
                }
                if (!creteState && !dataBaseR.deleteDataBase(dbName)) {
                    logger.error("[tx]can't roll back create db operation while create db=" + dbName);
                }
                if (!addRootPriState && !dbpriSrv.revokeRootPriFromDB(dbName)) {
                    logger.error("[tx]can't roll back grant pri to user=" + userName + " while create db=" + dbName);
                }
                return false;
            }
        });
        dbpriSrv.flushAndLog(state, logger);
        return state ? newDB : null;
    }

    /**
     * del the db both in local(meta-info) and remote(real-db)
     * first:del db-record and pri-record at local mysql
     * second:del all pri on db at remote cluster
     * finally:delete db at remote cluster
     * cant ensure the table space must being deleted
     */
    public boolean del(final int id, final String dbname) {
        return Db.use(DSBox.LOCAL).tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                if (!(dataBaseL.deleteById(id) && dbpriSrv.delLocalPri(id))) return false;
                boolean state = Db.use(DSBox.REMOTE).tx(new IAtom() {
                    @Override
                    public boolean run() throws SQLException {
                        if (!dbpriSrv.revokeAllPriFromDB(dbname)) return false;
                        if (!dataBaseR.deleteDataBase(dbname)) return false;
                        if (!dropTableSpace(buildTSName(dbname))) {
                            logger.error("[drop]can't drop table space=" + buildTSName(dbname) + "  while del db=" + dbname);
                        }
                        return true;
                    }
                });
                dbpriSrv.flushAndLog(state, logger);
                return state;
            }
        });
    }

    /**
     * del db both at local mysql and remote cluster,can't roll back if any operation failed
     * because almost every operation is DDL
     */
    public boolean delBySpace(int spaceid) {
        List<DataBaseL> list = dataBaseL.getBySpace(spaceid);
        boolean state = true;
        try {
            for (DataBaseL db : list) {
                String dbName = db.getStr(DataBaseL.NAME);
                boolean state1 = del(db.getNumber(DataBaseL.ID).intValue(), dbName);
                if (!state1 && logger.isErrorEnabled()) {
                    logger.error("can't delete db[name=" + dbName + "] while drop space");
                }
                state = state && state1;
            }
        } catch (Exception ex) {
            state = false;
        }
        return state;
    }


    /**
     * revoke all the unroot user's  pri  on this db at remote cluster
     * but not del pri-record at local in order to recovery operation
     */
    public boolean forbidDB(final int id, final String name) {
        boolean state = Db.use(DSBox.LOCAL).tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                return dataBaseL.findById(id).set(DataBaseL.STATE, DataBaseL.NOTUSE).save() && dbpriSrv.revokeUnrootPriFromDB(name);
            }
        });
        dbpriSrv.flushAndLog(state, logger);
        return state;
    }

    /**
     * recovery all pri on this db
     * transaction safe
     */
    public boolean recoveryDB(final String dbname, final int dbid) {
        final List<Record> records = dbuSrv.getUserAndPriOfDb(dbid);
        boolean state = Db.use(DSBox.LOCAL).tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                boolean state1 = dataBaseL.findById(dbid).set(DataBaseL.STATE, DataBaseL.NORMAL).save();
                boolean state2 = true;
                if (records.size() > 0) {
                    state2 = Db.use(DSBox.LOCAL).tx(new IAtom() {
                        @Override
                        public boolean run() throws SQLException {
                            for (Record record : records) {
                                int pri = record.getNumber(UserDbPriL.PRI).intValue();
                                String username = record.getStr(DBUserL.NAME);
                                boolean state = dbpriSrv.addUserPriToDB(username, dbname, pri);
                                if (!state) return false;
                            }
                            return true;
                        }
                    });
                }
                return state1 && state2;
            }
        });
        dbpriSrv.flushAndLog(state, logger);
        return state;
    }

    public boolean createTableSpace(String tsName, int fileSize) {
        return true;
    }

    public boolean dropTableSpace(String tsName) {

        return true;
    }

    private String buildTSName(String dbName) {
        return dbName + "_ts";
    }
}
