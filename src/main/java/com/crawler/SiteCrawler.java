package com.crawler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

public class SiteCrawler {
    private ProgressReporter reporter;
    private final CrawlerArgs args;

    public SiteCrawler(CrawlerArgs crawlerArgs) {
        args = crawlerArgs;
        reporter = new ProgressReporter();
    }

    public void crawl() throws Exception {
        checkAndCreateDownloadsFolder();
        System.out.println("Download page: " + args.getPage());
        Document doc = Jsoup.connect(args.getPage()).get();
        Elements links = doc.select(".gallery-icon a");
        downloadFiles(links);
    }

    private void downloadFiles(Elements links) {
        ExecutorService service = Executors.newFixedThreadPool(args.getMaxParallel());
        reporter.increment(links.size());
        for (Element link : links) {
            service.submit(new Runnable() {
                public void run() {
                    String href = link.attr("href");
                    String identifier = getLinkIdentifier(href);
                    try {
                        downloadFile(href, args.getLocation() + identifier);
                    } catch (Exception e) {
                        System.out.println("error: download failed: " + e.getMessage());
                    } finally {
                        reporter.decrement();
                        if (reporter.getPending() == 0) {
                            System.out.println("\nDone!");
                            service.shutdown();
                        }
                    }
                }
            });
        }
    }

    private String getLinkIdentifier(String link) {
        String[] parts = link.split("/");
        return parts[parts.length - 1];
    }

    private void checkAndCreateDownloadsFolder() {
        File theDir = new File(args.getLocation());
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
    }

    private long downloadFile(String link, String filename) throws IOException {
        var url = URI.create(link).toURL();
        try (InputStream in = url.openStream()) {
            return Files.copy(in, Paths.get(filename));
        }
    }
}
