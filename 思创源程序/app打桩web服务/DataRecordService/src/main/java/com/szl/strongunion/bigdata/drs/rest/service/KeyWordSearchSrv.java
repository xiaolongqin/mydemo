package com.szl.strongunion.bigdata.drs.rest.service;

import com.szl.strongunion.bigdata.drs.rest.dao.KeyWordSearchDao;

import java.util.Map;

/**
 * Created by liweiqi on 2015/7/14.
 */
public class KeyWordSearchSrv {
    private static KeyWordSearchSrv srv = new KeyWordSearchSrv();

    public static KeyWordSearchSrv me() {
        return srv;
    }

    private KeyWordSearchSrv() {
    }

    public boolean add(Map<String, Object> attrs) {
        attrs.put(KeyWordSearchDao.ADDTIME,System.currentTimeMillis()/1000L);
        return new KeyWordSearchDao().setAttrs(attrs).save();
    }
}
