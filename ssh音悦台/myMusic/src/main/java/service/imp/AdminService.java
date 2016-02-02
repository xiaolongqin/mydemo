package service.imp;

import entity.Administrator;

public interface AdminService {
	//查询管理员
    public Administrator queryAdmin(String adminname,String password);
   //新增管理员
    public void addAdmin(Administrator admin);
}
