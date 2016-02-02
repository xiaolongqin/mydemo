package com.szl.stronguion.aop.handler;

import com.jfinal.handler.Handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Tyfunwang on 2015/6/23.
 */
public class AddHandler extends Handler {
    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        response.addHeader("P3P", "CP=CAO PSA OUR");//iframe
        response.setHeader("access-control-allow-origin", "*");
        response.setCharacterEncoding("utf-8");
        nextHandler.handle(target, request, response, isHandled);
    }
}
