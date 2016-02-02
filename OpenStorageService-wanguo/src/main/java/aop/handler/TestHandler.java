package aop.handler;

import com.jfinal.handler.Handler;
import model.Account;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2014/9/28.
 */
public class TestHandler extends Handler {


    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        response.setCharacterEncoding("utf-8");
        response.setHeader("access-control-allow-origin", "*");
        Account account = new Account();
        account.setAccountId(110);
        account.setAccountEmail("810880747@qq.com");
        account.setAccountName("which");
        account.setAccountPhone("18384113067");
        request.getSession().setAttribute("loginUser", account);
        nextHandler.handle(target, request, response, isHandled);
    }
}
