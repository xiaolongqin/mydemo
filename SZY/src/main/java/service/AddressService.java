package service;

import model.Address;
import java.util.List;
/**
 * Created by Tyfunwang on 2014/12/9.
 */
public class AddressService {
    Address address = new Address();
    public List<Address> getProv(){
            return address.getProv();
    }
    public List<Address> getCity(int code){
        return address.getCity(code);
    }
    public List<Address> getArea(int code){
        return address.getArea(code);
    }
}
