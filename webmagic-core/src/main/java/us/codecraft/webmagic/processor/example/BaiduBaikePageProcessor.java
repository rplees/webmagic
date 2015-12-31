package us.codecraft.webmagic.processor.example;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author code4crafter@gmail.com <br>
 * @since 0.4.0
 */
public class BaiduBaikePageProcessor implements PageProcessor {

    private Site site = Site.me().setDomain("baike.baidu.com").addStartUrl("http://baike.baidu.com/search/word?word=水力发电&pic=1&sug=1&enc=utf8")//.setHttpProxy(new HttpHost("127.0.0.1",8888))
            .setRetryTimes(3).setSleepTime(1000).setUseGzip(true);

    @Override
    public void process(Page page) {
        page.putField("name", page.getHtml().xpath("//html/body[@class='wiki-lemma normal']/div[@class='body-wrapper']/div[@class='content-wrapper']/div[@class='content']/div[@class='main-content']/dl[@class='lemmaWgt-lemmaTitle lemmaWgt-lemmaTitle-']/dd[@class='lemmaWgt-lemmaTitle-title']/h1/text()").toString());
        page.putField("description", page.getHtml().xpath("//html/body[@class='wiki-lemma normal']/div[@class='body-wrapper']/div[@class='content-wrapper']/div[@class='content']/div[@class='main-content']/div[@class='lemma-summary']/div[@class='para']/text()"));
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        //single download
        Spider spider = Spider.create(new BaiduBaikePageProcessor()).thread(2);
        String urlTemplate = "http://baike.baidu.com/search/word?word=%s&pic=1&sug=1&enc=utf8";
        ResultItems resultItems = spider.<ResultItems>get(String.format(urlTemplate, "水力发电"));
        System.out.println(resultItems);

        //multidownload
        List<String> list = new ArrayList<String>();
        list.add(String.format(urlTemplate,"风力发电"));
        list.add(String.format(urlTemplate,"太阳能"));
        list.add(String.format(urlTemplate,"地热发电"));
        list.add(String.format(urlTemplate,"地热发电"));
        List<ResultItems> resultItemses = spider.<ResultItems>getAll(list);
        for (ResultItems resultItemse : resultItemses) {
            System.out.println(resultItemse.getAll());
        }
        spider.close();
    }
}
