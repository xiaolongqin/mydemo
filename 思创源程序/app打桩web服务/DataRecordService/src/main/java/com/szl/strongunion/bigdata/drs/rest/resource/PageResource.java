package com.szl.strongunion.bigdata.drs.rest.resource;

import com.jfinal.kit.JsonKit;
import com.szl.strongunion.bigdata.drs.rest.dao.PageDao;
import com.szl.strongunion.bigdata.drs.rest.service.PageActionSrv;
import com.szl.strongunion.bigdata.drs.rest.service.PageSrv;
import com.szl.strongunion.bigdata.drs.rest.util.CollectionUtil;
import com.szl.strongunion.bigdata.drs.rest.util.JsonHelper;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Map;

/**
 * Created by liweiqi on 2015/5/27.
 */
@Path("/page")
public class PageResource {
    @GET
    @Path("/get/{id:.*}")
    public String getById(@PathParam("id") String id
    ) {
        try {
            PageDao pageDao = PageSrv.me().findById(id);
            String result = pageDao == null ? "" : pageDao.toJson();
            return JsonHelper.buildSuccess(result);
        } catch (Exception ex) {
            return JsonHelper.buildFailed(ex.getMessage());
        }
    }

    @GET
    @Path("/get")
    public String getInPage(@QueryParam("page_number") @DefaultValue("1") int pageNumber,
                            @QueryParam("page_size") @DefaultValue("10") int pageSize
    ) {
        try {
            return JsonHelper.buildSuccess(JsonKit.toJson(PageSrv.me().findInPage(pageNumber, pageSize)));
        } catch (Exception ex) {
            return JsonHelper.buildFailed(ex.getMessage());
        }
    }

    @GET
    @Path("/get/{id:.*}/actions")
    public String getActionsByPage(@PathParam("id") String pageId,
                                   @QueryParam("page_number") @DefaultValue("1") int pageNumber,
                                   @QueryParam("page_size") @DefaultValue("10") int pageSize
    ) {
        try {
            return JsonHelper.buildSuccess(JsonKit.toJson(PageActionSrv.me().findInPage(pageNumber, pageSize, pageId)));
        } catch (Exception ex) {
            return JsonHelper.buildFailed(ex.getMessage());
        }
    }

    @POST
    @Path("/post")
    @Produces(MediaType.APPLICATION_JSON)
    public String post(MultivaluedMap<String, String> formParas
    ) {
        Map<String, Object> attrs = CollectionUtil.multiValueMapToMap(formParas);
        try {
            return JsonHelper.build(PageSrv.me().add(attrs));
        } catch (Exception ex) {
            return JsonHelper.buildFailed(ex.getMessage());
        }
    }

    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public String update(MultivaluedMap<String, String> formParas) {
        Map<String, Object> attrs = CollectionUtil.multiValueMapToMap(formParas);
        try {
            return JsonHelper.build(PageSrv.me().update(attrs));
        } catch (Exception ex) {
            return JsonHelper.buildFailed(ex.getMessage());
        }
    }
}
