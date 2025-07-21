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
            }
        };

        var crawler = new SiteCrawler(opts);
        try {
            crawler.crawl();
        } catch (Exception e) {
            System.out.printf("error: crawl failed: %s\n", e.getMessage());
        }
    }
}
