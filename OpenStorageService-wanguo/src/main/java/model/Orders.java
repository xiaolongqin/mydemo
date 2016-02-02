package model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * Created by 小龙
 * on 15-1-20
 * at 上午10:54.
 */

public class Orders extends Model<Orders> {
    public static Orders dao = new Orders();
    public static final String TABLE = "orders";
    public static final String ID = "order_num";
    public static final String SPACEID = "space_id";
    public static final String USERNAME = "user_realname";
    public static final String USEREMAIL = "user_email";
    public static final String USERPHONE = "user_phone";
    public static final String ORDERTIME = "order_time";
    public static final String PAYSTATE = "pay_state";//0 not pay ,1 have payed,2 is all
    public static final String CHECKSTATE = "check_state";//0 not check,1 refused ,2 ok,3 is all
    public static final String PRICE = "price";
    public static final String REMARKS = "remarks";
    public static final String ADDSPACE = "add_space";
    public static final String USEROPERATE = "user_operate";//0 not cancel ,1 is cancel


    public boolean open(String email, String phone, String realname, int spId) {
        long time = System.currentTimeMillis();
        char c = 'a';
        c = (char) (c + (int) (Math.random() * 26));

        return new Orders().set(ID, time + c).set(ORDERTIME, time).set(USEREMAIL, email).set(USERNAME, realname).
                set(USERPHONE, phone).set(REMARKS, "免费开通存储！").set(ADDSPACE, 5).set(SPACEID, spId).set(PAYSTATE, 2).set(CHECKSTATE, 2).set(PRICE, 0).save();
    }


    public static class CheckState {
        public static int DONTCHECK = 0;
        public static int REFUSED = 1;
        public static int ALLOW = 2;
    }


    public static class Paystate {
        public static int NOTPAY = 0;
        public static int PAYED = 1;
    }

    public static class UserOperate {
        public static int NOCANCEL = 0;
        public static int CANCEL = 1;
    }


    /**
     * 按check_state和订单状态查询订单
     *
     * @param check_state
     * @param pay_state
     * @param number
     * @param size
     * @return
     */
    public Page<Orders> queryByCstateAndPstate(int check_state, int pay_state, int number, int size) {
        Page<Orders> bases = null;
        if (check_state != 3 && pay_state != 2) {
            bases = dao.paginate(number, size, "select *", " from " + TABLE + " where " + CHECKSTATE + "= ? and " + PAYSTATE + "=? order by " + ORDERTIME + " desc", check_state, pay_state);
        } else if (check_state == 3 && pay_state != 2) {
            bases = dao.paginate(number, size, "select *", " from " + TABLE + " where " + PAYSTATE + "=? order by " + ORDERTIME + " desc", pay_state);
        } else if (check_state != 3 && pay_state == 2) {
            bases = dao.paginate(number, size, "select *", " from " + TABLE + " where " + CHECKSTATE + "= ? order by " + ORDERTIME + " desc", check_state);
        } else if (check_state == 3 && pay_state == 2) {
            bases = dao.paginate(number, size, "select *", " from " + TABLE + " order by " + ORDERTIME + " desc");
        }
        return bases;
    }

    /**
     * 拒绝订单
     *
     * @param id
     * @return
     */
    public boolean refusedById(String id) {
        return dao.findById(id).set(CHECKSTATE, CheckState.REFUSED).update();
    }

    /**
     * 审核通过
     *
     * @param id
     * @return
     */
    public boolean checkPassByid(String id) {
        return dao.findById(id).set(CHECKSTATE, CheckState.ALLOW).update();
    }

    /**
     * 确认已付款
     *
     * @param id
     * @return
     */
    public boolean payPassByid(String id) {
        return dao.findById(id).set(PAYSTATE, Paystate.PAYED).update();
    }

    /**
     * 取消订单
     *
     * @param id
     * @return
     */

    public boolean cancelByid(String id) {
        return dao.findById(id).set(USEROPERATE, UserOperate.CANCEL).update();
    }

    /**
     * 修改订单容量
     *
     * @param id
     * @param price
     * @param add_space
     * @return
     */
    public boolean addSpaceUpdate(String id, int price, int add_space) {
        return dao.findById(id).set(PRICE, price).set(ADDSPACE, add_space).update();
    }

    /**
     * 添加订单
     *
     * @param orders
     * @return
     */
    public boolean addOrder(Orders orders) {
        return orders.save();
    }

    /**
     * 用户按条件查询自己的订单
     *
     * @param email
     * @param index
     * @param condition
     * @param number
     * @param size
     * @return
     */

    public Page<Orders> querybyState(String email, int index, int condition, int number, int size) {
        Page<Orders> base = null;
        if (index == 0) {//all
            base = dao.paginate(number, size, "select *", " from  " + TABLE + " where " + USEREMAIL + "=?   order by  " + ORDERTIME + " desc", email);
        } else if (index == 1 || index == 2) {//1 not pay, 2 pay
            base = dao.paginate(number, size, "select *", " from " + TABLE + " where " + PAYSTATE + "=?  and " + USEREMAIL + "=?  order by " + ORDERTIME + " desc", condition, email);
        } else if (index == 3) {//is cancel
            base = dao.paginate(number, size, "select *", " from " + TABLE + " where " + USEROPERATE + "=? and  " + USEREMAIL + " =? order by " + ORDERTIME + " desc", condition, email);
        }

        return base;
    }





    /*=============================================================================================*/

    //查询已审核的订单
    public Page<Record> getChecked(int number, int currPage, int pageSize) {
        return Db.paginate(currPage, pageSize, "select " + TABLE + ".* , space.all_space,space.used_space,space.id",
                " from " + TABLE + "," + " " + Space.TABLE1 + " where " + TABLE + "." + SPACEID + " = " + Space.TABLE1 + "." + Space.ID + " and " + CHECKSTATE + " =? order by " + ORDERTIME + " desc", number);

    }

    //按订单号查询订单详情
    public List<Orders> getDetailsByNum(String num) {
        return dao.find("select " + TABLE + ".* , space.all_space,space.used_space,space.id from " + TABLE + ", " + Space.TABLE1 + " where " + TABLE + "." + SPACEID + " = " + Space.TABLE1 + "." + Space.ID + " and " + ID + " = ? order by " + ORDERTIME + " desc", num);
    }

    //添加订单备注
    public boolean addOrderRemarksByNum(String num, String remarks) {
        return dao.findById(num).set("" + REMARKS + "", remarks).update();
    }

    //按用户邮箱和审核状态查询订单
    public Page<Record> getOrderByEmailAndState(String email, int state, int currPage, int pageSize) {
        if (state != 2) {
            return Db.paginate(currPage, pageSize, "select *", " from " + TABLE + " where " + USEREMAIL + " = ? order by " + ORDERTIME + " desc", email);
        } else {
            return Db.paginate(currPage, pageSize, "select  " + TABLE + ".* , space.all_space,space.used_space,space.id",
                    " from " + TABLE + ", " + Space.TABLE1 + " where " + TABLE + "." + SPACEID + " = " +
                            Space.TABLE1 + "." + Space.ID + " and " + TABLE + "." + CHECKSTATE + " = " + state + " and " +
                            TABLE + "." + USEREMAIL + " = '" + email + "' order by " + ORDERTIME + " desc");
        }
    }


    //按用户真实姓名和审核状态查询订单
    public Page<Record> getOrderByNameAndState(String name, int state, int currPage, int pageSize) {
        if (state != 2) {
            return Db.paginate(currPage, pageSize, "select *", " from  " + TABLE + " where " + USERNAME + " = ? order by " + ORDERTIME + " desc", name);
        } else {
            return Db.paginate(currPage, pageSize, "select " + TABLE + ".* , space.all_space,space.used_space,space.id",
                    " from " + TABLE + ", " + Space.TABLE1 + " where " + TABLE + "." + SPACEID + " = " + Space.TABLE1 + "." + Space.ID + " and " + CHECKSTATE + " = ? and " + TABLE + "." + USERNAME + " =? order by " + ORDERTIME + " desc", state, name);
        }
    }


    //按订单号和审核状态查询订单
    public Page<Record> getOrderByNumAndState(String num, int state, int currPage, int pageSize) {
        if (state != 2) {
            return Db.paginate(currPage, pageSize, "select *", " from  " + TABLE + "  where  " + ID + " = ? order by " + ORDERTIME + " desc", num);
        }
        return null;
    }
}
