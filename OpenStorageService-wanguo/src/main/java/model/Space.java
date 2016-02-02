package model;

import com.jfinal.plugin.activerecord.Model;

import java.util.List;

/**
 * Created by 小龙
 * on 15-1-20
 * at 上午10:53.
 */

public class Space extends Model<Space> {
    public static Space dao = new Space();
    public static final String TABLE1 = "space";
    public static final String SPACENAME = "space_name";
    public static final String ID = "id";
    public static final String REMARKS = "space_remarks";
    public static final String ALLSPACE = "all_space";
    public static final String USEDSPACE = "used_space";
    public static final String USEREMAIL = "user_email";


    //update
    public boolean addSpace(int id, int add) {
        return dao.findById(id).set(ALLSPACE, new Integer(add * 1000 * 1000 * 1000).floatValue() + dao.findById(id).getFloat(ALLSPACE)).update();
    }

    //add new space
    public boolean addSpace(String email) {
        //添加一个默认开通的空间数据
        return new Space().set(USEREMAIL, email).save();
    }

    //get space
    public Space getSPId(String email) {
        return dao.findFirst("select * from space where user_email = '" + email + "'");
    }

    //upload file
    public boolean reduce(long size, String email) {
        Space space = dao.findFirst("select * from " + TABLE1 + " where " + Space.USEREMAIL + " = '" + email + "'");
        return new Space().findById(space.getInt("id")).set(Space.USEDSPACE, (space.getFloat(Space.USEDSPACE) + new Long(size).floatValue())).update();

    }

    //delete file
    public boolean updateSp(long size, String email) {
        Space space = dao.findFirst("select * from " + TABLE1 + " where " + Space.USEREMAIL + " = '" + email + "'");
        return new Space().findById(space.getInt("id")).set(Space.USEDSPACE, (space.getFloat(USEDSPACE) - new Long(size).floatValue())).update();
    }

    //check the space usedsapce
    public Space checkSp(String email) {
        return dao.findFirst("select * from " + TABLE1 + " where " + Space.USEREMAIL + " = '" + email + "'");
    }

    public List<Space> isHas(String email) {
        return dao.find("select * from " + TABLE1 + " where " + USEREMAIL + " = '" + email + "'");
    }

    //shared file move to list
    public boolean moveReduce(float size, String email) {
        Space space = dao.findFirst("select * from " + TABLE1 + " where " + Space.USEREMAIL + " = '" + email + "'");
        boolean flag = false;
        flag = new Space().findById(space.getInt("id")).set(Space.USEDSPACE, (space.getFloat(Space.USEDSPACE) + size)).update();
        return flag;
    }
}
