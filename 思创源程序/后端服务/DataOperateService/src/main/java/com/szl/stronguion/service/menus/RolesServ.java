package com.szl.stronguion.service.menus;

import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.menus.Roles;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/6/25.
 */
public class RolesServ {
    private Roles roles = new Roles();

    //get all roles for addAccount
    public List<Roles> getRoles() {
        return roles.getRoles();
    }
    public boolean testRole(String name) {
        return roles.testRole(name);
    }

    //add role
    public boolean addRole(String name, String desc, String json) {
        return roles.addRole(name, desc, json);
    }

    //delete role
    public boolean deleteRole(int role_id) {
        return roles.deleteRole(role_id);
    }

    //search role by name or descr
    public List<Record> searchRoles(String param,int pageNumber) {
        if (param == null || "".equals(param)) {
            long totol=roles.getTotalPage(param);
            List<Record> list1=roles.searchRoles(param, pageNumber);
            if (list1.size()>0){
                list1.get(0).set("total",Math.ceil(totol/(Roles.pageSize*1.0)));
            }
            return list1;
        }
        long t2=roles.getTotalPage(param);
        List<Record> list2=roles.searchRoles(param, pageNumber);
        if (list2.size()>0){
            list2.get(0).set("total",Math.ceil(t2/(Roles.pageSize*1.0)));
        }
        return list2;
    }

    //edit role
    public boolean editRole(int role_id,String name, String desc, String json) {
        return roles.editRole(role_id,name,desc,json);
    }
    //role_id 检测该角色下是否有用户
    public boolean checkRole(int role_id) {
        //true 可以删除， false不可以删除
        return roles.checkRole(role_id).isEmpty();
    }
}
