package com.orangeHRM.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ExtentManager {

	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> test =new ThreadLocal<>();
	private static Map<Long,WebDriver>driverMap=new HashMap<>();

	//initialise the Extent Report

	public synchronized static ExtentReports getReporter() {
		if(extent==null) {
			String reportPath=System.getProperty("user.dir")+"/src/test/resources/ExtentReport/ExtentReport.html";
			ExtentSparkReporter spark=new ExtentSparkReporter(reportPath);
			spark.config().setReportName("Automation Test Report");
			spark.config().setDocumentTitle("OrangeHRM Report");
			spark.config().setTheme(Theme.DARK);

			extent=new ExtentReports();
			extent.attachReporter(spark);
			//Adding System information
			extent.setSystemInfo("Operating System", System.getProperty("os.name"));
			extent.setSystemInfo("Java Version", System.getProperty("java.version"));
			extent.setSystemInfo("User Name", System.getProperty("user.name"));
		}
		return extent;
	}

	//start the test
	public synchronized static ExtentTest startTest(String testName) {
		// 1. Ensure the reporter is initialized FIRST
	    if (extent == null) {
	        getReporter();
	    }
	    
	    // 2. Now create the test from the guaranteed 'extent' object
	    ExtentTest extentTest = extent.createTest(testName);
	    
	    // 3. Set it to the ThreadLocal
	    test.set(extentTest);
	    
	    return extentTest;
	}

	//End a test
	public synchronized static void endTest() { 
		getReporter().flush();
	}

	//Get Current Thread's test
	public synchronized static ExtentTest getTest() {
		return test.get();
	}

	//Method to get the name of the current test
	public static String getTestName() {
		ExtentTest currentTest=getTest();
		if(currentTest!=null) {
			return currentTest.getModel().getName();
		}
		else
			return "No test is currently active for this thread";
	}

	//Log a step
	public static void logStep(String logMessage) {
		getTest().info(logMessage);
	}

	//Log a step validation with screenshot
	public static void logStepWithScreenshot(WebDriver driver, String logMessage, String screenShotMessage) {
		getTest().pass(logMessage);
		
		//ScreenshotMethod
		attachScreenshot(driver,screenShotMessage);
	}
	
	//Log a step validation with screenshot for API
		public static void logStepValidationForAPI(String logMessage) {
			getTest().pass(logMessage);
		}

	//Log a failure
	public static void logFailure(WebDriver driver, String logMessage, String screenShotMessage) {
		String colorMessage =String.format("<span style='color:orange;'>%s</span>", logMessage);
		getTest().fail(colorMessage);
		attachScreenshot(driver,screenShotMessage);
	}
	
	//Log a failure for API
		public static void logFailureAPI(String logMessage) {
			String colorMessage =String.format("<span style='color:orange;'>%s</span>", logMessage);
			getTest().fail(colorMessage);
		}

	//log a skip
	public static void logSkip(String logMessage) {
		String colorMessage =String.format("<span style='color:orange;'>%s</span>", logMessage);
		getTest().skip(colorMessage);
	}

	//Take a screenshot with date and time in the file
	public synchronized static String takeScreenShot(WebDriver driver,String screenshotName) {
		TakesScreenshot ts=(TakesScreenshot)driver;
		File src=ts.getScreenshotAs(OutputType.FILE);
		String timeStamp= new SimpleDateFormat("yyyy-mm-dd_HH-mm-ss").format(new Date());

		//Saving the screenshot 
		String destPath=System.getProperty("user.dir")+"/src/test/resources/ExtentReport/screeenshots.html "+screenshotName+"_"+timeStamp+".png";
		File finalPath=new File(destPath);
		try {
			FileUtils.copyFile(src, finalPath);

		}
		catch(IOException e) {
			e.printStackTrace();
		}
		//convert screenshot to BASE64 for embedding in the report
		String base64Format=convertToBase64(src);
		return base64Format;
	}

	//convert screenshot to BASE64 format
	public static String convertToBase64(File screenShotFile) {
		String base64Format="";
		//Read the file content into a byte array
		byte[] fileContent;
		try {
			fileContent = FileUtils.readFileToByteArray(screenShotFile);
			return base64Format=Base64.getEncoder().encodeToString(fileContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Convert the byte array to Base64 String
		return base64Format;
	}
	
	//Attach Screenshot to report using Base64
	public synchronized static void attachScreenshot(WebDriver driver, String message) {
		try {
			String screenShotBase64=takeScreenShot(driver,getTestName());
			getTest().info(message,com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String(screenShotBase64).build());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			getTest().fail("Failed to capture screnshot "+message);
			e.printStackTrace();
		}
	}
	//Register WebDriver for current thread
	public static void registerDriver(WebDriver driver) {
		driverMap.put(Thread.currentThread().getId(), driver);
	}
}
