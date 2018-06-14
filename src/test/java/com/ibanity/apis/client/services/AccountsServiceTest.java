package com.ibanity.apis.client.services;

import com.ibanity.apis.client.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.sandbox.services.FinancialInstitutionAccountsServiceTest;
import com.ibanity.apis.client.sandbox.services.FinancialInstitutionUsersServiceTest;
import com.ibanity.apis.client.sandbox.services.SandboxFinancialInstitutionsServiceTest;
import com.ibanity.apis.client.services.configuration.IbanityConfiguration;
import com.ibanity.apis.client.services.impl.AccountsServiceImpl;
import com.ibanity.apis.client.utils.FileUtils;
import com.ibanity.apis.client.utils.OSValidator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * AccountsServiceImpl Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Jun 14, 2018</pre>
 */
public class AccountsServiceTest {
    private static final String FAKE_TPP_ACCOUNT_INFORMATION_ACCESS_REDIRECT_URL = IbanityConfiguration.getConfiguration().getString(IbanityConfiguration.IBANITY_PROPERTIES_PREFIX + "tpp.accounts.information.access.result.redirect.url");

    private static WebDriver driver;
    private static String baseUrl;
    private boolean acceptNextAlert = true;
    private static StringBuffer verificationErrors = new StringBuffer();

    private static final AccountsService accountsService = new AccountsServiceImpl();
    private static CustomerAccessToken generatedCustomerAccessToken;

    private static FinancialInstitution financialInstitution;
    private static FinancialInstitutionUser financialInstitutionUser;
    private static List<FinancialInstitutionAccount> financialInstitutionAccounts = new ArrayList<>();

    static {
        FileUtils fileUtils = new FileUtils();
        System.setProperty("webdriver.chrome.driver", fileUtils.getFile("chromedriver." + OSValidator.getSystemOperatingSystem()).getAbsolutePath());
    }

    @BeforeAll
    public static void beforeAll() throws Exception {
        ChromeOptions options = new ChromeOptions();

        options.addArguments("headless");
        driver = new ChromeDriver(options);
        options.addArguments("window-size=1200x600");
        baseUrl = "https://www.ibanity.com/";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        generatedCustomerAccessToken = CustomerAccessTokensServiceTest.getCustomerAccessToken(UUID.randomUUID().toString());
        financialInstitution = SandboxFinancialInstitutionsServiceTest.createFinancialInstitution();
        financialInstitutionUser = FinancialInstitutionUsersServiceTest.createFinancialInstitutionUser();
        for (int index = 0; index < 3 ; index ++){
            financialInstitutionAccounts.add(FinancialInstitutionAccountsServiceTest.createFinancialInstitutionAccount(financialInstitution, financialInstitutionUser.getId()));
        }
    }

    @AfterAll
    public static void afterAll() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
        for(FinancialInstitutionAccount financialInstitutionAccount : financialInstitutionAccounts){
            FinancialInstitutionAccountsServiceTest.deleteFinancialInstitutionAccount(financialInstitution.getId(),financialInstitutionUser.getId(), financialInstitutionAccount.getId());
        }
        FinancialInstitutionUsersServiceTest.deleteFinancialInstitutionUser(financialInstitutionUser.getId());
        SandboxFinancialInstitutionsServiceTest.deleteFinancialInstitution(financialInstitution.getId());
    }

    /**
     * Method: getCustomerAccount(CustomerAccessToken customerAccessToken, UUID accountId, UUID financialInstitutionId)
     */
    @Test
    public void testGetCustomerAccount() throws Exception {
    }

    /**
     * Method: getCustomerAccounts(CustomerAccessToken customerAccessToken)
     */
    @Test
    public void testGetCustomerAccountsCustomerAccessToken() throws Exception {
    }

    /**
     * Method: getCustomerAccounts(CustomerAccessToken customerAccessToken, IbanityPagingSpec pagingSpec)
     */
    @Test
    public void testGetCustomerAccountsForCustomerAccessTokenPagingSpec() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getCustomerAccounts(CustomerAccessToken customerAccessToken, UUID financialInstitutionId)
     */
    @Test
    public void testGetCustomerAccountsForCustomerAccessTokenFinancialInstitutionId() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getCustomerAccounts(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, IbanityPagingSpec pagingSpec)
     */
    @Test
    public void testGetCustomerAccountsForCustomerAccessTokenFinancialInstitutionIdPagingSpec() throws Exception {
//TODO: Test goes here... 
    }

    public static AccountInformationAccessRequest getAccountInformationAccessRequest() {
        AccountInformationAccessRequest accountInformationAccessRequest = new AccountInformationAccessRequest();
        accountInformationAccessRequest.setConsentReference(UUID.randomUUID().toString());
        accountInformationAccessRequest.setRedirectUri(FAKE_TPP_ACCOUNT_INFORMATION_ACCESS_REDIRECT_URL);
        accountInformationAccessRequest.setFinancialInstitution(financialInstitution);
        return accountsService.getAccountInformationAccessRequest(generatedCustomerAccessToken, accountInformationAccessRequest);
    }

    /**
     * Method: getAccountInformationAccessRequest(CustomerAccessToken customerAccessToken, AccountInformationAccessRequest accountInformationAccessRequest)
     */
    @Test
    public void testGetAccountInformationAccessRequest() throws Exception {
        AccountInformationAccessRequest accountInformationAccessRequest = getAccountInformationAccessRequest();
        assertNotNull(accountInformationAccessRequest.getLinks().getRedirect());
        assertNotNull(URI.create(accountInformationAccessRequest.getLinks().getRedirect()));
        this.authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());
    }

    /**
     * Method: getAccountsInformationAccessAuthorizations(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID accountInformationAccessRequestId)
     */
    @Test
    public void testGetAccountsInformationAccessAuthorizationsForCustomerAccessTokenFinancialInstitutionIdAccountInformationAccessRequestId() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getAccountsInformationAccessAuthorizations(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID accountInformationAccessRequestId, IbanityPagingSpec pagingSpec)
     */
    @Test
    public void testGetAccountsInformationAccessAuthorizationsForCustomerAccessTokenFinancialInstitutionIdAccountInformationAccessRequestIdPagingSpec() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: revokeAccountsAccessAuthorization(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, AccountInformationAccessAuthorization accountInformationAccessAuthorization)
     */
    @Test
    public void testRevokeAccountsAccessAuthorization() throws Exception {
//TODO: Test goes here... 
    }

    private void authorizeAccounts(String redirectUrl) {
        driver.get(redirectUrl);
        driver.findElement(By.id("login")).click();
        driver.findElement(By.id("login")).clear();
        driver.findElement(By.id("login")).sendKeys(financialInstitutionUser.getLogin());
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(financialInstitutionUser.getPassword());
        driver.findElement(By.xpath("//button[@type='submit']")).submit();
        driver.findElement(By.id("response")).clear();
        driver.findElement(By.id("response")).sendKeys("123456");
        driver.findElement(By.xpath("//button[@type='submit']")).submit();
        for(FinancialInstitutionAccount financialInstitutionAccount : financialInstitutionAccounts) {
            WebElement element = driver.findElement(By.xpath("//input[@value='" + financialInstitutionAccount.getReference() + "']"));
            if (element.isEnabled()) {
                element.click();
            }
        }
        driver.findElement(By.xpath("//button[@type='submit']")).submit();
        driver.findElement(By.xpath("//div[@id='app']/div/main/div/div[2]/div/button")).submit();
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}
