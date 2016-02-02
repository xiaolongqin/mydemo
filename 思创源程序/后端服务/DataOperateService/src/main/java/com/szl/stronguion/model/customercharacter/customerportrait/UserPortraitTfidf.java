package com.szl.stronguion.model.customercharacter.customerportrait;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/8/28.
 */
public class UserPortraitTfidf extends Model<UserPortraitTfidf> {
    public List<Record> getPortrait(int uid){
        return Db.use("main2").find("SELECT words FROM `sl_rpt_user_portrait_tfidf`" +
                " where uid = '"+uid+"' ORDER BY tfidf desc limit 3;\n");
        
    }
}
