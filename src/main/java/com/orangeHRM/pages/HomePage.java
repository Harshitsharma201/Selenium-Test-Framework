package com.orangeHRM.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangeHRM.actiondriver.ActionDriver;
import com.orangeHRM.base.BaseClass;

public class HomePage {
	private ActionDriver actionDriver;

//	public HomePage(WebDriver driver) {
//		this.actionDriver=new ActionDriver (driver);
//	}
//	
	public HomePage(WebDriver driver) {
		this.actionDriver=BaseClass.getAction();
	}
	//Define Locators using By class
	
	private By adminTab= By.xpath("//span[text()='Admin']");
	private By userIDButton=By.className("oxd-userdropdown-img");
	private By logoutButton=By.xpath("//a[text()='Logout']");
	private By OrangeHRMlogo=By.xpath("//div[@class='oxd-brand-banner']//img");
	
	private By pimTab=By.xpath("//span[text()='PIM']");
	private By employeeSearch=By.xpath("//label[text()='Employee Name']/parent::div/following-sibling::div/div/div/input");
	private By searchButton=By.xpath("//button[@type='submit']");
	private By emplFirstAndMiddleName=By.xpath("//div[@class='oxd-table-card']/div/div[3]");
	private By emplLastName=By.xpath("//div[@class='oxd-table-card']/div/div[4]");
	
	//Method to verify if admin tab is visible
	public boolean isAdminTabVisible() {
		return actionDriver.isDisplayed(adminTab);
		
	}
	//Method to navigate to PIM Tab
	public void clickOnPIMTab() {
		actionDriver.click(pimTab);
	}
	
	public void employeeSearch(String value) {
		actionDriver.enterText(employeeSearch, value);
		actionDriver.click(searchButton);
		actionDriver.scrollToElement(emplFirstAndMiddleName);
	}
	//Method to verify orangeHRM Logo
	public boolean verifyOrangeHRMlogo() {
		return actionDriver.isDisplayed(OrangeHRMlogo);
	}
	
	//verify employee first and middlename
	public boolean verifyEmployeeFirstandMiddleName(String emplFirstAndMiddleFromDB) {
		return actionDriver.compareText(emplFirstAndMiddleName, emplFirstAndMiddleFromDB);
	}
	
	public boolean verifyEmployeeLastName(String emplLastFromDB) {
		return actionDriver.compareText(emplLastName, emplLastFromDB);
	}
	
	//Method to perform logout operation
	public void logout() {
		actionDriver.click(userIDButton);
		actionDriver.click(logoutButton);
	}
}
