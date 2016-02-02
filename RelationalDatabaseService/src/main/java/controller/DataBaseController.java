package controller;

import aop.interceptor.captcha.CaptchaStack;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Page;
import model.local.DataBaseL;
import service.DBSrv;
import util.JsonHelper;

import java.util.Map;

/**
 * Created by liweiqi on 2014/10/28.
 */
public class DataBaseController extends Controller {
    private DBSrv srv = DBSrv.me();

    public void all() {
        String result;
        try {
            int id = getParaToInt(DataBaseL.SPACEID);
            int number = getParaToInt("number");
            int size = getParaToInt("size");
            Page<DataBaseL> dbs = srv.allPage(id, number, size);
            result = JsonHelper.buildSuccess(JsonKit.toJson(dbs));
        } catch (Exception ex) {
            result = JsonHelper.buildFailed("操作失败:"+ex.getMessage());
        }
        renderText(result);
    }

    public void add() {
        String result;
        try {
            Map<String, Object> attrs = DataBaseL.getAttrFromUrl(getParaMap());
            DataBaseL newDB = srv.add(attrs);
            String json = newDB == null ? JsonHelper.buildFailed() : JsonHelper.buildSuccess(JsonKit.toJson(newDB));
            result = json;
        } catch (Exception ex) {
            result = JsonHelper.buildFailed("操作失败:"+ex.getMessage());
        }
        renderText(result);
    }
    @Before(CaptchaStack.class)
    public void del() {
        String result;
        try {
            int id = getParaToInt(DataBaseL.ID);
            String name = getPara(DataBaseL.NAME);
            boolean state = srv.del(id, name);
            String json = state ? JsonHelper.buildSuccess() : JsonHelper.buildFailed();
            result = json;
        } catch (Exception ex) {
            result = JsonHelper.buildFailed("操作失败:"+ex.getMessage());
        }
        renderText(result);
    }

}
