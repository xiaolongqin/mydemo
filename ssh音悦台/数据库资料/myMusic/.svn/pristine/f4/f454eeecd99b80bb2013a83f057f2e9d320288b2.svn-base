package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

public class IoUpLoad {

	public static String  myUpLoad(String location,List<String> uploadFileName,List<File> upload ) throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		ServletContext application = request.getServletContext();
		String realpath = application.getRealPath(location);
		//String realpath = ServletActionContext.getRequest().getServletContext().getRealPath(location);  

		
		File file = new File(realpath);
         
		//System.out.println(realpath);//ÎÄ¼þ´æ´¢Â·¾¶

		if (!file.exists()) {
			file.mkdirs();
		}
		for(int i=0;i<upload.size();i++){
			
			FileInputStream is = new FileInputStream(upload.get(i));
			FileOutputStream os = new FileOutputStream(realpath + "\\"+uploadFileName.get(i));
			byte[] b = new byte[1024];
			int len = is.read(b);
			while (len != -1) {
				os.write(b, 0, len);
				len = is.read(b);
			}
			is.close();
			os.close();
		}
		return realpath;
	}
}
