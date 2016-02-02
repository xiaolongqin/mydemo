package com.szl.strongunion.bigdata.drs.rest.config;

import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.szl.strongunion.bigdata.drs.rest.dao.KeyWordSearchDao;
import com.szl.strongunion.bigdata.drs.rest.dao.PageActionDao;
import com.szl.strongunion.bigdata.drs.rest.dao.PageDao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by liweiqi on 2015/5/27.
 */
public class DbModule {
    private static List<IPlugin> plugins = new ArrayList<IPlugin>(2);

    public static void init() {
        Properties p = loadProperties();
        DruidPlugin druidPlugin = new DruidPlugin(p.getProperty("jdbcurl"), p.getProperty("user"), p.getProperty("pass"));
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        arp.addMapping(PageDao.TABLENAME, PageDao.ID, PageDao.class);
        arp.addMapping(PageActionDao.TABLENAME, PageActionDao.ID, PageActionDao.class);
        arp.addMapping(KeyWordSearchDao.TABLENAME,KeyWordSearchDao.ID,KeyWordSearchDao.class);
        plugins.add(druidPlugin);
        plugins.add(arp);
        druidPlugin.start();
        arp.start();
    }

    public static void destroy() {
        for (IPlugin plugin : plugins) {
            plugin.stop();
        }

    }

    private DbModule() {

    }

    private static Properties loadProperties() {
        Properties p = new Properties();
        try {
            p.load(DbModule.class.getClassLoader().getResourceAsStream("jdbc.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }
}
