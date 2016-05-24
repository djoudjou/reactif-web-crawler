package fr.djoutsop.crawler.service.akka.actor;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import fr.djoutsop.crawler.service.akka.Messages.Content;
import fr.djoutsop.crawler.service.akka.Messages.Index;
import fr.djoutsop.crawler.service.akka.Messages.Scrap;
import fr.djoutsop.crawler.service.akka.Messages.ScrapFinished;

public class Scraper extends UntypedActor {
	Logger logger = LoggerFactory.getLogger(Scraper.class);

	private final String EMPTY_STRING = "";
	private final String HREF = "href";
	
	final ActorRef indexer;
	final UrlValidator urlValidator;
	final Map<URL, Content> store;

	public static Props props(ActorRef indexer) {
		return Props.create(Scraper.class, () -> new Scraper(indexer));
	}

	public Scraper(ActorRef indexer) {
		this.indexer = indexer;
		this.urlValidator = new UrlValidator();
		this.store = new HashMap<>();
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if(message instanceof Scrap) {
			Scrap scrap = (Scrap) message;
			logger.debug("scraping {}",scrap.url);
		    Content content = parse(scrap.url);
		    sender().tell(new ScrapFinished(scrap.url),self());
		    indexer.tell(new Index(scrap.url, content),self());
		} else {
			unhandled(message);
		}
		
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
	
	Content parse(URL url) throws Exception {
		Content content;
		String link = url.toString();
		Response response = Jsoup
				.connect(link)
				.ignoreContentType(true)
				.userAgent(
						"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1")
				.execute();

		String contentType = response.contentType();
		if (contentType.startsWith("text/html")) {
			Document doc = response.parse();
			
			String title = doc.getElementsByTag("title").stream()
					.map(e -> e.text()).findFirst().orElse("");
			
			Stream<Element> descriptionTag = doc.getElementsByTag("meta")
					.stream().filter(e -> "description".equals(e.attr("name")));
			
			String description = "";
			
			if(!descriptionTag.equals(Stream.empty())) {
				description = descriptionTag.map(e -> e.attr("content")).findFirst().orElse("");
			}
							
			List<URL> links = doc.getElementsByTag("a").stream()
					.map(this::mapUrl).filter(s -> this.isValid(s))
					.map(lnk -> {
						try {
							return new URL(lnk);
						} catch (Exception e) {
							logger.error("link {} >> {}",lnk,e.getMessage());
						}
						return null;
					}).filter(lnk -> lnk!=null) .collect(Collectors.toList());
					

			content = new Content(title, description, links);
		} else {
			// e.g. if this is an image
			content = new Content(link, contentType, new ArrayList<>());
		}
		return content;
	}

	private boolean isValid(String url) {
		return urlValidator.isValid(url);
	}

}
