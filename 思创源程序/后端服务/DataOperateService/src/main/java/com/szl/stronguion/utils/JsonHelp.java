package com.szl.stronguion.utils;

/**
 * Created by Administrator on 2014/11/18.
 */
public class JsonHelp {
    public  static final String SUCCESS ="{\"state\":true,\"msg\":null,\"data\":null}";
    public  static final String FAILED = "{\"state\":false,\"msg\":null,\"data\":null}";
    public  static final String EXCEPTION = "exception";


    public static String buildSuccess(){
        return SUCCESS;
    }

    public static String buildSuccess(String data){
        return new StringBuilder().append("{\"state\":true,\"msg\":null,")
                .append("\"data\":").append(data).append("}").toString();
    }


    public static String buildSuccess(String msg,String data){
        return new StringBuilder().append("{\"state\":true,\"msg\":\"").append(msg).append("\",").append("\"data\":").append(data).append("}").toString();
    }


    public static String buildFailed(){
        return FAILED;
    }

    public static String buildFailed(String msg) {
        return new StringBuilder("{\"state\":false,\"msg\":\"").append(msg).append("\",")
                .append("\"data\":null}").toString();
    }

    public static String buildFailed(String msg, String data) {
        return new StringBuilder("{\"state\":false,\"msg\":\"").append(msg).append("\",")
                .append("\"data\":").append(data).append("}").toString();
    }

}
