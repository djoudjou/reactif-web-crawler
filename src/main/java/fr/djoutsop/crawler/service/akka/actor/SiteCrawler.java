package fr.djoutsop.crawler.service.akka.actor;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import fr.djoutsop.crawler.service.akka.Messages.Content;
import fr.djoutsop.crawler.service.akka.Messages.Index;
import fr.djoutsop.crawler.service.akka.Messages.IndexFinished;

public class SiteCrawler extends AbstractActor {
	Logger logger = LoggerFactory.getLogger(SiteCrawler.class);

	ActorRef supervisor;
	ActorRef indexer;


	public static Props props(ActorRef supervisor,ActorRef indexer) {
		return Props.create(SiteCrawler.class, () -> new SiteCrawler(supervisor, indexer));
	}

	public SiteCrawler(ActorRef supervisor,ActorRef indexer) {
		this.supervisor = supervisor;
		this.indexer = indexer;
		
		final String process = "Process next url";

		ActorRef scraper = context().actorOf(Scraper.props(indexer));
//	  implicit val timeout = Timeout(3 seconds)
//	  val tick =
//	    context.system.scheduler.schedule(0 millis, 1000 millis, self, process)
//	  var toProcess = List.empty[URL]
		
		
		receive(ReceiveBuilder.match(Index.class, index -> {
			logger.debug("saving page {} with {}", index.url, index.content);
//			store.put(index.url, index.content);
			supervisor.tell(new IndexFinished(index.url, index.content.urls), self());
		}).build());
	}


}
