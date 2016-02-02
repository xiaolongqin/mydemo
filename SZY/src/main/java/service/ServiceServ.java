package service;


import model.Service;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/1/30.
 */
public class ServiceServ {
    public Service service = new Service();

    public List<Service> getAll() {
        return service.getAll();
    }
}
