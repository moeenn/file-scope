package com.crawler;

public class Crawler {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: crawler <url>");
            System.exit(1);
        }

        String url = args[0];
        var builder = new CrawlerArgsBuilder()
                .setLocation("./downloads/")
                .setPage(url)
                .setMaxParallel(6);

        var crawler = new SiteCrawler(builder.build());
        try {
            crawler.crawl();
        } catch (Exception e) {
            System.out.printf("error: crawl failed: %s\n", e.getMessage());
        }
    }
}
