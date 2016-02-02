package com.szl.stronguion.model.customercharacter.customerportrait;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

/**
 * Created by Tyfunwang on 2015/8/24.
 */
public class AttentionGoods extends Model<AttentionGoods> {
    public Record getHobbyGood(int uid) {
        //用户群--喜好商品
        return Db.use("main2").findFirst("select t0.uid as uid ,/*用户id*/\n" +
                "       (case when t1.id is not null then t1.goods else '未关注商品' end) as num_1\n" +
                "  from sl_ods_attention_goods t0 left join sl_ods_goods t1 on t0.goodsid = t1.id where t0.uid = '"+uid+"';");
    }
}
