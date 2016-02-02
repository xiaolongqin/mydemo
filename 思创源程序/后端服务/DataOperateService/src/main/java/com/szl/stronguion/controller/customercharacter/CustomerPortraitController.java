package com.szl.stronguion.controller.customercharacter;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.service.customercharacter.CustomerPortraitServ;
import com.szl.stronguion.service.customercharacter.LifeCycleUsersServ;
import com.szl.stronguion.utils.JsonHelp;

import java.util.List;
import java.util.Map;

/**
 * Created by Tyfunwang on 2015/7/20.
 */
public class CustomerPortraitController extends Controller {
    private LifeCycleUsersServ cycleUsersServ = new LifeCycleUsersServ();
    private CustomerPortraitServ portraitServ = new CustomerPortraitServ();
    /**
     * 用户画像用户搜索*
     * * *
     */
    public void searchUser() {
        // 返回用户画像获取用户uid
        String param = getPara("param");
        //性别：男-1，女-0
        int sex = getParaToInt("sex");
        try {
            List<Record> list =  cycleUsersServ.searchUsers(param,sex);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }
    /**
     * 获取用户画像四个维度信息* 
     */
    public void getCustomPortrait(){
        //性别：男-1，女-0
//        int param = getParaToInt("uid");
//        String sex = "1";
//        try {
//            Map<String, Object> maps =  portraitServ.getCustPortrait(param,sex);
//            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(maps)));
//        } catch (Exception e) {
//            renderJson(JsonHelp.buildFailed(e.getMessage()));
//        }
    }
    /***
     *获取用户画像六个维度信息 * 
     */
    public void getCustomerPortrait(){
        int uid = getParaToInt("uid");
        try {
            Map<String, Object> maps =  portraitServ.getCustPortraits(uid);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(maps)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }
}
