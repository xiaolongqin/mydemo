package service.db;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by liweiqi on 2014/12/1.
 */
public class RDSDb {
    static RDSDbPro pro = new RDSDbPro();

    static <T> List<T> query(Connection conn, String sql, Object... paras) throws SQLException {
        return pro.query(conn, sql, paras);
    }

    public static <T> List<T> query(Connection conn, String sql) {
        return pro.query(conn, sql);
    }

    public static <T> T queryFirst(Connection conn, String sql, Object... paras) {
        return pro.queryFirst(conn, sql, paras);
    }

    public static <T> T queryFirst(Connection conn, String sql) {
        return pro.queryFirst(conn, sql);
    }

    // 26 queryXxx method below -----------------------------------------------

    /**
     * Execute sql query just return one column.
     *
     * @param <T>   the type of the column that in your sql's select statement
     * @param sql   an SQL statement that may contain one or more '?' IN parameter placeholders
     * @param paras the parameters of sql
     * @return List<T>
     */
    public static <T> T queryColumn(Connection conn, String sql, Object... paras) {
        return pro.queryColumn(conn, sql, paras);
    }

    public static <T> T queryColumn(Connection conn, String sql) {
        return pro.queryColumn(conn, sql);
    }

    public static String queryStr(Connection conn, String sql, Object... paras) {
        return pro.queryStr(conn, sql, paras);
    }

    public static String queryStr(Connection conn, String sql) {
        return pro.queryStr(conn, sql);
    }

    public static Integer queryInt(Connection conn, String sql, Object... paras) {
        return pro.queryInt(conn, sql, paras);
    }

    public static Integer queryInt(Connection conn, String sql) {
        return pro.queryInt(conn, sql);
    }

    public static Long queryLong(Connection conn, String sql, Object... paras) {
        return pro.queryLong(conn, sql, paras);
    }

    public static Long queryLong(Connection conn, String sql) {
        return pro.queryLong(conn, sql);
    }

    public static Double queryDouble(Connection conn, String sql, Object... paras) {
        return pro.queryDouble(conn, sql, paras);
    }

    public static Double queryDouble(Connection conn, String sql) {
        return pro.queryDouble(conn, sql);
    }

    public static Float queryFloat(Connection conn, String sql, Object... paras) {
        return pro.queryFloat(conn, sql, paras);
    }

    public static Float queryFloat(Connection conn, String sql) {
        return pro.queryFloat(conn, sql);
    }

    public static java.math.BigDecimal queryBigDecimal(Connection conn, String sql, Object... paras) {
        return pro.queryBigDecimal(conn, sql, paras);
    }

    public static java.math.BigDecimal queryBigDecimal(Connection conn, String sql) {
        return pro.queryBigDecimal(conn, sql);
    }

    public static byte[] queryBytes(Connection conn, String sql, Object... paras) {
        return pro.queryBytes(conn, sql, paras);
    }

    public static byte[] queryBytes(Connection conn, String sql) {
        return pro.queryBytes(conn, sql);
    }

    public static java.util.Date queryDate(Connection conn, String sql, Object... paras) {
        return pro.queryDate(conn, sql, paras);
    }

    public static java.util.Date queryDate(Connection conn, String sql) {
        return pro.queryDate(conn, sql);
    }

    public static java.sql.Time queryTime(Connection conn, String sql, Object... paras) {
        return pro.queryTime(conn, sql, paras);
    }

    public static java.sql.Time queryTime(Connection conn, String sql) {
        return pro.queryTime(conn, sql);
    }

    public static java.sql.Timestamp queryTimestamp(Connection conn, String sql, Object... paras) {
        return pro.queryTimestamp(conn, sql, paras);
    }

    public static java.sql.Timestamp queryTimestamp(Connection conn, String sql) {
        return pro.queryTimestamp(conn, sql);
    }

    public static Boolean queryBoolean(Connection conn, String sql, Object... paras) {
        return pro.queryBoolean(conn, sql, paras);
    }

    public static Boolean queryBoolean(Connection conn, String sql) {
        return pro.queryBoolean(conn, sql);
    }

    public static Number queryNumber(Connection conn, String sql, Object... paras) {
        return pro.queryNumber(conn, sql, paras);
    }

    public static Number queryNumber(Connection conn, String sql) {
        return pro.queryNumber(conn, sql);
    }
    // 26 queryXxx method under -----------------------------------------------

    public static int update(Connection conn, String sql, Object... paras) {
        return pro.update(conn, sql, paras);
    }

    public static int update(Connection conn, String sql) {
        return pro.update(conn, sql);
    }

    public static List<Record> find(Connection conn, String sql, Object... paras) {
        return pro.find(conn, sql, paras);
    }

    public static List<Record> find(Connection conn, String sql) {
        return pro.find(conn, sql);
    }

    public static Record findFirst(Connection conn, String sql, Object... paras) {
        return pro.findFirst(conn, sql, paras);
    }

    public static Record findFirst(Connection conn, String sql) {
        return pro.findFirst(conn, sql);
    }


    public static Record findById(Connection conn, String tableName, Number idValue, String columns) {
        return pro.findById(conn, tableName, idValue, columns);
    }

    public static Record findById(Connection conn, String tableName, String primaryKey, Number idValue) {
        return pro.findById(conn, tableName, primaryKey, idValue);
    }

    public static Record findById(Connection conn, String tableName, String primaryKey, Object idValue, String columns) {
        return pro.findById(conn, tableName, primaryKey, idValue, columns);
    }

    public static boolean deleteById(Connection conn, String tableName, Object id) {
        return pro.deleteById(conn, tableName, id);
    }

    public static boolean deleteById(Connection conn, String tableName, String primaryKey, Object id) {
        return pro.deleteById(conn, tableName, primaryKey, id);
    }

    public static boolean delete(Connection conn, String tableName, String primaryKey, Record record) {
        return pro.delete(conn, tableName, primaryKey, record);
    }

    public static boolean delete(Connection conn, String tableName, Record record) {
        return pro.delete(conn, tableName, record);
    }


    public static Page<Record> paginate(Connection conn, int pageNumber, int pageSize, String select, String sqlExceptSelect, Object... paras) {
        return pro.paginate(conn, pageNumber, pageSize, select, sqlExceptSelect, paras);
    }

    public static Page<Record> paginate(Connection conn, int pageNumber, int pageSize, String select, String sqlExceptSelect) {
        return pro.paginate(conn, pageNumber, pageSize, select, sqlExceptSelect);
    }


    public static boolean save(Connection conn, String tableName, String primaryKey, Record record) {
        return pro.save(conn, tableName, primaryKey, record);
    }

    public static boolean save(Connection conn, String tableName, Record record) {
        return pro.save(conn, tableName, record);
    }

    public static boolean update(Connection conn, String tableName, String primaryKey, Record record) {
        return pro.update(conn, tableName, primaryKey, record);
    }

    public static boolean update(Connection conn, String tableName, Record record) {
        return pro.update(conn, tableName, record);
    }


    public static boolean tx(Connection conn, int transactionLevel, RDSIAtom atom) {
        return pro.tx(conn, transactionLevel, atom);
    }

    public static boolean tx(Connection conn, RDSIAtom atom) {
        return pro.tx(conn, atom);
    }


}
