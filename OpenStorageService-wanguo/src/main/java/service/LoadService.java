package service;

import com.alibaba.fastjson.JSONArray;
import com.jfinal.upload.UploadFile;
import controller.HdfsController;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IOUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

/**
 * Created by Tyfunwang on 2015/1/29.
 */
public class LoadService {
    private static LoadService loadService = new LoadService();
    private long len;

    private LoadService() {
    }

    public static LoadService getInstance() {
        return loadService;
    }

    static final int BUFFER = 8192;
    private RpcService rpcService = RpcService.getRpcSer();
    ExecutorService tt = Executors.newFixedThreadPool(50);
    private SpaceServ spaceServ = SpaceServ.me();

    //upload files
    public boolean uploadFiles(final String filePath, final String email, final String dir, final List<UploadFile> uploadFiles) throws Exception {

        //check space
        if (check(email, uploadFiles)) {
            //start upload
            final FileSystem fs = rpcService.getFileSys();
            Future<Boolean> future = tt.submit(new Callable<Boolean>() {
                @Override
                public Boolean call() throws IOException {

                    FileInputStream in = null;
                    FSDataOutputStream fsout = null;
                    long len = 0;
                    try {
                        for (UploadFile uploadFile : uploadFiles) {
                            File file = uploadFile.getFile();
                            len += file.length();
                            Path dst = null;
                            if (dir == null) {
                                dst = new Path(filePath + email + "/" + uploadFile.getFileName());
                            } else {
                                dst = new Path(filePath + email + dir + "/" + uploadFile.getFileName());
                            }
                            in = new FileInputStream(file);
                            fsout = fs.create(dst, true);
                            IOUtils.copyBytes(in, fsout, 4096, true);
                            file.delete();
                        }

                        //修改空间表
                        // float f = len / 1024 / 1024/1024;
                        if (spaceServ.reduce(len, email)) {
                            return true;
                        } else {
                            return false;
                        }
                    } catch (Exception e) {
                        return false;
                    } finally {
                        if (in != null) in.close();
                        if (fsout != null) fsout.close();
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

    //download
    public boolean downloadLocal(final String hdfsPath, final JSONArray jsonArray, final String src_name, final String fName) throws Exception {
        Future<Boolean> future = tt.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                FileSystem fs = null;
                FSDataInputStream fsin = null;
                long totalLen = 0;
                boolean flag = true;
                FileOutputStream outputStream = new FileOutputStream(HdfsController.LOADPATH + fName);
                CheckedOutputStream cos = new CheckedOutputStream(outputStream, new CRC32());
                ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(cos));
                try {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        String dirurl = jsonArray.getString(i);
                        fs = rpcService.getFileSys();
                        //遍历压缩并下载到本地
                        itreFiles(fs, hdfsPath, dirurl, fsin, src_name, totalLen, zipout);
                        // zipout.close();
                        flag = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    flag = false;
                } finally {
                    if (fsin != null) fsin.close();
                    zipout.close();
                }
                return flag;
            }

        });
        while (!future.isDone()) {
            Thread.sleep(1000);
        }
        return future.get();
    }

    //单个下载
    public Object[] downLoad(String hdfsPath, String src_f) throws Exception {
        FileSystem fs = rpcService.getFileSys();
        Path path = null;
        if (src_f == null) {
            path = new Path(hdfsPath);
        } else {
            path = new Path(hdfsPath + "/" + src_f);
        }

        FileStatus file = fs.getFileStatus(path);
        Object[] obj = new Object[2];
        FSDataInputStream fsin = fs.open(path);
        obj[0] = file.getLen();
        obj[1] = fsin;
        return obj;
    }

    private void itreFiles(FileSystem fs, String filePath, String src_s, FSDataInputStream fsin, String src_name, long totalLen, ZipOutputStream zipout) throws Exception {
        //源
        BufferedInputStream bis = null;
        try {
            Path src_p = new Path(filePath + src_s);
            FileStatus[] fss = fs.listStatus(src_p);
            for (int i = 0; i < fss.length; i++) {
                FileStatus fis = fss[i];
                totalLen += fis.getLen();
                if (totalLen > 50 * 1024 * 1024) throw new RuntimeException("要下载内容过大，请使用客户端下载2！");

                String srcs = HdfService.cutString(fis.getPath().toString(), src_name);//文件目录
                Path src_Path = new Path(filePath + srcs);
                //一级 fs.getFileStatus(src_Path)
                if (fis.isDirectory()) {
                    //回调
                    itreFiles(fs, filePath, srcs, fsin, src_name, totalLen, zipout);
                } else {
                    // zip file
                    fsin = fs.open(src_Path);
                    bis = new BufferedInputStream(fsin);
                    //转码
//                    String sname = new String(srcs.getBytes("utf-8"), "gbk");//"utf-8"
                    ZipEntry entry = new ZipEntry(srcs);
                    zipout.putNextEntry(entry);
                    int count;
                    byte data[] = new byte[BUFFER];
                    while ((count = bis.read(data, 0, BUFFER)) != -1) {
                        zipout.write(data, 0, count);
                    }
                }
            }
        } finally {
            if (bis != null) bis.close();
        }


    }


    /**
     * util method
     */
//删除下载缓存
    public boolean deleteLocal(String localPath, String src_f) {
        File file = new File(localPath + src_f);
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }

    //删除上传缓存
    public boolean deleteUpload(String localPath) {
        File file = new File(localPath);
        boolean flag = false;
        try {
            if (file.exists()) {
                if (file.isDirectory()) {
                    String[] str = file.list();
                    File temp = null;
                    for (int i = 0; i < str.length; i++) {
                        if (localPath.endsWith(File.separator)) {
                            temp = new File(localPath + str[i]);
                        } else {
                            temp = new File(localPath + File.separator + str[i]);
                        }

                        if (temp.isFile()) {
                            temp.delete();
                        }

                        if (temp.isDirectory()) {
                            deleteUpload(localPath + "/" + str[i]);//先删除文件夹里面的文件
                            delFolder(localPath + "/" + str[i]);//再删除空文件夹
                            flag = true;
                        }
                    }
                }
            } else {
                return flag;
            }
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    //删除文件夹
//param folderPath 文件夹完整绝对路径
    private void delFolder(String folderPath) {
        try {
            deleteUpload(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            File myFilePath = new File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getSize(String hdfsPath, JSONArray jsonArray, String name) throws Exception {
        FileSystem fs = rpcService.getFileSys();
        long totalLen = 0;
        for (int i = 0; i < jsonArray.size(); i++) {
            String dirurl = jsonArray.getString(i);
            totalLen += itreFiles(fs, hdfsPath, dirurl, name);
        }
        if (totalLen / 1024 / 1024 > 30) return false;
        return true;
    }

    public long itreFiles(FileSystem fs, String filePath, String src_s, String name) throws Exception {
        //源
        Path src_p = new Path(filePath + src_s);
        FileStatus[] fss = fs.listStatus(src_p);
        len = 0;
        for (int i = 0; i < fss.length; i++) {
            FileStatus fileS = fss[i];
            if (fileS.isDirectory()) {
                String srcs = HdfService.cutString(fileS.getPath().toString(), name);//文件目录
                len += itreFiles(fs, filePath, srcs, name);
            } else {
                len += fileS.getLen();
            }
        }
        return len;

    }

    //check space
    private boolean check(String email, List<UploadFile> uploadFiles) {
        long len = 0;
        for (UploadFile uploadFile : uploadFiles) {
            UploadFile uploadFile1 = uploadFile;
            len += uploadFile1.getFile().length();
        }
        // f = len / 1024 / 1024/1024;//GB
        if (spaceServ.checkSp(len, email)) {
            return true;
        }
        return false;
    }


}
