package aop.interceptor.captcha;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.ext.render.CaptchaRender;
import com.jfinal.render.JsonRender;
import controller.CaptchaController;
import util.JsonHelper;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by liweiqi on 2014/10/27.
 */
public class CaptchaIC implements Interceptor {
    @Override
    public void intercept(ActionInvocation ai) {
        Controller c = ai.getController();
        String input = c.getPara("input").toUpperCase();
        String caKey = c.getPara(CaptchaController.CAPTCHA_KEY);
        if (!CaptchaRender.validate(c, input, caKey)) {
            String result = JsonHelper.buildFailed(" 验证码错误");
            c.renderText(result);
            return;
        }
        ai.invoke();
    }
}
