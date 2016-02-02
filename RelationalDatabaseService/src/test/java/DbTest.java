
import com.jfinal.plugin.activerecord.Record;
import org.junit.Assert;
import service.db.RDSDb;
import service.db.RDSIAtom;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by liweiqi on 2014/12/2.
 */
public class DbTest {
    // @Test
    public void insert() {
        try {
            DataSource ds = DSInit.init();
            Record record = new Record();
            record.set("email", "123@qq.com");
            record.set("phone", "12434");
            record.set("name", "which");
            boolean state = RDSDb.save(ds.getConnection(), "contact", record);
            Assert.assertTrue(state);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //@Test
    public void delete() {
        try {
            DataSource ds = DSInit.init();
            boolean state = RDSDb.deleteById(ds.getConnection(), "contact", 2);
            Assert.assertTrue(state);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //@Test
    public void update() {
        try {
            DataSource ds = DSInit.init();
            int state = RDSDb.update(ds.getConnection(), "update  contact set name=? where id=1", "which77");
            Assert.assertTrue(state > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //@Test
    public void select() {
        try {
            DataSource ds = DSInit.init();
            List<Record> records = RDSDb.find(ds.getConnection(), "select * from contact");
            Assert.assertTrue("which77".equals(records.get(0).getStr("name")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // @Test
    public void transaction() {
        try {
            DataSource ds = DSInit.init();
            RDSDb.tx(ds.getConnection(), new RDSIAtom() {
                @Override
                public boolean run(Connection conn) {
                    return RDSDb.update(null, "update contact set name=? where id=1", "error") >= 0;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            DataSource ds = DSInit.init();
            RDSDb.update(ds.getConnection(), "update contact set name=? where id=1", "error");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
