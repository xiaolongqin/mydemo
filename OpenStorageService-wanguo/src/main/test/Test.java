import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import service.FileDescSrv;
import util.FormatUtils;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Tyfunwang on 2015/1/16.
 */
public class Test {
    @org.junit.Test
    public void splitName(){
        String name = "http://www.cs.cornell.edu/projects/kddcup/download/hep-th-2002.tar.gz";
        
        String[] split = name.split("/");
        
        split[split.length-1]= "文件类型.txt";
        String newname = "";
        for(String s : split){
            newname += s+"/";
        }
        System.out.println("newname:"+newname.substring(0,newname.length()-1));
    }
    public void getDesc(){
         FileDescSrv fileDescSrv =  FileDescSrv.getInstance();
        System.out.println(fileDescSrv.getFileDesc("文件类型.txt"));
        
    }
    public void  foreach(){
        for (int i = 100; i>0 ;i--){
            if (i>10){
                if (i<50){
                    System.out.println("第二个if"+i+"break");
                    break;
                }
                System.out.println("第一个if"+i);
            }
            
        }
        
    }
    public void converst(){
        System.out.println("/");
        
    }
    public void suStringTest(){
        String s = "5 B";

        System.out.println(s.length());
        String str = s.substring(0,s.length()-2);
        System.out.println(str);
    }
    public void get() throws IOException {
        Properties properties = new Properties();
        properties.load(Test.class.getClassLoader().getResourceAsStream("jdbc.properties"));
        System.out.println(properties.getProperty("jdbcUrl"));
    }

   public void cutStr(){
        double f = 0.000007;
//        f=Math.floor(f * 10)/10;
        float  b   =  (float)(Math.round(f*10))/10;
        System.out.println(b);
   }
   public void steString(){
       String sss = "{\"dirUrl\":[\"/12313\",\"/asdfasdf\"]}";
       JSONObject json = JSONArray.parseObject(sss);
       JSONArray jsonA = JSONArray.parseArray(json.getString("dirUrl"));
       for (int i = 0; i <jsonA.size() ; i++) {
           System.out.println(jsonA.getString(i));
       }
//       System.out.println(json.get("dirUrl"));

   }
   public void testFile(){
       File file = new File("C:\\Users\\Administrator\\Desktop\\js.txt");
       System.out.println(file.getName());
   }
   public void splitStr(){

       String s2 = "C:\\Users\\Administrator\\Desktop\\java.txt";
       String[] strings2 = s2.split("\\\\");
       System.out.println(strings2.length+"name:"+strings2[strings2.length-1]);
   }
    public void testTime(){
       long time = System.currentTimeMillis();
       System.out.println(FormatUtils.time2String(time));

    }
}
