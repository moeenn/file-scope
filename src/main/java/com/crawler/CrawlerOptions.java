package com.crawler;

public class CrawlerOptions {
    private String page;
    private String location;

    public CrawlerOptions() {
        this.page = null;
        this.location = "./downloads/";
    }

    public String getPage() {
        return page;
    }

    public String getLocation() {
        return location;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
