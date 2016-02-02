package model.cluster;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import model.local.DBUserL;
import model.local.UserDbPriL;
import service.DSBox;

import java.sql.SQLException;

/**
 * Created by liweiqi on 2014/10/23.
 */
public class DBUserR {
    private static DBUserR dao = new DBUserR();
    private final String readPri = "Y,N,N,N,N,N,N,N,N,N,N,N,N,N,N,N,N,N,N";
    private final String writePri = "Y,Y,Y,Y,N,N,N,N,N,N,N,N,N,N,N,N,N,N,N";
    private final String allPri = "Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y";

    public static DBUserR me() {
        return dao;
    }

    private DBUserR() {
    }

    public boolean createUser(String name, String pass) {
        StringBuilder sql = new StringBuilder("CREATE USER '").append(name).append("'@'%' IDENTIFIED BY '").append(pass).append("'");
        return Db.use(DSBox.REMOTE).update(sql.toString()) >= 0;
    }

    public boolean deleteUser(String name) {
        return Db.use(DSBox.REMOTE).find("select 1 from mysql.user where user=? and host='%'", name).size() == 0 || Db.use(DSBox.REMOTE).update("drop user '" + name + "'@'%'") >= 0;
    }

    public boolean upPassword(String name, String pass) {
        String sql = "SET PASSWORD FOR '" + name + "'@'%' = PASSWORD('" + pass + "');";
        return Db.use(DSBox.REMOTE).update(sql) >= 0;
    }

    /**
     * add all privileges of a db to a space_root user
     */
    public boolean addPriToRootUser(String uname, String dbname) {
        StringBuilder sql = new StringBuilder("insert into mysql.db values ('%',?,?,").append(allPri)
                .append(") where no exists (select 1 from mysql.db where Host='%' and User='" + uname + "')")
                .append("and Db='" + dbname + "'");
        boolean state = Db.use(DSBox.REMOTE).update(sql.toString(), dbname, uname) >= 0;
        return state;
    }

    /**
     * add a optional pri to a user,maybe readonly or readwrite
     */
    public boolean addUserPriToDB(String uname, String dbname, int pri) {
        String priciliges = pri == UserDbPriL.READWRITE ? writePri : readPri;
        StringBuilder sql = new StringBuilder("insert into mysql.db values ('%',?,?,").append(priciliges)
                .append(") where no exists (select 1 from mysql.db where Host='%' and User='" + uname + "')")
                .append("and Db='" + dbname + "'");
        return Db.use(DSBox.REMOTE).update(sql.toString(), dbname, uname) >= 0;
    }

    public boolean revokeUserPriFromDB(String uname, String dbname) {
        StringBuilder sql = new StringBuilder("delete from mysql.db where User=? and Db=?");
        return Db.use(DSBox.REMOTE).update(sql.toString(), uname, dbname) >= 0;
    }

    public boolean revokeUnrootPriFromDB(String dbname) {
        StringBuilder sql = new StringBuilder("delete from mysql.db where Db=? and User not REGEXP ‘root$'");
        return Db.use(DSBox.REMOTE).update(sql.toString(), dbname) >= 0;
    }

    public boolean revokeAllPriFromDB(String dbname) {
        StringBuilder sql = new StringBuilder("delete from mysql.db where Db=?");
        return Db.use(DSBox.REMOTE).update(sql.toString(), dbname) >= 0;
    }

    public boolean revokeRootPriFromDB(String dbname) {
        StringBuilder sql = new StringBuilder("delete from mysql.db where Db=? and User REGEXP ‘root$'");
        return Db.use(DSBox.REMOTE).update(sql.toString(), dbname) >= 0;
    }

    public boolean upToPri(final String uname, final String dbname, final int pri) {
        return Db.use(DSBox.REMOTE).tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                return revokeUserPriFromDB(uname, dbname) && addUserPriToDB(uname, dbname, pri);
            }
        });
    }


}
