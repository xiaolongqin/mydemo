package com.szl.strongunion.bigdata.drs.rest.resource;

import com.jfinal.kit.JsonKit;
import com.jfinal.kit.StrKit;
import com.szl.strongunion.bigdata.drs.rest.dao.PageActionDao;
import com.szl.strongunion.bigdata.drs.rest.util.CollectionUtil;
import com.szl.strongunion.bigdata.drs.rest.util.JsonHelper;
import com.szl.strongunion.bigdata.drs.rest.service.PageActionSrv;

import javax.ws.rs.*;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Map;

/**
 * Created by liweiqi on 2015/5/27.
 */
@Path("/page_action")
public class PageActionResource {

    @GET
    @Path("/get/{id:.*}")
    public String getById(@PathParam("id") String id) {
        if (StrKit.isBlank(id)) return JsonHelper.buildSuccess();
        try {
            return JsonHelper.buildSuccess(PageActionSrv.me().findById(id).toJson());
        } catch (Exception ex) {
            return JsonHelper.buildFailed(ex.getMessage());
        }
    }

    @GET
    @Path("/get")
    public String getAllInPage(@QueryParam("page_number") @DefaultValue("1") int pageNumber,
                               @QueryParam("page_size") @DefaultValue("10") int pageSize) {
        try {
            return JsonHelper.buildSuccess(JsonKit.toJson(PageActionSrv.me().findInPage(pageNumber, pageSize), 5));
        } catch (Exception ex) {
            return JsonHelper.buildFailed(ex.getMessage());
        }
    }

    @Path("/post")
    @Produces("application/json")
    @POST
    public String create(MultivaluedMap<String, String> formParas
    ) {
        Map<String, Object> attrs = CollectionUtil.multiValueMapToMap(formParas);
        try {
            if (PageActionSrv.me().findByActionId(attrs.get(PageActionDao.ID).toString())){
                return JsonHelper.build(true);
            }
            return JsonHelper.build(PageActionSrv.me().add(attrs));
        } catch (Exception ex) {
            return JsonHelper.buildFailed(ex.getMessage());
        }
    }

    @Path("/update")
    @Produces("application/json")
    @POST
    public String update(MultivaluedMap<String, String> formParas
    ) {
        Map<String, Object> attrs = CollectionUtil.multiValueMapToMap(formParas);
        try {
            return JsonHelper.build(PageActionSrv.me().update(attrs));
        } catch (Exception ex) {
            return JsonHelper.buildFailed(ex.getMessage());
        }

    }
}
