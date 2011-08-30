package org.obm.testing.pages.calendar;

import org.obm.testing.support.Page;
import org.openqa.selenium.WebDriver;

public class ConsultEventPage extends Page {
	public ConsultEventPage(WebDriver driver) {
        super(driver);
    }
	
	public ConsultEventPage attachDocument(String filepath) {
		element("//input[@class='otherFile']").sendKeys(filepath);
		element("//input[@type='submit' and @value='Attacher']").click();
		return this;
	}
	
	public boolean displaysDocumentAttachmentZone() {
		return content().getText().contains("Ajouter des documents");
	}
}
