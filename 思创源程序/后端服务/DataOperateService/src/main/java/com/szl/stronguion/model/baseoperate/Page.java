package com.szl.stronguion.model.baseoperate;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/6/26.
 */
public class Page extends Model<Page> {
    public static final String PAGEID = "page_id";
    public static final String PAGEURL = "page_url";
    public static final String PAGETYPE = "page_type";//int
    public static final String PAGESTATE = "page_state";//int
    public static final String PAGENAME = "page_name";//页面描述
    public static final String PAGEVERSION = "page_version";
    private static Page dao = new Page();
    public static final int  pageSize = 5;

    public List<Page> checkPageId(String id) {
        return dao.find("select * from sl_ods_page where page_id = '" + id + "';");
    }

    //add new page
    public boolean addPage(String id, String url, String type, String state, String name, String version) {
        return new Page().set(PAGEID, id).set(PAGEURL, url).set(PAGETYPE, type)
                .set(PAGESTATE, state).set(PAGENAME, name).set(PAGEVERSION, version).save();
    }

    //update page
    public int updatePageById(String id, String url, String type, String state, String name, String version) {
        return Db.use("main2").update("update sl_ods_page set page_url = '" + url + "',page_type= '" + type + "',page_state = '" + state + "'," +
                "page_name = '" + name + "', page_version = '" + version + "' where page_id = '" + id + "'");
    }

   //分页
    public long getTotalPage(String type, String name) {
        return Db.use("main2").findFirst("SELECT count(*) as total from sl_ods_page where" +
                "  page_name LIKE '%"+name+"%' or page_url like '%"+name+"%' \n" +
                "                or page_id like '%"+name+"%' and page_type = '"+type+"' ORDER BY page_id").get("total");
    }
    //search page by type and name or url or page_id
    public List<Record> searchPage(String type, String name,int pageNumber) {
        return Db.use("main2").paginate(pageNumber,pageSize,"SELECT *"," FROM sl_ods_page where page_name LIKE '%" + name + "%' or page_url like '%" + name + "%' " +
                "or page_id like '%" + name + "%' and page_type = '" + type + "' ORDER BY page_id").getList();
    }

    public long getTotalPageByType(String type) {
        return Db.use("main2").findFirst("SELECT count(*) as total from sl_ods_page where" +
                "  page_type = '" + type + "' ORDER BY page_id;").get("total");
    }
    public List<Record> searchPageByType(String type,int pageNumber) {
        return Db.use("main2").paginate(pageNumber,pageSize,"SELECT * ","FROM sl_ods_page where  page_type = '" + type + "' ORDER BY page_id").getList();
    }
    //分页



    //search page by   name or url for keyroute
    public List<Record> searchPage(String name) {
        return Db.use("main2").find("SELECT * FROM sl_ods_page where page_name LIKE '%" + name + "%' or page_url like '%" + name + "%' ORDER BY page_id;");
    }

    public boolean changePage(String id, String state) {
        return dao.findById(id, "page_id").set("page_state", state).update();
    }

    public List<Page> getPage(String id) {
        return dao.find("SELECT * from sl_ods_page where page_id = '" + id + "' ;");
    }

    //模糊搜索by page name
    public List<Page> searchByPageName(String name) {
        return dao.find("select * from sl_ods_page where page_name like '%" + name + "%' ORDER BY page_id;");
    }

    public Page getPage(int param) {
        return dao.findFirst("select * from sl_ods_page where page_id = '" + param + "' ORDER BY page_id;");
    }
}
