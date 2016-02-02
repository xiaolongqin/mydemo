package service.sql;


/**
 * Created by liweiqi on 2014/12/5.
 */
public class SQLTansException extends RuntimeException {
    private static final long serialVersionUID = 142820722361248621L;

    public SQLTansException(String message) {
        super(message);
    }

    public SQLTansException(Throwable cause) {
        super(cause);
    }

    public SQLTansException(String message, Throwable cause) {
        super(message, cause);
    }
}
