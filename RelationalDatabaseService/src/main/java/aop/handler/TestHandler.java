package aop.handler;

import com.jfinal.handler.Handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2014/9/28.
 */
public class TestHandler extends Handler {


    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        response.setHeader("access-control-allow-origin", "*");
        nextHandler.handle(target, request, response, isHandled);
    }
}
