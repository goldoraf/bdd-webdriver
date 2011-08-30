package org.obm.testing.pages.common;

import org.obm.testing.support.Page;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class LoginPage extends Page {
	
	@FindBy(how = How.NAME, using = "loginform")
	private WebElement form;
	
	private WebElement login;
	private WebElement password;
	
	public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void loginAs(String loginStr, String passwordStr) {
        enter(loginStr, into(login));
        enter(passwordStr, into(password));
        select("Linagora (linagora.com)", in(field("sel_domain_id")));
        form.submit();
    }
    
    public boolean displaysErrorMessage() {
    	return body().getText().contains("Login ou mot de passe invalide.");
    }
}
