package com.szl.strongunion.bigdata.drs.rest.config;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * Created by liweiqi on 2015/5/27.
 */
public class ResourceConfigExt extends ResourceConfig {
    public ResourceConfigExt() {
        packages("com.szl.strongunion.bigdata.rest.resource");
    }
}
