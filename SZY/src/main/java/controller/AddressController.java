package controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import model.Address;
import service.AddressService;
import util.JsonHelp;

import java.util.List;
/**
 * Created by Tyfunwang on 2014/12/9.
 */
public class AddressController extends Controller {
    AddressService addressService = new AddressService();
    public void getProv(){
        List<Address> addressList = addressService.getProv();
        renderJson(JsonHelp.buildSuccess(JsonKit.toJson(addressList)));
    }
    public void getCity(){
        int code = getParaToInt("code");
        List<Address> addresses = addressService.getCity(code);
        renderJson(JsonHelp.buildSuccess(JsonKit.toJson(addresses)));
    }
    public void getArea(){
        int code = getParaToInt("code");
        List<Address> addresses = addressService.getArea(code);
        renderJson(JsonHelp.buildSuccess(JsonKit.toJson(addresses)));
    }
}
