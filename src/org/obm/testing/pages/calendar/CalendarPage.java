package org.obm.testing.pages.calendar;

import java.util.List;

import org.obm.testing.support.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class CalendarPage extends Page {
	
	@FindBy(how = How.LINK_TEXT, using = "Nouvel Evt")
	public WebElement newAppointmentLink;
	
	@FindBy(how = How.LINK_TEXT, using = "Gestion des droits")
	public WebElement rightsManagementLink;
	
	@FindBy(how = How.ID, using = "user")
	public WebElement userAutocomplete;
	
	@FindBy(how = How.XPATH, using = "//input[@type='submit' and @value='Valider']")
	public WebElement submitBtn;
	
	@FindBy(how = How.ID, using = "calendarGrid")
	public WebElement grid;
	
	public CalendarPage(WebDriver driver) {
        super(driver);
    }
	
	public String getTitle() {
		return "Agenda : Calendrier sur une semaine";
	}
	
	public WebElement grid() {
		return grid;
	}
	
	public ConsultEventPage consultEvent(String eventTitle) {
		clickOn(eventLink(eventTitle));
		return new ConsultEventPage(driver);
	}
	
	public CalendarPage displayAgenda(String userName) {
		addEntityWithAutocomplete(userName, userAutocomplete);
		clickOn(submitBtn);
		return this;
	}
	
	public boolean displaysSuccessfulInsertionMessage() {
    	return message().getText().contains("Evénement : Insertion réussie");
    }
	
	protected WebElement eventLink(String eventTitle) {
		List<WebElement> events = driver.findElements(By.xpath("//div[@title]"));
		for (WebElement event : events) {
			if (event.getAttribute("title").contains(eventTitle)) {
				return event.findElement(By.xpath("dl/dt/h1/a"));
			}
		}
		throw new NoSuchElementException("Event '"+eventTitle+"'");
	}
}
