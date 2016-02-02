package com.szl.stronguion.model.menus;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.utils.FormatUtils;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/6/29.
 */
public class Notice extends Model<Notice> {
    private static final String CONTENT = "content";
    private static final String TITLE = "title";
    private static final String TIME = "updatetime";
    public static final int pageSize =5;

    private static Notice dao = new Notice();

    public List<Notice> getLastedNotice() {
        return dao.find("select id,title,content,DATE_FORMAT(updatetime,'%Y-%m-%d') as 'time' from eb_web_notice order by id desc LIMIT 0,3;");
    }

    public List<Notice> getAllNotices() {

        return dao.find("select * from eb_web_notice order by id desc ;");
    }

    public boolean addNotice(String title, String content) {
        return new Notice().set(CONTENT, content).set(TITLE, title).set(TIME, FormatUtils.getDate()).save();
    }

    public boolean modifyNotice(int id, String title, String content) {
//      return   !Db.use("main1").find("update notice set " + TITLE + " = " + title + ", " + CONTENT + " = " + content + " where id = " + id + ";").isEmpty();
        return Db.use("main1").update("update eb_web_notice set " + TITLE + " = '" + title + "' , " + CONTENT + " = '" + content + "',updatetime='"+FormatUtils.getDate()+"'  where id = " + id + ";") == 1;
    }

    public List<Record> searchNotice(String content,int pageNumber) {
        return Db.use("main1").paginate(pageNumber,pageSize,"select * ","from eb_web_notice where  " + CONTENT + " like '%"+content+"%' or title like '%"+content+"%' order by id desc ").getList();
    }
    public long getTotalPage(String content) {
        return Db.use("main1").findFirst("select count(*) as total from eb_web_notice where  " + CONTENT + " like '%" + content + "%'or title like '%"+content+"%' ").get("total");
    }

    public boolean deleteNotice(int id) {
        return Db.use("main1").deleteById("eb_web_notice", id);
    }
}
