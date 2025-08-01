package com.scope;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;
import com.scope.crawlers.BaseCrawler;
import com.scope.crawlers.CrawlerOne;
import com.scope.crawlers.CrawlerTwo;

public class Scope {
    private static BaseCrawler crawlers[] = {
            new CrawlerOne(),
            new CrawlerTwo(),
    };

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: scope.jar <url | file-path.txt>");
            System.exit(1);
        }

        String url = args[0];
        if (url.startsWith("http")) {
            processURL(url);
            return;
        }

        Path jobFile = Paths.get(url);
        if (!jobFile.toFile().exists()) {
            System.err.printf("error: file %s does not exists\n", url);
            System.exit(1);
        }

        try (Stream<String> lines = Files.lines(jobFile)) {
            lines.forEach(line -> processURL(line.trim()));
        } catch (Exception e) {
            System.err.printf("error: Failed to process %s file: %s\n", jobFile, e.getMessage());
            System.exit(1);
        }
    }

    private static void processURL(String url) {
        var opts = new CrawlerOptions() {
            {
                setPage(url);
                setLocation("./downloads/");
                setMaxParallel(6);
            }
        };

        Optional<BaseCrawler> crawler = getCrawler(url, crawlers);
        if (!crawler.isPresent()) {
            System.err.println("error: the provided website is currently not supported");
            System.exit(1);
        }

        try {
            crawler.get().crawl(opts);
        } catch (Exception e) {
            System.out.printf("error: crawl failed: %s\n", e.getMessage());
        }
    }

    private static Optional<BaseCrawler> getCrawler(String url, BaseCrawler[] crawlers) {
        for (BaseCrawler crawler : crawlers) {
            if (crawler.matchCrawler(url)) {
                return Optional.ofNullable(crawler);
            }
        }

        return Optional.empty();
    }
}
