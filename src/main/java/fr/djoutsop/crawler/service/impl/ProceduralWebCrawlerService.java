package fr.djoutsop.crawler.service.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.catalina.util.RequestUtil;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.djoutsop.crawler.entity.Content;
import fr.djoutsop.crawler.service.WebCrawlerService;

public class ProceduralWebCrawlerService implements WebCrawlerService {
	Logger logger = LoggerFactory.getLogger(ProceduralWebCrawlerService.class);

	Scraper scraper;

	int maxDepth;

	Set<String> scrappedUrl = new HashSet<>();

	Set<String> allowedExtensions = new HashSet<>();

	public ProceduralWebCrawlerService(int maxDepth, String... extensions) {
		this.maxDepth = maxDepth;
		for (String ext : extensions) {
			this.allowedExtensions.add(ext.toLowerCase());
		}
	}

	boolean filterAllowedExtensions(String url) {
		if (!allowedExtensions.isEmpty()) {
			String extension = FilenameUtils.getExtension(url).toLowerCase();
			return "".equals(extension) || allowedExtensions.contains(extension);
		} else {
			return true;
		}
	}

	boolean filterNewUrl(String url) {
		return scrappedUrl.add(normalize(url));
	}

	boolean filterOnlySubUrl(String url) {
		String normalizedUrl = normalize(url);
		return scrappedUrl.stream().anyMatch(alreadyScrappedUrl -> normalizedUrl.startsWith(alreadyScrappedUrl));
	}

	boolean isFile(String url) {
		return !"".equals(FilenameUtils.getExtension(url));
	}
	
	String normalize(String url) {
		String normalizedUrl = RequestUtil.normalize(url);
		while (normalizedUrl.endsWith("/")) {
			normalizedUrl = normalizedUrl.substring(0, normalizedUrl.length() - 1);
		}
		return normalizedUrl;
	}

	Stream<String> recursiveScrap(String urlToScrap, String referer, int depth) {
		try {
			Content content = scraper.scrap(urlToScrap, referer);

			if (depth < maxDepth && content != null && !content.getUrls().isEmpty()) {
				return content.getUrls().stream()
						.filter(url -> filterOnlySubUrl(url) && filterNewUrl(url) && filterAllowedExtensions(url))
						.flatMap(url -> this.recursiveScrap(url, urlToScrap, depth + 1));
			} else {
				if (isFile(urlToScrap)) {
					return Stream.of(urlToScrap);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return Stream.empty();
	}

	

	public Stream<String> crawl(String url) throws IOException {
		scrappedUrl.clear();
		filterNewUrl(url);
		return recursiveScrap(url, url, 0);
	}

}
