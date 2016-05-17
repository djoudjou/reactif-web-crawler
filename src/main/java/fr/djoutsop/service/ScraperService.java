package fr.djoutsop.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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

import fr.djoutsop.entity.Content;

public class ScraperService {
	
	private Set<String> allowedExtensions;
	
	public ScraperService(String ... extensions) {
		this.allowedExtensions = new HashSet<>();
		for(String ext:extensions){
			this.allowedExtensions.add(ext.toLowerCase());
		}
		this.allowedExtensions.add("");
	}
	
	public Content scrap(URL urlToScrap,URL referer) throws IOException {

		Content content = null;

		Connection connection = Jsoup.connect(urlToScrap.toString()).ignoreContentType(true)
				.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");
		
		
		Response response = (referer != null) ? connection.referrer(referer.toString()).execute()
				: connection.execute();

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
			if (isNotQueryFilter(elt) && isSubUrl(elt) && isValidExtension(elt)) {
				URL url = new URL(elt.absUrl("href"));
				return url;
			}
		} catch (MalformedURLException e) {
			System.err.println(e);
		}
		return null;
	}

	boolean isSubUrl(Element elt) {
		return elt.absUrl("href").startsWith(elt.baseUri());
	}

	boolean isNotQueryFilter(Element elt) {
		return !elt.attr("href").startsWith("?");
	}
	
	boolean isValidExtension(Element elt) {
		String extension = FilenameUtils.getExtension(elt.attr("href")).toLowerCase();
		return allowedExtensions.contains(extension);
	}

	Content scrap(Document document) {
		String title = document.getElementsByTag("title").text();
		Optional<Element> descriptionTag = document.getElementsByTag("meta").stream().filter(e -> e.attr("name") == "description").findFirst();
		String description = descriptionTag.isPresent() ? descriptionTag.get().attr("content") : null;
		List<URL> links = document.getElementsByTag("a").stream().map(this::mapUrl).filter(url->url!=null).collect(Collectors.toList());
		return new Content(title, description, links);
	}
}
