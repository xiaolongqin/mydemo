package com.szl.stronguion.model.customercharacter.customerportrait;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

/**
 * Created by Tyfunwang on 2015/8/24.
 */
public class Attention extends Model<Attention> {
    public Record getHobbyShop(int uid) {
        //用户群--喜好商铺
        return Db.use("main2").findFirst("select t0.uid as uid, /*用户id*/\n" +
                "       (case when t1.id is not null then t1.shopsname else '未关注店铺' end) as '喜好商铺'\n" +
                "  from sl_ods_attention t0 left join sl_ods_shops t1 on t0.shopsid = t1.id where t0.uid = '" + uid + "'  LIMIT 1;");
    }
}
