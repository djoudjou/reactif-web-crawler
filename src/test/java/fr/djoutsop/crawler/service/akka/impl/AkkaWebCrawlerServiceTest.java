package fr.djoutsop.crawler.service.akka.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;


public class AkkaWebCrawlerServiceTest {
	
	AkkaWebCrawlerService subject;
	
	@Before
	public void setup() {
		subject = new AkkaWebCrawlerService();
	}

	@Test
	public void mapUrl_ShouldReturnAbsUrlWhenGiven() throws Exception {
		// Given
		

		// When
		Stream<String> result = subject.crawl("https://foat.me", 10, "zip");
		
		result.forEach(System.out::println);

		// Then
		assertThat(result, is("http://toto/pictures"));
	}
	
}
