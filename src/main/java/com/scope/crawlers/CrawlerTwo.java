package com.scope.crawlers;

import java.util.UUID;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import com.scope.CrawlerOptions;

public class CrawlerTwo extends BaseCrawler {
    @Override
    public boolean matchCrawler(String url) {
        String[] sites = {
                "domain-one.com",
                "domain-two.com",
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

    @Override
    protected String getLinkIdentifier(String pageURL, String link) {
        try {
            String[] pageURLParts = pageURL.split("/");
            String pageId = pageURLParts[pageURLParts.length - 1];
            String[] parts = link.split("/");
            return pageId + "-" + parts[parts.length - 1];
        } catch (Exception ex) {
            System.out.println(" error: failed to find link identifier");
            return UUID.randomUUID().toString() + ".jpg";
        }
    }
}
