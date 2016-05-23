package fr.djoutsop.crawler.service.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.RequestUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.djoutsop.crawler.entity.Content;
import fr.djoutsop.crawler.service.IWebCrawlerService;
import fr.djoutsop.crawler.utils.Loggable;

@Service
public class WebCrawlerService implements IWebCrawlerService {
	@Loggable
	Logger logger;

	@Autowired
	Scraper scraper;

	int maxDepth;

	Set<String> scrappedUrl = new HashSet<>();

	Set<String> allowedExtensions = new HashSet<>();

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
			logger.error("scrap url={} failed with={}", urlToScrap, ex.getMessage(), ex);
		}
		return Stream.empty();
	}

	public Stream<String> crawl(String url, int maxDepth, String... extensions) throws IOException {
		this.maxDepth = maxDepth;
		for (String ext : extensions) {
			this.allowedExtensions.add(ext.toLowerCase());
		}
		scrappedUrl.clear();
		filterNewUrl(url);
		return recursiveScrap(url, url, 0);
	}

}
