package sl.bigdata.util;

import com.alibaba.druid.pool.DruidDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
/**
 * Created by Tyfunwang on 2015/5/27.
 */
public class Connect {
    private static Connect connect = new Connect();
    private static FileInputStream fs=null;
    private Connect(){}
    public static Connect getInstance(){
        if (connect == null) return new Connect();
        return connect;
        
    }
    
    private static Properties properties = new Properties();
    private static DruidDataSource dataSource = null;//DruidDataSource

           static {
            try {
//                properties.load(Connect.class.getClassLoader().getResourceAsStream("jdbc.properties"));
                fs=new FileInputStream("/home/etl_szlsp/szl/BigdataPlatform/pro/ETL/data/rpt/daily/jdbc.properties");
                properties.load(fs);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (fs!=null){
                    try {
                        fs.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    }
//druid
    public  synchronized Connection getConnection() {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
    
    //jdbc
    public Connection getConnect() {
        Connection connection = null;
        try {
           Class.forName(properties.getProperty("driver"));
            connection = DriverManager.getConnection(properties.getProperty("mysqlUrl"), properties.getProperty("name"), properties.getProperty("password"));
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }


//    public static void main(String[] args) {
//        Connection connection = new Connect().getConnect();
//        try {
//            Statement statement = connection.createStatement();
//            String sql1 = "DROP TABLE IF EXISTS temp1_rpt_channel_aim1;" +
//                    "CREATE TABLE temp1_rpt_channel_aim1 AS " +
//                    "SELECT t.channel_apk_id,FROM_UNIXTIME(t.regtime,'%Y-%m-%d') AS ADDTIME, " +
//                    "       SUM(CASE WHEN t.aim_num IS NULL THEN 0 ELSE t.aim_num END) AS aim_num, " +
//                    "       SUM(CASE WHEN t.arrive_num IS NULL THEN 0 ELSE t.arrive_num END) AS arrive_num," +
//                    "       SUM(CASE WHEN t.reg_num IS NULL THEN 0 ELSE t.reg_num END) AS reg_num " +
//                    "   FROM sl_ods_reg_user_count t" +
//                    "  WHERE t.regtime >= UNIX_TIMESTAMP(DATE_SUB(CURDATE(),INTERVAL 1 DAY))" +
//                    "    AND t.regtime < UNIX_TIMESTAMP(CURDATE())" +
//                    "    AND (t.regtime IS NOT NULL OR t.regtime <> 0)" +
//                    "GROUP BY t.channel_apk_id ;";
//            System.out.println(statement.execute(sql1));
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}
