package model.local;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import util.param.TransmitUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liweiqi on 2014/10/16.
 */
public class SpaceL extends Model<SpaceL> {
    public static SpaceL dao = new SpaceL();
    public final static String TABLE = "space";
    public final static String ID = "id";
    public final static String NAME = "name";
    public final static String UID = "account_id";
    public final static String DESC = "desc";
    public final static String CTIME = "ctime";
    public final static String STATE = "state";//0 normal , 1 out date,2 forbidden
    public final static String STORAGE = "storage";
    public final static String MAXCONN = "max_conn";
    public final static String ENDTIME = "end_time";
    public final static int NORMAL = 0;//0 normal , 1 out date,2 forbidden
    public final static int OUTDATE = 1;//0 normal , 1 out date,2 forbidden
    public final static int FORBIDDEN = 2;//0 normal , 1 out date,2 forbidden

    public static Map<String, Object> getAttrFromUrl(Map<String, String[]> paras) {
        Map<String, Object> attrs = new HashMap<>();
        TransmitUtil.add(attrs, paras, ID, TransmitUtil.toInteger);
        TransmitUtil.add(attrs, paras, NAME);
        TransmitUtil.add(attrs, paras, DESC);
        TransmitUtil.add(attrs, paras, STORAGE, TransmitUtil.toInteger);
        TransmitUtil.add(attrs, paras, MAXCONN, TransmitUtil.toInteger);
        TransmitUtil.add(attrs, paras, ENDTIME, TransmitUtil.toLong);
        return attrs;
    }

    public SpaceL add(Map<String, Object> attrs) {
        SpaceL spaceL = new SpaceL();
        attrs.put(CTIME, new Date().getTime());
        boolean state = spaceL.setAttrs(attrs).save();
        return state ? spaceL : null;
    }

    public Page<SpaceL> all(int uid, int number, int size) {
        Page<SpaceL> bases = dao.paginate(number, size, "select *", " from " + TABLE + " where " + UID + "=? order by " + CTIME + " desc", uid);
        return bases;
    }

    public List<SpaceL> all(int account_id) {
        List<SpaceL> bases = dao.find("select * from " + TABLE + " where " + UID + "=? order by " + STATE, account_id);
        return bases;
    }

    public List<SpaceL> allInUse() {
        return dao.find("select * from " + TABLE + " where " + STATE + "=?", NORMAL);
    }
}
