package controller;

import com.jfinal.core.Controller;
import com.jfinal.ext.render.CaptchaRender;

/**
 * Created by liweiqi on 2014/10/27.
 */
//@RequiresRoles(value = {"admin","user"} , logical = Logical.OR)
public class CaptchaController extends Controller {
    public static final String ERROR_KEY = "error_msg";
    public void get() {
        //生成验证码
        String captcha = getPara("captcha");
        CaptchaRender render = new CaptchaRender(captcha);
        render(render);
    }
}
