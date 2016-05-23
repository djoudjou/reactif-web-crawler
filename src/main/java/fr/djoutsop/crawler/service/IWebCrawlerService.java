package fr.djoutsop.crawler.service;

import java.io.IOException;
import java.util.stream.Stream;

public interface IWebCrawlerService {
	public Stream<String> crawl(String url,int maxDepth, String... extensions) throws IOException;
}
