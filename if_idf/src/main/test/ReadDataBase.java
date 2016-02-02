import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.GetConnection;
import utils.FormatUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by Tyfunwang on 2015/8/11.*
 * Load sql database data to hive*
 * Now mysql and oracle are supported*
 */
public class ReadDataBase {

    private static final Logger LOG = LoggerFactory.getLogger(ReadDataBase.class);
    private String tempInputPath;
    private String TABLE_NAME = null;
    static Properties properties = new Properties();
    {
        try {
            properties.load(GetConnection.class.getClassLoader().getResourceAsStream("db.properties"));
            TABLE_NAME = properties.getProperty("tableName") + FormatUtils.getMonth(0);
            tempInputPath = TABLE_NAME + "/sqoopwork_/" + System.currentTimeMillis();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected void doWork() throws IOException, InterruptedException {
        //get Sqoop command line string
        List<String> argList = new ArrayList<>();
        argList.add("sqoop");
        argList.add("import");
        argList.add("--connect");
        argList.add(properties.getProperty("jdbcUrl"));
        argList.add("--username");
        argList.add(properties.getProperty("jdbcName"));
        argList.add("--password");
        argList.add(properties.getProperty("jdbcPass"));
        argList.add("--table");
        argList.add(TABLE_NAME);
        argList.add("--target-dir");
        argList.add(tempInputPath);
        argList.add("--outdir");
        argList.add(tempInputPath);
        argList.add("--hive-database");
        argList.add(properties.getProperty("hiveDatabase"));
        argList.add("--hive-table");
        argList.add(TABLE_NAME);
        argList.add("--hive-import");
//        argList.add("--create-hive-table");
        String[] arg = new String[argList.size()];
        argList.toArray(arg);

        //execute sqoop
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(arg);
        } catch (IOException e) {
            LOG.error("", e);
            throw new IOException("Sqoop execute with " + Arrays.toString(arg) + " failed!");
        }
        
        int ret = process != null ? process.waitFor() : -1;
        
        if (0 != ret) {
            LOG.error("Sqoop execute with " + Arrays.toString(arg) + " failed!");
            throw new IOException("Sqoop execute with " + Arrays.toString(arg) + " failed!");
        }
    }


//    @Override
//    protected void cleanWork() {
//        if (tempInputPath != null) {
//            deleteDirSilently(tempInputPath);
//        }
//    }
}
