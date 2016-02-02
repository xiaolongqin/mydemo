package com.szl.strongunion.bigdata.drs.rest.service;

import com.jfinal.plugin.activerecord.Page;
import com.szl.strongunion.bigdata.drs.rest.dao.PageDao;

import java.util.Map;

/**
 * Created by liweiqi on 2015/5/27.
 */
public class PageSrv {
    private static PageSrv srv = new PageSrv();

    public static PageSrv me() {
        return srv;
    }

    private PageSrv() {
    }

    public boolean add(Map<String, Object> attrs) {
        return new PageDao().setAttrs(attrs).save();
    }

    public boolean update(Map<String, Object> attrs) {
        return new PageDao().setAttrs(attrs).update();
    }

    public PageDao findById(String pageId) {
        return PageDao.dao.findById(pageId);
    }

    public Page<PageDao> findInPage(int pageNumber, int pageSize) {
        return PageDao.dao.findByPage(pageNumber, pageSize);
    }
}
