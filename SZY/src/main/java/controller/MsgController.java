package controller;

import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Page;
import interceptor.AccessInterceptor;
import model.Msg;
import model.User;
import service.MsgService;
import service.oss.LoadsSrv;
import util.EncAndDecByDES;
import util.JsonHelp;

import javax.mail.MessagingException;

/**
 * Created by Tyfunwang on 2014/12/12.
 */
@ClearInterceptor(ClearLayer.ALL)
@Before(AccessInterceptor.class)
public class MsgController extends Controller {
    MsgService msgService = new MsgService();
    EncAndDecByDES encAndDecByDES = new EncAndDecByDES();

    /**
     * send msg and email to user
     */
    public void send2Msg() {
        int userid = getParaToInt(User.USER_ID);
        String email = getPara(User.USER_EMAIL);
        String title = getPara(Msg.MSG_TITLE);
        String content = getPara(Msg.MSG_CONTENT);

        renderJson(msgService.sendMsg(userid, email, title, content) ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
    }

    /**
     * send msg to all user
     */
    public void send2All() {
        //userid默认为-1
        String title = getPara("title");
        String content = getPara("content");
        renderJson(msgService.sendMsg2All(title, content) ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
    }

    /**
     * get msg  for rds
     */
    public void getMsg() {
        int userid = getParaToInt(User.USER_ID);
        int currentPage = getParaToInt(User.USER_CURRENTPAGE);
        int pageSize = getParaToInt(User.USER_PAGESIZE);
        Page<Msg> msgPage = msgService.getMsg(userid, currentPage, pageSize);
        renderJson(JsonHelp.buildSuccess(JsonKit.toJson(msgPage)));
    }

    public void getMsgById() {
        int id = getParaToInt("id");
        Msg msgPage = msgService.getMsgById(id);
        renderJson(JsonHelp.buildSuccess(JsonKit.toJson(msgPage)));
    }

    /**
     * send msg for oss
     */

    public void sendOss() {

        String src_e= getPara("src_email");
        String src_email = encAndDecByDES.getEncString(src_e);
        String src_f = encAndDecByDES.getEncString(getPara("src_f"));
        long time = System.currentTimeMillis();
        String ti = encAndDecByDES.getEncString(time+"");
        String dst_email = getPara("dst_email");
        try {
            if (LoadsSrv.me().sendEmailOss(src_email,src_e, src_f, dst_email,ti)) {
                renderText("true");
                return;
            }
            renderText("false");
        } catch (MessagingException e1) {
            e1.printStackTrace();
            renderText("false");
        }
    }
}
