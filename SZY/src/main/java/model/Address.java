package model;

import com.jfinal.plugin.activerecord.Model;
import java.util.List;
/**
 * Created by Tyfunwang on 2014/12/9.
 */
public class Address extends Model<Address>{
    private static final String ADDRESS_CODE = "code";
    private static final String ADDRESS_NAME = "name";
    private static final String ADDRESS_ID = "id";
    private static final String ADDRESS_PARENTID = "parentId";
    private static final String ADDRESS_TABLE = "t_address";
    /**
     * 用来获取城市
     * */

    private static final Address dao = new Address();
    //get prov
    public List<Address> getProv(){
        List<Address> addresses = dao.find("select "+ADDRESS_ID+","+ADDRESS_NAME+","+ADDRESS_CODE+" from "+ADDRESS_TABLE+" where "+ADDRESS_PARENTID+" = "+0+"");
        return addresses;
    }
    //get city
    public List<Address> getCity(int code){
        List<Address> addresses = dao.find("select "+ADDRESS_ID+","+ADDRESS_NAME+","+ADDRESS_CODE+" from "+ADDRESS_TABLE+" where "+ADDRESS_PARENTID+" ="+code);
        return addresses;
    }
    //get city
    public List<Address> getArea(int code){
        List<Address> addresses = dao.find("select "+ADDRESS_ID+","+ADDRESS_NAME+","+ADDRESS_CODE+" from "+ADDRESS_TABLE+" where "+ADDRESS_PARENTID+" ="+code);
        return addresses;
    }
 }
