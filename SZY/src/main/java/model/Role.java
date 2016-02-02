package model;

import com.jfinal.plugin.activerecord.Model;

import java.util.List;
/**
 * Created by Tyfunwang on 2014/12/16.
 */
public class Role extends Model<Role> {
    public static final Role dao = new Role();


    //get all role
    public List<Role> getAllRoles(){
        try {
            return dao.find("select * from role where roleid < 10");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
