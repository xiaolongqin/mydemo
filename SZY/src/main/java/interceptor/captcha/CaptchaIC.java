package interceptor.captcha;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.ext.render.CaptchaRender;
import util.JsonHelp;

/**
 * Created by liweiqi on 2014/10/27.
 */
public class CaptchaIC implements Interceptor {
    @Override
    public void intercept(ActionInvocation ai) {
        Controller c = ai.getController();
        String input = c.getPara("input").toUpperCase();
        String caKey = c.getPara("captcha");
        //注释验证内容功能
        if (!CaptchaRender.validate(c, input, caKey)) {
//            String result = JsonHelp.buildFailed(" 验证码错误");
            c.renderJson(JsonHelp.buildFailed(" 验证码错误"));
            return;
        }
        ai.invoke();
    }
}
