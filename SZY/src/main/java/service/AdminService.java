package service;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import model.Admin;
import model.User;
import java.util.*;

/**
 * Created by Tyfunwang on 2014/12/4.
 */
public class AdminService {
    /**
     * admin service
     * */
    Admin admin = new Admin();
    User user = new User();
    //登录
    public Admin login(String email){
        return admin.login(email);
    }
    //注册
    public boolean register(String realname,String pwdMi,String email){
        return admin.register(realname,pwdMi,email);
    }


   //delete admin by adminid
    public boolean delAdmin(int adminid){
        return admin.delAdmin(adminid);
    }

    //获取用户详细信息
    public Admin getAdminInfo(int adminid){
        return admin.getAdminInfo(adminid);
    }
    //获取当前用户服务信息
    public List<Record> getAdminService(int adminid){
        return admin.getAdminService(adminid);
    }
    /**
     * modify personal information
     * */
    public boolean modifyInfo(int adminid,String name,String tel,String fixedline){
        return admin.modifyInfo(adminid,name,tel,fixedline);
    }
    //update admin pwd
    public boolean updatePwd(int adminid,String newPwdMi){return admin.updatePwd(adminid,newPwdMi);}
    //get all admins for superadmin
    public Page<Record> getAllAdmins(int currentPage,int pageSize){
            return admin.getAllAdmins(currentPage,pageSize);
    }
    public Admin getAdminByAdminId(String adminid){
        return admin.getAdminByAdminId(adminid);
    }

    //for realm
    public Admin getAdminByEmail(String email){
        return admin.getAdminByEmail(email);
    }
    public Admin realmAdmin(String email,String pwd){
        return admin.realmAdmin(email, pwd);
    }
    public List<Record> getRolesByEmail(String email){return admin.getRolesByEmail(email);}
}
