package com.ibanity.apis.client;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.exceptions.IbanityException;
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
import com.ibanity.apis.client.services.AccountInformationAccessRequestsService;
import com.ibanity.apis.client.services.AccountsService;
import com.ibanity.apis.client.services.CustomerAccessTokensService;
import com.ibanity.apis.client.services.impl.AccountInformationAccessRequestsServiceImpl;
import com.ibanity.apis.client.services.impl.AccountsServiceImpl;
import com.ibanity.apis.client.services.impl.CustomerAccessTokensServiceImpl;
import com.ibanity.apis.client.utils.FileUtils;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ContainerInfo;
import com.spotify.docker.client.messages.HostConfig;
import org.apache.commons.lang3.StringUtils;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class AbstractServiceTest {
    protected static final String FAKE_TPP_ACCOUNT_INFORMATION_ACCESS_REDIRECT_URL  = IbanityConfiguration.getConfiguration().getString(IbanityConfiguration.IBANITY_PROPERTIES_PREFIX + "tpp.accounts.information.access.result.redirect.url");
    protected static final String FAKE_TPP_PAYMENT_INITIATION_REDIRECT_URL          = IbanityConfiguration.getConfiguration().getString(IbanityConfiguration.IBANITY_PROPERTIES_PREFIX + "tpp.payments.initiation.result.redirect.url");

    private static final String IBANITY_CLIENT_SSL_CA_CERTIFICATE_PATH_PROPERTY_KEY         = IbanityConfiguration.IBANITY_PROPERTIES_PREFIX + "client.ssl.ca.certificate.path";
    private static final String IBANITY_CLIENT_SANDBOX_AUTHORIZATION_HOSTNAME_PROPERTY_KEY  = IbanityConfiguration.IBANITY_PROPERTIES_PREFIX + "client.sandbox.authorization.hostname";

    private static final String IBANITY_SANDBOX_AUTHORIZATION_CLI_DOCKER_IMAGE      = "ibanity/sandbox-authorization-cli:latest";

    private static final String TEST_CASE                                           = AbstractServiceTest.class.getSimpleName();

    protected static final String ERROR_DATA_CODE_RESOURCE_NOT_FOUND                = "resourceNotFound";
    protected static final String ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND              = "The requested resource was not found.";
    protected static final String ERROR_DATA_META_RESOURCE_KEY                      = "resource";

    private static final String DOCKER_SANDBOX_AUTHORIZATION_CLI_POSITIVE_ANSWER    = "Your authorization has been submitted";

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractServiceTest.class);

    protected Instant now;

    protected String name;

    protected AccountInformationAccessRequestsService accountInformationAccessRequestsService   = new AccountInformationAccessRequestsServiceImpl();
    protected AccountsService accountsService                                                   = new AccountsServiceImpl();
    protected FinancialInstitutionAccountsService financialInstitutionAccountsService           = new FinancialInstitutionAccountsServiceImpl();
    protected FinancialInstitutionUsersService financialInstitutionUsersService                 = new FinancialInstitutionUsersServiceImpl();
    protected SandboxFinancialInstitutionsService sandboxFinancialInstitutionsService           = new SandboxFinancialInstitutionsServiceImpl();
    protected CustomerAccessTokensService customerAccessTokensService                           = new CustomerAccessTokensServiceImpl();

    protected CustomerAccessToken generatedCustomerAccessToken;

    protected FinancialInstitution financialInstitution;
    protected FinancialInstitutionUser financialInstitutionUser;
    protected List<FinancialInstitutionAccount> financialInstitutionAccounts = new ArrayList<>();

    protected void initPublicAPIEnvironment() throws Exception {
        generatedCustomerAccessToken = getCustomerAccessToken(UUID.randomUUID().toString());
        financialInstitution = createFinancialInstitution(null);
        financialInstitutionUser = createFinancialInstitutionUser(null);
        for (int index = 0; index < 5; index++) {
            financialInstitutionAccounts.add(createFinancialInstitutionAccount(financialInstitution, financialInstitutionUser.getId(), null));
        }
    }

    protected void cleanPublicAPIEnvironment() throws Exception {
        for (FinancialInstitutionAccount financialInstitutionAccount : financialInstitutionAccounts) {
            deleteFinancialInstitutionAccount(financialInstitution.getId(), financialInstitutionUser.getId(),financialInstitutionAccount.getId());
        }
        deleteFinancialInstitution(financialInstitution.getId());
        deleteFinancialInstitutionUser(financialInstitutionUser.getId());
    }

    public FinancialInstitution createFinancialInstitution(UUID idempotencyKey) {
        now = Instant.now();
        name = TEST_CASE + "-" + now.toString();
        if (idempotencyKey != null) {
            return sandboxFinancialInstitutionsService.create(name, idempotencyKey);
        } else {
            return sandboxFinancialInstitutionsService.create(name);
        }
    }

    protected FinancialInstitutionUser createFinancialInstitutionUser(UUID idempotencyKey) {
        Instant now = Instant.now();
        FinancialInstitutionUser financialInstitutionUser = new FinancialInstitutionUser();
        financialInstitutionUser.setFirstName("FirstName-"+now);
        financialInstitutionUser.setLastName("LastName-"+now);
        financialInstitutionUser.setLogin("Login-"+now);
        financialInstitutionUser.setPassword("Password-"+now);
        if (idempotencyKey == null) {
            return financialInstitutionUsersService.create(financialInstitutionUser.getLogin(), financialInstitutionUser.getPassword(), financialInstitutionUser.getLastName(), financialInstitutionUser.getFirstName());
        } else {
            return financialInstitutionUsersService.create(financialInstitutionUser.getLogin(), financialInstitutionUser.getPassword(), financialInstitutionUser.getLastName(), financialInstitutionUser.getFirstName(), idempotencyKey);
        }
    }


    protected void exitPublicApiEnvironment() throws Exception {
        for (FinancialInstitutionAccount financialInstitutionAccount : financialInstitutionAccounts) {
            deleteFinancialInstitutionAccount(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId());
        }
        deleteFinancialInstitutionUser(financialInstitutionUser.getId());
        deleteFinancialInstitution(financialInstitution.getId());
    }

    protected void deleteFinancialInstitutionUser(UUID financialInstitutionUserID) throws ApiErrorsException {
        financialInstitutionUsersService.delete(financialInstitutionUserID);
    }


    protected FinancialInstitutionAccount createFinancialInstitutionAccount(FinancialInstitution financialInstitution, UUID financialInstitutionUser, UUID idempotencyKey) throws ApiErrorsException {
        FinancialInstitutionAccount financialInstitutionAccount = new FinancialInstitutionAccount();
        financialInstitutionAccount.setSubType("checking");
        financialInstitutionAccount.setReference(Iban.random(CountryCode.BE).toString());
        financialInstitutionAccount.setReferenceType("IBAN");
        financialInstitutionAccount.setDescription("Checking Account");
        financialInstitutionAccount.setCurrency("EUR");
        financialInstitutionAccount.setFinancialInstitution(financialInstitution);
        if (idempotencyKey == null) {
            return financialInstitutionAccountsService.create(financialInstitution.getId(), financialInstitutionUser, financialInstitutionAccount);
        } else {
            return financialInstitutionAccountsService.create(financialInstitution.getId(), financialInstitutionUser, financialInstitutionAccount, idempotencyKey);
        }
    }

    protected void deleteFinancialInstitutionAccount(UUID financialInstitutionId, UUID financialInstitutionUserId, UUID financialInstitutionAccountId) throws ApiErrorsException {
        financialInstitutionAccountsService.delete(financialInstitutionId, financialInstitutionUserId, financialInstitutionAccountId);
    }

    protected AccountInformationAccessRequest getAccountInformationAccessRequest() {
        return accountInformationAccessRequestsService.createForFinancialInstitution(generatedCustomerAccessToken.getToken(), financialInstitution.getId(), FAKE_TPP_ACCOUNT_INFORMATION_ACCESS_REDIRECT_URL, UUID.randomUUID().toString());
    }

    protected void deleteFinancialInstitution(UUID financialInstitutionId) throws ApiErrorsException {
        sandboxFinancialInstitutionsService.deleteFinancialInstitution(financialInstitutionId);
    }

    protected CustomerAccessToken getCustomerAccessToken(String applicationCustomerReference){
        return customerAccessTokensService.create(applicationCustomerReference);
    }

    protected void authorizeAccounts(String redirectUrl) {

        String iBanList = financialInstitutionAccounts.stream().map(financialInstitutionAccount -> financialInstitutionAccount.getReference()).collect(Collectors.joining(","));
        String sslCAFilePath = null;
        String sandboxAuthorizationHostname = null;

        if (IbanityConfiguration.getConfiguration().containsKey(IBANITY_CLIENT_SSL_CA_CERTIFICATE_PATH_PROPERTY_KEY)) {
            FileUtils filesUtils = new FileUtils();
            sslCAFilePath = filesUtils.getFile(IbanityConfiguration.getConfiguration().getString(IBANITY_CLIENT_SSL_CA_CERTIFICATE_PATH_PROPERTY_KEY)).getPath();
        }

        if (IbanityConfiguration.getConfiguration().containsKey(IBANITY_CLIENT_SANDBOX_AUTHORIZATION_HOSTNAME_PROPERTY_KEY)) {
            sandboxAuthorizationHostname = IbanityConfiguration.getConfiguration().getString(IBANITY_CLIENT_SANDBOX_AUTHORIZATION_HOSTNAME_PROPERTY_KEY);
        }

        try {
            // Create a client based on DOCKER_HOST and DOCKER_CERT_PATH env vars
            final DockerClient docker = DefaultDockerClient.fromEnv().build();

            docker.pull(IBANITY_SANDBOX_AUTHORIZATION_CLI_DOCKER_IMAGE);
            List<String> cmdParameters = new ArrayList<>();

            cmdParameters.add("account-information-access");

            cmdParameters.add("-l");
            cmdParameters.add(financialInstitutionUser.getLogin());

            cmdParameters.add("-p");
            cmdParameters.add(financialInstitutionUser.getPassword());

            cmdParameters.add("-f");
            cmdParameters.add(financialInstitution.getId().toString());

            cmdParameters.add("-a");
            cmdParameters.add(iBanList);

            cmdParameters.add("-r");
            cmdParameters.add(redirectUrl);

            if (sslCAFilePath != null){
                cmdParameters.add("-s");
                cmdParameters.add(sslCAFilePath);
            }

            if (sandboxAuthorizationHostname != null) {
                cmdParameters.add("-o");
                cmdParameters.add(sandboxAuthorizationHostname);
            }

            final ContainerConfig containerConfig = ContainerConfig.builder()
                    .image(IBANITY_SANDBOX_AUTHORIZATION_CLI_DOCKER_IMAGE)
                    .hostConfig(HostConfig.builder().build())
                    .cmd(cmdParameters.toArray(new String[cmdParameters.size()]))
                    .build();

            final ContainerCreation creation = docker.createContainer(containerConfig);
            final String id = creation.id();

            docker.startContainer(id);

            ContainerInfo containerInfo = docker.inspectContainer(id);

            int loop = 10000;
            while (containerInfo.state().running() && loop > 0){
                containerInfo = docker.inspectContainer(id);
                loop --;
            }

            String logs = docker.logs(id, DockerClient.LogsParam.stdout(), DockerClient.LogsParam.stderr()).readFully();
            if (!logs.isEmpty() && !StringUtils.contains(logs, DOCKER_SANDBOX_AUTHORIZATION_CLI_POSITIVE_ANSWER)) {
                throw new IbanityException("Impossible to authorize accounts through docker image:"+logs+":");
            }

            if (containerInfo.state().running()) {
                docker.stopContainer(id,4);
            }
            docker.removeContainer(id);
            docker.close();
        } catch (IbanityException ibanityException) {
            LOGGER.error("IbanityException",ibanityException);
            throw ibanityException;
        } catch (Exception e) {
            LOGGER.error("Exception",e);
            throw new IbanityException("Error during account authorization process",e);
        }
    }
}
