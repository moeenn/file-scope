package com.crawler.crawlers;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import com.crawler.CrawlerOptions;

public class HDPPCrawler extends BaseCrawler {
    public HDPPCrawler(CrawlerOptions crawleropts) {
        super(crawleropts);
    }

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
    public void crawl() throws Exception {
        Document doc = getDocument();
        Elements links = doc.select(".ss-image");
        downloadFiles(links);
    }
}
