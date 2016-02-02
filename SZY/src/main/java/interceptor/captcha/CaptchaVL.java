package interceptor.captcha;


import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import controller.CaptchaController;
import util.JsonHelp;

/**
 * Created by liweiqi on 2014/10/27.
 */
public class CaptchaVL extends Validator {


    @Override
    protected void validate(Controller c) {
        validateRequiredString("input", CaptchaController.ERROR_KEY, "请输入验证码");
    }

    @Override
    protected void handleError(Controller c) {
        String msg = c.getAttr(CaptchaController.ERROR_KEY);
        String result = JsonHelp.buildFailed(msg);
        c.renderText(result);
    }
}
