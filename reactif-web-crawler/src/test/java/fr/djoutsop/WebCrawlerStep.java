package fr.djoutsop;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import fr.djoutsop.entity.Link;
import fr.djoutsop.service.WebCrawlerService;

import static org.junit.Assert.assertEquals;


public class WebCrawlerStep {
	
	private WebCrawlerService webCrawler;
	 
    @Before
    public void setUp() {
    	webCrawler = new WebCrawlerService();
    }
 
 
    @When("^I parse the following html content :$")
    public void i_parse_the_following_html_content(String htmlContent) throws Throwable {
    	webCrawler.parseHtmlContent(htmlContent);
    }

    @Then("^result must should have title \"([^\"]*)\" with date \"([^\"]*)\"$")
    public void result_must_should_have_title_with_date(String path, String lastModified) throws Throwable {
    	
        assertEquals(true, webCrawler.getLinks().contains(new Link(path,lastModified)));
    }

}
