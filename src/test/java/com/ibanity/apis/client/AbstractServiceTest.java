package com.ibanity.apis.client;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.sandbox.services.FinancialInstitutionAccountsService;
import com.ibanity.apis.client.sandbox.services.FinancialInstitutionUsersService;
import com.ibanity.apis.client.sandbox.services.SandboxFinancialInstitutionsService;
import com.ibanity.apis.client.sandbox.services.impl.FinancialInstitutionAccountsServiceImpl;
import com.ibanity.apis.client.sandbox.services.impl.FinancialInstitutionUsersServiceImpl;
import com.ibanity.apis.client.sandbox.services.impl.SandboxFinancialInstitutionsServiceImpl;
import com.ibanity.apis.client.services.AccountsService;
import com.ibanity.apis.client.services.CustomerAccessTokensService;
import com.ibanity.apis.client.services.configuration.IbanityConfiguration;
import com.ibanity.apis.client.services.impl.AccountsServiceImpl;
import com.ibanity.apis.client.services.impl.CustomerAccessTokensServiceImpl;
import com.ibanity.apis.client.utils.FileUtils;
import com.ibanity.apis.client.utils.OSValidator;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public abstract class AbstractServiceTest {
    protected static final String FAKE_TPP_ACCOUNT_INFORMATION_ACCESS_REDIRECT_URL = IbanityConfiguration.getConfiguration().getString(IbanityConfiguration.IBANITY_PROPERTIES_PREFIX + "tpp.accounts.information.access.result.redirect.url");
    protected static final String FAKE_TPP_PAYMENT_INITIATION_REDIRECT_URL = IbanityConfiguration.getConfiguration().getString(IbanityConfiguration.IBANITY_PROPERTIES_PREFIX + "tpp.payments.initiation.result.redirect.url");
    private static final String TEST_CASE = AbstractServiceTest.class.getSimpleName();

    protected Instant now;

    protected String name;


    protected WebDriver driver;
    protected Actions actions;
    protected JavascriptExecutor javascriptExecutor;

    protected StringBuffer verificationErrors = new StringBuffer();

    protected AccountsService accountsService = new AccountsServiceImpl();
    protected FinancialInstitutionAccountsService financialInstitutionAccountsService = new FinancialInstitutionAccountsServiceImpl();
    protected FinancialInstitutionUsersService financialInstitutionUsersService = new FinancialInstitutionUsersServiceImpl();
    protected SandboxFinancialInstitutionsService sandboxFinancialInstitutionsService = new SandboxFinancialInstitutionsServiceImpl();
    protected CustomerAccessTokensService customerAccessTokensService = new CustomerAccessTokensServiceImpl();

    protected CustomerAccessToken generatedCustomerAccessToken;

    protected FinancialInstitution financialInstitution;
    protected FinancialInstitutionUser financialInstitutionUser;
    protected List<FinancialInstitutionAccount> financialInstitutionAccounts = new ArrayList<>();

    protected ExpectedCondition<Boolean> expectationPageCompleted = driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");


    protected void initSelenium() {
        FileUtils fileUtils = new FileUtils();
        String driverFilePath = fileUtils.getFile("chromedriver." + OSValidator.getSystemOperatingSystem()).getAbsolutePath();
        File driverFile = new File(driverFilePath);
        driverFile.setExecutable(true);
        System.setProperty("webdriver.chrome.driver", driverFilePath);

        ChromeOptions options = new ChromeOptions();

        options.addArguments("headless");
        driver = new ChromeDriver(options);
        actions = new Actions(driver);
        options.addArguments("window-size=1900x1600");
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        javascriptExecutor = (JavascriptExecutor) driver;
    }

    protected void exitSelenium() {
        driver.quit();
        driver = null;
    }

    protected void initPublicAPIEnvironment() throws Exception {
        generatedCustomerAccessToken = getCustomerAccessToken(UUID.randomUUID().toString());
        financialInstitution = createFinancialInstitution();
        financialInstitutionUser = createFinancialInstitutionUser();
        for (int index = 0; index < 5; index++) {
            financialInstitutionAccounts.add(createFinancialInstitutionAccount(financialInstitution, financialInstitutionUser.getId()));
        }
    }

    protected void cleanPublicAPIEnvironment() throws Exception {
        for (FinancialInstitutionAccount financialInstitutionAccount : financialInstitutionAccounts) {
            deleteFinancialInstitutionAccount(financialInstitution.getId(), financialInstitutionUser.getId(),financialInstitutionAccount.getId());
        }
        deleteFinancialInstitution(financialInstitution.getId());
        deleteFinancialInstitutionUser(financialInstitutionUser.getId());
    }

    public FinancialInstitution createFinancialInstitution() {
        now = Instant.now();
        name = TEST_CASE + "-" + now.toString();
        FinancialInstitution newFinancialInstitution = new FinancialInstitution();
        newFinancialInstitution.setSandbox(Boolean.TRUE);
        newFinancialInstitution.setName(name);
        return sandboxFinancialInstitutionsService.createFinancialInstitution(newFinancialInstitution);
    }

    protected FinancialInstitutionUser createFinancialInstitutionUser() {
        Instant now = Instant.now();
        FinancialInstitutionUser financialInstitutionUser = new FinancialInstitutionUser();
        financialInstitutionUser.setFirstName("FirstName-"+now);
        financialInstitutionUser.setLastName("LastName-"+now);
        financialInstitutionUser.setLogin("Login-"+now);
        financialInstitutionUser.setPassword("Password-"+now);
        return financialInstitutionUsersService.createFinancialInstitutionUser(financialInstitutionUser);
    }


    protected void exitPublicApiEnvironment() throws Exception {
        for (FinancialInstitutionAccount financialInstitutionAccount : financialInstitutionAccounts) {
            deleteFinancialInstitutionAccount(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId());
        }
        deleteFinancialInstitutionUser(financialInstitutionUser.getId());
        deleteFinancialInstitution(financialInstitution.getId());
    }

    protected void deleteFinancialInstitutionUser(UUID financialInstitutionUserID) throws ResourceNotFoundException {
        financialInstitutionUsersService.deleteFinancialInstitutionUser(financialInstitutionUserID);
    }


    protected FinancialInstitutionAccount createFinancialInstitutionAccount(FinancialInstitution financialInstitution, UUID financialInstitutionUser) throws ResourceNotFoundException {
        FinancialInstitutionAccount financialInstitutionAccount = new FinancialInstitutionAccount();
        financialInstitutionAccount.setSubType("checking");
        financialInstitutionAccount.setReference(Iban.random(CountryCode.BE).toString());
        financialInstitutionAccount.setReferenceType("IBAN");
        financialInstitutionAccount.setDescription("Checking Account");
        financialInstitutionAccount.setCurrency("EUR");
        financialInstitutionAccount.setFinancialInstitution(financialInstitution);
        return financialInstitutionAccountsService.createFinancialInstitutionAccount(financialInstitution.getId(), financialInstitutionUser, financialInstitutionAccount);
    }

    protected void deleteFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId) throws ResourceNotFoundException {
        financialInstitutionAccountsService.deleteFinancialInstitutionAccount(financialInstitutionId, financialInstitutionUserId, financialInstitutionAccountId);
    }

    protected AccountInformationAccessRequest getAccountInformationAccessRequest() {
        AccountInformationAccessRequest accountInformationAccessRequest = new AccountInformationAccessRequest();
        accountInformationAccessRequest.setConsentReference(UUID.randomUUID().toString());
        accountInformationAccessRequest.setRedirectUri(FAKE_TPP_ACCOUNT_INFORMATION_ACCESS_REDIRECT_URL);
        accountInformationAccessRequest.setFinancialInstitution(financialInstitution);
        return accountsService.getAccountInformationAccessRequest(generatedCustomerAccessToken, accountInformationAccessRequest);
    }

    protected void deleteFinancialInstitution(UUID financialInstitutionId) throws ResourceNotFoundException {
        sandboxFinancialInstitutionsService.deleteFinancialInstitution(financialInstitutionId);
    }

    protected CustomerAccessToken getCustomerAccessToken(String applicationCustomerReference){
        CustomerAccessToken customerAccessTokenRequest = new CustomerAccessToken();
        customerAccessTokenRequest.setApplicationCustomerReference(applicationCustomerReference);
        return customerAccessTokensService.createCustomerAccessToken(customerAccessTokenRequest);
    }


    protected void authorizeAccounts(String redirectUrl) {
        if (driver == null) {
            initSelenium();
        }
        driver.get(redirectUrl);
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(expectationPageCompleted);
        List<WebElement> webElements = driver.findElements(By.id("login"));
        // Checking with the authentication was already done or not.
        if (webElements.size() == 1){
            // Doing the authentication
            WebElement webElement = webElements.get(0);
            webElement.click();
            webElement.clear();
            webElement.sendKeys(financialInstitutionUser.getLogin());
            webElement = driver.findElements(By.id("password")).get(0);
            webElement.clear();
            webElement.sendKeys(financialInstitutionUser.getPassword());
            driver.findElement(By.xpath("//button[@type='submit']")).submit();
            wait = new WebDriverWait(driver, 30);
            wait.until(expectationPageCompleted);
            driver.findElement(By.id("response")).clear();
            driver.findElement(By.id("response")).sendKeys("123456");
            driver.findElement(By.xpath("//button[@type='submit']")).submit();

            wait = new WebDriverWait(driver, 30);
            wait.until(expectationPageCompleted);
        }

        List<String> iBanList = financialInstitutionAccounts.stream().map(financialInstitutionAccount -> financialInstitutionAccount.getReference()).collect(Collectors.toList());

        List<WebElement> uiAccountsList = driver.findElements(By.xpath("//input[@type='checkbox']"));
        uiAccountsList.stream()
                .filter(webElement -> iBanList.contains(webElement.getAttribute("value")))
                .forEach(webElement -> {
                    javascriptExecutor.executeScript("arguments[0].scrollIntoView();", webElement);
                    actions.moveToElement(webElement).click().build().perform();
                })
        ;

        WebElement webElementSelectButton = driver.findElement(By.xpath("//button[text()='Select' and @type='submit']"));
        if (webElementSelectButton.isEnabled() && webElementSelectButton.isDisplayed()) {
            javascriptExecutor.executeScript("arguments[0].scrollIntoView();", webElementSelectButton);
            javascriptExecutor.executeScript("arguments[0].click();", webElementSelectButton);
        } else {
            throw new RuntimeException("Accounts 'Select' button not visible or not displayed");
        }

        wait = new WebDriverWait(driver, 30);
        wait.until(expectationPageCompleted);

        WebElement acceptAccountsElement = driver.findElement(By.xpath("//button[text()='Accept']"));
        if (acceptAccountsElement.isEnabled() && acceptAccountsElement.isDisplayed()) {
            javascriptExecutor.executeScript("arguments[0].scrollIntoView();", acceptAccountsElement);
            javascriptExecutor.executeScript("arguments[0].click();", acceptAccountsElement);
            wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.urlToBe(FAKE_TPP_ACCOUNT_INFORMATION_ACCESS_REDIRECT_URL));
        } else {
            throw new RuntimeException("Accounts selection final 'Accept' button not visible or not displayed");
        }
    }

}
