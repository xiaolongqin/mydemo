package model;

import com.jfinal.plugin.activerecord.Model;

/**
 * Created by Tyfunwang on 2015/1/30.
 */
public class Feedback extends Model<Feedback> {
    public static final String FEED_SERVICE = "serv_name";
    public static final String FEED_CONTENT = "content";
    public static final String FEED_EMAIL = "email";

    public static final Feedback dao = new Feedback();
    public boolean addFeed(String serv_name,String content,String email){
            return new Feedback().set("serv_name",serv_name).set("content",content).set("email",email).save();
    }
}
