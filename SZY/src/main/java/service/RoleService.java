package service;

import model.Role;
import java.util.List;
/**
 * Created by Tyfunwang on 2014/12/16.
 */
public class RoleService  {
    Role role = new Role();
    //get all role for admin
    public List<Role> getRoles(){
        return role.getAllRoles();
    }
}
