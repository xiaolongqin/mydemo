package sl.bigdata.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sl.bigdata.util.Connect;
import sl.bigdata.util.GetMonth;
import sl.bigdata.util.LogSqlUtil;
import sl.bigdata.util.javaMail.SendMail;

import javax.mail.MessagingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TimerTask;

/**
 * Created by Tyfunwang on 2015/5/29.
 */
public class TimeTask {
    private static Logger logger = LoggerFactory.getLogger(TimeTask.class);
    private static Connect connect = Connect.getInstance();
    private static int rpt_run_count=0;

    //每天12点重置jra包运行次数
    public static TimerTask resetRptCount = new TimerTask() {

        @Override
        public void run() {
            rpt_run_count=0;
        }
    };

   //检测ODS日志任务
    public static TimerTask testOds = new TimerTask() {

        @Override
        public void run() {
            if (rpt_run_count==1){
                return;
            }

            Connection connection = connect.getConnect();
            Statement statement = null;
            ResultSet rs=null;
            int rsCount=0;
            int tableCount=21;

            String sqlStatus="SELECT table_name,status from  sl_ods_etl_log \n" +
                    "WHERE (lower(table_name)=lower('sl_ods_reg_user_count') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n" +
                    "OR (lower(table_name)=lower('sl_ods_channel_record') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_channel_type') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_order') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_detail') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_goods') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_goodstype') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_goodsimg') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_shops') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_shopstype') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_comshop') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_community') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_receipt') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_users') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_mobile_device') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_user_integral_level') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_attention') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_attention_goods') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_goods_special') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_shopping_cart') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_channel_apk') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d'));";

            try {
                //statement
                statement = connection.createStatement();
                rs= statement.executeQuery(sqlStatus);
                int table1_count=0;
                int table2_count=0;
                int table3_count=0;
                int table4_count=0;
                int table5_count=0;
                int table6_count=0;
                int table7_count=0;
                int table8_count=0;
                int table9_count=0;
                int table10_count=0;
                int table11_count=0;
                int table12_count=0;
                int table13_count=0;
                int table14_count=0;
                int table15_count=0;
                int table16_count=0;
                int table17_count=0;
                int table18_count=0;
                int table19_count=0;
                int table20_count=0;
                int table21_count=0;
                while (rs.next())
                {
                    if(rs.getString("table_name").equals("sl_ods_reg_user_count")&&rs.getInt("status")==1){
                        table1_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_channel_record")&&rs.getInt("status")==1){
                        table2_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_channel_apk")&&rs.getInt("status")==1){
                        table3_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_detail")&&rs.getInt("status")==1){
                        table4_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_channel_type")&&rs.getInt("status")==1){
                        table5_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_order")&&rs.getInt("status")==1){
                        table6_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_goods")&&rs.getInt("status")==1){
                        table7_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_goodstype")&&rs.getInt("status")==1){
                        table8_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_goodsimg")&&rs.getInt("status")==1){
                        table9_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_shops")&&rs.getInt("status")==1){
                        table10_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_shopstype")&&rs.getInt("status")==1){
                        table11_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_comshop")&&rs.getInt("status")==1){
                        table12_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_community")&&rs.getInt("status")==1){
                        table13_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_receipt")&&rs.getInt("status")==1){
                        table14_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_users")&&rs.getInt("status")==1){
                        table15_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_mobile_device")&&rs.getInt("status")==1){
                        table16_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_user_integral_level")&&rs.getInt("status")==1){
                        table17_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_attention")&&rs.getInt("status")==1){
                        table18_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_attention_goods")&&rs.getInt("status")==1){
                        table19_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_goods_special")&&rs.getInt("status")==1){
                        table20_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_shopping_cart")&&rs.getInt("status")==1){
                        table21_count=1;
                        continue;
                    }
                }
                rsCount=table1_count+table2_count+table3_count+table4_count+table5_count+table6_count+table7_count+table8_count+table9_count+table10_count+table11_count+table12_count+
                        table13_count+table14_count+table15_count+table16_count+table17_count+table18_count+table19_count+table20_count+table21_count;
                if (rpt_run_count==0&&rsCount==tableCount){
                    rpt_run_count=1;
                    TimeTask.task1.run();
                    TimeTask.task2.run();
                    TimeTask.task3.run();
                    TimeTask.task4.run();
                    TimeTask.task5.run();
                    TimeTask.task6.run();
                    TimeTask.task7.run();
                    TimeTask.task8.run();
                    TimeTask.task9.run();
                    TimeTask.task10.run();

                }else if (rsCount!=tableCount){
                    logger.error("日志表ods层其中有异常（失败）情况,今天到目前为止的数据同步进度为:"+rsCount+"/"+tableCount);
                }

            } catch (Exception e) {
                e.printStackTrace();
                logger.error("rpt 抽取 error:" + e.getMessage());
                try {
                    //rollback
                    connection.rollback();
                } catch (SQLException e1) {
                    logger.error("rpt rollback error:" + e.getMessage());
                }
            } finally {
                try {
                    if (rs!=null) rs.close();
                    if (statement != null) statement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    logger.error("rpt close connection  error:" + e.getMessage());
                }
            }



        }
    };

    //ods异常，发送邮件
    public static TimerTask mailOds = new TimerTask() {

        @Override
        public void run() {

            Connection connection = connect.getConnect();
            Statement statement = null;
            ResultSet rs=null;
            int rsCount=0;
            int tableCount=21;

            String sqlStatus="SELECT table_name,status from  sl_ods_etl_log \n" +
                    "WHERE (lower(table_name)=lower('sl_ods_reg_user_count') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n" +
                    "OR (lower(table_name)=lower('sl_ods_channel_record') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_channel_type') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_order') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_detail') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_goods') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_goodstype') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_goodsimg') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_shops') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_shopstype') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_comshop') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_community') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_receipt') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_users') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_mobile_device') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_user_integral_level') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_attention') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_attention_goods') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_goods_special') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_shopping_cart') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_channel_apk') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d'));";

            try {
                //statement
                statement = connection.createStatement();
                rs= statement.executeQuery(sqlStatus);
                int table1_count=0;
                int table2_count=0;
                int table3_count=0;
                int table4_count=0;
                int table5_count=0;
                int table6_count=0;
                int table7_count=0;
                int table8_count=0;
                int table9_count=0;
                int table10_count=0;
                int table11_count=0;
                int table12_count=0;
                int table13_count=0;
                int table14_count=0;
                int table15_count=0;
                int table16_count=0;
                int table17_count=0;
                int table18_count=0;
                int table19_count=0;
                int table20_count=0;
                int table21_count=0;
                while (rs.next())
                {
                    if(rs.getString("table_name").equals("sl_ods_reg_user_count")&&rs.getInt("status")==1){
                        table1_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_channel_record")&&rs.getInt("status")==1){
                        table2_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_channel_apk")&&rs.getInt("status")==1){
                        table3_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_detail")&&rs.getInt("status")==1){
                        table4_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_channel_type")&&rs.getInt("status")==1){
                        table5_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_order")&&rs.getInt("status")==1){
                        table6_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_goods")&&rs.getInt("status")==1){
                        table7_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_goodstype")&&rs.getInt("status")==1){
                        table8_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_goodsimg")&&rs.getInt("status")==1){
                        table9_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_shops")&&rs.getInt("status")==1){
                        table10_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_shopstype")&&rs.getInt("status")==1){
                        table11_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_comshop")&&rs.getInt("status")==1){
                        table12_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_community")&&rs.getInt("status")==1){
                        table13_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_receipt")&&rs.getInt("status")==1){
                        table14_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_users")&&rs.getInt("status")==1){
                        table15_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_mobile_device")&&rs.getInt("status")==1){
                        table16_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_user_integral_level")&&rs.getInt("status")==1){
                        table17_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_attention")&&rs.getInt("status")==1){
                        table18_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_attention_goods")&&rs.getInt("status")==1){
                        table19_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_goods_special")&&rs.getInt("status")==1){
                        table20_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_shopping_cart")&&rs.getInt("status")==1){
                        table21_count=1;
                        continue;
                    }
                }
                rsCount=table1_count+table2_count+table3_count+table4_count+table5_count+table6_count+table7_count+table8_count+table9_count+table10_count+table11_count+table12_count+
                        table13_count+table14_count+table15_count+table16_count+table17_count+table18_count+table19_count+table20_count+table21_count;
                 if (rsCount!=tableCount){
                     try {
                         SendMail.sendMail("qinxiaolong@unionbigdata.com", false, "思创数据同步(ods)出现异常,请维护人员查看服务器错误日志文件,找出问题,并维护!");
                         SendMail.sendMail("sunwh@strongunion.com.cn", false, "思创数据同步(ods)出现异常,请维护人员查看服务器错误日志文件,找出问题,并维护!");
                         SendMail.sendMail("zhangke@unionbigdata.com", false, "思创数据同步(ods)出现异常,请维护人员查看服务器错误日志文件,找出问题,并维护!");
                         SendMail.sendMail("wanglin@strongunion.com.cn", false, "思创数据同步(ods)出现异常,请维护人员查看服务器错误日志文件,找出问题,并维护!");
                     } catch (MessagingException e) {
                         logger.error(" error:" + e.getMessage());
                     }
                }

            } catch (Exception e) {
                e.printStackTrace();
                logger.error(" error:" + e.getMessage());
                try {
                    //rollback
                    connection.rollback();
                } catch (SQLException e1) {
                    logger.error("rpt rollback error:" + e.getMessage());
                }
            } finally {
                try {
                    if (rs!=null) rs.close();
                    if (statement != null) statement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    logger.error("rpt close connection  error:" + e.getMessage());
                }
            }



        }
    };



    /**
     * 第一张报表
     *名称 --%@NAME:sl_rpt_app_channel_aim1
     *功能描述 --%@COMMENT: 数据从ODS到RPT层
     *数据模块 --%@PARAM: RPT 集市域
     *执行周期 --%@PERIOD:按（天）
     *日志输入 --%@PARAM: SL_ODS_ETL_LOG : sl_ods_channel_record,sl_ods_reg_user_count,sl_ods_page_visit_record_sl_ods_order
     *日志输出 --%@PARAM: SL_ODS_ETL_LOG : sl_rpt_app_channel_aim1
     *日志参数 --%@PARAM: 过程JAR执行结果代码(日志状态，1-完成 0-开始，默认插入后均写 0),输出参数
     *日志描述 --%@PARAM: 过程执行结果描述,写入参数
     *创建人 --%@CREATOR: 覃小龙
     *创建时间 --%@CREATED_TIME: 2015-06-30
     *来源表 --%@FROM: sl_ods_reg_user_count --ODS用户注册表
     *来源表 --%@FROM: sl_ods_channel_apk --ODS渠道表
     *来源表 --%@FROM: sl_ods_channel_record --ODS渠道下载记录表
     *目标表 --%@TO:   sl_rpt_app_channel_aim1 --
    */
    public static TimerTask task1 = new TimerTask() {

        @Override
        public void run() {
            //使用或者上边的语句
            Connection connection = connect.getConnect();
            Statement statement = null;
            ResultSet rs=null;
            int rsCount=0;
            int tableCount=3;

            //向日志表sl_ods_etl_log插入sl_rpt_app_page_aim1表的更新记录
            String beginLog= LogSqlUtil.getBeginLog("sl_rpt_app_channel_aim1");
            String endLog=LogSqlUtil.getEndLog("sl_rpt_app_channel_aim1");
            //从sl_ods_etl_log日志表查询出sl_ods_reg_user_count表、sl_ods_channel_record表、sl_ods_channel_apk表相应日志记录状态status
            String sqlStatus="SELECT table_name,status from  sl_ods_etl_log \n" +
                    "WHERE (lower(table_name)=lower('sl_ods_reg_user_count') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n" +
                    "OR (lower(table_name)=lower('sl_ods_channel_record') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n"+
                    "OR (lower(table_name)=lower('sl_ods_channel_apk') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d'));";


            // 统计昨日总目标用户、到达用户数、注册用户数：主表*
            String sqlDrop1 = "DROP TABLE IF EXISTS temp1_rpt_channel_aim1;";
            String sql1 = "CREATE TABLE temp1_rpt_channel_aim1 as\n" +
                    "select t.channel_apk_id, /*推广渠道id*/\n" +
                    "       FROM_UNIXTIME(t.regtime, '%Y%m%d') as regtime,  /*注册时间*/\n" +
                    "       sum(case when t.aim_num is null then 0 else t.aim_num end) as aim_num, /*总目标用户数*/\n" +
                    "       sum(case when t.arrive_num is null then 0 else t.arrive_num end) as arrive_num, /*到达用户数*/\n" +
                    "       sum(case when t.reg_num is null then 0 else t.reg_num end) as reg_num /*注册用户数*/\n" +
                    "   from sl_ods_reg_user_count t\n" +
                    "  where t.regtime >= UNIX_TIMESTAMP(date_sub(curdate(),interval 1 day))\n" +
                    "    and t.regtime < UNIX_TIMESTAMP(CURDATE())\n" +
                    "    and (t.regtime is not null or t.regtime <> 0)\n" +
                    "group by t.channel_apk_id ;";
            //统计昨日下载用户数：
            String sqlDrop2 = "DROP TABLE IF EXISTS temp2_rpt_channel_aim1;";
            String sql2 = "CREATE TABLE temp2_rpt_channel_aim1 as\n" +
                    "select t.channel_apk_id,\n" +
                    "       FROM_UNIXTIME(t.downloadstart_time, '%Y%m%d') as downloadstart_time, \n" +
                    "       count(DISTINCT t.imei) as download_count /*下载用户数*/\n" +
                    "   from sl_ods_channel_record t\n" +
                    "  where t.downloadstart_time >= UNIX_TIMESTAMP(date_sub(curdate(),interval 1 day))\n" +
                    "    and t.downloadstart_time < UNIX_TIMESTAMP(CURDATE())\n" +
                    "    and (t.downloadstart_time is not null or t.downloadstart_time <> 0)\n" +
                    "group by t.channel_apk_id;";
            //统计昨日点击用户数：
            String sqlDrop3 = "DROP TABLE IF EXISTS temp3_rpt_channel_aim1;";
            String month = GetMonth.getMonth();
            String sql3 = "CREATE TABLE temp3_rpt_channel_aim1 as\n" +
                    "SELECT FROM_UNIXTIME(t.pagestart_time, '%Y%m%d') as pagestart_time, /*进入页面时间*/\n" +
                    "       t.channel_apk_id,\n" +
                    "       count(*) AS hit_num\n" +
                    "  from sl_ods_page_visit_record_"+month+" t\n" +
                    "  where t.pagestart_time >= UNIX_TIMESTAMP(date_sub(curdate(),interval 1 day))\n" +
                    "    and t.pagestart_time < UNIX_TIMESTAMP(CURDATE())\n" +
                    "    and (t.pagestart_time is not null or t.pagestart_time <> 0)\n" +
                    "  GROUP BY t.channel_apk_id;";

            //汇总总表
            /*先汇总渠道信息*/
            String sqlDrop4 = "DROP TABLE IF EXISTS temp0_rpt_channel_aim1;";
            String sql4 = "CREATE TABLE temp0_rpt_channel_aim1 as\n" +
                    "select t0.id,\n" +
                    "       t0.apptype,\n" +
                    "      (case when t0.apptype in ('1') then '用户端'\n" +
                    "            when t0.apptype in ('2') then '商户端'\n" +
                    "            when t0.apptype in ('3') then '管家端'\n" +
                    "       else '其它端' end) as prod_name,\n" +
                    "       t0.channel_typeid,\n" +
                    "       t1.name as channel_typename,\n" +
                    "       t0.name as name,\n" +
                    "       t0.description\n" +
                    "  from sl_ods_channel_apk t0 join sl_ods_channel_type t1 on t0.channel_typeid = t1.id\n" +
                    " group by t0.id, t0.apptype, t0.channel_typeid, t0.name, t0.description ;";


            //先删除当天已经写入的数据
            String sqlDrop5 = "DELETE from sl_rpt_app_channel_aim1 where day_no  = DATE_FORMAT(adddate(now(),-1), '%Y%m%d');";
            String sqlResult = "INSERT INTO sl_rpt_app_channel_aim1\n" +
                    "select DATE_FORMAT(adddate(now(),-1), '%Y%m%d') as day_no, /*统计时间*/\n" +
                    "       t0.prod_name as prod_name, /*产品渠道*/\n" +
                    "       t0.channel_typename as app_channel_type, /*app推广类型*/\n" +
                    "       t0.description as app_channel, /*app推广渠道*/\n" +
                    "       SUM(case when t1.aim_num is null then 0 else t1.aim_num end) as aim_num, /*总目标用户数*/\n" +
                    "       SUM(case when t1.arrive_num is null then 0 else t1.arrive_num end) as arrive_num, /*到达用户数*/\n" +
                    "       SUM(case when t3.hit_num is null then 0 else t3.hit_num end) as hit_num, /*点击用户数*/\n" +
                    "       SUM(case when t2.download_count is null then 0 else t2.download_count end) as download_count, /*下载用户数*/\n" +
                    "       SUM(case when t1.reg_num is null then 0 else t1.reg_num end) as reg_num /*注册用户数*/\n" +
                    "  from temp0_rpt_channel_aim1 t0 LEFT JOIN temp1_rpt_channel_aim1 t1 on t0.id = t1.channel_apk_id\n" +
                    "                                 LEFT JOIN temp2_rpt_channel_aim1 t2 on t0.id = t2.channel_apk_id\n" +
                    "                                 LEFT JOIN temp3_rpt_channel_aim1 t3 on t0.id = t3.channel_apk_id\n" +
                    "GROUP BY t0.prod_name, t0.channel_typename, t0.description ;";
            //删除临时表
            String drop1 = "DROP TABLE IF EXISTS temp1_rpt_channel_aim1;";
            String drop2 = "DROP TABLE IF EXISTS temp2_rpt_channel_aim1;";
            String drop3 = "DROP TABLE IF EXISTS temp3_rpt_channel_aim1;";
            String drop4 = "DROP TABLE IF EXISTS temp0_rpt_channel_aim1;";
            try {
                //statement
                statement = connection.createStatement();
                rs= statement.executeQuery(sqlStatus);
                int table1_count=0;
                int table2_count=0;
                int table3_count=0;
                while (rs.next())
                {
                     if(rs.getString("table_name").equals("sl_ods_reg_user_count")&&rs.getInt("status")==1){
                         table1_count=1;
                         continue;
                     }
                    if(rs.getString("table_name").equals("sl_ods_channel_record")&&rs.getInt("status")==1){
                        table2_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_channel_apk")&&rs.getInt("status")==1){
                        table3_count=1;
                        continue;
                    }
                }
                rsCount=table1_count+table2_count+table3_count;
                statement.execute(beginLog);
                if (rsCount==tableCount){
                    statement.execute(sqlDrop1);
                    statement.execute(sqlDrop2);
                    statement.execute(sqlDrop3);
                    statement.execute(sqlDrop4);

                    connection.setAutoCommit(false);
                    statement.execute(sql1);
                    statement.execute(sql2);
                    statement.execute(sql3);
                    statement.execute(sql4);
                    statement.execute(sqlDrop5);
                    statement.execute(sqlResult);
                    statement.execute(endLog);
                    connection.commit();
                    connection.setAutoCommit(true);

                    statement.execute(drop1);
                    statement.execute(drop2);
                    statement.execute(drop3);
                    statement.execute(drop4);
                }else {
                    logger.error("日志表ods层关于表sl_ods_reg_user_count、sl_ods_channel_record、sl_ods_channel_apk其中有异常（失败）情况" );
                }

            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("for sl_rpt_app_channel_aim1 error:" + e.getMessage());
                try {
                    //rollback
                    connection.rollback();
                } catch (SQLException e1) {
                    logger.error("The sl_rpt_app_channel_aim1 rollback error:" + e.getMessage());
                }
            } finally {
                try {
                    if (rs!=null) rs.close();
                    if (statement != null) statement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    logger.error("The sl_rpt_app_channel_aim1 close connection  error:" + e.getMessage());
                }
            }
        }
    };
    /**
     * 第二张报表: sl_rpt_app_channel_aim2
     *名称 --%@NAME:sl_rpt_app_channel_aim2
     *功能描述 --%@COMMENT:
     *数据模块 --%@PARAM: RPT 集市域
     *执行周期 --%@PERIOD:按（天）
     *日志输入 --%@PARAM: SL_ODS_ETL_LOG : sl_ods_users,sl_ods_order
     *日志输出 --%@PARAM: SL_ODS_ETL_LOG : sl_rpt_life_cycle_users
     *日志参数 --%@PARAM: 过程JAR执行结果代码(日志状态，1-完成 0-开始，默认插入后均写 0),输出参数
     *日志描述 --%@PARAM: 过程执行结果描述,写入参数
     *创建人 --%@CREATOR: 覃小龙
     *创建时间 --%@CREATED_TIME: 2015-06-30
     *来源表 --%@FROM: sl_ods_channel_apk --ODS渠道表
     *来源表 --%@FROM: sl_ods_channel_record --ODS渠道下载记录表
     *目标表 --%@TO:   sl_rpt_app_channel_aim2 --
     */
    public static TimerTask task2 = new TimerTask() {
        @Override
        public void run() {
            //使用或者上边的语句
            Connection connection = connect.getConnect();
            Statement statement = null;
            ResultSet rs=null;
            int rsCount=0;
            int tableCount=2;


            //向日志表sl_ods_etl_log插入sl_rpt_app_page_aim1表的更新记录
            String beginLog= LogSqlUtil.getBeginLog("sl_rpt_app_channel_aim2");
            String endLog=LogSqlUtil.getEndLog("sl_rpt_app_channel_aim2");


            //从sl_ods_etl_log日志表查询sl_ods_channel_apk表、sl_ods_channel_record表相应日志记录状态status
            String sqlStatus="SELECT table_name,status from  sl_ods_etl_log \n" +
                    "WHERE (lower(table_name)=lower('sl_ods_channel_apk') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n" +
                    "OR (lower(table_name)=lower('sl_ods_channel_record') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d'));";

            //统计当日下载用户数：
            String sqlDrop1 = "DROP TABLE IF EXISTS temp1_rpt_channel_aim2;";
            String sql1 = "CREATE TABLE temp1_rpt_channel_aim2 as\n" +
                    "select t.channel_apk_id,\n" +
                    "       FROM_UNIXTIME(t.downloadstart_time, '%Y%m%d') as downloadstart_time, \n" +
                    "       count(DISTINCT t.imei) as download_count /*下载用户数*/\n" +
                    "   from sl_ods_channel_record t\n" +
                    "  where t.downloadstart_time >= UNIX_TIMESTAMP(date_sub(curdate(),interval 1 day))\n" +
                    "    and t.downloadstart_time < UNIX_TIMESTAMP(CURDATE())\n" +
                    "    and (t.downloadstart_time is not null or t.downloadstart_time <> 0)\n" +
                    "group by t.channel_apk_id;";

            /*统计当日活跃用户数、浏览次数：*/
            String month = GetMonth.getMonth();
            String sqlDrop2 = "DROP TABLE IF EXISTS temp2_rpt_channel_aim2;";
            String sql2 = "CREATE TABLE temp2_rpt_channel_aim2 as\n" +
                    "SELECT FROM_UNIXTIME(t.pagestart_time, '%Y%m%d') as pagestart_time,\n" +
                    "       t.channel_apk_id,\n" +
                    "       count(distinct t.imei) AS active_num,\n" +
                    "       count(*) as hit_num\n" +
                    "  from sl_ods_page_visit_record_"+month+" t\n" +
                    "  where t.pagestart_time >= UNIX_TIMESTAMP(date_sub(curdate(),interval 1 day))\n" +
                    "    and t.pagestart_time < UNIX_TIMESTAMP(CURDATE())\n" +
                    "    and (t.pagestart_time is not null or t.pagestart_time <> 0)\n" +
                    "  GROUP BY t.channel_apk_id;";
            //汇总表
            /*先汇总渠道信息*/
            String sqlDrop3 = "DROP TABLE IF EXISTS temp0_rpt_channel_aim2;";
            String sql3 ="CREATE TABLE temp0_rpt_channel_aim2 as\n" +
                    "select t0.id,\n" +
                    "       t0.apptype,\n" +
                    "      (case when t0.apptype in ('1') then '用户端'\n" +
                    "            when t0.apptype in ('2') then '商户端'\n" +
                    "            when t0.apptype in ('3') then '管家端'\n" +
                    "       else '其它端' end) as prod_name,\n" +
                    "       t0.channel_typeid,\n" +
                    "       t1.name as channel_typename,\n" +
                    "       t0.name as name,\n" +
                    "       t0.description\n" +
                    "  from sl_ods_channel_apk t0 join sl_ods_channel_type t1 on t0.channel_typeid = t1.id\n" +
                    " group by t0.id, t0.apptype, t0.channel_typeid, t0.name, t0.description ;";

            //先删除当天已经写入的数据
            String sqlDrop4 = "DELETE from sl_rpt_app_channel_aim2 where day_no  = DATE_FORMAT(adddate(now(),-1), '%Y%m%d');";
            String sqlResult = "INSERT INTO sl_rpt_app_channel_aim2\n" +
                    "select DATE_FORMAT(adddate(now(),-1), '%Y%m%d') as day_no, /*统计时间*/\n" +
                    "       t0.prod_name as prod_name, /*产品渠道*/\n" +
                    "       t0.channel_typename as app_channel_type, /*app推广类型*/\n" +
                    "       t0.description as app_channel, /*app推广渠道*/\n" +
                    "       SUM(case when t1.download_count is null then 0 else t1.download_count end) as aim_num, /*下载用户数*/\n" +
                    "       SUM(case when t2.active_num is null then 0 else t2.active_num end) as arrive_num, /*活跃用户数*/\n" +
                    "       SUM(case when t2.hit_num is null then 0 else t2.hit_num end) as hit_num  /*浏览次数*/\n" +
                    "  from temp0_rpt_channel_aim2 t0 LEFT JOIN temp1_rpt_channel_aim2 t1 on t0.id = t1.channel_apk_id\n" +
                    "                                 LEFT JOIN temp2_rpt_channel_aim2 t2 on t0.id = t2.channel_apk_id\n" +
                    "GROUP BY t0.prod_name, t0.channel_typename, t0.description ;";
            //删除临时表
            String drop1 = "DROP TABLE IF EXISTS temp1_rpt_channel_aim2;";
            String drop2 = "DROP TABLE IF EXISTS temp2_rpt_channel_aim2;";
            String drop3 = "DROP TABLE IF EXISTS temp0_rpt_channel_aim2;";
            try {
                //statement
                statement = connection.createStatement();

                rs= statement.executeQuery(sqlStatus);
                int table1_count=0;
                int table2_count=0;
                while (rs.next())
                {
                    if(rs.getString("table_name").equals("sl_ods_channel_apk")&&rs.getInt("status")==1){
                        table1_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_channel_record")&&rs.getInt("status")==1){
                        table2_count=1;
                        continue;
                    }
                }
                rsCount=table1_count+table2_count;
                statement.execute(beginLog);
                if (rsCount==tableCount){
                    statement.execute(sqlDrop1);
                    statement.execute(sqlDrop2);
                    statement.execute(sqlDrop3);

                    connection.setAutoCommit(false);
                    statement.execute(sql1);
                    statement.execute(sql2);
                    statement.execute(sql3);
                    statement.execute(sqlDrop4);
                    statement.execute(sqlResult);
                    statement.execute(endLog);
                    connection.commit();
                    connection.setAutoCommit(true);

                    statement.execute(drop1);
                    statement.execute(drop2);
                    statement.execute(drop3);
                }else {
                    logger.error("日志表ods层关于表sl_ods_channel_record、sl_ods_channel_apk其中有异常（失败）情况" );
                }




            } catch (SQLException e) {
                logger.error("for sl_rpt_app_channel_aim2 error:" + e.getMessage());
                try {
                    //rollback
                    connection.rollback();
                } catch (SQLException e1) {
                    logger.error("The sl_rpt_app_channel_aim2 rollback error:" + e.getMessage());
                }
            } finally {
                try {
                    if (statement != null) statement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    logger.error("The sl_rpt_app_channel_aim2 close connection error:" + e.getMessage());
                }
            }
        }
    };
    /**
     * 第三张报表:sl_rpt_app_page_aim1
     *名称 --%@NAME:sl_rpt_app_page_aim1
     *功能描述 --%@COMMENT:
     *数据模块 --%@PARAM: RPT 集市域
     *执行周期 --%@PERIOD:按（天）
     *日志输入 --%@PARAM: SL_ODS_ETL_LOG :
     *日志输出 --%@PARAM: SL_ODS_ETL_LOG : sl_rpt_app_page_aim1
     *日志参数 --%@PARAM: 过程JAR执行结果代码(日志状态，1-完成 0-开始，默认插入后均写 0),输出参数
     *日志描述 --%@PARAM: 过程执行结果描述,写入参数
     *创建人 --%@CREATOR: 覃小龙
     *创建时间 --%@CREATED_TIME: 2015-06-30
     *来源表 --%@FROM:
     *来源表 --%@FROM:
     *目标表 --%@TO:   sl_rpt_app_page_aim1
     */
    public static TimerTask task3 = new TimerTask() {
        @Override
        public void run() {
            //使用或者下边的语句
            Connection connection = connect.getConnect();
            Statement statement = null;
            //向日志表sl_ods_etl_log插入sl_rpt_app_page_aim1表的更新记录
            String beginLog= LogSqlUtil.getBeginLog("sl_rpt_app_page_aim1");
            String endLog=LogSqlUtil.getEndLog("sl_rpt_app_page_aim1");


            //统计当日相关信息：
            String month = GetMonth.getMonth();
            String sqlDrop1 = "DROP TABLE IF EXISTS temp1_rpt_page_aim1;";
            String sql1 = "CREATE TABLE temp1_rpt_page_aim1 as\n" +
                    "SELECT FROM_UNIXTIME(t.pagestart_time, '%Y%m%d') as pagestart_time,\n" +
                    "       t.channel_apk_id,\n" +
                    "       t.page_id,\n" +
                    "       count(*) AS page_pv,\n" +
                    "       count(distinct t.imei) AS page_uv,\n" +
                    "       round(sum(case when t.pageend_time is null or t.pageend_time = 0 then 0\n" +
                    "           else TIMESTAMPDIFF(SECOND,FROM_UNIXTIME(t.pagestart_time, '%Y-%m-%d %H:%i:%S'),FROM_UNIXTIME(t.pageend_time, '%Y-%m-%d %H:%i:%S'))\n" +
                    "           end) /count(*),0) as avg_page_restime    \n" +
                    "       /*avg(TIMESTAMPDIFF(SECOND,FROM_UNIXTIME(t.pagestart_time, '%Y-%m-%d %H:%i:%S'),FROM_UNIXTIME(t.pageend_time, '%Y-%m-%d %H:%i:%S'))) as avg_page_restime,*/\n" +
                    "  from sl_ods_page_visit_record_"+month+" t\n" +
                    "  where t.pagestart_time >= UNIX_TIMESTAMP(date_sub(curdate(),interval 1 day))\n" +
                    "    and t.pagestart_time < UNIX_TIMESTAMP(CURDATE())\n" +
                    "    and (t.pagestart_time is not null or t.pagestart_time <> 0)\n" +
                    "  GROUP BY t.channel_apk_id, t.page_id;";
            //关联sl_ods_page 表取出url地址
            String sqlDrop2 = "DROP TABLE IF EXISTS temp2_rpt_page_aim1;";
            String sql2 = "CREATE TABLE temp2_rpt_page_aim1 as\n" +
                    "SELECT t0.pagestart_time,\n" +
                    "       t0.channel_apk_id,\n" +
                    "       t0.page_id,\n" +
                    "       (case when t1.page_url is null then '未知页面地址' else t1.page_url end) aS page_url,\n" +
                    "       (case when t1.page_name is null then '未知页面描述' else t1.page_name end) aS page_name,\n" +
                    "       t0.page_pv,\n" +
                    "       t0.page_uv,\n" +
                    "       t0.avg_page_restime\n" +
                    "  from temp1_rpt_page_aim1 t0 LEFT JOIN sl_ods_page t1 on t0.page_id = t1.page_id ;";

            //汇总表
            /*先汇总渠道信息*/
            String sqlDrop3 = "DROP TABLE IF EXISTS temp0_rpt_page_aim1;";
            String sql3 = "CREATE TABLE temp0_rpt_page_aim1 as\n" +
                    "select t0.id,\n" +
                    "       t0.apptype,\n" +
                    "      (case when t0.apptype in ('1') then '用户端'\n" +
                    "            when t0.apptype in ('2') then '商户端'\n" +
                    "            when t0.apptype in ('3') then '管家端'\n" +
                    "       else '其它端' end) as prod_name,\n" +
                    "       t0.channel_typeid,\n" +
                    "       t1.name as channel_typename,\n" +
                    "       t0.name as name,\n" +
                    "       t0.description\n" +
                    "  from sl_ods_channel_apk t0 join sl_ods_channel_type t1 on t0.channel_typeid = t1.id\n" +
                    " group by t0.id, t0.apptype, t0.channel_typeid, t0.name, t0.description ;";

            //先删除当天已经写入的数据
            String sqlDrop4 = "DELETE from sl_rpt_app_page_aim1 where day_no  = DATE_FORMAT(adddate(now(),-1), '%Y%m%d');";
            String sqlResult = "INSERT INTO sl_rpt_app_page_aim1\n" +
                    "select DATE_FORMAT(adddate(now(),-1), '%Y%m%d') as day_no, /*统计时间*/\n" +
                    "       t0.prod_name as prod_name, /*产品渠道*/\n" +
                    "       t0.channel_typename as app_channel_type, /*app推广类型*/\n" +
                    "       t0.description as app_channel, /*app推广渠道*/\n" +
                    "       (case when t1.page_url is null then '无' else t1.page_url end) aS url_link, /*页面位置url*/\n" +
                    "       (case when t1.page_name is null then '无' else t1.page_name end) aS page_name, /*页面描述*/\n" +
                    "       SUM(case when t1.page_pv is null then 0 else t1.page_pv end) as page_pv, /*页面PV*/         \n" +
                    "       SUM(case when t1.page_uv is null then 0 else t1.page_uv end) as page_uv, /*页面UV*/\n" +
                    "       SUM(case when t1.avg_page_restime is null then 0 else t1.avg_page_restime end) as avg_residence_time /*页面平均停留时间*/\n" +
                    "  from temp0_rpt_page_aim1 t0 LEFT JOIN temp2_rpt_page_aim1 t1 on t0.id = t1.channel_apk_id\n" +
                    "GROUP BY t0.prod_name, t0.channel_typename, t0.description, t1.page_url, t1.page_name ;";
            //删除临时表
            String drop1 = "DROP TABLE IF EXISTS temp1_rpt_page_aim1;";
            String drop2 = "DROP TABLE IF EXISTS temp2_rpt_page_aim1;";
            String drop3 = "DROP TABLE IF EXISTS temp0_rpt_page_aim1;";

            try {
                //statement
                statement = connection.createStatement();

                statement.execute(beginLog);
                statement.execute(sqlDrop1);
                statement.execute(sqlDrop2);
                statement.execute(sqlDrop3);

                connection.setAutoCommit(false);
                statement.execute(sql1);
                statement.execute(sql2);
                statement.execute(sql3);
                statement.execute(sqlDrop4);
                statement.execute(sqlResult);
                statement.execute(endLog);
                connection.commit();
                connection.setAutoCommit(true);

                statement.execute(drop1);
                statement.execute(drop2);
                statement.execute(drop3);
            } catch (SQLException e) {
                logger.error("for sl_rpt_app_page_aim1 error:" + e.getMessage());
                try {
                    //rollback
                    connection.rollback();
                } catch (SQLException e1) {
                    logger.error("The sl_rpt_app_page_aim1 rollback error:" + e.getMessage());
                }
            } finally {
                try {
                    if (statement != null) statement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    logger.error("The sl_rpt_app_page_aim1 close connection error:" + e.getMessage());
                }
            }
        }
    };

    /**
     * 第四张报表:sl_rpt_life_cycle_users
     *名称 --%@NAME:用户生命周期分析--用户基础表
     *功能描述 --%@COMMENT: 展现系统用户生命周期阀值计算，用户基本资料
     *数据模块 --%@PARAM: RPT 集市域
     *执行周期 --%@PERIOD:按（天）
     *日志输入 --%@PARAM: SL_ODS_ETL_LOG : sl_ods_users,sl_ods_order
     *日志输出 --%@PARAM: SL_ODS_ETL_LOG : sl_rpt_life_cycle_users
     *日志参数 --%@PARAM: 过程JAR执行结果代码(日志状态，1-完成 0-开始，默认插入后均写 0),输出参数
     *日志描述 --%@PARAM: 过程执行结果描述,写入参数
     *创建人 --%@CREATOR: 覃小龙
     *创建时间 --%@CREATED_TIME: 2015-06-30
     *来源表 --%@FROM: sl_ods_users --ODS用户表
     *来源表 --%@FROM: sl_ods_order --ODS订单表
     *目标表 --%@TO:   sl_rpt_life_cycle_users --生命周期分析计算：用户基础表
     */
    public static TimerTask task4 = new TimerTask() {
        @Override
        public void run() {
            //使用或者下边的语句
            Connection connection = connect.getConnect();
            Statement statement = null;
            ResultSet rs=null;
            int rsCount=0;
            int tableCount=2;


            //向日志表sl_ods_etl_log插入sl_rpt_app_page_aim1表的更新记录
            String beginLog= LogSqlUtil.getBeginLog("sl_rpt_life_cycle_users");
            String endLog=LogSqlUtil.getEndLog("sl_rpt_life_cycle_users");


            //从sl_ods_etl_log日志表查询sl_ods_users表、sl_ods_order表相应日志记录状态
            String sqlStatus="SELECT table_name,status from  sl_ods_etl_log \n" +
                    "WHERE (lower(table_name)=lower('sl_ods_users') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n" +
                    "OR (lower(table_name)=lower('sl_ods_order') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d'));";


            /*生成基础表*/

            String sqlDrop1 = "DROP TABLE IF EXISTS temp1_rpt_life_cycle_users;";
            String sql1 = "CREATE TABLE temp1_rpt_life_cycle_users as\n" +
                    "select \n" +
                    "  DATE_FORMAT(adddate(now(),-1), '%Y%m%d') as day_no, /*统计时间*/\n" +
                    "  province as province, /*省*/\n" +
                    "  city as city, /*市*/\n" +
                    "  district as town, /*区县*/\n" +
                    "  street as village, /*小区*/\n" +
                    "  t.address as address, /*注册地址*/\n" +
                    "  t.id as uid, /*用户ID*/\n" +
                    "  t.nickname /*name*/ as name, /*姓名*/\n" +
                    "  t.phone as phone, /*电话*/\n" +
                    "  DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(FROM_UNIXTIME(t.birth, '%Y%m%d'))), '%Y')+0 as age, /*年龄*/\n" +
                    "  (case when (MONTH(FROM_UNIXTIME(t.birth, '%Y%m%d')) = MONTH(DATE_FORMAT(now(), '%Y%m%d'))) and\n" +
                    "             (DAY(FROM_UNIXTIME(t.birth, '%Y%m%d')) = DAY(DATE_FORMAT(now(), '%Y%m%d')))\n" +
                    "        then 1 else 0 end) as birth_flag, /*是否过生*/\n" +
                    "  DATEDIFF(CAST(CONCAT(YEAR(NOW()),DATE_FORMAT(FROM_UNIXTIME(t.birth, '%Y%m%d'),'%m%d'))AS DATE),CAST(DATE_FORMAT(NOW(),'%y-%m-%d') AS DATE)) as birth_away_from_flag, /*距离几天过生标示*/\n" +
                    "  FROM_UNIXTIME(t.birth, '%Y%m%d') as birth, /*出生年月*/\n" +
                    "  CONCAT(substr(FROM_UNIXTIME(t.birth, '%Y%m%d'),3,1),'0') as birth_zeros_flag, /*出生几零后标示。如：60,70,80,90,00*/\n" +
                    "  (case when t.sex is null or t.sex='' then 1 else t.sex end) as sex, /*性别 男-1 女-0*/\n" +
                    "  t.addtime as reg_time, /*注册时间*/\n" +
                    "  TIMESTAMPDIFF(DAY,FROM_UNIXTIME(t.addtime, '%Y-%m-%d %H:%i:%S'),now()) as reg_age, /*网龄*/\n" +
                    "  t.code as code, /*邮编*/\n" +
                    "  t.email as email, /*电子邮件*/\n" +
                    "  t.points as points, /*会员积分*/\n" +
                    "  t.pointslevel as pointslevel, /*用户等级id*/\n" +
                    "  t.communityid as communityid /*社区id*/\n" +
                    " from sl_ods_users t\n" +
                    "where t.state in (1)\n" +
                    "  and t.id is not null\n" +
                    "  and (t.addtime is not null or t.addtime <> 0);";
            /*生成登录次数*/
            String sqlDrop2 = "DROP TABLE IF EXISTS temp2_rpt_life_cycle_users;";
            String sql2 = "CREATE TABLE temp2_rpt_life_cycle_users as\n" +
                    "select * from sl_ods_page_visit_record_" + GetMonth.getOrtherMonth(-3) + "\n" +
                    " union all\n" +
                    "select * from sl_ods_page_visit_record_" + GetMonth.getOrtherMonth(-2) +"\n" +
                    " union all\n" +
                    "select * from sl_ods_page_visit_record_" + GetMonth.getOrtherMonth(-1) +"\n" +
                    " union all\n" +
                    "select * from sl_ods_page_visit_record_" + GetMonth.getOrtherMonth(0) +";";

            String sqlDrop3 = "DROP TABLE IF EXISTS temp3_rpt_life_cycle_users;";
            String sql3 = "CREATE TABLE temp3_rpt_life_cycle_users as\n" +
                    "SELECT t.uid as uid, /*用户ID*/\n" +
                    "       count(*) as login_num\n" +
                    "  from temp2_rpt_life_cycle_users t\n" +
                    "  where t.uid not in ('-1') and t.uid is not null\n" +
                    "    and page_id in (1) /*确认登录页面的ID*/\n" +
                    "    and t.pagestart_time >= UNIX_TIMESTAMP(date_sub(curdate(),interval 91 day))\n" +
                    "    and t.pagestart_time < UNIX_TIMESTAMP(CURDATE())\n" +
                    "    and (t.pagestart_time is not null or t.pagestart_time <> 0)\n" +
                    "  GROUP BY t.uid;";
            /*生成订单信息*/
            String sqlDrop4 = "DROP TABLE IF EXISTS temp4_rpt_life_cycle_users;";
            String sql4= "CREATE TABLE temp4_rpt_life_cycle_users as\n" +
                    "select t.uid as uid, /*用户ID*/\n" +
                    "       count(t.id) as orders_num, /*累计订单量*/\n" +
                    "       round(sum(case when t.total is null then 0 else t.total end),2) as orders_total_price /*累计订单金额*/\n" +
                    "  from sl_ods_order t\n" +
                    " where t.state not in (-1) \n" +
                    "   and t.uid is not null\n" +
                    "   and t.addtime >= UNIX_TIMESTAMP(date_sub(curdate(),interval 91 day))\n" +
                    "   and t.addtime < UNIX_TIMESTAMP(CURDATE())\n" +
                    "   and (t.addtime is not null or t.addtime <> 0)\n" +
                    "group by t.uid;";



            /*每天全量更新数据*/
            String sqlDrop5 = "truncate table sl_rpt_life_cycle_users;";
            String sqlResult = "insert into sl_rpt_life_cycle_users\n" +
                    "select t0.day_no,\n" +
                    "       t0.province,\n" +
                    "       t0.city,\n" +
                    "       t0.town,\n" +
                    "       t0.village,\n" +
                    "       t0.address, \n" +
                    "       t0.uid, \n" +
                    "       t0.name, \n" +
                    "       t0.phone, \n" +
                    "       t0.age,\n" +
                    "       t0.birth_flag, \n" +
                    "       t0.birth_away_from_flag, \n" +
                    "       t0.birth, \n" +
                    "       t0.birth_zeros_flag,\n" +
                    "       t0.sex, \n" +
                    "       t0.reg_time, \n" +
                    "       t0.reg_age, \n" +
                    "       t0.code, \n" +
                    "       t0.email, \n" +
                    "       t0.points, \n" +
                    "       t0.pointslevel, /*用户等级id*/\n" +
                    "       (case when t3.id is null or t0.pointslevel in ('-1') then '非会员' else t3.levelname end) as levelname, /*等级描述*/ \n" +
                    "       (case when t4.uid is not null then 1 else 0 end) as intelligence_flag, /*是否智能机用户标示：1-是 0-否*/\n" +
                    "       (case when t4.type is not null then t4.type else '-1' end) as mobile_device_type, /*终端类型：1安卓，2ios，-1未知*/\n" +
                    "       (case when t4.model_name is not null then t4.model_name else '-1' end) as mobile_device_model_name, /*手机型号：mi 2s -1未知*/\n" +
                    "       (case when t4.system_version is not null then t4.system_version else '-1' end) as mobile_device_system_version, /*系统版本 -1未知*/\n" +
                    "       (case when t4.screen_resolution is not null then t4.screen_resolution else '-1' end) as mobile_device_screen_resolution, /*屏幕分辨率 -1未知*/\n" +
                    "        t0.communityid,\n" +
                    "       (case when t0.uid is null then 0 else t1.login_num end) as login_num,\n" +
                    "       (case when t0.uid is null then 0 else t2.orders_num end) as orders_num,\n" +
                    "       (case when t0.uid is null then 0 else t2.orders_total_price end) as orders_total_price\n" +
                    "  from temp1_rpt_life_cycle_users t0 left join temp3_rpt_life_cycle_users t1 on t0.uid = t1.uid\n" +
                    "                                     left join temp4_rpt_life_cycle_users t2 on t0.uid = t2.uid\n" +
                    "                                     left join (select * from sl_ods_user_integral_level where state in (1)) t3 on t0.pointslevel = t3.id\n" +
                    "                                     left join (select * from sl_ods_mobile_device where app_type in (1)) t4 on t0.uid = t4.uid ;";
            //删除临时表
            String drop1 = "DROP TABLE IF EXISTS temp1_rpt_life_cycle_users;";
            String drop2 = "DROP TABLE IF EXISTS temp2_rpt_life_cycle_users;";
            String drop3 = "DROP TABLE IF EXISTS temp3_rpt_life_cycle_users;";
            String drop4 = "DROP TABLE IF EXISTS temp4_rpt_life_cycle_users;";

            try {
                //statement
                statement = connection.createStatement();
                rs= statement.executeQuery(sqlStatus);
                int table1_count=0;
                int table2_count=0;
                while (rs.next())
                {
                    if(rs.getString("table_name").equals("sl_ods_users")&&rs.getInt("status")==1){
                        table1_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_order")&&rs.getInt("status")==1){
                        table2_count=1;
                        continue;
                    }
                }
                rsCount=table1_count+table2_count;
                statement.execute(beginLog);
                if (rsCount==tableCount){
                    statement.execute(sqlDrop1);
                    statement.execute(sqlDrop2);
                    statement.execute(sqlDrop3);
                    statement.execute(sqlDrop4);
                    statement.execute(sqlDrop5);

                    connection.setAutoCommit(false);
                    statement.execute(sql1);
                    statement.execute(sql2);
                    statement.execute(sql3);
                    statement.execute(sql4);
                    statement.execute(sqlResult);
                    statement.execute(endLog);

                    connection.commit();
                    connection.setAutoCommit(true);

                    statement.execute(drop1);
                    statement.execute(drop2);
                    statement.execute(drop3);
                    statement.execute(drop4);

                }else {
                    logger.error("日志表ods层关于表sl_ods_users、sl_ods_users其中有异常（失败）情况" );
                }

            } catch (SQLException e) {
                logger.error("for sl_rpt_life_cycle_users error:" + e.getMessage());
                try {
                    //rollback
                    connection.rollback();
                } catch (SQLException e1) {
                    logger.error("The sl_rpt_life_cycle_users rollback error:" + e.getMessage());
                }
            } finally {
                try {
                    if (statement != null) statement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    logger.error("The sl_rpt_life_cycle_users close connection error:" + e.getMessage());
                }
            }
        }
    };

    /**
     * 第5，6张报表
     * 店铺销售分析* sl_rpt_oder_any_aim1
     * 用户消费表 *sl_rpt_life_cycle_users_consume
     *功能描述 --%@COMMENT: 数据从ODS到RPT层
     *数据模块 --%@PARAM: RPT 集市域
     *执行周期 --%@PERIOD:按（天）
     *日志输入 --%@PARAM: SL_ODS_ETL_LOG : sl_ods_order,sl_ods_detail,sl_ods_goods,sl_ods_shops,sl_ods_goodstype
     *日志输出 --%@PARAM: SL_ODS_ETL_LOG : sl_rpt_oder_any_aim1,sl_rpt_life_cycle_users_consume
     *日志参数 --%@PARAM: 过程JAR执行结果代码(日志状态，1-完成 0-开始，默认插入后均写 0),输出参数
     *日志描述 --%@PARAM: 过程执行结果描述,写入参数
     *创建人 --%@CREATOR: 覃小龙
     *创建时间 --%@CREATED_TIME: 2015-07-02
     *来源表 --%@FROM: sl_ods_order --ODS订单表
     *来源表 --%@FROM: sl_ods_detail
     *来源表 --%@FROM: sl_ods_goods --ODS商品表
     *来源表 --%@FROM: sl_ods_shops --ODS商铺表
     *来源表 --%@FROM: sl_ods_goodstype --ODS商品类型表
     *目标表 --%@TO:   sl_rpt_oder_any_aim1 --店铺销售分析
     *目标表 --%@TO:   sl_rpt_life_cycle_users_consume --用户消费表
     */

    public static TimerTask task5 = new TimerTask() {
        @Override
        public void run() {
            //使用或者下边的语句
            Connection connection = connect.getConnect();
            Statement statement = null;
            ResultSet rs=null;
            int rsCount=0;
            int tableCount=5;


            //向日志表sl_ods_etl_log插入sl_rpt_oder_any_aim1表的更新记录
            String beginLog_oder= LogSqlUtil.getBeginLog("sl_rpt_oder_any_aim1");
            String endLog_oder=LogSqlUtil.getEndLog("sl_rpt_oder_any_aim1");

            //向日志表sl_ods_etl_log插入sl_rpt_life_cycle_users_consume表的更新记录
            String beginLog_life= LogSqlUtil.getBeginLog("sl_rpt_life_cycle_users_consume");
            String endLog_life=LogSqlUtil.getEndLog("sl_rpt_life_cycle_users_consume");


            //从sl_ods_etl_log日志表查询sl_ods_order、sl_ods_detail、sl_ods_shops、sl_ods_goods、sl_ods_goodstype表相应日志记录状态
            String sqlStatus="SELECT table_name,status from  sl_ods_etl_log \n" +
                    "WHERE (lower(table_name)=lower('sl_ods_shops') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n" +
                    "OR (lower(table_name)=lower('sl_ods_order') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n" +
                    "OR (lower(table_name)=lower('sl_ods_detail') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n" +
                    "OR (lower(table_name)=lower('sl_ods_goods') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n" +
                    "OR (lower(table_name)=lower('sl_ods_goodstype') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d'));";


           /*生成订单基础表*/

            String sqlDrop1 = "DROP TABLE IF EXISTS temp1_rpt_life_cycle_users_consume;";
            String sql1 = "CREATE TABLE temp1_rpt_life_cycle_users_consume as\n" +
                    "select \n" +
                    "  DATE_FORMAT(adddate(now(),-1), '%Y%m%d') as day_no, /*统计时间*/\n" +
                    "  province as province, /*省*/\n" +
                    "  city as city, /*市*/\n" +
                    "  district  as town, /*区县*/\n" +
                    "  street  as village, /*小区*/\n" +
                    "  t.address as address, /*订购地址*/\n" +
                    "  t.id as order_id, /*订单id*/\n" +
                    "  t.shopsid as shopsid, /*商店id号*/\n" +
                    "  t.uid as uid, /*用户ID*/\n" +
                    "  t1.goodsid as goodsid, /*商品id*/\n" +
                    "  t1.name as goods_name, /*商品名称*/\n" +
                    "  0 as brandid, /*品牌id*/\n" +
                    "  0 as brand_name, /*品牌*/\n" +
                    "  t.addtime as order_addtime, /*购买时间（下单时间）*/\n" +
                    "  round(sum(case when t1.price is null then 0 else t1.price end),2) as total, /*总金额*/\n" +
                    "  round(sum(case when t1.price is null then 0 else t1.price end),2) as goods_total_price, /*商品总价格*/\n" +
                    "  round(sum(case when t.freight_total_price is null then 0 else t.freight_total_price end),2) as freight_total_price, /*运费总价格*/\n" +
                    "  round(sum(case when t.coupon_total_price is null then 0 else t.coupon_total_price end),2) as coupon_total_price /*代金券总价格*/\n" +
                    "\n" +
                    "from sl_ods_order t join sl_ods_detail t1 on t.id = t1.orderid\n" +
                    " where t.state not in (-1) \n" +
                    "   and t.uid is not null\n" +
                    "   and (t.addtime is not null or t.addtime <> 0)\n" +
                    "group by t.address, t.id, t.shopsid, t.uid, t1.goodsid, t1.name, t.addtime ;";
//                    "   and t.addtime >= UNIX_TIMESTAMP(date_sub(curdate(),interval 90 day))\n" +
//                    "   and t.addtime <= UNIX_TIMESTAMP(CURDATE())\n" +

            /*生成店铺销售分析*/
            String sqlDrop2 = "truncate table sl_rpt_oder_any_aim1;";
            String sql2 = "insert into sl_rpt_oder_any_aim1\n" +
                    " select FROM_UNIXTIME(t.order_addtime, '%Y%m%d') as day_no, /*统计时间：订购时间*/\n" +
                    "        t.province as province, /*省*/\n" +
                    "        t.city as city, /*市*/\n" +
                    "        t.town as town, /*县区*/\n" +
                    "        t.village as village, /*小区*/\n" +
                    "        t.address as address, /*订购地址*/\n" +
                    "        (case when t1.shopsname is null then '未知店铺' else t1.shopsname end) as shopsname, /*店铺名称*/\n" +
                    "        count(distinct t.goodsid) as goods_num, /*商品购买量*/\n" +
                    "        count(distinct t.order_id) as orders_num, /*订单量*/\n" +
                    "        round(sum(case when t.total is null then 0 else t.total end),2) as total /*订单金额*/\n" +
                    "   from temp1_rpt_life_cycle_users_consume t left join sl_ods_shops t1 on t.shopsid = t1.id\n" +
                    "  group by t.order_addtime, t.province, t.city, t.town, t.village, t.address, t1.shopsname ;";
            /*扩展出商品类型*/
            String sqlDrop3 = "DROP TABLE IF EXISTS temp2_rpt_life_cycle_users_consume;";
            String sql3 = "CREATE TABLE temp2_rpt_life_cycle_users_consume as\n" +
                    "select t.day_no,\n" +
                    "       t.province,\n" +
                    "       t.city,\n" +
                    "       t.town,\n" +
                    "       t.village,\n" +
                    "       t.address,\n" +
                    "       t.order_id,\n" +
                    "       t.shopsid,\n" +
                    "       t.uid,\n" +
                    "       t.goodsid,\n" +
                    "       t.goods_name,\n" +
                    "       (case when t2.typeid is null then -1 else t2.typeid end) as typeid, /*商品类别id*/\n" +
                    "       t.brandid,\n" +
                    "       t.brand_name,\n" +
                    "       t.order_addtime,\n" +
                    "       t.total,\n" +
                    "       t.goods_total_price,\n" +
                    "       t.freight_total_price,\n" +
                    "       t.coupon_total_price\n" +
                    "  from temp1_rpt_life_cycle_users_consume t left join sl_ods_goods t2 on t.goodsid = t2.id ;";
            /*扩展商品类别名称，生成基础清单*/
            String sqlDrop4 = "truncate table sl_rpt_life_cycle_users_consume;";
            String sql4 = "insert into sl_rpt_life_cycle_users_consume\n" +
                    "select t.day_no,\n" +
                    "       t.province,\n" +
                    "       t.city,\n" +
                    "       t.town,\n" +
                    "       t.village,\n" +
                    "       t.address,\n" +
                    "       t.order_id,\n" +
                    "       t.shopsid,\n" +
                    "       t.uid,\n" +
                    "       t.goodsid,\n" +
                    "       t.goods_name,\n" +
                    "       t.typeid,\n" +
                    "       (case when t3.name is null then '未知类别' else t3.name end) as type_name, /*类别名称*/\n" +
                    "       t.brandid,\n" +
                    "       t.brand_name,\n" +
                    "       t.order_addtime,\n" +
                    "       t.total,\n" +
                    "       t.goods_total_price,\n" +
                    "       t.freight_total_price,\n" +
                    "       t.coupon_total_price\n" +
                    "  from temp2_rpt_life_cycle_users_consume t left join sl_ods_goodstype t3 on t.typeid = t3.id ;";




            //删除临时表
            String drop1 = "DROP TABLE IF EXISTS temp1_rpt_life_cycle_users_consume;";
            String drop2 = "DROP TABLE IF EXISTS temp2_rpt_life_cycle_users_consume;";

            try {
                //statement
                statement = connection.createStatement();
                rs= statement.executeQuery(sqlStatus);
                int table1_count=0;
                int table2_count=0;
                int table3_count=0;
                int table4_count=0;
                int table5_count=0;
                while (rs.next())
                {
                    if(rs.getString("table_name").equals("sl_ods_shops")&&rs.getInt("status")==1){
                        table1_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_order")&&rs.getInt("status")==1){
                        table2_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_detail")&&rs.getInt("status")==1){
                        table3_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_goods")&&rs.getInt("status")==1){
                        table4_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_goodstype")&&rs.getInt("status")==1){
                        table5_count=1;
                        continue;
                    }
                }
                rsCount=table1_count+table2_count+table3_count+table4_count+table5_count;
                statement.execute(beginLog_oder);
                if (rsCount==tableCount){
                    statement.execute(sqlDrop1);
                    statement.execute(sqlDrop2);
                    statement.execute(sqlDrop3);
                    statement.execute(sqlDrop4);

                    connection.setAutoCommit(false);
                    statement.execute(beginLog_life);
                    statement.execute(sql1);
                    statement.execute(sql2);
                    statement.execute(endLog_oder);
                    statement.execute(sql3);
                    statement.execute(sql4);
                    statement.execute(endLog_life);
                    connection.commit();
                    connection.setAutoCommit(true);

                    statement.execute(drop1);
                    statement.execute(drop2);
                }else {
                    logger.error("日志表ods层关于表sl_ods_shops、sl_ods_users、sl_ods_detail、sl_ods_goods、sl_ods_goodstype其中有异常（失败）情况" );
                }

            } catch (SQLException e) {
                logger.error("for sl_rpt_oder_any_aim1，sl_rpt_life_cycle_users_consume error:" + e.getMessage());
                try {
                    //rollback
                    connection.rollback();
                } catch (SQLException e1) {
                    logger.error("The sl_rpt_oder_any_aim1，sl_rpt_life_cycle_users_consume rollback error:" + e.getMessage());
                }
            } finally {
                try {
                    if (statement != null) statement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    logger.error("The sl_rpt_oder_any_aim1，sl_rpt_life_cycle_users_consume close connection error:" + e.getMessage());
                }
            }
        }
    };
    /**
     *第7张报表sl_rpt_life_cycle_users_flag(近2月活跃用户)
     *功能描述 --%@COMMENT: 数据从ODS到RPT层
     *数据模块 --%@PARAM: RPT 集市域
     *执行周期 --%@PERIOD:按（天）
     *日志输入 --%@PARAM: SL_ODS_ETL_LOG : sl_ods_order,sl_ods_detail
     *日志输出 --%@PARAM: SL_ODS_ETL_LOG : sl_rpt_life_cycle_users_flag
     *日志参数 --%@PARAM: 过程JAR执行结果代码(日志状态，1-完成 0-开始，默认插入后均写 0),输出参数
     *日志描述 --%@PARAM: 过程执行结果描述,写入参数
     *创建人 --%@CREATOR: 覃小龙
     *创建时间 --%@CREATED_TIME: 2015-07-03
     *来源表 --%@FROM: sl_ods_order --ODS订单表
     *来源表 --%@FROM: sl_ods_detail
     *目标表 --%@TO:   sl_rpt_life_cycle_users_flag --用户活跃表
     */

    public static TimerTask task6 = new TimerTask() {
        @Override
        public void run() {
            //使用或者下边的语句
            Connection connection = connect.getConnect();
            Statement statement = null;
            ResultSet rs=null;
            int rsCount=0;
            int tableCount=2;


            //向日志表sl_ods_etl_log插入sl_rpt_life_cycle_users_flag表的更新记录
            String beginLog= LogSqlUtil.getBeginLog("sl_rpt_life_cycle_users_flag");
            String endLog=LogSqlUtil.getEndLog("sl_rpt_life_cycle_users_flag");


            //从sl_ods_etl_log日志表查询sl_ods_order、sl_ods_detail表相应日志记录状态
            String sqlStatus="SELECT table_name,status from  sl_ods_etl_log \n" +
                    "WHERE (lower(table_name)=lower('sl_ods_order') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n" +
                    "OR (lower(table_name)=lower('sl_ods_detail') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d'));";


         /*取3个月日志样本数据*/

            String sqlDrop1 = "DROP TABLE IF EXISTS temp1_rpt_life_cycle_users_flag;";
            String sql1 = "CREATE TABLE temp1_rpt_life_cycle_users_flag as\n" +
                    "select * from sl_ods_page_visit_record_"+GetMonth.getOrtherMonth(-2)+"\n" +
                    " union all\n" +
                    "select * from sl_ods_page_visit_record_"+GetMonth.getOrtherMonth(-1)+"\n" +
                    " union all\n" +
                    "select * from sl_ods_page_visit_record_"+GetMonth.getOrtherMonth(0)+" ;";
           /*生成基础数据*/
            String sqlDrop2 = "DROP TABLE IF EXISTS temp2_rpt_life_cycle_users_flag;";
            String sql2 = "CREATE TABLE temp2_rpt_life_cycle_users_flag as\n" +
                    "SELECT DATE_FORMAT(adddate(now(),-1), '%Y%m%d') as day_no, /*统计时间*/\n" +
                    "       t.uid as uid, /*用户id*/\n" +
                    "       t.pagestart_time as pagestart_time, /*进入页面时间*/\n" +
                    "       t.pageend_time as pageend_time /*离开页面时间*/\n" +
                    "  from temp1_rpt_life_cycle_users_flag t\n" +
                    "  where t.uid not in ('-1') and t.uid is not null\n" +
                    "    and t.pagestart_time >= UNIX_TIMESTAMP(date_sub(curdate(),interval 61 day))\n" +
                    "    and t.pagestart_time < UNIX_TIMESTAMP(CURDATE())\n" +
                    "    and (t.pagestart_time is not null or t.pagestart_time <> 0)\n" +
                    "  GROUP BY t.uid, t.pagestart_time, t.pageend_time ;";
            /*近2月消费客户标示*/
            String sqlDrop3 = "DROP TABLE IF EXISTS temp3_rpt_life_cycle_users_flag;";
            String sql3 = "CREATE TABLE temp3_rpt_life_cycle_users_flag as\n" +
                    "select \n" +
                    "  DATE_FORMAT(adddate(now(),-1), '%Y%m%d') as day_no, /*统计时间*/\n" +
                    "  province as province, /*省*/\n" +
                    "  city as city, /*市*/\n" +
                    "  district  as town, /*区县*/\n" +
                    "  street  as village, /*小区*/\n" +
                    "  t.address as address, /*订购地址*/\n" +
                    "  t.id as order_id, /*订单id*/\n" +
                    "  t.shopsid as shopsid, /*商店id号*/\n" +
                    "  t.uid as uid, /*用户ID*/\n" +
                    "  t1.goodsid as goodsid, /*商品id*/\n" +
                    "  t1.name as goods_name, /*商品名称*/\n" +
                    "  0 as brandid, /*品牌id*/\n" +
                    "  0 as brand_name, /*品牌*/\n" +
                    "  t.addtime as order_addtime, /*购买时间（下单时间）*/\n" +
                    "  round(sum(case when t1.price is null then 0 else t1.price end),2) as total, /*总金额*/\n" +
                    "  round(sum(case when t1.price is null then 0 else t1.price end),2) as goods_total_price, /*商品总价格*/\n" +
                    "  round(sum(case when t.freight_total_price is null then 0 else t.freight_total_price end),2) as freight_total_price, /*运费总价格*/\n" +
                    "  round(sum(case when t.coupon_total_price is null then 0 else t.coupon_total_price end),2) as coupon_total_price /*代金券总价格*/\n" +
                    " from sl_ods_order t join sl_ods_detail t1 on t.id = t1.orderid\n" +
                    " where t.state not in (-1) \n" +
                    "   and t.uid is not null\n" +
                    "   and t.addtime >= UNIX_TIMESTAMP(date_sub(curdate(),interval 61 day))\n" +
                    "   and t.addtime < UNIX_TIMESTAMP(CURDATE())\n" +
                    "   and (t.addtime is not null or t.addtime <> 0)\n" +
                    "group by t.address, t.id, t.shopsid, t.uid, t1.goodsid, t1.name, t.addtime ;";

            String sqlDrop4 = "DROP TABLE IF EXISTS temp4_rpt_life_cycle_users_flag;";
            String sql4 = "CREATE TABLE temp4_rpt_life_cycle_users_flag as\n" +
                    "select t.uid  \n" +
                    "  from temp3_rpt_life_cycle_users_flag t\n" +
                    " where t.total > 0\n" +
                    " group by t.uid;";
            /*近2月活跃客户标示*/
             /*取本月*/
            String sqlDrop5 = "DROP TABLE IF EXISTS temp5_rpt_life_cycle_users_flag;";
            String sql5 = "CREATE TABLE temp5_rpt_life_cycle_users_flag as\n" +
                    "SELECT t.uid as uid1 /*用户id*/\n" +
                    "  from temp1_rpt_life_cycle_users_flag t\n" +
                    "  where t.uid not in ('-1') and t.uid is not null\n" +
                    "    and t.pagestart_time >= UNIX_TIMESTAMP(date_sub(curdate(),interval 31 day))\n" +
                    "    and t.pagestart_time < UNIX_TIMESTAMP(CURDATE())\n" +
                    "    and (t.pagestart_time is not null or t.pagestart_time <> 0)\n" +
                    "  GROUP BY t.uid;";

            /*取上月*/
            String sqlDrop6 = "DROP TABLE IF EXISTS temp6_rpt_life_cycle_users_flag;";
            String sql6 = "CREATE TABLE temp6_rpt_life_cycle_users_flag as\n" +
                    "SELECT t.uid as uid2 /*用户id*/\n" +
                    "  from temp1_rpt_life_cycle_users_flag t\n" +
                    "  where t.uid not in ('-1') and t.uid is not null\n" +
                    "    and t.pagestart_time >= UNIX_TIMESTAMP(date_sub(curdate(),interval 61 day))\n" +
                    "    and t.pagestart_time < UNIX_TIMESTAMP(date_sub(curdate(),interval 31 day))\n" +
                    "    and (t.pagestart_time is not null or t.pagestart_time <> 0)\n" +
                    "  GROUP BY t.uid;";
            /*取交集*/
            String sqlDrop7 = "DROP TABLE IF EXISTS temp7_rpt_life_cycle_users_flag;";
            String sql7 = "CREATE TABLE temp7_rpt_life_cycle_users_flag as\n" +
                    "select t1.uid1 as uid\n" +
                    "  from temp5_rpt_life_cycle_users_flag t1 join temp6_rpt_life_cycle_users_flag t2 on t1.uid1 = t2.uid2;";
            /*近1月沉默客户标示*/
            String sqlDrop8 = "DROP TABLE IF EXISTS temp8_rpt_life_cycle_users_flag;";
            String sql8 = "CREATE TABLE temp8_rpt_life_cycle_users_flag as\n" +
                    "select t1.uid2 as uid\n" +
                    "  from temp6_rpt_life_cycle_users_flag t1 left join temp5_rpt_life_cycle_users_flag t2 on t1.uid2 = t2.uid1\n" +
                    "where t2.uid1 is null ;";

            /*生成近2月活跃用户特征数据*/
            String sqlDrop9 = "truncate table sl_rpt_life_cycle_users_flag;";
            String sqlResult = "insert into sl_rpt_life_cycle_users_flag\n" +
                    "select t0.day_no as day_no,\n" +
                    "       t0.uid as uid,\n" +
                    "       t0.pagestart_time as pagestart_time,\n" +
                    "       t0.pageend_time as pageend_time,\n" +
                    "       (case when t1.uid is null then 0 else 1 end) as uid_consume_flag,\n" +
                    "       (case when t2.uid is null then 0 else 1 end) as uid_active_flag,\n" +
                    "       (case when t3.uid is null then 0 else 1 end) as uid_silence_flag\n" +
                    "  from temp2_rpt_life_cycle_users_flag t0 left join temp4_rpt_life_cycle_users_flag t1 on t0.uid = t1.uid\n" +
                    "                                          left join temp7_rpt_life_cycle_users_flag t2 on t0.uid = t2.uid\n" +
                    "                                          left join temp8_rpt_life_cycle_users_flag t3 on t0.uid = t3.uid ;";

            //删除临时表
            String drop1 = "DROP TABLE IF EXISTS temp1_rpt_life_cycle_users_flag;";
            String drop2 = "DROP TABLE IF EXISTS temp2_rpt_life_cycle_users_flag;";
            String drop3 = "DROP TABLE IF EXISTS temp3_rpt_life_cycle_users_flag;";
            String drop4 = "DROP TABLE IF EXISTS temp4_rpt_life_cycle_users_flag;";
            String drop5 = "DROP TABLE IF EXISTS temp5_rpt_life_cycle_users_flag;";
            String drop6 = "DROP TABLE IF EXISTS temp6_rpt_life_cycle_users_flag;";
            String drop7 = "DROP TABLE IF EXISTS temp7_rpt_life_cycle_users_flag;";
            String drop8 = "DROP TABLE IF EXISTS temp8_rpt_life_cycle_users_flag;";

            try {
                //statement
                statement = connection.createStatement();
                rs= statement.executeQuery(sqlStatus);
                int table1_count=0;
                int table2_count=0;
                while (rs.next())
                {
                    if(rs.getString("table_name").equals("sl_ods_order")&&rs.getInt("status")==1){
                        table1_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_detail")&&rs.getInt("status")==1){
                        table2_count=1;
                        continue;
                    }
                }
                rsCount=table1_count+table2_count;
                statement.execute(beginLog);
                if (rsCount==tableCount){
                    statement.execute(sqlDrop1);
                    statement.execute(sqlDrop2);
                    statement.execute(sqlDrop3);
                    statement.execute(sqlDrop4);
                    statement.execute(sqlDrop5);
                    statement.execute(sqlDrop6);
                    statement.execute(sqlDrop7);
                    statement.execute(sqlDrop8);
                    statement.execute(sqlDrop9);

                    connection.setAutoCommit(false);

                    statement.execute(sql1);
                    statement.execute(sql2);
                    statement.execute(sql3);
                    statement.execute(sql4);
                    statement.execute(sql5);
                    statement.execute(sql6);
                    statement.execute(sql7);
                    statement.execute(sql8);
                    statement.execute(sqlResult);

                    statement.execute(endLog);
                    connection.commit();

                    connection.setAutoCommit(true);
                    statement.execute(drop1);
                    statement.execute(drop2);
                    statement.execute(drop3);
                    statement.execute(drop4);
                    statement.execute(drop5);
                    statement.execute(drop6);
                    statement.execute(drop7);
                    statement.execute(drop8);

                }else {
                    logger.error("日志表ods层关于表sl_ods_detail、sl_ods_order其中有异常（失败）情况" );
                }

            } catch (SQLException e) {
                logger.error("for sl_rpt_life_cycle_users_flag error:" + e.getMessage());
                try {
                    //rollback
                    connection.rollback();
                } catch (SQLException e1) {
                    logger.error("The sl_rpt_life_cycle_users_flag rollback error:" + e.getMessage());
                }
            } finally {
                try {
                    if (statement != null) statement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    logger.error("The sl_rpt_life_cycle_users_flag close connection error:" + e.getMessage());
                }
            }
        }
    };

    /**
     * 第八张报表*所有用户特征标示表
     * sl_rpt_life_cycle_all_users_flag(所有用户)
     */
    public static TimerTask task7 = new TimerTask() {
        @Override
        public void run() {
            //使用或者下边的语句
            Connection connection = connect.getConnect();
            Statement statement = null;
            ResultSet rs=null;
            int rsCount=0;
            int tableCount=3;


            //向日志表sl_ods_etl_log插入sl_rpt_life_cycle_all_users_flag表的更新记录
            String beginLog= LogSqlUtil.getBeginLog("sl_rpt_life_cycle_all_users_flag");
            String endLog=LogSqlUtil.getEndLog("sl_rpt_life_cycle_all_users_flag");


            //从sl_ods_etl_log日志表查询sl_ods_users、sl_ods_order、sl_ods_detail表相应日志记录状态
            String sqlStatus="SELECT table_name,status from  sl_ods_etl_log \n" +
                    "WHERE (lower(table_name)=lower('sl_ods_users') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n" +
                    "OR (lower(table_name)=lower('sl_ods_order') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n" +
                    "OR (lower(table_name)=lower('sl_ods_detail') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d'));";

         /*形成用户基础数据*/
            String sqlDrop1 = "DROP TABLE IF EXISTS temp1_rpt_life_cycle_all_users;";
            String sql1 = "CREATE TABLE temp1_rpt_life_cycle_all_users as\n" +
                    "select \n" +
                    "  DATE_FORMAT(adddate(now(),-1), '%Y%m%d') as day_no, /*统计时间*/\n" +
                    "  province as province, /*省*/\n" +
                    "  city as city, /*市*/\n" +
                    "  district as town, /*区县*/\n" +
                    "  street  as village, /*小区*/\n" +
                    "  t.address as address, /*注册地址*/\n" +
                    "  t.id as uid, /*用户ID*/\n" +
                    "  t.nickname /*name*/ as name, /*姓名*/\n" +
                    "  t.phone as phone, /*电话*/\n" +
                    "  DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(FROM_UNIXTIME(t.birth, '%Y%m%d'))), '%Y')+0 as age, /*年龄*/\n" +
                    "  (case when (MONTH(FROM_UNIXTIME(t.birth, '%Y%m%d')) = MONTH(DATE_FORMAT(now(), '%Y%m%d'))) and\n" +
                    "             (DAY(FROM_UNIXTIME(t.birth, '%Y%m%d')) = DAY(DATE_FORMAT(now(), '%Y%m%d')))\n" +
                    "        then 1 else 0 end) as birth_flag, /*是否过生*/\n" +
                    "  DATEDIFF(CAST(CONCAT(YEAR(NOW()),DATE_FORMAT(FROM_UNIXTIME(t.birth, '%Y%m%d'),'%m%d'))AS DATE),CAST(DATE_FORMAT(NOW(),'%y-%m-%d') AS DATE)) as birth_away_from_flag, /*距离几天过生标示*/\n" +
                    "  FROM_UNIXTIME(t.birth, '%Y%m%d') as birth, /*出生年月*/\n" +
                    "  t.sex as sex, /*性别 男-1 女-0*/\n" +
                    "  t.addtime as reg_time, /*注册时间*/\n" +
                    "  TIMESTAMPDIFF(DAY,FROM_UNIXTIME(t.addtime, '%Y-%m-%d %H:%i:%S'),now()) as reg_age, /*网龄*/\n" +
                    "  t.code as code, /*邮编*/\n" +
                    "  t.email as email, /*电子邮件*/\n" +
                    "  t.points as points, /*会员积分*/\n" +
                    "  t.communityid as communityid /*社区id*/\n" +
                    " from sl_ods_users t\n" +
                    "where t.state in (1)\n" +
                    "  and t.id is not null\n" +
                    "  and (t.addtime is not null or t.addtime <> 0);";
           /*近1周消费用户*/
            String sqlDrop2 = "DROP TABLE IF EXISTS temp2a_rpt_life_cycle_all_users;";
            String sql2 = "CREATE TABLE temp2a_rpt_life_cycle_all_users as\n" +
                    "select \n" +
                    "  t.uid as uid, /*用户ID*/\n" +
                    "  round(sum(case when t.total is null then 0 else t.total end),2) as total /*总金额*/\n" +
                    "from sl_ods_order t join sl_ods_detail t1 on t.id = t1.orderid\n" +
                    " where t.state not in (-1) \n" +
                    "   and t.uid is not null\n" +
                    "   and t.addtime >= UNIX_TIMESTAMP(date_sub(curdate(),interval 7 day))\n" +
                    "   and t.addtime < UNIX_TIMESTAMP(CURDATE())\n" +
                    "   and (t.addtime is not null or t.addtime <> 0)\n" +
                    "group by t.id ;";
            /*本月消费用户*/
            String sqlDrop3 = "DROP TABLE IF EXISTS temp2b_rpt_life_cycle_all_users;";
            String sql3 = "CREATE TABLE temp2b_rpt_life_cycle_all_users as\n" +
                    "select \n" +
                    "  t.uid as uid, /*用户ID*/\n" +
                    "  round(sum(case when t.total is null then 0 else t.total end),2) as total /*总金额*/\n" +
                    "from sl_ods_order t join sl_ods_detail t1 on t.id = t1.orderid\n" +
                    " where t.state not in (-1) \n" +
                    "   and t.uid is not null\n" +
                    "   and t.addtime >= UNIX_TIMESTAMP(date_sub(curdate(),interval 31 day))\n" +
                    "   and t.addtime < UNIX_TIMESTAMP(CURDATE())\n" +
                    "   and (t.addtime is not null or t.addtime <> 0)\n" +
                    "group by t.uid ;";
              /*上月消费用户*/
            String sqlDrop4 = "DROP TABLE IF EXISTS temp2c_rpt_life_cycle_all_users;";
            String sql4 = "CREATE TABLE temp2c_rpt_life_cycle_all_users as\n" +
                    "select \n" +
                    "  t.uid as uid, /*用户ID*/\n" +
                    "  round(sum(case when t.total is null then 0 else t.total end),2) as total /*总金额*/\n" +
                    "from sl_ods_order t join sl_ods_detail t1 on t.id = t1.orderid\n" +
                    " where t.state not in (-1) \n" +
                    "   and t.uid is not null\n" +
                    "   and t.addtime >= UNIX_TIMESTAMP(date_sub(curdate(),interval 61 day))\n" +
                    "   and t.addtime < UNIX_TIMESTAMP(date_sub(curdate(),interval 31 day))\n" +
                    "   and (t.addtime is not null or t.addtime <> 0)\n" +
                    "group by t.uid ;";
            /*上上月消费用户*/
            String sqlDrop5 = "DROP TABLE IF EXISTS temp2d_rpt_life_cycle_all_users;";
            String sql5 = "CREATE TABLE temp2d_rpt_life_cycle_all_users as\n" +
                    "select \n" +
                    "  t.uid as uid, /*用户ID*/\n" +
                    "  round(sum(case when t.total is null then 0 else t.total end),2) as total /*总金额*/\n" +
                    "from sl_ods_order t join sl_ods_detail t1 on t.id = t1.orderid\n" +
                    " where t.state not in (-1) \n" +
                    "   and t.uid is not null\n" +
                    "   and t.addtime >= UNIX_TIMESTAMP(date_sub(curdate(),interval 91 day))\n" +
                    "   and t.addtime < UNIX_TIMESTAMP(date_sub(curdate(),interval 61 day))\n" +
                    "   and (t.addtime is not null or t.addtime <> 0)\n" +
                    "group by t.uid ;";

            /*连续3月消费用户标示*/
            String sqlDrop6 = "DROP TABLE IF EXISTS temp2_rpt_life_cycle_all_users;";
            String sql6 = "CREATE TABLE temp2_rpt_life_cycle_all_users as\n" +
                    "select t1.uid\n" +
                    "  from temp2b_rpt_life_cycle_all_users t1 join temp2c_rpt_life_cycle_all_users t2 on t1.uid = t2.uid\n" +
                    "                                          join temp2d_rpt_life_cycle_all_users t3 on t1.uid = t3.uid\n" +
                    " where t1.total >0 and t2.total>0 and t3.total>0 ;";
            /*连续2月消费用户标示*/
            String sqlDrop7 = "DROP TABLE IF EXISTS temp3_rpt_life_cycle_all_users;";
            String sql7 = "CREATE TABLE temp3_rpt_life_cycle_all_users as\n" +
                    "select t1.uid\n" +
                    "  from temp2b_rpt_life_cycle_all_users t1 join temp2c_rpt_life_cycle_all_users t2 on t1.uid = t2.uid\n" +
                    " where t1.total >0\n" +
                    "union \n" +
                    "select t2.uid\n" +
                    "  from temp2c_rpt_life_cycle_all_users t2 join temp2d_rpt_life_cycle_all_users t3 on t2.uid = t3.uid\n" +
                    " where t2.total >0 ;";
            /*最近1月消费用户标示*/
            String sqlDrop8 = "DROP TABLE IF EXISTS temp4_rpt_life_cycle_all_users;";
            String sql8 = "CREATE TABLE temp4_rpt_life_cycle_all_users as\n" +
                    "select t.uid\n" +
                    "  from temp2b_rpt_life_cycle_all_users t\n" +
                    " where t.total >0 ;";

            /*最近1周消费用户标示*/
            String sqlDrop9 = "DROP TABLE IF EXISTS temp5_rpt_life_cycle_all_users;";
            String sql9 = "CREATE TABLE temp5_rpt_life_cycle_all_users as\n" +
                    "select t.uid\n" +
                    "  from temp2a_rpt_life_cycle_all_users t\n" +
                    " where t.total >0 ;";

            /*近3月消费金额*/
            String sqlDrop10 = "DROP TABLE IF EXISTS temp6_rpt_life_cycle_all_users;";
            String sql10 = "CREATE TABLE temp6_rpt_life_cycle_all_users as\n" +
                    "select \n" +
                    "  t.uid as uid, /*用户ID*/\n" +
                    "  round(sum(case when t.total is null then 0 else t.total end),2) as total /*总金额*/\n" +
                    "from sl_ods_order t join sl_ods_detail t1 on t.id = t1.orderid\n" +
                    " where t.state not in (-1) \n" +
                    "   and t.uid is not null\n" +
                    "   and t.addtime >= UNIX_TIMESTAMP(date_sub(curdate(),interval 91 day))\n" +
                    "   and t.addtime < UNIX_TIMESTAMP(CURDATE())\n" +
                    "   and (t.addtime is not null or t.addtime <> 0)\n" +
                    "group by t.uid ;";
            /*余额习惯，由于银行数据没有，暂时设置为零*/
            /*活跃度*/
            /*取4个月日志样本数据*/
            String sqlDrop11 = "DROP TABLE IF EXISTS temp7a0_rpt_life_cycle_all_users;";
            String sql11 = "CREATE TABLE temp7a0_rpt_life_cycle_all_users as\n" +
                    "select * from sl_ods_page_visit_record_"+GetMonth.getOrtherMonth(-3)+"\n" +
                    " union all\n" +
                    "select * from sl_ods_page_visit_record_"+GetMonth.getOrtherMonth(-2)+"\n" +
                    " union all\n" +
                    "select * from sl_ods_page_visit_record_"+GetMonth.getOrtherMonth(-1)+"\n" +
                    " union all\n" +
                    "select * from sl_ods_page_visit_record_"+GetMonth.getOrtherMonth(0)+" ;";
            /*近1周活跃客户*/
            String sqlDrop12 = "DROP TABLE IF EXISTS temp7a_rpt_life_cycle_all_users;";
            String sql12 = "CREATE TABLE temp7a_rpt_life_cycle_all_users as\n" +
                    "SELECT t.uid as uid /*用户id*/\n" +
                    "  from temp7a0_rpt_life_cycle_all_users t\n" +
                    "  where t.uid not in ('-1') and t.uid is not null\n" +
                    "    and t.pagestart_time >= UNIX_TIMESTAMP(date_sub(curdate(),interval 7 day))\n" +
                    "    and t.pagestart_time < UNIX_TIMESTAMP(CURDATE())\n" +
                    "    and (t.pagestart_time is not null or t.pagestart_time <> 0)\n" +
                    "  GROUP BY t.uid;";
            /*本月活跃用户*/
            String sqlDrop13 = "DROP TABLE IF EXISTS temp7b_rpt_life_cycle_all_users;";
            String sql13 = "CREATE TABLE temp7b_rpt_life_cycle_all_users as\n" +
                    "SELECT t.uid as uid /*用户id*/\n" +
                    "  from temp7a0_rpt_life_cycle_all_users t\n" +
                    "  where t.uid not in ('-1') and t.uid is not null\n" +
                    "    and t.pagestart_time >= UNIX_TIMESTAMP(date_sub(curdate(),interval 31 day))\n" +
                    "    and t.pagestart_time < UNIX_TIMESTAMP(CURDATE())\n" +
                    "    and (t.pagestart_time is not null or t.pagestart_time <> 0)\n" +
                    "  GROUP BY t.uid;";
            /*上月活跃用户*/
            String sqlDrop14 = "DROP TABLE IF EXISTS temp7c_rpt_life_cycle_all_users;";
            String sql14 = "CREATE TABLE temp7c_rpt_life_cycle_all_users as\n" +
                    "SELECT t.uid as uid /*用户id*/\n" +
                    "  from temp7a0_rpt_life_cycle_all_users t\n" +
                    "  where t.uid not in ('-1') and t.uid is not null\n" +
                    "    and t.pagestart_time >= UNIX_TIMESTAMP(date_sub(curdate(),interval 61 day))\n" +
                    "    and t.pagestart_time < UNIX_TIMESTAMP(date_sub(curdate(),interval 31 day))\n" +
                    "    and (t.pagestart_time is not null or t.pagestart_time <> 0)\n" +
                    "  GROUP BY t.uid;";
            /*上上月活跃用户*/
            String sqlDrop15 = "DROP TABLE IF EXISTS temp7d_rpt_life_cycle_all_users;";
            String sql15 = "CREATE TABLE temp7d_rpt_life_cycle_all_users as\n" +
                    "SELECT t.uid as uid /*用户id*/\n" +
                    "  from temp7a0_rpt_life_cycle_all_users t\n" +
                    "  where t.uid not in ('-1') and t.uid is not null\n" +
                    "    and t.pagestart_time >= UNIX_TIMESTAMP(date_sub(curdate(),interval 91 day))\n" +
                    "    and t.pagestart_time < UNIX_TIMESTAMP(date_sub(curdate(),interval 61 day))\n" +
                    "    and (t.pagestart_time is not null or t.pagestart_time <> 0)\n" +
                    "  GROUP BY t.uid;";
            /*连续3月活跃客户标示*/
            String sqlDrop16 = "DROP TABLE IF EXISTS temp7_rpt_life_cycle_all_users;";
            String sql16 = "CREATE TABLE temp7_rpt_life_cycle_all_users as\n" +
                    "select t1.uid\n" +
                    "  from temp7b_rpt_life_cycle_all_users t1 join temp7c_rpt_life_cycle_all_users t2 on t1.uid = t2.uid\n" +
                    "                                          join temp7d_rpt_life_cycle_all_users t3 on t1.uid = t3.uid;";
            /*连续2月活跃客户标示*/
            String sqlDrop17 = "DROP TABLE IF EXISTS temp8_rpt_life_cycle_all_users;";
            String sql17 = "CREATE TABLE temp8_rpt_life_cycle_all_users as\n" +
                    "select t1.uid\n" +
                    "  from temp7b_rpt_life_cycle_all_users t1 join temp7c_rpt_life_cycle_all_users t2 on t1.uid = t2.uid\n" +
                    "union \n" +
                    "select t2.uid\n" +
                    "  from temp7c_rpt_life_cycle_all_users t2 join temp7d_rpt_life_cycle_all_users t3 on t2.uid = t3.uid;";
            /*近1月活跃客户标示*/
            String sqlDrop18 = "DROP TABLE IF EXISTS temp9_rpt_life_cycle_all_users;";
            String sql18 = "CREATE TABLE temp9_rpt_life_cycle_all_users as\n" +
                    "select t.uid\n" +
                    "  from temp7b_rpt_life_cycle_all_users t;";
            /*近1周活跃客户标示*/
            String sqlDrop19 = "DROP TABLE IF EXISTS temp10_rpt_life_cycle_all_users;";
            String sql19 = "CREATE TABLE temp10_rpt_life_cycle_all_users as\n" +
                    "select t.uid\n" +
                    "  from temp7a_rpt_life_cycle_all_users t;";
             /*近3月总活跃用户*/
            String sqlDrop20 = "DROP TABLE IF EXISTS temp11_rpt_life_cycle_all_users;";
            String sql20 = "CREATE TABLE temp11_rpt_life_cycle_all_users as\n" +
                    "select t1.uid\n" +
                    "  from temp7b_rpt_life_cycle_all_users t1 \n" +
                    "union \n" +
                    "select t2.uid\n" +
                    "  from temp7c_rpt_life_cycle_all_users t2 \n" +
                    "union \n" +
                    "select t3.uid\n" +
                    "  from temp7d_rpt_life_cycle_all_users t3;";
            /*先合并上月和本月有访问记录用户*/
            String sqlDrop21 = "DROP TABLE IF EXISTS temp12a_rpt_life_cycle_all_users;";
            String sql21 = "CREATE TABLE temp12a_rpt_life_cycle_all_users as\n" +
                    "select t1.uid\n" +
                    "  from temp7b_rpt_life_cycle_all_users t1 \n" +
                    "union \n" +
                    "select t2.uid\n" +
                    "  from temp7c_rpt_life_cycle_all_users t2 ;";
            /*开始比较：上上月有访问记录且本月和上月无访问记录*/
            String sqlDrop22 = "DROP TABLE IF EXISTS temp12_rpt_life_cycle_all_users;";
            String sql22 = "CREATE TABLE temp12_rpt_life_cycle_all_users as\n" +
                    "select t1.uid as uid\n" +
                    "  from temp7d_rpt_life_cycle_all_users t1 left join temp12a_rpt_life_cycle_all_users t2 on t1.uid = t2.uid\n" +
                    " where t2.uid is null ;";
            /*先合并上月和上上月有访问记录用户*/
            String sqlDrop23 = "DROP TABLE IF EXISTS temp13a_rpt_life_cycle_all_users;";
            String sql23 = "CREATE TABLE temp13a_rpt_life_cycle_all_users as\n" +
                    "select t1.uid\n" +
                    "  from temp7c_rpt_life_cycle_all_users t1 \n" +
                    "union\n" +
                    "select t2.uid\n" +
                    "  from temp7d_rpt_life_cycle_all_users t2 ;";
            /*开始比较：上月和上上月有访问记录且本月无访问记录*/
            String sqlDrop24 = "DROP TABLE IF EXISTS temp13_rpt_life_cycle_all_users;";
            String sql24 = "CREATE TABLE temp13_rpt_life_cycle_all_users as\n" +
                    "select t1.uid as uid\n" +
                    "  from temp13a_rpt_life_cycle_all_users t1 left join temp7b_rpt_life_cycle_all_users t2 on t1.uid = t2.uid\n" +
                    " where t2.uid is null ;";
            /*上周活跃用户*/
            String sqlDrop25 = "DROP TABLE IF EXISTS temp14a_rpt_life_cycle_all_users;";
            String sql25 = "CREATE TABLE temp14a_rpt_life_cycle_all_users as\n" +
                    "SELECT t.uid as uid /*用户id*/\n" +
                    "  from temp7a0_rpt_life_cycle_all_users t\n" +
                    "  where t.uid not in ('-1') and t.uid is not null\n" +
                    "    and t.pagestart_time >= UNIX_TIMESTAMP(date_sub(curdate(),interval 14 day))\n" +
                    "    and t.pagestart_time < UNIX_TIMESTAMP(date_sub(curdate(),interval 7 day))\n" +
                    "    and (t.pagestart_time is not null or t.pagestart_time <> 0)\n" +
                    "  GROUP BY t.uid;";
            /*近1周沉默客户*/
            String sqlDrop26 = "DROP TABLE IF EXISTS temp14_rpt_life_cycle_all_users;";
            String sql26 = "CREATE TABLE temp14_rpt_life_cycle_all_users as\n" +
                    "select t1.uid as uid\n" +
                    "  from temp14a_rpt_life_cycle_all_users t1 left join temp7a_rpt_life_cycle_all_users t2 on t1.uid = t2.uid\n" +
                    " where t2.uid is null ;";
            /****生成所有用户特征标示*******/
            String sqlDrop27 = "truncate table sl_rpt_life_cycle_all_users_flag;";
            String sqlResult = "insert into sl_rpt_life_cycle_all_users_flag\n" +
                    "select t0.day_no as day_no, /*统计时间：默认t-1天*/\n" +
                    "       t0.uid as uid, /*用户ID*/\n" +
                    "       (case when t1.uid is null then 0 else 1 end) as uid_consume_flag3, /*连续3月消费用户标示*/\n" +
                    "       (case when t2.uid is null then 0 else 1 end) as uid_consume_flag2, /*连续2月消费用户标示*/\n" +
                    "       (case when t3.uid is null then 0 else 1 end) as uid_consume_flag1, /*最近1月消费用户标示*/\n" +
                    "       (case when t4.uid is null then 0 else 1 end) as uid_consume_flag0, /*最近1周消费用户标示*/\n" +
                    "       (case when t5.total is null then 0 else t5.total end) as uid_consume_flag3a0, /*近3月消费金额*/\n" +
                    "       '-1' as uid_balance_flag3, /*连续3月余额大于0*/\n" +
                    "       '-1' as uid_balance_flag2, /*连续2月余额大于0*/\n" +
                    "       '-1' as uid_balance_flag1, /*近1月余额大于0*/\n" +
                    "       (case when t6.uid is null then 0 else 1 end) as uid_active_flag3, /*连续3月活跃客户标示*/\n" +
                    "       (case when t7.uid is null then 0 else 1 end) as uid_active_flag2, /*连续2月活跃客户标示*/\n" +
                    "       (case when t8.uid is null then 0 else 1 end) as uid_active_flag1, /*近1月活跃客户标示*/\n" +
                    "       (case when t9.uid is null then 0 else 1 end) as uid_active_flag0, /*近1周活跃客户标示*/\n" +
                    "       (case when t10.uid is null then 1 else 0 end) as uid_silent_flag3, /*近3月沉默客户标示*/\n" +
                    "       (case when t11.uid is null then 0 else 1 end) as uid_silent_flag2, /*近2月沉默客户标示*/\n" +
                    "       (case when t12.uid is null then 0 else 1 end) as uid_silent_flag1, /*近1月沉默客户标示*/\n" +
                    "       (case when t13.uid is null then 0 else 1 end) as uid_silent_flag0 /*近1周沉默客户标示*/\n" +
                    "  from temp1_rpt_life_cycle_all_users t0 left join temp2_rpt_life_cycle_all_users t1 on t0.uid = t1.uid\n" +
                    "                                         left join temp3_rpt_life_cycle_all_users t2 on t0.uid = t2.uid\n" +
                    "                                         left join temp4_rpt_life_cycle_all_users t3 on t0.uid = t3.uid\n" +
                    "                                         left join temp5_rpt_life_cycle_all_users t4 on t0.uid = t4.uid\n" +
                    "                                         left join temp6_rpt_life_cycle_all_users t5 on t0.uid = t5.uid\n" +
                    "                                         left join temp7_rpt_life_cycle_all_users t6 on t0.uid = t6.uid\n" +
                    "                                         left join temp8_rpt_life_cycle_all_users t7 on t0.uid = t7.uid\n" +
                    "                                         left join temp9_rpt_life_cycle_all_users t8 on t0.uid = t8.uid\n" +
                    "                                         left join temp10_rpt_life_cycle_all_users t9 on t0.uid = t9.uid\n" +
                    "                                         left join temp11_rpt_life_cycle_all_users t10 on t0.uid = t10.uid\n" +
                    "                                         left join temp12_rpt_life_cycle_all_users t11 on t0.uid = t11.uid\n" +
                    "                                         left join temp13_rpt_life_cycle_all_users t12 on t0.uid = t12.uid\n" +
                    "                                         left join temp14_rpt_life_cycle_all_users t13 on t0.uid = t13.uid ;";


            //删除临时表
            String drop1 = "DROP TABLE IF EXISTS temp1_rpt_life_cycle_all_users;";
            String drop2 = "DROP TABLE IF EXISTS temp2_rpt_life_cycle_all_users;";
            String drop2a = "DROP TABLE IF EXISTS temp2a_rpt_life_cycle_all_users;";
            String drop2b = "DROP TABLE IF EXISTS temp2b_rpt_life_cycle_all_users;";
            String drop2c = "DROP TABLE IF EXISTS temp2c_rpt_life_cycle_all_users;";
            String drop2d = "DROP TABLE IF EXISTS temp2d_rpt_life_cycle_all_users;";
            String drop3 = "DROP TABLE IF EXISTS temp3_rpt_life_cycle_all_users;";
            String drop4 = "DROP TABLE IF EXISTS temp4_rpt_life_cycle_all_users;";
            String drop5 = "DROP TABLE IF EXISTS temp5_rpt_life_cycle_all_users;";
            String drop6 = "DROP TABLE IF EXISTS temp6_rpt_life_cycle_all_users;";
            String drop7a0 = "DROP TABLE IF EXISTS temp7a0_rpt_life_cycle_all_users;";
            String drop7a = "DROP TABLE IF EXISTS temp7a_rpt_life_cycle_all_users;";
            String drop7b = "DROP TABLE IF EXISTS temp7b_rpt_life_cycle_all_users;";
            String drop7c = "DROP TABLE IF EXISTS temp7c_rpt_life_cycle_all_users;";
            String drop7d = "DROP TABLE IF EXISTS temp7d_rpt_life_cycle_all_users;";
            String drop7 = "DROP TABLE IF EXISTS temp7_rpt_life_cycle_all_users;";
            String drop8 = "DROP TABLE IF EXISTS temp8_rpt_life_cycle_all_users;";
            String drop9 = "DROP TABLE IF EXISTS temp9_rpt_life_cycle_all_users;";
            String drop10 = "DROP TABLE IF EXISTS temp10_rpt_life_cycle_all_users;";
            String drop11 = "DROP TABLE IF EXISTS temp11_rpt_life_cycle_all_users;";
            String drop12 = "DROP TABLE IF EXISTS temp12_rpt_life_cycle_all_users;";
            String drop12a = "DROP TABLE IF EXISTS temp12a_rpt_life_cycle_all_users;";
            String drop13 = "DROP TABLE IF EXISTS temp13_rpt_life_cycle_all_users;";
            String drop13a = "DROP TABLE IF EXISTS temp13a_rpt_life_cycle_all_users;";
            String drop14 = "DROP TABLE IF EXISTS temp14_rpt_life_cycle_all_users;";
            String drop14a = "DROP TABLE IF EXISTS temp14a_rpt_life_cycle_all_users;";

            try {
                //statement
                statement = connection.createStatement();
                rs= statement.executeQuery(sqlStatus);
                int table1_count=0;
                int table2_count=0;
                int table3_count=0;
                while (rs.next())
                {
                    if(rs.getString("table_name").equals("sl_ods_order")&&rs.getInt("status")==1){
                        table1_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_users")&&rs.getInt("status")==1){
                        table2_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_detail")&&rs.getInt("status")==1){
                        table3_count=1;
                        continue;
                    }
                }
                rsCount=table1_count+table2_count+table3_count;
                statement.execute(beginLog);
                if (rsCount==tableCount){
                    statement.execute(sqlDrop1);
                    statement.execute(sqlDrop2);
                    statement.execute(sqlDrop3);
                    statement.execute(sqlDrop4);
                    statement.execute(sqlDrop5);
                    statement.execute(sqlDrop6);
                    statement.execute(sqlDrop7);
                    statement.execute(sqlDrop8);
                    statement.execute(sqlDrop9);
                    statement.execute(sqlDrop10);
                    statement.execute(sqlDrop11);
                    statement.execute(sqlDrop12);
                    statement.execute(sqlDrop13);
                    statement.execute(sqlDrop14);
                    statement.execute(sqlDrop15);
                    statement.execute(sqlDrop16);
                    statement.execute(sqlDrop17);
                    statement.execute(sqlDrop18);
                    statement.execute(sqlDrop19);
                    statement.execute(sqlDrop20);
                    statement.execute(sqlDrop21);
                    statement.execute(sqlDrop22);
                    statement.execute(sqlDrop23);
                    statement.execute(sqlDrop24);
                    statement.execute(sqlDrop25);
                    statement.execute(sqlDrop26);
                    statement.execute(sqlDrop27);

                    connection.setAutoCommit(false);

                    statement.execute(sql1);
                    statement.execute(sql2);
                    statement.execute(sql3);
                    statement.execute(sql4);
                    statement.execute(sql5);
                    statement.execute(sql6);

                    statement.execute(sql7);
                    statement.execute(sql8);
                    statement.execute(sql9);
                    statement.execute(sql10);
                    statement.execute(sql11);
                    statement.execute(sql12);
                    statement.execute(sql13);
                    statement.execute(sql14);
                    statement.execute(sql15);
                    statement.execute(sql16);
                    statement.execute(sql17);
                    statement.execute(sql18);
                    statement.execute(sql19);
                    statement.execute(sql20);
                    statement.execute(sql21);
                    statement.execute(sql22);
                    statement.execute(sql23);
                    statement.execute(sql24);
                    statement.execute(sql25);
                    statement.execute(sql26);
                    statement.execute(sqlResult);

                    statement.execute(endLog);
                    connection.commit();

                    connection.setAutoCommit(true);
                    statement.execute(drop1);
                    statement.execute(drop2);
                    statement.execute(drop2c);
                    statement.execute(drop2d);
                    statement.execute(drop2a);
                    statement.execute(drop2b);
                    statement.execute(drop3);
                    statement.execute(drop4);
                    statement.execute(drop5);
                    statement.execute(drop6);
                    statement.execute(drop7);
                    statement.execute(drop7a0);
                    statement.execute(drop7a);
                    statement.execute(drop7b);
                    statement.execute(drop7c);
                    statement.execute(drop7d);
                    statement.execute(drop8);
                    statement.execute(drop9);
                    statement.execute(drop10);
                    statement.execute(drop11);
                    statement.execute(drop12);
                    statement.execute(drop12a);
                    statement.execute(drop13);
                    statement.execute(drop13a);
                    statement.execute(drop14);
                    statement.execute(drop14a);

                }else {
                    logger.error("日志表ods层关于表sl_ods_detail、sl_ods_order、sl_ods_users其中有异常（失败）情况" );
                }

            } catch (SQLException e) {
                logger.error("for sl_rpt_life_cycle_all_users_flag error:" + e.getMessage());
                try {
                    //rollback
                    connection.rollback();
                } catch (SQLException e1) {
                    logger.error("The sl_rpt_life_cycle_all_users_flag rollback error:" + e.getMessage());
                }
            } finally {
                try {
                    if (statement != null) statement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    logger.error("The sl_rpt_life_cycle_all_users_flag close connection error:" + e.getMessage());
                }
            }
        }
    };

    /**
     *订单支付行为：sl_rpt_analysis_order_flag
     *订单基础表 ：sl_rpt_analysis_order
     *功能描述 --%@COMMENT: 数据从ODS到RPT层
     *数据模块 --%@PARAM: RPT 集市域
     *执行周期 --%@PERIOD:按（天）
     *日志输入 --%@PARAM: SL_ODS_ETL_LOG : sl_rpt_analysis_order_flag,sl_rpt_analysis_order
     *日志输出 --%@PARAM: SL_ODS_ETL_LOG : sl_rpt_analysis_order_flag,sl_rpt_analysis_order
     *日志参数 --%@PARAM: 过程JAR执行结果代码(日志状态，1-完成 0-开始，默认插入后均写 0),输出参数
     *日志描述 --%@PARAM: 过程执行结果描述,写入参数
     *创建人 --%@CREATOR: 覃小龙
     *创建时间 --%@CREATED_TIME: 2015-07-21
     *来源表 --%@FROM: sl_ods_order --ODS订单表
     *来源表 --%@FROM: sl_ods_detail
     *来源表 --%@FROM: sl_ods_goods
     *来源表 --%@FROM: sl_ods_goodstype
     *目标表 --%@TO:   sl_rpt_analysis_order_flag --订单支付行为
     *目标表 --%@TO:   sl_rpt_analysis_order --订单基础表
     */

    public static TimerTask task8 = new TimerTask() {
        @Override
        public void run() {
            //使用或者下边的语句
            Connection connection = connect.getConnect();
            Statement statement = null;
            ResultSet rs=null;
            int rsCount=0;
            int tableCount=4;


            //向日志表sl_ods_etl_log插入sl_rpt_analysis_order表的更新记录
            String beginLog_oder= LogSqlUtil.getBeginLog("sl_rpt_analysis_order");
            String endLog_oder=LogSqlUtil.getEndLog("sl_rpt_analysis_order");

            //向日志表sl_ods_etl_log插入sl_rpt_analysis_order_flag表的更新记录
            String beginLog_flag= LogSqlUtil.getBeginLog("sl_rpt_analysis_order_flag");
            String endLog_flag=LogSqlUtil.getEndLog("sl_rpt_analysis_order_flag");


            //从sl_ods_etl_log日志表查询sl_ods_order、sl_ods_detail、sl_ods_goods、sl_ods_goodstype表相应日志记录状态
            String sqlStatus="SELECT table_name,status from  sl_ods_etl_log \n" +
                    "WHERE (lower(table_name)=lower('sl_ods_order') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n" +
                    "OR (lower(table_name)=lower('sl_ods_goods') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n" +
                    "OR (lower(table_name)=lower('sl_ods_goodstype') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d')) \n" +
                    "OR (lower(table_name)=lower('sl_ods_detail') AND DATE_FORMAT(end_time, '%Y%m%d')= DATE_FORMAT(ADDDATE(NOW(),0), '%Y%m%d'));";


           /*生成订单基础表*/

            String sqlDrop1 = "DROP TABLE IF EXISTS temp1_rpt_analysis_order;";
            String sql1 = "CREATE TABLE temp1_rpt_analysis_order as\n" +
                    "select \n" +
                    "  DATE_FORMAT(adddate(now(),-1), '%Y%m%d') as day_no, /*统计时间*/\n" +
                    "  province as province, /*省*/\n" +
                    "  city as city, /*市*/\n" +
                    "  district as town, /*区县*/\n" +
                    "  street as village, /*小区*/\n" +
                    "  t.address as address, /*订购地址*/\n" +
                    "  t.id as order_id, /*订单id*/\n" +
                    "  t.shopsid as shopsid, /*商店id号*/\n" +
                    "  t.uid as uid, /*用户ID*/\n" +
                    "  t.linkman as linkman, /*联系人*/\n" +
                    "  t.phone as phone, /*手机*/\n" +
                    "  t1.goodsid as goodsid, /*商品id*/\n" +
                    "  t1.name as goods_name, /*商品名称*/\n" +
                    "  0 as brandid, /*品牌id*/\n" +
                    "  0 as brand_name, /*品牌*/\n" +
                    "  t.state as state, /*订单状态*/\n" +
                    "  t.distri as distri, /*配送方式*/\n" +
                    "  t.payment as payment, /*支付方式*/\n" +
                    "  t.paystate as paystate, /*付款状态*/\n" +
                    "  t.astocktime as astocktime, /*预计备货时间*/\n" +
                    "  t.sstocktime as sstocktime, /*开始备货时间*/\n" +
                    "  t.bstocktime as bstocktime, /*备货完成时间*/\n" +
                    "  t.aservicetime as aservicetime, /*预计送达时间*/\n" +
                    "  t.sservicetime as sservicetime, /*开始派送时间*/\n" +
                    "  t.bservicetime as bservicetime, /*派送完成时间*/\n" +
                    "  t.receive_time as receive_time, /*商户接单时间*/\n" +
                    "  t.pay_overtime as pay_overtime, /*电子支付支付完成时间*/\n" +
                    "  t.addtime as order_addtime, /*购买时间（下单时间）*/\n" +
                    "  t.suretime as suretime, /*确认时间*/\n" +
                    "  t.gettime as gettime, /*收货时间*/\n" +
                    "  t.overtime as overtime, /*完成时间*/\n" +
                    "  t.revoketime as revoketime, /*撤销时间*/\n" +
                    "  t.revoke_type as revoke_type, /*撤销类型*/\n" +
                    "  t.revoke_reason as revoke_reason, /*撤销原因*/\n" +
                    "  t.longitude as longitude, /*收获地址经度*/\n" +
                    "  t.latitude as latitude, /*收获地址维度*/\n" +
                    "  round(sum(case when t1.price is null then 0 else t1.price end),2) as total, /*总金额*/\n" +
                    "  round(sum(case when t1.price is null then 0 else t1.price end),2) as goods_total_price, /*商品总价格*/\n" +
                    "  round(sum(case when t.freight_total_price is null then 0 else t.freight_total_price end),2) as freight_total_price, /*运费总价格*/\n" +
                    "  round(sum(case when t.coupon_total_price is null then 0 else t.coupon_total_price end),2) as coupon_total_price /*代金券总价格*/\n" +
                    "from sl_ods_order t join sl_ods_detail t1 on t.id = t1.orderid\n" +
                    " where t.uid is not null\n" +
                    "   and (t.addtime is not null or t.addtime <> 0)\n" +
                    "group by t.address, t.id, t.shopsid, t.uid, t.linkman, t.phone, t1.goodsid, t1.name, \n" +
                    "         t.state, t.distri, t.payment, t.paystate, t.astocktime, t.sstocktime, t.bstocktime,\n" +
                    "         t.aservicetime, t.sservicetime, t.bservicetime, t.receive_time, t.pay_overtime, t.addtime,\n" +
                    "         t.suretime, t.gettime, t.overtime, t.revoketime, t.revoke_type, t.revoke_reason, t.longitude, t.latitude ;";
//            "   and t.addtime >= UNIX_TIMESTAMP(date_sub(curdate(),interval 91 day))\n" +
//                    "   and t.addtime < UNIX_TIMESTAMP(CURDATE())\n" +
            /*扩展出商品类型*/
            String sqlDrop2 = "DROP TABLE IF EXISTS temp2_rpt_analysis_order;";
            String sql2 = "CREATE TABLE temp2_rpt_analysis_order as\n" +
                    "select t.day_no,\n" +
                    "       t.province,\n" +
                    "       t.city,\n" +
                    "       t.town,\n" +
                    "       t.village,\n" +
                    "       t.address,\n" +
                    "       t.order_id,\n" +
                    "       t.shopsid,\n" +
                    "       t.uid,\n" +
                    "       t.linkman,\n" +
                    "       t.phone,\n" +
                    "       t.goodsid,\n" +
                    "       t.goods_name,\n" +
                    "       (case when t2.typeid is null then -1 else t2.typeid end) as typeid, /*商品类别id*/\n" +
                    "       t.brandid,\n" +
                    "       t.brand_name,\n" +
                    "       t.state,\n" +
                    "       t.distri,\n" +
                    "       t.payment,\n" +
                    "       t.paystate,\n" +
                    "       t.astocktime,\n" +
                    "       t.sstocktime,\n" +
                    "       t.bstocktime,\n" +
                    "       t.aservicetime,\n" +
                    "       t.sservicetime,\n" +
                    "       t.bservicetime,\n" +
                    "       t.receive_time,\n" +
                    "       t.pay_overtime,\n" +
                    "       t.order_addtime,\n" +
                    "       t.suretime,\n" +
                    "       t.gettime,\n" +
                    "       t.overtime,\n" +
                    "       t.revoketime,\n" +
                    "       t.revoke_type,\n" +
                    "       t.revoke_reason,\n" +
                    "       t.longitude,\n" +
                    "       t.latitude,\n" +
                    "       t.total,\n" +
                    "       t.goods_total_price,\n" +
                    "       t.freight_total_price,\n" +
                    "       t.coupon_total_price\n" +
                    "  from temp1_rpt_analysis_order t left join sl_ods_goods t2 on t.goodsid = t2.id ;";
            /*扩展商品类别名称，生成基础清单*/
            String sqlDrop3 = "truncate table sl_rpt_analysis_order;";
            String sql3 = "insert into sl_rpt_analysis_order\n" +
                    "select t.day_no,\n" +
                    "  t.province,\n" +
                    "  t.city,\n" +
                    "  t.town,\n" +
                    "  t.village,\n" +
                    "  t.address,\n" +
                    "  t.order_id,\n" +
                    "  t.shopsid,\n" +
                    "  (case when t1.shopsname is null then '未知店铺' else t1.shopsname end) as shopsname, /*店铺名称*/\n" +
                    "  t.uid,\n" +
                    "  t.linkman,\n" +
                    "  t.phone,\n" +
                    "  t.goodsid,\n" +
                    "  t.goods_name,\n" +
                    "  t.typeid,\n" +
                    "  (case when t3.name is null then '未知类别' else t3.name end) as type_name, /*类别名称*/\n" +
                    "  t.brandid,\n" +
                    "  t.brand_name,\n" +
                    "  t.state,\n" +
                    "  t.distri,\n" +
                    "  t.payment,\n" +
                    "  t.paystate,\n" +
                    "  t.astocktime,\n" +
                    "  t.sstocktime,\n" +
                    "  t.bstocktime,\n" +
                    "  t.aservicetime,\n" +
                    "  t.sservicetime,\n" +
                    "  t.bservicetime,\n" +
                    "  t.receive_time,\n" +
                    "  t.pay_overtime,\n" +
                    "  t.order_addtime,\n" +
                    "  t.suretime,\n" +
                    "  t.gettime,\n" +
                    "  t.overtime,\n" +
                    "  t.revoketime,\n" +
                    "  t.revoke_type,\n" +
                    "  t.revoke_reason,\n" +
                    "  t.longitude,\n" +
                    "  t.latitude,\n" +
                    "  t.total,\n" +
                    "  t.goods_total_price,\n" +
                    "  t.freight_total_price,\n" +
                    "  t.coupon_total_price\n" +
                    "  from temp2_rpt_analysis_order t left join sl_ods_goodstype t3 on t.typeid = t3.id\n" +
                    "                                  left join sl_ods_shops t1 on t.shopsid = t1.id ;";
            /*生成订单支付行为标示表*/

              /*生成基础标示表*/
            String sqlDrop4 = "DROP TABLE IF EXISTS temp0_rpt_analysis_order_flag;";
            String sql4 = "CREATE TABLE temp0_rpt_analysis_order_flag as\n" +
                    "select \n" +
                    "  t.day_no,\n" +
                    "  t.province,\n" +
                    "  t.city,\n" +
                    "  t.town,\n" +
                    "  t.village,\n" +
                    "  t.address,\n" +
                    "  t.uid,\n" +
                    "  t.order_id,\n" +
                    "  t.shopsid,\n" +
                    "  t.order_addtime,\n" +
                    "  (case when (t.pay_overtime is null or t.pay_overtime = 0 ) then 0\n" +
                    "        when (TIMESTAMPDIFF(SECOND,FROM_UNIXTIME(t.order_addtime, '%Y-%m-%d %H:%i:%S'), FROM_UNIXTIME(t.pay_overtime, '%Y-%m-%d %H:%i:%S')) >=0\n" +
                    "             and TIMESTAMPDIFF(SECOND,FROM_UNIXTIME(t.order_addtime, '%Y-%m-%d %H:%i:%S'), FROM_UNIXTIME(t.pay_overtime, '%Y-%m-%d %H:%i:%S')) < 10800) then 1\n" +
                    "       else 0\n" +
                    "  end) as uid_order_flag1,/*生成订单0-3小时支付标示 1-是 0-否*/\n" +
                    "  (case when (t.pay_overtime is null or t.pay_overtime = 0 ) then 0\n" +
                    "        when (TIMESTAMPDIFF(SECOND,FROM_UNIXTIME(t.order_addtime, '%Y-%m-%d %H:%i:%S'), FROM_UNIXTIME(t.pay_overtime, '%Y-%m-%d %H:%i:%S')) >=10800\n" +
                    "             and TIMESTAMPDIFF(SECOND,FROM_UNIXTIME(t.order_addtime, '%Y-%m-%d %H:%i:%S'), FROM_UNIXTIME(t.pay_overtime, '%Y-%m-%d %H:%i:%S')) < 28800) then 1\n" +
                    "        else 0\n" +
                    "  end) as uid_order_flag2,/*生成订单3-8小时支付标示 1-是 0-否*/\n" +
                    "  (case when (t.pay_overtime is null or t.pay_overtime = 0 ) then 0\n" +
                    "        when (TIMESTAMPDIFF(SECOND,FROM_UNIXTIME(t.order_addtime, '%Y-%m-%d %H:%i:%S'), FROM_UNIXTIME(t.pay_overtime, '%Y-%m-%d %H:%i:%S')) >=28800\n" +
                    "             and TIMESTAMPDIFF(SECOND,FROM_UNIXTIME(t.order_addtime, '%Y-%m-%d %H:%i:%S'), FROM_UNIXTIME(t.pay_overtime, '%Y-%m-%d %H:%i:%S')) < 43200) then 1\n" +
                    "        else 0\n" +
                    "  end) as uid_order_flag3,/*生成订单8-12小时支付标示 1-是 0-否*/\n" +
                    "  (case when (t.pay_overtime is null or t.pay_overtime = 0 ) then 0\n" +
                    "        when (TIMESTAMPDIFF(SECOND,FROM_UNIXTIME(t.order_addtime, '%Y-%m-%d %H:%i:%S'), FROM_UNIXTIME(t.pay_overtime, '%Y-%m-%d %H:%i:%S')) >=43200\n" +
                    "             and TIMESTAMPDIFF(SECOND,FROM_UNIXTIME(t.order_addtime, '%Y-%m-%d %H:%i:%S'), FROM_UNIXTIME(t.pay_overtime, '%Y-%m-%d %H:%i:%S')) < 86400) then 1\n" +
                    "        else 0\n" +
                    "  end) as uid_order_flag4, /*生成订单12-24小时支付标示 1-是 0-否*/\n" +
                    "  (case when (t.pay_overtime is null or t.pay_overtime = 0 ) then 0\n" +
                    "        when (TIMESTAMPDIFF(SECOND,FROM_UNIXTIME(t.order_addtime, '%Y-%m-%d %H:%i:%S'), FROM_UNIXTIME(t.pay_overtime, '%Y-%m-%d %H:%i:%S')) >=0\n" +
                    "             and TIMESTAMPDIFF(SECOND,FROM_UNIXTIME(t.order_addtime, '%Y-%m-%d %H:%i:%S'), FROM_UNIXTIME(t.pay_overtime, '%Y-%m-%d %H:%i:%S')) < 172800) then 1\n" +
                    "        else 0\n" +
                    "  end) as uid_order_flag5, /*生成订单后48小时内支付(1-2天内支付)支付标示 1-是 0-否*/\n" +
                    "  (case when (t.revoketime is null or t.revoketime = 0 ) then 0\n" +
                    "        when (TIMESTAMPDIFF(SECOND,FROM_UNIXTIME(t.order_addtime, '%Y-%m-%d %H:%i:%S'), FROM_UNIXTIME(t.revoketime, '%Y-%m-%d %H:%i:%S')) >=0\n" +
                    "             and TIMESTAMPDIFF(SECOND,FROM_UNIXTIME(t.order_addtime, '%Y-%m-%d %H:%i:%S'), FROM_UNIXTIME(t.revoketime, '%Y-%m-%d %H:%i:%S')) < 172800) then 1\n" +
                    "        else 0\n" +
                    "  end) as uid_order_flag6 /*生成订单后取消订单(1-2天内)标示 1-是 0-否*/\n" +
                    "from sl_rpt_analysis_order t group BY\n" +
                    "  t.day_no,\n" +
                    "  t.province,\n" +
                    "  t.city,\n" +
                    "  t.town,\n" +
                    "  t.village,\n" +
                    "  t.address,\n" +
                    "  t.uid,\n" +
                    "  t.order_id,\n" +
                    "  t.shopsid,\n" +
                    "  t.order_addtime;";
            /*生成订单7天内已支付订单*/
            String sqlDrop5 ="DROP TABLE IF EXISTS temp1_rpt_analysis_order_flag;";
            String sql5="CREATE TABLE temp1_rpt_analysis_order_flag as\n" +
                    "select t.order_id\n" +
                    "  from sl_rpt_analysis_order t\n" +
                    " where TIMESTAMPDIFF(SECOND,FROM_UNIXTIME(t.order_addtime, '%Y-%m-%d %H:%i:%S'), FROM_UNIXTIME(t.pay_overtime, '%Y-%m-%d %H:%i:%S')) >=0\n" +
                    "   and TIMESTAMPDIFF(SECOND,FROM_UNIXTIME(t.order_addtime, '%Y-%m-%d %H:%i:%S'), FROM_UNIXTIME(t.pay_overtime, '%Y-%m-%d %H:%i:%S')) < 604800\n" +
                    "   and (t.pay_overtime is not null or t.pay_overtime <> 0)\n" +
                    "group by t.order_id ;";
            /*生成订单15天内已支付订单*/
            String sqlDrop6 ="DROP TABLE IF EXISTS temp2_rpt_analysis_order_flag;";
            String sql6="CREATE TABLE temp2_rpt_analysis_order_flag as\n" +
                    "select t.order_id\n" +
                    "  from sl_rpt_analysis_order t\n" +
                    " where TIMESTAMPDIFF(SECOND,FROM_UNIXTIME(t.order_addtime, '%Y-%m-%d %H:%i:%S'), FROM_UNIXTIME(t.pay_overtime, '%Y-%m-%d %H:%i:%S')) >=0\n" +
                    "   and TIMESTAMPDIFF(SECOND,FROM_UNIXTIME(t.order_addtime, '%Y-%m-%d %H:%i:%S'), FROM_UNIXTIME(t.pay_overtime, '%Y-%m-%d %H:%i:%S')) < 1296000\n" +
                    "   and (t.pay_overtime is not null or t.pay_overtime <> 0)\n" +
                    "group by t.order_id ;";
            /*生成订单30天内已支付订单*/
            String sqlDrop7 ="DROP TABLE IF EXISTS temp3_rpt_analysis_order_flag;";
            String sql7="CREATE TABLE temp3_rpt_analysis_order_flag as\n" +
                    "select t.order_id\n" +
                    "  from sl_rpt_analysis_order t\n" +
                    " where TIMESTAMPDIFF(SECOND,FROM_UNIXTIME(t.order_addtime, '%Y-%m-%d %H:%i:%S'), FROM_UNIXTIME(t.pay_overtime, '%Y-%m-%d %H:%i:%S')) >=0\n" +
                    "   and TIMESTAMPDIFF(SECOND,FROM_UNIXTIME(t.order_addtime, '%Y-%m-%d %H:%i:%S'), FROM_UNIXTIME(t.pay_overtime, '%Y-%m-%d %H:%i:%S')) < 2592000\n" +
                    "   and (t.pay_overtime is not null or t.pay_overtime <> 0)\n" +
                    "group by t.order_id ;";
            /*汇总标示表*/
            String sqlDrop8 ="truncate table sl_rpt_analysis_order_flag;";
            String sql8="insert into sl_rpt_analysis_order_flag\n" +
                    "select \n" +
                    "  t0.day_no,\n" +
                    "  t0.province,\n" +
                    "  t0.city,\n" +
                    "  t0.town,\n" +
                    "  t0.village,\n" +
                    "  t0.address,\n" +
                    "  t0.uid,\n" +
                    "  t0.order_id,\n" +
                    "  t0.shopsid,\n" +
                    "  t0.order_addtime,\n" +
                    "  t0.uid_order_flag1,\n" +
                    "  t0.uid_order_flag2,\n" +
                    "  t0.uid_order_flag3,\n" +
                    "  t0.uid_order_flag4,\n" +
                    "  t0.uid_order_flag5,\n" +
                    "  t0.uid_order_flag6,\n" +
                    "  (case when t1.order_id is null then 1 else 0 end) as uid_order_flag7, /*生成订单7天后无支付标示 1-是 0-否*/\n" +
                    "  (case when t2.order_id is null then 1 else 0 end) as uid_order_flag8, /*生成订单15天后无支付标示 1-是 0-否*/\n" +
                    "  (case when t3.order_id is null then 1 else 0 end) as uid_order_flag9 /*生成订单30天后无支付标示 1-是 0-否*/\n" +
                    "  from temp0_rpt_analysis_order_flag t0 left join temp1_rpt_analysis_order_flag t1 on t0.order_id = t1.order_id\n" +
                    "                                        left join temp2_rpt_analysis_order_flag t2 on t0.order_id = t2.order_id\n" +
                    "                                        left join temp3_rpt_analysis_order_flag t3 on t0.order_id = t3.order_id ;";


            //删除临时表
            String drop1 = "DROP TABLE IF EXISTS temp0_rpt_analysis_order_flag;";
            String drop2 = "DROP TABLE IF EXISTS temp1_rpt_analysis_order_flag;";
            String drop3 = "DROP TABLE IF EXISTS temp2_rpt_analysis_order_flag;";
            String drop4 = "DROP TABLE IF EXISTS temp3_rpt_analysis_order_flag;";
            String drop5 = "DROP TABLE IF EXISTS temp1_rpt_analysis_order;";
            String drop6 = "DROP TABLE IF EXISTS temp2_rpt_analysis_order;";

            try {
                //statement
                statement = connection.createStatement();
                rs= statement.executeQuery(sqlStatus);
                int table1_count=0;
                int table2_count=0;
                int table3_count=0;
                int table4_count=0;
                while (rs.next())
                {
                    if(rs.getString("table_name").equals("sl_ods_goods")&&rs.getInt("status")==1){
                        table1_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_order")&&rs.getInt("status")==1){
                        table2_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_detail")&&rs.getInt("status")==1){
                        table3_count=1;
                        continue;
                    }
                    if(rs.getString("table_name").equals("sl_ods_goodstype")&&rs.getInt("status")==1){
                        table4_count=1;
                        continue;
                    }

                }
                rsCount=table1_count+table2_count+table3_count+table4_count;
                statement.execute(beginLog_oder);
                if (rsCount==tableCount){
                    statement.execute(sqlDrop1);
                    statement.execute(sqlDrop2);
                    statement.execute(sqlDrop3);
                    statement.execute(sqlDrop4);
                    statement.execute(sqlDrop5);
                    statement.execute(sqlDrop6);
                    statement.execute(sqlDrop7);
                    statement.execute(sqlDrop8);

                    connection.setAutoCommit(false);
                    statement.execute(beginLog_flag);
                    statement.execute(sql1);
                    statement.execute(sql2);
                    statement.execute(sql3);
                    statement.execute(sql4);
                    statement.execute(sql5);
                    statement.execute(sql6);
                    statement.execute(sql7);
                    statement.execute(sql8);
                    statement.execute(endLog_oder);
                    statement.execute(endLog_flag);
                    connection.commit();
                    connection.setAutoCommit(true);

                    statement.execute(drop1);
                    statement.execute(drop2);
                    statement.execute(drop3);
                    statement.execute(drop4);
                    statement.execute(drop5);
                    statement.execute(drop6);
                }else {
                    logger.error("日志表ods层关于表sl_ods_order、sl_ods_detail、sl_ods_goods、sl_ods_goodstype其中有异常（失败）情况" );
                }

            } catch (SQLException e) {
                logger.error("for sl_rpt_analysis_order_flag，sl_rpt_analysis_order error:" + e.getMessage());
                try {
                    //rollback
                    connection.rollback();
                } catch (SQLException e1) {
                    logger.error("The sl_rpt_analysis_order_flag，sl_rpt_analysis_order rollback error:" + e.getMessage());
                }
            } finally {
                try {
                    if (statement != null) statement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    logger.error("The sl_rpt_analysis_order_flag，sl_rpt_analysis_order close connection error:" + e.getMessage());
                }
            }
        }
    };


    /**
     *购物车行为：sl_rpt_analysis_shoppingcart_flag
     *购物车行为基础订单 ：sl_rpt_analysis_shoppingcart_order
     *功能描述 --%@COMMENT: 数据从ODS到RPT层
     *数据模块 --%@PARAM: RPT 集市域
     *执行周期 --%@PERIOD:按（天）
     *日志输入 --%@PARAM: SL_ODS_ETL_LOG : sl_rpt_analysis_order_flag,sl_rpt_analysis_order
     *日志输出 --%@PARAM: SL_ODS_ETL_LOG : sl_rpt_analysis_order_flag,sl_rpt_analysis_order
     *日志参数 --%@PARAM: 过程JAR执行结果代码(日志状态，1-完成 0-开始，默认插入后均写 0),输出参数
     *日志描述 --%@PARAM: 过程执行结果描述,写入参数
     *创建人 --%@CREATOR: 覃小龙
     *创建时间 --%@CREATED_TIME: 2015-07-21
     *来源表 --%@FROM: sl_ods_order --ODS订单表
     *来源表 --%@FROM: sl_ods_detail
     *来源表 --%@FROM: sl_ods_goods
     *来源表 --%@FROM: sl_ods_goodstype
     *目标表 --%@TO:   sl_rpt_analysis_shoppingcart_flag --订单支付行为
     *目标表 --%@TO:   sl_rpt_analysis_shoppingcart_order --订单基础表
     */

    public static TimerTask task9 = new TimerTask() {
        @Override
        public void run() {
            //使用或者下边的语句
            Connection connection = connect.getConnect();
            Statement statement = null;

            //向日志表sl_ods_etl_log插入sl_rpt_analysis_shoppingcart_order表的更新记录
            String beginLog_oder= LogSqlUtil.getBeginLog("sl_rpt_analysis_shoppingcart_order");
            String endLog_oder=LogSqlUtil.getEndLog("sl_rpt_analysis_shoppingcart_order");

            //向日志表sl_ods_etl_log插入sl_rpt_analysis_shoppingcart_flag表的更新记录
            String beginLog_flag= LogSqlUtil.getBeginLog("sl_rpt_analysis_shoppingcart_flag");
            String endLog_flag=LogSqlUtil.getEndLog("sl_rpt_analysis_shoppingcart_flag");

           /*生成订单基础表*/

            String sqlDrop1 = "DROP TABLE IF EXISTS temp1_sl_rpt_analysis_shoppingcart;";
            String sql1 = "CREATE TABLE temp1_sl_rpt_analysis_shoppingcart as\n" +
                    "select \n" +
                    "  DATE_FORMAT(adddate(now(),-1), '%Y%m%d') as day_no, /*统计时间*/\n" +
                    "  t.province as province, /*省*/\n" +
                    "  t.city as city, /*市*/\n" +
                    "  t.district as town, /*区县*/\n" +
                    "  t.street as village, /*小区*/\n" +
                    "  t.address as address, /*订购地址*/\n" +
                    "  t.id as order_id, /*订单id*/\n" +
                    "  t.shopsid as shopsid, /*商店id号*/\n" +
                    "  t.uid as uid, /*用户ID*/\n" +
                    "  t.linkman as linkman, /*联系人*/\n" +
                    "  t.phone as phone, /*手机*/\n" +
                    "  t1.goodsid as goodsid, /*商品id*/\n" +
                    "  t1.name as goods_name, /*商品名称*/\n" +
                    "  0 as brandid, /*品牌id*/\n" +
                    "  0 as brand_name, /*品牌*/\n" +
                    "  t.state as state, /*订单状态*/\n" +
                    "  t.distri as distri, /*配送方式*/\n" +
                    "  t.payment as payment, /*支付方式*/\n" +
                    "  t.paystate as paystate, /*付款状态*/\n" +
                    "  t.astocktime as astocktime, /*预计备货时间*/\n" +
                    "  t.sstocktime as sstocktime, /*开始备货时间*/\n" +
                    "  t.bstocktime as bstocktime, /*备货完成时间*/\n" +
                    "  t.aservicetime as aservicetime, /*预计送达时间*/\n" +
                    "  t.sservicetime as sservicetime, /*开始派送时间*/\n" +
                    "  t.bservicetime as bservicetime, /*派送完成时间*/\n" +
                    "  t.receive_time as receive_time, /*商户接单时间*/\n" +
                    "  t.pay_overtime as pay_overtime, /*电子支付支付完成时间*/\n" +
                    "  t.addtime as order_addtime, /*购买时间（下单时间）*/\n" +
                    "  t.suretime as suretime, /*确认时间*/\n" +
                    "  t.gettime as gettime, /*收货时间*/\n" +
                    "  t.overtime as overtime, /*完成时间*/\n" +
                    "  t.revoketime as revoketime, /*撤销时间*/\n" +
                    "  t.revoke_type as revoke_type, /*撤销类型*/\n" +
                    "  t.revoke_reason as revoke_reason, /*撤销原因*/\n" +
                    "  t.longitude as longitude, /*收获地址经度*/\n" +
                    "  t.latitude as latitude, /*收获地址维度*/\n" +
                    "  round(sum(case when t1.price is null then 0 else t1.price end),2) as total, /*总金额*/\n" +
                    "  round(sum(case when t1.price is null then 0 else t1.price end),2) as goods_total_price, /*商品总价格*/\n" +
                    "  round(sum(case when t.freight_total_price is null then 0 else t.freight_total_price end),2) as freight_total_price, /*运费总价格*/\n" +
                    "  round(sum(case when t.coupon_total_price is null then 0 else t.coupon_total_price end),2) as coupon_total_price /*代金券总价格*/\n" +
                    "from sl_ods_order t join sl_ods_detail t1 on t.id = t1.orderid\n" +
                    " where t.uid is not null\n" +
                    "   and (t.addtime is not null or t.addtime <> 0)\n" +
                    "group by t.address, t.id, t.shopsid, t.uid, t.linkman, t.phone, t1.goodsid, t1.name, \n" +
                    "         t.state, t.distri, t.payment, t.paystate, t.astocktime, t.sstocktime, t.bstocktime,\n" +
                    "         t.aservicetime, t.sservicetime, t.bservicetime, t.receive_time, t.pay_overtime, t.addtime,\n" +
                    "         t.suretime, t.gettime, t.overtime, t.revoketime, t.revoke_type, t.revoke_reason, t.longitude, t.latitude ;";
//                    "   and t.addtime >= UNIX_TIMESTAMP(date_sub(curdate(),interval 186 day))\n" +
//                    "   and t.addtime < UNIX_TIMESTAMP(CURDATE())\n" +
            /*扩展出商品类型*/
            String sqlDrop2 = "DROP TABLE IF EXISTS temp2_sl_rpt_analysis_shoppingcart;";
            String sql2 = "CREATE TABLE temp2_sl_rpt_analysis_shoppingcart as\n" +
                    "select t.day_no,\n" +
                    "       t.province,\n" +
                    "       t.city,\n" +
                    "       t.town,\n" +
                    "       t.village,\n" +
                    "       t.address,\n" +
                    "       t.order_id,\n" +
                    "       t.shopsid,\n" +
                    "       t.uid,\n" +
                    "       t.linkman,\n" +
                    "       t.phone,\n" +
                    "       t.goodsid,\n" +
                    "       t.goods_name,\n" +
                    "       (case when t2.typeid is null then -1 else t2.typeid end) as typeid, /*商品类别id*/\n" +
                    "       t.brandid,\n" +
                    "       t.brand_name,\n" +
                    "       t.state,\n" +
                    "       t.distri,\n" +
                    "       t.payment,\n" +
                    "       t.paystate,\n" +
                    "       t.astocktime,\n" +
                    "       t.sstocktime,\n" +
                    "       t.bstocktime,\n" +
                    "       t.aservicetime,\n" +
                    "       t.sservicetime,\n" +
                    "       t.bservicetime,\n" +
                    "       t.receive_time,\n" +
                    "       t.pay_overtime,\n" +
                    "       t.order_addtime,\n" +
                    "       t.suretime,\n" +
                    "       t.gettime,\n" +
                    "       t.overtime,\n" +
                    "       t.revoketime,\n" +
                    "       t.revoke_type,\n" +
                    "       t.revoke_reason,\n" +
                    "       t.longitude,\n" +
                    "       t.latitude,\n" +
                    "       t.total,\n" +
                    "       t.goods_total_price,\n" +
                    "       t.freight_total_price,\n" +
                    "       t.coupon_total_price\n" +
                    "  from temp1_sl_rpt_analysis_shoppingcart t left join sl_ods_goods t2 on t.goodsid = t2.id ;";
            /*扩展商品类别名称，店铺名称，生成基础清单*/
            String sqlDrop3 = "truncate table sl_rpt_analysis_shoppingcart_order;";
            String sql3 = "insert into sl_rpt_analysis_shoppingcart_order\n" +
                    "select t.day_no,\n" +
                    "  t.province,\n" +
                    "  t.city,\n" +
                    "  t.town,\n" +
                    "  t.village,\n" +
                    "  t.address,\n" +
                    "  t.order_id,\n" +
                    "  t.shopsid,\n" +
                    "  (case when t1.shopsname is null then '未知店铺' else t1.shopsname end) as shopsname, /*店铺名称*/\n" +
                    "  t.uid,\n" +
                    "  t.linkman,\n" +
                    "  t.phone,\n" +
                    "  t.goodsid,\n" +
                    "  t.goods_name,\n" +
                    "  t.typeid,\n" +
                    "  (case when t3.name is null then '未知类别' else t3.name end) as type_name, /*类别名称*/\n" +
                    "  t.brandid,\n" +
                    "  t.brand_name,\n" +
                    "  t.state,\n" +
                    "  t.distri,\n" +
                    "  t.payment,\n" +
                    "  t.paystate,\n" +
                    "  t.astocktime,\n" +
                    "  t.sstocktime,\n" +
                    "  t.bstocktime,\n" +
                    "  t.aservicetime,\n" +
                    "  t.sservicetime,\n" +
                    "  t.bservicetime,\n" +
                    "  t.receive_time,\n" +
                    "  t.pay_overtime,\n" +
                    "  t.order_addtime,\n" +
                    "  t.suretime,\n" +
                    "  t.gettime,\n" +
                    "  t.overtime,\n" +
                    "  t.revoketime,\n" +
                    "  t.revoke_type,\n" +
                    "  t.revoke_reason,\n" +
                    "  t.longitude,\n" +
                    "  t.latitude,\n" +
                    "  t.total,\n" +
                    "  t.goods_total_price,\n" +
                    "  t.freight_total_price,\n" +
                    "  t.coupon_total_price\n" +
                    "  from temp2_sl_rpt_analysis_shoppingcart t left join sl_ods_goodstype t3 on t.typeid = t3.id\n" +
                    "                                            left join sl_ods_shops t1 on t.shopsid = t1.id ;";

              /*生成购物车基础表*/
            String sqlDrop4 = "DROP TABLE IF EXISTS temp3_sl_rpt_analysis_shoppingcart;";
            String sql4 = "CREATE TABLE temp3_sl_rpt_analysis_shoppingcart as\n" +
                    "select \n" +
                    "  DATE_FORMAT(adddate(now(),-1), '%Y%m%d') as day_no, /*统计时间*/\n" +
                    "  t.province as province, /*省*/\n" +
                    "  t.city as city, /*市*/\n" +
                    "  t.district as town, /*区县*/\n" +
                    "  t.street as village, /*小区*/\n" +
                    "  t.address as address, /*订购地址*/\n" +
                    "  t.uid as uid, /*用户ID*/\n" +
                    "  t.goodsid as goodsid, /*商品id*/\n" +
                    "  (case when t1.goods is null then '未知商品' else t1.goods end) as goods, /*商品名称*/\n" +
                    "  t.shopsid as shopsid, /*店铺id*/\n" +
                    "  (case when t2.shopsname is null then '未知店铺' else t2.shopsname end) as shopsname, /*店铺名称*/\n" +
                    "  t.num as num, /*购买数量*/\n" +
                    "  t.is_special as is_special, /*是否是特价商品 1：是 ，2：否*/\n" +
                    "  t.special_num as special_num, /*特价商品限量 为0表示不限制*/\n" +
                    "  t.my_special_num as my_special_num, /*我能购买的特价数量*/\n" +
                    "  t.exist as exist, /*购物车存在状态: 1存在2不存在*/\n" +
                    "  t.is_over as is_over, /*是否完成订单1、完成 2、未完成*/\n" +
                    "  t.orderid as orderid, /*订单id*/\n" +
                    "  t.addtime as shoppingcart_addtime, /*购物车添加时间（存：商品具体加入购物车时间）*/\n" +
                    "  t.update_time as update_time, /*最后更新时间*/\n" +
                    "  t.delete_time as delete_time, /*删除时间*/\n" +
                    "  t.over_time as over_time, /*订单生成时间(完成时间)*/\n" +
                    "  (case when t2.freight_price is null then 0 else t2.freight_price end) as goodsid_freight_price /*商品加入购物车的运费金额,备注：关联不上的店铺默认运费为0*/\n" +
                    "\n" +
                    "from sl_ods_shopping_cart t left join sl_ods_goods t1 on t.goodsid = t1.id\n" +
                    "                            left join sl_ods_shops t2 on t.shopsid = t2.id\n" +
                    "where t.uid is not null\n" +
                    "  and (t.addtime is not null or t.addtime <> 0) ;";
//            "  and t.addtime >= UNIX_TIMESTAMP(date_sub(curdate(),interval 91 day))\n" +
//            "  and t.addtime < UNIX_TIMESTAMP(CURDATE())\n" +

            /*【生成加入购物车7天内有订单生成的用户】和【加入购物车7天外有订单生成用户】*/
            String sqlDrop5 ="DROP TABLE IF EXISTS temp4_sl_rpt_analysis_shoppingcart;";
            String sql5="CREATE TABLE temp4_sl_rpt_analysis_shoppingcart as\n" +
                    "select t.uid as uid /*用户ID*/\n" +
                    "  from temp3_sl_rpt_analysis_shoppingcart t\n" +
                    " where TIMESTAMPDIFF(SECOND,FROM_UNIXTIME(t.shoppingcart_addtime, '%Y-%m-%d %H:%i:%S'), FROM_UNIXTIME(t.over_time, '%Y-%m-%d %H:%i:%S')) >=0\n" +
                    "   and TIMESTAMPDIFF(SECOND,FROM_UNIXTIME(t.shoppingcart_addtime, '%Y-%m-%d %H:%i:%S'), FROM_UNIXTIME(t.over_time, '%Y-%m-%d %H:%i:%S')) < 604800\n" +
                    "   and (t.over_time is not null or t.over_time <> 0)\n" +
                    "group by t.uid\n" +
                    "union\n" +
                    " select t.uid as uid /*用户ID*/\n" +
                    "  from temp3_sl_rpt_analysis_shoppingcart t\n" +
                    " where TIMESTAMPDIFF(SECOND,FROM_UNIXTIME(t.shoppingcart_addtime, '%Y-%m-%d %H:%i:%S'), FROM_UNIXTIME(t.over_time, '%Y-%m-%d %H:%i:%S')) >=604800\n" +
                    "   and (t.over_time is not null or t.over_time <> 0)\n" +
                    "group by t.uid ;";
            /*生成购物车标示表*/
            String sqlDrop6 ="truncate table sl_rpt_analysis_shoppingcart_flag;";
            String sql6="insert into sl_rpt_analysis_shoppingcart_flag\n" +
                    "select \n" +
                    "  t.day_no as day_no,\n" +
                    "  t.province as province,\n" +
                    "  t.city as city,\n" +
                    "  t.town as town,\n" +
                    "  t.village as village,\n" +
                    "  t.address as address,\n" +
                    "  t.uid as uid,\n" +
                    "  t.goodsid as goodsid,\n" +
                    "  t.goods as goods,\n" +
                    "  t.shopsid as shopsid,\n" +
                    "  t.shopsname as shopsname,\n" +
                    "  t.shoppingcart_addtime as shoppingcart_addtime,\n" +
                    "  (case when t1.state in ('3') then '1' else '0' end) as goodsid_shoppingcart_flag1, /*加入购物车成功交易标示 1-是 0-否*/\n" +
                    "  (case when t.exist in ('2') then '1' else '0' end) as goodsid_shoppingcart_flag2, /*加入购物车删除标示 1-是 0-否*/\n" +
                    "  t.goodsid_freight_price as goodsid_freight_price, /*商品加入购物车的运费金额*/\n" +
                    "  (case when t2.uid is null then 1 else 0 end) as goodsid_shoppingcart_flag3 /*加入购物车7天后无订单生成标示 1-是 0-否*/\n" +
                    "                       \n" +
                    "from temp3_sl_rpt_analysis_shoppingcart t left join sl_rpt_analysis_shoppingcart_order t1 on t.orderid = t1.order_id\n" +
                    "                                          left join temp4_sl_rpt_analysis_shoppingcart t2 on t.uid = t2.uid ;";


            //删除临时表
            String drop1 = "DROP TABLE IF EXISTS temp1_sl_rpt_analysis_shoppingcart;";
            String drop2 = "DROP TABLE IF EXISTS temp2_sl_rpt_analysis_shoppingcart;";
            String drop3 = "DROP TABLE IF EXISTS temp3_sl_rpt_analysis_shoppingcart;";
            String drop4 = "DROP TABLE IF EXISTS temp4_sl_rpt_analysis_shoppingcart;";

            try {
                //statement
                statement = connection.createStatement();

                statement.execute(beginLog_oder);
                    statement.execute(sqlDrop1);
                    statement.execute(sqlDrop2);
                    statement.execute(sqlDrop3);
                    statement.execute(sqlDrop4);
                    statement.execute(sqlDrop5);
                    statement.execute(sqlDrop6);

                    connection.setAutoCommit(false);
                    statement.execute(beginLog_flag);
                    statement.execute(sql1);
                    statement.execute(sql2);
                    statement.execute(sql3);
                    statement.execute(sql4);
                    statement.execute(sql5);
                    statement.execute(sql6);
                    statement.execute(endLog_oder);
                    statement.execute(endLog_flag);
                    connection.commit();
                    connection.setAutoCommit(true);

                    statement.execute(drop1);
                    statement.execute(drop2);
                    statement.execute(drop3);
                    statement.execute(drop4);
            } catch (SQLException e) {
                logger.error("for sl_rpt_analysis_shoppingcart_flag，sl_rpt_analysis_shoppingcart_order error:" + e.getMessage());
                try {
                    //rollback
                    connection.rollback();
                } catch (SQLException e1) {
                    logger.error("The sl_rpt_analysis_shoppingcart_flag，sl_rpt_analysis_shoppingcart_order rollback error:" + e.getMessage());
                }
            } finally {
                try {
                    if (statement != null) statement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    logger.error("The sl_rpt_analysis_shoppingcart_flag，sl_rpt_analysis_shoppingcart_order close connection error:" + e.getMessage());
                }
            }
        }
    };


    public static TimerTask task10 = new TimerTask() {
        @Override
        public void run() {
            //使用或者下边的语句
            Connection connection = connect.getConnect();
            Statement statement = null;

            //向日志表sl_ods_etl_log插入sl_rpt_app_page_visit表的更新记录
            String beginLog= LogSqlUtil.getBeginLog("sl_rpt_app_page_visit");
            String endLog=LogSqlUtil.getEndLog("sl_rpt_app_page_visit");


         /*取6个月日志样本数据*/

            String sqlDrop1 = "DROP TABLE IF EXISTS temp0_visit_route;";
            String sql1 ="CREATE TABLE temp0_visit_route as\n" +
                    "select * from sl_ods_page_visit_record_"+GetMonth.getOrtherMonth(-5)+"\n" +
                    " union all\n" +
                    "select * from sl_ods_page_visit_record_"+GetMonth.getOrtherMonth(-4)+"\n" +
                    " union all\n" +
                    "select * from sl_ods_page_visit_record_"+GetMonth.getOrtherMonth(-3)+"\n" +
                    " union all\n" +
                    "select * from sl_ods_page_visit_record_"+GetMonth.getOrtherMonth(-2)+"\n" +
                    " union all\n" +
                    "select * from sl_ods_page_visit_record_"+GetMonth.getOrtherMonth(-1)+"\n" +
                    " union all\n" +
                    "select * from sl_ods_page_visit_record_"+GetMonth.getOrtherMonth(0)+";";


           /*找出上级页面id和下级页面id*/
            String sqlDrop2 = "DROP TABLE IF EXISTS temp1_visit_route;";
            String sql2 = "CREATE TABLE temp1_visit_route as\n" +
                    "select t.parent_id as parent_id, /*上级页面id*/\n" +
                    "       t.page_id as page_id, /*当前页面id*/\n" +
                    "       t.sub_page_id as sub_page_id, /*下级页面id*/\n" +
                    "       max(t.pagestart_time) as pagestart_time /*最近一次页面进入时间*/\n" +
                    "  from temp0_visit_route t\n" +
                    " where (t.pagestart_time is not null or t.pagestart_time <> 0)\n" +
                    " group by t.parent_id, t.page_id, t.sub_page_id;";
            /*计算当前页面、上级页面、下级页面的PV、UV、以及平均停留时间*/
              /*计算当前页面*/
            String sqlDrop3 = "DROP TABLE IF EXISTS temp2_visit_route;";
            String sql3 = "CREATE TABLE temp2_visit_route as\n" +
                    "SELECT t.page_id, /*当前页面ID*/\n" +
                    "       count(*) AS page_pv, /*PV*/\n" +
                    "       count(distinct t.imei) AS page_uv, /*UV*/\n" +
                    "       round(sum(case when t.pageend_time is null or t.pageend_time = 0 then 0\n" +
                    "           else TIMESTAMPDIFF(SECOND,FROM_UNIXTIME(t.pagestart_time, '%Y-%m-%d %H:%i:%S'),FROM_UNIXTIME(t.pageend_time, '%Y-%m-%d %H:%i:%S'))\n" +
                    "           end) /count(*),0) as avg_page_restime /*页面平均停留时间*/\n" +
                    "       /*avg(TIMESTAMPDIFF(SECOND,t.pagestart_time,t.pageend_time)) as avg_page_restime,*/\n" +
                    "  from temp0_visit_route t\n" +
                    "  where (t.pagestart_time is not null or t.pagestart_time <> 0)\n" +
                    "GROUP BY t.page_id ;";
              /*计算上级页面*/
            String sqlDrop4 = "DROP TABLE IF EXISTS temp3_visit_route;";
            String sql4 = "CREATE TABLE temp3_visit_route as\n" +
                    "SELECT t.parent_id, /*上级页面ID*/\n" +
                    "       count(*) AS page_pv, /*PV*/\n" +
                    "       count(distinct t.imei) AS page_uv, /*UV*/\n" +
                    "       round(sum(case when t.pageend_time is null or t.pageend_time = 0 then 0\n" +
                    "           else TIMESTAMPDIFF(SECOND,FROM_UNIXTIME(t.pagestart_time, '%Y-%m-%d %H:%i:%S'),FROM_UNIXTIME(t.pageend_time, '%Y-%m-%d %H:%i:%S'))\n" +
                    "           end) /count(*),0) as avg_page_restime /*页面平均停留时间*/\n" +
                    "       /*avg(TIMESTAMPDIFF(SECOND,t.pagestart_time,t.pageend_time)) as avg_page_restime,*/\n" +
                    "  from temp0_visit_route t\n" +
                    "  where (t.pagestart_time is not null or t.pagestart_time <> 0)\n" +
                    "  GROUP BY t.parent_id ;";
            /*计算下级页面*/
            String sqlDrop5 = "DROP TABLE IF EXISTS temp4_visit_route;";
            String sql5 = "CREATE TABLE temp4_visit_route as\n" +
                    "SELECT t.sub_page_id, /*下级页面ID*/\n" +
                    "       count(*) AS page_pv, /*PV*/\n" +
                    "       count(distinct t.imei) AS page_uv, /*UV*/\n" +
                    "       round(sum(case when t.pageend_time is null or t.pageend_time = 0 then 0\n" +
                    "           else TIMESTAMPDIFF(SECOND,FROM_UNIXTIME(t.pagestart_time, '%Y-%m-%d %H:%i:%S'),FROM_UNIXTIME(t.pageend_time, '%Y-%m-%d %H:%i:%S'))\n" +
                    "           end) /count(*),0) as avg_page_restime /*页面平均停留时间*/\n" +
                    "       /*avg(TIMESTAMPDIFF(SECOND,t.pagestart_time,t.pageend_time)) as avg_page_restime,*/\n" +
                    "  from temp0_visit_route t\n" +
                    "  where (t.pagestart_time is not null or t.pagestart_time <> 0)\n" +
                    "  GROUP BY t.sub_page_id ;";

            /*汇总*/
            String sqlDrop6 = "DROP TABLE IF EXISTS temp5_visit_route;";
            String sql6 = "CREATE TABLE temp5_visit_route as\n" +
                    "select distinct t0.parent_id,\n" +
                    "       t2.page_pv as parent_id_pv,\n" +
                    "       t2.page_uv as parent_id_uv,\n" +
                    "       t2.avg_page_restime as parent_id_restime,\n" +
                    "       t0.page_id,\n" +
                    "       t1.page_pv as page_id_pv,\n" +
                    "       t1.page_uv as page_id_uv,\n" +
                    "       t1.avg_page_restime as page_id_restime,\n" +
                    "       t0.sub_page_id,\n" +
                    "       t3.page_pv as sub_page_id_pv,\n" +
                    "       t3.page_uv as sub_page_id_uv,\n" +
                    "       t3.avg_page_restime as sub_page_restime,\n" +
                    "       t0.pagestart_time\n" +
                    "  from temp1_visit_route t0\n" +
                    "      join temp2_visit_route t1 on t0.page_id = t1.page_id\n" +
                    "      join temp3_visit_route t2 on t0.parent_id = t2.parent_id\n" +
                    "      join temp4_visit_route t3 on t0.sub_page_id = t3.sub_page_id ;";

            String sqlDrop7 = "DROP TABLE IF EXISTS sl_rpt_app_page_visit;";
            String sql7 = "CREATE TABLE sl_rpt_app_page_visit as\n" +
                    "SELECT t0.parent_id,\n" +
                    "       t2.page_name as parent_id_name,\n" +
                    "       t0.parent_id_pv,\n" +
                    "       t0.parent_id_uv,\n" +
                    "       t0.parent_id_restime,\n" +
                    "       t0.page_id,\n" +
                    "       t1.page_name as page_id_name,\n" +
                    "       t0.page_id_pv,\n" +
                    "       t0.page_id_uv,\n" +
                    "       t0.page_id_restime,\n" +
                    "       t0.sub_page_id,\n" +
                    "       t3.page_name as sub_page_id_name,\n" +
                    "       t0.sub_page_id_pv,\n" +
                    "       t0.sub_page_id_uv,\n" +
                    "       t0.sub_page_restime,\n" +
                    "       t0.pagestart_time\n" +
                    "  from temp5_visit_route t0 join sl_ods_page t1 on t0.page_id = t1.page_id\n" +
                    "                            join sl_ods_page t2 on t0.parent_id = t2.page_id\n" +
                    "                            join sl_ods_page t3 on t0.sub_page_id = t3.page_id;";

            //删除临时表
            String drop1 = "DROP TABLE IF EXISTS temp0_visit_route;";
            String drop2 = "DROP TABLE IF EXISTS temp1_visit_route;";
            String drop3 = "DROP TABLE IF EXISTS temp2_visit_route;";
            String drop4 = "DROP TABLE IF EXISTS temp3_visit_route;";
            String drop5 = "DROP TABLE IF EXISTS temp4_visit_route;";
            String drop6 = "DROP TABLE IF EXISTS temp5_visit_route;";

            try {
                //statement
                statement = connection.createStatement();

                statement.execute(beginLog);
                    statement.execute(sqlDrop1);
                    statement.execute(sqlDrop2);
                    statement.execute(sqlDrop3);
                    statement.execute(sqlDrop4);
                    statement.execute(sqlDrop5);
                    statement.execute(sqlDrop6);
                    statement.execute(sqlDrop7);

                    connection.setAutoCommit(false);

                    statement.execute(sql1);
                    statement.execute(sql2);
                    statement.execute(sql3);
                    statement.execute(sql4);
                    statement.execute(sql5);
                    statement.execute(sql6);
                    statement.execute(sql7);


                    statement.execute(endLog);
                    connection.commit();

                    connection.setAutoCommit(true);
                    statement.execute(drop1);
                    statement.execute(drop2);
                    statement.execute(drop3);
                    statement.execute(drop4);
                    statement.execute(drop5);
                    statement.execute(drop6);

            } catch (SQLException e) {
                logger.error("for sl_rpt_app_page_visit error:" + e.getMessage());
                try {
                    //rollback
                    connection.rollback();
                } catch (SQLException e1) {
                    logger.error("The sl_rpt_app_page_visit rollback error:" + e.getMessage());
                }
            } finally {
                try {
                    if (statement != null) statement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    logger.error("The sl_rpt_app_page_visit close connection error:" + e.getMessage());
                }
            }
        }
    };



}
