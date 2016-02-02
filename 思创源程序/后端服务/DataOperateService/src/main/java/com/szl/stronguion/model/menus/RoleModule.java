package com.szl.stronguion.model.menus;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/6/25.
 */
public class RoleModule extends Model<RoleModule> {
    public static final String CODE_ID = "code_id";
    public static final String MODULE_ID = "module_id";
    private static RoleModule dao = new RoleModule();

    //get Module By RoleId
    public List<RoleModule> getModuleByRoleid(int role_id) {
        return dao.find("SELECT * from role_function_module where role_id = '" + role_id + "'  GROUP BY module_id;");
    }

    //保存新增的role对应的模块
    public boolean addRoleModule(int role_id, String module_id) {
        return new RoleModule().set("role_id", role_id).set("module_id", module_id).save();
    }

    //delete rolemodule
    public int deleteRoleModule(int role_id) {
        return Db.use("main1").update("delete from role_function_module where role_id = '"+role_id+"';");
    }

}
