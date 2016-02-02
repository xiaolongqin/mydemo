package service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import model.cluster.DBUserR;
import model.local.UserDbPriL;
import org.slf4j.Logger;

import java.sql.SQLException;

/**
 * Created by liweiqi on 2014/12/16.
 */
public class DBPriSrv {
    private static DBPriSrv srv = new DBPriSrv();
    private UserDbPriL priL = UserDbPriL.me();
    private DBUserR dbUserR = DBUserR.me();

    public static DBPriSrv me() {
        return srv;
    }

    private DBPriSrv() {
    }

    /**
     * when do insert into mysql.db ,we should flush privileges
     *
     * @param state  to choose whether to do flush
     * @param logger should provide a logger to log when flush filed
     */
    public void flushAndLog(boolean state, Logger logger) {
        if (!state) return;
        if (!doFlush()) {
            if (logger.isErrorEnabled()) {
                logger.error("flush privileges failed,flush by hands plz");
            }
        }
    }

    private boolean doFlush() {
        boolean state = false;
        for (int i = 0; i < 10; i++) {
            state = Db.use(DSBox.REMOTE).update("flush privileges") >= 0;
            if (state) break;
        }
        return state;
    }

    /**
     * delete all pri record of this space
     *
     * @param spaceid the key of the space
     * @return the state of this db operation
     */
    public boolean delSpacePri(int spaceid) {
        return priL.delSpacePri(spaceid);
    }

    /**
     * del user pri in local ,used when del user both in local and remote
     * you just need to delete privilege record in local because remote db
     * automatically remove user's privileges when we use 'drop user..'
     */
    public boolean delUserPri(int id) {
        return priL.delUserPri(id);
    }

    /**
     * del pri on the db in local ,used when del db both in local and remote
     * you just need to delete privilege record in local because remote db
     * automatically remove db's privileges when we use 'drop database..'
     */
    public boolean delLocalPri(int id) {
        return priL.delDbPri(id);
    }

    public boolean addDbPriToUser(final String userName, final String dbName, final int spaceId, final int dbId, final int uid, final int role) {
        return Db.use(DSBox.LOCAL).tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                return srv.addDbPriToUser(uid, dbId, role, spaceId)
                        && DBUserR.me().addUserPriToDB(userName, dbName, role);
            }
        });
    }

    public boolean changeUserPri(final String userName, final String dbName, final int spaceId, final int dbId, final int uid, final int role) {
        return Db.use(DSBox.LOCAL).tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                return srv.upUserPriToDB(uid, dbId, role, spaceId)
                        && DBUserR.me().upToPri(userName, dbName, role);
            }
        });
    }

    public boolean revokeUserPriToDB(final String userName, final String dbName, final int spaceId, final int dbId, final int uid) {
        return Db.use(DSBox.LOCAL).tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                return srv.delDbPriFromUser(uid, dbId, spaceId)
                        && DBUserR.me().revokeUserPriFromDB(userName, dbName);
            }
        });
    }

    public boolean revokeRootPriFromDB(String dbName) {
        return dbUserR.revokeRootPriFromDB(dbName);
    }

    public boolean revokeUnrootPriFromDB(String dbName) {
        return dbUserR.revokeUnrootPriFromDB(dbName);
    }

    public boolean revokeAllPriFromDB(String dbName) {
        return dbUserR.revokeAllPriFromDB(dbName);
    }

    public boolean addUserPriToDB(String userName, String dbName, int pri) {
        return dbUserR.addUserPriToDB(userName, dbName, pri);
    }

    public boolean addPriToRootUser(String uname, String dbname) {
        return dbUserR.addPriToRootUser(uname, dbname);
    }

    /**
     * del the db's pri from user,in local
     */
    public boolean delDbPriFromUser(int uid, int dbid, int spaceid) {
        return priL.delDbPriFromUser(uid, dbid, spaceid);
    }

    /**
     * add a db's readOnly-pri or readWrite-pri to user in local db
     *
     * @param uid  the key of the user
     * @param dbid the key of the db
     * @param pri  READONLY or READWRITE
     */
    public boolean addDbPriToUser(int uid, int dbid, int pri, int spaceid) {
        return priL.addDbPriToUser(uid, dbid, pri, spaceid);
    }


    /**
     * update user's pri in local
     */
    public boolean upUserPriToDB(int uid, int dbid, int pri, int spaceid) {
        return priL.upUserPriToDB(uid, dbid, pri, spaceid);
    }

}
