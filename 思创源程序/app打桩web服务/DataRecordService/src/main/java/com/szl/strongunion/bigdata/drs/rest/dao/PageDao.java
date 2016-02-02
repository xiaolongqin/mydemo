package com.szl.strongunion.bigdata.drs.rest.dao;


import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Created by liweiqi on 2015/5/27.
 */
public class PageDao extends Model<PageDao> {
    public final static String TABLENAME = "sl_ods_page";
    public final static String ID = "page_id";
    public final static String PAGEURL = "page_url";
    public final static String PAGETYPE = "page_type";
    public final static String PAGESTATE = "page_state";
    public final static String PAGENAME = "page_name";
    public final static String PAGEVERSION = "page_version";
    public static PageDao dao = new PageDao();

    public Page<PageDao> findByPage(int pageNumber, int pageSize) {
        return PageDao.dao.paginate(pageNumber, pageSize, "select * ", "from " + PageDao.TABLENAME);
    }
}
