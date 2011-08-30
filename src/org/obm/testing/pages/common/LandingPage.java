package org.obm.testing.pages.common;

import org.obm.testing.support.Page;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class LandingPage extends Page {
	
	public static final String PATH = "/obm.php";
	
	@FindBy(how = How.LINK_TEXT, using = "Mon agenda")
	public WebElement myCalendarLink;
	
	public LandingPage(WebDriver driver) {
        super(driver);
    }
}
