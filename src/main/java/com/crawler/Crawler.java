package com.crawler;

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
                new EPPCrawler(opts),
        };

        boolean isCrawled = false;
        for (BaseCrawler crawler : crawlers) {
            if (crawler.matchCrawler(url)) {
                isCrawled = true;
                try {
                    crawler.crawl();
                } catch (Exception e) {
                    System.out.printf("error: crawl failed: %s\n", e.getMessage());
                }
                break;
            }
        }

        if (!isCrawled) {
            System.err.println("error: the provided website is currently not supported");
        }
    }
}
