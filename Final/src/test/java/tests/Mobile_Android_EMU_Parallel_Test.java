package tests;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.URL;

import static tests.Config.region;


public class  Mobile_Android_EMU_Parallel_Test {

    private static final String APP = "Android.SauceLabs.Mobile.Sample.app.2.7.0.apk";
    URL url;
    private static ThreadLocal<AndroidDriver> androidDriver = new ThreadLocal<AndroidDriver>(); //added
//    private AndroidDriver driver; //deleted

    String usernameID = "test-Username";
    String passwordID = "test-Password";
    String submitButtonID = "test-LOGIN";
    By ProductTitle = By.xpath("//android.widget.TextView[@text='PRODUCTS']");

    @BeforeMethod
    public void setUp (Method method) throws Exception { // add later
        System.out.println("Sauce Android EMU App Test - Parallel - BeforeMethod hook");
        String methodName = method.getName();
        String username = "annmillikin";//System.getenv("SAUCE_USERNAME");
        String accesskey = "9cb81ab8-8bc9-4c25-becc-435245809b6c";//System.getenv("SAUCE_ACCESS_KEY");
        System.out.println("methodName: "+methodName);
        System.out.println("username: "+username);
        System.out.println("accesskey: "+accesskey);
        
        String sauceUrl;
        if (region.equalsIgnoreCase("eu")) {
            sauceUrl = "@ondemand.eu-central-1.saucelabs.com:443";
        } else {
            sauceUrl = "@ondemand.us-west-1.saucelabs.com:443";
        }
        System.out.println("sauceUrl: "+sauceUrl);
        String SAUCE_REMOTE_URL = "https://annmillikin:7823683c-b9c6-4bfd-bfa1-23d43e1297c8@ondemand.us-west-1.saucelabs.com:443/wd/hub";
        System.out.println("SAUCE_REMOTE_URL: "+SAUCE_REMOTE_URL);
        url = new URL(SAUCE_REMOTE_URL);
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion", "8.0");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("appWaitActivity", "com.swaglabsmobileapp.MainActivity");
        capabilities.setCapability("app", "storage:filename=" + APP);
        capabilities.setCapability("name", methodName);
        System.out.println("before androidDriver line 65");
        androidDriver.set(new AndroidDriver(url, capabilities));// updated
        System.out.println("after androidDriver line 65");
    }

    @AfterMethod
    public void teardown(ITestResult result) {
        System.out.println("Sauce - AfterMethod hook");
        try {
            if (getAndroidDriver() != null) { // changed to getAndroidDriver()
                ((JavascriptExecutor) getAndroidDriver()).executeScript("sauce:job-result=" + (result.isSuccess() ? "passed" : "failed"));  //changed to  getAndroidDriver()
            }
        }finally {
            System.out.println("Sauce - release driver");
            getAndroidDriver().quit();  //changed to  getAndroidDriver()
        } //try, finally added

    }
    public  AndroidDriver getAndroidDriver() {return androidDriver.get(); } // added -- can replace with AndroidDriver.get in every case instead of creating the getAndroidDriver class

    @Test
    public void loginToSwagLabsTestValid () {
        System.out.println("Sauce - Start loginToSwagLabsTestValid test");

        login("standard_user", "secret_sauce");

        // Verification
        Assert.assertTrue(isOnProductsPage());
    }

    @Test
    public void loginTestValidProblem () {
        System.out.println("Sauce - Start loginTestValidProblem test in Mobile_Android_EMU_Parallel_test.java");

        login("problem_user", "secret_sauce");

        // Verification - we on Product page
        Assert.assertTrue(isOnProductsPage());
    }

    public void login (String user, String pass){

        WebDriverWait wait = new WebDriverWait(getAndroidDriver(), 5); //changed to  getAndroidDriver()
        final WebElement usernameEdit = wait.until(ExpectedConditions.visibilityOfElementLocated(new MobileBy.ByAccessibilityId(usernameID)));

        usernameEdit.click();
        usernameEdit.sendKeys(user);

        WebElement passwordEdit = getAndroidDriver().findElementByAccessibilityId(passwordID); //changed to  getAndroidDriver()
        passwordEdit.click();
        passwordEdit.sendKeys(pass);

        WebElement submitButton = getAndroidDriver().findElementByAccessibilityId(submitButtonID); //changed to  getAndroidDriver()
        submitButton.click();
    }

    public boolean isOnProductsPage () {
        //Create an instance of an Appium explicit wait so that we can dynamically wait for an element
        WebDriverWait wait = new WebDriverWait(getAndroidDriver(), 5); //changed to  getAndroidDriver()

        //wait for the product field to be visible and store that element into a variable
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(ProductTitle));
        } catch (TimeoutException e) {
            System.out.println("*** Timed out waiting for product page to load.");
            return false;
        }
        return true;
    }
}


