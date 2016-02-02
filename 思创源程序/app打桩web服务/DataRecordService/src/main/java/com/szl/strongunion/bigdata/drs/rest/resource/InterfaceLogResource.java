package com.szl.strongunion.bigdata.drs.rest.resource;

import com.szl.strongunion.bigdata.drs.rest.dao.InterfaceLogDao;
import com.szl.strongunion.bigdata.drs.rest.service.InterfaceLogSrv;
import com.szl.strongunion.bigdata.drs.rest.util.JsonHelper;

import javax.ws.rs.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liweiqi on 2015/5/27.
 */
@Path("/log")
public class InterfaceLogResource {
    @Path("/post")
    @Produces("application/json")
    @POST
    public String post(@FormParam(InterfaceLogDao.STATE) @DefaultValue("1") int state,
                       @FormParam(InterfaceLogDao.URL) String url,
                       @FormParam(InterfaceLogDao.POSTVALUE) @DefaultValue("") String postValue,
                       @FormParam(InterfaceLogDao.RETURNVALUE) @DefaultValue("") String returnValue
    ) {
//        if (!InterfaceLogSrv.me().chekTableExist()){
//            return JsonHelper.buildFailed("failed to create table");
//        }
        Map<String, Object> attrs = new HashMap<String, Object>();
        attrs.put(InterfaceLogDao.STATE, state);
        attrs.put(InterfaceLogDao.URL, url);
        attrs.put(InterfaceLogDao.POSTVALUE, postValue);
        attrs.put(InterfaceLogDao.RETURNVALUE, returnValue);
        attrs.put(InterfaceLogDao.ADDTIME, System.currentTimeMillis()/1000L);
        try {
            return JsonHelper.build(InterfaceLogSrv.me().add(attrs));
        }catch (Exception e){
           return JsonHelper.buildFailed(e.getMessage());
        }
    }
}
