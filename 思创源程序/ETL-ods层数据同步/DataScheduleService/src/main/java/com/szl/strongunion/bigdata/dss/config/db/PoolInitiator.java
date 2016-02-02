package com.szl.strongunion.bigdata.dss.config.db;

import com.jfinal.plugin.activerecord.Config;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.druid.DruidPlugin;
import com.szl.strongunion.bigdata.dss.util.EncAndDecByDES;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * Created by liweiqi on 2015/6/2.
 */
public class PoolInitiator {
    private static PoolInitiator poolInitiator = new PoolInitiator();
    private static Properties properties = new Properties();
    private static FileInputStream fs=null;
    private static EncAndDecByDES enc=new EncAndDecByDES();
    public static PoolInitiator instance() {
        return poolInitiator;
    }
    private PoolInitiator() {
    }

    static {
        try {
//            properties.load(PoolInitiator.class.getClassLoader().getResourceAsStream("jdbc.properties"));
             fs=new FileInputStream("/home/etl_szlsp/szl/BigdataPlatform/pro/ETL/data/ods/daily/jdbc.properties");
            properties.load(fs);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fs!=null){
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static String[] getPoolConfigName() {
        return getPoolConfigName();
    }


    public String[] getRegistPoolConfigName() {
        String[] names = new String[2];
        String time = String.valueOf(new Date().getTime());
        String sourceConfigName = registToConfig(enc.getDesString(properties.getProperty("sourceurl")), enc.getDesString(properties.getProperty("sourceuser")), enc.getDesString(properties.getProperty("sourcepass")),
                enc.getDesString(properties.getProperty("sourcedb")), "_source_" + time);
        String targetConfigName = registToConfig(enc.getDesString(properties.getProperty("targeturl")), enc.getDesString(properties.getProperty("targetuser")), enc.getDesString(properties.getProperty("targetpass")),
                enc.getDesString(properties.getProperty("targetdb")), "_target_" + time);
        names[0] = sourceConfigName;
        names[1] = targetConfigName;
        return names;
    }

    private String registToConfig(String url, String user, String pass, String id, String timePostfix) {
        DruidPlugin plugin = new DruidPlugin(url, user, pass);
        plugin.setInitialSize(29);
        plugin.setMinIdle(29);
        plugin.setMaxActive(100);
        plugin.start();
        String configName = id + timePostfix;
        Config config = new Config(configName, plugin.getDataSource());
        DbKit.addConfig(config);
        return configName;
    }
}
