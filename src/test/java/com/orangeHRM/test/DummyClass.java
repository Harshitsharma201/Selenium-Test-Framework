package com.orangeHRM.test;

import org.testng.SkipException;
import org.testng.annotations.Test;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.utilities.ExtentManager;


public class DummyClass extends BaseClass{
	
	@Test
	public void dummyTest() {
//		ExtentManager.startTest("Dummy class Test1");-- Already written in testng listner class
	String title= getDriver().getTitle();
	
	assert title.equals("OrangeHRM"):"Test Failed: Title is not matching";
	ExtentManager.logStep("Validation Successfull: title is matching");
	System.out.print("Test Case is passed: Title is matching");
	
	throw new SkipException("Skipping the test");
}
}
