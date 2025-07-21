package com.crawler;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class EPPCrawler extends BaseCrawler {
    public EPPCrawler(CrawlerOptions crawleropts) {
        super(crawleropts);
    }

    @Override
    public boolean matchCrawler(String url) {
        return url.contains("epicpornpics.com");
    }

    @Override
    public void crawl() throws Exception {
        Document doc = getDocument();
        Elements links = doc.select(".gallery-icon a");
        downloadFiles(links);
    }
}
