package com.zx.csdn;

import com.alibaba.fastjson.JSONObject;
import com.sun.webkit.network.CookieManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.util.*;

/**
 * @author: zhaoxu
 * @description:
 */
public class CsdnUtil {
    static String[] ua = {"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36 OPR/37.0.2178.32",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586",
            "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",
            "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)",
            "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)",
            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0)",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 BIDUBrowser/8.3 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36 Core/1.47.277.400 QQBrowser/9.4.7658.400",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 UBrowser/5.6.12150.8 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36 TheWorld 7",
            "Mozilla/5.0 (Windows NT 6.1; W…) Gecko/20100101 Firefox/60.0"};

    static String myUrl = "https://blog.csdn.net/qq_39898191";
    static String userName = "13149105475";
    static String password = "Zz458114067";
    static CookieManager manager = new CookieManager();

    public static void main(String[] args) throws IOException {
        CookieHandler.setDefault(manager);
        //暂时无法登录
//        login();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
//                    HttpUrlConnectionUtil.ips("http://api.xiequ.cn/VAD/GetIp.aspx?act=get&num=200&time=30&plat=1&re=1&type=2&so=1&ow=1&spl=1&addr=&db=1");
                    ArrayList<String> blogs = getBlogs();
                    blogs.stream().forEach(href -> {
                        try {
//                            setIpProxy();
                            HttpUrlConnectionUtil.get(null, href);
                            Integer length = href.split("/").length;
                            like(href.split("/")[length - 1]);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 60 * 1000);
    }

    public static ArrayList<String> getBlogs() throws IOException {
        Random r = new Random();
        int k = r.nextInt(14);
        ArrayList blogs = new ArrayList();
        Integer page = 1;
        while (true) {
            Document doc = Jsoup.connect(myUrl + "/article/list/" + String.valueOf(page))
                    .timeout(10000)
                    .ignoreHttpErrors(true)
                    .userAgent(ua[k])
                    .get();
            Elements hrefs = doc.select(".csdn-tracking-statistics h4 a");
            for (Element e : hrefs) {
                blogs.add(e.attr("href"));
            }
            page++;
            if (hrefs.size() == 0) {
                break;
            }
        }
        return blogs;
    }

    public static void login() throws IOException {
        JSONObject params = new JSONObject();
        params.put("loginType", "1");
        params.put("pwdOrVerifyCode", password);
        params.put("userIdentification", userName);
        HttpUrlConnectionUtil.post(null, "https://passport.csdn.net/v1/register/pc/login/doLogin", params);
    }

    public static void like(String articleCode) throws IOException {
        JSONObject headers = new JSONObject();
        HashMap cookies = new HashMap();
        Map map = manager.get(URI.create("https://blog.csdn.net/"), cookies);
        headers.put("cookie", map.get("Cookie"));
        String post = HttpUrlConnectionUtil.post(headers, "https://blog.csdn.net//phoenix/web/v1/article/like?articleId=" + articleCode, null);
        System.out.println(post);
    }

    /**
     * 暂时没有用到
     */
//    public static void setIpProxy() {
//        JSONObject headers = new JSONObject();
//        try {
//            String hostAddress = InetAddress.getLocalHost().getHostAddress();
//            System.getProperties().setProperty("http.proxyHost", hostAddress);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        int rand = (int) ((Math.random() * (100 - 0 + 1)) + 0);
//        String[] r1 = HttpUrlConnectionUtil.ips.get(rand).split(":");
//        if (HttpUrlConnectionUtil.ips.size() == 0 && r1[1].length() > 5) {
//            return;
//        } else {
//            System.getProperties().setProperty("http.proxyHost", r1[0]);
//            System.getProperties().setProperty("http.proxyPort", r1[1]);
//        }
////        System.err.println(r1[0] + ":" + r1[1]);
//    }
}
