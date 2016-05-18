package fr.djoutsop.crawler.service;

import java.io.IOException;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.djoutsop.crawler.entity.Content;

public class WebCrawlerService {
	private Logger logger = LoggerFactory.getLogger(WebCrawlerService.class);

	int maxDepth;

	ScraperService scraperService;

	public WebCrawlerService(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	public void setScraperService(ScraperService scraperService) {
		this.scraperService = scraperService;
	}

	Stream<String> recursive_scrap(String urlToScrap, String referer, int depth) {
		try {
			Content content = scraperService.scrap(urlToScrap, referer);

			if (depth < maxDepth && content != null && !content.getUrls().isEmpty()) {
				return content.getUrls().stream().flatMap(url -> this.recursive_scrap(url, urlToScrap, depth + 1));
			} else {
				return Stream.of(urlToScrap);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return Stream.empty();
	}

	public Stream<String> crawl(String url) throws IOException {
		return recursive_scrap(url, url, 0);
	}

}
