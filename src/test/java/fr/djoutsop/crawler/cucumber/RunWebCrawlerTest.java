package fr.djoutsop.crawler.cucumber;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
		monochrome = true,
		plugin = {"pretty", "html:target/cucumber"},
        features = "classpath:cucumber/webcrawler.feature"
)
public class RunWebCrawlerTest {
	
	
}
