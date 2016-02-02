package util;

import com.google.gson.Gson;
import model.Service;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tyfunwang on 2014/12/8.
 */
public class ReadXml {

    ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    private Runnable runnable = new Runnable() {
        String str = null;

        @Override
        public void run() {


        }
    };

    public void start() {
        service.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.SECONDS);
    }


    public String read() {
        List<Service> serviceList = new ArrayList<Service>();
        Gson gson = new Gson();
        SAXReader reader = new SAXReader();

        InputStream in = ClassLoader.getSystemResourceAsStream("services.xml");
        StringBuilder sb = new StringBuilder();

        try {
            Document document = reader.read(in);
            Element users = document.getRootElement();

            for (Iterator i = users.elementIterator(); i.hasNext(); ) {
                Service service = new Service();
                Element user = (Element) i.next();

                String typeid = user.element("typeid").getText();
                String typename = user.element("typename").getText();
                String serviceid = user.element("serviceid").getText();
                String name = user.element("name").getText();
                String url = user.element("url").getText();
                String des = user.element("description").getText();

                serviceList.add(service);
            }
        } catch (DocumentException e) {
            System.out.println(e.getMessage());
        }
        //封装json
        String str = gson.toJson(serviceList);
        return str;
    }


}
