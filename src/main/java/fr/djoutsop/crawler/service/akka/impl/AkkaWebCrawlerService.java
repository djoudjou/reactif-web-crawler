package fr.djoutsop.crawler.service.akka.impl;

import java.io.IOException;
import java.util.stream.Stream;

import fr.djoutsop.crawler.service.IWebCrawlerService;

public class AkkaWebCrawlerService implements IWebCrawlerService {

	@Override
	public Stream<String> crawl(String url, int maxDepth, String... extensions) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
