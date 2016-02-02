package com.szl.stronguion.controller.menus;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.szl.stronguion.model.menus.Account;
import com.szl.stronguion.model.menus.Module;
import com.szl.stronguion.service.menus.ModuleServ;
import com.szl.stronguion.utils.JsonHelp;

/**
 * Created by Tyfunwang on 2015/6/25.
 */
public class ModuleController extends Controller {

    private ModuleServ moduleServ = new ModuleServ();

    /**
     * show left menus
     * * */
    //get left Menus for account
    public void getLeftMenus() {
        try {
            //session 获取用户的role_id，生成菜单
            Account account = getSessionAttr(AccountController.ACCOUNTS);
            int role_id = account.getInt(Account.ROLE_ID);//string --> int
            if (0 == role_id) {
                //管理员
                renderJson(JsonHelp.buildSuccess(JsonKit.toJson(moduleServ.getAdminModules("00"))));
                return;
            }
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(moduleServ.getModuleByRoleId(role_id))));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    //get next module 
    public void getNextModule() {

        //session 获取用户的role_id，生成菜单
        Account account = getSessionAttr(AccountController.ACCOUNTS);
        int role_id = account.getInt(Account.ROLE_ID);//string --> int

        String parentid = getPara(Module.PARENTID);
        String level = getPara("level");
        int id = getParaToInt(Module.ID,0);
        try {
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(moduleServ.getNextModule(id,parentid,role_id,level))));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));

        }

    }
    //get role and module for edit roles
    public void getRoleModule(){
        String param = getPara("role_id");
        try {
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(moduleServ.getRoleModule(param))));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }
    //show  all modules for add role
    public void getFunctionModules() {
        try {
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(moduleServ.getModules())));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }
}
