import org.junit.Test;
import sl.bigdata.util.Connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by Tyfunwang on 2015/5/27.
 */
public class getConnect {
    private Connect connection = Connect.getInstance();

    @Test
    public void startDruid(){
        System.out.println("startTime:"+new Date().getTime());
       Connection connection1 =  connection.getConnect();
        PreparedStatement preparedStatement=null;
        ResultSet set = null;
        String sql = "select * from sl_rpt_app_page_aim1";
        try {
            preparedStatement = connection1.prepareStatement(sql);
            set = preparedStatement.executeQuery();
            while(set.next()){
                System.out.println(set.getString(4));
            }
            System.out.println("stopTime:"+ new Date().getTime());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

//    public void getCon(){
//        System.out.println("startTime:"+new Date().getTime());
//        Connection con = connection.getConnect();
//        try {
//            Statement sta = con.createStatement();
//            String sql = "select * from space";
//            ResultSet query = sta.executeQuery(sql);
//            while(query.next()){
//                System.out.println(query.getString(4));
//            }
//            System.out.println("stopTime:"+ new Date().getTime());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}
