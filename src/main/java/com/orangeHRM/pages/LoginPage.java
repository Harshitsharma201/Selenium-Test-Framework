package com.orangeHRM.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangeHRM.actiondriver.ActionDriver;
import com.orangeHRM.base.BaseClass;

public class LoginPage {
	private ActionDriver actionDriver;
	
//	public LoginPage(WebDriver driver) {
//		this.actionDriver=new ActionDriver(driver);
//	}
	
	public LoginPage(WebDriver driver) {
		this.actionDriver=BaseClass.getAction();
	}
	//Define locators by using By class
	private By userNameField=By.name("username");
	private By passwordField=By.cssSelector("input[type='password']");
	private By loginButton= By.xpath("//button[text()=' Login ']");
	private By errorMessage=By.xpath("//p[text()='Invalid credentials']");
	
	//Method to perform action
	
	public void logIn(String userName, String password){
		actionDriver.enterText(userNameField,userName);
		actionDriver.enterText(passwordField,password);
		actionDriver.click(loginButton);
		
	}
	
	//method to check if error message is displayed
	public boolean isErrorMessageDisplayed() {
		return actionDriver.isDisplayed(errorMessage);
	}
	
	//method to read the text of error message.
	public String getErrorMessageText(){
		return actionDriver.getText(errorMessage);
	}
	

	public boolean verifyErrorMessage(String expectedError) {
		// TODO Auto-generated method stub
		return actionDriver.compareText(errorMessage, expectedError);
	
	}
	
	
}
