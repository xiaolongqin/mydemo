package utils;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * Created by Tyfunwang on 2015/8/10.
 */
public class GetConnection {
    public static  String TABLE_NAME = null;
    static Properties properties = new Properties();
    static {
        try {
            properties.load(GetConnection.class.getClassLoader().getResourceAsStream("jdbc.properties"));
            TABLE_NAME = properties.getProperty("tableName") + FormatUtils.getMonth(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //jdbc
    public static Connection getConnectionJdbc() {
        Connection connection = null;
        try {
            Class.forName(properties.getProperty("jdbcDriver"));
            connection = DriverManager.getConnection(properties.getProperty("jdbcUrl"), properties.getProperty("jdbcName"), properties.getProperty("jdbcPass"));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    //hive_jdbc
    public static Connection getConnectionHive() {
        Connection connection = null;
        try {
            Class.forName(properties.getProperty("hiveDriver"));
            connection = DriverManager.getConnection(properties.getProperty("hiveUrl"),"","" );//properties.getProperty("hiveName")properties.getProperty("hivePass")
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }


    //close jdbc 
    public static void closeConnection(ResultSet resultSet,Statement statement,Connection connection) {
        try {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //close hive
    public static void closeConnection(Statement statement,Connection connection) {
        try {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
