package org.obm.testing.pages.calendar;

import org.obm.testing.support.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class RightsManagementPage extends Page {
	
	@FindBy(how = How.ID, using = "userSearch")
	public WebElement userAutocomplete;
	
	public RightsManagementPage(WebDriver driver) {
        super(driver);
    }
	
	public void addAccessRightsOnUser(String username) {
		addEntityWithAutocomplete(username, userAutocomplete);
		findCorrespondingCell(username);
		clickOn(button("Valider les permissions"));
	}
	
	protected void findCorrespondingCell(String username) {
		for (WebElement td : driver.findElements(By.xpath("//td"))) {
            if (td.getText().equals(username)) {
                WebElement chkbox = td.findElement(By.xpath("../td[3]/input[@type='checkbox']"));
                chkbox.setSelected();
            }
        }
	}
}
