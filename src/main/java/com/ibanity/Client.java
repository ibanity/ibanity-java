package com.ibanity;

import com.ibanity.api.CustomerAccessTokensService;
import com.ibanity.api.FinancialInstitutionsService;
import com.ibanity.api.configuration.IbanityConfiguration;
import com.ibanity.api.impl.CustomerAccessTokensServiceImpl;
import com.ibanity.api.impl.FinancialInstitutionsServiceImpl;
import com.ibanity.exceptions.ResourceNotFoundException;
import com.ibanity.models.AccountInformationAccessRequest;
import com.ibanity.models.CustomerAccessToken;
import com.ibanity.models.FinancialInstitution;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class Client {
    private static final Logger LOGGER = LogManager.getLogger(Client.class);

    private static final String IBANITY_API_ENDPOINT = IbanityConfiguration.getConfiguration().getString(IbanityConfiguration.IBANITY_PROPERTIES_PREFIX + "api.endpoint");

    //Don't forget to configure the REDIRECT URL in your Application configuration on the iBanity Developper Portal
    private static final String FAKE_TPP_ACCOUNT_INFORMATION_ACCESS_REDIRECT_URL = "https://faketpp.com/account-access-granted";

    private FinancialInstitutionsService financialInstitutionsService = new FinancialInstitutionsServiceImpl();
    private CustomerAccessTokensService customerAccessTokensService = new CustomerAccessTokensServiceImpl();

    public static void main(String[] args){
        Client client  = new Client();
        client.startFlow1();
    }

    public void startFlow1(){
        AtomicReference<FinancialInstitution> inUseFinancialInstitution = new AtomicReference();
        financialInstitutionsService.getFinancialInstitutions().stream().forEach(financialInstitution -> {
                                                            inUseFinancialInstitution.set(financialInstitution);
                                                            LOGGER.debug(financialInstitution.toString());}
                                                            );
        LOGGER.debug("Start : Customer Access Token Request");
        CustomerAccessToken customerAccessToken = new CustomerAccessToken("application_customer_reference");
        CustomerAccessToken generatedCustomerAccessToken = customerAccessTokensService.createCustomerAccessToken(customerAccessToken);
        LOGGER.debug(generatedCustomerAccessToken);
        LOGGER.debug("End : Customer Access Token Request");

        LOGGER.debug("Start : Account Information Access Request");
        AccountInformationAccessRequest accountInformationAccessRequest = new AccountInformationAccessRequest();
        accountInformationAccessRequest.setConsentReference(UUID.randomUUID().toString());
        accountInformationAccessRequest.setRedirectUri(FAKE_TPP_ACCOUNT_INFORMATION_ACCESS_REDIRECT_URL);
        AccountInformationAccessRequest resultingAccountInformationAccessRequest = financialInstitutionsService.getAccountInformationAccessRedirectUrl(generatedCustomerAccessToken, inUseFinancialInstitution.get().getId(), accountInformationAccessRequest);
        LOGGER.debug("Account Information Access Request: End-User to be redirected to:"+resultingAccountInformationAccessRequest.getLinks().getRedirect());
        LOGGER.debug("End : Account Information Access Request");
    }

    public void getFinancialInstitutions() {
        financialInstitutionsService.getFinancialInstitutions().stream().forEach(financialInstitution -> LOGGER.debug(financialInstitution.toString()));
    }

    public void getFinancialInstitution(UUID id){
        try {
            LOGGER.debug(financialInstitutionsService.getFinancialInstitution(id).toString());
        } catch (ResourceNotFoundException e) {
            LOGGER.debug(e.getMessage());
        }
    }

    public void getCustomerAccessToken(){
        CustomerAccessToken customerAccessToken = new CustomerAccessToken("application_customer_reference");
        LOGGER.debug(customerAccessTokensService.createCustomerAccessToken(customerAccessToken));
    }
}
