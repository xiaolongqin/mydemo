package service;

import model.User;

/**
 * Created by Tyfunwang on 2014/12/31.
 */
public class ControllerService {
    //utils
    public User formatUser(User user) {
        return user.remove("address", "classofindustry", "comname", "fixedline"
                , "identitycard", "primarybusiness", "websiteurl", "time", "type", "pwd", "authentication", "status");
    }
}
