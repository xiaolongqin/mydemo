package com.szl.strongunion.bigdata.drs.rest.resource;

import com.szl.strongunion.bigdata.drs.rest.dao.PageVisitRecordDao;
import com.szl.strongunion.bigdata.drs.rest.service.PageVisitRecordSrv;
import com.szl.strongunion.bigdata.drs.rest.util.CollectionUtil;
import com.szl.strongunion.bigdata.drs.rest.util.JsonHelper;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MultivaluedMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liweiqi on 2015/5/27.
 */
@Path("/page_record")
public class PageVisitRecordResource {
    @Path("/post")
    @Produces("application/json")
    @POST
    public String post(MultivaluedMap<String, String> formParas
    ) {
        Map<String, Object> attrs = CollectionUtil.multiValueMapToMap(formParas);
//        if (!PageVisitRecordSrv.me().chekTableExist()){
//            return JsonHelper.buildFailed("failed to create table");
//        }
        try {
            int id=PageVisitRecordSrv.me().add(attrs);
            return JsonHelper.buildSuccess("{\"action_id\":"+"\""+id+"\""+"}");
        } catch (Exception ex) {
            return JsonHelper.buildFailed(ex.getMessage());
        }
    }

    @Path("/update")
    @Produces("application/json")
    @POST
    public String update(@FormParam(PageVisitRecordDao.ID) int id,
                         @FormParam(PageVisitRecordDao.ENDTIME) Long endtime
    ) {
//        PageVisitRecordSrv.me().chekTableExist();


        Map<String, Object> attrs = new HashMap<String, Object>();
        attrs.put(PageVisitRecordDao.ID, id);
        attrs.put(PageVisitRecordDao.ENDTIME, endtime);
        try {
            return JsonHelper.build(PageVisitRecordSrv.me().update(attrs));
        }catch (Exception e){
            return JsonHelper.buildFailed(e.getMessage());
       }
    }
}
