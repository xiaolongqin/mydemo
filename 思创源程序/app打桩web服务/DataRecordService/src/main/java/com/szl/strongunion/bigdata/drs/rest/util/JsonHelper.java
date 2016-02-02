package com.szl.strongunion.bigdata.drs.rest.util;

/**
 * Created by liweiqi on 2015/5/27.
 */
public class JsonHelper {
    private JsonHelper(){}
    public  static final String SUCCESS ="{\"state\":true,\"msg\":null,\"data\":null}";
    public  static final String FAILED = "{\"state\":false,\"msg\":null,\"data\":null}";


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
        StringBuilder ssb=msgChange(msg);
        return new StringBuilder("{\"state\":false,\"msg\":\"").append(ssb).append("\",")
                .append("\"data\":null}").toString();
    }

    public static String buildFailed(String msg, String data) {
        return new StringBuilder("{\"state\":false,\"msg\":\"").append(msg).append("\",")
                .append("\"data\":").append(data).append("}").toString();
    }



    public static String build(boolean state) {
        return new StringBuilder("{\"state\":" + state + ",\"msg\":\"\",")
                .append("\"data\":").append("null").append("}").toString();
    }

    public static StringBuilder msgChange(String  msg) {
        if (msg==null){
            return null;
        }
        StringBuilder ssb = new StringBuilder();
        for (Character character : msg.toCharArray()) {
            if (character.toString().equals("\"") || character.toString().equals("\'")) continue;
            ssb.append(character);
        }
        return  ssb;
    }

    public static void main(String[] args) {
        String s="for inpu: \"asdad\"asdasd\'haha\'";
        System.out.println(buildFailed(s));
    }
}
