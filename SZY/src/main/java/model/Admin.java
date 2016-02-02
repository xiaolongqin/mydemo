package model;

import com.jfinal.plugin.activerecord.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2014/11/6.
 */
public class Admin extends Model<Admin> {
    public static final String ADMIN_ID = "adminid";
    public static final String ADMIN_NAME = "name";
    public static final String ADMIN_EMAIL = "email";
    public static final String ADMIN_PWD = "pwd";
    public static final String ADMIN_TEL = "tel";
    public static final String ADMIN_REALNAME = "realname";
    public static final String ADMIN_TYPE = "type";
    public static final String ADMIN_TIME = "time";
    public static final String ADMIN_FIXEDLINE = "fixedline";
    public static final String ADMIN_SESSIONNAME = "adminSession";
    public static final String ADMIN_COOKIENAME = "adminCookie";

    public static final Admin dao = new Admin();

    /**
     * 管理员用户操作，
     * 注册前需要进行用户名是否存在检查
     *
     * @register
     */
    //admin login
    public Admin login(String email) {
        Admin adminList = dao.findFirst("select * from admin where " + ADMIN_EMAIL + " = '" + email + "'");
        Admin admin = new Admin();
        if (admin.findFirst("select a.adminid  from admin a,rolerelation rl ,role r where a.adminid = rl.adminid and" +
                " rl.roleid = r.roleid and r.rolename = 'superadmin' and a.email = '" + email + "'") != null) {
            adminList.remove(ADMIN_PWD);
            adminList.set("type", "superadmin");
            Timestamp ts = new Timestamp(Long.valueOf(adminList.get(ADMIN_TIME).toString()));
            adminList.set(ADMIN_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ts));
            return adminList;
        }

        adminList.remove(ADMIN_PWD);
        adminList.set("type", "admin");
        Timestamp ts = new Timestamp(Long.valueOf(adminList.get(ADMIN_TIME).toString()));
        adminList.set(ADMIN_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ts));
        return adminList;
    }


    //register admin(add admin)
    public boolean register(String realname, String pwdMi, String email) {
        long time = System.currentTimeMillis();
        try {
            if (Db.findFirst("select * from user where " + User.USER_EMAIL + " = '" + email + "'") == null) {
                return new Admin().set(ADMIN_REALNAME, realname).set(ADMIN_PWD, pwdMi).set(ADMIN_EMAIL, email).set(ADMIN_TIME, time).save();
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //delete admin
    public boolean delAdmin(final int adminid) {
        return Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                return dao.deleteById(adminid) && Db.update("delete from rolerelation where " + Admin.ADMIN_ID + " = " + adminid) >= 0;
            }
        });

    }


    //获取用户信息
    public Admin getAdminInfo(int id) {
        return dao.findFirst("select * from admin where " + ADMIN_ID + " = " + "'" + id + "'");
    }
    //获取当前管理员的服务信息

    public List<Record> getAdminService(int id) {
        List<Record> list = Db.find("select s.servicename,s.serviceid , s.manservice, s.adminurl from " +
                "service s,admin a ,role r ,rolerelation rl " +
                "where r.roleid = rl.roleid and a.adminid = rl.adminid and " +
                "((a.adminid = " + id + " and rl.roleid=10) or (s.serviceid = r.serviceid and a.adminid = " + id + "))");

        return list.isEmpty() ? null : list;
    }

    //get all admins for superadmin
    public Page<Record> getAllAdmins(int currentPage, int pageSize) {
        try {
            //order by adminid desc
            Page<Record> adminList1 = Db.paginate(currentPage, pageSize, "select ro.roleid as roleid, a.adminid adminid, a.realname realname, a.email email, GROUP_CONCAT(ro.roleid,ro.servicename) as servicename",
                    "from admin a  left join rolerelation re on a.adminid = re.adminid " +
                            " left join role ro  on re.roleid = ro.roleid group by a.adminid ");

            List<Record> recordList = adminList1.getList();
            Iterator<Record> iterator = recordList.iterator();
            while (iterator.hasNext()) {
                Record r = iterator.next();
                if (r.get("roleid") != null && r.getInt("roleId") == 10) {
                    iterator.remove();
                    continue;
                }
                String service = r.getStr("servicename");
                List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
                // Map<Integer, Integer> adminIdindex = new HashedMap();
                if (service != null) {
                    String[] s = service.split(",");
                    for (int i = 0; i < s.length; i++) {
                        Map<String, String> map = new HashMap<String, String>();
                        String serviceid = s[i].substring(0, 1);
                        String servicename = s[i].substring(1, s[i].length());
                        map.put("serviceid", serviceid);
                        map.put("servicename", servicename);
                        mapList.add(map);
                    }
                    r.remove("servicename");
                    r.set("services", mapList);
                } else {
                    r.remove("servicename");
                    r.set("services", null);
                }
            }
            return adminList1;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //admin 修改个人资料
    public boolean modifyInfo(int adminid, String name, String tel, String fixedline) {
        return new Admin().findById(adminid).set(ADMIN_NAME, name).set(ADMIN_TEL, tel).set(ADMIN_FIXEDLINE, fixedline).update();
    }


    //admin 修改密码
    public boolean updatePwd(int adminid, String newPwdMi) {
        return new Admin().set(Admin.ADMIN_ID, adminid).set(Admin.ADMIN_PWD, newPwdMi).update();

    }


    public Admin getAdminByAdminId(String adminid) {
        return dao.findFirst("select * from admin where " + Admin.ADMIN_ID + " = " + adminid);
    }

    public Admin getAdminByEmail(String email) {
        return dao.findFirst("select * from admin where " + Admin.ADMIN_EMAIL + " = '" + email + "'");
    }

    public Admin realmAdmin(String email, String pwd) {
        return dao.findFirst("select * from admin where " + Admin.ADMIN_EMAIL + "=" + "'" + email + "'" + " and " + Admin.ADMIN_PWD + "=" + "'" + pwd + "'");
    }

    public List<Record> getRolesByEmail(String email) {
        List<Record> list = Db.find("select r.rolename from role r ,rolerelation rl ,admin a where" +
                " r.roleid = rl.roleid and rl.adminid = a.adminid and a.email = '" + email + "'");
        return list;
    }
}
