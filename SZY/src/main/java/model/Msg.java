package model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.AuthService;
import util.FormatTime;

import javax.mail.MessagingException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Tyfunwang on 2014/12/12.
 */
public class Msg extends Model<Msg> {
    public static final String MSG_USERID = "userid";
    public static final String MSG_TIME = "time";
    public static final String MSG_TITLE = "title";
    public static final String MSG_CONTENT = "content";
    public static final Msg dao = new Msg();
    public static final Logger log = LoggerFactory.getLogger(Msg.class);

    /**
     * send msg and email  to user
     */
    public boolean sendMsg2User(int userid, String email, String title, String content) {
        long time = System.currentTimeMillis();
        //插入数据库后，给用户发送邮件
        try {
            if (dao.set(MSG_USERID, userid).set(MSG_TITLE, title).set(MSG_CONTENT, content).set(MSG_TIME, time).save()) {
                String time1 = FormatTime.time2String(new Timestamp(dao.getLong("time")));
                dao.clear();
                return sendEmail2User(email, title, content, time1);
            }
            return false;
        } catch (Exception e) {
            log.warn("sendMsg2User:" + e.getMessage());
            return false;
        }
    }

    // send message to user by email;
    public boolean sendEmail2User(String email, String title, String content, String time) throws MessagingException {
        return new AuthService().sendMsgMail(email, title, content, time);
    }

    /**
     * send message to all user
     * userid -1
     */
    public boolean sendMsg2All(String title, String content) {
        long time = System.currentTimeMillis();
        try {
            return new Msg().set(MSG_USERID, -1).set(MSG_TITLE, title).set(MSG_CONTENT, content).set(Msg.MSG_TIME, time).save();
        } catch (Exception e) {
            log.warn("sendMsg2All:" + e.getMessage());
            return false;
        }
    }


    /**
     * get msgs for rds
     */
    public Page<Msg> getMsg(int userid, int currentPage, int pageSize) {
        Page<Msg> msgPage = dao.paginate(currentPage, pageSize, "select *", "from msg where " + User.USER_ID + " = -1 or " + User.USER_ID + " = "
                + userid + " order by time desc");
        Iterator<Msg> iterator = msgPage.getList().iterator();
        while (iterator.hasNext()) {
            Msg msg = iterator.next();
            String time = FormatTime.time2String(new Timestamp(msg.getLong("time")));
            msg.remove("time");
            msg.set("time", time);
        }
        return msgPage;
    }

    public Msg getMsgById(int id) {
        return dao.findFirst("select * from msg  where msgid = " + "'" + id + "'");
    }
}
