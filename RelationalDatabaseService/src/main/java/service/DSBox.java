package service;

import com.alibaba.druid.pool.DruidDataSource;
import model.local.DBUserL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by liweiqi on 2014/12/1.
 * another measure to store user's DataSource and manager their lifecycle by HttpSession.
 * the data-source is created in need ,and destroyed when HttpSession be cleared,so that you can only focus on
 * the logic of creating and getting DataSource
 */
public class DSBox {
    public final static String LOCAL = "local_root";
    public final static String REMOTE = "remote_root";
    private static Properties p = new Properties();
    private static String APPENDSTR = "?characterEncoding = utf8 & autoReconnect = true & failOverReadOnly = false";
    public static final String NAMEINSESSION = "datasource";
    private static final Logger LOGGER = LoggerFactory.getLogger(DSBox.class);
    private Map<String, DruidDataSource> dsMap = new HashMap<>(5);
    private boolean isStopped = false;

    static {
        try {
            p.load(DSBox.class.getClassLoader().getResourceAsStream("jdbc.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * get connection by space_id,the user is space_root user
     *
     * @param spaceId the id of space
     * @return a connection,which has all privileges of this space's db
     * @throws java.sql.SQLException
     */
    public Connection getConnection(int spaceId) throws SQLException {
        if (isStopped) throw new RuntimeException("DataSource box has stopped");
        if (!dsMap.containsKey(spaceId)) {
            try {
                //find space and create DataSource,not complete
                DBUserL user = DBUserSrv.me().getSpaceRootUser(spaceId);
                String name = user.getStr(DBUserL.NAME);
                String pass = user.getStr(DBUserL.PASS);
                DruidDataSource dataSource = initDataSource(name, pass);
                dsMap.put(spaceId + "", dataSource);
            } catch (Exception ex) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("can not init DataSource from  space [space" + spaceId + "]:" + ex.getMessage());
                }
            }
        }

        return dsMap.get(spaceId).getConnection();
    }

    /**
     * get connection by space_id,the user is space_root user
     *
     * @param spaceId the id of space
     * @return a connection,which has all privileges of this space's db
     * @throws java.sql.SQLException
     */
    public Connection getConnection(int spaceId, String db) throws SQLException {
        if (isStopped) throw new RuntimeException("DataSource box has stopped");
        if (!dsMap.containsKey(spaceId + db)) {
            try {
                //find space and create DataSource,not complete
                DBUserL user = DBUserSrv.me().getSpaceRootUser(spaceId);
                String name = user.getStr(DBUserL.NAME);
                String pass = user.getStr(DBUserL.PASS);
                DruidDataSource dataSource = initDataSource(name, pass, db);
                dsMap.put(spaceId + db, dataSource);
            } catch (Exception ex) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("can not init DataSource from  space [space" + spaceId + "]:" + ex.getMessage());
                }
            }
        }

        return dsMap.get(spaceId).getConnection();
    }

    private DruidDataSource initDataSource(String name, String pass, String db) throws SQLException {
        DruidDataSource ds = new DruidDataSource();
        ds.setUsername(name);
        ds.setPassword(pass);
        ds.setUrl(p.get("remote_url").toString() + db + APPENDSTR);
        initDataSource(ds);
        ds.init();
        return ds;
    }

    /**
     * init DataSource with common properties
     *
     * @param name db's user name
     * @param pass user's pass
     * @return the DruidDataSource instance
     * @throws java.sql.SQLException
     */
    private DruidDataSource initDataSource(String name, String pass) throws SQLException {
        DruidDataSource ds = new DruidDataSource();
        ds.setUsername(name);
        ds.setPassword(pass);
        ds.setUrl(p.get("remote_url").toString() + APPENDSTR);
        initDataSource(ds);
        ds.init();
        return ds;
    }

    private void initDataSource(DruidDataSource ds) {
        if (ds == null) return;
        ds.setMaxActive(3);
        ds.setMinIdle(1);
        ds.setInitialSize(1);
        ds.setValidationQuery("select 1");
        ds.setTestWhileIdle(true);
        ds.setTestOnBorrow(false);
        ds.setTestOnReturn(false);
        ds.setMinEvictableIdleTimeMillis(3 * 60 * 1000);
        ds.setTimeBetweenEvictionRunsMillis(60 * 1000);
    }


    public void stop() {
        try {
            for (DruidDataSource source : dsMap.values()) {
                source.close();
            }
            isStopped = true;
        } catch (Exception ex) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("DataSource close failed:" + ex.getMessage());
            }
        }
    }
}
