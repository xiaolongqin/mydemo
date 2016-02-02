package model;

import com.jfinal.plugin.activerecord.Model;
import java.util.List;
/**
 * Created by Tyfunwang on 2014/12/5.
 */
public class Service extends Model<Service> {
    public static final Service dao = new Service();
    public List<Service> getAll(){
        return dao.find("select servicename from service");
    }
}
