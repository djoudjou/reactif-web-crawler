package fr.djoutsop.service;

import java.io.IOException;
import java.net.URL;
import java.util.stream.Stream;

import fr.djoutsop.entity.Content;

public class WebCrawlerService {

	ScraperService scraperService;

	// public void parseHtmlContent(String htmlContent) {
	//
	// Document doc = Jsoup.parse(htmlContent);
	//
	// doc.getElementsByTag("a").stream().map(e -> new Link(e.attr("href"), new
	// Date())).forEach(System.out::println);
	//
	// }

	public void setScraperService(ScraperService scraperService) {
		this.scraperService = scraperService;
	}

	protected Stream<Content> recursive_scrap(URL urlToScrap, int depth) {
		try {
			Content content = scraperService.scrap(urlToScrap);

			if (depth > 0 && content != null && !content.getUrls().isEmpty()) {
				return content.getUrls().stream().flatMap(url -> this.recursive_scrap(url, depth - 1));
			} else {
				System.out.println("stop>>" + urlToScrap);
				return Stream.of(content);
			}
		} catch (Exception ex) {
			System.err.println(ex);
		}
		return Stream.empty();
	}

	public void crawl(URL url) throws IOException {
		recursive_scrap(url, 4).forEach(System.out::println);
	}

}
