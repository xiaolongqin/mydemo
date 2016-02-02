package service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import controller.HdfsController;
import model.Account;
import model.Space;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import util.FormatUtils;
import util.PropertyUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//import org.apache.hadoop.io.IOUtils;

/**
 * Created by Tyfunwang on 2015/1/19.
 */
public class HdfService {
    //public static final Logger log = LoggerFactory.getLogger(HdfsService.class);
    public RpcService rpcService = RpcService.getRpcSer();
    private static HdfService hdfService = new HdfService();
    private static FileDescSrv fileDescSrv = FileDescSrv.getInstance();
    public static Map<String, String> loadInfo = new HashMap<String, String>();


    private HdfService() {
    }

    public static HdfService getHdfsServ() {
        return hdfService;
    }

    ExecutorService tt = Executors.newFixedThreadPool(50);
    private SpaceServ spaceServ = SpaceServ.me();
    private OrdersSrv ordersSrv = OrdersSrv.me();
    private LoadService loadService = LoadService.getInstance();
//    static {
//        URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
//    }

    //openService 以该用户的名字建立文件夹
    public boolean openService(String filePath, String sharedPath, final String email, final String phone, final String realname) throws Exception {
        boolean flag = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                //开通用户，即开通我的分享,同时加入空间数据,并生产订单
                return spaceServ.addSpace(email) && ordersSrv.open(email, phone, realname);
            }
        });

        if (flag) {
            FileSystem fs = rpcService.getFileSys();
            try {
                return fs.mkdirs(new Path(sharedPath)) && fs.mkdirs(new Path(filePath));
            } finally {
                rpcService.closeFileSys(fs);
            }
        }
        return false;
    }

    //make directory
    public boolean makeDir(String filePath, String dirname) throws Exception {
        FileSystem fs = rpcService.getFileSys();
        try {
            return fs.mkdirs(new Path(filePath + dirname));
        } finally {
            rpcService.closeFileSys(fs);
        }
    }

    //delete directorys
    public boolean deleteAll(String filePath, String email, String pathName) throws Exception {
        FileSystem fs = rpcService.getFileSys();
        JSONObject jsonArray = JSONArray.parseObject(pathName);
        JSONArray jsonA = JSONArray.parseArray(jsonArray.getString(HdfsController.DIRNAME));
        long len = 0;
        try {
            for (int i = 0; i < jsonA.size(); i++) {
                Path path = new Path(filePath + email + jsonA.getString(i));
                len += fs.getFileStatus(path).getLen();
                if (fs.delete(path, true)) {
                    continue;
                }
                return false;
            }
            // float f = len / 1024 / 1024;
            if (spaceServ.updateSp(len, email)) {
                return true;
            } else {
                return false;
            }
        } finally {
            rpcService.closeFileSys(fs);
        }
    }

    //delete directorys from shared
    public boolean del4Shared(String filePath, String email, String pathName) throws Exception {
        FileSystem fs = rpcService.getFileSys();
        JSONObject jsonArray = JSONArray.parseObject(pathName);
        JSONArray jsonA = JSONArray.parseArray(jsonArray.getString(HdfsController.DIRNAME));
        try {
            for (int i = 0; i < jsonA.size(); i++) {
                Path path = new Path(filePath + email + jsonA.getString(i));
                if (fs.delete(path, true)) {
                    continue;
                }
                return false;
            }
            return true;
        } finally {
            rpcService.closeFileSys(fs);
        }
    }

    //delete user
    public boolean delUser(String filePath) throws Exception {
        FileSystem fs = rpcService.getFileSys();
        Path path = new Path(filePath);
        try {
            fs.delete(path, true);
        } finally {
            rpcService.closeFileSys(fs);
        }
        return true;
    }

    //rename directory or file
    public boolean rename(String filePath, String old_st, String new_st) throws Exception {
        FileSystem fs = rpcService.getFileSys();
        try {
            Path oldPath = new Path(filePath + old_st);
            Path newPath = new Path(filePath + new_st);
            if (fs.getFileStatus(oldPath).isDirectory()) {
                //directory
                return fs.rename(oldPath, newPath);
            }
            //file
            if (fileDescSrv.checkFilename(new_st)) {
                //没有重名，可以修改
                return modifyFilename(new_st, old_st) && fs.rename(oldPath, newPath);
            } else {
                throw new Exception("文件名重复，请确认后再次提交！");
            }
        } finally {
            rpcService.closeFileSys(fs);
        }
    }

    //Batch move directory
    public boolean moveFile(final String filePath, final String src_st, final String dst_st) throws Exception {
        Future<Boolean> future = tt.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                //  boolean flag = false;
                JSONObject jsonObject = JSONObject.parseObject(src_st);
                JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString(HdfsController.DIRNAME));
                FileSystem fs = rpcService.getFileSys();
                try {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        Path oldPath = new Path(filePath + jsonArray.getString(i));
                        oldPath.getParent();
                        Path newPath = new Path(filePath + dst_st);//+ jsonArray.getString(i)
                        newPath.getName();
                        if (fs.rename(oldPath, newPath)) {
                            continue;
                        } else {
                            return false;
                        }
                    }
                } finally {
                    rpcService.closeFileSys(fs);
                }
                return true;
            }
        });
        while (!future.isDone()) {
            Thread.sleep(1000);
        }
        return future.get();

    }

    //move shared to list
    public boolean moveFiles(final String sharedPath, final String src_st, final String dst_st, final String dstPath, final String size, final String email) throws Exception {
        //事务，修改数据库space信息
        boolean success = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                boolean success = false;
                float count = countLength(size);
                //先检查space是否够用
                if (spaceServ.checkSp(count, email)) {
                    return spaceServ.moveReduce(count, email);
                }
                return false;
            }

        });
        if (success) {
            Future<Boolean> future = tt.submit(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    // boolean flag = false;
                    JSONObject jsonObject = JSONObject.parseObject(src_st);
                    JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString(HdfsController.DIRNAME));
                    final FileSystem fs = rpcService.getFileSys();
                    try {
                        for (int i = 0; i < jsonArray.size(); i++) {
                            final Path oldPath = new Path(sharedPath + jsonArray.getString(i));
                            oldPath.getParent();
                            Path newPath = new Path(dstPath + dst_st);
                            //newPath.getName();
                            if (fs.rename(oldPath, newPath)) {
                                continue;
                            } else {
                                return false;
                            }
                        }
                        return true;
                    } finally {
                        rpcService.closeFileSys(fs);
                    }
                }
            });
            while (!future.isDone()) {
                Thread.sleep(1000);
            }
            return future.get();
        } else {
            return false;
        }
    }

    //get User's file
    public List<Map<String, String>> getUserFile(String rootPath, Account account) throws Exception {
        //根目录--用户
        FileSystem fs = rpcService.getFileSys();
        Path path = new Path(rootPath);
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        try {
            // FileStatus[] fileStatus = fs.listStatus(path);
            //select space info
            Space space = spaceServ.getSp(account.getAccountEmail());

//            if (fileStatus.length == 0) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("email", account.getAccountEmail());
            map.put("name", account.getAccountName());
            map.put("userid", String.valueOf(account.getAccountId()));

            float f = space.getFloat(Space.USEDSPACE) / 1000 / 1000;
            float b = (float) (Math.round(f * 10)) / 10;
            float all = space.getFloat(Space.ALLSPACE) / 1000 / 1000;
            float allSto = (float) (Math.round(all * 10)) / 10;

            map.put("useSto", String.valueOf(b));
            map.put("allSto", String.valueOf(allSto));

            list.add(map);
            return list;
        } finally {
            rpcService.closeFileSys(fs);
        }
    }

    //getFile获取给定目录下的所有子目录以及子文件
    public List<Map<String, String>> getNextFile(String filePath, String name, String dirName) throws Exception {
        FileSystem fs = rpcService.getFileSys();
        Path path;
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        if (dirName == null || dirName.equals("")) {
            //根目录
            path = new Path(filePath);
            try {
                FileStatus[] fileStatus = fs.listStatus(path);

                for (int i = 0; i < fileStatus.length; i++) {
                    FileStatus status = fileStatus[i];
                    Map<String, String> map = new HashMap<String, String>();

                    String name1 = fileStatus[i].getPath().getName();
                    long time = fileStatus[i].getModificationTime();
                    long size = fileStatus[i].getLen();

                    String[] s = cutString(fileStatus[i].getPath().toString()).split("/");
                    String route = "";
                    for (int j = 0; j < s.length; j++) {
                        //cdh3u6 j>3
                        if (j > 2) route += "/" + s[j];
                        //hadoop2.4.1 j>2
                    }

                    if (status.isDirectory()) {
                        String src = cutString(status.getPath().toString(), name);
                        size += loadService.itreFiles(fs, filePath, src, name);
                    }


                    map.put("file", name1);
                    map.put("dirUrl", route);
                    map.put("size", convertSize(size));
                    map.put("ctime", FormatUtils.time2String(time));
                    map.put("time", time + "");
                    map.put("url", "");
                    getFileType(fileStatus[i], map);
                    //获取文件描述
                    getFileDesc(fileStatus[i], map);
                    list.add(map);
                }
                list = sortList(list);
                return list;
            } finally {
                rpcService.closeFileSys(fs);
            }
        } else {
            //文件下目录
            path = new Path(filePath + dirName);
            try {
                FileStatus[] fileStatus = fs.listStatus(path);
                for (int i = 0; i < fileStatus.length; i++) {
                    FileStatus status = fileStatus[i];

                    String name1 = fileStatus[i].getPath().getName();

                    String[] s = cutString(fileStatus[i].getPath().toString()).split("/");
                    String route = "";
                    for (int j = 0; j < s.length; j++) {
                        //cdh3u6 j>3
                        if (j > 2) route += "/" + s[j];
                        //hadoop2.4.1 j>2
                    }

                    long time = fileStatus[i].getModificationTime();
                    long size = fileStatus[i].getLen();
                    //size
                    if (status.isDirectory()) {
                        String src = cutString(status.getPath().toString(), name);
                        size += loadService.itreFiles(fs, filePath, src, name);
                    }
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("file", name1);
                    map.put("dirUrl", route);
                    map.put("size", convertSize(size));
                    map.put("ctime", FormatUtils.time2String(time));
                    map.put("time", time + "");
                    map.put("url", "");
                    getFileType(fileStatus[i], map);
                    //获取文件描述
                    getFileDesc(fileStatus[i], map);
                    list.add(map);
                }
                list = sortList(list);
                return list;
            } finally {
                rpcService.closeFileSys(fs);
            }
        }
    }

    //getDir
    public List<Map<String, String>> getDirs(String rootPath, String dirName, String src_f) throws Exception {
        FileSystem fs = rpcService.getFileSys();
        Path path;
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        JSONArray json = null;
        if (src_f != null) {
            JSONObject jsonObject = JSONObject.parseObject(src_f);
            json = JSONArray.parseArray(jsonObject.getString(HdfsController.DST_DIR));
        }
        if (dirName == null || "".equals(dirName)) {
            //根目录
            path = new Path(rootPath);
            try {
                FileStatus[] fileStatusr = fs.listStatus(path);
                //user
                for (int i = 0; i < fileStatusr.length; i++) {
                    if (fileStatusr[i].isDirectory()) {
                        FileStatus fileS = fileStatusr[i];
                        list = addList(list, fileS, json);
                    }
                }
                return list;
            } finally {
                rpcService.closeFileSys(fs);
            }
        } else {
            //文件下目录
            //根目录
            path = new Path(rootPath + dirName);
            try {
                FileStatus[] fileStatusr = fs.listStatus(path);
                //user
                for (int i = 0; i < fileStatusr.length; i++) {
                    if (fileStatusr[i].isDirectory()) {
                        FileStatus fileS = fileStatusr[i];
                        list = addList(list, fileS, json);
                    }
                }
                return list;
            } finally {
                rpcService.closeFileSys(fs);
            }
        }
    }

    public List<Map<String, String>> getDir(String filePath, String dirName, String src_f) throws Exception {
        FileSystem fs = rpcService.getFileSys();
        Path path;
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        JSONArray json = null;
        if (src_f != null) {
            JSONObject jsonObject = JSONObject.parseObject(src_f);
            json = JSONArray.parseArray(jsonObject.getString(HdfsController.DST_DIR));
        }
        if (dirName == null || "".equals(dirName)) {
            //根目录
            path = new Path(filePath);
            try {
                FileStatus[] fileStatus = fs.listStatus(path);
                for (int i = 0; i < fileStatus.length; i++) {
                    if (fileStatus[i].isDirectory()) {
                        FileStatus fileS = fileStatus[i];
                        list = addList(list, fileS, json);
                    }
                }
                return list;
            } finally {
                rpcService.closeFileSys(fs);
            }
        } else {
            //文件下目录
            path = new Path(filePath + dirName);
            try {
                FileStatus[] fileStatus = fs.listStatus(path);
                for (int i = 0; i < fileStatus.length; i++) {
                    if (fileStatus[i].isDirectory()) {
                        FileStatus fileS = fileStatus[i];
                        list = addList(list, fileS, json);
                    }
                }

                return list;
            } finally {
                rpcService.closeFileSys(fs);
            }
        }
    }

    //share files
    public boolean shareFiles(final String filePath, final String src_name, final String src_s, final String dst_s) throws Exception {
        Future<Boolean> futures = tt.submit(new Callable<Boolean>() {
                                                @Override
                                                public Boolean call() throws Exception {
                                                    Boolean flag = false;
                                                    FSDataInputStream fsin = null;
                                                    FSDataOutputStream fsout = null;
                                                    FileSystem fs = null;

                                                    long totalLen = 0;
                                                    int level = 0;//限制层级
                                                    int amount_f = 0;
                                                    try {

                                                        JSONObject jsonArray = JSONArray.parseObject(src_s);
                                                        JSONArray jsonA = JSONArray.parseArray(jsonArray.getString(HdfsController.DIRNAME));
                                                        fs = rpcService.getFileSys();
                                                        for (int i = 0; i < jsonA.size(); i++) {
                                                            itreFiles(fs, filePath, jsonA.getString(i), fsin, fsout, src_name, dst_s, level, amount_f, totalLen);
                                                        }
                                                        flag = true;
                                                    } finally {
                                                        try {
                                                            if (fsin != null) fsin.close();
                                                            if (fsout != null) fsout.close();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                    return flag;
                                                }

                                            }
        );
        while (!futures.isDone()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return futures.get();
        //return flag;
    }

    //util methods
    private List<Map<String, String>> addMap(List<Map<String, String>> list, FileStatus fileStatus) {
        Map<String, String> map = new HashMap<String, String>();
        String name = fileStatus.getPath().getName();
        String[] s = cutString(fileStatus.getPath().toString()).split("/");
        Long time = fileStatus.getModificationTime();
        String route = "";
        for (int z = 0; z < s.length; z++) {
            //hadoop2.4.1 z>2 === test
            if (z > 2) route += "/" + s[z];
            //chd3u6 z>3
        }
        map.put("dirName", name);
        map.put("dirUrl", route);
        map.put("url", "");
        map.put("time", time + "");
        getFileType(fileStatus, map);
        list.add(map);
        return list;
    }

    private List<Map<String, String>> addList(List<Map<String, String>> list, FileStatus fileStatus, JSONArray json) {
        String[] dirUrl = cutString(fileStatus.getPath().toString()).split("/");
        if (json != null) {
            boolean flag = true;
            for (int j = 0; j < json.size(); j++) {
                //排除本身的目录
                String[] js = json.getString(j).split("/");//up
                if (dirUrl[dirUrl.length - 1].equals(js[js.length - 1])) {
                    //addMap to list
                    flag = false;
                    break;
                }
            }
            if (flag) {
                //addMap to list
                list = addMap(list, fileStatus);
            }
        } else {
            //addMap to list
            list = addMap(list, fileStatus);
        }
        return list;
    }

    //sort list by time
    private List<Map<String, String>> sortList(List<Map<String, String>> list) {
        //按时间顺序排列
        Collections.sort(list, new Comparator<Map<String, String>>() {
            @Override
            public int compare(Map<String, String> o1, Map<String, String> o2) {
                return o2.get("time").compareTo(o1.get("time"));
            }
        });
        //按照文件形式排列

//        for (int i = 0; i < list.size(); i++) {
//            Map<String, String> map1 = list.get(i);
//            Long time1 = Long.decode(map1.get("time"));
//
//            Map<String, String> map3=null;
//            for (int j = i+1 ; j < list.size(); j++) {
//
//                Map<String, String> map2 = list.get(j);
//                Long time2 = Long.decode(map2.get("time"));
//
//                if (time1 > time2) {
//                    map3 = map1;
//                    list.set(i,map2 );
//                    list.set(j, map3);
//                }
//            }
//
//        }

        return list;
    }

    private void itreFiles(FileSystem fs, String filePath, String src_s, FSDataInputStream fsin, FSDataOutputStream fsout, String src_name, String dst_f, int level, int amount_f, long totalLen) throws Exception {
        Path src_p = new Path(filePath + src_s);
        FileStatus[] fss = fs.listStatus(src_p);

        for (int i = 0; i < fss.length; i++) {
            FileStatus fis = fss[i];

            String srcs = cutString(fis.getPath().toString(), src_name);

            Path src_Path = new Path(filePath + srcs);
            Path dst_path = new Path(dst_f + srcs);
            //一级
            if (fs.getFileStatus(src_Path).isDirectory()) {
                if (fs.mkdirs(dst_path)) {
                    itreFiles(fs, filePath, srcs, fsin, fsout, src_name, dst_f, level, amount_f, totalLen);
                } else {
                    throw new RuntimeException("分享失败，请重新分享。");
                }
//                level++;
//                if (level > 5) throw new RuntimeException("目录层数太深，不能分享。");
                //二级，回调

            } else {
                // copy file
                copyFile(fs, fsin, fsout, src_Path, dst_path, amount_f, totalLen);
            }
        }
    }

    private void copyFile(FileSystem fs, FSDataInputStream fsin, FSDataOutputStream fsout, Path src_f, Path dst_f, int amount_f, long totalLen) throws Exception {
//        amount_f++;
        FileStatus fss = fs.getFileStatus(src_f);
//        totalLen += fss.getLen();//size
//        if (totalLen > 50 * 1024 * 1024)
//            throw new RuntimeException("文件太大，不能分享，请选择客户端分享。");
//        if (amount_f > 100)
//            throw new RuntimeException("文件数量太大，不能分享，请选择客户端分享。");
        fsin = fs.open(src_f);
        fsout = fs.create(dst_f, true);
        IOUtils.copyBytes(fsin, fsout, 4096, true);
    }

    private static String cutString(String uri) {
        //8020为链接hdfs的端口
        String port = PropertyUtil.port.getProperty("port");
        return uri.substring(uri.indexOf(port) + 5);
    }


    public static String cutString(String uri, String src_name) {
        return uri.substring(uri.indexOf(src_name) + src_name.length());
    }

    /**
     * file desc utils methods
     */
    private void getFileDesc(FileStatus fileStatus, Map<String, String> map) {
        if (!fileStatus.isDirectory()) {
            String filename = fileStatus.getPath().getName();
            String desc = fileDescSrv.getFileDesc(filename);
            map.put("desc", desc);
        }
    }

    private boolean modifyFilename(String newname, String oldname) {
        String name = fileDescSrv.getFilename(oldname);
        if (name != null) {
            String[] split = name.split("/");
            // newname="/文件类型.txt" split 1
            split[split.length - 1] = newname.split("/")[1];
            String longname = "";
            for (String s : split) {
                longname += s + "/";
            }

            String updateName = longname.substring(0, longname.length() - 1);

            return fileDescSrv.modifyFilename(updateName, oldname);
        }
        return false;
    }

    /**
     * file type utils methods
     */
    private void getFileType(FileStatus fileStatus, Map<String, String> map) {
        if (fileStatus.isDirectory()) {
            map.put("type", "文件夹");
        } else {
            String str = fileStatus.getPath().getName();
            String[] strs = str.split("\\.");
            String type = strs[strs.length - 1];

            if (type.equals("txt")) {
                map.put("type", "文本");
            } else if (type.equals("doc") || type.equals("docx")) {
                map.put("type", "办公文档");
            } else if (type.equals("jpg") || type.equals("png") || type.equals("gif")) {
                map.put("type", "图片");
            } else if (type.equals("mp4") || type.equals("avi") || type.equals("mov")) {
                map.put("type", "视频");
            } else if (type.equals("rar") || type.equals("zip")) {
                map.put("type", "压缩文件");
            } else if (type.equals("ppt")) {
                map.put("type", "演示文稿");
            } else if (type.equals("xls") || type.equals("xlsx")) {
                map.put("type", "表格");
            } else if (type.equals("pdf")) {
                map.put("type", "电子文档");
            } else if (type.equals("mp3")) {
                map.put("type", "音乐");
            } else {
                map.put("type", "未知文件");
            }
        }
    }

    /**
     * 字节数转换
     *
     * @param size
     * @return
     */
    public synchronized static String convertSize(long size) {
        String result = String.valueOf(size);
        if (size < 1000 * 1000) {
            result = String.valueOf(size / 1000) + " KB";
        } else if (size >= 1000 * 1000 && size < 1000 * 1000 * 1000) {
            result = String.valueOf(size / 1000 / 1000) + " MB";
        } else if (size >= 1000 * 1000 * 1000) {
            result = String.valueOf(size / 1000 / 1000 / 1000) + " GB";
        } else {
            result = result + " B";
        }
        return result;
    }

    //计算从分享移动到用户目录下的文件大小
    private float countLength(String len) {
        //正则判断最后两位的单位
        int index = len.length();
        float count = 0;
        if (len.contains("KB")) {
            count = Float.valueOf(len.substring(0, index - 3)) * 1000;
            //count = (float) (1000 * new Long(len.substring(0, index - 3)));
        }
        if (len.contains("MB")) {
            count = Float.valueOf(len.substring(0, index - 3)) * 1000 * 1000;
        }
        if (len.contains("GB")) {
            count = Float.valueOf(len.substring(0, index - 3)) * 1000 * 1000 * 1000;
        }
        return count;

    }

}
