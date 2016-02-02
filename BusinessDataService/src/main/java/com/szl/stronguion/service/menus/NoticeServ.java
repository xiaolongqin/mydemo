package com.szl.stronguion.service.menus;

import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.menus.Notice;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/6/29.
 */
public class NoticeServ {
    private Notice notice = new Notice();

    public List<Notice> getLastedNotice() {
        return notice.getLastedNotice();
    }

    /**
     * for notice controller*
     */
    public List<Notice> getAllNotices() {
        return notice.getAllNotices();
    }

    public boolean addNotice(String title, String content) {
        return notice.addNotice(title, content);
    }

    public boolean modifyNotice(int id, String title, String content) {
        return notice.modifyNotice(id, title, content);
    }

    public List<Record> searchNotice( String content,int pageNumber) {
        if (content == null || "".equals(content)) {
            long totol=notice.getTotalPage(content);
            List<Record> list1=notice.searchNotice(content, pageNumber);
            if (list1.size()>0){
                list1.get(0).set("total",Math.ceil(totol/(Notice.pageSize*1.0)));
            }
            return list1;
        }
        long t2=notice.getTotalPage(content);
        List<Record> list2=notice.searchNotice(content, pageNumber);
        if (list2.size()>0){
            list2.get(0).set("total",Math.ceil(t2/(Notice.pageSize*1.0)));
        }
        return list2;
    }

    public boolean deleteNotice(int id) {
        return notice.deleteNotice(id);
    }
}
