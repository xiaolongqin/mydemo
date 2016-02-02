package filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;

import entity.Administrator;

public class AccessFilter implements Filter {
    
	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {

	request.setCharacterEncoding("utf-8");
	Map session = ActionContext.getContext().getSession();   
	Administrator admin = (Administrator) session.get("admin");
	if(admin==null){
		((HttpServletResponse)response).sendRedirect("/myMusic/syslogin.jsp");
	}else{
		filterChain.doFilter(request, response);
	}
	

	}

	public void init(FilterConfig arg0) throws ServletException {

	}

}
