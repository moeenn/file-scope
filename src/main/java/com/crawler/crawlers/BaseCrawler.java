package com.crawler.crawlers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.crawler.CrawlerOptions;
import com.crawler.ProgressReporter;
import org.jsoup.Jsoup;

public abstract class BaseCrawler {
    protected ProgressReporter reporter;
    protected final CrawlerOptions opts;

    public BaseCrawler(CrawlerOptions crawleropts) {
        opts = crawleropts;
        reporter = new ProgressReporter();
    }

    public abstract boolean matchCrawler(String url);

    public abstract void crawl() throws Exception;

    private void checkAndCreateDownloadsFolder() {
        File theDir = new File(opts.getLocation());
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
    }

    private long downloadFile(String link, String folder, String filename) throws IOException {
        var url = URI.create(link).toURL();
        try (InputStream in = url.openStream()) {
            File file = new File(folder + filename);
            if (file.exists()) {
                String id = UUID.randomUUID().toString();
                filename = filename + "_" + id + ".jpg";
            }

            return Files.copy(in, Paths.get(folder + filename));
        }
    }

    protected void downloadFiles(Elements links) {
        if (links.size() == 0) {
            System.out.println("No files found on page!");
            return;
        }

        try (ExecutorService executor = Executors.newFixedThreadPool(opts.getMaxParallel(),
                Thread.ofVirtual().factory())) {
            reporter.setTotal(links.size());
            for (Element link : links) {
                executor.submit(new Runnable() {
                    public void run() {
                        String href = link.attr("href");
                        String identifier = getLinkIdentifier(href);
                        try {
                            downloadFile(href, opts.getLocation(), identifier);
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

    protected Document getDocument() throws Exception {
        checkAndCreateDownloadsFolder();
        System.out.println("Download page: " + opts.getPage());
        Document doc = Jsoup.connect(opts.getPage()).get();
        return doc;
    }

    private String getLinkIdentifier(String link) {
        String[] parts = link.split("/");
        return parts[parts.length - 1];
    }
}
