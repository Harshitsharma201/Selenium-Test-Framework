package com.orangeHRM.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.util.Assert;
import com.orangeHRM.actiondriver.ActionDriver;
import com.orangeHRM.utilities.ExtentManager;
import com.orangeHRM.utilities.LoggerManager;

//import com.orangeHRM.actiondriver.ActionDriver;

public class BaseClass {
	protected static Properties prop;
//	protected static WebDriver driver;
//	private static ActionDriver action;
	
	private static ThreadLocal<WebDriver> driver=new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> action=new ThreadLocal<>();
	
	protected ThreadLocal<SoftAssert> softAssert=ThreadLocal.withInitial(SoftAssert::new);
	public SoftAssert getSoftAssert() {
		return softAssert.get();
	}
	
	public static final Logger logger= LoggerManager.getLogger(BaseClass.class);
	
	
	
	//Load the configuration file
	@BeforeSuite
	public void loadConfig() throws IOException {


		prop=new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+ "/src/main/resources/config.properties");
		prop.load(fis);
		logger.info("Config.properties file is loaded");
		
		//Start the extentreport
		//ExtentManager.getReporter();--> this has been done in the TestListner class.
	}

	@BeforeMethod
	public void setup() throws IOException {
		System.out.println("Setting up web driver for" +this.getClass().getSimpleName());
		launchBrowser();
		configureBrowser();
		staticWait(2);
		logger.info("Web Driver is initialised");
		logger.trace("This is a trace message");
		logger.error("This is an error message");
		logger.debug("This is a debug message");
		logger.fatal("This is a Fatal message");
		
//		if(action==null) {
//			action= new ActionDriver(driver);
//			logger.info("Action Driver instance is created "+Thread.currentThread().getId());
//		}
		
		//initialise actiondriver for current thread
		action.set(new ActionDriver(getDriver()));
		logger.info("Action driver is initialised for thread: "+Thread.currentThread().getId());
	}
	//Load the browser
	private synchronized void launchBrowser() {

		String browser=prop.getProperty("browser");

		if(browser.equalsIgnoreCase("chrome")) {
			//driver= new ChromeDriver();
			
			//create chrome options
			ChromeOptions options=new ChromeOptions();
			options.addArguments("--headless");
			options.addArguments("--disable-gpu");
			options.addArguments("--window-size=1920,1080");
			options.addArguments("--disable-notifications");
			options.addArguments("--no-sandbox");
			options.addArguments("disable-dev-shm-usage");
			
			
			driver.set(new ChromeDriver());
			logger.info("Chrome is initialised");
			ExtentManager.registerDriver(getDriver());
		}
		
		else if(browser.equalsIgnoreCase("firefox")) {
//			driver= new FirefoxDriver();
			driver.set(getDriver());
			logger.info("Chrome is initialised");
		}

		else if(browser.equalsIgnoreCase("edge")) {
//			driver= new EdgeDriver();
			driver.set(getDriver());
			logger.info("Chrome is initialised");
		}

		else {
			throw new IllegalArgumentException("Browser not supported "+browser);
		}
	}

	//configure browser settings as implicit wait , window maximize

	private void configureBrowser() {
		//implicit wait

		int implicitWait= Integer.parseInt(prop.getProperty("implicitWait").trim());
		driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

		//maximize the browser
		driver.get().manage().window().maximize();

		//Navigate to URL
		try
		{
			driver.get().get(prop.getProperty("url"));
		}
		catch(Exception e){
			System.out.println("Unable to find the browser"+ e.getMessage());

		}

	}

	//to remain the browser open for few seconds.
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}

	//getter and setter so that webDriver can also be accessed outside the package.
	//	public WebDriver getDriver(){
	//		return driver;
	//	}
	//
	//	public void setDriver(WebDriver driver){
	//		this.driver=driver;
	//	}

	public synchronized static WebDriver getDriver() {
		if(driver.get()==null) {
			System.out.println("Web driver is not initialised");
			throw new IllegalStateException();
		}
		return driver.get();
	}

	//Getter method for ActionDriver
	public synchronized static ActionDriver getAction() {
		if(action.get()==null) {
			System.out.println("Web driver is not initialised");
			throw new IllegalStateException();
		}
		return action.get();
	}
	
	//getter and setter to access prop variable outside the base class
	public static Properties getProp() {
		return prop;
	}

	public static void setProp(Properties prop) {
		BaseClass.prop = prop;
	}

	@AfterMethod
	public void tearDown() {
		if (driver.get()!=null) {
			try {
				driver.get().quit();
				
			}
			catch(Exception e) {
				System.out.println("Unable to quit the driver "+e.getMessage());

			}
		}
		logger.info("WebDriver instance closed");
//		driver=null;
//		action=null;
		driver.remove();
		action.remove();
//		ExtentManager.endTest();-->This has been implemented in TestListner class.
	}
}
