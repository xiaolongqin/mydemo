package service;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Tyfunwang on 2014/12/28.
 */
public class HttpClientService {
    public static Set<String> set = new HashSet<String>();

    //httpClient
    public String getMethod(String url, Map<String, String[]> map) {
        String strings = null;
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod(getUrl(map, url));
        try {
            int i = httpClient.executeMethod(getMethod);
            if (i == HttpStatus.SC_OK) {
                InputStream in = getMethod.getResponseBodyAsStream();
                strings = IOUtils.toString(in, "utf-8");
            }
            return strings;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            getMethod.releaseConnection();
        }
    }

    //httpClient
    public String postMethod(String url, Map<String, String[]> map) {
        String strings = null;
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        Iterator<Map.Entry<String, String[]>> it = map.entrySet().iterator();
        //postMethod.getParams().setContentCharset("utf-8");
        while (it.hasNext()) {
            Map.Entry<String, String[]> entry = it.next();
            postMethod.setParameter(entry.getKey(), entry.getValue()[0]);
        }
        try {

            int i = httpClient.executeMethod(postMethod);
            if (i == HttpStatus.SC_OK) {
                InputStream in = postMethod.getResponseBodyAsStream();
                strings = IOUtils.toString(in, "utf-8");
            }
            return strings;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            postMethod.releaseConnection();
        }
    }

    //httpClient
    public String postMethod(String str) {
        String strings = null;
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(str);
        postMethod.getParams().setContentCharset("utf-8");
        try {

            int i = httpClient.executeMethod(postMethod);
            if (i == HttpStatus.SC_OK) {
                InputStream in = postMethod.getResponseBodyAsStream();
                strings = IOUtils.toString(in, "utf-8");
            }
            return strings;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            postMethod.releaseConnection();
        }
    }


    //set url for rds
    private String getUrl(Map<String, String[]> map, String URL) {
        Iterator<Map.Entry<String, String[]>> it = map.entrySet().iterator();
        StringBuilder sb = new StringBuilder("?");
        while (it.hasNext()) {
            Map.Entry<String, String[]> entry = it.next();
            sb.append(entry.getKey()).append("=").append(entry.getValue()[0]).append("&");
        }
        String url = URL + sb.toString();
        return url;
    }

}
