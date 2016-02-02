package com.szl.stronguion.model.baseoperate;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.utils.FormatUtils;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/7/7.
 */
public class KeyRoute extends Model<KeyRoute> {
    private static KeyRoute dao = new KeyRoute();
    public  static final int pageSize=5;


    //删除关键路径
    public boolean delKeyRouteById(int route_id) {
        int del=Db.use("main2").update("delete from sl_rpt_key_route  where route_id = '" + route_id + "';");
        return del>0?true:false;
    }

    //获取路径列表
    public List<Record> getRoutes() {
        return Db.use("main2").find("select t.route_id ,t.route_name,t.route_state," +
                "FROM_UNIXTIME(t.start_time,'%Y-%m-%d') as start_time," +
                "FROM_UNIXTIME(t.end_time,'%Y-%m-%d') as end_time " +
                "from sl_rpt_key_route t ORDER BY t.route_id desc");
    }

    //获取总页数
    public List<Record> getRoutesPaginateTotal() {
        return Db.use("main2").find("select COUNT(*) as totolpage " +
                "from sl_rpt_key_route t ORDER BY t.route_id desc");
    }
    public List<Record> searchRoutesTotal(String attri) {
        return Db.use("main2").find("select COUNT(*) as totolpage " +
                " from sl_rpt_key_route sr where sr.route_id = '" + attri + "' " +
                " or sr.route_name like '%" + attri + "%' or sr.route_state = '" + attri + "' ORDER BY sr.route_id desc");
    }


    //分页获取路径列表
    public List<Record> getRoutesPaginate(int pageNumber) {
//        return Db.use("main2").find("select t.route_id ,t.route_name,t.route_state," +
//                "FROM_UNIXTIME(t.start_time,'%Y-%m-%d') as start_time," +
//                "FROM_UNIXTIME(t.end_time,'%Y-%m-%d') as end_time " +
//                "from sl_rpt_key_route t ORDER BY t.route_id desc;");
        return Db.use("main2").paginate(pageNumber,pageSize,"select t.route_id ,t.route_name,t.route_state," +
                "FROM_UNIXTIME(t.start_time,'%Y-%m-%d') as start_time," +
                "FROM_UNIXTIME(t.end_time,'%Y-%m-%d') as end_time ","from sl_rpt_key_route t ORDER BY t.route_id desc ").getList();
    }

    //分页获取路径列表
    public List<Record> searchRoutes(int pageNumber,String attri) {
//        return Db.use("main2").find("select sr.route_id ,sr.route_name,sr.route_state," +
//                "FROM_UNIXTIME(sr.start_time,'%Y-%m-%d') as start_time," +
//                "FROM_UNIXTIME(sr.end_time,'%Y-%m-%d') as end_time " +
//                " from sl_rpt_key_route sr where sr.route_id = '" + attri + "' " +
//                " or sr.route_name = '" + attri + "' or sr.route_state = '" + attri + "' ORDER BY sr.route_id;;");
        return Db.use("main2").paginate(pageNumber,pageSize,"select sr.route_id ,sr.route_name,sr.route_state," +
                "FROM_UNIXTIME(sr.start_time,'%Y-%m-%d') as start_time," +
                "FROM_UNIXTIME(sr.end_time,'%Y-%m-%d') as end_time ",
                " from sl_rpt_key_route sr where sr.route_id = '" + attri + "' " +
                " or sr.route_name like '%" + attri + "%' or sr.route_state = '" + attri + "' ORDER BY sr.route_id desc").getList();
    }

    //修改路径状态
    public int modifyState(int route_id, int state) {
        return Db.use("main2").update("update sl_rpt_key_route set route_state = '" + state + "' where route_id = '" + route_id + "';");
    }

    /**
     * 获取关键路径*
     */

//封装sql脚本
    public boolean createTemps(String startTime, String endTime, String startPageId, String nextPageId) {
        // 取6个月样本数据
        String month1 = FormatUtils.getMonth(0);
        String month2 = FormatUtils.getMonth(-1);
        String month3 = FormatUtils.getMonth(-2);
        String month4 = FormatUtils.getMonth(-3);
        String month5 = FormatUtils.getMonth(-4);
        String month6 = FormatUtils.getMonth(-5);
        String sql01 = null;
        String sql02 = null;
        String sql11 = null;
        String sql12 = null;
        String sql31 = null;
        String sql32 = null;
        String sql41 = null;
        String sql42 = null;
        String sql51 = null;
        String sql52 = null;
        try {
            // create temp0
            sql01 = "  DROP TABLE IF EXISTS temp0_rela_route_node;";
            sql02 = "CREATE TABLE temp0_rela_route_node as\n" +
                    "        select * from sl_ods_page_visit_record_" + month6 + "\n" +
                    "        union all\n" +
                    "        select * from sl_ods_page_visit_record_" + month5 + "\n" +
                    "        union all\n" +
                    "        select * from sl_ods_page_visit_record_" + month4 + "\n" +
                    "        union all\n" +
                    "        select * from sl_ods_page_visit_record_" + month3 + "\n" +
                    "        union all\n" +
                    "        select * from sl_ods_page_visit_record_" + month2 + "\n" +
                    "        union all\n" +
                    "        select * from sl_ods_page_visit_record_" + month1 + ";";
            Db.use("main2").update(sql01);
            Db.use("main2").update(sql02);
            // create temp1
            sql11 = " DROP TABLE IF EXISTS temp1_rela_route_node;";
            sql12 = "CREATE TABLE temp1_rela_route_node as \n" +
                    "SELECT t.uid, \n" +/*用户ID*/
                    "       t.parent_id, \n" + /*上级页面ID*/
                    "       t.page_id, \n" + /*该次行为访问的页面id*/
                    "       count(*) AS page_pv,\n" + /*PV*/
                    "       count(distinct t.imei) AS page_uv,\n" + /*UV*/
                    "       round(sum(case when t.pageend_time is null or t.pageend_time = 0 then 0 " +
                    "       else TIMESTAMPDIFF(SECOND,FROM_UNIXTIME(t.pagestart_time, '%Y-%m-%d %H:%i:%S'),FROM_UNIXTIME(t.pageend_time, '%Y-%m-%d %H:%i:%S')) "+
                    "       end) /count(*),0) as avg_page_restime /*页面平均停留时间*/ " +
                    "       /*avg(TIMESTAMPDIFF(SECOND,t.pagestart_time,t.pageend_time)) as avg_page_restime,*/ \n" +
                    "  from temp0_rela_route_node t\n" +
                    "  where t.parent_id = '" + startPageId + "' and page_id = '" + nextPageId + "'\n" +
                    "    and FROM_UNIXTIME(t.pagestart_time, '%Y%m%d') >= '" + startTime + "'\n" +
                    "    and FROM_UNIXTIME(t.pagestart_time, '%Y%m%d') <= '" + endTime + "'\n" +
                    "    and (t.pagestart_time is not null or t.pagestart_time <> 0)\n" +
                    "  GROUP BY t.uid , t.parent_id, t.page_id ;";


            Db.use("main2").update(sql11);
            Db.use("main2").update(sql12);
            // create temp3 先抓取全量在网有效的用户和等级关系
            sql31 = " DROP TABLE IF EXISTS temp3_rela_route_node;";
            sql32 = "CREATE TABLE temp3_rela_route_node as \n" +
                    "        select \n" +
                    "        t.id as uid,\n" + /*用户ID*/
                    "                t.pointslevel as pointslevel \n" +/*用户等级id*/
                    "        from sl_ods_users t \n" +
                    "        where t.state in (1) \n" +
                    "        and t.id is not null \n" +
                    "        and (t.addtime is not null or t.addtime <> 0);";
            Db.use("main2").update(sql31);
            Db.use("main2").update(sql32);
            // create temp4 扩展用户等级
            sql41 = "DROP TABLE IF EXISTS temp4_rela_route_node;";
            sql42 = "CREATE TABLE temp4_rela_route_node as \n" +
                    "select t0.uid, \n" +/*用户ID*/
                    "       (case when t1.uid is null then '-1' else t1.pointslevel end) as pointslevel,\n" + /*用户等级id*/
                    "       t0.parent_id,\n" + /*上级页面ID*/
                    "       t0.page_id, \n" +/*该次行为访问的页面id*/
                    "       t0.page_pv,\n" + /*PV*/
                    "       t0.page_uv, \n" +/*UV*/
                    "       t0.avg_page_restime \n" + /*页面平均停留时间*/
                    "  from temp1_rela_route_node t0 left join temp3_rela_route_node t1 on t0.uid = t1.uid ;";
            Db.use("main2").update(sql41);
            Db.use("main2").update(sql42);
            //create temp5
        /*生成最终结果页面数据*/
            sql51 = " DROP TABLE IF EXISTS temp5_rela_route_node;";
            sql52 = "CREATE TABLE temp5_rela_route_node as \n" +
                    "        select t0.uid,\n" + /*用户ID*/
                    "                t0.pointslevel,\n" + /*用户等级id*/
                    "                (case when t.id is null or t0.pointslevel in ('-1') then '非会员' else t.levelname end) as levelname,\n" + /*等级描述*/
                    "        t0.parent_id,\n" + /*上级页面ID*/
                    "                t0.page_id, \n" +/*该次行为访问的页面id*/
                    "                t0.page_pv, /*PV*/\n" +
                    "                t0.page_uv, /*UV*/\n" +
                    "                t0.avg_page_restime \n" + /*页面平均停留时间*/
                    "        from temp4_rela_route_node t0 left join  (select * from sl_ods_user_integral_level where state in (1)) as t on t0.pointslevel = t.id;";
            Db.use("main2").update(sql51);
            Db.use("main2").update(sql52);
            return true;
        } catch (Exception e) {
            //失败后再次尝试
            try {
                Db.use("main2").update(sql01);
                Db.use("main2").update(sql02);

                Db.use("main2").update(sql11);
                Db.use("main2").update(sql12);

                Db.use("main2").update(sql31);
                Db.use("main2").update(sql32);

                Db.use("main2").update(sql41);
                Db.use("main2").update(sql42);

                Db.use("main2").update(sql51);
                Db.use("main2").update(sql52);
                return true;
            } catch (Exception e1) {
                e1.printStackTrace();
                return false;
            }

        }
    }

    //获取关键路径信息
    public List<Record> getKeyRouteDetail() {
        return Db.use("main2").find("select * from temp5_rela_route_node;");
    }

    /**
     * 添加，修改关键路径和节点*
     * *
     */

    //获取最大的一个route_id
    public List<Record> getLastRouteId() {
        return Db.use("main2").find("select route_id  from sl_rpt_key_route  ORDER BY route_id desc LIMIT 1;");
    }

    //获取最新的一个route
    public List<Record> getLastRoute() {
        return Db.use("main2").find("select * from sl_rpt_key_route ORDER BY addtime desc limit 1;");
    }

    public int addRoute(String startTime, String endTime, String route_id, String route_name, long time) {
        return Db.use("main2").update("INSERT INTO sl_rpt_key_route values (UNIX_TIMESTAMP('" + startTime + "'),UNIX_TIMESTAMP('" + endTime + "'),'" + route_id + "','" + route_name + "','1','" + time + "');");
    }


    //修改路径 by route_id
    public int updateRoute(String startTime, String endTime, String route_id, String route_name, long time) {
        return Db.use("main2").update("update sl_rpt_key_route t " +
                "set t.start_time = UNIX_TIMESTAMP('" + startTime + "') ,t.end_time = UNIX_TIMESTAMP('" + endTime + "'), t.route_name = '" + route_name + "',t.addtime = '" + time + "' " +
                "where t.route_id = '" + route_id + "'; ");
    }
}
