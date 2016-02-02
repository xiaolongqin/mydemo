package interceptor.get;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import model.User;
import util.JsonHelp;

/**
 * Created by Tyfunwang on 2014/12/9.
 */
public class getUserInfo implements Interceptor{

    @Override
    public void intercept(ActionInvocation ai) {
        Controller controller = ai.getController();
        try {
            User user = (User) controller.getSession().getAttribute("loginUser");
            if (user == null) {
                controller.renderJson(JsonHelp.buildFailed());
            }
            controller.renderJson(JsonHelp.buildSuccess(JsonKit.toJson(user)));
            System.out.println(user.getStr("email"));
        }catch (Exception e){
            e.printStackTrace();
            controller.renderJson(JsonHelp.buildFailed());
        }
        ai.invoke();
    }
}
