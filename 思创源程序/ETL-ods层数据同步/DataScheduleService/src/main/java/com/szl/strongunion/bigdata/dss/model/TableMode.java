package com.szl.strongunion.bigdata.dss.model;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.DbPro;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.szl.strongunion.bigdata.dss.config.db.PoolInitiator;
import com.szl.strongunion.bigdata.dss.util.CalendarUtil;
import com.szl.strongunion.bigdata.dss.util.LogSqlUtil;
import com.szl.strongunion.bigdata.dss.util.QueryTableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by 小龙
 * on 15-7-9
 * at 上午11:18.
 */

public class TableMode {
    private String source_name;
    private String target_name;
    private String timeName;
    private String cycle;
    private String table_fields;
    private static int pageSize=10000;
    private static PoolInitiator poolInitiator=PoolInitiator.instance();
    private static String source_config_name=poolInitiator.getRegistPoolConfigName()[0];
    private static String target_config_name=poolInitiator.getRegistPoolConfigName()[1];
    private static Logger logger = LoggerFactory.getLogger(TableMode.class);

    public TableMode(String source_name,String target_name,String timeName,String cycle,String fields){
        this.source_name=source_name;
        this.target_name=target_name;
        this.timeName=timeName;
        this.cycle=cycle;
        this.table_fields=fields;

    }
    public TableMode(){

    }
    //统计source表的记录数
    public int source_count(){
        int count;
        List<Record> source_record= DbPro.use(source_config_name).query("SELECT COUNT(*) as counts from  "+source_name);
        count=Integer.valueOf(String.valueOf(source_record.get(0)));
        return count;
    }

    //统计target_name表的记录数
    public int target_count(){
        int count;
        List<Record> source_record= DbPro.use(target_config_name).query("SELECT COUNT(*) as counts from  "+target_name);
        count=Integer.valueOf(String.valueOf(source_record.get(0)));
        return count;
    }
    //日志功能
    public void synLog(){
        DbPro.use(target_config_name).update(LogSqlUtil.getBeginLog(target_name));
    }
    //获取表字段个数，返回相应的value(?,?...)
    public String valueCount(){
        String table_value = "?";
        char c = ',';
        char[] chars = table_fields.toCharArray();
        for(int i = 0; i < chars.length; i++)
        {
            if(c == chars[i])
            {
                table_value+=",?";
            }
        }
        return  table_value;
    }

    //全量同步
   @Before(Tx.class)
    public boolean saveAll(){
       List<Record> source_record;
       int n=source_count();
       boolean flag=false;
       String table_value=valueCount();
       int sucess_save=0;
       try {
           if (n>pageSize){
               DbPro.use(target_config_name).update("truncate table "+target_name);
               for(int i=1;i<=(int)Math.ceil(n*1.0/pageSize);i++){
                   source_record= DbPro.use(source_config_name).paginate(i,pageSize,"select "+table_fields," from "+source_name).getList();
                   int saveConut[]= DbPro.use(target_config_name).batch("insert into "+target_name+"("+table_fields+") values("+table_value+")",table_fields,source_record,500);
                   sucess_save+=saveConut.length;
               }
           }else {
               source_record=DbPro.use(source_config_name).find("select "+table_fields+" from "+source_name);
               if (source_record.size()!=0){
                   DbPro.use(target_config_name).update("truncate table "+target_name);
                   int saveConut[]=DbPro.use(target_config_name).batch("insert into "+target_name+"("+table_fields+") values("+table_value+")",table_fields,source_record,source_record.size()>500?500:source_record.size());
                   sucess_save=saveConut.length;
               }
           }
           if (sucess_save==n){
               flag=true;
               DbPro.use(target_config_name).update(LogSqlUtil.getEndLog(target_name));
           }
       }catch (Exception e){
           logger.error("全量同步表"+source_name+"遇到问题:"+e.getMessage());
       }
       return flag;
    }
    //增量同步
    @Before(Tx.class)
    public boolean  savaByCycle(){
        boolean flag=false;
        int sucess_save=0;
        String table_value=valueCount();
        try {
            List<Record> source_record=DbPro.use(source_config_name).find("select "+table_fields+"\n" +
                    "  from "+source_name+
                    "  where  "+timeName+" >= UNIX_TIMESTAMP(date_sub(curdate(),interval "+cycle+" day))\n" +
                    "  and "+timeName+" < UNIX_TIMESTAMP(CURDATE());");
            if (source_record.size()!=0){
                DbPro.use(target_config_name).update("delete from "+target_name+"  where "+timeName+" >= UNIX_TIMESTAMP(date_sub(curdate(),interval "+cycle+" day))\n" +
                        "   and "+timeName+" < UNIX_TIMESTAMP(CURDATE());\n");
                    sucess_save= DbPro.use(target_config_name).batch("insert into "+target_name+"("+table_fields+") values("+table_value+")",table_fields,source_record,source_record.size()>500?500:source_record.size()).length;
            }
            if (sucess_save==source_record.size()){
                flag=true;
                DbPro.use(target_config_name).update(LogSqlUtil.getEndLog(target_name));
            }
        }catch (Exception e){
            logger.error("增量同步表"+source_name+"遇到问题:"+e.getMessage());
        }
        return flag;
    }

    /**
     * 数据同步
     * @param isAll:判断是否是全量同步参数
     * @return
     */
    public boolean source_to_targert(boolean isAll){
        synLog();
        if (isAll||target_count()==0){
           return saveAll();
        }else {
           return savaByCycle();
        }
    }

    /**
     * 定时建表
     * type:1为访问记录表，2为错误日志表
     */

    public void createTable(String TABLNEME,String TABLESCHEMA,int type){

        String realTableName = TABLNEME + CalendarUtil.getMonthPostFix();
        int isHasTable=Integer.valueOf(Db.use(target_config_name).query(QueryTableUtil.querySql(realTableName, TABLESCHEMA)).get(0).toString());
        if (isHasTable==0){
            if(type==1){
                 Db.use(target_config_name).update(QueryTableUtil.createVisitTable(realTableName));
            }else {
                Db.use(target_config_name).update(QueryTableUtil.createLogTable(realTableName));
            }

        }

    }



}
