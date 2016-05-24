package fr.djoutsop.crawler.service.akka.actor;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import fr.djoutsop.crawler.service.akka.Messages.Content;
import fr.djoutsop.crawler.service.akka.Messages.Index;
import fr.djoutsop.crawler.service.akka.Messages.IndexFinished;

public class Indexer extends UntypedActor {
	Logger logger = LoggerFactory.getLogger(Indexer.class);

	ActorRef supervisor;

	Map<URL, Content> store = new HashMap<>();

	public static Props props(ActorRef supervisor) {
		return Props.create(Indexer.class, () -> new Indexer(supervisor));
	}

	public Indexer(ActorRef supervisor) {
		this.supervisor = supervisor;
	}

	@Override
	public void postStop() throws Exception {
		super.postStop();
		//store.forEach((url, content) -> logger.debug("{} {}", url, content));
		//logger.debug("{}", store.size());
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if(message instanceof Index) {
			Index index = (Index)message;
			logger.debug("saving page {} with {}", index.url, index.content);
			store.put(index.url, index.content);
			supervisor.tell(new IndexFinished(index.url, index.content.urls), self());
		} else {
			unhandled(message);
		}
		
	}
}
