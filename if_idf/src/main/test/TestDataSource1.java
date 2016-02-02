import org.junit.Test;
import utils.GetConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Tyfunwang on 2015/8/10.
 */
public class TestDataSource1 {

@Test
    public void getDataHive() {
        Connection connection = GetConnection.getConnectionHive();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            String sql1 = "select * from record.sl_ods_page_visit_record_201508 order by id ";
            resultSet = statement.executeQuery(sql1);
            while (resultSet.next()) {
                System.out.println("uid:" + resultSet.getInt("sl_ods_page_visit_record_201508.uid") 
                        + "contents:" + resultSet.getString("sl_ods_page_visit_record_201508.contents"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            GetConnection.closeConnection(resultSet, statement, connection);
        }
    }
    public void getData1() {
        Connection connection = GetConnection.getConnectionJdbc();
        Statement statement = null;
        ResultSet resultSet =null;
        try {
            statement = connection.createStatement();
            String sql1 = "select * from strongunion_online.sl_fact_user_portrait_action";
             resultSet = statement.executeQuery(sql1);
            while (resultSet.next()) {
                System.out.println("uid:" + resultSet.getString("uid") + "words:" + resultSet.getString("words") + "counts:" + resultSet.getString("tfidf"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            GetConnection.closeConnection(resultSet,statement, connection);
        }
    }
}
