package com.szl.strongunion.bigdata.drs.rest.resource;

import com.szl.strongunion.bigdata.drs.rest.service.KeyWordSearchSrv;
import com.szl.strongunion.bigdata.drs.rest.util.CollectionUtil;
import com.szl.strongunion.bigdata.drs.rest.util.JsonHelper;

import javax.ws.rs.*;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Map;

/**
 * Created by liweiqi on 2015/7/14.
 */
@Path("/search_key")
public class KeyWordSearchResource {
    @Path("/post")
    @Produces("application/json")
    @POST
    public String post(MultivaluedMap<String, String> formParas
    ) {
        Map<String, Object> attrs = CollectionUtil.multiValueMapToMap(formParas);
        try {
            return JsonHelper.build(KeyWordSearchSrv.me().add(attrs));
        } catch (Exception ex) {
            return JsonHelper.buildFailed(ex.getMessage());
        }
    }
}
