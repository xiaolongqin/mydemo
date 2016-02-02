package controller;

import aop.interceptor.captcha.CaptchaStack;
import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Page;
import model.local.SpaceL;
import service.SpaceSrv;
import util.JsonHelper;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by liweiqi on 2014/10/23.
 */
@Before(CaptchaStack.class)
public class SpaceController extends Controller {
    private SpaceSrv srv = SpaceSrv.me();

    @ClearInterceptor
    public void all() {
        HttpSession session = getRequest().getSession();
        int id = 1;
        int number = getParaToInt("number");
        int size = getParaToInt("size");
        Page<SpaceL> spaces = srv.all(id, number, size);
        renderText(JsonHelper.buildSuccess(JsonKit.toJson(spaces)));
    }


    public void add() {
        int id = 1;
        Map<String, Object> attrs = SpaceL.getAttrFromUrl(getParaMap());
        attrs.put(SpaceL.UID, id);
        //if (!attrs.containsKey(SpaceL.MAXSTORE)) attrs.put(SpaceL.MAXSTORE, 100L);
        SpaceL newSpace = null;
        StringBuilder sb = new StringBuilder();
        try {
            newSpace = srv.addSpace(attrs, attrs);
        } catch (Exception ex) {
            sb.append("操作失败:" + ex.getMessage());
        }
        String result = newSpace == null ? JsonHelper.buildFailed(sb.toString()) : JsonHelper.buildSuccess(JsonKit.toJson(newSpace));
        renderText(result);
    }

    public void del() {
        int id = 1;
        int spaceid = getParaToInt(SpaceL.ID);
        boolean state = srv.del(spaceid, id);
        String result = JsonHelper.build(state);
        renderText(result);
    }

    public void up() {
        Map<String, Object> attrs = SpaceL.getAttrFromUrl(getParaMap());
        boolean state = srv.up(attrs);
        String result = state ? JsonHelper.buildSuccess() : JsonHelper.buildFailed("操作失败");
        renderText(result);
    }
}
