package com.szl.strongunion.bigdata.drs.rest.dao;

import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.szl.strongunion.bigdata.drs.rest.util.CalendarUtil;
import com.szl.strongunion.bigdata.drs.rest.util.QueryTableUtil;

import java.util.Map;

/**
 * Created by liweiqi on 2015/5/27.
 */
public class PageVisitRecordDao {
    public static PageVisitRecordDao dao = new PageVisitRecordDao();

    public final static String TABLNEME = "sl_ods_page_visit_record";
    public final static String ID = "id";
    public final static String UID = "uid";
    public final static String PARENTACTID = "parent_act_id";
    public final static String PARENTID = "parent_id";
    public final static String SUBACTID = "sub_act_id";
    public final static String SUBPAGEID = "sub_page_id";
    public final static String CONTENT = "contents";
    public final static String PAGEID = "page_id";
    public final static String PAGEACTIONID = "page_action_id";
    public final static String CHANNELAPKID = "channel_apk_id";
    public final static String IMEI = "imei";
    public final static String IP = "ip_adress";
    public final static String STARTTIME = "pagestart_time";
    public final static String ENDTIME = "pageend_time";
    public final static String ADDTIME = "add_time";
    public final static String TABLESCHEMA = "strongunion_online_test";

    public int add(Map<String, Object> attrs) {
        attrs.put(ADDTIME, System.currentTimeMillis()/1000L);
        Record record = new Record().setColumns(attrs);
        String realTableName = TABLNEME + CalendarUtil.getMonthPostFix();
        return Db.save(realTableName, record) ? record.getNumber(ID).intValue() : -1;
    }

    public boolean updateAcrossTable(Map<String, Object> attrs) {
        Record record = new Record().setColumns(attrs);
        if (!attrs.containsKey(PageVisitRecordDao.ID)) {
            throw new ActiveRecordException("You can't updateAcrossTable model without Primary Key.");
        }
        String realTableName = TABLNEME + CalendarUtil.getMonthPostFix();
        if (Db.findById(realTableName, attrs.get(PageVisitRecordDao.ID)) == null) {
            realTableName = TABLNEME + CalendarUtil.getPrevMonthPostFix();
        }
        return Db.update(realTableName, record);
    }

    public Record findAcrossTable(Object id) {
        String realTableName = TABLNEME + CalendarUtil.getMonthPostFix();
        Record record = Db.findById(realTableName, id);
        return record == null ? Db.findById(TABLNEME + CalendarUtil.getPrevMonthPostFix(), id) : record;
    }

    public boolean isHasTable(){
        boolean flag=true;
        String realTableName = TABLNEME + CalendarUtil.getMonthPostFix();
        int isHasTable=Integer.valueOf(Db.query(QueryTableUtil.querySql(realTableName, TABLESCHEMA)).get(0).toString());
        if (isHasTable==0){
            flag=false;
            int create= Db.update(QueryTableUtil.createVisitTable(realTableName));
            if (create==0){
                flag=true;
            }
        }
        return flag;
    }
}
