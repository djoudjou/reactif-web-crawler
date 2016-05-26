package fr.djoutsop.crawler.cucumber;

import static org.junit.Assert.assertEquals;

import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import fr.djoutsop.crawler.entity.Content;
import fr.djoutsop.crawler.service.IScraperService;
import fr.djoutsop.crawler.service.impl.ScraperService;


public class ScraperStep {
	
	private IScraperService scraperService;
	private Content result;
	
    @Before
    public void setUp() {
    	scraperService = new ScraperService();
    }
 
 
    @When("^I parse the following html content :$")
    public void i_parse_the_following_html_content(String htmlContent) throws Throwable {
    	result = scraperService.scrapHtmlContent(htmlContent);
    }


    @Then("^result should have path \"([^\"]*)\"$")
    public void result_should_have_path(String path) throws Throwable {
    	assertEquals(true, result.getUrls().contains(path));
    }

}
