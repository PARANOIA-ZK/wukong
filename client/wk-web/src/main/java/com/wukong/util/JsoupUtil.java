package com.wukong.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * @author PARANOIA_ZK
 * @date 2018/5/4 15:47
 */
public class JsoupUtil {

    /**
     * 根据url获取Document对象
     *
     * @param url 页面URL
     * @return Document对象
     */
    public static Document getDocument(String url) {

        Document doc = null;
        try {
            doc = Jsoup.connect(url)
                       .header("Accept", "*/*")
                       .header("Accept-Encoding", "gzip, deflate")
                       .header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                       .header("Content-Type", "application/json;charset=UTF-8")
                       .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
                       .get();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return doc;
    }
}
