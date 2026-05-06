package com.orangeHRM.test;

import org.testng.annotations.Test;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.utilities.ExtentManager;


public class DummyClass2 extends BaseClass{
	
	@Test
	public void dummyTest() {
//		ExtentManager.startTest("Dummy class Test1");	
	String title= getDriver().getTitle();
	ExtentManager.logStep("Verifying the title");
	assert title.equals("OrangeHRM"):"Test Failed: Title is not matching";
	ExtentManager.logStep("Validation Successfull: title is matching");
	
	System.out.print("Test Case is passed: Title is matching");
}
}
