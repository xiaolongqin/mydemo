package service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import model.RoleRelation;
import model.ServiceId;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Tyfunwang on 2014/12/16.
 */
public class RelationService {
    RoleRelation relation = new RoleRelation();


    //modify admin's role by relation
    public boolean modifyRole(String serviceIds, final int adminid) {
        Gson gson = new Gson();
        final List<ServiceId> serviceidList = gson.fromJson(serviceIds, new TypeToken<List<ServiceId>>() {
        }.getType());

        boolean flag = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                //循环遍历，根据状态调用相应的方法
                for (ServiceId ser : serviceidList) {
                    if ((ser.getDer()).equals("1")) {
                        //add role for admin
                        return relation.addRole(ser.getServiceid(), adminid);
                    } else if (ser.getDer().equals("0")) {
                        //delete role from admin
                        return relation.delRole(ser.getServiceid(), adminid);
                    }
                }
                return false;
            }
        });
        return flag;
    }
}
