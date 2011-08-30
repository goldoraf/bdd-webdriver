package org.obm.testing.support;

import org.openqa.selenium.WebDriver;

import org.obm.testing.pages.calendar.*;
import org.obm.testing.pages.common.*;

public class Navigation {
	
	public static String HOST = "";
	
	protected final WebDriver driver;
	
	public Navigation(WebDriver driver) {
        this.driver = driver;
    }
	
	public LoginPage goToLoginPage() {
		goTo("/");
		return new LoginPage(driver);
	}
	
	public LandingPage goHome() {
		goTo("/obm.php");
		return new LandingPage(driver);
	}
    
	public CalendarPage goToMyCalendar() {
		LandingPage landingPage = goHome();
		landingPage.myCalendarLink.click();
		return new CalendarPage(driver);
	}
	
	public NewEventPage goToNewEventPage() {
		CalendarPage calendarPage = goToMyCalendar();
		calendarPage.newAppointmentLink.click();
		return new NewEventPage(driver);
	}
	
	public RightsManagementPage goToCalendarRightsPage() {
		CalendarPage calendarPage = goToMyCalendar();
		calendarPage.rightsManagementLink.click();
		return new RightsManagementPage(driver);
	}
	
	private void goTo(String path) {
		driver.get(HOST + path);
	}
}
