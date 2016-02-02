package action;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionContext;

import entity.Administrator;
import service.imp.AdminService;

@Component("adminAction")
@Scope("prototype")
public class AdminAction {

	@Resource
	private AdminService adminService;
	
	private String adminname;
	private String password;
	private String url;
	private Administrator admin;
	private String nickname;
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Administrator getAdmin() {
		return admin;
	}
	public void setAdmin(Administrator admin) {
		this.admin = admin;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAdminname() {
		return adminname;
	}
	public void setAdminname(String adminname) {
		this.adminname = adminname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	//管理员登录
	public String login(){
		Administrator admin = adminService.queryAdmin(adminname, password);
		if(admin!=null){
			Map session = ActionContext.getContext().getSession();
			session.put("admin", admin);
			url = "/sys/index.jsp";
			return "ok";
		}
		url = "/syslogin.jsp";
		return "ok";
		
	}
	//安全退出
	public String exit(){
		Map session = ActionContext.getContext().getSession();
        session.remove("admin");
        url = "/syslogin.jsp";
		return "ok";
	}
	//管理员注册
	public String regAdmin(){
		System.out.println("name===="+admin.getName());
		System.out.println("password==="+admin.getPassword());
		adminService.addAdmin(admin);
		url = "/sysregsuccess.jsp";
		return "ok";
		
	}
	
}
