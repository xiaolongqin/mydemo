package com.szl.stronguion.service.menus;

import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.menus.FeedBack;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/6/29.
 */
public class FeedServ {
    private FeedBack feed = new FeedBack();

    public List<FeedBack> getLastedFeedBack() {
        return feed.getLastedFeedBack();
    }

    /**
     * for notice controller*
     */
    public List<FeedBack> getAllFeedBack() {
        return feed.getAllFeedBack();
    }

    public FeedBack getFeedBackByID(long id) {
        return feed.getFeedBackByID(id);
    }

    public boolean addFeedBack(String title, String content,String contact,long feedback_time) {
        return feed.addFeedBack( title,  content, contact, feedback_time);
    }

    public boolean modifyFeedBack(long id, String title, String content,String contact) {
        return feed.modifyFeedBack( id,  title,  content, contact);
    }

    public List<Record> searchFeedBack( String content,int pageNumber) {
        long t2=feed.getTotalPage(content);
        List<Record> list2=feed.searchFeedBack(content, pageNumber);
        if (list2.size()>0){
            list2.get(0).set("total",Math.ceil(t2/(FeedBack.pageSize*1.0)));
        }
        return list2;
    }

    public boolean deleteFeedBack(int id) {
        return feed.deleteFeedBack(id);
    }
}
