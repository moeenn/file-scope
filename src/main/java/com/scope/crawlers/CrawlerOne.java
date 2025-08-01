package com.scope.crawlers;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import com.scope.CrawlerOptions;

public class CrawlerOne extends BaseCrawler {
    @Override
    public boolean matchCrawler(String url) {
        String sites[] = {
                "example-domain.com",
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
        Elements links = doc.select(".gallery-icon a");
        downloadFiles(opts, links);
    }
}
