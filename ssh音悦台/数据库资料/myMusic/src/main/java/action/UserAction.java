package action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import entity.Mv;
import entity.Recommend;
import entity.User;
import service.imp.UserServiceImp;
@Controller
@Scope("prototype")
public class UserAction {
	@Resource
	private UserServiceImp userService;
	private String name;
	private int page;
	private User user;
	private String url;
	private int id;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}

	private List<User> users; 
	
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	//查询
	/*public String query(){//静态数据
		//userService.queryUser();
		 users =userService.queryUser(1, "");
		System.out.println(users);
		return "success";
		
	}*/
public String queryUserBynickname() throws IOException{
		
		HttpServletResponse response = ServletActionContext.getResponse();
		List<User> list=userService.queryUser(page, name);
		JSONArray jsonArray = JSONArray.fromObject(list);
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();	
		out.println(jsonArray);
		out.flush();
		out.close();
		return null;
		
	}
	
	//添加用户
	public String addUser(){
		userService.addUser(user);
		return "save_success";
	}
	//注册用户
		public String registerUser(){
			System.out.println("user.getUsername()"+user.getUsername()+""+user.getEmail());
			userService.addUser(user);
			return "register_success";
		}
	
	//分页
   public String pageuserBynickname() throws IOException{	
	HttpServletResponse response = ServletActionContext.getResponse();
	List<User> list=userService.queryUser(page, name);
		JSONArray jsonArray = JSONArray.fromObject(list);
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();	
		out.println(jsonArray);
		out.flush();
		out.close();
		return null;
		
	}
   
    //删除用户
  
  public String deleteuser(){
	   userService.deleteuser(id);
	   return null;
   }
  
  public String queryUserByID() throws IOException{
	  System.out.println("action--id"+id);
	  HttpServletResponse response = ServletActionContext.getResponse();
		List<User> list=userService.queryUserByID(id);
		
		System.out.println("actin--list"+list);
			JSONArray jsonArray = JSONArray.fromObject(list);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();	
			out.println(jsonArray);
			out.flush();
			out.close();
			return null;
  }
  
    //编辑用户
  public String edituser(){
	  userService.edituser(user);
	  return "save_success";
  }
	
}
