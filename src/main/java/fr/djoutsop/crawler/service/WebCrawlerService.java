package fr.djoutsop.crawler.service;

import java.io.IOException;
import java.util.stream.Stream;

public interface WebCrawlerService {
	public Stream<String> crawl(String url) throws IOException;
}
