package com.szl.stronguion.aop.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.ext.render.CaptchaRender;
import com.szl.stronguion.controller.CaptchaController;
import com.szl.stronguion.utils.JsonHelp;

/**
 * Created by liweiqi on 2014/10/27.验证验证码是否正确
 */
public class CaptchaIC implements Interceptor {
    @Override
    public void intercept(ActionInvocation ai) {
        Controller c = ai.getController();
        String input = c.getPara("inputRandomCode").toUpperCase();
        String caKey = c.getPara(CaptchaController.CAPTCHA_KEY);
        if (!CaptchaRender.validate(c, input, caKey)) {
            String result = JsonHelp.buildFailed(" 验证码错误");
            c.renderText(result);
            return;
        }
        ai.invoke();
    }
}
