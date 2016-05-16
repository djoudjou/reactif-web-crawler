package fr.djoutsop;

import org.junit.Ignore;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@Ignore
@RunWith(Cucumber.class)
@CucumberOptions(
		monochrome = true,
		plugin = {"pretty", "html:target/cucumber"},
        features = "classpath:cucumber/calculator.feature"
)
public class RunCalculatorTest {
	
	
}
