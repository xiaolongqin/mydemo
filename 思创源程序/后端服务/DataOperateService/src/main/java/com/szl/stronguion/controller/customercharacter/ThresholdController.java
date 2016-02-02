package com.szl.stronguion.controller.customercharacter;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.controller.menus.AccountController;
import com.szl.stronguion.model.menus.Account;
import com.szl.stronguion.service.customercharacter.ThresholdValueServ;
import com.szl.stronguion.utils.JsonHelp;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/7/13.
 */
public class ThresholdController extends Controller {
    private ThresholdValueServ thresholdValueServ = new ThresholdValueServ();

    //获取当前用户配置的阀值
    public void getThreshold() {
        Account account = getSessionAttr(AccountController.ACCOUNTS);
        int id = account.getLong(Account.ID).intValue();
        try {
            List<Record> list = thresholdValueServ.getThreshold(id);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     * 添加阀值*
     */
    public void addThreshold() {
        Account account = getSessionAttr(AccountController.ACCOUNTS);
        int id = account.getLong(Account.ID).intValue();
        String json = getPara("json");
//        String json = "[\n" +
//                "    {\n" +
//                "        \"thresholdid\": \"1\"\n" +
//                "    },{\n" +
//                "        \"thresholdid\": \"2\",\"start_time\": \"1\", \"end_time\": \"2\",\n" +
//                "        \"start_login_num\": \"10\",\n" +
//                "        \"end_login_num\": \"20\",\n" +
//                "        \"start_order_num\": \"20\",\n" +
//                "        \"end_order_num\": \"30\",\n" +
//                "        \"start_order_price\": \"30\",\n" +
//                "        \"end_order_price\": \"40\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "        \"thresholdid\": \"3\",\n" +
//                "        \"start_time\": \"1\",\n" +
//                "        \"end_time\": \"2\",\n" +
//                "        \"start_login_num\": \"10\",\n" +
//                "        \"end_login_num\": \"20\",\n" +
//                "        \"start_order_num\": \"20\",\n" +
//                "        \"end_order_num\": \"30\",\n" +
//                "        \"start_order_price\": \"30\",\n" +
//                "        \"end_order_price\": \"40\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "        \"thresholdid\": \"4\",\n" +
//                "        \"start_time\": \"1\",\n" +
//                "        \"end_time\": \"2\",\n" +
//                "        \"start_login_num\": \"10\",\n" +
//                "        \"end_login_num\": \"20\",\n" +
//                "        \"start_order_num\": \"20\",\n" +
//                "        \"end_order_num\": \"30\",\n" +
//                "        \"start_order_price\": \"30\",\n" +
//                "        \"end_order_price\": \"40\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "        \"thresholdid\": \"5\",\n" +
//                "        \"start_time\": \"5\",\n" +
//                "        \"end_time\": \"6\",\n" +
//                "        \"start_login_num\": \"50\",\n" +
//                "        \"end_login_num\": \"60\",\n" +
//                "        \"start_order_num\": \"60\",\n" +
//                "        \"end_order_num\": \"70\",\n" +
//                "        \"start_order_price\": \"70\",\n" +
//                "        \"end_order_price\": \"80\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "        \"thresholdid\": \"6\"\n" +
//                "    }\n" +
//                "]";
        try {
            boolean flag = thresholdValueServ.addThresholdValue(id, json);
            renderJson(flag ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());

        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }

    }

    /**
     * 修改阀值*
     * *
     */
    public void updateThreshold() {
        Account account = getSessionAttr(AccountController.ACCOUNTS);
        int id = account.getLong(Account.ID).intValue();
        String json = getPara("json");
//        String json = "[\n" +
//                "    {\n" +
//                "        \"thresholdid\": \"1\"\n" +
//                "    },{\n" +
//                "        \"thresholdid\": \"2\",\"start_time\": \"1\", \"end_time\": \"2\",\n" +
//                "        \"start_login_num\": \"10\",\n" +
//                "        \"end_login_num\": \"20\",\n" +
//                "        \"start_order_num\": \"20\",\n" +
//                "        \"end_order_num\": \"30\",\n" +
//                "        \"start_order_price\": \"30\",\n" +
//                "        \"end_order_price\": \"40\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "        \"thresholdid\": \"3\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "        \"thresholdid\": \"4\",\n" +
//                "        \"start_time\": \"1\",\n" +
//                "        \"end_time\": \"2\",\n" +
//                "        \"start_login_num\": \"10\",\n" +
//                "        \"end_login_num\": \"20\",\n" +
//                "        \"start_order_num\": \"20\",\n" +
//                "        \"end_order_num\": \"30\",\n" +
//                "        \"start_order_price\": \"30\",\n" +
//                "        \"end_order_price\": \"40\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "        \"thresholdid\": \"5\",\n" +
//                "        \"start_time\": \"5\",\n" +
//                "        \"end_time\": \"6\",\n" +
//                "        \"start_login_num\": \"50\",\n" +
//                "        \"end_login_num\": \"60\",\n" +
//                "        \"start_order_num\": \"60\",\n" +
//                "        \"end_order_num\": \"70\",\n" +
//                "        \"start_order_price\": \"70\",\n" +
//                "        \"end_order_price\": \"80\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "        \"thresholdid\": \"6\",\n" +
//                "        \"start_time\": \"7\",\n" +
//                "        \"end_time\": \"8\",\n" +
//                "        \"start_login_num\": \"60\",\n" +
//                "        \"end_login_num\": \"70\",\n" +
//                "        \"start_order_num\": \"66\",\n" +
//                "        \"end_order_num\": \"77\",\n" +
//                "        \"start_order_price\": \"77\",\n" +
//                "        \"end_order_price\": \"80\"\n" +
//                "    }\n" +
//                "]";
        try {
            boolean flag = thresholdValueServ.updateThresholdValue(id, json);
            renderJson(flag ? JsonHelp.buildSuccess() : JsonHelp.buildFailed());

        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));

        }

    }


}
