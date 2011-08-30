package org.obm.testing.pages.calendar;

import java.util.Calendar;
import java.text.SimpleDateFormat;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import org.obm.testing.support.Page;

public class NewEventPage extends Page {
	
	public static final String PATH = "/calendar/calendar_index.php?action=new";
	
	@FindBy(how = How.ID, using = "tf_title")
	public WebElement titleField;
	
	@FindBy(how = How.ID, using = "userSearch")
	public WebElement userAutocomplete;
	
	@FindBy(how = How.CLASS_NAME, using = "otherFile")
	public WebElement uploadDocumentField;
	
	@FindBy(how = How.ID, using = "cba_allow_documents")
	public WebElement allowAttachmentsCheckbox;
	
	@FindBy(how = How.ID, using = "insertBtn")
	public WebElement submitBtn;
	
	protected SimpleDateFormat dateFormat;
	
	public NewEventPage(WebDriver driver) {
        super(driver);
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    }
	
	public void setTitle(String title) {
		enter(title, into(titleField));
	}
	
	public void setBeginDate(Calendar cal) {
		String date = dateFormat.format(cal.getTime());
		String hour = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
		String min = Integer.toString(cal.get(Calendar.MINUTE));
		enter(date, into(field("tf_date_begin")));
		select(hour, in(field("sel_time_begin")));
		select(min, in(field("sel_min_begin")));
	}
	
	public void setEndDate(Calendar cal) {
		String date = dateFormat.format(cal.getTime());
		String hour = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
		String min = Integer.toString(cal.get(Calendar.MINUTE));
		enter(date, into(field("tf_date_end")));
		select(hour, in(field("sel_time_end")));
		select(min, in(field("sel_min_end")));
	}
	
	public void addAttendee(String username) {
		addEntityWithAutocomplete(username, userAutocomplete);
	}
	
	public void attachDocument(String filepath) {
		uploadDocumentField.sendKeys(filepath);
	}
	
	public void allowAttachments() {
		allowAttachmentsCheckbox.setSelected();
	}
	
	public Page submit() {
		clickOn(submitBtn);
		return currentPage();
	}
}