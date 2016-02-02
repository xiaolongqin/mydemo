package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.upload.UploadFile;
import model.FileDesc;
import org.apache.hadoop.fs.FSDataInputStream;
import service.*;
import util.EncAndDecByDES;
import util.FileRender.FileRender;
import util.FileRender.FileRenders;
import util.JsonHelp;
import util.PropertyUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tyfunwang on 2015/1/20.
 */
@ClearInterceptor(ClearLayer.ALL)
public class HdfsController extends Controller {
    private RpcService rpcService = RpcService.getRpcSer();
    private HdfService hdfService = HdfService.getHdfsServ();
    private LoadService loadService = LoadService.getInstance();
    private FileDescSrv fileDescSrv = FileDescSrv.getInstance();
    public static final String HDFS_USER_TYPE = "user";
    public static final String HDFS_SHARED_TYPE = "shared";
    public static final String ACCOUNTEMAIL = "email";
    public static final String ACCOUNTPHONE = "tel";
    public static final String ACCOUNTREALNAME = "realname";
    public static final String DIRNAME = "dirUrl";
    public static final String DST_DIR = "dstDir";
    public static final String ACTIONTYPE = "type";
    public static final String OLD_FILE = "old_file";
    public static final String NEW_FILE = "new_file";
    public static final String DST_NAME = "dst_email";
    //    public static final String LOADPATH = "d:" + File.separator;
    public static String LOADPATH = null;
    private static String ROOTPATH = null;
    public static String HDFS_ROOT_PATH = null;
    public static String HDFS_SHARED_PATH = null;
    public static String SZY_URL = null;
    public static String MSG_URL = null;
    private SpaceServ serv = SpaceServ.me();
    private EncAndDecByDES encAndDecByDES = new EncAndDecByDES();

    {
        try {

            ROOTPATH = PropertyUtil.urls.getProperty("downurl");
            HDFS_ROOT_PATH = rpcService.getFileSys().getUri() + "/oss/user/";
            HDFS_SHARED_PATH = rpcService.getFileSys().getUri() + "/oss/shared/";
            SZY_URL = PropertyUtil.urls.getProperty("szyurl");
            MSG_URL = PropertyUtil.urls.getProperty("msgurl");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * action for users
     */
    //openServ
    //@Before(LoginInterceptor.class)
    public void openServ() {
        //open hdfs service and set the space on table
        String nameM = getPara(ACCOUNTEMAIL);
        String phoneM = getPara(ACCOUNTPHONE);
        String realnameM = getPara(ACCOUNTREALNAME);
        String name = encAndDecByDES.getDesString(nameM);
        String phone = encAndDecByDES.getDesString(phoneM);
        String realname = encAndDecByDES.getDesString(realnameM);

        try {
            if (hdfService.openService(combinePath(HDFS_ROOT_PATH, name), combinePath(HDFS_SHARED_PATH, name), name, phone, realname)) {
                renderJson(JsonHelp.buildSuccess());
                return;
            }
            renderJson(JsonHelp.buildFailed());
        } catch (Exception e) {

            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    //make directory
    public void mkdir() {
        String name = getPara(ACCOUNTEMAIL);
        String dirName = getPara(DIRNAME);
        try {
            if (hdfService.makeDir(combinePath(HDFS_ROOT_PATH, name), dirName)) {
                renderJson(JsonHelp.buildSuccess());
                return;
            }
            renderJson(JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    //delete
    public void delete() {
        String type = getPara(ACTIONTYPE);
        String name = getPara(ACCOUNTEMAIL);
        String pathName = getPara(DIRNAME);

        try {
            if (type.equals(HDFS_USER_TYPE)) {
                if (hdfService.deleteAll(HDFS_ROOT_PATH, name, pathName)) {
                    renderJson(JsonHelp.buildSuccess());
                    return;
                }
                renderJson(JsonHelp.buildFailed());
            } else if (type.equals(HDFS_SHARED_TYPE)) {
                if (hdfService.del4Shared(HDFS_SHARED_PATH, name, pathName)) {
                    renderJson(JsonHelp.buildSuccess());
                    return;
                }
                renderJson(JsonHelp.buildFailed());
            }
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    //delete user
    public void delUser() {
        String name = "Administrator";
        try {
            if (hdfService.delUser(combinePath(HDFS_ROOT_PATH, name))) {
                renderJson(JsonHelp.buildSuccess());
                return;
            }
            renderJson(JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        }
    }

    //rename directory or file
    public void rename() {
        String name = getPara(ACCOUNTEMAIL);
        String old_st = getPara(OLD_FILE);
        String new_st = getPara(NEW_FILE);
        try {
            if (hdfService.rename(combinePath(HDFS_ROOT_PATH, name), old_st, new_st)) {
                renderJson(JsonHelp.buildSuccess());
                return;
            }
            renderJson(JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    //move file and directory
    public void mvFile() {
        String type = getPara(ACTIONTYPE);
        String email = getPara(ACCOUNTEMAIL);
        String src_f = getPara(DIRNAME);
        String dst_f = getPara(DST_DIR);
        String size = getPara("size");//要移动的文件大小
        try {
            if (type.equals(HDFS_USER_TYPE)) {
                if (hdfService.   moveFile(combinePath(HDFS_ROOT_PATH, email), src_f, dst_f)) {
                    renderJson(JsonHelp.buildSuccess());
                    return;
                }
                renderJson(JsonHelp.buildFailed());
            } else if (type.equals(HDFS_SHARED_TYPE)) {
                if (hdfService.moveFile(combinePath(HDFS_SHARED_PATH, email), src_f, dst_f)) {
                    renderJson(JsonHelp.buildSuccess());
                    return;
                }
                renderJson(JsonHelp.buildFailed());
            } else {
                //move shared file to my list
                //shared-->user-->
                if (hdfService.moveFiles(combinePath(HDFS_SHARED_PATH, email), src_f, dst_f, combinePath(HDFS_ROOT_PATH, email), size, email)) {
                    renderJson(JsonHelp.buildSuccess());
                    return;
                }
                renderJson(JsonHelp.buildFailed());
            }
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    //move shared file to my list
//    public void sharedMv2List(){
//        String email = getPara(ACCOUNTEMAIL);
//        String src_f = getPara(DIRNAME);
//        String dst_f = getPara(DST_DIR);
//        String size = getPara("size");//要移动的文件大小
//        try {
//                //shared-->user-->
//                if (hdfService.moveFiles(combinePath(HDFS_SHARED_PATH, email), src_f, dst_f, combinePath(HDFS_ROOT_PATH, email), size,email)) {
//                    renderJson(JsonHelp.buildSuccess());
//                    return;
//                }
//                renderJson(JsonHelp.buildFailed());
//        } catch (Exception e) {
//            renderJson(JsonHelp.buildFailed(e.getMessage()));
//        }
//    }

    /**
     * add
     */

    public void isHas() {
        String email = getPara("email");
        if (serv.isHas(email)) {
            renderText("true");
            return;
        } else {
            renderText("false");
            return;
        }
    }

    //getNextFile获取给定目录下全部的子目录:包括根目录
    public void getNextFile() {
        String type = getPara(ACTIONTYPE);//type
        String name = getPara(ACCOUNTEMAIL);//accountName
        String dirName = getPara(DIRNAME);//:/one/
        try {
            List<Map<String, String>> fileUri = null;

            if (type.equals(HDFS_USER_TYPE)) {
                fileUri = hdfService.getNextFile(combinePath(HDFS_ROOT_PATH, name), name, dirName);
            } else if (type.equals(HDFS_SHARED_TYPE)) {
                fileUri = hdfService.getNextFile(combinePath(HDFS_SHARED_PATH, name), name, dirName);
            }
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(fileUri)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    //getDir获取给定目录下的子文件夹
    public void getDir() {
        String type = getPara(ACTIONTYPE);
        String email = getPara(ACCOUNTEMAIL);
        String dirName = getPara(DIRNAME);
        String dst_f = getPara(DST_DIR);
        try {
            List<Map<String, String>> fileUri = null;
            if (type.equals(HDFS_USER_TYPE)) {
                fileUri = hdfService.getDir(combinePath(HDFS_ROOT_PATH, email), dirName, dst_f);
            } else if (type.equals(HDFS_SHARED_TYPE)) {
                fileUri = hdfService.getDir(combinePath(HDFS_SHARED_PATH, email), dirName, dst_f);
            } else {
                fileUri = hdfService.getDirs(combinePath(HDFS_ROOT_PATH, email), dirName, dst_f);
            }
            renderJson(JsonHelp.buildSuccess(JsonKit.toJson(fileUri)));
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    //单个分享
    public void sharedFile() {
        //szy需要判断是否本地用户，如果不是，发送下载链接
        String src_name = getPara(ACCOUNTEMAIL);
        String dst_name = getPara(DST_NAME);
        String src_s = getPara(DIRNAME);
        try {
            JSONObject jsonObject = JSON.parseObject(dst_name);
            JSONArray jsonArray = jsonObject.getJSONArray(DST_NAME);
            String dst_email = jsonArray.getString(0);

            // check email
            Map<String, String[]> map = new HashMap<String, String[]>();
            map.put("dst_email", new String[]{dst_email});
            map.put("src_email", new String[]{src_name});
            map.put("src_f", new String[]{src_s});
            String url = SZY_URL + "check";

            //检查邮箱
            if (!EmailServ.me().check(url, map)) {
                if (!src_name.equals(dst_email)) {
                    //开始分享
                    renderJson(hdfService.shareFiles(combinePath(HDFS_ROOT_PATH, src_name), src_name, src_s, combinePath(HDFS_SHARED_PATH, dst_email)) ?
                            JsonHelp.buildSuccess() : JsonHelp.buildFailed());
                    return;
                }
                renderJson(JsonHelp.buildFailed("不能给自己分享数据"));
                return;
            } else {
                //发送邮件链接加密
                // String url2 = ROOTPATH + "";
                String url3 = MSG_URL + "sendOss";
                if (EmailServ.me().sendOss(url3, map)) {
                    renderJson(JsonHelp.buildSuccess("下载链接已经发送至:" + dst_email, "null"));
                    return;
                }
                renderJson(JsonHelp.buildFailed());
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    //多个 sharedFile
    public void sharedFiles() {
        //szy需要判断是否本地用户，如果不是，发送下载链接
        String src_name = getPara(ACCOUNTEMAIL);
        String dst_name = getPara(DST_NAME);
        String src_s = getPara(DIRNAME);
        try {
            JSONObject jsonObject = JSON.parseObject(dst_name);
            JSONArray jsonArray = jsonObject.getJSONArray(DST_NAME);
            //检查邮箱
            boolean flag = checkEmail(jsonArray);

            if (flag) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    String dst_email = jsonArray.getString(i);
                    // check email
                    Map<String, String[]> map = new HashMap<String, String[]>();
                    map.put("dst_email", new String[]{dst_email});
                    map.put("src_email", new String[]{src_name});
                    map.put("src_f", new String[]{src_s});
                    // String url = SZY_URL + "check";

                    Map<String, String[]> data = new HashMap<String, String[]>();
                    String resp = null;

                    if (!src_name.equals(dst_email)) {
                        //开始分享
                        if (hdfService.shareFiles(combinePath(HDFS_ROOT_PATH, src_name), src_name, src_s, combinePath(HDFS_SHARED_PATH, dst_email))) {
                            resp += dst_email + "、";
                            data.put("success", new String[]{resp});
                            // renderJson(JsonHelp.buildSuccess());
                            // return;
                        } else {
                            data.put("failed", new String[]{dst_email});
                            renderJson(JsonHelp.buildFailed("failed", JsonKit.toJson(data)));
                        }
                        //return;
                    } else {
                        renderJson(JsonHelp.buildFailed("不能给自己分享数据"));
                    }
                }
            } else {
                throw new RuntimeException("邮箱包含外部邮箱，请检查后再分享！");
            }
            renderJson(JsonHelp.buildSuccess());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }

    //upload
    public void uploadFile() {
        String email = getPara(ACCOUNTEMAIL);
        String dir = getPara("dirUrl", null);
        LOADPATH = getSession().getServletContext().getRealPath("/") + "upload/";
        List<UploadFile> fileList = null;
        String dirzh = null;
        String emailzh = null;
        try {
            if (dir == null) {
                dirzh = dir;
            } else {
                dirzh = new String(dir.getBytes("ISO8859-1"), "utf-8");
            }
            emailzh = new String(email.getBytes("ISO8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            try {
                fileList = getFiles(LOADPATH, 10000 * 1024 * 1024);//30MB
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
            if (loadService.uploadFiles(HDFS_ROOT_PATH, emailzh, dirzh, fileList)) {
                renderJson(JsonHelp.buildSuccess());
                return;
            }
            renderJson(JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
            return;
        } finally {
            loadService.deleteUpload(LOADPATH);
        }
    }

    //文件下载
    public void loadUrl() {
        String type = getPara(ACTIONTYPE);
        String name = getPara(ACCOUNTEMAIL);
        String src_f = getPara(DIRNAME);
        String fName = getPara("fName");
        JSONObject jsonObject = JSON.parseObject(src_f);
        JSONArray jsonArray = jsonObject.getJSONArray("dirUrl");
        boolean flag = false;
        try {
            if (type.equals(HDFS_USER_TYPE)) {
                flag = loadService.getSize(combinePath(HDFS_ROOT_PATH, name), jsonArray, name);
            } else if (type.equals(HDFS_SHARED_TYPE)) {
                flag = loadService.getSize(combinePath(HDFS_SHARED_PATH, name), jsonArray, name);
            }

            if (flag) {
                String url = ROOTPATH + "download" + "?email=" + name + "&type=" + type + "&fName=" + fName;
                Map<String, String> map = new HashMap<String, String>();
                map.put("url", url);
                renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
            } else {
                throw new RuntimeException("要下载内容过大，请使用客户端下载1！");
            }
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }

    }

    public void download() {
        HttpServletResponse response = getResponse();

        String type = getPara(ACTIONTYPE);
        String name = getPara(ACCOUNTEMAIL);
        String src_F = getPara(DIRNAME);
        String Fname = getPara("fName");
        String src_f = null;
        String fName = null;//ISO8859-1
        try {
            fName = new String(Fname.getBytes("ISO8859-1"), "utf-8");
            src_f = new String(src_F.getBytes("ISO8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = JSON.parseObject(src_f);
        JSONArray jsonArray = jsonObject.getJSONArray("dirUrl");
        FSDataInputStream fsin = null;
        try {
            if (type.equals(HDFS_USER_TYPE)) {
                Object[] obj = loadService.downLoad(combinePath(HDFS_ROOT_PATH, name), jsonArray.getString(0));
                long len = (Long) obj[0];
                fsin = (FSDataInputStream) obj[1];
                new FileRenders(len, fsin, fName, response).render();
                renderNull();
                return;
            } else if (type.equals(HDFS_SHARED_TYPE)) {
                Object[] obj = loadService.downLoad(combinePath(HDFS_SHARED_PATH, name), jsonArray.getString(0));
                long len = (Long) obj[0];
                fsin = (FSDataInputStream) obj[1];
                new FileRenders(len, fsin, fName, response).render();
                renderNull();
                return;
            }
            renderJson(JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed());
        } finally {
            if (fsin != null) try {
                fsin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //new FileRenders();

    }

    //链接下载 download
    public void downloadLink() {
        HttpServletResponse response = getResponse();
        long time = Long.valueOf(encAndDecByDES.getDesString(getPara("time")));
        String name = encAndDecByDES.getDesString(getPara(ACCOUNTEMAIL));
        String src_F = encAndDecByDES.getDesString(getPara(DIRNAME));
        LOADPATH = getSession().getServletContext().getRealPath("/");

        String src_f = null;
        try {
            src_f = new String(src_F.getBytes("ISO8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if ((System.currentTimeMillis() - time) > 3 * 24 * 60 * 60 * 1000) {
            //链接失效时间，三天
            renderJson(JsonHelp.buildFailed("链接已经失效！"));
            return;
        }
        JSONObject jsonObject = JSON.parseObject(src_f);
        JSONArray jsonArray = jsonObject.getJSONArray("dirUrl");
        String fName = jsonArray.getString(0).split("/")[1] + ".zip";//下载压缩包文件名

        try {
            boolean obj = false;
            //load the local
            obj = loadService.downloadLocal(combinePath(HDFS_ROOT_PATH, name), jsonArray, name, fName);
            try {
                //send to the browser
                if (obj) {
                    new FileRender(fName, response, LOADPATH).render();
                } else {
                    throw new RuntimeException("服务器出现问题，请重新下载！");
                }
            } catch (Exception e) {
                renderJson(JsonHelp.buildFailed(e.getMessage()));
            }
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        } finally {
            //delete the local data
            loadService.deleteLocal(LOADPATH, fName);
        }
    }

    //批量下载
    public void downUrl() {
        String type = getPara(ACTIONTYPE);
        String name = getPara(ACCOUNTEMAIL);
        String src_f = getPara(DIRNAME);
        LOADPATH = getSession().getServletContext().getRealPath("/");
        boolean flag = false;
        try {
            JSONObject jsonObject = JSON.parseObject(src_f);
            JSONArray jsonArray = jsonObject.getJSONArray("dirUrl");
            String src = jsonArray.getString(0);
            String fName = src.split("/")[1] + ".zip";
            if (type.equals(HDFS_USER_TYPE)) {
                flag = loadService.getSize(combinePath(HDFS_ROOT_PATH, name), jsonArray, name);
            } else if (type.equals(HDFS_SHARED_TYPE)) {
                flag = loadService.getSize(combinePath(HDFS_SHARED_PATH, name), jsonArray, name);
            }

            if (flag) {
                String url = ROOTPATH + "downloadLocals" + "?email=" + name + "&type=" + type + "&fName=" + fName;
                Map<String, String> map = new HashMap<String, String>();
                map.put("url", url);
                renderJson(JsonHelp.buildSuccess(JsonKit.toJson(map)));
            } else {
                throw new RuntimeException("要下载内容过大，请使用客户端下载1！");
            }

        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }

    }

    public void downloadLocals() {
        HttpServletResponse response = getResponse();
        String type = getPara(ACTIONTYPE);
        String name = getPara(ACCOUNTEMAIL);

        String src_F = getPara(DIRNAME);
        String Fname = getPara("fName");
        String src_f = null;
        String fName = null;
        try {
            fName = new String(Fname.getBytes("ISO8859-1"), "utf-8");
            src_f = new String(src_F.getBytes("ISO8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        LOADPATH = getSession().getServletContext().getRealPath("/");
        JSONObject jsonObject = JSON.parseObject(src_f);
        JSONArray jsonArray = jsonObject.getJSONArray("dirUrl");

        try {
            boolean obj = false;
            //load the local
            if (type.equals(HDFS_USER_TYPE)) {
                obj = loadService.downloadLocal(combinePath(HDFS_ROOT_PATH, name), jsonArray, name, fName);
            } else if (type.equals(HDFS_SHARED_TYPE)) {
                obj = loadService.downloadLocal(combinePath(HDFS_SHARED_PATH, name), jsonArray, name, fName);
            }
            try {
                //send to the browser
                if (obj) {
                    new FileRender(fName, response, LOADPATH).render();
                    renderNull();
                } else {
                    throw new RuntimeException("服务器出现问题，请重新下载！");
                }
            } catch (Exception e) {
                renderJson(JsonHelp.buildFailed(e.getMessage()));
            }
            renderJson(JsonHelp.buildSuccess());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        } finally {
            //delete the local data
            loadService.deleteLocal(LOADPATH, fName);
        }
    }


    /**
     * 给文件修改描述
     */
    public void modifyDesc() {
        String filename = getPara(FileDesc.FILENAME);
        String desc = getPara(FileDesc.DESC);
        try {
            if (fileDescSrv.modifyDesc(filename, desc)) {
                renderJson(JsonHelp.buildSuccess());
                return;
            }
            renderJson(JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }

    }
    //utils

    private boolean checkEmail(JSONArray jsonArray) {
        boolean flag = false;
        for (int i = 0; i < jsonArray.size(); i++) {
            //检查全部邮箱是否内部邮箱
            String dst_email = jsonArray.getString(i);
            // check email
            Map<String, String[]> map = new HashMap<String, String[]>();
            map.put("dst_email", new String[]{dst_email});
            String url = SZY_URL + "check";

            if (!EmailServ.me().check(url, map)) {
                flag = true;
            } else {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public String combinePath(String HDFS_ROOT_PATH, String name) {
        return HDFS_ROOT_PATH + name;
    }
}
