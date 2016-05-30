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

	Set<String> allowedExtensions = new HashSet<>();

	boolean filterAllowedExtensions(String url) {
		if (!allowedExtensions.isEmpty()) {
			String extension = FilenameUtils.getExtension(url).toLowerCase();
			return allowedExtensions.contains(extension);
		} else {
			return true;
		}
	}

	String normalize(String url) {
		String normalizedUrl = RequestUtil.normalize(url);
		while (normalizedUrl.endsWith("/")) {
			normalizedUrl = normalizedUrl.substring(0, normalizedUrl.length() - 1);
		}
		return normalizedUrl;
	}
	
	@Override
	public Stream<String> crawl(String url, int maxDepth, String... extensions) throws IOException {

		for (String ext : extensions) {
			this.allowedExtensions.add(ext.toLowerCase());
		}
		List<String> result = crawl(url);
		return result.stream().filter(this::filterAllowedExtensions);
	}

	List<String> crawl(String url) throws MalformedURLException {
		List<String> result = new ArrayList<>();

		ActorSystem system = ActorSystem.create("scraper-system");
		ActorRef supervisor = system.actorOf(Supervisor.props(system, result));

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
