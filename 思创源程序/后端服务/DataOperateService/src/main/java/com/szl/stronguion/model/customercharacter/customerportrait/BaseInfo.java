package com.szl.stronguion.model.customercharacter.customerportrait;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

/**
 * Created by Tyfunwang on 2015/7/20.
 * 用户基本信息*
 */
public class BaseInfo extends Model<BaseInfo> {
    public Record getBaseInfo(int uid){
        return Db.use("main3").findFirst("select * from sl_fact_user_baseinfo where uid = '"+uid+"';");
        
    }
}
