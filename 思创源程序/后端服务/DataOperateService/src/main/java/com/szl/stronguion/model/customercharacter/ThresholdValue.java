package com.szl.stronguion.model.customercharacter;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;
import java.util.Map;

/**
 * Created by Tyfunwang on 2015/7/13.
 */
public class ThresholdValue extends Model<ThresholdValue> {
    public static final String TABLE = "threshold_value";
    public static final String UID = "uid";
    public static final String THRESID = "thresholdid";
    public static final String STARTTIME = "start_time";
    public static final String ENDTIME = "end_time";
    public static final String STARTLOGINNUM = "start_login_num";
    public static final String ENDLOGINNUM = "end_login_num";
    public static final String STARTORDERNUM = "start_order_num";
    public static final String ENDORDERNUM = "end_order_num";
    public static final String STARTORDERPRICE = "start_order_price";
    public static final String ENDORDERPRICE = "end_order_price";
    private static ThresholdValue dao = new ThresholdValue();

    //add threshold value
    public boolean addThresholdValue(Map<String, Object> thresvalue) {
        Record record = new Record().setColumns(thresvalue);
        return Db.use("main1").save(TABLE, record);
    }

    //获取阀值
    public List<Record> getThreshold(int id) {
        return Db.use("main1").find("select  tv.* from threshold_value tv  where tv.uid = '" + id + "' ORDER BY tv.thresholdid asc;");
    }

    //update threshold value
    public int updateThresholdValue(int id, Object threshold, Object startTime, Object endTime, Object startLoginNum, Object endLoginNum,
                                    Object startOrderNum, Object endOrderNum, Object startOrderPrice, Object endOrderPrice) {
        return Db.use("main1").update("update threshold_value tv  set  tv.start_time='" + startTime + "',tv.end_time='" + endTime + "'," +
                "tv.start_login_num='" + startLoginNum + "',tv.end_login_num='" + endLoginNum + "',\n" +
                "tv.start_order_num='" + startOrderNum + "',tv.end_order_num='" + endOrderNum + "'," +
                "tv.start_order_price='" + startOrderPrice + "',tv.end_order_price='" + endOrderPrice + "'" +
                " where tv.thresholdid = '" + threshold + "'  and tv.uid = '" + id + "';");
    }
    //获取生命周期用户总数
    public Record getTotalUser() {
        return Db.findFirst("select COUNT(uid) as total from  strongunion_online.sl_rpt_life_cycle_users");
    }



    //get  Indicator percent
    public List<Record> getIndicator(String uid, int thresholdid,String str1) {
        /*统计各个时期的占比 1-5*/
        if (thresholdid != 6) {
            return Db.find("select count(*) as fenzi from\n" +
                    "       (select slu.*\n" +
                    "        from strongunion_web.threshold_value tv ,\n" +
                    "       strongunion_online.sl_rpt_life_cycle_users slu \n" +
                    " where slu.reg_age >= tv.start_time and  slu.reg_age < tv.end_time and tv.uid = '"+uid+"' and tv.thresholdid = '"+thresholdid+"' "+str1+") g");
        }
        /*threshold = 6*/
        return Db.find("select count(*) as fenzi from\n" +
                "         (select slu.*\n" +
                "           from strongunion_web.threshold_value tv ,\n" +
                "                strongunion_online.sl_rpt_life_cycle_users slu \n" +
                "          where slu.reg_age >= tv.start_time  and tv.uid = '"+uid+"' and tv.thresholdid = '"+thresholdid+"'  "+str1+") g");
    }
}
