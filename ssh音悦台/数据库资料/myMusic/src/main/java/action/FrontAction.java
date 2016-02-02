package action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import service.FrontService;
import service.imp.FrontServiceImp;
import service.imp.MvServiceImp;
import service.imp.SingerServiceImp;
import service.imp.UserServiceImp;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import entity.Attention;
import entity.Comment;
import entity.Mv;
import entity.Recommend;
import entity.Singer;
import entity.User;
@Controller
@Scope("prototype")
public class FrontAction extends ActionSupport {

	@Resource
	private UserServiceImp userService;
	@Resource
	private FrontServiceImp frontService;
	@Resource
	private MvServiceImp mvService;
	@Resource
	private SingerServiceImp singerService;
	
	private Comment comment;
	private Attention attention;
	private String username;
	private String password;
	private User user;
	private int id;
	
	public Attention getAttention() {
		return attention;
	}
	public void setAttention(Attention attention) {
		this.attention = attention;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Comment getComment() {
		return comment;
	}
	public void setComment(Comment comment) {
		this.comment = comment;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String login()throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		User user=null;
		user =userService.login(username, password);
		if(user!=null){
			Map	session = ActionContext.getContext().getSession();
			session.put("user", user);
		}
		JSONObject jsonObject = JSONObject.fromObject(user);
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();	
		out.println(jsonObject);
		out.flush();
		out.close();
		return null;
		
	}
	public String exit()throws IOException{
		Map	session = ActionContext.getContext().getSession();
		session.remove("user");
		return null;
		
	}
	public String islogin()throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		User user=null;
		Map	session = ActionContext.getContext().getSession();
		user =(User) session.get("user");
		JSONObject jsonObject = JSONObject.fromObject(user);
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();	
		out.println(jsonObject);
		out.flush();
		out.close();
		return null;	
	}
	public String saveComment()throws IOException{
		User user=null;
		Map	session = ActionContext.getContext().getSession();
		user =(User) session.get("user");
		comment.setUser(user);
		comment.setMv(mvService.queryMvById(id));
		frontService.saveComment(comment);
		return null;	
	}
	public String saveAttention()throws IOException{
		User user=null;
		Map	session = ActionContext.getContext().getSession();
		user =(User) session.get("user");
		attention.setUser(user);
		Singer singer=singerService.querySingerById(id);
		attention.setSinger(singer);
		System.out.println(attention.getSinger().getName());
		frontService.saveAttention(attention);
		return null;	
	}
	public String queryCommentByMv() throws IOException{		
		HttpServletResponse response = ServletActionContext.getResponse();
		List<Comment> list=frontService.queryCommentByMv(mvService.queryMvById(id));
		JSONArray jsonArray = JSONArray.fromObject(list);
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();	
		out.println(jsonArray);
		out.flush();
		out.close();
		return null;		
	}
	public String queryAttentionByUser() throws IOException{		
		HttpServletResponse response = ServletActionContext.getResponse();
		Map	session = ActionContext.getContext().getSession();
		user =(User) session.get("user");
		List<Attention> list=frontService.queryAttentionByUser(user);
		JSONArray jsonArray = JSONArray.fromObject(list);
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();	
		out.println(jsonArray);
		out.flush();
		out.close();
		return null;		
	}
	public String queryAttentionBySingerAndUser() throws IOException{		
		HttpServletResponse response = ServletActionContext.getResponse();
		Map	session = ActionContext.getContext().getSession();
		user =(User) session.get("user");
		Singer singer=singerService.querySingerById(id);
		List<Attention> list=frontService.queryAttentionBySingerAndUser(singer, user);
		boolean isAttention=false;
		System.out.println(list);
		if(list.size()>0){
			isAttention=true;
		}
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();	
		out.println(isAttention);
		out.flush();
		out.close();
		return null;		
	}
}
