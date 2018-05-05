package com.wukong.controller;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Dijkstra.DijkstraSegment;
import com.hankcs.hanlp.seg.NShort.NShortSegment;
import com.hankcs.hanlp.seg.Segment;
import com.wukong.util.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static com.wukong.util.JsoupUtil.getDocument;

/**
 * @author PARANOIA_ZK
 * @date 2018/4/23 11:53
 */

@RestController
public class BaiDuController {

    private static volatile StringBuilder sb = new StringBuilder();

    private static ExecutorService service = Executors.newFixedThreadPool(50);
    private static CompletionService<String> execcomp = new ExecutorCompletionService<String>(service);

    @RequestMapping("/ceshi")
    public String ceshi(@RequestParam String key) throws UnsupportedEncodingException {
        System.out.println("key = " + key);
        String requestUrl = "https://www.baidu.com/s?ie=utf-8&newi=1&mod=11&isbd=1&isid=9a0ea9450002df62&wd=" + URLEncoder.encode(key, "UTF-8") +
                "&rsv_spt=1&rsv_iqid=0xbca8b48c0002a1bb&issp=1&f=8&rsv_bp=1&rsv_idx=2&ie=utf-8&rqlang=cn&tn=02003390_2_hao_pg&rsv_enter=1&oq=%25E6%25B5%258B%25E8%25AF%2595&rsv_t=9ad6F8IPnV9z3ex5Mg5dpEdAXdOi5LzoWbNOO8D4%2Bj2BG3j5%2BnIdzYRJgds9nSWlUQLYJgmIOAM&rsv_pq=9a0ea9450002df62&rsv_sug3=11&bs=%E6%B5%8B%E8%AF%95&rsv_sid=&_ss=1&clist=756a0e4bd2a0c14f&hsug=&csor=2&pstg=5&_cr1=33625";
        List<String> pages = getAllPages(requestUrl);
        System.out.println("pages = " + pages.size());
        pages.forEach(page -> {
            execcomp.submit(getH3AndHref(page));
        });
        pages.forEach(page -> {
            try {
                Future<String> future = execcomp.take();
                sb.append(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        //todo 使用线程池的情况下阻塞主线程 是否有更佳方案?
        System.out.println("sb = " + StringUtil.format(sb.toString()));
        List<String> keywordList = HanLP.extractKeyword(StringUtil.format(sb.toString()), 20);
        System.out.println("高频词汇：" + keywordList);
        return keywordList.toString();
    }

    @SuppressWarnings("AlibabaThreadPoolCreation")
    public static void main(String[] args) throws IOException, InterruptedException {

        Segment nShortSegment = new NShortSegment().enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
        Segment shortestSegment = new DijkstraSegment().enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
        String[] testCase = new String[]{
                "今天，刘志军案的关键人物,山西女商人丁书苗在市二中院出庭受审。",
                "刘喜杰石国祥会见吴亚琴先进事迹报告团成员",
        };
        for (String sentence : testCase)
        {
            System.out.println("N-最短分词：" + nShortSegment.seg(sentence) + "\n最短路分词：" + shortestSegment.seg(sentence));
        }
    }


    /**
     * 拿取搜索关键字下的所有分页
     *
     * @param url
     * @return 结果集set(去重)
     */
    public static List<String> getAllPages(String url) {
        List<String> result = new ArrayList<>(10000);
        result.add(url);
        String nextPageUrl = url;
        for (int i = 0; i < 100; i++) {
            try {
                Document parentDoc = getDocument(nextPageUrl);
                Element pageElements = parentDoc.getElementById("page");
                Element nextPage = pageElements.getElementsByClass("n").last();
                nextPageUrl = "https://www.baidu.com" + nextPage.attr("href");
                //判断是否到达了最后一页
                if (nextPageUrl.contains("page=-1")) {
                    break;
                }
                System.out.println("nextPageUrl = " + nextPageUrl);
                result.add(nextPageUrl);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        return result;
    }


    public static Callable<String> getH3AndHref(String url) {

        StringBuilder sb = new StringBuilder();
        Callable<String> task = new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("解析的url = " + url);
                Document parentDoc = getDocument(url);
                Element content_left = parentDoc.getElementById("content_left");
                Elements resultsInOnePage = content_left.children();
                System.out.println("resultsInOnePage.size() = " + resultsInOnePage.size());
                Element h3;

                for (Element one : resultsInOnePage) {
                    try {
                        h3 = one.getElementsByTag("h3").get(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                    String h3Text = h3.text();
                    String h3Href = h3.getElementsByTag("a").get(0).attr("href");
                    sb.append(h3Text);
                    System.out.println("h3Text = " + h3Text);
                    System.out.println("h3Href = " + h3Href);
                }
                return sb.toString();
            }
        };
        return task;
    }

}
