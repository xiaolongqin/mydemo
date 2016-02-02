package com.szl.stronguion.controller.baseoperate;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.baseoperate.Page;
import com.szl.stronguion.service.baseoperate.KeyNodeServ;
import com.szl.stronguion.service.baseoperate.KeyRouteServ;
import com.szl.stronguion.utils.FormatUtils;
import com.szl.stronguion.utils.JsonHelp;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/7/7.
 */
public class KeyRouteController extends Controller {
    private KeyRouteServ keyRouteServ = new KeyRouteServ();
    private KeyNodeServ keyNodeServ = new KeyNodeServ();


    //删除关键路径
    public void delRoutes() {
        int rout_id=getParaToInt("route_id");
        try {
            boolean flag = keyRouteServ.delRoutes(rout_id);
            renderJson(flag ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));

        }
    }

    //获取全部路径列表
    public void getRoutes() {
        try {
            List<Record> list = keyRouteServ.getRoutes();
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));

        }
    }

    //搜索路径列表
    public void searchRoutes() {
        String param = getPara("name");
        int pageNumber=getParaToInt("pageNumber",1);
        try {
            List<Record> list = keyRouteServ.searchRoutes(pageNumber,param);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));

        }
    }


    public void getNodes() {
        String param = getPara("route_id");
//        String attri = "110";
        try {
            List<Record> list = keyNodeServ.getNodes(param);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    //修改页面状态
    public void modifyState() {
        int route_id = getParaToInt("route_id");
        int state = getParaToInt("state");
        try {
            boolean list = keyRouteServ.modifyState(route_id, state);
            renderJson(list ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }

    }

    //获取节点对应的页面的名称 返回名称和改节点需要的参数
    public void getPageName() {
//        String attri = getPara("attribute");
        String attri = "110";
        try {
            List<Record> list = keyNodeServ.getPageName(attri);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));

        }
    }

    /**
     * 添加关键路径和节点*
     * *
     */
    public void searchPage() {
        //搜索页面
        String param = getPara("param");
        try {
            List<Record> list = keyRouteServ.searchPage(param);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));

        }
    }
    public void getPageById() {
        //获取页面通过id
        int param = getParaToInt("id");
        try {
            Page list = keyRouteServ.getPage(param);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));

        }
    }
    public void addRouteNodes() {
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        String node = getPara("node");//包括route和node
        String route = getPara("route");//包括route和node

        try {
            if (keyRouteServ.addRouteNodes(startTime, endTime, node, route)) {
                renderJson(JsonHelp.buildSuccess());
                return;
            }
            renderJson(JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

/**
 *修改关键路径和节点*
 */
    public void modifyRouteNodes(){
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        String node = getPara("node");//包括route和node
        String route = getPara("route");//包括route和node

        try {
            if (keyRouteServ.modifyRouteNodes(startTime, endTime, node, route)) {
                renderJson(JsonHelp.buildSuccess());
                return;
            }
            renderJson(JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     * 为前台发送最大的id，返回该id+1*
     */
    //获取最大的一个node_id
    public void getLastNodeId() {
        try {
            List<Record> list = keyNodeServ.getLastNodeId();
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    //获取最大的一个route_id
    public void getLastRouteId() {
        try {
            List<Record> list = keyRouteServ.getLastRouteId();
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }
    /**
     * 关键路径分析
     * * *
     */
    public void getRouteResult() {
        long startTime = getParaToLong("startTime");
        long endTime = getParaToLong("endTime");
        String startPageId = getPara("startPageId");
        String nextPageId = getPara("nextPageId");

        try {
            List<Record> list = keyRouteServ.getKeyRouteDetail(FormatUtils.time2String(startTime), FormatUtils.time2String(endTime), startPageId, nextPageId);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));

        }
    }
}
