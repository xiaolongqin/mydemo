package service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import model.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Administrator on 2014/11/17.
 */
public class UserService {
    /**
     * user service
     */
    User user = new User();


    private static Map<Integer, Long> loginUserSet = new HashMap<Integer, Long>();// new  ConcurrentHashMap<Integer, Long>();// HashMap<Integer, Long>();
    private static ReadWriteLock lock = new ReentrantReadWriteLock();
    private static Lock writeLock = lock.writeLock();
    private static  Lock readLock = lock.readLock();

    /**
     *check user's email and send email
     * @param email
     * @param card
     * @return
     */
    public boolean checkEmailAndCard(String email,String card) {
        return user.checkEmailAndCard(email,card);
    }

    /**
     * resetPwd
     */
    public boolean resetPwd(String email, String newPwdMi) {
        return user.resetPwd(email, newPwdMi);
    }

    /**
     * 检查信息处理
     */
    public boolean checkEmail(String email) {
        return user.checkEmail(email);
    }

    public boolean checkUsername(String username) {
        return user.checkUsername(username);
    }

    public boolean checkRealname(String realname) {
        return user.checkRealname(realname);
    }

    public boolean checkTel(String Tel) {
        return user.checkTel(Tel);
    }

    public boolean checkCard(String card) {
        return user.checkCard(card);
    }

    /**
     * user register and send
     *
     * @register
     */
    public boolean register(final String userName, final String pwdMi, final String email, final String realName, final String tel, final String comName, final String card) {
        return Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                //返回该用户的ID
                User user1 = user.register(userName, pwdMi, email, realName, tel, comName, card);
                try {
                    return user1 != null && new AuthService().sendAuthMail(user1.getInt(User.USER_ID), email);
                } catch (Exception e) {
                    return false;
                }
            }
        });

    }

    /**
     * use login
     */
    public User login(String email) {
        return user.login(email);
    }

    /**
     * updatePwd
     */
    public boolean updatePwd(int id, String newPwdMi) {
        return user.updatePwd(id, newPwdMi);
    }

    /**
     * modify personal information
     */
    public boolean modifyInfo(int id, String tel, String realName, String comName, String classofindustry,
                              String primarybusiness, String websiteurl, String addr,
                              String fixedline) {
        return user.modifyInfo(id, tel, realName, comName, classofindustry,
                primarybusiness, websiteurl, addr, fixedline);
    }

    //获取用户详细信息
    public User getUserInfo(int userid) {
        return user.getUserInfo(userid);
    }


    /**
     * 以下为对普通用户展示的操作
     * updateStatus
     */

    public boolean updateStatus(int id, int status) {
        return user.updateStatus(id, status);
    }

    /**
     * 以下为管理员对用户的操作
     * <p/>
     * show user List
     * getUsersBypage
     */
    public List<User> getUsersBypage(int currentPage, int pageSize) {
        List<User> userList = user.getUsersByPage(currentPage, pageSize).getList();
        User.formatUser(userList);
        return userList;
    }

    //get userList byStatus
    public List<User> getUsersByStatus(int currentPage, int pageSize, int status) {
        List<User> userList = user.getUserByStatus(currentPage, pageSize, status).getList();
        User.formatUser(userList);
        return userList.isEmpty() ? null : userList;
    }

    public List<User> fuzzyFindUser(String attr) {
        return user.getUserByAttr(attr);
    }

    /**
     * getUsersByAuthentication
     */
    public String getUsersByAuthentication(int authentication, int currentPage, int pageSize) {
        return user.getUserByAuthentication(authentication, currentPage, pageSize);
    }

    //get totalPage
    public int getTotal(int pageSize, int status) {
        return user.getTotalPages(pageSize, status);
    }

    /**
     * get userid for rds
     */
    public int getUserId(String attr, int status) {
        User user1 = user.getUserIdByAttr(attr, status);
        if (user1 == null) return -1;
        return user1.getInt(User.USER_ID);
    }

    public User getUserByUserId(String userid) {
        return user.getUserByUserId(userid);
    }

    //for realm
    public User getUserByEmial(String email) {
        return user.getUserByEmail(email);
    }

    public User realmUser(String email, String pwd) {
        return user.realmUser(email, pwd);
    }




    //处理集合Map<Integer,Long>
    public void addLoginUserSet(int userid) {
        try {
            writeLock.lock();
            loginUserSet.put(userid, System.currentTimeMillis());
        } finally {
            writeLock.unlock();
        }

    }

    public static void   remLoginUser(int userid) {
        try {
            writeLock.lock();
            if (loginUserSet.get(userid) != null && loginUserSet.get(userid) - System.currentTimeMillis() > 30 * 60 * 1000) {
                loginUserSet.remove(userid);
            }
        } finally {
            writeLock.unlock();
        }
    }

    public static void simpleRemLoginUser(int userid) {
        try {
            writeLock.lock();
            loginUserSet.remove(userid);
        } finally {
            writeLock.unlock();
        }
    }

    public static boolean isUserLogin(int id) {
        try{
            readLock.lock();
            return loginUserSet.containsKey(id);
        }finally {
            readLock.unlock();
        }

    }
}
