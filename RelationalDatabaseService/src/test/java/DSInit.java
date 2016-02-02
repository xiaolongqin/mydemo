import com.alibaba.druid.pool.DruidDataSource;

import java.sql.SQLException;

/**
 * Created by liweiqi on 2014/12/2.
 */
public class DSInit {
    public static DruidDataSource init() throws SQLException {
        DruidDataSource ds = new DruidDataSource();
        ds.setUsername("root");
        ds.setPassword("root");
        ds.setUrl("jdbc:mysql://localhost:3306/sbm");
        ds.setMaxActive(3);
        ds.setMinIdle(1);
        ds.setTestWhileIdle(true);
        ds.setValidationQuery("select 1");
        ds.init();
        return ds;
    }
}
