package com.szl.stronguion.controller.menus;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.szl.stronguion.aop.interceptor.LoginInterceptor;

/**
 * Created by 小龙
 * on 15-12-17
 * at 下午4:30.
 */

public class IndexController extends Controller{
//    @ClearInterceptor(ClearLayer.ALL)
    @Before(LoginInterceptor.class)
    public void index(){
        redirect("/view/html/index.html");
    }
}
