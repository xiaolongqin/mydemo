package com.szl.strongunion.bigdata.drs.rest.dao;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Created by liweiqi on 2015/5/27.
 */
public class PageActionDao extends Model<PageActionDao> {
    public static PageActionDao dao=new PageActionDao();
    public final static String TABLENAME = "sl_ods_page_action";
    public final static String ID = "page_action_id";
    public final static String PAGEID = "page_id";
    public final static String PAGEACTIONNAME = "page_action_name";
    public Page<PageActionDao> findByPage(int pageNumber, int pageSize) {
        return PageActionDao.dao.paginate(pageNumber, pageSize, "select * ", "from " + PageActionDao.TABLENAME);
    }
    public Page<PageActionDao> findByPage(int pageNumber, int pageSize, String pageId) {
        StringBuilder sqlBuilder = new StringBuilder("from ").append(PageActionDao.TABLENAME).append(" where ").append(PageActionDao.PAGEID).append("=?");
        return PageActionDao.dao.paginate(pageNumber, pageSize, "select * ", sqlBuilder.toString(), pageId);
    }

    public boolean findByActionId(String actionId) {
        PageActionDao a=PageActionDao.dao.findById(actionId);
        boolean flag=false;
        if (a!=null){
            flag=true;
        }
        return flag;
    }


}
