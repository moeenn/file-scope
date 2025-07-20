package com.crawler;

public class CrawlerArgsBuilder implements ICrawlerArgsBuilder {
    private CrawlerArgs args = new CrawlerArgs();

    public CrawlerArgsBuilder setPage(String page) {
        args.setPage(page);
        return this;
    }

    public CrawlerArgsBuilder setLocation(String location) {
        args.setLocation(location);
        return this;
    }

    public CrawlerArgsBuilder setMaxParallel(int max) {
        args.setMaxParallel(max);
        return this;
    }

    public CrawlerArgs build() throws IllegalArgumentException {
        if (args.getPage() == null) {
            throw new IllegalArgumentException("page is required");
        }

        if (args.getLocation() == null) {
            throw new IllegalArgumentException("location is required");
        }

        if (args.getMaxParallel() == 0) {
            throw new IllegalArgumentException("maxParallel is required");
        }

        return args;
    }
}
