package com.szl.stronguion.model.menus;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/6/29.
 */
public class Notice extends Model<Notice> {
    private static final String CONTENT = "content";
    private static final String TITLE = "title";
    public static final int pageSize =5;

    private static Notice dao = new Notice();

    public List<Notice> getLastedNotice() {
        return dao.find("select * from notice order by id desc LIMIT 0,3;");
    }

    public List<Notice> getAllNotices() {
        return dao.find("select * from notice order by id desc ;");
    }

    public boolean addNotice(String title, String content) {
        return new Notice().set(CONTENT, content).set(TITLE, title).save();
    }

    public boolean modifyNotice(int id, String title, String content) {
//      return   !Db.use("main1").find("update notice set " + TITLE + " = " + title + ", " + CONTENT + " = " + content + " where id = " + id + ";").isEmpty();
        return Db.use("main1").update("update notice set " + TITLE + " = '" + title + "' , " + CONTENT + " = '" + content + "' where id = " + id + ";") == 1;
//        return dao.findById(id).set(CONTENT, content).set(TITLE, title).save();
    }

    public List<Record> searchNotice(String content,int pageNumber) {
        return Db.use("main1").paginate(pageNumber,pageSize,"select * ","from notice where  " + CONTENT + " like '%" + content + "%' or title like '%"+content+"%' order by id desc ").getList();
    }
    public long getTotalPage(String content) {
        return Db.use("main1").findFirst("select count(*) as total from notice where  " + CONTENT + " like '%" + content + "%'").get("total");
    }

    public boolean deleteNotice(int id) {
        return Db.use("main1").deleteById("notice", id);
    }
}
