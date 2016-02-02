package com.szl.stronguion.model.pagecontent;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.utils.FormatUtils;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/7/20.
 */
public class Temp0VisitRoute extends Model<Temp0VisitRoute> {
    private static Temp0VisitRoute dao = new Temp0VisitRoute();

    //取得当前页面对应的上下级页面
    public List<Record> getPageRelation(String startTime, String endTime, String pageName) {
       getSampleData();

        return Db.use("main2").find("select t.parent_id as parent_id, /*上级页面id*/\n" +
                "       t.page_id as page_id, /*当前页面id*/\n" +
                "       t.sub_page_id as sub_page_id /*下级页面id*/\n" +
                "  from temp0_visit_route t, sl_ods_page t0\n" +
                " where t.page_id = t0.page_id\n" +
                "   and FROM_UNIXTIME(t.pagestart_time, '%Y%m%d') >= '" + startTime + "'\n" +
                "   and FROM_UNIXTIME(t.pagestart_time, '%Y%m%d') <= '" + endTime + "'\n" +
                "   and t0.page_name = '" + pageName + "'\n" +
                "   and (t.pagestart_time is not null or t.pagestart_time <> 0)\n" +
                " group by t.parent_id, t.page_id, t.sub_page_id;");
    }

    //取得上级页面数据
    public List<Record> getParentPage(String pageName, String startTime, String endTime) {
        getSampleData();
        return Db.use("main2").find("SELECT t.parent_id,t.parent_id_name as 'page_name',t.parent_id_pv as 'page_pv',t.parent_id_uv as 'page_uv',t.parent_id_restime,\n" +
                "(CASE WHEN sum(t.parent_id_uv) is null or  0 then 0 ELSE ROUND(t.parent_id_uv*100/sum(t.parent_id_uv),2) END) as 'percent' from sl_rpt_app_page_visit t \n" +
                "  where t.page_id_name = '" + pageName + "'\n" +
                "   and FROM_UNIXTIME(t.pagestart_time, '%Y%m%d') >= '" + startTime + "'\n" +
                "   and FROM_UNIXTIME(t.pagestart_time, '%Y%m%d') <= '" + endTime + "'\n" +
                "   GROUP BY t.parent_id");
    }

    //取得当前页面数据
    public List<Record> getPage(String pageName, String startTime, String endTime) {
        getSampleData();
        return Db.use("main2").find("SELECT t.page_id_name as 'page_name', ROUND(avg(t.page_id_pv),2) as 'page_pv',ROUND(avg(t.page_id_uv),2) as 'page_uv', ROUND(avg(t.page_id_restime),2) as 'page_id_restime',\n" +
                "       (CASE WHEN avg(t.page_id_uv) is null or  0 then 0 ELSE ROUND(avg(t.page_id_uv)*100/sum(t.page_id_uv),2) END) as 'percent'\n" +
                " from sl_rpt_app_page_visit t \n" +
                "  where t.page_id_name = '"+pageName+"'\n" +
                "   and FROM_UNIXTIME(t.pagestart_time, '%Y%m%d') >= '"+startTime+"'\n" +
                "   and FROM_UNIXTIME(t.pagestart_time, '%Y%m%d') <= '"+endTime+"'\n" +
                "   GROUP BY t.page_id_name");
    }
   

    //取得下级页面数据
    public List<Record> getSubPage(String pageName, String startTime, String endTime) {
        getSampleData();
        return Db.use("main2").find("SELECT t.sub_page_id,t.sub_page_id_name as 'page_name',t.sub_page_id_pv as 'page_pv',t.sub_page_id_uv as 'page_uv',t.sub_page_restime,\n" +
                "(CASE WHEN sum(t.sub_page_id_uv) is null or  0 then 0 ELSE ROUND(t.sub_page_id_uv*100/sum(t.sub_page_id_uv),2) END) as 'percent' from sl_rpt_app_page_visit t \n" +
                "  where t.page_id_name = '"+pageName+"'\n" +
                "   and FROM_UNIXTIME(t.pagestart_time, '%Y%m%d') >= '"+startTime+"'\n" +
                "   and FROM_UNIXTIME(t.pagestart_time, '%Y%m%d') <= '"+endTime+"'\n" +
                "   GROUP BY t.parent_id");
    }


    public boolean getSampleData() {
            /*取2个月样本数据*/
        String drop = null;
        String create = null;
        try {
            drop = "DROP TABLE IF EXISTS temp0_visit_route;";
            create = "CREATE TABLE temp0_visit_route as\n" +
                    "select * from sl_ods_page_visit_record_" + FormatUtils.getMonth(0) + "\n" +
                    " union all\n" +
                    "select * from sl_ods_page_visit_record_" + FormatUtils.getMonth(-1) + " ;";
            Db.use("main2").update(drop);
            Db.use("main2").update(create);
            return true;
        } catch (Exception e) {
            try {
                    Db.use("main2").update(drop);
                Db.use("main2").update(create);
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }
    }
}
