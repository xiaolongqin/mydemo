package service;

import com.jfinal.plugin.activerecord.Page;
import model.Orders;
import model.Space;
import util.FormatUtils;

import java.util.List;

/**
 * Created by Administrator on 2015/1/28.
 */
public class UserSrv   {
    private static UserSrv srv = new UserSrv();
    private Orders orderDao = Orders.dao;
    public  static UserSrv me(){
        return  srv;
    }

    public Page<Orders>  queryByState(String  email,int index, int condition,int number,int size){
        Page<Orders> page=orderDao.querybyState(email, index, condition, number, size);
        for(Orders orders:page.getList()){
            orders.put(Orders.ORDERTIME, FormatUtils.format2S((Long) orders.get(orders.ORDERTIME)));
        }
        return page;
    }

    public boolean cancelByid(String id){
        return  orderDao.cancelByid(id);
    }

    public List<Orders> getDetailsByNum(String num){
        List<Orders> list=orderDao.getDetailsByNum(num);
        for(Orders orders:list){
            orders.put(Orders.ORDERTIME, FormatUtils.format2S((Long) orders.get(orders.ORDERTIME)));
            orders.put(Space.USEDSPACE, FormatUtils.formatSpace((Float) orders.get(Space.USEDSPACE)));
        }
        return list;
    }
}
