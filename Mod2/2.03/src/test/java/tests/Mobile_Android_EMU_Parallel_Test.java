package tests;

import java.lang.reflect.Method; // added
import java.net.URL;

import static tests.Config.region; // added

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult; // added
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;

public class Mobile_Android_EMU_Parallel_Test {
    private static final String APP = "Android.SauceLabs.Mobile.Sample.app.2.7.1.apk"; // don't need dir
    URL url; //added, deleted APPIUM
    private static ThreadLocal<AndroidDriver> androidDriver = new ThreadLocal<AndroidDriver>();

private String sessionId;

String usernameID = "test-Username";
String passwordID = "test-Password";
String submitButtonID = "test-LOGIN";
By ProductTitle = By.xpath("//android.widget.TextView[@text='PRODUCTS']");

@BeforeMethod
public void setUp(Method method) throws Exception {
	System.out.println("Sauce Android EMU App Test - Parallel - BeforeMethod hook");       
    String username = System.getenv("SAUCE_USERNAME"); // added
    String accesskey = System.getenv("SAUCE_ACCESS_KEY"); //added
    String sauceUrl;
    String methodName = method.getName(); // added
        if (region.equalsIgnoreCase("eu")) {
            sauceUrl = "@ondemand.eu-central-1.saucelabs.com:443";
        } else {
            sauceUrl = "@ondemand.us-west-1.saucelabs.com:443";
        }
    String SAUCE_REMOTE_URL = "https://" + username + ":" + accesskey + sauceUrl + "/wd/hub"; // if else statement added
    url = new URL(SAUCE_REMOTE_URL); //added
    
DesiredCapabilities capabilities = new DesiredCapabilities();
capabilities.setCapability("deviceName", "Android GoogleAPI Emulator"); //Changed to GoogleAPI Emu
capabilities.setCapability("platformName", "Android");
capabilities.setCapability("platformVersion", "8.0");
capabilities.setCapability("automationName", "UiAutomator2");
capabilities.setCapability("appWaitActivity", "com.swaglabsmobileapp.MainActivity");
capabilities.setCapability("app", "storage:filename=" + APP); //changed
capabilities.setCapability("name", methodName);
androidDriver.set(new AndroidDriver(url, capabilities)); // updated
}

@AfterMethod
public void tearDown(ITestResult result) {
	System.out.println("Sauce Android Mobile EMU Parallel Test - AfterMethod hook");// added
	try {
		if (androidDriver.get() != null) {
			((JavascriptExecutor) androidDriver.get()).executeScript("sauce:job-result=", (result.isSuccess() ? "passed" : "failed"));
		}
	} finally {
		androidDriver.get().quit();
		System.out.println("Sauce - Released Driver");
	}
}

@Test
public void loginToSwagLabsTestValid() {
System.out.println("Sauce - Start loginToSwagLabsTestValid test");

login("standard_user", "secret_sauce");

// Verification
Assert.assertTrue(isOnProductsPage());
}

@Test
public void loginTestValidProblem() {
System.out.println("Sauce - Start loginTestValidProblem test");

login("problem_user", "secret_sauce");

// Verification - we on Product page
Assert.assertTrue(isOnProductsPage());
}

public void login(String user, String pass){

WebDriverWait wait = new WebDriverWait(androidDriver.get(), 5);
final WebElement usernameEdit = wait.until(ExpectedConditions.visibilityOfElementLocated(new MobileBy.ByAccessibilityId(usernameID)));

usernameEdit.click();
usernameEdit.sendKeys(user);

WebElement passwordEdit = androidDriver.get().findElementByAccessibilityId(passwordID);
passwordEdit.click();
passwordEdit.sendKeys(pass);

WebElement submitButton = androidDriver.get().findElementByAccessibilityId(submitButtonID);
submitButton.click();
}

public boolean isOnProductsPage() {
//Create an instance of a Appium explicit wait so that we can dynamically wait for an element
WebDriverWait wait = new WebDriverWait(androidDriver.get(), 5);

//wait for the product field to be visible and store that element into a variable
try {
    wait.until(ExpectedConditions.visibilityOfElementLocated(ProductTitle));
} catch (TimeoutException e){
    System.out.println("*** Timed out waiting for product page to load.");
    return false;
}
return true;
}

}
