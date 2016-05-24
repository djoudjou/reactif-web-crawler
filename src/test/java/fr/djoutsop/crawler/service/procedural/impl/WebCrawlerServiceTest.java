package fr.djoutsop.crawler.service.procedural.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import fr.djoutsop.crawler.entity.Content;
import fr.djoutsop.crawler.service.procedural.impl.Scraper;
import fr.djoutsop.crawler.service.procedural.impl.WebCrawlerService;

@RunWith(MockitoJUnitRunner.class)
public class WebCrawlerServiceTest {

	WebCrawlerService subject;
	Scraper scraperServiceMock;
	Logger loggerMock;
	
	@Before
	public void setup() {
		subject = new WebCrawlerService();
		scraperServiceMock = mock(Scraper.class);
		loggerMock = mock(Logger.class);
		
		subject.scraper = scraperServiceMock;
		subject.logger = loggerMock;
	}

	@Test
	public void crawl_ShouldOnlyScrapNewUrl() throws IOException {
		// Given
		when(scraperServiceMock.scrap(eq("http://bidule"), anyString())).thenReturn(new Content("", "", Arrays
				.asList("http://bidule/1.zip", "http://bidule/sub1", "http://bidule/sub2")));
		
		when(scraperServiceMock.scrap(eq("http://bidule/sub1"), anyString())).thenReturn(new Content("", "", Arrays
				.asList("http://bidule/2.zip", "http://bidule", "http://bidule/sub1")));
		
		when(scraperServiceMock.scrap(eq("http://bidule/sub2"), anyString())).thenReturn(new Content("", "", Arrays
				.asList("http://bidule/3.zip", "http://bidule", "http://bidule/sub2")));
		
		// When
		List<String> result = subject.crawl("http://bidule",4).collect(Collectors.toList());
		
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
		when(scraperServiceMock.scrap(eq("http://bidule"), anyString())).thenReturn(new Content("", "", Arrays
				.asList("http://bidule/1.zip", "http://bidule/2.png", "http://bidule/3.txt")));
		
		// When
		List<String> result = subject.crawl("http://bidule",4).collect(Collectors.toList());
		
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
		when(scraperServiceMock.scrap(eq("http://bidule"), anyString())).thenReturn(new Content("", "", Arrays
				.asList("http://bidule/1.zip", "http://bidule/2.png", "http://bidule/3.txt", "http://bidule/4.zIp")));
		
		// When
		List<String> result = subject.crawl("http://bidule",4,"zip").collect(Collectors.toList());
		
		// Then
		assertThat(result.size(), is(2));
		assertThat(result, containsInAnyOrder(
				"http://bidule/1.zip",
				"http://bidule/4.zIp"));
	}
	
	
	@Test
	public void crawl_ShouldOnlyScrapSubUrl() throws IOException {
		// Given
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
		List<String> result = subject.crawl("http://bidule",4).collect(Collectors.toList());
		
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
		List<String> result = subject.crawl("http://bidule", maxDepth).collect(Collectors.toList());
		
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
		List<String> result = subject.crawl("http://bidule",maxDepth).collect(Collectors.toList());
		
		// Then
		assertThat(result.size(), is(0));
	}

	@Test
	public void recursiveScrap_ShouldReturnEmptyResultWhenException() throws IOException {
		// Given
		IOException ioException = new IOException();
		when(scraperServiceMock.scrap(anyString(), anyString())).thenThrow(ioException);
		String url = "http://bidule";
		
		// When
		List<String> result = subject.recursiveScrap(url, url, 0).collect(Collectors.toList());
		
		// Then
		verify(loggerMock, times(1)).error("scrap url={} failed with={}", url, ioException.getMessage(),ioException);
		assertThat(result.isEmpty(), is(true));
	}

	
	@Test
	public void filterNewUrl_ShouldReturnTrueIfNewUrl() throws Exception {
		// Given
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
