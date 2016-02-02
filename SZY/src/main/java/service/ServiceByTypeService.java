package service;

import model.ServiceByType;

import java.util.List;

/**
 * Created by Tyfunwang on 2014/12/22.
 */
public class ServiceByTypeService {
    ServiceByType serviceByType = new ServiceByType();
    //get all service by type
    public String  allService(){
        try {
            List<ServiceByType> serviceByTypeList = serviceByType.allService();
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < serviceByTypeList.size(); i++) {
                sb.append(serviceByTypeList.get(i).toJson());
            }
            if(sb.length() > 0) sb.setLength(sb.length()-1);
            sb.append("]");
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
