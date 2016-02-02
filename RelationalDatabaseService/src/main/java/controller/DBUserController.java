package controller;

import aop.interceptor.captcha.CaptchaStack;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import model.local.DBUserL;
import model.local.DataBaseL;
import service.DBPriSrv;
import service.DBSrv;
import service.DBUserSrv;
import util.JsonHelper;

import java.util.List;
import java.util.Map;

/**
 * Created by liweiqi on 2014/10/30.
 */
public class DBUserController extends Controller {
    private DBUserSrv srv = DBUserSrv.me();
    private DBPriSrv pri = DBPriSrv.me();

    public void all() {
        int spaceid = getParaToInt(DBUserL.SPACEID);
        int number = getParaToInt("number");
        int size = getParaToInt("size");
        Page<DBUserL> users = srv.allPage(spaceid, number, size);
        renderText(JsonHelper.buildSuccess(JsonKit.toJson(users)));
    }

    @Before(CaptchaStack.class)
    public void add() {
        String result;
        try {
            Map<String, Object> attrs = DBUserL.getAttrFromUrl(getParaMap());
            DBUserL newUser = srv.add(attrs);
            result = newUser == null ? JsonHelper.buildFailed("操作失败") : JsonHelper.buildSuccess(JsonKit.toJson(newUser));
        } catch (Exception ex) {
            result = JsonHelper.buildFailed("操作失败:"+ex.getMessage());
        }
        renderText(result);
    }

    public void up() {
        String result;
        try {
            Map<String, Object> attrs = DBUserL.getAttrFromUrl(getParaMap());
            boolean state = srv.up(attrs);
            result = state ? JsonHelper.buildSuccess() : JsonHelper.buildFailed("操作失败");
        } catch (Exception ex) {
            result = JsonHelper.buildFailed("操作失败:"+ex.getMessage());
        }
        renderText(result);
    }

    @Before(CaptchaStack.class)
    public void upPass() {
        String result;
        try {
            Map<String, Object> attrs = DBUserL.getAttrFromUrl(getParaMap());
            boolean state = srv.upPassword(attrs);
            result = state ? JsonHelper.buildSuccess() : JsonHelper.buildFailed("操作失败");
        } catch (Exception ex) {
            result = JsonHelper.buildFailed("操作失败:"+ex.getMessage());
        }
        renderText(result);
    }

    @Before(CaptchaStack.class)
    public void del() {
        String result;
        try {
            int userID = getParaToInt(DBUserL.ID);
            String name = getPara(DBUserL.NAME);
            boolean state = srv.del(userID, name);
            result = state ? JsonHelper.buildSuccess() : JsonHelper.buildFailed("操作失败");
        } catch (Exception ex) {
            result = JsonHelper.buildFailed("操作失败:"+ex.getMessage());
        }
        renderText(result);
    }

    public void get() {
        String result;
        try {
            int userID = getParaToInt(DBUserL.ID);
            DBUserL userL = srv.getById(userID);
            if (userL == null) {
                result = JsonHelper.buildFailed();
            } else {
                int spaceid = getParaToInt(DBUserL.SPACEID);
                int number = getParaToInt("number");
                int size = getParaToInt("size");
                Page<Record> records = DBSrv.me().getByUserPage(userID, spaceid, number, size);
                StringBuilder sb = new StringBuilder("{");
                sb.append("\"db\":").append(JsonKit.toJson(records));
                sb.append("\"user\":").append(JsonKit.toJson(userL)).append(",");
                sb.append("}");
                result = JsonHelper.buildSuccess(sb.toString());
            }
        } catch (Exception ex) {
            result = JsonHelper.buildFailed("操作失败:"+ex.getMessage());
        }
        renderText(result);
    }

    public void getUn() {
        String result;
        try {
            int uid = getParaToInt(DBUserL.ID);
            int space_id = getParaToInt(DBUserL.SPACEID);
            List<DataBaseL> list = DBSrv.me().getByUserPri(uid, space_id);
            result = JsonHelper.buildSuccess(JsonKit.toJson(list));
        } catch (Exception ex) {
            result = JsonHelper.buildFailed("操作失败:"+ex.getMessage());
        }
        renderText(result);

    }

    public void addPri() {
        String result;
        try {
            String userName = getPara(DBUserL.NAME);
            String dbName = getPara(DataBaseL.NAME);
            int userID = getParaToInt(DBUserL.ID);
            int spaceId = getParaToInt(DBUserL.SPACEID);
            int dbId = getParaToInt("db_id");
            int role = getParaToInt(DBUserL.ROLE);
            boolean state = pri.addDbPriToUser(userName, dbName, spaceId, dbId, userID, role);
            result = state ? JsonHelper.buildSuccess() : JsonHelper.buildFailed("操作失败");
        } catch (Exception ex) {
            result = JsonHelper.buildFailed("操作失败:"+ex.getMessage());
        }
        renderText(result);
    }

    public void upPri() {
        String result;
        try {
            String userName = getPara(DBUserL.NAME);
            String dbName = getPara(DataBaseL.NAME);
            int userID = getParaToInt(DBUserL.ID);
            int spaceId = getParaToInt(DBUserL.SPACEID);
            int dbId = getParaToInt("db_id");
            int role = getParaToInt(DBUserL.ROLE);
            boolean state = pri.changeUserPri(userName, dbName, spaceId, dbId, userID, role);
            result = state ? JsonHelper.buildSuccess() : JsonHelper.buildFailed("操作失败");
        } catch (Exception ex) {
            result = JsonHelper.buildFailed("操作失败:"+ex.getMessage());
        }
        renderText(result);
    }

    @Before(CaptchaStack.class)
    public void delPri() {
        String result;
        try {
            String userName = getPara(DBUserL.NAME);
            String dbName = getPara(DataBaseL.NAME);
            int userID = getParaToInt(DBUserL.ID);
            int spaceId = getParaToInt(DBUserL.SPACEID);
            int dbId = getParaToInt("db_id");
            boolean state = pri.revokeUserPriToDB(userName, dbName, spaceId, dbId, userID);
            result = state ? JsonHelper.buildSuccess() : JsonHelper.buildFailed();
        } catch (Exception ex) {
            result = JsonHelper.buildFailed("操作失败:"+ex.getMessage());
        }
        renderText(result);
    }

}
