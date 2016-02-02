package sl.bigdata.util;

/**
 * Created by 小龙
 * on 15-7-2
 * at 上午10:22.
 */

public class LogSqlUtil {


    public static String  getBeginLog(String table_name){
        String begLog="insert into sl_ods_etl_log\n" +
                "SELECT NULL as seq_id,\n" +
                "       lower('strongunion_online') as table_schema,\n" +
                "       lower(\'"+table_name+"\') as table_name,\n" +
                "       lower('TABLE') as table_type,\n" +
                "       lower('rpt') as table_levels,\n" +
                "       now() as begin_time,\n" +
                "       NULL as end_time,\n" +
                "       0  as status ;";
        return  begLog;
    }

    public static String  getEndLog(String table_name){
        String endLog="UPDATE sl_ods_etl_log\n" +
                "SET end_time = now(),\n" +
                "    STATUS = 1\n" +
                "WHERE\n" +
                "\ttable_schema = 'strongunion_online'\n" +
                "AND table_name = \'"+table_name+"\'\n" +
                "AND table_type = 'table'\n" +
                "AND table_levels = 'rpt'\n" +
                "AND DATE_FORMAT(begin_time, '%Y-%m-%d %H:%i:%S') in (\n" +
                "select a.maxdate \n" +
                "from (select max(DATE_FORMAT(begin_time, '%Y-%m-%d %H:%i:%S')) as maxdate\n" +
                "   from sl_ods_etl_log \n" +
                "\twhere table_schema = 'strongunion_online'\n" +
                "    AND table_name = \'"+table_name+"\'\n" +
                "    AND table_type = 'table'\n" +
                "    AND table_levels = 'rpt') a) ;";
        return  endLog;
    }

}
