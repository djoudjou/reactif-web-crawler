package fr.djoutsop.crawler.service.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProceduralWebCrawlerServiceIntTest {


	private Server server;

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
	public void crawl() throws IOException {
		// Given
		ProceduralWebCrawlerService subject = new ProceduralWebCrawlerService(4,"zip");
		subject.scraper = new Scraper();
		
		// When
		List<String> result = subject.crawl("http://localhost:8080/src/test/resources/fakesite/one_punchman_56161ed820296").collect(Collectors.toList());
		
		// Then
		assertThat(result, containsInAnyOrder(
				"http://localhost:8080/src/test/resources/fakesite/one_punchman_56161ed820296/0_0_le_jour_de_cong_de_tatsumaki_5676a32b90a8a/%5BMFT%5DOne_Punch-Man_v10_c0.zip",
				"http://localhost:8080/src/test/resources/fakesite/one_punchman_56161ed820296/0_2_special_sentai_572ca2d478266/%5BMFT%5DOne_Punch-Man_c0_s2.zip"));

	}
}
