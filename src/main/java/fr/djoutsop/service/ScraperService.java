package fr.djoutsop.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import fr.djoutsop.entity.Content;

public class ScraperService {
	public Content scrap(URL urlToScrap) throws IOException {

		Content content = null;

		Response response = Jsoup.connect(urlToScrap.getPath()).ignoreContentType(true)
				.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1").execute();

		String contentType = response.contentType();

		if (contentType.startsWith("text/html")) {
			content = scrap(response.parse());
		}
		return content;
	}

	public Content scrap(String htmlContent) {
		return scrap(Jsoup.parse(htmlContent));
	}

	URL mapUrl(Element elt) {
		try {
			return new URL(elt.attr("href"));
		} catch (MalformedURLException e) {
			return null;
		}
	}

	Content scrap(Document document) {
		String title = document.getElementsByTag("title").text();
		Optional<Element> descriptionTag = document.getElementsByTag("meta").stream().filter(e -> e.attr("name") == "description").findFirst();
		String description = descriptionTag.isPresent() ? descriptionTag.get().attr("content") : null;
		List<URL> links = document.getElementsByTag("a").stream().map(this::mapUrl).filter(url->url!=null).collect(Collectors.toList());
		return new Content(title, description, links);
	}
}