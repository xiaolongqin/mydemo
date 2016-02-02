package util.FileRender;

import com.jfinal.render.Render;
import com.jfinal.render.RenderException;
import org.apache.tools.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

import static com.jfinal.core.Const.DEFAULT_FILE_CONTENT_TYPE;

/**
 * Created by Tyfunwang on 2015/1/26.
 */
public class FileRender extends Render {
    private static final long serialVersionUID = 4293616220202691369L;
    private String fName;
    private String loadPath;
    private HttpServletResponse response;

    public FileRender( String fName, HttpServletResponse response, String loadPath) {
        this.loadPath = loadPath;
        this.fName = fName;
        this.response = response;
    }

    public  void  render() {
        String fileName = fName;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            File file = new File(loadPath +fileName);
            inputStream = new BufferedInputStream(new FileInputStream(file));

            String fileName1 = new String(fName.getBytes("utf-8"), "ISO8859-1");//ISO8859-1
            response.addHeader("Content-disposition", "attachment; filename=" + fileName1);
            response.setContentType(DEFAULT_FILE_CONTENT_TYPE);
            response.setContentLength(Integer.valueOf(file.length() + ""));
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            outputStream = response.getOutputStream();
            new ZipOutputStream(outputStream);
            byte[] buffer = new byte[1024];
            assert inputStream != null;
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

        }
    }
}
