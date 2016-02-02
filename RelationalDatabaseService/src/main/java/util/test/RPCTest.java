package util.test;

import com.unionbigdata.mysqlcluser.jdbc.MysqlClusterService;
import com.unionbigdata.mysqlcluser.jdbc.MysqlClusterServiceRPCFramework;

/**
 * Created by liweiqi on 2014/12/17.
 */
public class RPCTest {
    public static void main(String[] args) throws Exception {
        MysqlClusterService service = MysqlClusterServiceRPCFramework.refer(MysqlClusterService.class, "192.168.2.24", 10101);
       // service.addDiskStorage();

    }
}
