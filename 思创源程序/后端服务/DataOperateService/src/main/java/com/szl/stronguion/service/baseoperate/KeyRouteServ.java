package com.szl.stronguion.service.baseoperate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.szl.stronguion.model.baseoperate.KeyNode;
import com.szl.stronguion.model.baseoperate.KeyRoute;
import com.szl.stronguion.model.baseoperate.Page;
import com.szl.stronguion.model.baseoperate.RelaRouteNode;

import java.util.List;
import java.util.Map;

/**
 * Created by Tyfunwang on 2015/7/7.
 */
public class KeyRouteServ {
    private KeyRoute keyRoute = new KeyRoute();
    private KeyNode keyNode = new KeyNode();
    private RelaRouteNode relaRouteNode = new RelaRouteNode();
    private Page page = new Page();



    //删除关键路径
    @Before(Tx.class)
    public boolean delRoutes(int route_id) {
        boolean result=false;
        boolean route_flag=keyRoute.delKeyRouteById(route_id);
        int[] node_id=relaRouteNode.delRelaRouteNodeByRouteId(route_id);
        boolean node_flag=keyNode.deleteKeyNodeByNodeId(node_id);
        if (route_flag&&node_flag){
            result=true;
        }
        return result;

    }

    public List<Record> getRoutes() {
        return keyRoute.getRoutes();
    }

    //搜索路径
    public List<Record> searchRoutes(int pageNumber,String attri) {
        if (attri == null || "".equals(attri)){
            long totol=keyRoute.getRoutesPaginateTotal().get(0).get("totolpage");
            List<Record> list1=keyRoute.getRoutesPaginate(pageNumber);
            if (list1.size()>0){
                list1.get(0).set("total",Math.ceil(totol/(KeyRoute.pageSize*1.0)));
            }
            return list1;
        }
        long t2=keyRoute.searchRoutesTotal(attri).get(0).get("totolpage");
        List<Record> list2=keyRoute.searchRoutes(pageNumber,attri);
        if (list2.size()>0){
            list2.get(0).set("total",Math.ceil(t2/(KeyRoute.pageSize*1.0)));
        }
        return list2;
    }

    //修改路径状态
    public boolean modifyState(int route_id, int state) {
        return keyRoute.modifyState(route_id, state) == 1;

    }

    //关键路经
    public List<Record> getKeyRouteDetail(String startTime, String endTime, String startPageId, String nextPageId) {
        if (keyRoute.createTemps(startTime, endTime, startPageId, nextPageId)) {
            return keyRoute.getKeyRouteDetail();
        }
        //再次尝试
        keyRoute.createTemps(startTime, endTime, startPageId, nextPageId);
        return keyRoute.getKeyRouteDetail();
    }

    /**
     * 添加，修改关键路径和节点*
     * *
     */
    public List<Record> searchPage(String param) {
        //搜索页面
        return page.searchPage(param);
    }
    public Page getPage(int param) {
        //搜索页面
        return page.getPage(param);
    }

    public List<Record> getLastRouteId() {
        //获取最大的一个route_id
        return keyRoute.getLastRouteId();
    }

    //添加路径
    @Before(Tx.class)
    public boolean addRouteNodes(String startTime, String endTime, String node, String route) {
        long time = System.currentTimeMillis();
        JSONArray array = JSONArray.parseArray(node);
        String[] nodes = new String[array.size()];

        if (keyNode.checkNodes(array)) {
            //检测node_id是否重复，重复返回false，不重复就继续执行 

            //save node
            for (int i = 0; i < array.size(); i++) {
                Map<String, Object> map2 = (Map<String, Object>) array.get(i);
                String node_id = map2.get("node_id").toString();
                nodes[i] = node_id;
                String node_name = map2.get("node_name").toString();
                String startPageId = map2.get("startPageId").toString();
                String nextPageId = map2.get("nextPageId").toString();
                if (keyNode.addNodes(startTime, endTime, node_id, node_name, startPageId, nextPageId, time) == 1)
                    continue;
                return false;
            }

            //save route
            Map<String, Object> map2 = JSONObject.parseObject(route);
            String route_id = map2.get("route_id").toString();
            String route_name = map2.get("route_name").toString();

            if (keyRoute.addRoute(startTime, endTime, route_id, route_name, time) == 1) {
                // save rela_route_node
                for (int i = 0; i < nodes.length; i++) {
                    String node_id = nodes[i];
                    if (relaRouteNode.addRelaRouteNodes(startTime, endTime, route_id, node_id, time) == 1) continue;
                    return false;
                }
            } else {
                return false;
            }

        } else {
            return false;
        }
        return true;
    }

    //修改路径
    @Before(Tx.class)
    public boolean modifyRouteNodes(String startTime, String endTime, String node, String route) {
        //先删除该route下所有nodes，然后再添加nodes
        long time = System.currentTimeMillis();
        //route
        Map<String, Object> map2 = JSONObject.parseObject(route);
        String route_id = map2.get("route_id").toString();
        String route_name = map2.get("route_name").toString();
        //node
        JSONArray array = JSONArray.parseArray(node);
        String[] nodes = new String[array.size()];

        if (keyNode.deleteNodes(route_id) >= 1) {
                //save node
                for (int i = 0; i < array.size(); i++) {
                    Map<String, Object> map3 = (Map<String, Object>) array.get(i);
                    String node_id = map3.get("node_id").toString();
                    nodes[i] = node_id;
                    String node_name = map3.get("node_name").toString();
                    String startPageId = map3.get("startPageId").toString();
                    String nextPageId = map3.get("nextPageId").toString();
                    if (keyNode.addNodes(startTime, endTime, node_id, node_name, startPageId, nextPageId, time) == 1)
                        continue;
                    return false;
                }
                //update route by route_id
                if (keyRoute.updateRoute(startTime, endTime, route_id, route_name, time) == 1) {
                    // save rela_route_node
                    for (int i = 0; i < nodes.length; i++) {
                        String node_id = nodes[i];
                        if (relaRouteNode.addRelaRouteNodes(startTime, endTime, route_id, node_id, time) == 1) continue;
                        return false;
                    }
                    return true;
                }
                return false;
            } else {
                return false;
            }
    }

}
