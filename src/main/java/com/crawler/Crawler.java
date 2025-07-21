package com.crawler;

import java.util.Optional;
import com.crawler.crawlers.BaseCrawler;
import com.crawler.crawlers.HDPPCrawler;
import com.crawler.crawlers.MBTBCrawler;

public class Crawler {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: crawler <url>");
            System.exit(1);
        }

        String url = args[0];
        var opts = new CrawlerOptions() {
            {
                setPage(url);
                setLocation("./downloads/");
                setMaxParallel(6);
            }
        };

        BaseCrawler crawlers[] = {
                new MBTBCrawler(opts),
                new HDPPCrawler(opts),
        };

        Optional<BaseCrawler> crawler = getCrawler(url, crawlers);
        if (!crawler.isPresent()) {
            System.err.println("error: the provided website is currently not supported");
            System.exit(1);
        }

        try {
            crawler.get().crawl();
        } catch (Exception e) {
            System.out.printf("error: crawl failed: %s\n", e.getMessage());
        }
    }

    private static Optional<BaseCrawler> getCrawler(String url, BaseCrawler[] crawlers) {
        for (BaseCrawler crawler : crawlers) {
            if (crawler.matchCrawler(url)) {
                return Optional.ofNullable(crawler);
            }
        }

        return Optional.empty();
    }
}
