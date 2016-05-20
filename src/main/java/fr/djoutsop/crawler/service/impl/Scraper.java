package fr.djoutsop.crawler.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import fr.djoutsop.crawler.entity.Content;

public class Scraper {

	private final String EMPTY_STRING = "";
	private final String HREF = "href";

	public Content scrap(String urlToScrap, String referer) throws IOException {

		Content content = null;

		Connection connection = Jsoup.connect(urlToScrap).ignoreContentType(true)
				.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");

		Response response = connection.referrer(referer.toString()).execute();

		String contentType = response.contentType();

		if (contentType.startsWith("text/html")) {
			content = scrap(response.parse());
		}
		return content;
	}

	public Content scrapHtmlContent(String htmlContent) {
		return scrap(Jsoup.parse(htmlContent));
	}

	String mapUrl(Element elt) {
		String result = null;
		if (!isQueryFilter(elt)) {
			result = elt.absUrl(HREF);
			if (EMPTY_STRING.equals(result)) {
				result = elt.attr(HREF);
			}
		}
		return result;
	}
	
	boolean isQueryFilter(Element elt) {
		return elt.attr(HREF).startsWith("?");
	}

	public Content scrap(Document document) {
		String title = document.getElementsByTag("title").text();
		Optional<Element> descriptionTag = document.getElementsByTag("meta").stream()
				.filter(e -> e.attr("name").equals("description")).findFirst();
		String description = descriptionTag.isPresent() ? descriptionTag.get().attr("content") : null;
		List<String> links = document.getElementsByTag("a").stream().map(this::mapUrl).filter(url -> url != null)
				.collect(Collectors.toList());
		return new Content(title, description, links);
	}
}
