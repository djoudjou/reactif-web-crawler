package fr.djoutsop.crawler.service.akka.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import fr.djoutsop.crawler.WebCrawlerConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(WebCrawlerConfiguration.class)
public class AkkaWebCrawlerServiceTest {

	@Autowired
	ApplicationContext applicationContext;
	
	
	@Test
	public void akkaWebCrawlerService_ShouldNotBeSingleton(){
		// Given
		AkkaWebCrawlerService bean =  applicationContext.getBean(AkkaWebCrawlerService.class);
		
		// When
		AkkaWebCrawlerService otherBean =  applicationContext.getBean(AkkaWebCrawlerService.class);		
		
		// Then
		assertThat(bean, not(is(sameInstance(otherBean))));
		
	}
	
}
