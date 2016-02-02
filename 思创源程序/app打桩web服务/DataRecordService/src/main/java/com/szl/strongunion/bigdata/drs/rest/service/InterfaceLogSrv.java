package com.szl.strongunion.bigdata.drs.rest.service;

import com.szl.strongunion.bigdata.drs.rest.dao.InterfaceLogDao;

import java.util.Map;

/**
 * Created by liweiqi on 2015/5/27.
 */
public class InterfaceLogSrv {


    public static InterfaceLogSrv me() {
        return srv;
    }

    public boolean add(Map<String, Object> attrs) {
        return InterfaceLogDao.dao.add(attrs);
    }

    public boolean chekTableExist() {
        return InterfaceLogDao.dao.checkExistTable();
    }

    private static InterfaceLogSrv srv = new InterfaceLogSrv();

    private InterfaceLogSrv() {
    }

}
