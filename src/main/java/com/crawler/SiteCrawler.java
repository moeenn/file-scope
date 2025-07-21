package com.crawler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

public class SiteCrawler {
    private ProgressReporter reporter;
    private final CrawlerOptions opts;

    public SiteCrawler(CrawlerOptions crawleropts) {
        opts = crawleropts;
        reporter = new ProgressReporter();
    }

    public void crawl() throws Exception {
        checkAndCreateDownloadsFolder();
        System.out.println("Download page: " + opts.getPage());
        Document doc = Jsoup.connect(opts.getPage()).get();
        Elements links = doc.select(".gallery-icon a");
        downloadFiles(links);
    }

    private void downloadFiles(Elements links) {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            reporter.setTotal(links.size());
            for (Element link : links) {
                executor.submit(new Runnable() {
                    public void run() {
                        String href = link.attr("href");
                        String identifier = getLinkIdentifier(href);
                        try {
                            downloadFile(href, opts.getLocation() + identifier);
                        } catch (Exception e) {
                            System.out.println(" :: error: download failed: " + e.getMessage());
                        } finally {
                            reporter.decrement();
                            if (reporter.getPending() == 0) {
                                System.out.println("\nDone!");
                                executor.shutdown();
                            }
                        }
                    }
                });
            }

            try {
                executor.shutdown();
                executor.awaitTermination(1, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private String getLinkIdentifier(String link) {
        String[] parts = link.split("/");
        return parts[parts.length - 1];
    }

    private void checkAndCreateDownloadsFolder() {
        File theDir = new File(opts.getLocation());
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
    }

    private long downloadFile(String link, String filename) throws IOException {
        var url = URI.create(link).toURL();
        try (InputStream in = url.openStream()) {
            File file = new File(filename);
            if (file.exists()) {
                throw new Error("file already exists");
            }

            return Files.copy(in, Paths.get(filename));
        }
    }
}
