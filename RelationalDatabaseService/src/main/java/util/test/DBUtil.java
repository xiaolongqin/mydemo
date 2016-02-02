package util.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.mysql.jdbc.DatabaseMetaData;
import model.local.DBUserL;
import model.local.DataBaseL;
import model.local.SpaceL;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * Created by Administrator on 2014/10/17.
 */
public class DBUtil {
    public static void start() {
        Properties p = new Properties();
        try {
            p.load(DBUtil.class.getClassLoader().getResourceAsStream("jdbc.properties"));
            C3p0Plugin local = new C3p0Plugin(p.getProperty("local_url"), p.getProperty("local_user"), p.getProperty("local_password"));
            C3p0Plugin remote = new C3p0Plugin(p.getProperty("remote_url"), p.getProperty("remote_user"), p.getProperty("remote_password"));
            local.start();
            remote.start();
            ActiveRecordPlugin arpLocal = new ActiveRecordPlugin("local", local);
            ActiveRecordPlugin arpRemote = new ActiveRecordPlugin("remote", remote);
            arpLocal.addMapping(DataBaseL.TABLE, DataBaseL.class);
            arpLocal.addMapping(DBUserL.TABLE, DBUserL.class);
            arpLocal.addMapping(SpaceL.TABLE, SpaceL.class);
            arpLocal.start();
            arpRemote.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        DruidDataSource ds = new DruidDataSource();
        ds.setUrl("jdbc:mysql://localhost:3306/");
        ds.setUsername("root");
        ds.setPassword("root");
        System.out.println(System.getProperty("file.encoding"));
        DruidPooledConnection conn = ds.getConnection();
        DatabaseMetaData metadata = (DatabaseMetaData) conn.getMetaData();
        ResultSet index = metadata.getIndexInfo("rds", null, "test21", false, true);
        while (index.next()) {
            System.out.println(index.getString("NON_UNIQUE"));
            System.out.println(index.getString("INDEX_NAME"));
            System.out.println(index.getString("COLUMN_NAME"));
            System.out.println(index.getString("TYPE"));
            System.out.println();
        }
        index.close();
        conn.disable();
        conn.getConnection().close();
        conn.close();
        System.out.println("连接已关闭");
    }
}

