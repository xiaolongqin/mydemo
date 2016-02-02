package com.szl.stronguion.service.menus;

import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.menus.Module;
import com.szl.stronguion.model.menus.RoleModule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Tyfunwang on 2015/6/23.
 */
public class ModuleServ {
    private Module module = new Module();
    private RoleModule roleModule = new RoleModule();

    //get all modules for add role
    public List<Module> getModules() {
        return module.getModules();
    }

    /**
     * get modules for left menus
     * * *
     */

    //get all modules for admin
    public List<Module> getAdminModules(String parentid) {
        return module.getAdminModules(parentid);
    }


    //get Module By RoleId
    public List<Module> getModuleByRoleId(int role_id) {
        List<RoleModule> roleModules = roleModule.getModuleByRoleid(role_id);
        List<Module> modules = new ArrayList<Module>();
        Iterator<RoleModule> it = roleModules.iterator();
        while (it.hasNext()) {
            String module_id = it.next().getInt(RoleModule.MODULE_ID).toString();
            Module module1 = module.getAccountModules(module_id);
            if (module1 == null) continue;
            modules.add(module1);
        }

        return modules;
    }

    //get next module
    public List<Module> getNextModule(int id, String parentid,int role_id,String level) {
        //添加count+1
        if (id != 0) module.addCount(id);
        return module.getNextModule(parentid,role_id,level);
    }

    //get role and module for edit roles
    public Object[] getRoleModule(String param) {
        //Module.MODULES 安装模块数来分开获取
        List<Record> list = new ArrayList<Record>();
        Object[] arrays = new Object[Module.MODULES];
        for (int i = 1; i <= Module.MODULES; i++) {
            arrays[i - 1] = module.getRoleModule(param, i-1);
        }
        return arrays;
    }

    //获取页面点击次数
    public List<Record> getPagesOrder() {
        return module.getPagesOrder();
    }
}
