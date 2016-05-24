package fr.djoutsop.crawler.service.akka.actor;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.UntypedActor;
import fr.djoutsop.crawler.service.akka.Messages.IndexFinished;
import fr.djoutsop.crawler.service.akka.Messages.Scrap;
import fr.djoutsop.crawler.service.akka.Messages.ScrapFailure;
import fr.djoutsop.crawler.service.akka.Messages.ScrapFinished;
import fr.djoutsop.crawler.service.akka.Messages.Start;
import fr.djoutsop.crawler.service.akka.impl.ScrapObserver;

public class Supervisor extends UntypedActor {

	Logger logger = LoggerFactory.getLogger(Supervisor.class);

	final int maxPages = 100;
	final int maxRetries = 2;
	int numVisited = 0;
	final Set<URL> toScrap = new HashSet<>();
	final Map<URL, Integer> scrapCounts = new HashMap<>();
	final Map<String, ActorRef> host2Actor = new HashMap<>();
	final ActorRef indexer;
	final ActorSystem system;
	final ScrapObserver scrapObserver;
	
	
	public static Props props(ActorSystem system,ScrapObserver scrapObserver) {
		return Props.create(Supervisor.class, () -> new Supervisor(system,scrapObserver));
	}
	
	public Supervisor(ActorSystem system, ScrapObserver scrapObserver) {
		this.system = system;
		this.scrapObserver = scrapObserver;
		indexer = context().actorOf(Indexer.props(self()));
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Start) {
			Start start = (Start) message;
			logger.debug("starting {}", start.url);
			scrap(start.url);
		} else if (message instanceof ScrapFinished) {
			ScrapFinished scrapFinished = (ScrapFinished) message;
			logger.debug("scraping finished {}", scrapFinished.url);
		} else if (message instanceof IndexFinished) {
			IndexFinished indexFinished = (IndexFinished) message;
			if (numVisited < maxPages) {
				(new HashSet<>(indexFinished.urls)).stream()
						.filter(l -> !scrapCounts.containsKey(l))
						.forEach(this::scrap);
			}
			checkAndShutdown(indexFinished.url);
		} else if (message instanceof ScrapFailure) {
			ScrapFailure scrapFailure = (ScrapFailure) message;
			int retries = scrapCounts.get(scrapFailure.url);
			logger.debug("scraping failed {}, {}, reason = {}",scrapFailure.url, retries, scrapFailure.reason.getMessage());
			if (retries < maxRetries) {
				countVisits(scrapFailure.url);
				host2Actor.get(scrapFailure.url.getHost()).tell(
						new Scrap(scrapFailure.url), self());
			} else {
				checkAndShutdown(scrapFailure.url);
			}
		} else {
			unhandled(message);
		}
	}

	void scrap(URL url) {
		String host = url.getHost();
		logger.debug("host = {}", host);
		if (!host.isEmpty()) {
			ActorRef actor = host2Actor.get(host);

			if (actor == null) {
				ActorRef buff = context().system().actorOf(
						SiteCrawler.props(self(), indexer));
				host2Actor.put(host, buff);
				actor = buff;
			}

			numVisited += 1;
			toScrap.add(url);
			scrapObserver.scrap(url);
			countVisits(url);
			actor.tell(new Scrap(url), self());
		}
	}

	void checkAndShutdown(URL url) {
		toScrap.remove(url);
		// if nothing to visit
		if (toScrap.isEmpty()) {
			self().tell(PoisonPill.getInstance(), self());
			system.terminate();
		}
	}

	void countVisits(URL url) {
		scrapCounts.put(url, new Integer(scrapCounts.getOrDefault(url, 0) + 1));
	}
}
