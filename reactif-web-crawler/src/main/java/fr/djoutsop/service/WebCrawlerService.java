package fr.djoutsop.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import fr.djoutsop.entity.Link;

public class WebCrawlerService {

	public void parseHtmlContent(String htmlContent) {

		Document doc = Jsoup.parse(htmlContent);

		doc.getElementsByTag("a").stream().map(e -> new Link(e.attr("href"), new Date())).forEach(System.out::println);

	}

	public List<Link> getLinks() {
		// TODO Auto-generated method stub
		return null;
	}

	static Stream<Link> scrap(Link urlToScrap) {
		try {
			Response response = Jsoup.connect(urlToScrap.getPath()).ignoreContentType(true)
					.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1").execute();

			String contentType = response.contentType();

			if (contentType.startsWith("text/html")) {
				List<Link> urls = response.parse().getElementsByTag("a").stream().map(e -> new Link(urlToScrap.getPath() + "/" + e.attr("href"), new Date()))
						.collect(Collectors.toList());
				
				return urls.stream().flatMap(WebCrawlerService::scrap);
			} else {
				System.out.println("stop>>" + urlToScrap);
				return Stream.of(urlToScrap);
			}
		} catch (Exception ex) {
			System.err.println(ex);
		}
		return Stream.empty();
    }
	
	
//	private 

	public void crawl(Link url) throws IOException {
		
		

		// ScrapOperation scrap = (Link link) -> {
		// Response response =
		// Jsoup.connect(link.getPath()).ignoreContentType(true)
		// .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0)
		// Gecko/20100101 Firefox/40.1").execute();
		//
		// String contentType = response.contentType();
		//
		// if (contentType.startsWith("text/html")) {
		// return response.parse().getElementsByTag("a").stream().map(e -> new
		// Link(e.attr("href"), new Date()))
		// .collect(Collectors.toList());
		// } else {
		// return new ArrayList<>();
		// }
		// };

		scrap(url).forEach(System.out::println);

		// scrap(url).parallelStream()
		// .map(

		// scrap(url).forEach(System.out::println);

	}

}
