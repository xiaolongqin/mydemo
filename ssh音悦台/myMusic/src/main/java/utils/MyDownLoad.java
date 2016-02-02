package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//‘≠…˙Ã¨œ¬‘ÿ
public class MyDownLoad {

	public String downFile(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		response.setContentType("application/x-download");
		String g_id = request.getParameter("goodsId");
		String index = request.getParameter("index");
		
		OutputStream out = response.getOutputStream();
		String file="";
		if("1".equals(index)){
			file = request.getRealPath("/").replaceAll("\\\\","/" )+"resource/cydp.pdf";
		}else if("2".equals(index)){
			file = request.getRealPath("/").replaceAll("\\\\","/" )+"resource/dxbc.pdf";
			
		}else if("3".equals(index)){
			file = request.getRealPath("/").replaceAll("\\\\","/" )+"resource/cydp.pdf";
			
		}else{
			return "r:error.html";
		}
		
		InputStream input = new FileInputStream(file);
		int i = -1;
		byte[] b = new byte[1024*1024];
		while((i = input.read(b)) != -1){
			out.write(b,0,i);
		}
		
		input.close();
		out.flush();
		out.close();
		return "r:goodsdetail.do?v=queryGoodsInfo&g_id="+g_id;
		
	}
	
}
