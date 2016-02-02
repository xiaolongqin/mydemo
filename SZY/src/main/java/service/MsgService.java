package service;

import com.jfinal.plugin.activerecord.Page;
import model.Msg;

/**
 * Created by Tyfunwang on 2014/12/12.
 */
public class MsgService {
    Msg msg = new Msg();

    //save messgage at msg
    public boolean sendMsg(int userid,String email,String title ,String content){
        return msg.sendMsg2User(userid,email,title,content);
    }

    public boolean sendMsg2All(String title ,String content){
        return msg.sendMsg2All(title,content);
    }

    //get msg for rds
    public Page<Msg> getMsg(int userid, int currentPage,int pageSize){
        return msg.getMsg(userid,currentPage, pageSize);
    }

    public Msg getMsgById(int id) {
        return msg.getMsgById(id);
    }

}
