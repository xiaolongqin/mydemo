package service;

import model.FileDesc;

/**
 * Created by Tyfunwang on 2015/4/29.
 */
public class FileDescSrv {
    public FileDesc fileDesc = new FileDesc();

    private FileDescSrv() {
    }

    private static FileDescSrv fileDescSrv = new FileDescSrv();

    public static FileDescSrv getInstance() {
        if (fileDescSrv == null) fileDescSrv = new FileDescSrv();
        return fileDescSrv;

    }

    //check filename 
    public boolean checkFilename(String filename) {
        //true 可以修改 false 不可以修改
        return fileDesc.checkFileName(filename) == null;

    }

    //get filename
    public String getFilename(String filename) {
        FileDesc dao = fileDesc.getDescByFileName(filename);
        if (dao != null) return dao.get(FileDesc.FILENAME);
        return null;
    }

    //modify  filename
    public boolean modifyFilename(String newname, String oldname) {
        return fileDesc.modifyFilename(newname, oldname);
    }


    //get file's desc
    public String getFileDesc(String filename) {
        FileDesc dao = fileDesc.getDescByFileName(filename);
        if (dao != null) return dao.get(FileDesc.DESC);
        return null;
    }

    //modify descc by filename
    public boolean modifyDesc(String filename, String desc) {
        return fileDesc.mofifyDesc(filename, desc);
    }

}
