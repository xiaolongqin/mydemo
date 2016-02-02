package service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import model.local.OrderL;
import model.local.SpaceL;
import util.PassFactory;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by liweiqi on 2014/12/14.
 */
public class OrderSrv {
    private static OrderSrv srv = new OrderSrv();
    private OrderL orderDao = OrderL.dao;
    private SpaceSrv spaceSrv = SpaceSrv.me();

    public static OrderSrv me() {
        return srv;
    }

    private OrderSrv() {
    }

    /**
     * pay for the order ,add its content to the space,update both order and space table
     */
    public boolean payForOrder(final int orderid) {
        return Db.use(DSBox.LOCAL).tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                OrderL orderL = orderDao.findById(orderid);
                int spaceId = orderL.getNumber(OrderL.SPACEID).intValue();
                int addConn = orderL.getNumber(OrderL.ADDCONN).intValue();
                int addStore = orderL.getNumber(OrderL.ADDSTORE).intValue();
                int addMonth = orderL.getNumber(OrderL.ADDMONTH).intValue();
                SpaceL spaceL = spaceSrv.getById(spaceId);
                long endTime = spaceL.getLong(SpaceL.ENDTIME);
                int maxConn = spaceL.getNumber(SpaceL.MAXCONN).intValue();
                int storage = spaceL.getNumber(SpaceL.STORAGE).intValue();
                Calendar calendar = Calendar.getInstance();
                endTime = endTime > System.currentTimeMillis() ? endTime : System.currentTimeMillis();
                calendar.setTimeInMillis(endTime);
                calendar.add(Calendar.MONTH, addMonth);
                spaceL.set(SpaceL.ENDTIME, calendar.getTime())
                        .set(SpaceL.MAXCONN, maxConn + addConn)
                        .set(SpaceL.STORAGE, storage + addStore);
                if (spaceL.getNumber(SpaceL.STATE) != SpaceL.FORBIDDEN) spaceL.set(SpaceL.STATE, SpaceL.NORMAL);
                orderL.set(OrderL.STATE, OrderL.State.PAYED);
                return orderL.update() && spaceL.update();
            }
        });
    }

    public OrderL addOrder(Map<String, Object> attrs) {
        return orderDao.add(attrs);
    }

    public String generateOrderNum() {
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        return timeStamp + PassFactory.createRandomPass();
    }

    public boolean refuseOrder(final int orderid) {
        return Db.use(DSBox.LOCAL).tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                OrderL orderL = orderDao.findById(orderid);
                int spaceId = orderL.getNumber(OrderL.SPACEID).intValue();
                return orderL.set(OrderL.STATE, OrderL.State.REFUSED).update()
                        && (orderL.getNumber(OrderL.TYPE) == OrderL.Type.NEW || spaceSrv.simpleDel(spaceId));
            }
        });
    }
}
