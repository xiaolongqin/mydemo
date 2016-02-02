package com.szl.stronguion.model.baseoperate;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/7/7.
 */
public class RelaRouteNode extends Model<RelaRouteNode> {
    private static RelaRouteNode dao = new RelaRouteNode();

    //删除真是关键节点和返回对于关键节点的nodeid
    public int[] delRelaRouteNodeByRouteId(int route_id){
        List<Record> r= Db.use("main2").find("select node_id from sl_rpt_rela_route_node  where route_id = '" + route_id + "';");
        int[] nodeid = new int[r.size()];
        for(int i=0;i<r.size();i++){
          nodeid[i]=Integer.valueOf(r.get(i).get("node_id").toString());
        }
        int del=Db.use("main2").update("delete from sl_rpt_rela_route_node  where route_id = '" + route_id + "';");
        return del>0?nodeid:null;
    }

    public int addRelaRouteNodes(String startTime, String endTime, String route_id, String node_id, long time) {
        return Db.use("main2").update("INSERT  INTO sl_rpt_rela_route_node values (UNIX_TIMESTAMP('" + startTime + "'),UNIX_TIMESTAMP('" + endTime + "')" +
                ",'" + route_id + "','" + node_id + "','" + time + "');");
    }

}
