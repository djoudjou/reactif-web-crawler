package fr.djoutsop.service;

import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import fr.djoutsop.entity.Content;

public class ScrapServiceTest {

	ScraperService subject;
	
	@Before
	public void setup() {
		subject = new ScraperService();
	}
	
	@Test
	public void scrap() throws IOException {
//		Link link = new Link("http://wallagain.cc/content/comics/one_punchman_56161ed820296", new Date());
		Content content = subject.scrap(new URL("https://foat.me"),null);
		System.out.println(content);
	}
}
