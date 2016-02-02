package controller;

import aop.interceptor.LoginInterceptor;
import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import model.Account;
import model.Orders;
import service.OrdersSrv;
import util.JsonHelp;

import java.util.List;

/**
 * Created by 小龙
 * on 15-1-20
 * at 上午11:36.
 */
@ClearInterceptor(ClearLayer.ALL)
//@Before(PermitInter.class)
public class OrderController extends Controller {

    private OrdersSrv ordersSrv = OrdersSrv.me();
    //manage order  for admin

    /**
     * 查询已审核的订单
     */
    public void getChecked() {
        try {
            int number = getParaToInt("check_state");
            int currPage = getParaToInt("currPage");
            int pageSize = getParaToInt("pageSize");
            Page<Record> page = ordersSrv.getChecked(number, currPage, pageSize);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(page)));
        } catch (NullPointerException e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    /**
     * 按订单号查询订单详情
     */
    public void getDetailsByNum() {
        try {
            String num = getPara("order_num");
            List<Orders> list = ordersSrv.getDetailsByNum(num);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (NullPointerException e) {
            renderJson(JsonHelp.buildFailed());
        }

    }

    /**
     * 添加订单备注
     */
    public void addOrderRemarksByNum() {
        try {
            String num = getPara("order_num");
            String remarks = getPara("remarks");
            boolean b = ordersSrv.addOrderRemarksByNum(num, remarks);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(b)));
        } catch (NullPointerException e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    /**
     * 按条件查询    0:邮箱  1:用户真实姓名 2：订单
     */
    public void getOrderByConditions() {
        int a = getParaToInt("condition_state");
        int state = getParaToInt("check_state");
        int currPage = getParaToInt("currPage");
        int pageSize = getParaToInt("pageSize");
        String condition = getPara("condition");

        if (a == 0) {
            try {
                Page<Record> page = ordersSrv.getOrderByEmailAndState(condition, state, currPage, pageSize);
                renderJson(JsonHelp.buildSuccess(JsonKit.toJson(page)));
            } catch (NullPointerException e) {
                renderJson(JsonHelp.buildFailed());
            }
        } else if (a == 1) {
            try {
                Page<Record> page = ordersSrv.getOrderByNameAndState(condition, state, currPage, pageSize);
                renderJson(JsonHelp.buildSuccess(JsonKit.toJson(page)));
            } catch (NullPointerException e) {
                renderJson(JsonHelp.buildFailed());
            }
        } else if (a == 2) {
            try {
                Page<Record> page = ordersSrv.getOrderByNumAndState(condition, state, currPage, pageSize);
                renderJson(JsonHelp.buildSuccess(JsonKit.toJson(page)));
            } catch (NullPointerException e) {
                renderJson(JsonHelp.buildFailed());
            }
        }
    }



    /*--------------------------------------------------------*/


    /**
     * query by CheckState and Pay_state
     */
    public void queryByCstateAndPstate() {
        int check_state = getParaToInt("check_state");
        int pay_state = getParaToInt("pay_state");
        int number = getParaToInt("number");
        int size = getParaToInt("size");
        try {
            Page<Orders> orders;
            orders = ordersSrv.queryByCstateAndPstate(check_state, pay_state, number, size);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(orders)));
        } catch (Exception ex) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    /**
     * 拒绝订单
     */
    public void refusedById() {
        String id = getPara("id");
        try {
            boolean state = ordersSrv.refusedById(id);
            String result = state ? JsonHelp.buildSuccess() : JsonHelp.buildFailed();
            renderJson(result);
        } catch (Exception ex) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    /**
     * 已付款并通过
     */
    public void passByid() {
        String id = getPara("id");
        try {
            boolean state = ordersSrv.passByid(id);
            String result = state ? JsonHelp.buildSuccess() : JsonHelp.buildFailed();
            renderJson(result);
        } catch (Exception ex) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    /**
     * 扩容信息修改
     */
    public void addSpaceUpdate() {
        String id = getPara("id");
        int price = getParaToInt("price");
        int add_space = getParaToInt("add_space");
        try {
            boolean state = ordersSrv.addSpaceUpdate(id, price, add_space);
            String result = state ? JsonHelp.buildSuccess() : JsonHelp.buildFailed();
            renderJson(result);
        } catch (Exception ex) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    /**
     * 增加订单
     */
    @Before(LoginInterceptor.class)
    public void addOrder() {

        int sp = getParaToInt("add_space");
        int price = getParaToInt("price");
       Account account = (Account) getSession().getAttribute("loginUser");
        try {
            boolean state = ordersSrv.addOrder(sp, price, account);
            String result = state ? JsonHelp.buildSuccess() : JsonHelp.buildFailed();
            renderJson(result);
        } catch (Exception ex) {
            renderJson(JsonHelp.buildFailed());
        }
    }
}
