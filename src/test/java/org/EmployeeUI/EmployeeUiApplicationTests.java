package org.EmployeeUI;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/main/resources/feature/", plugin="pretty")
public class EmployeeUiApplicationTests {

	@Test
	public void contextLoads() {

	}

}
