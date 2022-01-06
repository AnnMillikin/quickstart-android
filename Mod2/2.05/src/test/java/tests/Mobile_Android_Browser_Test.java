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
    import static tests.Config.region; // added
    import java.net.URL;


    public class Mobile_Android_Browser_Test {
        URL url;
        // Deleted APP and APPIUM
        String appUrl = "https://www.saucedemo.com/"; //added

        private AndroidDriver driver;

    //    String usernameID = "test-Username";
    //    String passwordID = "test-Password";
    //    String submitButtonID = "test-LOGIN";
    //    By ProductTitle = By.xpath("//android.widget.TextView[@text='PRODUCTS']");
        By usernameInput = By.id("user-name");
        By passwordInput = By.id("password");
        By submitButton = By.className("btn_action");
        By productTitle = By.className("inventory_item_label");


        @BeforeMethod
        public void setUp() throws Exception {
            System.out.println("Sauce Android Mobile Browser EMU Test - AfterMethod Hook"); //added
            String username = System.getenv("SAUCE_USERNAME"); // added
            String accesskey = System.getenv("SAUCE_ACCESS_KEY"); //added
            String sauceUrl;
            if (region.equalsIgnoreCase("eu")) {
                sauceUrl = "@ondemand.eu-central-1.saucelabs.com:443";
            } else {
                sauceUrl = "@ondemand.us-west-1.saucelabs.com:443";
            }
            String SAUCE_REMOTE_URL = "https://" + username + ":" + accesskey + sauceUrl + "/wd/hub"; // if else statement added
            url = new URL(SAUCE_REMOTE_URL); //added
            System.out.println("url: "+ url);
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("deviceName", "Android Emulator"); // added
            capabilities.setCapability("platformName", "Android");
            capabilities.setCapability("platformVersion","8.0" );
            capabilities.setCapability("automationName", "UiAutomator2");
    //        capabilities.setCapability("appWaitActivity", "com.swaglabsmobileapp.MainActivity"); //removed
            capabilities.setCapability("browserName", "Chrome");// added
    //        driver = new AndroidDriver(new URL(APPIUM), capabilities); //removed
            driver = new AndroidDriver(url, capabilities); //added
        }

        @AfterMethod
        public void tearDown() {
            System.out.println("Sauce Android Mobile Browser EMU Test - AfterMethod Hook");// added
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

        @Test
        public void loginTestValidProblem() {
            System.out.println("Sauce - Start loginTestValidProblem test");

            login("problem_user", "secret_sauce");

            // Verification - we on Product page
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

