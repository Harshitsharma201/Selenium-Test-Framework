package com.orangeHRM.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.pages.HomePage;
import com.orangeHRM.pages.LoginPage;
import com.orangeHRM.utilities.DataProviders;
import com.orangeHRM.utilities.ExtentManager;

public class LoginPageTest extends BaseClass {
	
	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod
	public void setupPages() {
		loginPage=new LoginPage(getDriver());
		homePage=new HomePage(getDriver());
	}
	
	@Test(dataProvider="validLoginData", dataProviderClass=DataProviders.class)
	public void VerifyValidLoginTest(String username, String password) {
		
//		ExtentManager.startTest("Valid Login Test");
		System.out.println("Test Started on thread "+Thread.currentThread().getId());
		ExtentManager.logStep("Navigating to Login Page using credentials");
		loginPage.logIn(username, password);
		ExtentManager.logStep("Verifying admin tab is visible or not");
		Assert.assertTrue(homePage.isAdminTabVisible(),"Admin Tab should be visible to ");
		ExtentManager.logStep("Validation Successfull");
		homePage.logout();
		ExtentManager.logStep("Logged out Successfully");
		staticWait(2);
	}
	
	@Test(dataProvider="invalidLoginData", dataProviderClass=DataProviders.class)
	public void invalidLoginTest(String username,String password) {
//		ExtentManager.startTest("InValid Login Test");
		ExtentManager.logStep("Navigating to Login Page using credentials");
		loginPage.logIn(username,"pass");
		String expectedErrorMessage="Invalid credentials";
		Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage),"Test Failed: Invalid Error Message");
		ExtentManager.logStep("Validation Successfull");
	}
}
