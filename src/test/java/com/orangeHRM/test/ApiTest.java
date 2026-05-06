package com.orangeHRM.test;

import org.testng.annotations.Test;


import org.testng.asserts.SoftAssert;

import com.orangeHRM.utilities.ApiUtility;
import com.orangeHRM.utilities.ExtentManager;
import com.orangeHRM.utilities.RetryAnalyzer;

import io.restassured.response.Response;


public class ApiTest {

	@Test
	public void verifyGetUserAPI() {
		//sTEP1 :Define API Endpoint
		SoftAssert softAssert= new SoftAssert();
		String enpoint="https://jsonplaceholder.typicode.com/users/1";
		ExtentManager.logStep("API Endpoint"+enpoint);
		
		
		//Step 2:- Send Get Request
		ExtentManager.logStep("Sending GET Request to the API");
		Response response=ApiUtility.sendGetRequest(enpoint);
		
		//Step 3:validate status code
		boolean isStatusCodeValid;
		
			ExtentManager.logStep("Validating API Response status code");
			isStatusCodeValid = ApiUtility.validateStatusCode(response,200);
			softAssert.assertTrue(isStatusCodeValid,"Status code is not valid");
			
			if(isStatusCodeValid) {
				ExtentManager.logStepValidationForAPI("Status code Validation Passed");
				
			}
			else
				ExtentManager.logFailureAPI("Status code validation failed");
		 
		
	
		
		//step 4: Validate userName
		ExtentManager.logStep("Validating response body for username");
		String userName=ApiUtility.getJsonValue(response,"username");
		boolean isUserNameValid="Bret".equals(userName);
		softAssert.assertTrue(isUserNameValid,"User Name is not valid");
		if(isUserNameValid) {
			ExtentManager.logStepValidationForAPI("UserName validation is passed");
		}
		else {
			ExtentManager.logFailureAPI("User Name validation failed");
		}
		
	
		//step 4: Validate email
		
		ExtentManager.logStep("Validating response body for email");
		String userEmail=ApiUtility.getJsonValue(response,"email");
		boolean isEmailValid="Sincere@april.biz".equals(userEmail);
		softAssert.assertTrue(isEmailValid,"email Name is not valid");
		if(isEmailValid) {
			ExtentManager.logStepValidationForAPI("email Name validation is passed");
		}
		else {
			ExtentManager.logFailureAPI("email validation failed");
		}
	
	softAssert.assertAll();
}
}
