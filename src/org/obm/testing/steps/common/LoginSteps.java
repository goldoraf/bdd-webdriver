package org.obm.testing.steps.common;

import static org.jbehave.Ensure.ensureThat;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.annotations.Then;

import org.obm.testing.pages.common.LoginPage;
import org.obm.testing.support.Steps;

public class LoginSteps extends Steps {
	
	@Given("I am on the login page")
	public void goToLoginPage() {
		nav.goToLoginPage();
	}
    
    @When("I log in with login $userName and password $password")
	public void logIn(String userName, String password) {
    	LoginPage loginPage = new LoginPage(driver);
		loginPage.loginAs(userName, password);
    }
    
    @Then("I should see the landing page")
    public void landingPageVisible() {
    	ensureThat(currentPage(), isLandingPage());
    }
    
    @Then("I should see the login page")
    public void loginPageVisible() {
    	ensureThat(currentPage(), isLoginPage());
    }
    
    @Then("I should be informed that my credentials are invalid")
    public void verifyInvalidCredentialsMessage() {
    	LoginPage loginPage = new LoginPage(driver);
    	ensureThat(loginPage.displaysErrorMessage());
    }
}
