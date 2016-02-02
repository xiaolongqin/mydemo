package aop.handler;

import com.jfinal.handler.Handler;
import com.jfinal.kit.HandlerKit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by liweiqi on 2014/10/15.
 */
public class LoginHandler extends Handler {
    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        HttpSession session = request.getSession();
        if (target.matches(".*/html/.*")) {
            response.setHeader("cache-control", "no-cache");
            response.setHeader("expires", "0");
            HandlerKit.redirect("/rds/main", request, response, isHandled);
            return;
        }
        if (target.equals("/login") || target.matches("/(css|js|font|img)/.*")) {
            nextHandler.handle(target, request, response, isHandled);
            return;
        }

        response.setHeader("cache-control", "no-cache");
        response.setHeader("expires", "0");
        if (target.equals("/")) {
            if (session.getAttribute("loginUser") != null) {
                HandlerKit.redirect("/rds/main", request, response, isHandled);
                return;
            }
            nextHandler.handle(target, request, response, isHandled);
            return;
        }
        if (target.equals("/main") && session.getAttribute("loginUser") == null) {
            HandlerKit.redirect("/rds/", request, response, isHandled);
            return;
        }
        if (session.getAttribute("loginUser") == null) {
            return;
        }
        nextHandler.handle(target, request, response, isHandled);

    }
}
