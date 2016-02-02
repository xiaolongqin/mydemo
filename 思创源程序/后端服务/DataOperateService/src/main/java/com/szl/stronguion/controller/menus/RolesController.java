package com.szl.stronguion.controller.menus;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.szl.stronguion.model.menus.Roles;
import com.szl.stronguion.service.menus.RolesServ;
import com.szl.stronguion.utils.JsonHelp;

/**
 * Created by Tyfunwang on 2015/6/25.
 */
public class RolesController extends Controller {
    private RolesServ rolesServ = new RolesServ();

    //add new role
    public void addRole() {
        String name = getPara(Roles.NAME);
        String desc = getPara(Roles.DESC);
        String json = getPara("json");
        if (rolesServ.testRole(name)){
            renderJson(JsonHelp.buildFailed("此角色名已经存在,请重新输入!"));
            return;
        }
        try {
            renderJson(rolesServ.addRole(name, desc, json) ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (Exception e) {
            JsonHelp.buildFailed(e.getMessage());
        }
    }
    //edit roles
    public void editRole(){
        int role_id = getParaToInt(Roles.ID);
        String name = getPara(Roles.NAME);
        String desc = getPara(Roles.DESC);
        String json = getPara("json");
        try {
            renderJson(rolesServ.editRole(role_id, name, desc, json) ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (Exception e) {
            JsonHelp.buildFailed(e.getMessage());
        }
    }
    //get all roles
    public void getRoles() {
        try {
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(rolesServ.getRoles())));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    //search role by name or desc
    public void searchRole(){
        int pageNumber=getParaToInt("pageNumber",1);
        String param = getPara("param","");
        try {
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(rolesServ.searchRoles(param,pageNumber))));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
        
    }
    //delete role
    public void deleteRoles() {
        int role_id = getParaToInt("role_id",0);
        try {
            if (role_id == 0){
                throw new Exception ("请选择角色！");
            }
            boolean flag = rolesServ.deleteRole(role_id);
            renderJson(flag ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }
    
    //role_id 检测该角色下是否有用户
    public void checkRole(){
        int role_id = getParaToInt("role_id");
        try {
            boolean flag = rolesServ.checkRole(role_id);
            renderJson(flag ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }
}
