package com.szl.strongunion.bigdata.drs.rest.dao;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.szl.strongunion.bigdata.drs.rest.util.CalendarUtil;
import com.szl.strongunion.bigdata.drs.rest.util.QueryTableUtil;

import java.util.Map;

/**
 * Created by liweiqi on 2015/5/27.
 */
public class InterfaceLogDao {
    public static InterfaceLogDao dao = new InterfaceLogDao();
    public final static String TABLENAME = "sl_ods_interface_errorlog";
    public final static String ID = "id";
    public final static String STATE = "state";
    public final static String URL = "url";
    public final static String POSTVALUE = "post_value";
    public final static String RETURNVALUE = "return_value";
    public final static String ADDTIME = "add_time";
//    public final static String TABLESCHEMA = "strongunion_online_test";
    public final static String TABLESCHEMA = "strongunion_online";

    public boolean add(Map<String, Object> attrs) {
        Record record = new Record().setColumns(attrs);
        String realTableName = TABLENAME + CalendarUtil.getMonthPostFix();
        return Db.save(realTableName, record);
    }

    public boolean checkExistTable(){
        boolean flag=true;
        String realTableName = TABLENAME + CalendarUtil.getMonthPostFix();
        int isHasTable=Integer.valueOf(Db.query(QueryTableUtil.querySql(realTableName, TABLESCHEMA)).get(0).toString());
        if (isHasTable==0){
            flag=false;
            int create= Db.update(QueryTableUtil.createLogTable(realTableName));
            if (create==0){
                flag=true;
            }
        }
        return flag;
    }
}
