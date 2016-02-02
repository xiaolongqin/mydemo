package filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by liweiqi on 2014/11/20.
 */
public class MyFilter extends AccessControlFilter {

    //判断请求是否允许
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return request.getAttribute("ticket") != null;

    }

    //请求拒绝
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        HttpSession session = ((HttpServletRequest)request).getSession();
        HttpServletResponse servletResponse =  ((HttpServletResponse)response);
        String url = request.getParameter("url");
        String name = request.getParameter("name");

        if (!subject.isAuthenticated()){
            //验证未通过--登录
            servletResponse.sendRedirect("http://wwww.baidu.com");
            return false;
        }

        if(url != null){
            //把Url存储在Session中
            session.setAttribute(name,url);
        }

        return true;
    }
}
