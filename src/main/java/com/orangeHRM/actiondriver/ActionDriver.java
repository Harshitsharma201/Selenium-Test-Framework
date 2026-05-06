package com.orangeHRM.actiondriver;

import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.utilities.ExtentManager;

public class ActionDriver {

	private WebDriver driver;
	private WebDriverWait wait;
	public static final Logger logger=BaseClass.logger;
	public ActionDriver(WebDriver driver) {
		this.driver=driver;
		int explicitWait=Integer.parseInt(BaseClass.getProp().getProperty("explicitWait").trim());
		this.wait= new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
		logger.info("Web Driver instance is created");
	}

	//Click an element
	public void click(By by) {
		String elementDescription=getElementDescription(by);
		try {
			waitForElementToBeClickable(by);
			driver.findElement(by).click(); 
			ExtentManager.logStep("clicked an element"+elementDescription);
			logger.info("Element is clicked " +elementDescription);
//			applyBorder(by,"green");
		}
		catch(Exception e){
			ExtentManager.logFailure(BaseClass.getDriver(),"Unable to click the element", elementDescription);
			logger.error("Unable to click the element "+e.getMessage());
//			applyBorder(by,"red");
		}
	}

	//Method to input text in an input field.
	public void enterText(By by,String value) {

		try {
			waitForElementToBeVisible(by);
			//			driver.findElement(by).clear();
			//			driver.findElement(by).sendKeys(value);

			WebElement element=driver.findElement(by);
			element.clear();
			element.sendKeys(value);
			applyBorder(by,"green");
			logger.info("Text entered on " +getElementDescription(by)+" is "+value);
		}

		catch(Exception e) {
			logger.error("Not able to write inside the element "+e.getMessage());
			applyBorder(by,"red");
		}
	}

	//Method to get data from an element
	public String getText(By by) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by,"green");
			return driver.findElement(by).getText();

		}
		catch(Exception e) {
			logger.error("Unable to get the text of the element "+e.getMessage());
			applyBorder(by,"red");
			return "";

		}
	}

	//Method to compare two text
	//made the changes from void to boolean.

	public boolean compareText(By by, String text) {
		try {
			waitForElementToBeVisible(by);
			String actualText=driver.findElement(by).getText();
			if(text.equals(actualText)) {
				logger.info("Text are matching "+actualText+" is equal to "+text);
				ExtentManager.logStepWithScreenshot(BaseClass.getDriver(),"Text are matching, CompareText test verified Succeddfully ", actualText);
				return true;
			}
			else
				logger.info("Text are not matching "+actualText+" is not equal to "+text);
			ExtentManager.logFailure(BaseClass.getDriver(),"Text are not matching ", actualText);
			return false;
		}
		catch(Exception e){
			logger.error("Unable to compare text "+e.getMessage());
			return false;
		}
	}

	//Method to check if an element is displayed

	//	public boolean isDisplayed(By by) {
	//		try {
	//			waitForElementToBeVisible(by);
	//			boolean isDisplayed=driver.findElement(by).isDisplayed();
	//			if(isDisplayed) {
	//				System.out.println("Item is displayed");
	//				return isDisplayed;
	//			}
	//			else
	//				System.out.println("Item is displayed");
	//			return isDisplayed;
	//		} catch (Exception e) {
	//			// TODO Auto-generated catch block
	//			System.out.println("Element is not displayed "+e.getMessage());
	//			return false;
	//		}
	//	}

	public boolean isDisplayed(By by) {
		try {
			waitForElementToBeVisible(by);
			logger.info("Element is displayed "+getElementDescription(by));
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(),"Element is Displayed","Element is displayed "+getElementDescription(by));
			applyBorder(by,"green");
			return driver.findElement(by).isDisplayed();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Element is not displayed "+e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(),"Element is not displayed", "Element is not displayed "+getElementDescription(by));
			applyBorder(by,"red");
			return false;
		}

	}

	//wait for page to load
	public void waitForPageLoad(int timeOutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(driver->(JavascriptExecutor)driver)
			.executeScript("return document.readyState").equals("complete");
			logger.info("Page loaded Successfully");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("unable to load the page "+e.getMessage());
		}
	}

	//scroll to an element using javascript
	public void scrollToElement(By by) {
		try {
			JavascriptExecutor js=(JavascriptExecutor)driver;
			WebElement element=driver.findElement(by);
			js.executeScript("arguments[0].scrollIntoView(true);",element);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Unable to scroll to the desired element" +e.getMessage());
		}
	}

	//Wait for element to be clickable
	private void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		}
		catch(Exception e) {
			logger.error("Unable to click the element "+e.getMessage());
		}
	}

	//Wait for element to be visible
	private void waitForElementToBeVisible(By by) {

		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		}
		catch(Exception e) {
			logger.error("Element is not visible "+e.getMessage());
		}
	}

	//Method to get the description  of an element

	public String getElementDescription(By locator) {
		//check for null driver or locator to avoid NULLpointer Exception

		if(driver==null) {
			return "driver is null";
		}
		if(locator==null) {
			return "locator is null";
		}

		try {
			WebElement element= driver.findElement(locator);

			//Get Element Attributes
			String name=element.getDomAttribute("name");
			String id=element.getDomAttribute("id");
			String text=element.getText();
			String className=element.getDomAttribute("class");
			String placeHolder=element.getDomAttribute("placeholder");

			//Return the description based on element attributes
			if(isNotEmpty(name)) {
				return "Element with name: "+name;
			}

			if(isNotEmpty(id)) {
				return "Element with id: "+id;
			}

			if(isNotEmpty(text)) {
				return "Element with text: "+ truncate(text,50);
			}

			if(isNotEmpty(className)) {
				return "Element with class name: "+className;
			}
			if(isNotEmpty(placeHolder)) {
				return "Element with placeholder: "+placeHolder;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Unable to locate the element"+e.getMessage());
		}
		return "Unable to find the element";

	}


	//Utility method to check if a string is not null or empty
	private boolean isNotEmpty(String value) {
		return value!=null&&!value.isEmpty();
	}

	//Utility method to truncate long String
	private String truncate(String value,int maxLength) {
		if(value==null || value.length()<=maxLength) {
			return value;
		}
		return value.substring(0,maxLength)+"...";
	}

	//Utility method to border elements
	public void applyBorder(By by,String color) {
		//Locate the element
		try {
			WebElement element=driver.findElement(by);
			//Apply the border
			String script="arguments[0].style.border='3px solid "+color+"'";
			JavascriptExecutor js=(JavascriptExecutor)driver;
			js.executeScript(script, element);
			logger.info("Aplied the border with color "+color+" to element "+getElementDescription(by));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info("Unable to apply the border with color "+color+" to element "+getElementDescription(by));
		}
	}
	
	//========================================Select Methods=======================================

//Method to select a dropdown by visible text
	public void selectByVisibleText(By by,String value) {
		try {
			WebElement element=driver.findElement(by);
			new Select(element).selectByVisibleText(value);
			applyBorder(by,"green");
			logger.info("Selected dropdown value "+value);
		}
		catch(Exception e) {
			applyBorder(by,"red");
			System.out.println("Unable to click drop-down "+e );
		}
	}

	//SelectByIndex
	public void selectByValue(By by,String value) {
		try {
			WebElement element=driver.findElement(by);
			new Select(element).selectByValue(value);
			applyBorder(by,"green");
			logger.info("Selected dropdown value "+value);
		}
		catch(Exception e) {
			applyBorder(by,"red");
			System.out.println("Unable to click drop-down "+e );
		}
	}
	
	//Select By Index
	public void selectByIndex(By by,int index) {
		WebElement element=driver.findElement(by);
		new Select(element).selectByIndex(index);
		applyBorder(by,"green");
		logger.info("Selected the element with index "+index);
	}

	//Method to click using Javascript
	public void clickUsingJS(By by) {
		WebElement element=driver.findElement(by);
		JavascriptExecutor js=(JavascriptExecutor)driver;
		js.executeScript("arguments[0].click;", element);
		applyBorder(by,"green");
		logger.info("Clicked the element using JS "+element);
	}
	
	public void scrollToBottom() {
	    try {
	        JavascriptExecutor js = (JavascriptExecutor) driver;
	        // window.scrollTo(x-coord, y-coord)
	        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
	        ExtentManager.logStep("Scrolled to the bottom of the page");
	    } catch (Exception e) {
	        ExtentManager.logStepValidationForAPI("Failed to scroll: " + e.getMessage());
	    }
	}





}
