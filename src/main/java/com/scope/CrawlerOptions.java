package com.scope;

public class CrawlerOptions {
    private String page;
    private String location;
    private int maxParallel;

    public CrawlerOptions() {
        this.page = null;
        this.location = "./downloads/";
        this.maxParallel = 4;
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

    public int getMaxParallel() {
        return maxParallel;
    }

    public void setMaxParallel(int maxParallel) {
        this.maxParallel = maxParallel;
    }
}
