package com.szl.stronguion.model.menus;

import com.alibaba.fastjson.JSONArray;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Tyfunwang on 2015/6/25.
 */
public class Roles extends Model<Roles> {
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DESC = "descr";
    public static final int  pageSize = 5;

    private static Roles dao = new Roles();
    private RoleModule roleModule = new RoleModule();

    public List<Roles> getRoles() {
        return dao.find("SELECT *  from roles order by id desc");
    }

    public long getTotalPage(String param) {
         return Db.use("main1").findFirst("SELECT count(*) as total from roles where name like '%" + param + "%' or descr like '%" + param + "%';").get("total");
    }

    //search role by name or desc
    public List<Record> searchRoles(String param,int pageNumber) {
        return Db.use("main1").paginate(pageNumber,pageSize,"select *"," from roles where name like '%" + param + "%' or descr like '%" + param + "%' order by id desc").getList();
    }

    public boolean testRole(String name){
        Record record=Db.use("main1").findFirst("select * from roles WHERE name='"+name+"'");
        if (record==null){
            return false;
        }
        return record.get("name")!=null?true:false;
    }
    //add role
    public boolean addRole(final String name, final String desc, final String json) {
        return Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                boolean flag = new Roles().set(NAME, name).set(DESC, desc).save();

                if (flag) {
                    Roles roles = dao.findFirst("select * from roles GROUP BY id DESC LIMIT 0,1;");
                    if (roles != null) {
                        int id = roles.getInt(Roles.ID);
                        //保存该角色可以查看的模块
                        Iterator<Object> it = JSONArray.parseArray(json).iterator();
                        while (it.hasNext()) {
                            Iterator<Object> itt = JSONArray.parseArray(it.next().toString()).iterator();
                            while (itt.hasNext()) {
                                Map<String, Object> obj = (Map<String, Object>) itt.next();
                                String module_id = String.valueOf(obj.get(RoleModule.MODULE_ID));
                                if (roleModule.addRoleModule(id, module_id)) continue;
                                return false;
                            }

                        }
                        return true;
                    }
                    return false;
                }
                return false;
            }
        });
    }

    //delete role
    public boolean deleteRole(int role_id) {
        int i = roleModule.deleteRoleModule(role_id);
        return dao.deleteById(role_id) && (i > 0);
    }



    //修改角色
    public boolean editRole(final int role_id, final String name, final String desc, final String json) {
        return Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                //修改roles
                int flag = Db.use("main1").update("update roles set name = '" + name + "' ,descr = '" + desc + "' where id = '" + role_id + "';");
                if (flag == 1) {
                    //修改role  module ，首先删除role module 然后再添加
                    int i = roleModule.deleteRoleModule(role_id);
                    if (i > 0) {
                        //添加role module
                        Iterator<Object> it = JSONArray.parseArray(json).iterator();
                        while (it.hasNext()) {
                            Iterator<Object> itt = JSONArray.parseArray(it.next().toString()).iterator();
                            while (itt.hasNext()) {
                                Map<String, Object> obj = (Map<String, Object>) itt.next();
                                String module_id = String.valueOf(obj.get(RoleModule.MODULE_ID));
                                if (roleModule.addRoleModule(role_id, module_id)) continue;
                                return false;
                            }
                        }
                        return true;
                    }
                    return false;
                }
                return false;
            }
        });


    }

    //role_id 检测该角色下是否有用户
    public List<Roles> checkRole(int role_id) {
        return dao.find("select * from roles r,account a where a.role_id = r.id and r.id = '" + role_id + "';");
    }
}
