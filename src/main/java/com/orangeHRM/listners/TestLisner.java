package com.orangeHRM.listners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.IConfigurationAnnotation;
import org.testng.annotations.ITestAnnotation;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.utilities.ExtentManager;
import com.orangeHRM.utilities.RetryAnalyzer;

public class TestLisner implements ITestListener,IAnnotationTransformer{
	
	
	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor,
			Method testMethod) {
		annotation.setRetryAnalyzer(RetryAnalyzer.class);
	}

	@Override
	public void onTestStart(ITestResult result) {
		String testName=result.getMethod().getMethodName();
		//Start logging in Extent Reports
		ExtentManager.startTest(testName);
		ExtentManager.logStep("Test case started: "+testName);
	}
	
	@Override
	public void onTestSuccess(ITestResult result) {
		String testName=result.getMethod().getMethodName();
		String className = result.getTestClass().getName().toLowerCase();
		if(!className.contains("api")){
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(),"Test passed Successfully: ", testName);
		}
		else {
			ExtentManager.logStepValidationForAPI("Test case failed"+ testName);
		}
	}
	
	//Triggered when a test fails
	@Override
	public void onTestFailure(ITestResult result) {
		String testName=result.getMethod().getMethodName();
		String failureMessage=result.getThrowable().getMessage();
		ExtentManager.logStep(failureMessage);
		if(!result.getClass().getName().toLowerCase().contains("api")){
			
			ExtentManager.logFailure(BaseClass.getDriver(),"Test Failed! ", testName);
		}
		else {
			ExtentManager.logFailureAPI("Test case failed with failure message"+failureMessage);
		}
	
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		String testName=result.getMethod().getMethodName();
		ExtentManager.logSkip("test Skipped: "+testName);
	}

	

//used in api validation above
//	//Triggers on test success....
//	@Override
//	public void onTestSuccess(ITestResult result) {
//		String testName=result.getMethod().getMethodName();
//		ExtentManager.logStepWithScreenshot(BaseClass.getDriver(),"Test passed Successfully: ", testName);
//	}
	
	
	
	@Override
	public synchronized void onStart(ITestContext context) {
		ExtentManager.getReporter();
	}
	@Override
	public void onFinish(ITestContext context) {
		ExtentManager.endTest();
	}
}
