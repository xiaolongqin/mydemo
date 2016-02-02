package service;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Tyfunwang on 2014/12/28.
 */
public class HttpServ {
    public static Set<String> set = new HashSet<String>();
    private static HttpServ hcs = new HttpServ();

    private HttpServ() {

    }

    public static HttpServ me() {
        return hcs;
    }

    //httpClient
    public String getMethod(String url, Map<String, String[]> map) {
        String strings = null;
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod(getUrl(map, url));

        try {
            int i = httpClient.executeMethod(getMethod);
            if (i == HttpStatus.SC_OK) {
                InputStream in = getMethod.getResponseBodyAsStream();
                strings = IOUtils.toString(in, "UTF-8");
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
        postMethod.getParams().setContentCharset("UTF-8");
        postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        while (it.hasNext()) {
            Map.Entry<String, String[]> entry = it.next();
            postMethod.setParameter(entry.getKey(), entry.getValue()[0]);
        }
        try {

            int i = httpClient.executeMethod(postMethod);
            if (i == HttpStatus.SC_OK) {
                InputStream in = postMethod.getResponseBodyAsStream();
                strings = IOUtils.toString(in, "UTF-8");
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
        postMethod.getParams().setContentCharset("UTF-8");
        try {
            int i = httpClient.executeMethod(postMethod);
            if (i == HttpStatus.SC_OK) {
                InputStream in = postMethod.getResponseBodyAsStream();
                strings = IOUtils.toString(in, "UTF-8");
            }
            return strings;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            postMethod.releaseConnection();
        }
    }

    //set url get
    private String getUrl(Map<String, String[]> map, String URL) {
        Iterator<Map.Entry<String, String[]>> it = map.entrySet().iterator();
        StringBuilder sb = new StringBuilder("?");
        try {
            while (it.hasNext()) {
                Map.Entry<String, String[]> entry = it.next();
                sb.append(URLEncoder.encode(entry.getKey(), "utf-8")).append("=").
                        append(URLEncoder.encode(entry.getValue()[0], "utf-8")).append("&");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        sb.delete(sb.length() - 1, sb.length());
        String url = URL + sb.toString();
        return url;
    }

}
