package com.szl.stronguion.controller.customercharacter;

import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.controller.menus.AccountController;
import com.szl.stronguion.model.menus.Account;
import com.szl.stronguion.service.customercharacter.*;
import com.szl.stronguion.utils.FormatUtils;
import com.szl.stronguion.utils.JsonHelp;
import com.szl.stronguion.utils.Poi;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by Tyfunwang on 2015/7/14.
 * dateType: 1:昨天，2：最近3天，3：最近7天，4：最近一个月，5：最近一季度 6:最近14天*
 * *
 */
public class LifeCycleController extends Controller {
    private ThresholdValueServ thresholdValueServ = new ThresholdValueServ();
    private LifeCycleUsersServ cycleUsersServ = new LifeCycleUsersServ();
    private LifeCycleConsumeServ cycleConsumeServ = new LifeCycleConsumeServ();
    private LifeCycleFlagServ cycleFlagServ = new LifeCycleFlagServ();
    private AllCycleUsersFlagServ allCycleFlagServ = new AllCycleUsersFlagServ();

    /**
     * 获取指标分布占比*
     * * *
     */
    public void getIndicatorPercent() {
        Account account = getSessionAttr(AccountController.ACCOUNTS);
        String id = account.getLong(Account.ID).toString();
        try {
            Map<String, Object> map = thresholdValueServ.getIndicator(id);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     * 获取消费客户群体特征分析*
     * *
     */
    public void getCustomerGroup() {
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        try {
            Map<String, Map<String, Object>> list = cycleUsersServ.getCustmorGroup(time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     * 客户习惯分析*
     */
    public void getCustHabit() {
        int paramtype = getParaToInt("paramType", 1);//1:消费客户,2:活跃客户,3:沉默,4:全部客户
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }
        try {
//            Map<String, List<Map<Integer, Object>>> list = cycleFlagServ.getCoustHabit(paramtype, time[0], time[1]);
            Map<String, List<Record>> list = cycleFlagServ.getCustHabit(paramtype, time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     * 消费客户地域分布分*
     */
    public void getCustDistribute() {
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        int paramType = getParaToInt("paramType", 1);//1: 成交金额,2:购买人数,3:成交笔数,4:客单价
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }

        try {
            List<Record> list = cycleConsumeServ.getCustDistri(time[0], time[1], paramType);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     * 客户消费top5分析*
     * *
     */
    public void getTop5Brand() {
        int dateType = getParaToInt("dateType", 1);//dateType=0 ：客户选择时间段
        String[] time = new String[2];
        if (dateType == 0) {
            time[0] = getPara("startTime");
            time[1] = getPara("endTime");
        } else {
            time = FormatUtils.getDateTime(dateType);
        }

        try {
            List<Record> list = cycleConsumeServ.getTop5(time[0], time[1]);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     * 生日祝福*
     * *
     */
    public void getBirthBless() {
        int birthType = getParaToInt("birthType");//1:明天过生，2：最近7天过生，3：最近30天过生
        try {
//            Map<String, Object> list = cycleUsersServ.getBirthBless(birthType);
            Record list = cycleUsersServ.getBirth(birthType);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    /**
     * 离网预测*
     */
    public void getCustPredict() {
        try {
            Map<String, Object> list = allCycleFlagServ.getCustPerdict();
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    //查询生日信息
    public void getBirthInfo() {
        int birthType = getParaToInt("birthType");//1:明天过生，2：最近7天过生，3：最近30天过生
        String param = getPara("param");//按照地区搜索的内容

        try {
            List<Record> list = cycleUsersServ.getPersonInfo(birthType, param);
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(list)));
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    //导出查询的用户数据
    @ClearInterceptor(ClearLayer.ALL)
    public void exportExcel() {
        int birthType = getParaToInt("birthType");//1:明天过生，2：最近7天过生，3：最近30天过生
        String param = getPara("param");//按照地区搜索的内容
        try {
            List<Record> list = cycleUsersServ.getPersonInfo(birthType, param);
            HSSFWorkbook wb = Poi.toExcel(list);
            sendFile(wb);
            return ;
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    private void sendFile(HSSFWorkbook wb) {
        try {
            HttpServletResponse response = getResponse();
            HttpServletRequest request = getRequest();
            String filename = "用户信息表.xls";//设置下载时客户端Excel的名称
            // 请见：http://zmx.javaeye.com/blog/622529
            filename = Poi.encodeFilename(filename, request);
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            OutputStream ouputStream = response.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
