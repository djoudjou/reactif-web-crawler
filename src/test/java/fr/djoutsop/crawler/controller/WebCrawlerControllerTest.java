package fr.djoutsop.crawler.controller;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import fr.djoutsop.crawler.service.akka.impl.AkkaWebCrawlerService;
import fr.djoutsop.crawler.service.procedural.impl.StandardWebCrawlerService;

@RunWith(MockitoJUnitRunner.class)
public class WebCrawlerControllerTest {

	private MockMvc mockMvc;
	
	final int defaultDepth = 0;
	final String defaultMethod = "std";
	final String[] defaultExtensions = new String[0];

	@Mock
	StandardWebCrawlerService webCrawlerServiceMock;
	
	@Mock
	AkkaWebCrawlerService akkaWebCrawlerServiceMock;

	@Mock
	Logger loggerMock;

	@InjectMocks
	private WebCrawlerController subject;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(subject).build();
	}

	@Test
	public void crawl_ShouldFailedWithoutUrl() throws Exception {
		// Given
		when(webCrawlerServiceMock.crawl(anyString(), anyInt(), any())).thenReturn(Stream.empty());

		// When
		ResultActions resultActions = mockMvc.perform(get("/crawl"));

		// Then
		resultActions.andExpect(status().isBadRequest());
	}

	@Test
	public void crawl_WhithUrl_ShouldUseDefaultParams() throws Exception {
		// Given
		String url = "http://toto.com";
		when(webCrawlerServiceMock.crawl(url, defaultDepth, defaultExtensions)).thenReturn(Stream.empty());

		// When
		ResultActions resultActions = mockMvc.perform(get("/crawl?url=" + url));

		// Then
		resultActions.andExpect(status().isOk());
		verify(webCrawlerServiceMock, times(1)).crawl(url, defaultDepth);
	}

	@Test
	public void crawl_ShouldUseDepth() throws Exception {
		// Given
		String url = "http://toto.com";
		int depth = 10;
		when(webCrawlerServiceMock.crawl(url, depth, new String[0])).thenReturn(Stream.empty());

		// When
		ResultActions resultActions = mockMvc.perform(get("/crawl?url=" + url + "&depth=" + depth));

		// Then
		resultActions.andExpect(status().isOk());
		verify(webCrawlerServiceMock, times(1)).crawl(url, depth);
	}

	@Test
	public void crawl_ShouldUseExtensions() throws Exception {
		// Given
		String url = "http://toto.com";
		String[] extensions = new String[] { "zip", "png" };
		when(webCrawlerServiceMock.crawl(url, 0, extensions)).thenReturn(Stream.empty());

		// When
		ResultActions resultActions = mockMvc.perform(get("/crawl?url=" + url + "&filter=zip&filter=png"));

		// Then
		resultActions.andExpect(status().isOk());
		verify(webCrawlerServiceMock, times(1)).crawl(url, 0, extensions);
	}

	@Test
	public void crawl_ShouldReturnWebCrawlerServiceResult() throws Exception {
		// Given
		String url = "http://toto.com";
		String urlResult1 = "http://toto.com/sub1.zip";
		String urlResult2 = "http://toto.com/sub2.zip";
		int depth = 10;
		String[] extensions = new String[] { "zip" };
		when(webCrawlerServiceMock.crawl(url, depth, extensions)).thenReturn(Stream.of(urlResult1, urlResult2));

		// When
		ResultActions resultActions = mockMvc.perform(get("/crawl?url=" + url + "&filter=zip&depth=" + depth));

		// Then
		verify(webCrawlerServiceMock, times(1)).crawl(url, depth, extensions);
		resultActions.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0]", is(urlResult1))).andExpect(jsonPath("$[1]", is(urlResult2)));
	}
	
	@Test
	public void crawl_ShouldCallStandardWebCrawlerWithoutMethod() throws Exception {
		// Given
		String url = "http://toto.com";
		when(webCrawlerServiceMock.crawl(url, defaultDepth, defaultExtensions)).thenReturn(Stream.empty());

		// When
		ResultActions resultActions = mockMvc.perform(get("/crawl?url="+url));

		// Then
		resultActions.andExpect(status().isOk());
		verify(webCrawlerServiceMock, times(1)).crawl(url, defaultDepth);
	}
	
	@Test
	public void crawl_ShouldCallAkkWebCrawlerWithAkkaMethod() throws Exception {
		// Given
		String url = "http://toto.com";
		when(webCrawlerServiceMock.crawl(url, defaultDepth, defaultExtensions)).thenReturn(Stream.empty());
		when(akkaWebCrawlerServiceMock.crawl(url, defaultDepth, defaultExtensions)).thenReturn(Stream.empty());

		// When
		ResultActions resultActions = mockMvc.perform(get("/crawl?url="+url+"&method=AKKA"));

		// Then
		resultActions.andExpect(status().isOk());
		verify(akkaWebCrawlerServiceMock, times(1)).crawl(url, defaultDepth);
		verifyZeroInteractions(webCrawlerServiceMock);
	}
	
	@Test
	public void crawl_ShouldCallStandardWebCrawlerWithStandardMethod() throws Exception {
		// Given
		String url = "http://toto.com";
		when(webCrawlerServiceMock.crawl(url, defaultDepth, defaultExtensions)).thenReturn(Stream.empty());
		when(akkaWebCrawlerServiceMock.crawl(url, defaultDepth, defaultExtensions)).thenReturn(Stream.empty());

		// When
		ResultActions resultActions = mockMvc.perform(get("/crawl?url="+url+"&method=STANDARD"));

		// Then
		resultActions.andExpect(status().isOk());
		verify(webCrawlerServiceMock, times(1)).crawl(url, defaultDepth);
		verifyZeroInteractions(akkaWebCrawlerServiceMock);
	}
	
	@Test
	public void crawl_ShouldFaildWithUnknowMethod() throws Exception {
		// Given
		String url = "http://toto.com";
		when(webCrawlerServiceMock.crawl(url, defaultDepth, defaultExtensions)).thenReturn(Stream.empty());
		when(akkaWebCrawlerServiceMock.crawl(url, defaultDepth, defaultExtensions)).thenReturn(Stream.empty());

		// When
		ResultActions resultActions = mockMvc.perform(get("/crawl?url="+url+"&method=toto"));

		// Then
		resultActions.andExpect(status().isBadRequest());
		verifyZeroInteractions(akkaWebCrawlerServiceMock,webCrawlerServiceMock);
	}
}
