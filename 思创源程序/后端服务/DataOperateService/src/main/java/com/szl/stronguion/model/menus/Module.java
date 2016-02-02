package com.szl.stronguion.model.menus;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/6/23.
 */
public class Module extends Model<Module> {
    public static final Integer MODULES = 5; //模块个数
    public static final String CODE = "code";
    public static final String PARENTID = "parentid";
    public static final String ID = "id";
    private static Module dao = new Module();

    //get all modules for add role
    public List<Module> getModules() {
        return dao.find("select * from function_module where  `code` like '1%' and level <> 3 GROUP BY id;");
    }

    //get modules for account's menus by different roles
    public Module getAccountModules(String module_id) {
        return dao.findFirst("select * from function_module where  id = '" + module_id + "' and parentid = '00' GROUP BY id;");
    }

    //get modules for admin's menus
    public List<Module> getAdminModules(String parentid) {
        return dao.find("select * from function_module where  parentid = '" + parentid + "' GROUP BY id;");
    }

    //get next module
    public List<Module> getNextModule(String parentid,int role_id,String level) {
        int lev=Integer.valueOf(level);
        if (role_id==0){
            return dao.find("select * from function_module where  parentid = '" + parentid + "' GROUP BY id;");
        }
        if (lev==1){
            return dao.find("select rfm.module_id as id,fm.code,fm.name,fm.level,fm.url,fm.childstate FROM role_function_module rfm,function_module fm\n" +
                    "                WHERE rfm.module_id = fm.id and rfm.role_id = '"+role_id+"' and fm.parentid = '"+parentid+"' ORDER BY fm.id;");
        }else {
                return dao.find("select * from function_module where  parentid = '"+parentid+"' GROUP BY id;");
        }
    }

    public List<Record> getRoleModule(String param, int i) {
        return Db.use("main1").find("select * FROM role_function_module rfm,function_module fm" +
                " WHERE rfm.module_id = fm.id and rfm.role_id = '" + param + "' and fm.service = '" + i + "' ORDER BY fm.`level`;");
    }

    /**
     * @param id
     * @return 有页面模块的count+1*
     */
    public void addCount(int id) {
        Record record = Db.use("main1").findFirst("select id,count from function_module where id = " + id + ";");
        int count;
        try {
            count = record.getInt("count");
        } catch (Exception e) {
            count = 0;
        }

        count = count + 1;

        Db.use("main1").update("update function_module set count = " + count + " where id = " + id + ";");
    }

    //获取页面点击次数
    public List<Record> getPagesOrder() {
        return Db.use("main1").find("select id,name,count from function_module where  (url is not null or url <> 0) \n" +
                "\t\tORDER BY count desc LIMIT 3;");
    }
}
