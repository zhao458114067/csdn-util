package com.zx.csdn;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author: zhaoxu
 * @description:
 */
public class HttpUrlConnectionUtil {
    static private int length;
    static ArrayList<String> ips = new ArrayList();
    /**
     * get请求
     *
     * @param headers 请求头，可为空
     * @param url
     * @return
     * @throws IOException
     */
    public static String get(JSONObject headers, String url) throws IOException {
        String response = "";
        HttpURLConnection httpURLConnection = (HttpURLConnection) (new URL(url).openConnection());
        httpURLConnection.setRequestMethod("GET");
        if (headers != null) {
            Iterator<String> iterator = headers.keySet().iterator();
            while (iterator.hasNext()) {
                String headerName = iterator.next();
                httpURLConnection.setRequestProperty(headerName, headers.get(headerName).toString());
            }
        }
        httpURLConnection.connect();
        if (httpURLConnection.getResponseCode() == 200) {
            InputStream inputStream = httpURLConnection.getInputStream();
            byte[] buffer;
            buffer = new byte[1024];
//            while ((length = inputStream.read(buffer)) != -1) {
//                response = response + new String(buffer, 0, length, "UTF-8");
//            }
            httpURLConnection.disconnect();
        }
        return response;
    }

    /**
     * post请求
     *
     * @param headers 请求头，可为空
     * @param url
     * @param params  post请求体，可为空
     * @return
     * @throws IOException
     */
    public static String post(JSONObject headers, String url, JSONObject params) throws IOException {
        String response = "";
        HttpURLConnection httpURLConnection = (HttpURLConnection) (new URL(url).openConnection());
        httpURLConnection.setRequestMethod("POST");
        if (headers != null) {
            Iterator<String> iterator = headers.keySet().iterator();
            while (iterator.hasNext()) {
                String headerName = iterator.next();
                httpURLConnection.setRequestProperty(headerName, headers.get(headerName).toString());
            }
        }
        httpURLConnection.setDoOutput(true);
        httpURLConnection.connect();
        if (params != null) {
            httpURLConnection.getOutputStream().write(params.toJSONString().getBytes("UTF-8"));
        }
        if (httpURLConnection.getResponseCode() == 200) {
            InputStream inputStream = httpURLConnection.getInputStream();
            byte[] buffer;
            buffer = new byte[1024];
            while ((length = inputStream.read(buffer)) != -1) {
                response = response + new String(buffer, 0, length, "UTF-8");
            }
        }
        httpURLConnection.disconnect();
        return response;
    }

    /**
     * 暂时没有用到
     * @param getIpUrl
     * @return
     * @throws IOException
     */
    public static ArrayList<String> ips(String getIpUrl) throws IOException {
        if ("".equals(getIpUrl)) {
            return ips;
        }
        String path = getIpUrl;// 要获得html页面内容的地址

        URL url = new URL(path);// 创建url对象

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();// 打开连接
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        conn.setRequestProperty("contentType", "GBK"); // 设置url中文参数编码

        conn.setConnectTimeout(5 * 1000);// 请求的时间

        conn.setRequestMethod("GET");// 请求方式

        InputStream inStream = conn.getInputStream();
        // readLesoSysXML(inStream);

        BufferedReader in = new BufferedReader(new InputStreamReader(inStream, "GBK"));
        StringBuffer buffer = new StringBuffer();
        ArrayList<String> ipp = new ArrayList<String>();
        String line = "";
        // 读取获取到内容的最后一行,写入
        while ((line = in.readLine()) != null) {
            buffer.append(line);
            ipp.add(line);
        }
        String str = buffer.toString();
//    JSONObject json1 = JSONObject.parseObject(str);
//    JSONArray jsons =  JSONArray.parseArray(json1.get("data").toString());

//    for(Object json:jsons){
//        JSONObject ips = JSONObject.parseObject(json.toString());
//        String ip = ips.get("IP").toString();
//        System.out.println(ip);
//        ipp.add(ip);
//    }
        ips = ipp;
        return ipp;

    }
}
