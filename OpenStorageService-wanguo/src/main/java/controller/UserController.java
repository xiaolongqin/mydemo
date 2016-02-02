package controller;

import aop.interceptor.PermitInter;
import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Page;
import model.Orders;
import service.UserSrv;
import util.JsonHelp;

import java.util.List;

/**
 * Created by Administrator on 2015/1/28.
 */
@ClearInterceptor(ClearLayer.ALL)
@Before(PermitInter.class)
public class UserController extends Controller {
    //manage order for user
    private UserSrv userSrv = UserSrv.me();

    /**
     * query by userOperate and Pay_state
     */
    public void queryByState() {
        int index = getParaToInt("index");
        int condition = getParaToInt("condition");
        int number = getParaToInt("number");
        int size = getParaToInt("size");
        String email = getPara("account_email");
        try {
            Page<Orders> orders;
            orders = userSrv.queryByState(email, index, condition, number, size);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(orders)));
        } catch (Exception ex) {
            ex.printStackTrace();
            renderJson(ex.getMessage());
        }

    }

    /**
     * 取消订单
     */

    public void cancelByid() {
        String id = getPara("id");

        try {
            boolean state = userSrv.cancelByid(id);
            String result = state ? JsonHelp.buildSuccess() : JsonHelp.buildFailed();
            renderJson(result);
        } catch (Exception ex) {
            renderJson(JsonHelp.buildFailed());
        }

    }

    /**
     * 用户查看订单详情
     */
    public void getDetailsByNum() {
        try {
            String num = getPara("order_num");
            List<Orders> list = userSrv.getDetailsByNum(num);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (NullPointerException e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

}

