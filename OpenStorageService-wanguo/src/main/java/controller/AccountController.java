package controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import model.Account;
import service.HdfService;
import util.EncAndDecByDES;
import util.JsonHelp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liweiqi on 2014/12/31.
 */
public class AccountController extends Controller {
    private HdfService hdfService = HdfService.getHdfsServ();
    private HdfsController hdfsController = new HdfsController();
    private EncAndDecByDES encAndDecByDES = new EncAndDecByDES();
    
    public void get() {
        try {
            Account account = (Account) getSession().getAttribute("loginUser");
            String email = account.getAccountEmail();
            String phone = account.getAccountPhone();
            String realName = account.getAccountRealName();
            Map<String, String> map = new HashMap<String, String>();
            map.put("email", encAndDecByDES.getEncString(email));
            map.put("realname", encAndDecByDES.getEncString(realName));
            map.put("phone", encAndDecByDES.getEncString(phone));
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    public void getUser(){
        try {
            Account account = (Account) getSession().getAttribute("loginUser");
            String email = account.getAccountEmail();
            String phone = account.getAccountPhone();
            String realName = account.getAccountRealName();
            String name = account.getAccountName();
            Map<String, String> map = new HashMap<String, String>();
            map.put("email", email);
            map.put("name", name);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }
    public void getUserFile() {
        //rootPath  --用户信息
        Account account = (Account) getSession().getAttribute("loginUser");
        String email = account.getAccountEmail();
        try {
            List<Map<String, String>> fileUri = hdfService.getUserFile(hdfsController.combinePath(HdfsController.HDFS_ROOT_PATH, email), account);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(fileUri)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed());
        }
    }

    public void logout() {
        getSession().removeAttribute("loginUser");

        renderJson(JsonHelp.buildSuccess());
    }
}
