package com.szl.stronguion.model.baseoperate;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;
/**
 * Created by Tyfunwang on 2015/7/2.
 */
public class OrderAim1 extends Model<OrderAim1> {
    private static OrderAim1 dao = new OrderAim1();
    public List<Record> getOrderAim1(String startTime,String endTime,String address){
        return Db.use("main2").find("select a1.shopsname,a1.province,a1.city,a1.village,SUM(a1.goods_num) goods_num, SUM(a1.orders_num) orders_num,SUM(a1.orders_total_price) order_total_price \n" +
                "       from sl_rpt_oder_any_aim1 a1\n" +
                "       where (a1.province like '%"+address+"%' or a1.city LIKE '%"+address+"%' or a1.town LIKE '%"+address+"%' or a1.village LIKE '%"+address+"%' or a1.address LIKE '%"+address+"%' or a1.shopsname LIKE '%"+address+"%') \n" +
                "       and a1.day_no >= '"+startTime+"' and a1.day_no <= '"+endTime+"' \n" +
                "       GROUP BY a1.shopsname ORDER BY a1.shopsname DESC ;");
    }
}
