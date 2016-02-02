package com.szl.strongunion.bigdata.drs.rest.service;

import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.plugin.activerecord.Record;
import com.szl.strongunion.bigdata.drs.rest.dao.PageVisitRecordDao;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liweiqi on 2015/5/27.
 */
public class PageVisitRecordSrv {
    private static PageVisitRecordSrv srv = new PageVisitRecordSrv();

    private PageVisitRecordSrv() {
    }

    public static PageVisitRecordSrv me() {
        return srv;
    }

    public int add(Map<String, Object> attrs) {
        int parent_act_id = Integer.valueOf(attrs.get(PageVisitRecordDao.PARENTACTID).toString());

        attrs.put(PageVisitRecordDao.UID,Integer.valueOf(attrs.get(PageVisitRecordDao.UID).toString()));
        attrs.put(PageVisitRecordDao.PAGEID,Integer.valueOf(attrs.get(PageVisitRecordDao.PAGEID).toString()));
        attrs.put(PageVisitRecordDao.STARTTIME,Integer.valueOf(attrs.get(PageVisitRecordDao.STARTTIME).toString()));
        attrs.put(PageVisitRecordDao.CHANNELAPKID,Integer.valueOf(attrs.get(PageVisitRecordDao.CHANNELAPKID).toString()));
        attrs.put(PageVisitRecordDao.PAGEACTIONID,Integer.valueOf(attrs.get(PageVisitRecordDao.PAGEACTIONID).toString()));

        if (parent_act_id >= 0) {
            Record parentAction = PageVisitRecordDao.dao.findAcrossTable(parent_act_id);
            if (parentAction != null){
                attrs.put(PageVisitRecordDao.PARENTID, parentAction.getNumber(PageVisitRecordDao.PAGEID).intValue());
            }else {
                throw new ActiveRecordException("parent_act_id is error");
            }
        }else {
            attrs.put(PageVisitRecordDao.PARENTID, -1);
        }
        Object pageId = attrs.get(PageVisitRecordDao.PAGEID);
        int id = PageVisitRecordDao.dao.add(attrs);
        try {
            if (parent_act_id > 0) {
                Map<String, Object> parentUpAttrs = new HashMap<String,Object>();
                parentUpAttrs.put(PageVisitRecordDao.ID, parent_act_id);
                parentUpAttrs.put(PageVisitRecordDao.SUBACTID, id);
                parentUpAttrs.put(PageVisitRecordDao.SUBPAGEID, Integer.valueOf(pageId.toString()));
                boolean state = update(parentUpAttrs);
            }
        } catch (Exception ex) {
            //todo log the failed updateAcrossTable operation
        }
        return id;
    }

    public boolean update(Map<String, Object> attrs) {
        return PageVisitRecordDao.dao.updateAcrossTable(attrs);
    }

    public boolean chekTableExist() {
        return PageVisitRecordDao.dao.isHasTable();
    }
}
