package org.obm.testing.pages.calendar;

import org.obm.testing.support.Page;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class WaitingEventsPage extends Page {
	
	public static final String PATH = "/calendar/calendar_index.php?action=waiting_events";
	
	@FindBy(how = How.CLASS_NAME, using = "spreadSheet")
	public WebElement table;
	
	public WaitingEventsPage(WebDriver driver) {
        super(driver);
    }
	
	public String getTitle() {
		return "Agenda - Evts en attente";
	}
	
	public boolean containsEvent(String eventTitle) {
		return table.getText().contains(eventTitle);
	}
	
	public boolean displaysCountEqualTo(Integer count) {
		return info().getText().contains("Rendez-vous en attente : " + count.toString());
	}
}
