package org.obm.testing.support;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.RenderedWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementHandler;
import org.openqa.selenium.support.ui.Select;

public class Page {
	
	protected final WebDriver driver;
	
	public Page(WebDriver driver) {
        this.driver = driver;
        initElements(this);
    }
	
	public String getTitle() {
		return element("//h1[@class='title']").getText();
	}
	
	public WebElement body() {
    	return element("//body");
    }
	
	public WebElement content() {
    	return element("//div[@id='mainContent']");
    }
	
	public WebElement message() {
    	return element("//p[@class='message ok']");
    }
	
	public WebElement info() {
    	return element("//p[@class='message info']");
    }
    
    public WebElement warning() {
    	return element("//p[@class='message warning']");
    }
    
    public WebElement userinfo() {
    	return element("//ul[@id='information']");
    }
	
	protected WebElement into(WebElement inputField) {
        return inputField;
    }
	
	protected WebElement in(WebElement inputField) {
        return inputField;
    }
	
	protected void enter(String value, WebElement inputField) {
		inputField.clear();
		inputField.sendKeys(value);
    }
	
	protected void select(String value, WebElement inputField) {
        Select select = new Select(inputField);
        select.selectByVisibleText(value);
    }

    protected void clickOn(WebElement webElement) {
        webElement.click();
    }
    
    protected WebElement element(String xpathExpression) {
    	return driver.findElement(By.xpath(xpathExpression));
    }
    
    protected WebElement field(String name) {
        return driver.findElement(By.id(name));
    }
    
    protected WebElement link(String linkText) {
        return driver.findElement(By.linkText(linkText));
    }
    
    protected WebElement button(String label) {
        try {
            return driver.findElement(By.xpath("//input[@type = 'submit' and @value = '" + label + "']"));
        } catch (NoSuchElementException e) {
            for (WebElement button : driver.findElements(By.xpath("//button"))) {
                if (button.getText().equals(label)) {
                    return button;
                }
            }
            throw e;
        }
    }
    
    protected Page currentPage() {
    	return new Page(driver);
    }
    
    protected void addEntityWithAutocomplete(String entityName, WebElement autocomplete) {
    	enter(entityName.substring(0, 3), into(autocomplete));
    	selectEntityInAutocompleteResultBox(entityName);
    }
    
    protected void selectEntityInAutocompleteResultBox(String entityName) {
		try {
			for (WebElement div : driver.findElements(By.xpath("//div[@class='autoCompleteResultBox']"))) {
				for (WebElement span : div.findElements(By.xpath("//span"))) {
		            if (span.getText().equals(entityName)) {
		                span.click();
		                return;
		            }
				}
	        }
			throw new NoSuchElementException("Entity "+entityName+" not found in autocomplete results");
		} catch (StaleElementReferenceException e) {
			
		}
	}
	
    protected void initElements(Object page) {
    	Class<?> proxyIn = page.getClass();
    	ElementLocatorFactory factory = new DefaultElementLocatorFactory(driver);
    	while (proxyIn != Object.class) {
	        proxyFields(factory, page, proxyIn);
	        proxyIn = proxyIn.getSuperclass();
	    }
    }
    
    protected void proxyFields(ElementLocatorFactory factory, Object page, Class<?> proxyIn) {
        Field[] fields = proxyIn.getDeclaredFields();
        for (Field field : fields) {
            if (!WebElement.class.isAssignableFrom(field.getType()))
              continue;

            field.setAccessible(true);
            proxyElement(factory, page, field);
        }
    }

    protected void proxyElement(ElementLocatorFactory factory, Object page, Field field) {
    	ElementLocator locator = factory.createLocator(field);
		if (locator == null) {
			return;
		}
		InvocationHandler handler = new LocatingElementHandler(locator);
		WebElement proxy;
		if (field.getType().equals(RenderedWebElement.class)){
			proxy = (RenderedWebElement) Proxy.newProxyInstance(
					page.getClass().getClassLoader(), new Class[]{RenderedWebElement.class}, handler);
		} else {
			proxy = (WebElement) Proxy.newProxyInstance(
					page.getClass().getClassLoader(), new Class[]{WebElement.class}, handler);
		}
		try {
			field.set(page, proxy);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
    }
}
