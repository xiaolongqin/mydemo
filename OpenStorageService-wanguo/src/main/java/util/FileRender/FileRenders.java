package util.FileRender;

import com.jfinal.render.Render;
import com.jfinal.render.RenderException;
import org.apache.hadoop.fs.FSDataInputStream;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.jfinal.core.Const.DEFAULT_FILE_CONTENT_TYPE;

/**
 * Created by Tyfunwang on 2015/1/26.
 */
public class FileRenders extends Render {
    private static final long serialVersionUID = 4293616220202691369L;
    private FSDataInputStream fsin;
    private String fName;
    private long len;
    private HttpServletResponse response;
    public FileRenders(long len, FSDataInputStream fsin, String fName, HttpServletResponse response) {
        this.len = len;
        this.fsin = fsin;
        this.fName = fName;
        this.response = response;
    }

    public void render() {
        try {
            String fileName1 = new String(fName.getBytes("utf-8"), "ISO8859-1");//ISO8859-1
            response.addHeader("Content-disposition", "attachment; filename=" + fileName1);
            response.setContentType(DEFAULT_FILE_CONTENT_TYPE);
            response.setContentLength(Integer.valueOf(len + ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(fsin);
            outputStream = response.getOutputStream();
            byte[] buffer = new byte[1024];
            for (int n = -1; (n = inputStream.read(buffer)) != -1; ) {
                outputStream.write(buffer, 0, n);
            }
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RenderException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fsin != null) {
                try {
                    fsin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
