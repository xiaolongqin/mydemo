package action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import service.imp.SingerServiceImp;
import utils.Common;
import utils.Support;

import com.opensymphony.xwork2.ActionContext;

import entity.Singer;


@Component("singerAction")
@Scope("prototype")//多例模式
public class SingerAction {
	@Resource
	private SingerServiceImp singerServiceIMP ;
	//新增歌手
	private Singer singer;
	private String url;
	private long totalPage;
	
	
	
	public long getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Singer getSinger() {
		return singer;
	}
	public void setSinger(Singer singer) {
		this.singer = singer;
	}
	
	private String location="/images";
	private File upload;
	private String uploadFileName;
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public File getUpload() {
		return upload;
	}
	public void setUpload(File upload) {
		this.upload = upload;
	}
	public String getUploadFileName() {
		return uploadFileName;
	}
	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	
	
	public String addSinger() throws IOException{
		System.out.println(singer.getName());
		//api  :
		
			
			HttpServletRequest request = ServletActionContext.getRequest();
			ServletContext application = request.getServletContext();
			String realpath = application.getRealPath(location);
			File file = new File(realpath);
			System.out.println(realpath);
			if(!file.exists()){
				file.mkdirs();
			}
			FileInputStream is = new FileInputStream(upload);
			FileOutputStream os = new FileOutputStream(realpath+"\\"+uploadFileName);
			byte [] b= new byte[1024];
			int len = is.read(b);
			while(len!=-1){
				os.write(b, 0, len);
				len = is.read(b);
			}
			is.close();
			os.close();
		
		singer.setPic_url("images"+"/"+uploadFileName);
		singerServiceIMP.addSinger(singer);
		url = "sys/singer/index.jsp";
		return "ok";
	}
	//编辑更新歌手
	public String upDateSinger() throws IOException{
		if(upload!=null){
		HttpServletRequest request = ServletActionContext.getRequest();
		ServletContext application = request.getServletContext();
		String realpath = application.getRealPath(location);
		File file = new File(realpath);
		System.out.println(realpath);
		if(!file.exists()){
			file.mkdirs();
		}
		FileInputStream is = new FileInputStream(upload);
		FileOutputStream os = new FileOutputStream(realpath+"\\"+uploadFileName);
		byte [] b= new byte[1024];
		int len = is.read(b);
		while(len!=-1){
			os.write(b, 0, len);
			len = is.read(b);
		}
		is.close();
		os.close();
		singer.setPic_url("images"+"/"+uploadFileName);
		}
	singerServiceIMP.upDateSinger(singer);
	url = "sys/singer/index.jsp";
	return "ok";		
	}
	
	//分页查询歌手 
	private int nowpage;
	public int getNowpage() {
		return nowpage;
	}
	public void setNowpage(int nowpage) {
		this.nowpage = nowpage;
	}
//	public String getSingerByPage(){
//		System.out.println(nowpage+"==============");
//		Support spt = singerServiceIMP.querySingerByPage(nowpage);
//		long totalPage = (spt.getTotalRecord()+Common.PageSize-1)/Common.PageSize;
//		spt.setTotalPage(totalPage);
//		Map request = (Map)ActionContext.getContext().get("request");
//		request.put("singers", spt);
//		url = "sys/singer/index.jsp";
//		return "ok";
//	}
	
	
	//通过地区查询歌手
	private String area;
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	
	public String querySingerByArea() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		List<Singer> list=singerServiceIMP.querySingerByArea(nowpage, area);
		JSONArray jsonArray = JSONArray.fromObject(list);
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();	
		out.println(jsonArray);
		out.flush();
		out.close();
		return null;
	}
	public String querySingerByAreaAndName() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		List<Singer> list=singerServiceIMP.querySingerByAreaAndName(nowpage, name, area);
		JSONArray jsonArray = JSONArray.fromObject(list);
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();	
		out.println(jsonArray);
		out.flush();
		out.close();
		return null;
	}
	
	//通过姓名查询歌手
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String querySingerByNameAndPage() throws IOException{
//		totalPage = singerServiceIMP.findTotalPage();
		HttpServletResponse response = ServletActionContext.getResponse();
		List<Singer> singers = singerServiceIMP.querySingerByNameAndPage(nowpage, name);
//		Map map=new HashMap();
//		map.put("totalPage", totalPage);
//		HttpServletRequest request=ServletActionContext.getRequest();
//		request.setAttribute("totalPage", totalPage);
		JSONArray jsonArray = JSONArray.fromObject(singers);
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.println(jsonArray);
		out.flush();
		out.close();
		return null;
	}
	//查询歌手总页数
	public String findTotalPage() throws IOException{
		long totalPage = singerServiceIMP.findTotalPage(name);
		HttpServletResponse response = ServletActionContext.getResponse();		
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.println(totalPage);
		out.flush();
		out.close();
		return null;
	}
	
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	//通过歌手ID查询歌手
	public String querySingerById() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		Singer singer=singerServiceIMP.querySingerById(id);
		JSONObject jsonArray = JSONObject.fromObject(singer);
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();	
		out.println(jsonArray);
		out.flush();
		out.close();
		return null;
	}
	//查询所有歌手
	private List<Singer> list;

	public List<Singer> getList() {
		return list;
	}
	public void setList(List<Singer> list) {
		this.list = list;
	}
	public String allSinger(){
		list = singerServiceIMP.allSingers();
		 url = "sys/singer/index.jsp";
		return "ok";
	}
	
	//删除歌手
	public String deleteSinger(){
		singerServiceIMP.deleteSinger(singer.getId());
		url = "sys/singer/index.jsp";
		return "ok";
	}
}
