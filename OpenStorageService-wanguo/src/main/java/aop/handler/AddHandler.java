package aop.handler;

import aop.interceptor.LoginInterceptor;
import com.jfinal.handler.Handler;
import com.jfinal.kit.HandlerKit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Tyfunwang on 2015/1/23.
 */
public class AddHandler extends Handler {
    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        response.setHeader("access-control-allow-origin", "*");
        if ("/html/.*".matches(target)) {
            LoginInterceptor.flushCache(response);
            HandlerKit.redirect("oss/view/index", request, response, isHandled);
            return;
        }
        response.setCharacterEncoding("utf-8");
        nextHandler.handle(target, request, response, isHandled);
    }
}
