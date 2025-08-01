package com.scope.crawlers;

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
import com.scope.CrawlerOptions;
import com.scope.ProgressReporter;
import org.jsoup.Jsoup;

public abstract class BaseCrawler {
    protected ProgressReporter reporter;

    public BaseCrawler() {
        reporter = new ProgressReporter();
    }

    public abstract boolean matchCrawler(String url);

    public abstract void crawl(CrawlerOptions opts) throws Exception;

    private void checkAndCreateDownloadsFolder(String location) {
        File theDir = new File(location);
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

    protected void downloadFiles(CrawlerOptions opts, Elements links) {
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
                        String identifier = getLinkIdentifier(opts.getPage(), href);
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

    protected Document getDocument(CrawlerOptions opts) throws Exception {
        checkAndCreateDownloadsFolder(opts.getLocation());
        System.out.println("Download page: " + opts.getPage());
        Document doc = Jsoup.connect(opts.getPage()).get();
        return doc;
    }

    protected String getLinkIdentifier(String pageURL, String link) {
        String[] parts = link.split("/");
        return parts[parts.length - 1];
    }
}
