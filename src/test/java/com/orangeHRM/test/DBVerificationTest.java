package com.orangeHRM.test;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.pages.HomePage;
import com.orangeHRM.pages.LoginPage;
import com.orangeHRM.utilities.DBConnection;
import com.orangeHRM.utilities.DataProviders;
import com.orangeHRM.utilities.ExtentManager;

public class DBVerificationTest extends BaseClass{
	
	
	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod
	public void setupPages() {
		loginPage=new LoginPage(getDriver());
		homePage=new HomePage(getDriver());
	}
	
	@Test(dataProvider="emplVerification", dataProviderClass=DataProviders.class)
public void verifyEmployeeNameVerificationFromDB(String emplID, String empName){
		
		SoftAssert softAssert=getSoftAssert();
	ExtentManager.logStep("Logging with admin credentials");
	loginPage.logIn(prop.getProperty("username"),prop.getProperty("password"));
	
	ExtentManager.logStep("click on PIM Tab");
	homePage.clickOnPIMTab();
	
	ExtentManager.logStep("Search For Employee");
	homePage.employeeSearch(empName);
	
	ExtentManager.logStep("Get the Employee Name from DB");
	String employee_id=emplID;
	
	//Fetch data into Map
	Map<String,String>employeeDetails=DBConnection.getEmployeeDetails(employee_id);
	
	String emplFirstName=employeeDetails.get("firstName");
	String emplMiddleName=employeeDetails.get("middleName");
	String emplLastName=employeeDetails.get("lastName");

	String emplFirstAndMiddleName=(emplFirstName+" "+emplMiddleName).trim();
	
	softAssert.assertTrue(homePage.verifyEmployeeFirstandMiddleName(emplFirstAndMiddleName),"First and middle name are not matching");
	
	ExtentManager.logStep("Verify the employee with last name");
	softAssert.assertTrue(homePage.verifyEmployeeLastName(emplLastName),"Employee last name is not matching");
	
	ExtentManager.logStep("db verification completed");
	
	softAssert.assertAll();
}
}
