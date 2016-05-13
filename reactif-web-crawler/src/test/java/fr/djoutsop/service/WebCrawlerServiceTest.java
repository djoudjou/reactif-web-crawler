package fr.djoutsop.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import fr.djoutsop.entity.Link;

public class WebCrawlerServiceTest {

	WebCrawlerService subject;
	
	@Before
	public void setup() {
		subject = new WebCrawlerService();
	}
	
	@Test
	public void ozef() throws IOException {
		//Link link = new Link("http://wallagain.cc/content/comics/one_punchman_56161ed820296", new Date());
		Link link = new Link("https://foat.me", new Date());
		subject.crawl(link);
	}
}
