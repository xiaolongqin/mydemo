package model;

import com.jfinal.kit.JsonKit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Tyfunwang on 2014/12/22.
 */
public class ServiceByType implements Serializable {
    public static final String SERVICE_TYPEID = "typeid";
    public static final String SERVICE_TYPENAME = "typename";
    public static final String SERVICE_ID = "serviceid";
    public static final String SERVICE_NAME = "servicename";
    public static final String SERVICE_DESC = "servicedesc";
    public static final String SERVICE_URL = "indexurl";
    private Service service = new Service();
    private ServiceType serviceType = new ServiceType();


    private int typeid;
    private String typename;
    private List<Service> serviceList;


    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public List<Service> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<Service> serviceList) {
        this.serviceList = serviceList;
    }

    //get all services by servicetype
    public List<ServiceByType> allService() {
        List<ServiceByType> serviceByTypes = new ArrayList<ServiceByType>();

        List<ServiceType> serviceTypes = serviceType.find("select * from servicetype");

        //遍历获取serviceList
        Iterator<ServiceType> serviceit = serviceTypes.iterator();
        while (serviceit.hasNext()) {
            ServiceType serviceType = serviceit.next();
            ServiceByType serviceByType = new ServiceByType();

            int typeid = serviceType.getInt("typeid");
            String typename = serviceType.getStr("typename");
            serviceByType.setTypeid(typeid);
            serviceByType.setTypename(typename);

            List<Service> servicesList = service.find("select * from service s,servicerelation sr where sr.serviceid = s.serviceid and sr.typeid = '" + typeid + "'");
            serviceByType.setServiceList(servicesList);

            serviceByTypes.add(typeid - 1, serviceByType);
        }
        return serviceByTypes;
    }

    public String toJson() {
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"typeid\":" + "\"").append(getTypeid()).append("\"" + ",");
        sb.append("\"typename\":" + "\"").append(getTypename()).append("\"" + ",");
        sb.append("\"list\":").append(JsonKit.listToJson(getServiceList(), 8));
        sb.append("}" + ",");
        return sb.toString();
    }
}


