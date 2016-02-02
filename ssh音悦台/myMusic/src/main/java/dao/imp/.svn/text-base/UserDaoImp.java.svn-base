package dao.imp;

import java.util.List;
import java.util.Map;

import entity.User;


public interface UserDaoImp {
	//查询
	public List<User> queryUser(int page, String name);
	
	//添加
	public void addUser(User user);
	
	//分页
	public List<User> PageUser(String periods,int pageSize);
	
	//删除
	public void deleteuser(int id);
	
	//获取编辑的数据
	public void edituser(User user);
	//根据ID获取数据
	public List<User> queryUserByID(int id );
	
	//登录
	public User login(String username,String password);
	
	
}
