package service;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import java.io.IOException;
import java.net.URI;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Tyfunwang on 2015/1/19.
 */
public class RpcService {
    private static RpcService rpcService = new RpcService();
    private  Properties p = new Properties();
    private ExecutorService tt = Executors.newFixedThreadPool(10);

    private RpcService() {
        try {
            p.load(RpcService.class.getClassLoader().getResourceAsStream("rpc.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static RpcService getRpcSer() {
        return rpcService;
    }

    public FileSystem getFileSys() throws Exception {

        Configuration conf = new Configuration();
        String ip = p.getProperty("ip");
        int port = Integer.valueOf(p.getProperty("port"));
        String Uri = "hdfs://" + ip + ":" + port;
        conf.set("fs.default.name", Uri);
        return FileSystem.get(URI.create(Uri), conf);
    }

//    public FileSystem getFileSys() {
//        FileSystem fs  =null;
//        Future<FileSystem> future = tt.submit(new Callable<FileSystem>() {
//            @Override
//            public FileSystem call() throws Exception {
//                Configuration conf = new Configuration();
//                String ip = p.getProperty("ip");
//                int port = Integer.valueOf(p.getProperty("port"));
//                String Uri = "hdfs://" + ip + ":" + port;
//                conf.set("fs.default.name", Uri);
//                return FileSystem.get(URI.create(Uri), conf);
//            }
//        });
//        try {
//            while (!future.isDone()){
//                Thread.sleep(1000);
//            }
//            fs =  future.get();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return fs;
//    }

    public Configuration getConf() throws IOException {
        Configuration conf = new Configuration();

        String ip = p.getProperty("ip");
        int port = Integer.valueOf(p.getProperty("port"));
        String Uri = "hdfs://" + ip + ":" + port;

        conf.set("fs.default.name", Uri);
        return conf;
    }

    public String getUri() throws Exception {
        return getFileSys().getUri() + "";
    }

    public void closeFileSys(FileSystem fs) throws IOException {
        if (fs != null) fs.close();
    }
}


