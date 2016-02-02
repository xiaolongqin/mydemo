package com.szl.stronguion.model.menus;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * Created by  on 2015/6/29.
 */
public class FeedBack extends Model<FeedBack> {
    public static final int pageSize =2;

    private static FeedBack dao = new FeedBack();

    public List<FeedBack> getLastedFeedBack() {
        return dao.find("select * from eb_web_feedback order by id desc LIMIT 0,3;");
    }

    public List<FeedBack> getAllFeedBack() {
        return dao.find("SELECT b.*, a.name FROM eb_web_feedback b LEFT JOIN eb_web_account a ON b.userid = a.id ORDER BY b.id DESC;");
    }

    public FeedBack getFeedBackByID(long id) {
        return dao.findFirst("SELECT b.*, a.name FROM eb_web_feedback b LEFT JOIN eb_web_account a ON b.userid = a.id where b.id = '" + id + "' ORDER BY b.id DESC;");
    }

    public boolean addFeedBack(String title, String content,String contact,long feedback_time) {
        return new FeedBack().set("content", content).set("contact", contact).set("feedback_time", feedback_time).set("title", title).save();
    }

    public boolean modifyFeedBack(long id, String title, String content,String contact) {
//      return   !Db.use("main1").find("update notice set " + TITLE + " = " + title + ", " + CONTENT + " = " + content + " where id = " + id + ";").isEmpty();
        return Db.use("main1").update("update eb_web_feedback set title = '" + title + "' , content = '" + content + "', contact = '" + contact + "' where id = " + id + ";") == 1;
//        return dao.findById(id).set(CONTENT, content).set(TITLE, title).save();
    }

    public List<Record> searchFeedBack(String content,int pageNumber) {
        return Db.use("main1").paginate(pageNumber,pageSize,"SELECT a. NAME, b.* ","FROM eb_web_feedback b LEFT JOIN eb_web_account a ON b.userid = a.id  where  b.content like '%" + content + "%' or b.title like '%"+content+"%' order by b.id desc ").getList();
    }
    public long getTotalPage(String content) {
        return Db.use("main1").findFirst("select count(*) as total from eb_web_feedback where  content like '%" + content + "%'or title like '%"+content+"%' ").get("total");
    }

    public boolean deleteFeedBack(int id) {
        return Db.use("main1").deleteById("eb_web_feedback", id);
    }
}
