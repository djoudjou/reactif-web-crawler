package fr.djoutsop.crawler.service.akka.actor;

import java.net.URL;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.dispatch.Futures;
import akka.dispatch.Recover;
import akka.pattern.Patterns;
import akka.util.Timeout;
import fr.djoutsop.crawler.service.akka.Messages.Scrap;
import fr.djoutsop.crawler.service.akka.Messages.*;

public class SiteCrawler extends UntypedActor {
	Logger logger = LoggerFactory.getLogger(SiteCrawler.class);

	final String process = "Process next url";

	final ActorRef scraper;

	ActorRef supervisor;
	ActorRef indexer;

	Queue<URL> toProcess = new ConcurrentLinkedQueue<>();

	public static Props props(ActorRef supervisor, ActorRef indexer) {
		return Props.create(SiteCrawler.class, () -> new SiteCrawler(
				supervisor, indexer));
	}

	public SiteCrawler(ActorRef supervisor, ActorRef indexer) {
		this.supervisor = supervisor;
		this.indexer = indexer;

		scraper = context().actorOf(Scraper.props(indexer));

		ActorSystem system = context().system();
		system.scheduler().schedule(Duration.Zero(),
				Duration.create(1000, TimeUnit.MILLISECONDS), self(), process,
				system.dispatcher(), null);

	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Scrap) {
			Scrap scrap = (Scrap) message;
			logger.debug("waiting... {}", scrap.url);
			toProcess.offer(scrap.url);
		}
		if (process.equals(message)) {
			URL url = toProcess.poll();
			if (url != null) {
				logger.debug("site scraping... {}", url);

				Timeout timeout = new Timeout(Duration.create(3, "seconds"));
				Future<Object> ask = Patterns.ask(scraper, new Scrap(url),timeout);

				final ExecutionContext ec = context().dispatcher();

				Future<ScrapFailure> askWithRecover = ask.recoverWith(
						new Recover<Future<ScrapFailure>>() {

							@Override
							public Future<ScrapFailure> recover(
									Throwable exception) throws Throwable {
								logger.error("site scraping {} error {}", url,exception.getMessage());
								return Futures.future(() -> new ScrapFailure(url,exception), ec);
							}

						}, ec);
				akka.pattern.Patterns.pipe(askWithRecover, ec).to(supervisor);
			}
		} else {
			unhandled(message);
		}

	}

}
