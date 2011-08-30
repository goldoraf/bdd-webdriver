package org.obm.testing.support;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.junit.internal.matchers.TypeSafeMatcher;
import org.dbunit.ObmDatabaseTester;
import org.hamcrest.Matcher;
import org.hamcrest.Description;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;

import static org.hamcrest.core.IsNot.not;
import static org.jbehave.Ensure.ensureThat;

import org.obm.testing.pages.common.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@SuppressWarnings("serial")
public class Steps extends org.jbehave.scenario.steps.Steps {
	
	protected static final Map<String, String> USERS = new HashMap<String, String>() {{
		put("user1", "Doe John");
		put("user2", "Doe Jane");
		put("user3", "Bauer Jack");
	}};
	
	protected WebDriver driver;
	protected Navigation nav;
	protected ObmDatabaseTester db;
	
	@BeforeScenario
    public void setupDriver() {
		driver = Runner.getDriver();
		nav = new Navigation(driver);
    }
    
    @BeforeScenario
    public void setupDb() throws Exception {
    	db = new ObmDatabaseTester();
    	db.onSetup();
    }

    @AfterScenario
    public void closeWebdriver() {
        driver.close();
    }
    
    @AfterScenario
    public void tearDownDb() throws Exception {
    	db.onTearDown();
    }
    
    @Given("I log in as $login")
    public void loginAs(String login) {
    	LoginPage loginPage = nav.goToLoginPage();
    	loginPage.loginAs(login, "password");
    }
    
    @Given("I log out")
    public void logout() {
		currentPage().link("Déconnexion").click();
	}
    
    @Then("the text \"$text\" should be visible")
    public void textIsVisible(String text) {
		ensureThat(currentPage().body(), containsText(text));
    }
    
    @Then("the text \"$text\" should not be visible")
    public void textIsNotVisible(String text) {
		ensureThat(currentPage().body(), not(containsText(text)));
    }
    
    @Then("I should see a notice \"$text\"")
	public void noticeVisible(String text) {
		ensureThat(currentPage().message(), containsText(text));
	}
    
    protected Page currentPage() {
    	return new Page(driver);
    }
    
    protected String getUserFullname(String userLogin) {
    	return USERS.get(userLogin);
    }

    //// Matchers ////

    protected Matcher<WebElement> containsText(final String expectedText) {
    	return new TypeSafeMatcher<WebElement>() {
            public boolean matchesSafely(WebElement element) {
            	return element.getText().contains(expectedText);
            }

            public void describeTo(Description description) {
                description.appendText("WebElement containing \"" + expectedText + "\"");
            }
        };
    }
    
    protected Matcher<Page> is(final Page expectedPage) {
    	return new TypeSafeMatcher<Page>() {
            public boolean matchesSafely(Page page) {
            	return page.getTitle().equals(expectedPage.getTitle());
            }

            public void describeTo(Description description) {
                description.appendText("Page of " + expectedPage.getClass());
            }
        };
    }
    
    protected Matcher<Page> isLandingPage() {
    	return new TypeSafeMatcher<Page>() {
            public boolean matchesSafely(Page page) {
            	return page.getTitle().contains("OBM version");
            }

            public void describeTo(Description description) {
                description.appendText("Landing page");
            }
        };
    }
    
    protected Matcher<Page> isLoginPage() {
    	return new TypeSafeMatcher<Page>() {
            public boolean matchesSafely(Page page) {
            	return isElementPresent("//input[@name='login']");
            }

            public void describeTo(Description description) {
                description.appendText("Login page");
            }
        };
    }
    
    protected boolean isElementPresent(String xpath) {
        try {
            driver.findElement(By.xpath(xpath));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    
    protected Calendar getDate(String day, Integer hour, Integer min) {
		Calendar cal = new GregorianCalendar();
		if (day.equals("tomorrow")) {
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, min);
		return cal;
	}
}