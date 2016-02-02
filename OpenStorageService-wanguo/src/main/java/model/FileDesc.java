package model;

import com.jfinal.plugin.activerecord.Model;

/**
 * Created by Tyfunwang on 2015/4/29.
 */
public class FileDesc extends Model<FileDesc> {
    public static FileDesc dao = new FileDesc();
    public static final String TABLE = "filedesc";
    public static final String ID = "id";
    public static final String DIRECTORY = "directory";
    public static final String DESC = "description";
    public static final String FILENAME = "filename";


    /**
     * filename
     */
    public FileDesc checkFileName(String filename) {
        return dao.findFirst("select * from " + TABLE + " where " + FILENAME + " like '%" + filename + "'");
    }

    public boolean modifyFilename(String newname, String oldname) {
        return new FileDesc().findFirst("select * from " + TABLE + " where " + FILENAME + " like '%" + oldname + "'").set(FILENAME,newname).update();
    }


    //get filename
    public FileDesc getFileName(String filename) {
        return dao.findFirst("select * from " + TABLE + " where " + FILENAME + " like '%" + filename + "'");
    }


    /**
     * desc
     */
    //get file's des by filename
    public FileDesc getDescByFileName(String filename) {
        return dao.findFirst("select * from " + TABLE + " where " + FILENAME + " like '%" + filename + "'");
    }

    //modify desc by filename
    public boolean mofifyDesc(String filename, String desc) {
        return new FileDesc().findFirst("select * from " + TABLE + " where " + FILENAME + " like '%" + filename + "'").set(DESC,desc).update();
    }


}
