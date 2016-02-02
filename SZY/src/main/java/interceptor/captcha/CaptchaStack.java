package interceptor.captcha;

import com.jfinal.aop.InterceptorStack;

/**
 * Created by liweiqi on 2014/10/27.
 */
public class CaptchaStack extends InterceptorStack {
    @Override
    public void config() {
        addInterceptors(new CaptchaVL(), new CaptchaIC());
    }
}
