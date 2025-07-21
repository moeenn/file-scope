package com.crawler.crawlers;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import com.crawler.CrawlerOptions;

public class HDPPCrawler extends BaseCrawler {
    @Override
    public boolean matchCrawler(String url) {
        String[] sites = {
                "alllatinapics.com",
                "hdpornpics.com",
        };

        for (String site : sites) {
            if (url.contains(site)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void crawl(CrawlerOptions opts) throws Exception {
        Document doc = getDocument(opts);
        Elements links = doc.select(".ss-image");
        downloadFiles(opts, links);
    }
}
