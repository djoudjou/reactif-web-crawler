package fr.djoutsop.service;

import java.io.IOException;
import java.net.URL;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import fr.djoutsop.entity.Content;

public class WebCrawlerService {

	ScraperService scraperService;

	public void setScraperService(ScraperService scraperService) {
		this.scraperService = scraperService;
	}

	protected Stream<Content> recursive_scrap(URL urlToScrap, URL referer, int depth) {
		try {
			Content content = scraperService.scrap(urlToScrap, referer);

			StringBuilder tabs = new StringBuilder();
			for(int tabIdx=0;tabIdx<depth;tabIdx++) {
				tabs.append("--");
			}
			System.out.println(tabs.toString() + " > " + content);
			
			if (depth < 4 && content != null && !content.getUrls().isEmpty()) {
				return content.getUrls().stream().filter(onlySubUrl(urlToScrap)).flatMap(url -> this.recursive_scrap(url, urlToScrap, depth + 1));
			} else {
				return Stream.of(content);
			}
		} catch (Exception ex) {
			System.err.println(ex);
		}
		return Stream.empty();
	}

	Predicate<? super URL> onlySubUrl(URL urlToScrap) {
		return url -> url.toString().startsWith(urlToScrap.toString());
	}

	public void crawl(URL url) throws IOException {
		recursive_scrap(url, null, 0).count();
	}

}
