package fr.djoutsop.crawler.service.akka.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.RequestUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.util.Timeout;
import fr.djoutsop.crawler.service.IWebCrawlerService;
import fr.djoutsop.crawler.service.akka.Messages.Start;
import fr.djoutsop.crawler.service.akka.actor.Supervisor;
import fr.djoutsop.crawler.utils.Loggable;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

@Service
@Scope("prototype")
public class AkkaWebCrawlerService implements IWebCrawlerService {
	@Loggable
	Logger logger;
	
	@Value("${crawler_duration}")
	private int duration;

	@Override
	public Stream<String> crawl(String url, int maxDepth, String... extensions) throws IOException {

		List<String> result = crawl(url,extensions);
		return result.stream();
	}

	List<String> crawl(String url,String... extensions) throws MalformedURLException {
		List<String> result = new ArrayList<>();

		ActorSystem system = ActorSystem.create("scraper-system");
		ActorRef supervisor = system.actorOf(Supervisor.props(system, extensions, result));

		supervisor.tell(new Start(new URL(url)), ActorRef.noSender());

		Timeout timeout = new Timeout(Duration.create(duration, "seconds"));
		try {
			Await.result(system.whenTerminated(), timeout.duration());
		} catch (TimeoutException e) {
			logger.debug("c'est fini trop tard");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		supervisor.tell(PoisonPill.getInstance(), ActorRef.noSender());

		system.terminate();
		return result;
	}
}
