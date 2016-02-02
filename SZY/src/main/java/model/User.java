package model;

import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FormatTime;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2014/11/6.
 */
public class User extends Model<User> {
    public static final String USER_ID = "userid";
    public static final String USER_NAME = "name";
    public static final String USER_EMAIL = "email";
    public static final String USER_PWD = "pwd";
    public static final String USER_REALNAME = "realname";
    public static final String USER_TEL = "tel";
    public static final String USER_COMNAME = "comname";
    public static final String USER_CARD = "identitycard";
    public static final String USER_CLASSOFINDUSTRY = "classofindustry";
    public static final String USER_PRIMARYBUSINESS = "primarybusiness";
    public static final String USER_WEBSITEURL = "websiteurl";
    public static final String USER_ADDRESS = "address";
    public static final String USER_FIXEDLINE = "fixedline";
    public static final String USER_TIME = "time";
    public static final String USER_STATUS = "status";
    public static final String USER_AUTHENTICATION = "authentication";
    public static final String USER_SESSIONNAME = "userSession";
    public static final String USER_COOKIENAME = "userCookie";
    public static final String USER_CURRENTPAGE = "currentPage";
    public static final String USER_PAGESIZE = "pageSize";
    public static final Logger log = LoggerFactory.getLogger(User.class);
    /**
     * 普通用户操作，User
     */
    public static final User dao = new User();

    /**
     * 发送重置密码邮件时先验证邮箱和身份证号码的正确性
     * @param email
     * @param card
     * @return
     */
    public boolean checkEmailAndCard(String email,String card) {
        List<User> users = dao.find("select * from user where " + USER_EMAIL + " = " + "'" + email + "'"+" and "+ USER_CARD + " = " + "'" + card + "'");
        return !users.isEmpty();
    }
    /**
     * user 重置密码
     *
     * @updatePwd
     */
    public boolean resetPwd(String email,String newPwdMi) {
       return dao.findFirst("select * from user where "+ USER_EMAIL + " = " + "'" + email+ "'").set(User.USER_PWD, newPwdMi).update();
    }


    /**
     * 注册前需要进行邮箱是否存在检查--邮箱唯一
     *
     * @checkEmail
     */
    public boolean checkEmail(String email) {
        List<User> users = dao.find("select * from user where " + USER_EMAIL + " = " + "'" + email + "'");
        return users.isEmpty();
    }

    /**
     * 注册前需要进行用户名是否存在检查--用户名唯一
     *
     * @checkUsername
     */
    public boolean checkUsername(String username) {
        List<User> users = dao.find("select * from user where " + USER_NAME + " = " + "'" + username + "'");
        return users.isEmpty();
    }

    /**
     * 注册前需要进行用户真名是否存在检查--用户真名唯一
     *
     * @checkEmail
     */
    public boolean checkRealname(String realname) {
        List<User> users = dao.find("select * from user where " + USER_REALNAME + " = " + "'" + realname + "'");
        return users.isEmpty();
    }

    /**
     * 注册前需要进行手机号码是否存在检查--手机号码唯一
     *
     * @checkEmail
     */
    public boolean checkTel(String tel) {
        List<User> users = dao.find("select * from user where " + USER_TEL + " = " + "'" + tel + "'");
        return users.isEmpty();
    }

    public boolean checkCard(String card) {
        User users = dao.findFirst("select * from user where " + USER_CARD + " = " + "'" + card + "'");
        return users == null;
    }

    /**
     * register user
     *
     * @register
     */
    public User register(String userName, String pwdMi, String email, String realName, String tel, String comName, String card) {
        long time = System.currentTimeMillis();
        User user = new User();
        try {
            if (user.set(USER_NAME, userName).set(USER_PWD, pwdMi).set(USER_EMAIL, email).set(USER_CARD, card)
                    .set(USER_REALNAME, realName).set(USER_TEL, tel).set(USER_COMNAME, comName).set(USER_TIME, time).save()) {
                //如果插入成功，返回user
                return user;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 邮箱验证，更改authentication
     *
     * @authValidate
     */
    public boolean authValidate(int userid) {
        return new User().findById(userid).set(USER_AUTHENTICATION, 1).update();
    }

    /**
     * user login
     */
    public User login(String email) {
        return dao.findFirst("select * from user where " + USER_EMAIL + " = '" + email + "'");
    }

    /**
     * user 修改密码
     *
     * @updatePwd
     */
    public boolean updatePwd(int id, String newPwdMi) {
        return new User().set(User.USER_ID, id).set(User.USER_PWD, newPwdMi).update();
    }

    //user 修改个人资料
    public boolean modifyInfo(int id, String tel, String realName, String comName, String classofindustry,
                              String primarybusiness, String websiteurl, String addr,
                              String fixedline) {
        return new User().findById(id).set(USER_TEL, tel).set(USER_REALNAME, realName).set(USER_COMNAME, comName)
                .set(USER_CLASSOFINDUSTRY, classofindustry).set(USER_PRIMARYBUSINESS, primarybusiness)
                .set(USER_WEBSITEURL, websiteurl).set(USER_ADDRESS, addr)
                .set(USER_FIXEDLINE, fixedline).update();
    }

    //获取用户信息
    public User getUserInfo(int id) {
        List<User> userList = dao.find("select * from user where " + USER_ID + " = " + "'" + id + "'");
        formatUser(userList);
        return userList.isEmpty() ? null : userList.get(0);
    }


    /**
     * 以下为管理员对普通用户的管理操作
     * admin 修改用户状态
     */
    public boolean updateStatus(int id, int status) {
        try {
            return new User().findById(id).set(USER_STATUS, status).update();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 以下为用户的展示--列表分页展示
     */
    //全部用户列表
    public Page<User> getUsersByPage(int currentPage, int pageSize) {
        Page<User> userPage = dao.paginate(currentPage, pageSize, "select *", "from user");
        return userPage;
    }

    /**
     * 根据条件获取用户
     * email or realname
     */


    //根据用户Status获取用户
    public Page<User> getUserByStatus(int currentPage, int pageSize, int status) {
        Page<User> userPage = dao.paginate(currentPage, pageSize, "select *", "from user where " + USER_STATUS + " = ?", status);
        return userPage;
    }

    //根据用户Email获取用户
    public User getUserByEmail(String email) {
        return dao.findFirst("select * from user where " + User.USER_EMAIL + " = '" + email + "' and  status = 1 and authentication = 1");
    }

    //根据用户realname获取用户
    public User getUserByRealname(String realname) {
        List<User> userList = dao.find("select * from user where " + User.USER_REALNAME + " = " + "'" + realname + "'");
        formatUser(userList);
        return null;
    }

    //根据用户Authentication获取用户
    public String getUserByAuthentication(int authentication, int currentPage, int pageSize) {
        Page<User> userPage = dao.paginate(currentPage, pageSize, "select * ", "from user where " + USER_AUTHENTICATION + " = ?", authentication);
        String pageJson = JsonKit.toJson(userPage);
        return pageJson;
    }


    /**
     * get user for rds service
     */
    public User getUserIdByAttr(String attr, int status) {
        if (status == 0) {
            return dao.findFirst("select * from user where " + User.USER_EMAIL + " = " + "'" + attr + "'");
        }
        if (status == 1) {
            return dao.findFirst("select * from user where " + User.USER_REALNAME + " = " + "'" + attr + "'");
        }
        return null;
    }


    //format user method
    public static void formatUser(List<User> userList) {
        Iterator<User> j = userList.iterator();
        while (j.hasNext()) {
            User user = j.next();
            user.remove(USER_PWD);
            user.remove(USER_AUTHENTICATION);
            Timestamp ts = new Timestamp(user.getLong(USER_TIME));
            user.set(USER_TIME, FormatTime.time2String(ts));
        }
    }

    /**
     * get totalPages
     */
    public int getTotalPages(int pageSize, int status) {
        long totalRow = 0;
        int totalPage = 0;

        List result = null;
        if (status >= 2) {
            result = Db.query("select count(*) from user");
        } else {
            result = Db.query("select count(*) from user where " + User.USER_STATUS + " =" + status);
        }

        int size = result.size();
        if (size == 1)
            totalRow = ((Number) result.get(0)).longValue();        // totalRow = (Long)result.get(0);
        else if (size > 1)
            totalRow = result.size();
        else return totalPage;
        // totalRow = 0;

        totalPage = (int) (totalRow / pageSize);
        if (totalRow % pageSize != 0) {
            totalPage++;
        }
        return totalPage;
    }


    //test forward
    public User getUserByUserId(String userid) {
        User user = dao.findFirst("select * from user where " + User.USER_ID + " = " + userid + " and status = 1");
        return user;
    }

    public List<User> getUserByAttr(String attr) {
        return dao.find("select * from user where " + USER_EMAIL + " = " + "'" + attr + "'" + " or " + USER_REALNAME + " = " + "'" + attr + "'");
    }

    public User realmUser(String email, String pwd) {
        return dao.findFirst("select * from user where " + User.USER_EMAIL + " = " + "'" + email + "'" + " and " + User.USER_PWD + "=" + "'" + pwd + "'");
    }


    public User getUser2Resend(String email) {
        return dao.findFirst("select * from user where " + User.USER_EMAIL + " = '" + email + "' and " + User.USER_AUTHENTICATION + " = 0");
    }
}
