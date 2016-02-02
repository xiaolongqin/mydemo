package service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import model.Account;
import model.Orders;
import model.Space;
import util.FormatUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by 小龙
 * on 15-1-20
 * at 上午11:38.
 */

public class OrdersSrv {

    private static OrdersSrv srv = new OrdersSrv();
    private Orders orderDao = Orders.dao;
    private Space spaceDao = Space.dao;

    public static OrdersSrv me() {
        return srv;
    }

    public Page<Record> getChecked(int number, int currPage, int pageSize) {

        Page<Record> page = orderDao.getChecked(number, currPage, pageSize);
        for (int i = 0; i < page.getList().size(); i++) {
            page.getList().get(i).set(Space.USEDSPACE, FormatUtils.formatSpace((Float) page.getList().get(i).get(Space.USEDSPACE)));
        }
        return page;
    }

    public List<Orders> getDetailsByNum(String num) {
        List<Orders> list = orderDao.getDetailsByNum(num);
        for (Orders orders : list) {

            orders.put(Orders.ORDERTIME, FormatUtils.format2S((Long) orders.get(orders.ORDERTIME)));
            orders.put(Space.USEDSPACE, FormatUtils.formatSpace((Float) orders.get(Space.USEDSPACE)));
        }

        return list;
    }

    public boolean addOrderRemarksByNum(String num, String remarks) {
        return orderDao.addOrderRemarksByNum(num, remarks);
    }

    public Page<Record> getOrderByEmailAndState(String email, int state, int currPage, int pageSize) {
        Page<Record> page = orderDao.getOrderByEmailAndState(email, state, currPage, pageSize);
        for (Record record : page.getList()) {
            record.set(Space.USEDSPACE, FormatUtils.formatSpace((Float) record.get(Space.USEDSPACE)));
        }
        return page;
    }

    public Page<Record> getOrderByNameAndState(String name, int state, int currPage, int pageSize) {
        return orderDao.getOrderByNameAndState(name, state, currPage, pageSize);
    }

    public Page<Record> getOrderByNumAndState(String num, int state, int currPage, int pageSize) {
        return orderDao.getOrderByNumAndState(num, state, currPage, pageSize);
    }

    /*-----------------------------------------------------------------*/
    public Page<Orders> queryByCstateAndPstate(int check_state, int pay_state, int number, int size) {
        Page<Orders> page = orderDao.queryByCstateAndPstate(check_state, pay_state, number, size);
        for (Orders orders : page.getList()) {
            orders.put(Orders.ORDERTIME, FormatUtils.format2S((Long) orders.get(orders.ORDERTIME)));
        }
        return page;
    }


    public boolean refusedById(String id) {
        return orderDao.refusedById(id);
    }

    public boolean passByid(final String id) {
        return Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                int space_id = orderDao.findById(id).getInt(Orders.SPACEID);
                int add = orderDao.findById(id).getInt(Orders.ADDSPACE);

                if (orderDao.checkPassByid(id) && orderDao.payPassByid(id) && spaceDao.addSpace(space_id, add)) {
                    return true;
                }
                return false;
            }
        });
    }

    public boolean addSpaceUpdate(String id, int price, int add_space) {
        return orderDao.addSpaceUpdate(id, price, add_space);
    }


    public boolean addOrder(Orders orders) {

        return orderDao.addOrder(orders);
    }


    public boolean addOrder(int sp, int price, Account account) {
        char c = 'a';
        c = (char) (c + (int) (Math.random() * 26));
        int spIDer = spaceDao.getSPId(account.getAccountEmail()).getInt(Space.ID);

        Orders orders = new Orders();
        orders.put(Orders.ORDERTIME, System.currentTimeMillis());
        orders.put(Orders.ID, System.currentTimeMillis() + "" + c);
        orders.put(Orders.ADDSPACE, sp);
        orders.put(Orders.PRICE, price);
        orders.put(Orders.SPACEID, spIDer);
        orders.put(Orders.USEREMAIL, account.getAccountEmail());
        orders.put(Orders.USERNAME, account.getAccountRealName());
        orders.put(Orders.USERPHONE, account.getAccountPhone());

        return orderDao.addOrder(orders);
    }

    public boolean open(String email, String phone, String realname) {
        int spIDer = spaceDao.getSPId(email).getInt(Space.ID);
        return orderDao.open(email, phone, realname, spIDer);
    }
}