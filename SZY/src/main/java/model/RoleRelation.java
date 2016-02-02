package model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

/**
 * Created by Tyfunwang on 2014/12/16.
 */
public class RoleRelation extends Model<RoleRelation> {
    public static final String RELATION_ROLEID = "roleid";
    public static final String RELATION_ADMINID = "adminid";
    public static final RoleRelation dao = new RoleRelation();

    //modify admin's role by relation
    public boolean modifyRole(String serviceId, int adminid) {
        try {
            return new RoleRelation().set(RELATION_ROLEID, serviceId).set(RELATION_ADMINID, adminid).save();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //add role for admin
    public boolean addRole(String serviceId, int adminid) {
        try {
            return new RoleRelation().set(RELATION_ROLEID, serviceId).set(RELATION_ADMINID, adminid).save();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //delete role from admin
    public boolean delRole(String serviceId, int adminid) {
        try {
            int i = Db.update("delete from rolerelation where roleid = " + serviceId + " and adminid = " + adminid + "");
            return i == 1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
