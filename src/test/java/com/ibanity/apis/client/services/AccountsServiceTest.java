package com.ibanity.apis.client.services;

import com.ibanity.apis.client.exceptions.ResourceNotFoundException;
import com.ibanity.apis.client.models.Account;
import com.ibanity.apis.client.models.AccountInformationAccessAuthorization;
import com.ibanity.apis.client.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
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
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

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
    private static Actions actions;

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
        actions = new Actions(driver);
        options.addArguments("window-size=1900x1600");
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        generatedCustomerAccessToken = CustomerAccessTokensServiceTest.getCustomerAccessToken(UUID.randomUUID().toString());
        financialInstitution = SandboxFinancialInstitutionsServiceTest.createFinancialInstitution();
        financialInstitutionUser = FinancialInstitutionUsersServiceTest.createFinancialInstitutionUser();
        for (int index = 0; index < 3; index++) {
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
        for (FinancialInstitutionAccount financialInstitutionAccount : financialInstitutionAccounts) {
            FinancialInstitutionAccountsServiceTest.deleteFinancialInstitutionAccount(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId());
        }
        FinancialInstitutionUsersServiceTest.deleteFinancialInstitutionUser(financialInstitutionUser.getId());
        SandboxFinancialInstitutionsServiceTest.deleteFinancialInstitution(financialInstitution.getId());
    }

    /**
     * Method: getCustomerAccount(CustomerAccessToken customerAccessToken, UUID accountId, UUID financialInstitutionId)
     */
    @Test
    public void testGetCustomerAccount() throws Exception {
        AccountInformationAccessRequest accountInformationAccessRequest = getAccountInformationAccessRequest();
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());
        List<Account> accountsList = accountsService.getCustomerAccounts(generatedCustomerAccessToken, financialInstitution.getId());
        for (Account account : accountsList) {
            Account accountResult = accountsService.getCustomerAccount(generatedCustomerAccessToken, account.getId(), financialInstitution.getId());
            assertTrue(account.getReference().equals(accountResult.getReference()));
            assertTrue(account.getReferenceType().equals(accountResult.getReferenceType()));
            assertTrue(account.getCurrency().equals(accountResult.getCurrency()));
            assertTrue(account.getSubType().equals(accountResult.getSubType()));
            assertTrue(account.getAvailableBalance().equals(accountResult.getAvailableBalance()));
            assertTrue(account.getCurrentBalance().equals(accountResult.getCurrentBalance()));
        }
    }

    @Test
    public void testGetCustomerAccountWithWrongIDs() throws Exception {
        assertThrows(ResourceNotFoundException.class, () -> accountsService.getCustomerAccount(generatedCustomerAccessToken, UUID.randomUUID(), UUID.randomUUID()));
    }

    /**
     * Method: getCustomerAccounts(CustomerAccessToken customerAccessToken)
     */
    @Test
    public void testGetCustomerAccountsCustomerAccessToken() throws Exception {
        //TODO check why list are not identical
        AccountInformationAccessRequest accountInformationAccessRequest = getAccountInformationAccessRequest();
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());
        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        pagingSpec.setLimit(50L);
        List<Account> accountsList = accountsService.getCustomerAccounts(generatedCustomerAccessToken, pagingSpec);
        System.out.println("***** FinancialInstitutionAccounts *****");
        financialInstitutionAccounts.stream().forEach(financialInstitutionAccount -> System.out.println(financialInstitutionAccount.getReference()));
        System.out.println("***** Accounts *****");
        accountsList.stream().forEach((account -> System.out.println(account.getReference())));
        assertTrue(financialInstitutionAccounts.size() == accountsList.size());
    }

    @Test
    public void testGetCustomerAccountsCustomerAccessTokenNoAccountsAuthorized() throws Exception {
        AccountInformationAccessRequest accountInformationAccessRequest = getAccountInformationAccessRequest();
        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        pagingSpec.setLimit(50L);
        List<Account> accountsList = accountsService.getCustomerAccounts(generatedCustomerAccessToken, pagingSpec);
        assertTrue(accountsList.isEmpty());
        assertFalse(financialInstitutionAccounts.size() == accountsList.size());
    }

    /**
     * Method: getCustomerAccounts(CustomerAccessToken customerAccessToken, IbanityPagingSpec pagingSpec)
     */
    @Test
    public void testGetCustomerAccountsForCustomerAccessTokenPagingSpec() throws Exception {
        AccountInformationAccessRequest accountInformationAccessRequest = getAccountInformationAccessRequest();
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());
        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        pagingSpec.setLimit(1L);
        List<Account> accountsList = accountsService.getCustomerAccounts(generatedCustomerAccessToken, pagingSpec);
        assertTrue(accountsList.size() == 1);
        Account account = accountsList.get(0);
        assertTrue(financialInstitutionAccounts.stream().filter(financialInstitutionAccount -> financialInstitutionAccount.getReference().equals(account.getReference())).collect(Collectors.toList()).size() == 1);
    }

    /**
     * Method: getCustomerAccounts(CustomerAccessToken customerAccessToken, UUID financialInstitutionId)
     */
    @Test
    public void testGetCustomerAccountsForCustomerAccessTokenFinancialInstitutionId() throws Exception {
        //TODO check why list is not equals
        AccountInformationAccessRequest accountInformationAccessRequest = getAccountInformationAccessRequest();
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());
        List<Account> accountsList = accountsService.getCustomerAccounts(generatedCustomerAccessToken, financialInstitution.getId());
        assertTrue(accountsList.size() == financialInstitutionAccounts.size());
    }

    @Test
    public void testGetCustomerAccountsForCustomerAccessTokenUnknownFinancialInstitutionId() throws Exception {
        assertThrows(ResourceNotFoundException.class, () -> accountsService.getCustomerAccounts(generatedCustomerAccessToken, UUID.randomUUID()));
    }

    /**
     * Method: getCustomerAccounts(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, IbanityPagingSpec pagingSpec)
     */
    @Test
    public void testGetCustomerAccountsForCustomerAccessTokenFinancialInstitutionIdPagingSpec() throws Exception {
        AccountInformationAccessRequest accountInformationAccessRequest = getAccountInformationAccessRequest();
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());
        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        pagingSpec.setLimit(1L);
        List<Account> accountsList = accountsService.getCustomerAccounts(generatedCustomerAccessToken, financialInstitution.getId(), pagingSpec);
        Account account = accountsList.get(0);
        assertTrue(financialInstitutionAccounts.stream().filter(financialInstitutionAccount -> financialInstitutionAccount.getReference().equals(account.getReference())).collect(Collectors.toList()).size() == 1);
    }

    @Test
    public void testGetCustomerAccountsForCustomerAccessTokenUnknownFinancialInstitutionIdPagingSpec() throws Exception {
        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        pagingSpec.setLimit(1L);
        assertThrows(ResourceNotFoundException.class, () -> accountsService.getCustomerAccounts(generatedCustomerAccessToken, UUID.randomUUID(), pagingSpec));
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
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());
        List<Account> accounts = accountsService.getCustomerAccounts(generatedCustomerAccessToken, financialInstitution.getId());
        assertTrue(accounts.size() > 0);
    }

    /**
     * Method: getAccountsInformationAccessAuthorizations(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID accountInformationAccessRequestId)
     */
    @Test
    public void testGetAccountsInformationAccessAuthorizationsForCustomerAccessTokenFinancialInstitutionIdAccountInformationAccessRequestId() throws Exception {
        //TODO CHECK WHY FAILING
        AccountInformationAccessRequest accountInformationAccessRequest = getAccountInformationAccessRequest();
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());
        List<AccountInformationAccessAuthorization> results = accountsService.getAccountsInformationAccessAuthorizations(generatedCustomerAccessToken, financialInstitution.getId(), accountInformationAccessRequest.getId());
        results.size();
    }

    /**
     * Method: getAccountsInformationAccessAuthorizations(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, UUID accountInformationAccessRequestId, IbanityPagingSpec pagingSpec)
     */
    @Test
    public void testGetAccountsInformationAccessAuthorizationsForCustomerAccessTokenFinancialInstitutionIdAccountInformationAccessRequestIdPagingSpec() throws Exception {
        //TODO check why failing
        AccountInformationAccessRequest accountInformationAccessRequest = getAccountInformationAccessRequest();
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());
        IbanityPagingSpec pagingSpec = new IbanityPagingSpec();
        pagingSpec.setLimit(1L);
        List<AccountInformationAccessAuthorization> authorisationsList = accountsService.getAccountsInformationAccessAuthorizations(generatedCustomerAccessToken, financialInstitution.getId(), accountInformationAccessRequest.getId(), pagingSpec);
        assertTrue(authorisationsList.size() == 1);
    }

    /**
     * Method: revokeAccountsAccessAuthorization(CustomerAccessToken customerAccessToken, UUID financialInstitutionId, AccountInformationAccessAuthorization accountInformationAccessAuthorization)
     */
    @Test
    public void testRevokeAccountsAccessAuthorization() throws Exception {
        //TODO Check why failing
        AccountInformationAccessRequest accountInformationAccessRequest = getAccountInformationAccessRequest();
        authorizeAccounts(accountInformationAccessRequest.getLinks().getRedirect());
        List<AccountInformationAccessAuthorization> authorizationsList = accountsService.getAccountsInformationAccessAuthorizations(generatedCustomerAccessToken, financialInstitution.getId(), accountInformationAccessRequest.getId());
        accountsService.revokeAccountsAccessAuthorization(generatedCustomerAccessToken, financialInstitution.getId(), authorizationsList.get(0));
        List<AccountInformationAccessAuthorization> updatedAuthorizationsList = accountsService.getAccountsInformationAccessAuthorizations(generatedCustomerAccessToken, financialInstitution.getId(), accountInformationAccessRequest.getId());
        assertTrue(authorizationsList.size() == updatedAuthorizationsList.size() + 1);
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

        List<String> iBanList = financialInstitutionAccounts.stream().map(financialInstitutionAccount -> financialInstitutionAccount.getReference()).collect(Collectors.toList());

        List<WebElement> uiAccountsList = driver.findElements(By.xpath("//input[@type='checkbox']"));
        uiAccountsList.stream()
                .filter(webElement -> iBanList.contains(webElement.getAttribute("value")))
                .forEach(webElement -> {
                    actions.moveToElement(webElement).click().build().perform();
                    takeScreenshot(webElement);
                })
                ;

        WebElement webElementSelectButton = driver.findElement(By.xpath("//button[text()='Select' and @type='submit']"));
        if (webElementSelectButton.isEnabled() && webElementSelectButton.isDisplayed()) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", webElementSelectButton);
        } else {
            throw new RuntimeException("Accounts 'Select' button not visible or not displayed");
        }

        WebElement acceptAccountsElement = driver.findElement(By.xpath("//button[text()='Accept']"));
        if (acceptAccountsElement.isEnabled() && acceptAccountsElement.isDisplayed()) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", acceptAccountsElement);
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.urlToBe(FAKE_TPP_ACCOUNT_INFORMATION_ACCESS_REDIRECT_URL));
        } else {
            throw new RuntimeException("Accounts selection final 'Accept' button not visible or not displayed");
        }
    }

    private void takeScreenshot(WebElement webElement) {
        TakesScreenshot screenshot = (TakesScreenshot) driver;

        Point point = webElement.getLocation();
        int eleWidth = webElement.getSize().getWidth();
        int eleHeight = webElement.getSize().getHeight();

        File src = screenshot.getScreenshotAs(OutputType.FILE);
        try {
            BufferedImage fullImg = ImageIO.read(src);
            ImageIO.write(fullImg, "png", src);
            org.apache.commons.io.FileUtils.copyFile(src, new File("/Users/daniel.deluca/Downloads/full.png"));
            BufferedImage eleScreenshot = fullImg.getSubimage(point.getX(), point.getY(),
                    eleWidth, eleHeight);
            ImageIO.write(eleScreenshot, "png", src);
            org.apache.commons.io.FileUtils.copyFile(src, new File("/Users/daniel.deluca/Downloads/"+webElement.getAttribute("value")+".png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
