package fr.djoutsop.crawler.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.io.IOException;
import java.util.List;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import fr.djoutsop.crawler.WebCrawlerConfiguration;
import fr.djoutsop.crawler.entity.Method;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(WebCrawlerConfiguration.class)
@WebIntegrationTest(randomPort = true)
public class WebCrawlerControllerIntTest {

	private Server server;

	@Value("${local.server.port}")
	private int port;

	/**
	 * Returns the base url for your rest interface
	 * 
	 * @return
	 */
	private String getBaseUrl() {
		return "http://localhost:" + port;
	}

	@Before
	public void startJetty() throws Exception {
		server = new Server(8080);
		ResourceHandler resource_handler = new ResourceHandler();
		resource_handler.setDirectoriesListed(true);
		resource_handler.setWelcomeFiles(new String[] { "index.html" });
		resource_handler.setResourceBase(".");
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { resource_handler, new DefaultHandler() });
		server.setHandler(handlers);
		server.start();
	}

	@After
	public void stop() throws Exception {
		if (server != null) {
			server.stop();
			server.join();
			server.destroy();
			server = null;
		}
	}

	@Test
	public void crawl_WithStandardMethod() throws IOException {
		// Given
		RestTemplate restTemplate = new TestRestTemplate();
		String url = "http://localhost:8080/src/test/resources/fakesite/one_punchman_56161ed820296";
		String queryUri = getBaseUrl() + "/crawl?depth=4&filter=zip&url="+url;

		// When
		List<String> result = restTemplate.getForObject(queryUri, List.class);

		// Then
		assertThat(result, containsInAnyOrder(
				url+"/0_0_le_jour_de_cong_de_tatsumaki_5676a32b90a8a/One_Punch-Man_v10_c0.zip",
				url+"/0_2_special_sentai_572ca2d478266/One_Punch-Man_c0_s2.zip"));

	}
	
	@Test
	public void crawl_WithAkkaMethod() throws IOException {
		// Given
		RestTemplate restTemplate = new TestRestTemplate();
		String url = "http://localhost:8080/src/test/resources/fakesite/one_punchman_56161ed820296";
		String queryUri = getBaseUrl() + "/crawl?depth=4&filter=zip&url="+url+"&method="+Method.AKKA;

		// When
		List<String> result = restTemplate.getForObject(queryUri, List.class);

		// Then
		assertThat(result, containsInAnyOrder(
				url+"/0_0_le_jour_de_cong_de_tatsumaki_5676a32b90a8a/One_Punch-Man_v10_c0.zip",
				url+"/0_2_special_sentai_572ca2d478266/One_Punch-Man_c0_s2.zip"));

	}
}
