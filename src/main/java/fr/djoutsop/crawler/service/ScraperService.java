package fr.djoutsop.crawler.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import fr.djoutsop.crawler.entity.Content;

public class ScraperService {

	private static final String HREF = "href";
	Set<String> allowedExtensions;

	public ScraperService(String... extensions) {
		this.allowedExtensions = new HashSet<>();
		for (String ext : extensions) {
			this.allowedExtensions.add(ext.toLowerCase());
		}
		this.allowedExtensions.add("");
	}

	public Content scrap(String urlToScrap, String referer) throws IOException {

		Content content = null;

		Connection connection = Jsoup.connect(urlToScrap).ignoreContentType(true)
				.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");

		Response response = (referer != null) ? connection.referrer(referer.toString()).execute()
				: connection.execute();

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
		String result;
		if (!isQueryFilter(elt) && isSubUrl(elt) && isValidExtension(elt)) {
			result = elt.absUrl(HREF);
			if ("".equals(result)) {
				result = elt.attr(HREF);
			}
		} else {
			result = null;
		}
		return result;
	}

	boolean isSubUrl(Element elt) {
		boolean result;

		String url = elt.absUrl(HREF);

		if ("".equals(url)) {
			url = elt.attr(HREF);
			result = !url.startsWith("/") && !url.contains("..");
		} else {
			result = url.startsWith(elt.baseUri());
		}

		return result;
	}

	boolean isQueryFilter(Element elt) {
		return elt.attr(HREF).startsWith("?");
	}

	boolean isValidExtension(Element elt) {
		String extension = FilenameUtils.getExtension(elt.attr(HREF)).toLowerCase();
		return allowedExtensions.contains(extension);
	}

	Content scrap(Document document) {
		String title = document.getElementsByTag("title").text();
		Optional<Element> descriptionTag = document.getElementsByTag("meta").stream()
				.filter(e -> e.attr("name").equals("description")).findFirst();
		String description = descriptionTag.isPresent() ? descriptionTag.get().attr("content") : null;
		List<String> links = document.getElementsByTag("a").stream().map(this::mapUrl).filter(url -> url != null)
				.collect(Collectors.toList());
		return new Content(title, description, links);
	}
}
