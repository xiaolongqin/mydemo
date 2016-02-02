package controller;

import com.jfinal.core.Controller;
import service.ServiceByTypeService;
import util.JsonHelp;
import util.PassFactory;

import java.io.IOException;

/**
 * Created by Tyfunwang on 2014/12/22.
 */
public class UtilController extends Controller {
    ServiceByTypeService serviceByType = new ServiceByTypeService();

    /**
     * UtilController use for the utils
     */

    //获取所有服务信息
    public void getService() {
        String recordList = serviceByType.allService();
        renderJson(JsonHelp.buildSuccess(recordList));
    }

    /**
     * get url for services by urltype
     * 1-9
     * 1，订单；2，服务
     */
    public void getUrl() {

    }

    //随机生成密码
    public void getRandomPwd() {
        String pwd = PassFactory.createRandomPass();
        renderJson(JsonHelp.buildSuccess("\"" + pwd + "\""));
    }

    public void test() throws IOException {
        String user = "sss";
        getResponse().sendRedirect("www.baidu.com?ac=" + user);
    }

}
