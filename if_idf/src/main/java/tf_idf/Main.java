package tf_idf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Administrator on 2015/6/23.
 */

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    //tfidf输出
    public static void main(String[] args){
        try {
            new TfIdfResult().getTfIdf();
        } catch (IOException e) {
            LOG.error("TFIDF输出异常！", e);
        }
    }
}