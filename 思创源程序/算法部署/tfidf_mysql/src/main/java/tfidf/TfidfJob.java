package tfidf;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import utils.GetConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by 小龙
 * on 15-9-1
 * at 下午2:55.
 */

public class TfidfJob implements Job {
    public static String CRON = "0 5 0 * * ?";
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Connection conn=null;
        Statement mysqlstam=null;
        ResultSet mysqlrs=null;

        try {

           /*
mysql-->分词_tf_idf_tfidf-->mysql
*/
           ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            conn = GetConnection.getConnectionJdbc();


           PreparedStatement truncate_sql = conn.prepareStatement("truncate table sl_rpt_user_portrait_tfidf ");
           truncate_sql.executeUpdate();
           //创建语句对象，用以执行sql语言
            mysqlstam = conn.createStatement();
            mysqlrs = mysqlstam.executeQuery("select * from " + GetConnection.TABLE_NAME + "");
           String content_mysql;
           String phone_no_mysql;

           StringBuilder sb_mysql = new StringBuilder();
           Map<String, String> dbResultMap = new HashMap<String, String>();
           while (mysqlrs.next()) {
               String line = "";
               phone_no_mysql = mysqlrs.getString("uid");
               content_mysql = mysqlrs.getString("contents");
               line = phone_no_mysql + "," + content_mysql + "\r\n";
               //  System.out.println(line);
               sb_mysql.append(line);
               dbResultMap.put(phone_no_mysql, content_mysql);
           }

//输出tfidf
           Map<String, HashMap<String, Float>> tfidf = ReadFilesFromDbResult.tfidf(dbResultMap);
           for (String filename : tfidf.keySet()) {
               //TODO 插入HIVE
               Map<String, Float> tfidfMap = tfidf.get(filename);
               Set<String> tfidfKeySet = tfidfMap.keySet();
               for (String tfidfKey : tfidfKeySet) {
                   String key1 = filename;
                   String word1 = tfidfKey;
                   float count1 = tfidfMap.get(tfidfKey);
                   //与rdbms数据交互,输出分词的tfidf
                   PreparedStatement insert_tfidf = conn.prepareStatement("insert into sl_rpt_user_portrait_tfidf values (" + key1 + ",'" + word1 + "','" + count1 + "')");
                   insert_tfidf.executeUpdate();
               }
           }
       }catch (Exception e){
        e.printStackTrace();

       }finally {
            try {
                if (mysqlrs != null) mysqlrs.close();
                if (mysqlstam != null) mysqlstam.close();
                if (conn != null) conn.close();
            }catch (Exception e){
                 e.printStackTrace();
            }
       }

    }
}
