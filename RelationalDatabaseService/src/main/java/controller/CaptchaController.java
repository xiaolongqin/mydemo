package controller;

import com.jfinal.core.Controller;
import com.jfinal.ext.render.CaptchaRender;

/**
 * Created by liweiqi on 2014/10/27.
 */
public class CaptchaController extends Controller {
    public static final String ERROR_KEY = "error_msg";
    public static final String CAPTCHA_KEY = "captcha";


    public void get() {
        String caKey = getPara(CAPTCHA_KEY);
        CaptchaRender render = new CaptchaRender(caKey);
        render(render);
    }


}
