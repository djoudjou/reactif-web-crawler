package fr.djoutsop.crawler.service.akka.actor;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.pf.ReceiveBuilder;
import fr.djoutsop.crawler.service.akka.Messages.Content;
import fr.djoutsop.crawler.service.akka.Messages.IndexFinished;
import fr.djoutsop.crawler.service.akka.Messages.Scrap;

public class Scraper extends UntypedActor {
	Logger logger = LoggerFactory.getLogger(Scraper.class);

	ActorRef indexer;

	Map<URL, Content> store = new HashMap<>();

	public static Props props(ActorRef indexer) {
		return Props.create(Scraper.class, () -> new Scraper(indexer));
	}

	public Scraper(ActorRef indexer) {
		this.indexer = indexer;
		receive(ReceiveBuilder.match(Scrap.class, index -> {
			logger.debug("saving page {} with {}", index.url, index.content);
			store.put(index.url, index.content);
			supervisor.tell(new IndexFinished(index.url, index.content.urls), self());
		}).build());
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if(message instanceof Scrap) {
			Scrap scrap = (Scrap) message;
			logger.debug("scraping {}",scrap.url);
		    Content content = parse(scrap.url);
		    sender().tell(new ScrapFinished(scrap.url),self());
		    sender().tell(new Index(scrap.url, content);
		}
		
	}
	
	Content parse(URL url) {
		Content content;
		    String link = url.toString();
		    Response response = Jsoup.connect(link).ignoreContentType(true)
		      .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1").execute()

		    String contentType = response.contentType();
		    if (contentType.startsWith("text/html")) {
		      Document doc = response.parse();
//		      String title = doc.getElementsByTag("title").asScala.map(e => e.text()).head
//		      val descriptionTag = doc.getElementsByTag("meta").asScala.filter(e => e.attr("name") == "description")
//		      val description = if (descriptionTag.isEmpty) "" else descriptionTag.map(e => e.attr("content")).head
//		      val links: List[URL] = doc.getElementsByTag("a").asScala.map(e => e.attr("href")).filter(s =>
//		        urlValidator.isValid(s)).map(link => new URL(link)).toList
		      content = Content(title, description, links);
		    } else {
		      // e.g. if this is an image
		    content = Content(link, contentType, List());
		    }
		    return content;
		  }

}
