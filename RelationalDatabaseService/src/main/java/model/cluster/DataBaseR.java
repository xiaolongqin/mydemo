package model.cluster;

import com.jfinal.plugin.activerecord.Db;
import service.DSBox;
import service.DSManager;


/**
 * Created by liweiqi on 2014/10/23.
 */
public class DataBaseR {
    private static DataBaseR dao = new DataBaseR();

    public static DataBaseR me() {
        return dao;
    }

    private DataBaseR() {
    }

    public boolean createDataBase(String name, String charset) {
        return Db.use(DSBox.REMOTE).update("create database " + name + " DEFAULT CHARSET " + charset) >= 0;
    }

    public boolean deleteDataBase(String name) {
        return Db.use(DSBox.REMOTE).update("drop database if exists " + name) >= 0;
    }

//    public boolean createtableSpace(String fileName, int fileNum) {
//        StringBuilder sql = new StringBuilder();
//        sql.append("CREATE TABLESPACE ").append(fileName).append(" \n");
//        for (int i = 0; i < fileNum; i++) {
//            sql.append("ADD DATAFILE '" + fileName + "_data+" + i + ".dat' \n");
//        }
//        if (fileNum > 0) sql.setLength(sql.length() - 2);
//        sql.append("USE LOGFILE GROUP ").append(fileName + "_log \n")
//                .append("INITIAL_SIZE 5G \n ")
//                .append("ENGINE NDBCLUSTER");
//        return Db.use(DSBox.REMOTE).update(sql.toString()) >= 0;
//    }

}
