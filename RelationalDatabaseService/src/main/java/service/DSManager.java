package service;

import com.alibaba.druid.pool.DruidDataSource;
import com.jfinal.plugin.activerecord.Config;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.activerecord.DbPro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liweiqi on 2014/10/21.
 */
@Deprecated
public class DSManager {
    public final static String LOCAL = "local_root";
    public final static String REMOTE = "remote_root";
    private static DSManager manager = new DSManager();

    // contain user's data source to manager their life-cycle
    private final Map<String, DataSource> dataSourceStore = new ConcurrentHashMap<>();

    //idle time of the data-source
    private final Map<String, Long> idleTimeStore = new ConcurrentHashMap<>();

    private ExecutorService executors = Executors.newFixedThreadPool(50);

    static Logger logger = LoggerFactory.getLogger(DSManager.class);

    private final Properties p = new Properties();

    private boolean isMonitorStarted = false;

    private final long timeToClose = 5 * 1000;

    private final long timeToClear = 10 * 1000;

    private DSManager() {
        try {
            p.load(DSManager.class.getClassLoader().getResourceAsStream("mysqlurl.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public void imBusy(String name) {
        if (!isMonitorStarted) return;
        idleTimeStore.put(name, System.currentTimeMillis());
    }

    @Deprecated
    public boolean isBusy(String name) {
        DruidDataSource dataSource = (DruidDataSource) dataSourceStore.get(name);
        int count = dataSource.getActiveCount();
        return count > 0;
    }

    public static DSManager getInstance() {
        return manager;
    }

    @Deprecated
    /**
     * create new data source,identified by user name
     */
    public final boolean createDataSource(String dname, String name, String pass) {
        if (dataSourceStore.containsKey(name)) return false;
        if (!cleanCacheFromJfinal(name)) {
            logger.warn("failed to clear cache while "
                    + name + " data-source created");
            return false;
        }
        DruidDataSource ds = new DruidDataSource();
        initDataSource(ds, name, pass);
        try {
            ds.init();
            DbKit.addConfig(new Config(dname, ds));
            dataSourceStore.put(dname, ds);
            isBusy(dname);
        } catch (Exception ex) {
            ds.close();
            return false;
        }
        return true;
    }

    @Deprecated
    /**
     * close druid data source
     */
    public final boolean closeDataSource(String name) {
        DataSource ds = dataSourceStore.get(name);
        //maybe data source has been already cleared
        if (ds == null) {
            boolean state = cleanCacheFromJfinal(name);
            logger.warn(name + " failed to close the  null data-source at "
                    + new Date() + ": is cache cleared ? " + state);
            return false;
        }
        DruidDataSource druidDS = (DruidDataSource) ds;
        druidDS.close();
        return true;
    }

    @Deprecated
    /**
     * start druid data source identified by name
     */

    public final boolean startDataSource(String name) {
        DataSource ds = dataSourceStore.get(name);
        DruidDataSource druidDs = (DruidDataSource) ds;
        try {
            druidDs.restart();
            isBusy(name);
        } catch (Exception ex) {
            logger.warn(name + " failed to restart at "
                    + new Date() + "  " + ex.getMessage());
            druidDs.close();
        }
        return true;
    }

    @Deprecated
    /**
     * clear data-source and cache
     */
    public final boolean clearDataSource(String name) {
        DataSource ds = dataSourceStore.get(name);
        if (!(ds == null)) {
            DruidDataSource druidDS = (DruidDataSource) ds;
            druidDS.close();
            dataSourceStore.remove(name);
            idleTimeStore.remove(name);
        }
        return cleanCacheFromJfinal(name);
    }

    @Deprecated
    /**
     * init common data source properties
     */
    private void initDataSource(DruidDataSource dataSource, String name, String pass) {
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306");
        dataSource.setUsername(name);
        dataSource.setPassword(pass);
        dataSource.setMaxActive(3);
        dataSource.setMinIdle(1);
        dataSource.setConnectProperties(p);
        dataSource.setTimeBetweenEvictionRunsMillis(3 * 1000L);
    }

    @Deprecated
    /**
     * clean cache in jfinal's class
     */
    private boolean cleanCacheFromJfinal(String name) {
        return clearConfigCache(name) && cleanDbProCache(name);
    }

    @Deprecated
    /**
     * use reflect to clean DbPro cache
     */
    private boolean cleanDbProCache(String name) {
        try {
            DbPro.use(name);
        } catch (IllegalArgumentException ex) {
            return true;
        }
        Class<DbPro> clazz = DbPro.class;
        try {
            Field field = clazz.getDeclaredField("map");
            field.setAccessible(true);
            Map<String, Config> configs = (Map<String, Config>) field.get(null);
            configs.remove(name);
            System.out.println(123);
        } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Deprecated
    /**
     * use reflect to clean config cache
     */
    private boolean clearConfigCache(String name) {
        if (DbKit.getConfig(name) == null) return true;
        Class<DbKit> clazz = DbKit.class;
        try {
            Field field = clazz.getDeclaredField("configNameToConfig");
            field.setAccessible(true);
            Map<String, Config> configs = (Map<String, Config>) field.get(null);
            configs.remove(name);
        } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Deprecated
    /**
     * start monitor to close data-source in long-time idle
     * here is 10 minutes
     */
    private void startMonitor() {
        if (isMonitorStarted) return;
        isMonitorStarted = true;
        executors.execute(new Runnable() {
                              @Override
                              public void run() {
                                  while (isMonitorStarted) {
                                      Iterator<Map.Entry<String, Long>> it = idleTimeStore.entrySet().iterator();
                                      while (it.hasNext()) {
                                          Map.Entry<String, Long> entry = it.next();
                                          final String name = entry.getKey();
                                          Long lastTime = entry.getValue();
                                          Long timeNow = System.currentTimeMillis();
                                          if (isBusy(name)) continue;
                                          Long idle = timeNow - lastTime;
                                          if (idle < timeToClear && idle >= timeToClose) {
                                              closeDataSource(name);
                                          }
                                          if (idle > timeToClear) {
                                              clearDataSource(name);
                                              it.remove();
                                          }
                                      }
                                  }
                              }
                          }
        );
    }

    @Deprecated
    private void stopMonitor() {
        if (isMonitorStarted) isMonitorStarted = false;
    }

    @Deprecated
    public static void main(String[] args) throws Exception {
    }
}
