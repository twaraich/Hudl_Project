import Helper.Helper_Selectors;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static Helper.Helper_Selectors.*;


/**
 * User Story :
 * As a Hudl user, I want to be able to successfully login using my credentials
 * and be able to view my account dashboard
 */

public class HudlLoginTest {
    WebDriver driver;
    Logger logger = Logger.getLogger(getClass().getSimpleName());
    ConfigFileReader configFileReader;
    static HashMap<Object, Object> creds = new HashMap<>();

    @BeforeClass
    public static void setupWebdriverChromeDriver() {
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/src/main/resources/chromedriver.exe");

    }

    @Before
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @After
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Verify a user can navigate to Hudl login page by hitting the login endpoint
     * Verify the login page gets successfully loaded
     */
    @Test
    public void LoginPage_DisplayedTest() {
        logger.info("---------Login Page Displayed Test Running--------->");
        driver.get(HUDL_LOGIN_URL);
        verifyTitleOfPage(PageTitle_Selector, LoginScreen_Title_Text);
    }

    /**
     * Verify a user with valid credentials can login into Hudl account(non-org account)
     * Verify the secure password is masked and not shown via password input field
     */
    @Test
    public void LoginPage_SuccessfulLoginTest() {
        logger.info("---------Successful Login Test Running--------->");
        configFileReader = new ConfigFileReader();
        driver.get(HUDL_LOGIN_URL);

        //Reading the valid username via a config properties file
        WebElement userInput = driver.findElement(By.xpath(Username_Selector));
        userInput.sendKeys(configFileReader.getUserName());

        //Reading the valid password via a config properties file
        WebElement passInput = driver.findElement(By.xpath(Password_Selector));
        passInput.sendKeys(configFileReader.getPassword());

        //Verifying password field does not store the user input(masking the real password)
        WebElement passText = driver.findElement(By.xpath(Helper_Selectors.Password_Selector));
        Assert.assertTrue(passText.getAttribute("innerText").isEmpty());
        Assert.assertTrue(passText.getText().isEmpty());

        WebElement loginButton = driver.findElement(By.xpath(LoginBtn_Selector));
        loginButton.click();

        //Waiting for user account screen to load and verifying the title of the page
        WebDriverWait wait = new WebDriverWait(driver, 15000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='explore-header']")));
        verifyTitleOfPage(PageTitle_Selector, AccountHomeScreen_Title_Text);
    }

    /**
     * Verify a user with invalid credentials cannot login into Hudl account(non-org account)
     * Verify the Invalid credentials alert gets displayed
     * Verify the login button gets disabled while the alert is displayed
     */
    @Test
    public void LoginPage_UnsuccessfulLoginTest() {
        logger.info("---------Unsuccessful Login Test Running--------->");

        //Different pairs of invalid creds to verify unsuccessful login logic
        creds.put("John Doe", "ABC");
        creds.put("", "");
        creds.put("@#$%^", "abc");
        creds.put("abc@gmail.com", "@#$%^");
        creds.put("tarun.waraich@gmail.com", 1234);
        creds.put(111, 112);

        //Looping through the hashmap to get key/value pairs to be used as username/pass
        for (HashMap.Entry<Object, Object> set : creds.entrySet()) {
            driver.get(HUDL_LOGIN_URL);

            WebElement userInput = driver.findElement(By.xpath(Username_Selector));
            userInput.sendKeys(String.valueOf(set.getKey()));

            WebElement passInput = driver.findElement(By.xpath(Password_Selector));
            passInput.sendKeys(String.valueOf(set.getValue()));

            WebElement loginButton = driver.findElement(By.xpath(LoginBtn_Selector));
            loginButton.click();

            //Waiting for alert to display
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            WebElement alertText = driver.findElement(By.cssSelector(LoginAlert_Selector));

            //Login button should be disabled and an alert should be observed
            Assert.assertEquals("Incorrect creds alert not displayed", LoginScreen_InvalidCredsAlert, alertText.getAttribute("innerText"));
            Assert.assertFalse("Login button is enabled", loginButton.isEnabled());
        }
    }

    /**
     * Verify a user can click the back button icon on login screen
     * Verify the user gets navigated to the default hudl home screen
     * Verify the login button on the home screen can be clicked to reach back the login page
     */
    @Test
    public void BackButton_FunctionTest() {
        logger.info("---------Back button Test Running--------->");

        driver.get(HUDL_LOGIN_URL);

        WebElement backBTN = driver.findElement(By.cssSelector(BackBtn_Selector));
        backBTN.click();

        //Waiting for Hudl Home page to load and verifying the title of Home page
        WebDriverWait wait = new WebDriverWait(driver, 15000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='nav']")));
        verifyTitleOfPage(PageTitle_Selector, HomePage_Title_Text);

        //Clicking the header bar login button to go back to the login screen
        clickHeaderBarLoginBtn();
        verifyTitleOfPage(PageTitle_Selector, LoginScreen_Title_Text);
    }

    /**
     * Verify a user can click the Sign Up button on login screen
     * Verify the user gets navigated to the register/signup screen
     * Verify the login button on the signup page can be clicked to reach back the login page
     */
    @Test
    public void SignUpButton_FunctionTest() {
        logger.info("---------Sign Up button Test Running--------->");
        driver.get(HUDL_LOGIN_URL);

        WebElement signUPBTN = driver.findElement(By.cssSelector(SignUpBtn_Selector));
        signUPBTN.click();

        //Waiting for Sign Up page to load and verifying the title of Home page
        WebDriverWait wait = new WebDriverWait(driver, 15000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='nav']")));
        verifyTitleOfPage(PageTitle_Selector, SingUpPage_Title_Text);

        //Clicking the header bar login button to go back to the login screen
        clickHeaderBarLoginBtn();
        verifyTitleOfPage(PageTitle_Selector, LoginScreen_Title_Text);
    }

    /**
     * Verify a user can click Need Help option to use to recover the forgotten credentials
     * Verify the user gets navigated to the reset password screen
     * Verify text on the reset password screen
     * Verify alert popup if a user enters an invalid email address
     * Verify alert popup if a user enters a valid email address
     * Verify if a user enter invalid creds on login screen, an alert appears
     * Verify the alert contains 'need help?' option and if selected it would redirect to reset password screen
     */
    @Test
    public void LoginPage_NeedHelpTest() {
        logger.info("---------Login page 'Need help?' Test Running--------->");
        configFileReader = new ConfigFileReader();
        driver.get(HUDL_LOGIN_URL);

        //Click "Need help?" button
        WebElement needHelpBTN = driver.findElement(By.xpath(NeedHelpBtn_Selector));
        needHelpBTN.click();
        //Verifying reset password screen is displayed
        WebDriverWait wait = new WebDriverWait(driver, 15000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='resetBtn']")));
        WebElement resetText = driver.findElement(By.cssSelector(ResetPassHeading_Selector));
        Assert.assertEquals("Reset Password Message not displayed", NeedHelpPage_HeadingText, resetText.getAttribute("innerText"));

        //Verify alert popup if user enters invalid email format
        WebElement userInput = driver.findElement(By.xpath(ForgetEmail_Selector));
        userInput.sendKeys("aa");
        driver.findElement(By.id("resetBtn")).click();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        WebElement invalidEmailText = driver.findElement(By.cssSelector(NeedHelp_InvalidEmailAlert_Selector));
        Assert.assertEquals("Invalid email format message not displayed", NeedHelpPage_InvalidEmailFormatAlert, invalidEmailText.getAttribute("innerText"));

        //Verify alert popup if user enters valid email format
        userInput.clear();
        userInput.sendKeys(String.valueOf(configFileReader.getUserName()));
        driver.findElement(By.id("resetBtn")).click();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        WebElement checkEmailText = driver.findElement(By.cssSelector(NeedHelp_CheckEmail_Selector));
        Assert.assertEquals("Check email for instructions message not displayed", NeedHelp_CheckEmailAlert, checkEmailText.getAttribute("innerText"));

        driver.findElement(By.id("back-to-login")).click();
        verifyTitleOfPage(PageTitle_Selector, LoginScreen_Title_Text);

        //Verify entering invalid creds on login screen
        WebElement userInput1 = driver.findElement(By.xpath(Username_Selector));
        userInput1.sendKeys("aa");
        WebElement passInput = driver.findElement(By.xpath(Password_Selector));
        passInput.sendKeys("aa");
        WebElement loginButton = driver.findElement(By.xpath(LoginBtn_Selector));
        loginButton.click();

        //Verify 'need help?' option appears as part of the alert text and user can click it
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebElement needHelpAlertButton = driver.findElement(By.cssSelector(BadCreds_NeedHelp_Selector));
        needHelpAlertButton.click();

        //Verifying reset password screen is displayed
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='resetBtn']")));
        WebElement resetText2 = driver.findElement(By.cssSelector(ResetPassHeading_Selector));
        Assert.assertEquals("Reset Password Message not displayed", NeedHelpPage_HeadingText, resetText2.getAttribute("innerText"));
    }

    /**
     * Verify a user can click 'Login with an Organization' button
     * Verify the user gets navigated to the Org. login screen
     * Verify text on the reset password screen
     * Verify alert popup if a user enters hudl user email address(non-org account) and redirects the user to the login screen
     */
    @Test
    public void LoginWithOrgButton_Part1() {
        logger.info("---------Login using organization-Part1 Test Running--------->");
        WebDriverWait wait = new WebDriverWait(driver, 15000);

        configFileReader = new ConfigFileReader();
        driver.get(HUDL_LOGIN_URL);

        WebElement loginPageOrgBtn = driver.findElement(By.xpath(LoginOrgBtn_Selector));
        loginPageOrgBtn.click();

        //Wait for 'Login with org' page to be loaded
        WebElement loginOrgHeader = driver.findElement(By.cssSelector(LoginOrg_HeadingSelector));
        Assert.assertEquals("'Login with your org' message not displayed", LoginOrgBtn_HeadingText, loginOrgHeader.getAttribute("innerText"));

        // Enter a huld user email in the email field
        WebElement userInput = driver.findElement(By.id("uniId_1"));
        userInput.clear();
        userInput.sendKeys(String.valueOf(configFileReader.getUserName()));
        WebElement orgPageLoginBtn = driver.findElement(By.xpath(LoginOrg_LoginButton_Selector));
        orgPageLoginBtn.click();

        //Verify alert message popup for invalid email provided
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        WebElement checkAlertEmailText = driver.findElement(By.cssSelector(LoginOrg_InvalidEmailAlert_Selector));
        Assert.assertEquals("'This account can't log in with an organization yet.' message not displayed", LoginOrgBtn_InvalidEmailAlert, checkAlertEmailText.getAttribute("innerText"));

        //Verify user is on the login screen
        verifyTitleOfPage(PageTitle_Selector, LoginScreen_Title_Text);
    }

    /**
     * Verify a user can click Login with an Organization button
     * Verify the user gets navigated to the Org. login screen
     * Verify text on the reset password screen
     * Verify entering random string(invalid email format) is not accepted by the email input field
     * Verify entering valid email format entry(non-org email) prompts an alert and redirects the user to the login screen
     */
    @Test
    public void LoginWithOrgButton_Part2() {
        logger.info("---------Login using organization-Part2 Test Running--------->");
        driver.get(HUDL_LOGIN_URL);
        WebElement loginPageOrgBtn = driver.findElement(By.xpath(LoginOrgBtn_Selector));
        loginPageOrgBtn.click();

        // Enter 'admin' in the email field
        WebElement userInput = driver.findElement(By.id("uniId_1"));
        userInput.clear();
        userInput.sendKeys("admin");
        WebElement orgPageLoginBtn = driver.findElement(By.xpath(LoginOrg_LoginButton_Selector));
        orgPageLoginBtn.click();

        //Verifying user stays on the same page
        verifyTitleOfPage(LoginOrg_HeadingSelector, LoginOrgBtn_HeadingText);
        driver.get("https://www.hudl.com/app/auth/login/organization");
        WebDriverWait wait = new WebDriverWait(driver, 15000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("uniId_1")));

        //Enter valid email format entry but should be a non-org account
        WebElement userInput1 = driver.findElement(By.id("uniId_1"));
        userInput1.clear();
        userInput1.sendKeys("admin@gmail.com");
        WebElement orgPageLoginBtn1 = driver.findElement(By.xpath(LoginOrg_LoginButton_Selector));
        orgPageLoginBtn1.click();

        //Verify alert popup
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        WebElement checkAlertEmailText = driver.findElement(By.cssSelector(LoginOrg_InvalidEmailAlert_Selector));
        Assert.assertEquals("'This account can't log in with an organization yet.' message not displayed", LoginOrgBtn_InvalidEmailAlert, checkAlertEmailText.getAttribute("innerText"));

        //Verify user is on the login screen
        verifyTitleOfPage(PageTitle_Selector, LoginScreen_Title_Text);
    }

    /**
     * Verify a user can select the 'remember me' option on the login screen
     * Verify a user can login using valid creds from the configurations.properties file
     * Verify user gets redirected to user account page and can logout of it
     * Verify user can navigate back to login screen and shall only enter password as the username should be prefilled
     */
    @Test
    public void RememberMeButton_FunctionTest() {
        logger.info("---------Remember Me button Test Running--------->");
        configFileReader = new ConfigFileReader();
        driver.get(HUDL_LOGIN_URL);

        //Enter valid username and pass from config file
        WebElement userInput = driver.findElement(By.xpath(Username_Selector));
        userInput.sendKeys(configFileReader.getUserName());
        WebElement passInput = driver.findElement(By.xpath(Password_Selector));
        passInput.sendKeys(configFileReader.getPassword());

        //Select 'remember me' option on the login screen
        WebElement rememberMeCheckBox = driver.findElement(By.cssSelector(("body .super-wrap .login-container .remember-help label")));
        rememberMeCheckBox.click();

        //Click login button
        WebElement loginButton = driver.findElement(By.xpath(LoginBtn_Selector));
        loginButton.click();

        //Wait for user account home to load
        WebDriverWait wait = new WebDriverWait(driver, 15000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='explore-header']")));
        verifyTitleOfPage(PageTitle_Selector, AccountHomeScreen_Title_Text);

        //Log out of the user account
        Actions action = new Actions(driver);
        WebElement dropDownList = driver.findElement(By.xpath(UserAccount_DropdownList));
        WebElement logOutBTN = driver.findElement(By.xpath(UserAccount_LogOut_Selector));

        //Hover the mouse over the log out option and click it
        action.moveToElement(dropDownList).moveToElement(logOutBTN).click().build().perform();

        //Verify user gets navigated to Hudl Home Page
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='nav']")));
        verifyTitleOfPage(PageTitle_Selector, HomePage_Title_Text);

        //Navigate to Login screen
        clickHeaderBarLoginBtn();
        verifyTitleOfPage(PageTitle_Selector, LoginScreen_Title_Text);

        //Enter only the password since username is already prefilled
        WebElement passInput2 = driver.findElement(By.xpath(Password_Selector));
        passInput2.sendKeys(configFileReader.getPassword());

        //Click login button
        WebElement loginButton2 = driver.findElement(By.xpath(LoginBtn_Selector));
        loginButton2.click();

        //Verify user account is displayed as user was successfully able to login with prefilled username
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='explore-header']")));
        verifyTitleOfPage(PageTitle_Selector, AccountHomeScreen_Title_Text);
    }

    /**
     * Verify the title of the current page
     *
     * @param titleSelector element selector to be used(example: css,id,xpath)
     * @param titleText     title text to be validated
     */
    public void verifyTitleOfPage(String titleSelector, String titleText) {
        WebElement title = driver.findElement(By.cssSelector(titleSelector));
        Assert.assertEquals("Page not displayed", titleText, title.getAttribute("innerText"));
    }

    /**
     * Find and click the 'log in' button that appears in the the header bar of Hudl home and Signup page
     */
    public void clickHeaderBarLoginBtn() {
        WebElement loginBTN = driver.findElement(By.xpath("//*[contains(text(), 'Log in')]"));
        loginBTN.click();
        WebDriverWait wait = new WebDriverWait(driver, 15000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Username_Selector)));
    }
}