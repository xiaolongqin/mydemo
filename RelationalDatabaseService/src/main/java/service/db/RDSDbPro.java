package service.db;

import com.jfinal.plugin.activerecord.*;
import com.jfinal.plugin.activerecord.dialect.Dialect;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

@SuppressWarnings("unchecked")
public class RDSDbPro {
    private ThreadLocal<Connection> threadLocal = new ThreadLocal<>();
    private static Dialect dialect = new MysqlDialect();
    private static int TRNSAACTION_LEVEL = 2;
    private static final Logger LOGGER = LoggerFactory.getLogger(RDSDbPro.class);
    private static final Object[] NULL_PARA_ARRAY = new Object[0];

    private static void closeQuietly(ResultSet rs, Statement st) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("can not close ResultSet:" + e.getMessage());
                }
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("can not close Statement:" + e.getMessage());
                }
            }
        }
    }

    private static void closeQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("can not close connection:" + e.getMessage());
                }
            }
        }
    }

    private static void closeQuietly(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("can not close connection:" + e.getMessage());
                }
            }
        }
    }

    private static void fillStatement(PreparedStatement pst, Object... paras) throws SQLException {
        for (int i = 0; i < paras.length; i++) {
            pst.setObject(i + 1, paras[i]);
        }
    }

    /**
     * prepare for transaction
     */
    private Connection validateConnection(Connection conn) {
        if (conn == null) {
            if (threadLocal.get() == null) {
                throw new RuntimeException("no connection to run method");
            }
            return threadLocal.get();
        }
        if (threadLocal.get() != null) {
            closeQuietly(conn);
            throw new RuntimeException("can't assign another connection to do transaction");
        }
        return conn;
    }

    private boolean isTx() {
        return threadLocal.get() != null;
    }

    <T> List<T> query(Connection conn, String sql, Object... paras) {
        conn = validateConnection(conn);
        try {
            List result = new ArrayList();
            PreparedStatement pst = conn.prepareStatement(sql);
            fillStatement(pst, paras);
            ResultSet rs = pst.executeQuery();
            int colAmount = rs.getMetaData().getColumnCount();
            if (colAmount > 1) {
                while (rs.next()) {
                    Object[] temp = new Object[colAmount];
                    for (int i = 0; i < colAmount; i++) {
                        temp[i] = rs.getObject(i + 1);
                    }
                    result.add(temp);
                }
            } else if (colAmount == 1) {
                while (rs.next()) {
                    result.add(rs.getObject(1));
                }
            }
            closeQuietly(rs, pst);
            return result;
        } catch (Exception ex) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("SQLException:" + ex.getMessage());
            }
            throw new RuntimeException(ex);
        } finally {
            if (!isTx()) {
                closeQuietly(conn);
            }
        }
    }


    public <T> List<T> query(Connection connection, String sql) {
        return query(connection, sql, NULL_PARA_ARRAY);
    }

    public <T> T queryFirst(Connection conn, String sql, Object... paras) {
        List<T> result = query(conn, sql, paras);
        return (result.size() > 0 ? result.get(0) : null);
    }

    public <T> T queryFirst(Connection conn, String sql) {
        List<T> result = query(conn, sql, NULL_PARA_ARRAY);
        return (result.size() > 0 ? result.get(0) : null);
    }

    // 26 queryXxx method below -----------------------------------------------

    public <T> T queryColumn(Connection conn, String sql, Object... paras) {
        List<T> result = query(conn, sql, paras);
        if (result.size() > 0) {
            T temp = result.get(0);
            if (temp instanceof Object[])
                throw new RuntimeException("Only ONE COLUMN can be queried.");
            return temp;
        }
        return null;
    }

    public <T> T queryColumn(Connection conn, String sql) {
        return (T) queryColumn(conn, sql, NULL_PARA_ARRAY);
    }

    public String queryStr(Connection conn, String sql, Object... paras) {
        return (String) queryColumn(conn, sql, paras);
    }

    public String queryStr(Connection conn, String sql) {
        return (String) queryColumn(conn, sql, NULL_PARA_ARRAY);
    }

    public Integer queryInt(Connection conn, String sql, Object... paras) {
        return (Integer) queryColumn(conn, sql, paras);
    }

    public Integer queryInt(Connection conn, String sql) {
        return (Integer) queryColumn(conn, sql, NULL_PARA_ARRAY);
    }

    public Long queryLong(Connection conn, String sql, Object... paras) {
        return (Long) queryColumn(conn, sql, paras);
    }

    public Long queryLong(Connection conn, String sql) {
        return (Long) queryColumn(conn, sql, NULL_PARA_ARRAY);
    }

    public Double queryDouble(Connection conn, String sql, Object... paras) {
        return (Double) queryColumn(conn, sql, paras);
    }

    public Double queryDouble(Connection conn, String sql) {
        return (Double) queryColumn(conn, sql, NULL_PARA_ARRAY);
    }

    public Float queryFloat(Connection conn, String sql, Object... paras) {
        return (Float) queryColumn(conn, sql, paras);
    }

    public Float queryFloat(Connection conn, String sql) {
        return (Float) queryColumn(conn, sql, NULL_PARA_ARRAY);
    }

    public java.math.BigDecimal queryBigDecimal(Connection conn, String sql, Object... paras) {
        return (java.math.BigDecimal) queryColumn(conn, sql, paras);
    }

    public java.math.BigDecimal queryBigDecimal(Connection conn, String sql) {
        return (java.math.BigDecimal) queryColumn(conn, sql, NULL_PARA_ARRAY);
    }

    public byte[] queryBytes(Connection conn, String sql, Object... paras) {
        return (byte[]) queryColumn(conn, sql, paras);
    }

    public byte[] queryBytes(Connection conn, String sql) {
        return (byte[]) queryColumn(conn, sql, NULL_PARA_ARRAY);
    }

    public java.util.Date queryDate(Connection conn, String sql, Object... paras) {
        return (java.util.Date) queryColumn(conn, sql, paras);
    }

    public java.util.Date queryDate(Connection conn, String sql) {
        return (java.util.Date) queryColumn(conn, sql, NULL_PARA_ARRAY);
    }

    public java.sql.Time queryTime(Connection conn, String sql, Object... paras) {
        return (java.sql.Time) queryColumn(conn, sql, paras);
    }

    public java.sql.Time queryTime(Connection conn, String sql) {
        return (java.sql.Time) queryColumn(conn, sql, NULL_PARA_ARRAY);
    }

    public java.sql.Timestamp queryTimestamp(Connection conn, String sql, Object... paras) {
        return (java.sql.Timestamp) queryColumn(conn, sql, paras);
    }

    public java.sql.Timestamp queryTimestamp(Connection conn, String sql) {
        return (java.sql.Timestamp) queryColumn(conn, sql, NULL_PARA_ARRAY);
    }

    public Boolean queryBoolean(Connection conn, String sql, Object... paras) {
        return (Boolean) queryColumn(conn, sql, paras);
    }

    public Boolean queryBoolean(Connection conn, String sql) {
        return (Boolean) queryColumn(conn, sql, NULL_PARA_ARRAY);
    }

    public Number queryNumber(Connection conn, String sql, Object... paras) {
        return (Number) queryColumn(conn, sql, paras);
    }

    public Number queryNumber(Connection conn, String sql) {
        return (Number) queryColumn(conn, sql, NULL_PARA_ARRAY);
    }
    // 26 queryXxx method under -----------------------------------------------

    /**
     * Execute sql update
     */
    int update(Connection conn, String sql, Object... paras) {
        conn = validateConnection(conn);
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            fillStatement(pst, paras);
            int result = pst.executeUpdate();
            closeQuietly(pst);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (!isTx()) {
                closeQuietly(conn);
            }
        }
    }

    public int update(Connection conn, String sql) {
        return update(conn, sql, NULL_PARA_ARRAY);
    }

    /**
     * Get id after insert method getGeneratedKey().
     */
    private Object getGeneratedKey(PreparedStatement pst) throws SQLException {
        ResultSet rs = pst.getGeneratedKeys();
        Object id = null;
        if (rs.next())
            id = rs.getObject(1);
        rs.close();
        return id;
    }

    public List<Record> find(Connection conn, String sql, Object... paras) {
        conn = validateConnection(conn);
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            fillStatement(pst, paras);
            ResultSet rs = pst.executeQuery();
            List<Record> result = RDSRecordBuilder.build(rs);
            closeQuietly(rs, pst);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (!isTx()) {
                closeQuietly(conn);
            }
        }
    }

    public List<Record> find(Connection conn, String sql) {
        return find(conn, sql, NULL_PARA_ARRAY);
    }

    public Record findFirst(Connection conn, String sql, Object... paras) {
        List<Record> result = find(conn, sql, paras);
        return result.size() > 0 ? result.get(0) : null;
    }

    public Record findFirst(Connection conn, String sql) {
        List<Record> result = find(conn, sql, NULL_PARA_ARRAY);
        return result.size() > 0 ? result.get(0) : null;
    }

    public Record findById(Connection conn, String tableName, Object idValue, String idColumn) {
        return findById(conn, tableName, idColumn, idValue, "*");
    }

    public Record findById(Connection conn, String tableName, Number idValue, String columns) {
        return findById(conn, tableName, dialect.getDefaultPrimaryKey(), idValue, columns);
    }

    public Record findById(Connection conn, String tableName, String primaryKey, Number idValue) {
        return findById(conn, tableName, primaryKey, idValue, "*");
    }

    public Record findById(Connection conn, String tableName, String primaryKey, Object idValue, String columns) {
        String sql = dialect.forDbFindById(tableName, primaryKey, columns);
        List<Record> result = find(conn, sql, idValue);
        return result.size() > 0 ? result.get(0) : null;
    }

    public boolean deleteById(Connection conn, String tableName, Object id) {
        return deleteById(conn, tableName, dialect.getDefaultPrimaryKey(), id);
    }

    public boolean deleteById(Connection conn, String tableName, String primaryKey, Object id) {
        if (id == null)
            throw new IllegalArgumentException("id can not be null");

        String sql = dialect.forDbDeleteById(tableName, primaryKey);
        return update(conn, sql, id) >= 1;
    }

    public boolean delete(Connection conn, String tableName, String primaryKey, Record record) {
        return deleteById(conn, tableName, primaryKey, record.get(primaryKey));
    }

    public boolean delete(Connection conn, String tableName, Record record) {
        String defaultPrimaryKey = dialect.getDefaultPrimaryKey();
        return deleteById(conn, tableName, defaultPrimaryKey, record.get(defaultPrimaryKey));
    }

    public Page<Record> paginate(Connection conn, int pageNumber, int pageSize, String select, String sqlExceptSelect, Object... paras) {
        conn = validateConnection(conn);
        try {
            if (pageNumber < 1 || pageSize < 1)
                throw new RuntimeException("pageNumber and pageSize must be more than 0");

            if (dialect.isTakeOverDbPaginate())
                return dialect.takeOverDbPaginate(conn, pageNumber, pageSize, select, sqlExceptSelect, paras);

            long totalRow = 0;
            int totalPage = 0;
            List result = query(conn, "select count(*) " + DbKit.replaceFormatSqlOrderBy(sqlExceptSelect), paras);
            int size = result.size();
            if (size == 1)
                totalRow = ((Number) result.get(0)).longValue();
            else if (size > 1)
                totalRow = result.size();
            else
                return new Page<Record>(new ArrayList<Record>(0), pageNumber, pageSize, 0, 0);

            totalPage = (int) (totalRow / pageSize);
            if (totalRow % pageSize != 0) {
                totalPage++;
            }

            // --------
            StringBuilder sql = new StringBuilder();
            dialect.forPaginate(sql, pageNumber, pageSize, select, sqlExceptSelect);
            List<Record> list = find(conn, sql.toString(), paras);
            return new Page<>(list, pageNumber, pageSize, totalPage, (int) totalRow);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (!isTx()) {
                closeQuietly(conn);
            }
        }
    }

    public Page<Record> paginate(Connection conn, int pageNumber, int pageSize, String select, String sqlExceptSelect) {
        return paginate(conn, pageNumber, pageSize, select, sqlExceptSelect, NULL_PARA_ARRAY);
    }

    public boolean save(Connection conn, String tableName, String primaryKey, Record record) {
        conn = validateConnection(conn);
        try {
            List<Object> paras = new ArrayList<Object>();
            StringBuilder sql = new StringBuilder();
            dialect.forDbSave(sql, paras, tableName, record);

            PreparedStatement pst;
            if (dialect.isOracle())
                pst = conn.prepareStatement(sql.toString(), new String[]{primaryKey});
            else
                pst = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);

            dialect.fillStatement(pst, paras);
            int result = pst.executeUpdate();
            record.set(primaryKey, getGeneratedKey(pst));
            closeQuietly(pst);
            return result >= 1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (!isTx()) {
                closeQuietly(conn);
            }
        }
    }

    public boolean save(Connection conn, String tableName, Record record) {
        return save(conn, tableName, dialect.getDefaultPrimaryKey(), record);
    }

    public boolean update(Connection conn, String tableName, String primaryKey, Record record) {
        conn = validateConnection(conn);
        try {
            Object id = record.get(primaryKey);
            if (id == null)
                throw new RuntimeException("You can't update model without Primary Key.");
            StringBuilder sql = new StringBuilder();
            List<Object> paras = new ArrayList<>();
            dialect.forDbUpdate(tableName, primaryKey, id, record, sql, paras);

            if (paras.size() <= 1) {    // Needn't update
                return false;
            }
            return update(conn, sql.toString(), paras.toArray()) >= 1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (!isTx()) {
                closeQuietly(conn);
            }
        }
    }

    public boolean update(Connection conn, String tableName, Record record) {
        return update(conn, tableName, dialect.getDefaultPrimaryKey(), record);
    }

    public boolean tx(Connection conn, int transactionLevel, RDSIAtom atom) {
        try {
            if (conn.getTransactionIsolation() < transactionLevel)
                conn.setTransactionIsolation(transactionLevel);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        threadLocal.set(conn);
        Boolean autoCommit = null;
        try {
            autoCommit = conn.getAutoCommit();
            conn.setTransactionIsolation(transactionLevel);
            conn.setAutoCommit(false);
            boolean result = atom.run(conn);
            if (result)
                conn.commit();
            else
                conn.rollback();
            return result;
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception e1) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("transaction can't rollback:" + e1.getMessage());
                }
                e1.printStackTrace();
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (autoCommit != null)
                    conn.setAutoCommit(autoCommit);
                conn.close();
            } catch (Exception e) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("can't close conn in transaction:" + e.getMessage());
                }
                e.printStackTrace();    // can not throw exception here, otherwise the more important exception in previous catch block can not be thrown
            } finally {
                threadLocal.remove();
            }
        }
    }

    public boolean tx(Connection conn, RDSIAtom atom) {
        return tx(conn, TRNSAACTION_LEVEL, atom);
    }

    public List<String> getTables(String db, Connection connection) {
        try {
            List<String> tables = new ArrayList<>();
            DatabaseMetaData data = connection.getMetaData();
            ResultSet set = data.getTables(db, null, null, new String[]{"TABLE"});
            while (set.next()) {
                tables.add(set.getString("TABLE_NAME"));
            }
            return tables;
        } catch (Exception ex) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("SQLException:" + ex.getMessage());
            }
            throw new RuntimeException(ex);
        } finally {
            closeQuietly(connection);
        }
    }

    public Map<String, Object> getMetaData(String db, String table, Connection connection) throws SQLException {
        try {
            Map<String, Object> tableInfo = new HashMap<>();
            tableInfo.put("name", table);
            Map<String, Object> fieldsInfo = new HashMap<>();
            tableInfo.put("fields", fieldsInfo);
            DatabaseMetaData data = connection.getMetaData();
            //fields
            ResultSet fields = data.getColumns(db, null, table, null);
            while (fields.next()) {
                Map<String, Object> columnInfo = new HashMap<>();
                String filedName = fields.getString(4);
                columnInfo.put("fieldName", filedName);
                columnInfo.put("type", fields.getString(6));
                columnInfo.put("length", fields.getString(7));
                columnInfo.put("nullable", fields.getString(11));
                columnInfo.put("autoincrement", fields.getString(23));
                columnInfo.put("isPk", 0);
                columnInfo.put("union", 0);
                columnInfo.put("index", 0);
                columnInfo.put("fk", "");
                fieldsInfo.put(filedName, columnInfo);
            }
            fields.close();
            //primaryKey
            ResultSet primaryKey = data.getPrimaryKeys(db, null, table);
            while (primaryKey.next()) {
                String pkFieldName = primaryKey.getString(4);
                if (fieldsInfo.containsKey(pkFieldName)) {
                    ((Map<String, Object>) fieldsInfo.get(pkFieldName)).put("isPK", 1);
                }
            }
            primaryKey.close();
            //foreign key
            ResultSet fk = data.getExportedKeys(db, null, table);
            while (fk.next()) {
                String fkFieldName = fk.getString("FKCOLUMN_NAME");
                if (fieldsInfo.containsKey(fkFieldName)) {
                    String fkStr = new StringBuilder().append(fk.getString("PKTABLE_CAT"))
                            .append(".").append(fk.getString("PKTABLE_NAME"))
                            .append(".").append(fk.getString("PKCOLUMN_NAME")).toString();
                    ((Map<String, Object>) fieldsInfo.get(fkFieldName)).put("fk", fkStr);
                }
            }
            fk.close();
            //index
            ResultSet index = data.getIndexInfo(db, null, table, false, true);
            while (index.next()) {
                boolean nonUnique = index.getBoolean("NON_UNIQUE");
                String indexName = index.getString("INDEX_NAME");
                String indexColumnName = index.getString("COLUMN_NAME");
                if (!indexName.equals("PRIMARY")) {
                    if (fieldsInfo.containsKey(indexColumnName)) {
                        int type = nonUnique ? 0 : 1;
                        ((Map<String, Object>) fieldsInfo.get(indexColumnName)).put("fk", type);
                    }
                }
            }
            index.close();
            return tableInfo;
        } catch (Exception ex) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("SQLException:" + ex.getMessage());
            }
            throw new RuntimeException(ex);
        } finally {
            closeQuietly(connection);
        }
    }
}

