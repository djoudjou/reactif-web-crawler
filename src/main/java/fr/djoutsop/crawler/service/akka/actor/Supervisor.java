package fr.djoutsop.crawler.service.akka.actor;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tomcat.util.http.RequestUtil;
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
	final List<String> resultCollector;

	Set<String> scrappedNormalizedUrl = new HashSet<>();

	boolean filterNewUrl(String url) {
		return scrappedNormalizedUrl.add(normalize(url));
	}

	boolean filterOnlySubUrl(String url) {
		String normalizedUrl = normalize(url);
		return scrappedNormalizedUrl.stream()
				.anyMatch(alreadyScrappedUrl -> normalizedUrl.startsWith(alreadyScrappedUrl));
	}

	String normalize(String url) {
		String normalizedUrl = RequestUtil.normalize(url);
		while (normalizedUrl.endsWith("/")) {
			normalizedUrl = normalizedUrl.substring(0, normalizedUrl.length() - 1);
		}
		return normalizedUrl;
	}

	public static Props props(ActorSystem system, List<String> resultCollector) {
		return Props.create(Supervisor.class, () -> new Supervisor(system, resultCollector));
	}

	public Supervisor(ActorSystem system, List<String> resultCollector) {
		this.system = system;
		this.resultCollector = resultCollector;
		indexer = context().actorOf(Indexer.props(self()));
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Start) {
			Start start = (Start) message;
			this.filterNewUrl(start.url.toString());
			logger.debug("starting {}", start.url);
			scrap(start.url);
		} else if (message instanceof ScrapFinished) {
			ScrapFinished scrapFinished = (ScrapFinished) message;
			logger.debug("scraping finished {}", scrapFinished.url);
		} else if (message instanceof IndexFinished) {
			IndexFinished indexFinished = (IndexFinished) message;
			if (numVisited < maxPages) {
				indexFinished.urls.stream()
						.filter(url -> this.filterNewUrl(url.toString()) && this.filterOnlySubUrl(url.toString()))
						.filter(l -> !scrapCounts.containsKey(l)).forEach(this::scrap);
			}
			checkAndShutdown(indexFinished.url);
		} else if (message instanceof ScrapFailure) {
			ScrapFailure scrapFailure = (ScrapFailure) message;
			int retries = scrapCounts.get(scrapFailure.url);
			logger.debug("scraping failed {}, {}, reason = {}", scrapFailure.url, retries,
					scrapFailure.reason.getMessage());
			if (retries < maxRetries) {
				countVisits(scrapFailure.url);
				host2Actor.get(scrapFailure.url.getHost()).tell(new Scrap(scrapFailure.url), self());
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
				ActorRef buff = context().system().actorOf(SiteCrawler.props(self(), indexer));
				host2Actor.put(host, buff);
				actor = buff;
			}

			numVisited += 1;
			toScrap.add(url);
			resultCollector.add(url.toString());
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
