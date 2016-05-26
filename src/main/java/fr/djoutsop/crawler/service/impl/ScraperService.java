package fr.djoutsop.crawler.service.impl;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.djoutsop.crawler.entity.Content;
import fr.djoutsop.crawler.service.IScraperService;

@Service
public class ScraperService implements IScraperService {
	Logger logger = LoggerFactory.getLogger(ScraperService.class);
	final String EMPTY_STRING = "";
	final String HREF = "href";
	UrlValidator urlValidator;

	public ScraperService() {
		this.urlValidator = new UrlValidator(UrlValidator.ALLOW_2_SLASHES + UrlValidator.ALLOW_ALL_SCHEMES + UrlValidator.ALLOW_LOCAL_URLS);
	}

	@Override
	public Content scrap(URL url) throws IOException {
		String urlToScrap = url.toString();
		return scrap(urlToScrap, urlToScrap);
	}

	@Override
	public Content scrap(String urlToScrap) throws IOException {
		return scrap(urlToScrap, urlToScrap);
	}

	@Override
	public Content scrap(String urlToScrap, String referer) throws IOException {
		String link = urlToScrap;
		Connection connection = Jsoup.connect(urlToScrap).ignoreContentType(true)
				.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");

		Response response = connection.referrer(referer.toString()).execute();

		String contentType = response.contentType();

		Content content = null;

		if (isHtmlContent(contentType)) {
			content = scrap(response.parse());
		} else {
			// e.g. if this is something else
			content = new Content(link, contentType, new ArrayList<>());
		}
		return content;
	}

	@Override
	public Content scrapHtmlContent(String htmlContent) {
		return scrap(Jsoup.parse(htmlContent));
	}

	Content scrap(Document document) {
		String title = document.getElementsByTag("title").stream().map(e -> e.text()).findFirst().orElse("");

		Stream<Element> descriptionTag = document.getElementsByTag("meta").stream()
				.filter(e -> "description".equals(e.attr("name")));

		String description = "";

		if (!descriptionTag.equals(Stream.empty())) {
			description = descriptionTag.map(e -> e.attr("content")).findFirst().orElse("");
		}

		List<String> links = document.getElementsByTag("a").stream().map(this::mapUrl).filter(s -> this.isValid(s))
				.collect(Collectors.toList());

		return new Content(title, description, links);
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

	boolean isValid(String url) {
		return urlValidator.isValid(url);
	}

	boolean isHtmlContent(String contentType) {
		return contentType.startsWith("text/html");
	}

}
