package fr.djoutsop.crawler.runner;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.djoutsop.crawler.service.WebCrawlerService;
import fr.djoutsop.crawler.service.impl.ProceduralWebCrawlerService;

public class ScraperRunner {
	static Logger logger = LoggerFactory.getLogger(ScraperRunner.class);
	
	public static void main(String[] args) throws IOException {

		int depth = Integer.parseInt(args[0]);
		String urlToCrawl = args[1];
		
		WebCrawlerService webCrawlerService = new ProceduralWebCrawlerService(depth);
		
		webCrawlerService.crawl(urlToCrawl).forEach(content -> logger.debug("{}", content));
	}

}
