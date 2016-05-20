package fr.djoutsop.crawler.service.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import fr.djoutsop.crawler.entity.Content;

@RunWith(MockitoJUnitRunner.class)
public class ProceduralWebCrawlerServiceTest {


	@Test
	public void crawl_ShouldOnlyScrapNewUrl() throws IOException {
		// Given
		Scraper scraperServiceMock = mock(Scraper.class);
		ProceduralWebCrawlerService subject = new ProceduralWebCrawlerService(4);
		subject.scraper = scraperServiceMock;
		
		when(scraperServiceMock.scrap(eq("http://bidule"), anyString())).thenReturn(new Content("", "", Arrays
				.asList("http://bidule/1.zip", "http://bidule/sub1", "http://bidule/sub2")));
		
		when(scraperServiceMock.scrap(eq("http://bidule/sub1"), anyString())).thenReturn(new Content("", "", Arrays
				.asList("http://bidule/2.zip", "http://bidule", "http://bidule/sub1")));
		
		when(scraperServiceMock.scrap(eq("http://bidule/sub2"), anyString())).thenReturn(new Content("", "", Arrays
				.asList("http://bidule/3.zip", "http://bidule", "http://bidule/sub2")));
		
		// When
		List<String> result = subject.crawl("http://bidule").collect(Collectors.toList());
		
		// Then
		assertThat(result.size(), is(3));
		assertThat(result, containsInAnyOrder(
				"http://bidule/1.zip",
				"http://bidule/2.zip",
				"http://bidule/3.zip"));
	}
	
	
	@Test
	public void crawl_WithoutSpecifiedExtension_ShouldReturnAllTerminalFiles() throws IOException {
		// Given
		Scraper scraperServiceMock = mock(Scraper.class);
		ProceduralWebCrawlerService subject = new ProceduralWebCrawlerService(4);
		subject.scraper = scraperServiceMock;
		
		when(scraperServiceMock.scrap(eq("http://bidule"), anyString())).thenReturn(new Content("", "", Arrays
				.asList("http://bidule/1.zip", "http://bidule/2.png", "http://bidule/3.txt")));
		
		// When
		List<String> result = subject.crawl("http://bidule").collect(Collectors.toList());
		
		// Then
		assertThat(result.size(), is(3));
		assertThat(result, containsInAnyOrder(
				"http://bidule/1.zip",
				"http://bidule/2.png",
				"http://bidule/3.txt"));
	}
	
	@Test
	public void crawl_WithSpecifiedExtension_ShouldReturnAllowedTerminalFiles() throws IOException {
		// Given
		Scraper scraperServiceMock = mock(Scraper.class);
		ProceduralWebCrawlerService subject = new ProceduralWebCrawlerService(4,"zip");
		subject.scraper = scraperServiceMock;
		
		when(scraperServiceMock.scrap(eq("http://bidule"), anyString())).thenReturn(new Content("", "", Arrays
				.asList("http://bidule/1.zip", "http://bidule/2.png", "http://bidule/3.txt", "http://bidule/4.zIp")));
		
		// When
		List<String> result = subject.crawl("http://bidule").collect(Collectors.toList());
		
		// Then
		assertThat(result.size(), is(2));
		assertThat(result, containsInAnyOrder(
				"http://bidule/1.zip",
				"http://bidule/4.zIp"));
	}
	
	
	@Test
	public void crawl_ShouldOnlyScrapSubUrl() throws IOException {
		// Given
		Scraper scraperServiceMock = mock(Scraper.class);
		ProceduralWebCrawlerService subject = new ProceduralWebCrawlerService(4);
		subject.scraper = scraperServiceMock;
		
		when(scraperServiceMock.scrap(eq("http://bidule"), anyString())).thenReturn(new Content("", "", Arrays
				.asList("http://bidule/1.zip", "http://bidule/sub1", "http://bidule/sub2", "http://bidule/../branch", "http://branch2")));
		
		when(scraperServiceMock.scrap(eq("http://bidule/sub1"), anyString())).thenReturn(new Content("", "", Arrays
				.asList("http://bidule/2.zip", "http://bidule", "http://bidule/sub1")));
		
		when(scraperServiceMock.scrap(eq("http://bidule/sub2"), anyString())).thenReturn(new Content("", "", Arrays
				.asList("http://bidule/3.zip", "http://bidule", "http://bidule/sub2")));
		
		when(scraperServiceMock.scrap(eq("http://bidule/../branch"), anyString())).thenReturn(new Content("", "", Arrays
				.asList("http://branch/4.zip", "http://branch/5.zip", "http://branch/6.zip")));
		
		when(scraperServiceMock.scrap(eq("http://branch2"), anyString())).thenReturn(new Content("", "", Arrays
				.asList("http://branch2/7.zip", "http://branch2/8.zip", "http://branch2/9.zip")));
		
		// When
		List<String> result = subject.crawl("http://bidule").collect(Collectors.toList());
		
		// Then
		assertThat(result.size(), is(3));
		assertThat(result, containsInAnyOrder(
				"http://bidule/1.zip",
				"http://bidule/2.zip",
				"http://bidule/3.zip"));
	}
	
	@Test
	public void crawl_ShouldOnlyScrapSpecifiedDepth() throws IOException {
		// Given
		int maxDepth = 2;
		Scraper scraperServiceMock = mock(Scraper.class);
		ProceduralWebCrawlerService subject = new ProceduralWebCrawlerService(maxDepth);
		subject.scraper = scraperServiceMock;
		
		when(scraperServiceMock.scrap(eq("http://bidule"), anyString())).thenReturn(new Content("", "", Arrays
				.asList("http://bidule/1.zip", "http://bidule/sub1")));
		
		when(scraperServiceMock.scrap(eq("http://bidule/sub1"), anyString())).thenReturn(new Content("", "", Arrays
				.asList("http://bidule/2.zip", "http://bidule/sub2")));
		
		when(scraperServiceMock.scrap(eq("http://bidule/sub2"), anyString())).thenReturn(new Content("", "", Arrays
				.asList("http://bidule/3.zip", "http://bidule/sub3")));

		when(scraperServiceMock.scrap(eq("http://bidule/sub3"), anyString())).thenReturn(new Content("", "", Arrays
				.asList("http://bidule/4.zip", "http://bidule/sub4")));
		
		when(scraperServiceMock.scrap(eq("http://bidule/sub4"), anyString())).thenReturn(new Content("", "", Arrays
				.asList("http://bidule/5.zip")));
		
		// When
		List<String> result = subject.crawl("http://bidule").collect(Collectors.toList());
		
		// Then
		assertThat(result.size(), is(2));
		assertThat(result, containsInAnyOrder(
				"http://bidule/1.zip",
				"http://bidule/2.zip"));
	}
	
	
	@Test
	public void crawl_ShouldScrapNothingWithoutDepth() throws IOException {
		// Given
		int maxDepth = 0;
		Scraper scraperServiceMock = mock(Scraper.class);
		ProceduralWebCrawlerService subject = new ProceduralWebCrawlerService(maxDepth);
		subject.scraper = scraperServiceMock;
		
		when(scraperServiceMock.scrap(eq("http://bidule"), anyString())).thenReturn(new Content("", "", Arrays
				.asList("http://bidule/1.zip", "http://bidule/sub1")));
		
		when(scraperServiceMock.scrap(eq("http://bidule/sub1"), anyString())).thenReturn(new Content("", "", Arrays
				.asList("http://bidule/2.zip", "http://bidule/sub2")));
		
		when(scraperServiceMock.scrap(eq("http://bidule/sub2"), anyString())).thenReturn(new Content("", "", Arrays
				.asList("http://bidule/3.zip", "http://bidule/sub3")));

		when(scraperServiceMock.scrap(eq("http://bidule/sub3"), anyString())).thenReturn(new Content("", "", Arrays
				.asList("http://bidule/4.zip", "http://bidule/sub4")));
		
		when(scraperServiceMock.scrap(eq("http://bidule/sub4"), anyString())).thenReturn(new Content("", "", Arrays
				.asList("http://bidule/5.zip")));
		
		// When
		List<String> result = subject.crawl("http://bidule").collect(Collectors.toList());
		
		// Then
		assertThat(result.size(), is(0));
	}

	@Test
	public void recursiveScrap_ShouldReturnEmptyResultWhenException() throws IOException {
		// Given
		Scraper scraperServiceMock = mock(Scraper.class);
		Logger loggerMock = mock(Logger.class);
		
		ProceduralWebCrawlerService subject = new ProceduralWebCrawlerService(4);
		subject.scraper = scraperServiceMock;
		subject.logger = loggerMock;
		
		IOException ioException = new IOException();
		when(scraperServiceMock.scrap(anyString(), anyString())).thenThrow(ioException);
		
		// When
		List<String> result = subject.recursiveScrap("http://bidule", "http://bidule", 0).collect(Collectors.toList());
		
		// Then
		verify(loggerMock, times(1)).error(ioException.getMessage(),ioException);
		assertThat(result.isEmpty(), is(true));
	}

	
	@Test
	public void filterNewUrl_ShouldReturnTrueIfNewUrl() throws Exception {
		// Given
		ProceduralWebCrawlerService subject = new ProceduralWebCrawlerService(4);
		subject.scrappedUrl.add("http://url1");
		
		// When
		boolean resultFirst = subject.filterNewUrl("http://url2");
		boolean resultSecond = subject.filterNewUrl("http://url2");
		
		// Then
		assertThat(resultFirst, is(true));
		assertThat(resultSecond, is(false));
	}
	
	@Test
	public void filterNewUrl_ShouldReturnFalseIfSameUrl() throws Exception {
		// Given
		ProceduralWebCrawlerService subject = new ProceduralWebCrawlerService(4);
		subject.filterNewUrl("http://bidule/path");
		subject.filterNewUrl("http://bidule/path/1");
		
		// When
		boolean result1 = subject.filterNewUrl("http://bidule/path/1/");
		boolean result2 = subject.filterNewUrl("http://bidule/path/1/../1");
		boolean result3 = subject.filterNewUrl("http://bidule/path/1/./../");
		boolean result4 = subject.filterNewUrl("http://bidule/path/1/.././1");
		boolean result5 = subject.filterNewUrl("http://bidule/path/./");
		
		// Then
		assertThat(result1, is(false));
		assertThat(result2, is(false));
		assertThat(result3, is(false));
		assertThat(result4, is(false));
		assertThat(result5, is(false));
	}
	
	
//	@Ignore("Pas possible pour le moment")
//	@Test
//	public void scrap_ShouldBeThreadSafe() throws IOException, InterruptedException {
//		// Given
//		Scraper scraperServiceMock = mock(Scraper.class);
//		ProceduralWebCrawlerService subject = new ProceduralWebCrawlerService(4);
//		subject.scraper = scraperServiceMock;
//		
//		when(scraperServiceMock.scrap(eq("http://bidule"), anyString())).thenReturn(new Content("", "", Arrays
//				.asList("http://bidule/1.zip", "http://bidule/sub1", "http://bidule/sub2")));
//		
//		when(scraperServiceMock.scrap(eq("http://bidule/sub1"), anyString())).thenReturn(new Content("", "", Arrays
//				.asList("http://bidule/2.zip", "http://bidule", "http://bidule/sub1")));
//		
//		when(scraperServiceMock.scrap(eq("http://bidule/sub2"), anyString())).thenReturn(new Content("", "", Arrays
//				.asList("http://bidule/3.zip", "http://bidule", "http://bidule/sub2")));
//		
//		List<Callable<List<String>>> callables = new ArrayList<>();
//		for(int i=0;i<100;i++) {
//			callables.add(
//					() -> subject.crawl("http://bidule").collect(Collectors.toList())
//			);
//		}
//		
//		ExecutorService executor = Executors.newWorkStealingPool();
//
//		// When
//		List<List<String>> results = executor.invokeAll(callables).stream()
//		    .map(future -> {
//		        try {
//		            return future.get();
//		        }
//		        catch (Exception e) {
//		            throw new IllegalStateException(e);
//		        }
//		    }).collect(Collectors.toList());
//		
//		// Then
//		assertThat(results.stream().allMatch(result ->
//			result.containsAll(Arrays.asList(
//				"http://bidule/1.zip",
//				"http://bidule/2.zip",
//				"http://bidule/3.zip"))), is(true));
//	}
	
	
}
