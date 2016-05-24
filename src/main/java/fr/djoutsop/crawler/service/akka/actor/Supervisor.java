package fr.djoutsop.crawler.service.akka.actor;


import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.japi.pf.ReceiveBuilder;
import fr.djoutsop.crawler.service.akka.Messages.IndexFinished;
import fr.djoutsop.crawler.service.akka.Messages.Start;

public class Supervisor extends UntypedActor {
	
	Logger logger = LoggerFactory.getLogger(Supervisor.class);
	
	final int maxPages = 100;
	final int maxRetries = 2;
	int numVisited = 0;
	
	Set<String> toScrap = new HashSet<>();
	Map<String,Integer> scrapCounts = new HashMap<>();
	Map<String, ActorRef> host2Actor = new HashMap<>();
	
	public Supervisor() {
		final ActorRef indexer = context().actorOf(Indexer.props(self()));
		
		receive(ReceiveBuilder.
			      match(Start.class, start -> {
					logger.debug("starting {}", start.url);
					store.put(index.url, index.content);
					supervisor.tell(new IndexFinished(index.url, index.content.urls), self());
			      }).build()
			    );
		
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if(message instanceof Start) {
			Start start = (Start)message;
			logger.debug("starting {}", start.url);
			scrap(start.url);
		} else {
			unhandled(message);
		}
		
	}

	void scrap(URL url) {
		String host = url.getHost();
		logger.debug("host = {}",host);
		if (!host.isEmpty()) {
			ActorRef actor = host2Actor.get(host);
			if(actor == null) {
				SiteCrawler
//		        ActorRef buff = context().system().actorOf( Props(new SiteCrawler(self, indexer)))
		        host2Actor += (host -> buff)
		        buff
		      }

			      numVisited += 1
			      toScrap += url
			      countVisits(url)
			      actor ! Scrap(url)
			    }
	}
	
	
	
	

//
//			  def receive: Receive = {
//			    case Start(url) =>
//			      println(s"starting $url")
//			      scrap(url)
//			    case ScrapFinished(url) =>
//			      println(s"scraping finished $url")
//			    case IndexFinished(url, urls) =>
//			      if (numVisited < maxPages)
//			        urls.toSet.filter(l => !scrapCounts.contains(l)).foreach(scrap)
//			      checkAndShutdown(url)
//			    case ScrapFailure(url, reason) =>
//			      val retries: Int = scrapCounts(url)
//			      println(s"scraping failed $url, $retries, reason = $reason")
//			      if (retries < maxRetries) {
//			        countVisits(url)
//			        host2Actor(url.getHost) ! Scrap(url)
//			      } else
//			        checkAndShutdown(url)
//			  }
//
//			  def checkAndShutdown(url: URL): Unit = {
//			    toScrap -= url
//			    // if nothing to visit
//			    if (toScrap.isEmpty) {
//			      self ! PoisonPill
//			      system.terminate()
//			    }
//			  }
//
//			  def scrap(url: URL) = {
//			    val host = url.getHost
//			    println(s"host = $host")
//			    if (!host.isEmpty) {
//			      val actor = host2Actor.getOrElse(host, {
//			        val buff = system.actorOf(Props(new SiteCrawler(self, indexer)))
//			        host2Actor += (host -> buff)
//			        buff
//			      })
//
//			      numVisited += 1
//			      toScrap += url
//			      countVisits(url)
//			      actor ! Scrap(url)
//			    }
//			  }
//
//			  def countVisits(url: URL): Unit = scrapCounts += (url -> (scrapCounts.getOrElse(url, 0) + 1))
}
