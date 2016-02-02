package com.szl.stronguion.aop.handler;

import com.jfinal.handler.Handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 小龙
 * on 15-10-16
 * at 上午9:51.
 */
public class AddHandler extends Handler {
    //概念：只要协议、域名、端口有任何一个不同，都被当作是不同域。
    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
//        response.addHeader("P3P", "CP=CAO PSA OUR");//iframe
        response.setHeader("access-control-allow-origin", "*");
        response.setCharacterEncoding("utf-8");
//        if (target.startsWith("/")){
//            try {
//                response.sendRedirect("/BusinessDataService/view/html/index.html");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        nextHandler.handle(target, request, response, isHandled);


//        response.setHeader("access-control-allow-origin", "*");//允许跨域
//        response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
//        response.setHeader("Pragma", "no-cache"); //HTTP 1.0
//        response.setDateHeader("Expires", 0); //prevents caching at the proxy server
//        nextHandler.handle(target, request, response, isHandled);

    }
}
