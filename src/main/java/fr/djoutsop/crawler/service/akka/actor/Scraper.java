package fr.djoutsop.crawler.service.akka.actor;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import fr.djoutsop.crawler.service.IScraperService;
import fr.djoutsop.crawler.service.akka.Messages.ContentMessage;
import fr.djoutsop.crawler.service.akka.Messages.Index;
import fr.djoutsop.crawler.service.akka.Messages.Scrap;
import fr.djoutsop.crawler.service.akka.Messages.ScrapFinished;
import fr.djoutsop.crawler.service.impl.ScraperService;

public class Scraper extends UntypedActor {
	Logger logger = LoggerFactory.getLogger(Scraper.class);

	final ActorRef indexer;
	final Map<URL, ContentMessage> store;
	final IScraperService scraperService;

	public static Props props(ActorRef indexer) {
		return Props.create(Scraper.class, () -> new Scraper(indexer));
	}

	public Scraper(ActorRef indexer) {
		this.indexer = indexer;
		this.store = new HashMap<>();
		this.scraperService = new ScraperService();
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Scrap) {
			Scrap scrap = (Scrap) message;
			logger.debug("scraping {}", scrap.url);
			ContentMessage content = new ContentMessage(scraperService.scrap(scrap.url));
			sender().tell(new ScrapFinished(scrap.url), self());
			indexer.tell(new Index(scrap.url, content), self());
		} else {
			unhandled(message);
		}
	}

}
