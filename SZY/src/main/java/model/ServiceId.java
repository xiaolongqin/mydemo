package model;

import com.jfinal.plugin.activerecord.Model;

/**
 * Created by Tyfunwang on 2014/12/17.
 */
public class ServiceId extends Model<ServiceId> {
    public String der;
    public String serviceid;

    public ServiceId() {
    }

    public String getDer() {
        return der;
    }

    public void setDer(String der) {
        this.der = der;
    }

    public String getServiceid() {
        return serviceid;
    }

    public void setServiceid(String serviceid) {
        this.serviceid = serviceid;
    }
}
