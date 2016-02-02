package controller.forall;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import model.Feedback;
import model.Service;
import model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import plugin.shiro.annotation.ClearAuthHandler;
import plugin.shiro.annotation.RequiresRoles;
import service.FeedbaceServ;
import service.ServiceServ;
import util.JsonHelp;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/1/30.
 */
@RequiresRoles(value = {"user"})
public class FeedbackController extends Controller {
    private ServiceServ serv = new ServiceServ();
    private FeedbaceServ feedbaceServ = new FeedbaceServ();

    public void getServices() {
        try {
            List<Service> list = serv.getAll();
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    //反馈信息
    @ClearAuthHandler
    public void addFeed() {
        String serv = getPara(Feedback.FEED_SERVICE);
        String content = getPara(Feedback.FEED_CONTENT);
        String email = getPara(Feedback.FEED_EMAIL);
//        Subject currentUser = SecurityUtils.getSubject();
        try {
//            User user = (User) currentUser.getSession().getAttribute(User.USER_SESSIONNAME);
//            String email = user.getStr(User.USER_EMAIL);
            if (feedbaceServ.addFeed(serv, content, email)) {
                renderJson(JsonHelp.buildSuccess());
                return;
            }
            renderJson(JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }

    }
}
