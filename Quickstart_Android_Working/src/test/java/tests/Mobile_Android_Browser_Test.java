package tests;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URL;
import static tests.Config.region;


public class Mobile_Android_Browser_Test {

    String appUrl = "https://saucedemo.com"; //added
    private AndroidDriver driver;

    By usernameInput = By.id("user-name");
    By passwordInput = By.id("password");
    By submitButton = By.className("btn_action");
    By productTitle = By.className("product_label");

    @BeforeMethod
    public void setUp() throws Exception {
        String username = System.getenv("SAUCE_USERNAME");
        String accesskey = System.getenv("SAUCE_ACCESS_KEY");
        String sauceUrl ;
        if (region.equalsIgnoreCase("eu")) {
            sauceUrl = "@ondemand.eu-central-1.saucelabs.com:443";
        }else {
            sauceUrl = "@ondemand.us-west-1.saucelabs.com:443";
        }
        String SAUCE_REMOTE_URL = "https://" + username + ":" + accesskey + sauceUrl +"/wd/hub";
        URL url = new URL(SAUCE_REMOTE_URL);
        //All lines above in setup added to connect to Sauce Labs

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", "Android GoogleAPI Emulator"); //changed
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion","9.0" );
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("browserName", "Chrome"); // added


        driver = new AndroidDriver(url, capabilities); //updated

    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }

    }

    @Test
    public void loginToSwagLabsTestValid() {
        System.out.println("Sauce - Start loginToSwagLabsTestValid test");
        login("standard_user", "secret_sauce");

        // Verification
        Assert.assertTrue(isOnProductsPage());
    }

    public void login(String user, String pass){
        driver.get(appUrl);
        driver.findElement(usernameInput).sendKeys(user);
        driver.findElement(passwordInput).sendKeys(pass);

        driver.findElement(submitButton).click();
    }

    public boolean isOnProductsPage() {
        return driver.findElement(productTitle).isDisplayed();

    }

}

