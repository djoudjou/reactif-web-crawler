package fr.djoutsop;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import fr.djoutsop.entity.Content;
import fr.djoutsop.service.ScraperService;
import fr.djoutsop.service.WebCrawlerService;


public class WebCrawlerStep {
	
	private WebCrawlerService webCrawler;
	private ScraperService scraperService;
	private Content result;
	 
    @Before
    public void setUp() {
    	webCrawler = new WebCrawlerService();
    	scraperService = new ScraperService();
    	webCrawler.setScraperService(scraperService);
    }
 
 
    @When("^I parse the following html content :$")
    public void i_parse_the_following_html_content(String htmlContent) throws Throwable {
    	result = scraperService.scrap(htmlContent);
    }

    @Then("^result should have title \"([^\"]*)\" with date \"([^\"]*)\"$")
    public void result_must_should_have_title_with_date(String path, String lastModified) throws Throwable {
    	System.out.println(result);
        assertEquals(true, result.getUrls().contains(new URL(path)));
    }

}
