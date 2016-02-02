package controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import model.Admin;
import model.Role;
import service.RelationService;
import service.RoleService;
import util.JsonHelp;

import java.util.List;

/**
 * Created by Tyfunwang on 2014/12/16.
 */
public class RoleController extends Controller {
    RoleService roleService = new RoleService();
    RelationService service = new RelationService();

    /**
     * manage admins for superadmin
     */
    //get all roles
    public void getRoles() {
        List<Role> roleList = roleService.getRoles();
        renderJson(roleList != null ? JsonHelp.buildSuccess(JsonKit.toJson(roleList)) : JsonHelp.buildFailed());
    }

    //modify role of admin
    public void modifyRole() {
        String serviceId = getPara("serviceid");
        int adminid = getParaToInt(Admin.ADMIN_ID);
        renderJson(service.modifyRole(serviceId, adminid) ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());
    }
}
