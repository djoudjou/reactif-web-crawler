package fr.djoutsop.crawler.runner;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.djoutsop.crawler.service.ScraperService;
import fr.djoutsop.crawler.service.WebCrawlerService;

public class ScraperRunner {
	static Logger logger = LoggerFactory.getLogger(ScraperRunner.class);
	
	public static void main(String[] args) throws IOException {

		WebCrawlerService webCrawlerService = new WebCrawlerService(5);
		webCrawlerService.setScraperService(new ScraperService("zip"));

		webCrawlerService.crawl("http://wallagain.cc/content/comics/one_punchman_56161ed820296")
				.forEach(content -> logger.debug("{}", content));
	}

}
