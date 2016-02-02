package service.db;

import java.sql.Connection;

/**
 * Created by liweiqi on 2014/12/1.
 */
public interface RDSIAtom {
    public boolean run(Connection conn);
}
