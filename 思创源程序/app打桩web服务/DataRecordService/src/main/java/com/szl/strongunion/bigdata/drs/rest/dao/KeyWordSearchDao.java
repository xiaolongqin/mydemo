package com.szl.strongunion.bigdata.drs.rest.dao;

import com.jfinal.plugin.activerecord.Model;

import java.util.Map;

/**
 * Created by liweiqi on 2015/7/14.
 */
public class KeyWordSearchDao extends Model<KeyWordSearchDao> {
    public static InterfaceLogDao dao = new InterfaceLogDao();
    public final static String TABLENAME = "sl_ods_keywords_search";
    public final static String ID = "id";
    public final static String STATE = "uid";
    public final static String IPADRESS = "ip_adress";
    public final static String POSTVALUE = "imei";
    public final static String RETURNVALUE = "word_content";
    public final static String PAGEID = "page_id";
    public final static String PAGEURL = "page_url";
    public final static String ACTION = "action";
    public final static String TIMES = "times";
    public final static String DATETIME = "datetime";
    public final static String ADDTIME = "add_time";


    public boolean add(Map<String, Object> attrs) {
        return new KeyWordSearchDao().setAttrs(attrs).save();
    }
}
