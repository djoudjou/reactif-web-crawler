package fr.djoutsop.crawler.service.akka.impl;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import scala.concurrent.Await;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.util.Timeout;
import fr.djoutsop.crawler.service.IWebCrawlerService;
import fr.djoutsop.crawler.service.akka.Messages.Start;
import fr.djoutsop.crawler.service.akka.actor.Supervisor;
import fr.djoutsop.crawler.utils.Loggable;

@Service
public class AkkaWebCrawlerService implements IWebCrawlerService,ScrapObserver {
	@Loggable
	Logger logger;
	//Logger logger = LoggerFactory.getLogger(AkkaWebCrawlerService.class);
	
	List<String> result = new ArrayList<>();

	@Override
	public Stream<String> crawl(String url, int maxDepth,String... extensions)
			throws IOException {

		ActorSystem system = ActorSystem.create();
		ActorRef supervisor = system.actorOf(Supervisor.props(system,this));

		supervisor.tell(new Start(new URL(url)), ActorRef.noSender());

//		Timeout timeout = new Timeout(Duration.create(10, "minutes"));
		Timeout timeout = new Timeout(Duration.create(5, "seconds"));
		try {
			Await.result(system.whenTerminated(), timeout.duration());
		} catch (TimeoutException e) {
			logger.debug("c'est fini trop tard");
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}

		supervisor.tell(PoisonPill.getInstance(), ActorRef.noSender());

		system.terminate();
		return result.stream();
	}

	@Override
	public void scrap(URL url) {
		result.add(url.toString());		
	}



	

}
