package com.springdataexample.springdataexample;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataService {
    Document doc;
    Logger logger = LoggerFactory.getLogger(DataService.class);
    String siteURL;
    String site = "siteadresi";
    String sitePage = "/sayfa";
    String sitePaging = "?value=50";
    int count = 1;

    @Scheduled(cron = "0 0/1 * * * ?")
    @RequestMapping("/")
    public String index() throws Exception {
        siteURL = site + sitePage + sitePaging;
        try {
            doc = Jsoup.connect(siteURL).get();
        } catch (HttpStatusException e) {

        }
        Elements ilanBasliklari = doc.getElementsByClass("classifiedTitle");
        Elements nextPage = doc.getElementsByClass("prevNextBut");

        for (Element row : ilanBasliklari) {
            logger.info(count + " - " + row.text() + " - " + dataContent(row.attr("href")));
            count++;
        }

        for (Element row : nextPage) {
            sitePaging = String.valueOf(row.attr("href"));
        }

        return ilanBasliklari.text();
    }

    public String dataContent(String ilanLink) throws Exception {
        Thread.sleep(10000);
        String fiyat = null;
        siteURL = site + ilanLink;
        try {
            doc = Jsoup.connect(siteURL).get();
        } catch (HttpStatusException e) {

        }

        Elements ilanDetaylari = doc.select("div .classifiedInfo h3");

        for (Element row : ilanDetaylari) {
            row.getElementsByTag("a").remove();
            fiyat = row.text();
        }

        return fiyat;
    }

}
