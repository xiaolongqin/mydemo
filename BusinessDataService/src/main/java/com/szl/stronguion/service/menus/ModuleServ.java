package com.szl.stronguion.service.menus;

import com.szl.stronguion.model.menus.Module;

import java.util.List;

/**
 * Created by 小龙
 * on 15-10-16
 * at 上午9:51.
 */
public class ModuleServ {
    private Module module = new Module();

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

    //get next module
    public List<Module> getNextModule(int id, String parentid,int role_id,String level) {
        //添加count+1
//        if (id != 0) module.addCount(id);
        return module.getNextModule(parentid,role_id,level);
    }

   /*----------------------------------------------------------------------*/

}
