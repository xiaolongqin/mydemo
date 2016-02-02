package com.szl.stronguion.model.baseoperate;

import com.alibaba.fastjson.JSONArray;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;
import java.util.Map;

/**
 * Created by Tyfunwang on 2015/7/7.
 */
public class KeyNode extends Model<KeyNode> {
    private static KeyNode dao = new KeyNode();

    //根据node_id删除记录
    public boolean deleteKeyNodeByNodeId(int[] node_id) {
        boolean flag=true;
        for(int i=0;i<node_id.length;i++){
            int del=-1;
            del=Db.use("main2").update("delete from sl_rpt_key_node  where node_id = '"+node_id[i]+"';");
            if (del<0){
                return false;
            }
        }
        return flag;
    }


    //获取节点列表
    public List<Record> getNodes(String routeId) {
//        return Db.use("main2").find("select srn.* from sl_rpt_key_node srn,sl_rpt_rela_route_node srrn where srn.node_id = srrn.node_id and srrn.route_id  = '" + routeId + "';\n");
        return Db.use("main2").find("select u1.start_time, u1.end_time,u1.node_id,u1.node_name,\n" +
                "       u1.start_page_id,(case when page.page_id is null then '未知' else page.page_url end) as start_page_url,\n" +
                "       u1.next_page_id,(case when page2.page_id is null then '未知' else page2.page_url end) as next_page_url\n" +
                "\t\t\tfrom \n" +
                "(select srn.* from \n" +
                "\t\tsl_rpt_key_node srn,sl_rpt_rela_route_node srrn\n" +
                "\t\twhere srn.node_id = srrn.node_id and srrn.route_id  = '" + routeId + "'\n" +
                "\t\tGROUP BY srn.node_id ORDER BY srn.node_id) u1 left join sl_ods_page page on u1.start_page_id = page.page_id\n" +
                "                                                 left join sl_ods_page page2 on u1.next_page_id = page2.page_id" +
                "   order by u1.node_id desc;");
    }

    //获取节点对应的下级页面的名称
    public List<Record> getPageName(String routeId) {
        return Db.use("main2").find("select sop.page_name,skn.start_page_id,skn.next_page_id,skn.start_time,skn.end_time " +
                "from sl_ods_page sop,sl_rpt_key_node skn ,sl_rpt_rela_route_node srn where \n" +
                "skn.node_id = srn.node_id  and skn.next_page_id = sop.page_id and srn.route_id = '" + routeId + "'");
    }

    /**
     * 添加，修改关键路径和节点*
     * *
     */
    //获取最大的一个node_id
    public List<Record> getLastNodeId() {
        return Db.use("main2").find("select node_id  from sl_rpt_key_node  ORDER BY node_id desc LIMIT 1;");
    }

    //添加路径节点
    public int addNodes(String startTime, String endTime, String node_id, String node_name, String startPageId, String nextPageId, long time) {
        //node_status 默认1
        return Db.use("main2").update("INSERT INTO sl_rpt_key_node values (UNIX_TIMESTAMP('" + startTime + "'),UNIX_TIMESTAMP('" + endTime + "'),'"
                + node_id + "','" + node_name + "','" + startPageId + "','" + nextPageId + "','1','" + time + "');");
    }

    //修改路径节点--先删除该路径下的所有nodes，然后addNodes
    public int deleteNodes(String route_id) {
        return Db.use("main2").update("delete kn,rrn from sl_rpt_key_node kn, sl_rpt_rela_route_node rrn \n" +
                "    where kn.node_id = rrn.node_id and rrn.route_id = '"+route_id+"';");
    }
    //检测node_id是否重复，重复返回false，不重复返回true
    public boolean checkNodes(JSONArray array) {
        for (int i = 0; i < array.size(); i++) {
            Map<String, Object> map2 = (Map<String, Object>) array.get(i);
            String node_id = map2.get("node_id").toString();
            List<Record> list = Db.use("main2").find("select node_id from sl_rpt_key_node where node_id = '" + node_id + "';");
            if (!list.isEmpty()) return false;
        }
        return true;
    }
}
