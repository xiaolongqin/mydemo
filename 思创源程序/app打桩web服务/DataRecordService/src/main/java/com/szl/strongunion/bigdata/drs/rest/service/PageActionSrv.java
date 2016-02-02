package com.szl.strongunion.bigdata.drs.rest.service;

import com.jfinal.plugin.activerecord.Page;
import com.szl.strongunion.bigdata.drs.rest.dao.PageActionDao;

import java.util.Map;

/**
 * Created by liweiqi on 2015/5/27.
 */
public class PageActionSrv {
    private static PageActionSrv srv = new PageActionSrv();

    public static PageActionSrv me() {
        return srv;
    }

    private PageActionSrv() {
    }

    public boolean add(Map<String, Object> attrs) {
        return new PageActionDao().setAttrs(attrs).save();
    }

    public boolean update(Map<String, Object> attrs) {
        return new PageActionDao().setAttrs(attrs).update();
    }

    public PageActionDao findById(String id) {
        return PageActionDao.dao.findById(id);
    }

    public Page<PageActionDao> findInPage(int pageNumber, int pageSize) {
        return PageActionDao.dao.findByPage(pageNumber, pageSize);
    }

    public Page<PageActionDao> findInPage(int pageNumber, int pageSize, String pageId) {
        return PageActionDao.dao.findByPage(pageNumber, pageSize, pageId);
    }

    public boolean findByActionId(String actionId) {
     return PageActionDao.dao.findByActionId(actionId);
    }

}
