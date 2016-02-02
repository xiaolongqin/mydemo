package tf_idf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FormatUtils;
import utils.GetConnection;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Tyfunwang on 2015/8/14.
 */
public class TfIdfResult {
    private static final Logger LOG = LoggerFactory.getLogger(TfIdfResult.class);
    public void getTfIdf() throws IOException {
/**
 *  mysql-->分词_tf_idf_tfidf-->mysql*
*/
        //定义我的mysql数据接口
        Connection jdbcCon = GetConnection.getConnectionJdbc();

        PreparedStatement truncate_sql = null;
        PreparedStatement truncate_tfidf = null;
        try {
            //清除分词原有数据
            truncate_sql = jdbcCon.prepareStatement("truncate table sl_ods_fenci ");
            truncate_sql.executeUpdate();

            //清除事实tfidf原有数据
            truncate_tfidf = jdbcCon.prepareStatement("truncate table sl_rpt_user_portrait_tfidf ");
            truncate_tfidf.executeUpdate();
        } catch (SQLException e) {
            LOG.error("mysql连接异常",e);
        }

/**hive-->分词_tf_idf_tfidf-->mysql*/
        Connection hiveCon = GetConnection.getConnectionHive();
        Statement hiveStam = null;
        try {
            hiveStam = hiveCon.createStatement();
            //先清除每次挖掘的样本数据源
            hiveStam.executeQuery("drop  table record.sl_ods_page_visit_record_" + FormatUtils.getMonth(0) + "");//record.sl_ods_page_visit_record_201508
            hiveStam.executeQuery("drop  table record.sl_ods_datafile");
        } catch (SQLException e) {
            LOG.error("hive连接异常", e);
        }

        /**
         *从mysql取数据到hive * 
         */
        try {
            new MysqlImportHive().doWork();
        } catch (InterruptedException e) {
            LOG.error("mysql导入hive异常", e);
            try {
                new MysqlImportHive().doWork();
            } catch (InterruptedException | IOException e1) {
                LOG.error("mysql导入hive异常", e1);
            }
        } catch (IOException e) {
            LOG.error("mysql导入hive异常", e);
        }


        //hive分布式批量清洗样本
        ResultSet hivers = null;
        try {
            hiveStam.executeQuery("create  table record.sl_ods_datafile(uid bigint,contents string)");

            String hive_sql2 = "insert into table record.sl_ods_datafile select uid,concat_ws('" + "," + "',collect_set(contents))" +
                    " from record.record.sl_ods_page_visit_record_" + FormatUtils.getMonth(0) + "  where uid is not null group by uid";
            hiveStam.executeQuery(hive_sql2);
            //hive分布式批量清洗样本
            hivers = hiveStam.executeQuery("select  * from record.sl_ods_datafile");

        } catch (SQLException e) {
            LOG.error("hive清洗样本异常",e);
        }

        StringBuilder sb_hive = new StringBuilder();
        Map<String, String> dbResultMap_hive = new HashMap<String, String>();
        //返回集合
        try {
            while (hivers != null && hivers.next()) {
                String phone_no_hive = hivers.getString("sl_ods_datafile.uid");
                String content_hive = hivers.getString("sl_ods_datafile.contents");

                sb_hive.append(phone_no_hive).append(",").append(content_hive).append("\r\n");
                dbResultMap_hive.put(phone_no_hive, content_hive);
            }
        } catch (SQLException e) {
            LOG.error("hive获取样本异常",e);
        }

        //输出分词对应key的个数
        Map<String, HashMap<String, Integer>> normal_hive = ReadFilesFromDbResult.NormalTFOfAll(dbResultMap_hive);
        // System.out.println("-------------------文本分词计数----------------------");
        for (String filename : normal_hive.keySet()) {
            //TODO 插入tfidf
            Map<String, Integer> ifMap = normal_hive.get(filename);
            Set<String> ifKeySet = ifMap.keySet();
            for (String ifKey : ifKeySet) {
                String key3 = filename;
                String word3 = ifKey;
                Integer count3 = ifMap.get(ifKey);
            }
        }

        //输出tfidf
        Map<String, HashMap<String, Float>> tfidf_hive = ReadFilesFromDbResult.tfidf(dbResultMap_hive);
        PreparedStatement insert_tfidf = null;
        for (String filename : tfidf_hive.keySet()) {
            //TODO 插入HIVE
            Map<String, Float> tfidfMap_hive = tfidf_hive.get(filename);
            Set<String> tfidfKeySet_hive = tfidfMap_hive.keySet();
            for (String tfidfKey_hive : tfidfKeySet_hive) {
                String key2 = filename;
                String word2 = tfidfKey_hive;
                float count2 = tfidfMap_hive.get(tfidfKey_hive);
                //与rdbms数据交互,输出分词的tfidf
                try {
                    insert_tfidf = jdbcCon.prepareStatement("insert into sl_rpt_user_portrait_tfidf values (" + key2 + ",'" + word2 + "','" + count2 + "')");
                    insert_tfidf.executeUpdate();
                } catch (SQLException e) {
                    LOG.error("mysql输出数据异常",e);
                }
            }
        }

        GetConnection.closeConnection(hivers, hiveStam, hiveCon);//close hive
        GetConnection.closeConnection(truncate_sql, jdbcCon);//close jdbc
        GetConnection.closeConnection(truncate_tfidf, jdbcCon);//close jdbc
        GetConnection.closeConnection(insert_tfidf, jdbcCon);//close jdbc
    }
}
