package fr.djoutsop.service;

import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

public class WebCrawlerServiceTest {

	WebCrawlerService subject;
	
	@Before
	public void setup() {
		subject = new WebCrawlerService();
		subject.setScraperService(new ScraperService("zip"));
	}
	
	@Test
	public void crawl() throws IOException {
//		Link link = new Link("http://wallagain.cc/content/comics/one_punchman_56161ed820296", new Date());
		subject.crawl(new URL("https://foat.me"));
//		subject.crawl(new URL("http://wallagain.cc/content/comics/one_punchman_56161ed820296"));
	}
}
