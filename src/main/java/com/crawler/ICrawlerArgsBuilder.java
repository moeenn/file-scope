package com.crawler;

public interface ICrawlerArgsBuilder {
    ICrawlerArgsBuilder setPage(String page);

    ICrawlerArgsBuilder setLocation(String location);

    ICrawlerArgsBuilder setMaxParallel(int max);
}
