package service;

import model.Feedback;

/**
 * Created by Tyfunwang on 2015/1/30.
 */
public class FeedbaceServ {
    private Feedback feedback = new Feedback();
    public boolean addFeed(String serv_name,String content,String email){
        return feedback.addFeed(serv_name,content,email);
    }
}
