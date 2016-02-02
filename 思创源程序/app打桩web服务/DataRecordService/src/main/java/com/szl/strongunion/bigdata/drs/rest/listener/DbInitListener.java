package com.szl.strongunion.bigdata.drs.rest.listener;

import com.szl.strongunion.bigdata.drs.rest.config.DbModule;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by liweiqi on 2015/5/27.
 */
public class DbInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DbModule.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DbModule.destroy();
    }
}
