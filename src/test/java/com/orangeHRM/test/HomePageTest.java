package com.orangeHRM.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.pages.HomePage;
import com.orangeHRM.pages.LoginPage;
import com.orangeHRM.utilities.DataProviders;
import com.orangeHRM.utilities.ExtentManager;

public class HomePageTest extends BaseClass{

	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod
	public void setupPages() {
		loginPage=new LoginPage(getDriver());
		homePage=new HomePage(getDriver());
	}
	
	@Test(dataProvider="validLoginData", dataProviderClass=DataProviders.class)
	public void verifyOrangeHRMLogo(String username,String password) {
//		ExtentManager.startTest("Home Page Logo Test");
		ExtentManager.logStep("Navigating to Login Page entering username and password");
		loginPage.logIn(username,password);
		ExtentManager.logStep("Verify logo is visible or not");
		Assert.assertTrue(homePage.verifyOrangeHRMlogo(),"Logo is not visible");
		ExtentManager.logStep("Validation Successfull");
		ExtentManager.logStep("Logged out successfully");
	}
}
