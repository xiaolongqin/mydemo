package controller;

import aop.interceptor.LoginStateInterceptor;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * Created by Tyfunwang on 2015/1/30.
 */
@Before(LoginStateInterceptor.class)
public class ViewController extends Controller {
    public void index() {
        render("/html/ossAll.html");
    }

    public void main() {
        render("/html/ossAll.html");
    }
    public void open() {
        render("/html/oss_kaitong.html");
    }


    public void res() {
        render("/html/user_response.html");
    }
}
