package com.ibanity.apis.client;

import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.exceptions.IbanityException;
import com.ibanity.apis.client.models.Account;
import com.ibanity.apis.client.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.models.factory.create.AccountInformationAccessRequestCreationQuery;
import com.ibanity.apis.client.models.factory.create.CustomerAccessTokenCreationQuery;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionAccount;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.sandbox.models.factory.create.FinancialInstitutionAccountCreationQuery;
import com.ibanity.apis.client.sandbox.models.factory.create.FinancialInstitutionCreationQuery;
import com.ibanity.apis.client.sandbox.models.factory.create.FinancialInstitutionUserCreationQuery;
import com.ibanity.apis.client.sandbox.models.factory.delete.FinancialInstitutionAccountDeleteQuery;
import com.ibanity.apis.client.sandbox.models.factory.delete.FinancialInstitutionDeleteQuery;
import com.ibanity.apis.client.sandbox.models.factory.delete.FinancialInstitutionUserDeleteQuery;
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
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ContainerInfo;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.PortBinding;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ibanity.apis.client.configuration.IbanityConfiguration.getConfiguration;
import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractServiceTest {
    private static final String IBANITY_CLIENT_SSL_CA_CERTIFICATES_FOLDER_PROPERTY_KEY = "client.ssl.ca.certificates.folder";
    private static final String DOCKER_SANDBOX_AUTHORIZATION_CLI_POSITIVE_ANSWER    = "Your authorization has been submitted";

    private static final Logger LOGGER = LogManager.getLogger(AbstractServiceTest.class);

    private static final String IBANITY_CLIENT_DOCKER_EXTRAHOST_CALLBACK_NAME_PROPERTY_KEY                  = "client.docker.extrahost.callback.name";
    private static final String IBANITY_CLIENT_DOCKER_EXTRAHOST_CALLBACK_IP_PROPERTY_KEY                    = "client.docker.extrahost.callback.ip";
    private static final String IBANITY_CLIENT_DOCKER_EXTRAHOST_SANDBOX_AUTHORIZATION_NAME_PROPERTY_KEY     = "client.docker.extrahost.sandbox.authorization.name";
    private static final String IBANITY_CLIENT_DOCKER_EXTRAHOST_SANDBOX_AUTHORIZATION_IP_PROPERTY_KEY       = "client.docker.extrahost.sandbox.authorization.ip";

    private static final String IBANITY_SANDBOX_AUTHORIZATION_CLI_DOCKER_IMAGE      = "ibanity/sandbox-authorization-cli:latest";
    private static final String IBANITY_SANDBOX_AUTHORIZATION_CLI_DOCKER_VOLUME     = "/usr/local/share/ca-certificates";
    private static final String APACHE_WIRE_LOGGING_PACKAGE = "org.apache.http.wire";

    protected static final String TEST_CASE                                           = AbstractServiceTest.class.getSimpleName();

    protected static final String ERROR_DATA_CODE_RESOURCE_NOT_FOUND                = "resourceNotFound";
    protected static final String ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND              = "The requested resource was not found.";
    protected static final String ERROR_DATA_META_RESOURCE_KEY                      = "resource";

    protected final AccountInformationAccessRequestsService accountInformationAccessRequestsService   = new AccountInformationAccessRequestsServiceImpl();
    protected final AccountsService accountsService                                                   = new AccountsServiceImpl(null, null);
    protected final FinancialInstitutionAccountsService financialInstitutionAccountsService           = new FinancialInstitutionAccountsServiceImpl();
    protected final FinancialInstitutionUsersService financialInstitutionUsersService                 = new FinancialInstitutionUsersServiceImpl();
    protected final SandboxFinancialInstitutionsService sandboxFinancialInstitutionsService           = new SandboxFinancialInstitutionsServiceImpl();
    protected final CustomerAccessTokensService customerAccessTokensService                           = new CustomerAccessTokensServiceImpl(null, null);

    protected final String fakeTppAccountInformationAccessRedirectUrl = getConfiguration("tpp.accounts.information.access.result.redirect.url");
    protected final String fakeTppPaymentInitiationRedirectUrl = getConfiguration("tpp.payments.initiation.result.redirect.url");

    protected List<FinancialInstitutionAccount> financialInstitutionAccounts;
    protected CustomerAccessToken generatedCustomerAccessToken;
    protected FinancialInstitution financialInstitution;
    protected FinancialInstitutionUser financialInstitutionUser;

    @BeforeEach
    public void beforeEach(TestInfo testInfo){
        LOGGER.info("Testing {}", testInfo.getTestClass().get().getSimpleName()
                        + "."
                        + testInfo.getTestMethod().get().getName());
    }

    protected void initPublicAPIEnvironment() {
        generatedCustomerAccessToken = createCustomerAccessToken(UUID.randomUUID().toString());
        financialInstitution = createFinancialInstitution();
        financialInstitutionUser = createFinancialInstitutionUser();
        financialInstitutionAccounts = new ArrayList<>();
        for (int index = 0; index < 5; index++) {
            financialInstitutionAccounts.add(
                    createFinancialInstitutionAccount(
                            financialInstitution, financialInstitutionUser.getId()));
        }
    }

    protected void cleanPublicAPIEnvironment() {
        for (FinancialInstitutionAccount financialInstitutionAccount : financialInstitutionAccounts) {
            deleteFinancialInstitutionAccount(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId());
        }
        deleteFinancialInstitution(financialInstitution.getId());
        deleteFinancialInstitutionUser(financialInstitutionUser.getId());
    }

    protected String generateFinancialInstitutionName() {
        return TEST_CASE + "-" + Instant.now().toString();
    }

    protected FinancialInstitution createFinancialInstitution() {
        return this.createFinancialInstitution((UUID) null);
    }

    protected FinancialInstitution createFinancialInstitution(final String name) {
        return this.createFinancialInstitution(name, null);
    }

    protected FinancialInstitution createFinancialInstitution(final UUID idempotencyKey) {
        return createFinancialInstitution(generateFinancialInstitutionName(), idempotencyKey);
    }

    protected FinancialInstitution createFinancialInstitution(final String name, final UUID idempotencyKey) {
        FinancialInstitutionCreationQuery financialInstitutionCreationQuery =
                FinancialInstitutionCreationQuery.builder()
                .name(name)
                .idempotencyKey(idempotencyKey)
                .build();

        return sandboxFinancialInstitutionsService.create(financialInstitutionCreationQuery);
    }

    protected FinancialInstitutionUser createFinancialInstitutionUser() {
        return this.createFinancialInstitutionUser(null);
    }

    protected FinancialInstitutionUser createFinancialInstitutionUser(final UUID idempotencyKey) {

        FinancialInstitutionUserCreationQuery userCreationQuery =
                FinancialInstitutionUserCreationQuery.builder()
                .login("Login-"+Instant.now())
                .password("Password")
                .lastName("LastName")
                .firstName("FirstName")
                .idempotencyKey(idempotencyKey)
                .build();

        return financialInstitutionUsersService.create(userCreationQuery);
    }

    protected void exitPublicApiEnvironment() {
        for (FinancialInstitutionAccount financialInstitutionAccount : financialInstitutionAccounts) {
            deleteFinancialInstitutionAccount(financialInstitution.getId(),
                    financialInstitutionUser.getId(), financialInstitutionAccount.getId());
        }
        deleteFinancialInstitutionUser(financialInstitutionUser.getId());
        deleteFinancialInstitution(financialInstitution.getId());
    }

    protected void deleteFinancialInstitutionUser(final UUID financialInstitutionUserID) {
        FinancialInstitutionUserDeleteQuery userDeleteQuery =
                FinancialInstitutionUserDeleteQuery.builder()
                        .financialInstitutionUserId(financialInstitutionUserID)
                        .build();

        financialInstitutionUsersService.delete(userDeleteQuery);
    }

    protected FinancialInstitutionAccount createFinancialInstitutionAccount(
            final FinancialInstitution financialInstitution, final UUID financialInstitutionUser) {
        return this.createFinancialInstitutionAccount(financialInstitution, financialInstitutionUser, null);
    }

    protected FinancialInstitutionAccount createFinancialInstitutionAccount(
            final FinancialInstitution financialInstitution, final UUID financialInstitutionUserId,
            final UUID idempotencyKey) {

        FinancialInstitutionAccountCreationQuery accountCreationQuery =
                FinancialInstitutionAccountCreationQuery.builder()
                        .subType("checking")
                        .reference(Iban.random(CountryCode.BE).toString())
                        .referenceType("IBAN")
                        .description("Checking Account")
                        .currency("EUR")
                        .financialInstitutionId(financialInstitution.getId())
                        .financialInstitutionUserId(financialInstitutionUserId)
                        .idempotencyKey(idempotencyKey)
                        .build();

        return financialInstitutionAccountsService.create(accountCreationQuery);
    }

    protected void deleteFinancialInstitutionAccount(final UUID financialInstitutionId,
                                                     final UUID financialInstitutionUserId,
                                                     final UUID financialInstitutionAccountId) {
        FinancialInstitutionAccountDeleteQuery accountDeleteQuery =
                FinancialInstitutionAccountDeleteQuery.builder()
                .financialInstitutionId(financialInstitutionId)
                .financialInstitutionUserId(financialInstitutionUserId)
                .financialInstitutionAccountId(financialInstitutionAccountId)
                .build();

        financialInstitutionAccountsService.delete(accountDeleteQuery);
    }

    protected AccountInformationAccessRequest createAccountInformationAccessRequest() {
        AccountInformationAccessRequestCreationQuery accountInformationAccessRequestCreationQuery =
                AccountInformationAccessRequestCreationQuery.builder()
                    .customerAccessToken(generatedCustomerAccessToken.getToken())
                    .financialInstitutionId(financialInstitution.getId())
                    .redirectURI(fakeTppAccountInformationAccessRedirectUrl)
                    .consentReference(UUID.randomUUID().toString())
                    .build();

        return accountInformationAccessRequestsService.create(accountInformationAccessRequestCreationQuery);
    }

    protected void deleteFinancialInstitution(final UUID financialInstitutionId) {
        FinancialInstitutionDeleteQuery financialInstitutionDeleteQuery =
                FinancialInstitutionDeleteQuery.builder()
                .financialInstitutionId(financialInstitutionId)
                .build();

        sandboxFinancialInstitutionsService.delete(financialInstitutionDeleteQuery);
    }

    protected CustomerAccessToken createCustomerAccessToken(final String applicationCustomerReference) {
        CustomerAccessTokenCreationQuery customerAccessTokenCreationQuery =
                CustomerAccessTokenCreationQuery.builder()
                        .applicationCustomerReference(applicationCustomerReference)
                        .build();

        return customerAccessTokensService.create(customerAccessTokenCreationQuery);
    }

    protected void authorizeAccounts(final String redirectUrl) {

        String iBanList = financialInstitutionAccounts.stream()
                .map(Account::getReference)
                .collect(Collectors.joining(","));
        String sslCAFilesPath = null;
        String sandboxAuthorizationHostname = null;

        if (getConfiguration(IBANITY_CLIENT_SSL_CA_CERTIFICATES_FOLDER_PROPERTY_KEY) != null) {
            sslCAFilesPath = new File((getConfiguration(IBANITY_CLIENT_SSL_CA_CERTIFICATES_FOLDER_PROPERTY_KEY))).getPath();
        }

        if (getConfiguration(IBANITY_CLIENT_DOCKER_EXTRAHOST_SANDBOX_AUTHORIZATION_NAME_PROPERTY_KEY) != null) {
            sandboxAuthorizationHostname = getConfiguration(IBANITY_CLIENT_DOCKER_EXTRAHOST_SANDBOX_AUTHORIZATION_NAME_PROPERTY_KEY);
        }

        try {
            // Disable Apache Http Client Wire logging during Docker pull to prevent tonnes of logs
            Logger apacheWireLogger = LogManager.getLogger(APACHE_WIRE_LOGGING_PACKAGE);
            Level currentLogLevel = apacheWireLogger.getLevel();
            Configurator.setLevel(APACHE_WIRE_LOGGING_PACKAGE, Level.ERROR);

            // Create a client based on DOCKER_HOST and DOCKER_CERT_PATH env vars
            final DockerClient docker = DefaultDockerClient.fromEnv().build();

            // Bind container ports to host ports
            final String[] ports = {"80", "22", "443"};
            final Map<String, List<PortBinding>> portBindings = new HashMap<>();
            for (String port : ports) {
                List<PortBinding> hostPorts = new ArrayList<>();
                hostPorts.add(PortBinding.of(getConfiguration(IBANITY_CLIENT_DOCKER_EXTRAHOST_CALLBACK_IP_PROPERTY_KEY), port));
                portBindings.put(port, hostPorts);
            }

            HostConfig hostConfig;
            if (getConfiguration(IBANITY_CLIENT_DOCKER_EXTRAHOST_CALLBACK_NAME_PROPERTY_KEY) != null
                    && getConfiguration(IBANITY_CLIENT_DOCKER_EXTRAHOST_SANDBOX_AUTHORIZATION_NAME_PROPERTY_KEY) != null
                    && getConfiguration(IBANITY_CLIENT_SSL_CA_CERTIFICATES_FOLDER_PROPERTY_KEY) != null) {
                hostConfig = HostConfig.builder()
                        .appendBinds(sslCAFilesPath + ":" + IBANITY_SANDBOX_AUTHORIZATION_CLI_DOCKER_VOLUME)
                        .extraHosts(
                                getConfiguration(IBANITY_CLIENT_DOCKER_EXTRAHOST_CALLBACK_NAME_PROPERTY_KEY) + ":" + getConfiguration(IBANITY_CLIENT_DOCKER_EXTRAHOST_CALLBACK_IP_PROPERTY_KEY)
                                , getConfiguration(IBANITY_CLIENT_DOCKER_EXTRAHOST_SANDBOX_AUTHORIZATION_NAME_PROPERTY_KEY) + ":" + getConfiguration(IBANITY_CLIENT_DOCKER_EXTRAHOST_SANDBOX_AUTHORIZATION_IP_PROPERTY_KEY))
                        .portBindings(portBindings).build();
            } else {
                hostConfig = HostConfig.builder().build();
            }

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

            if (sandboxAuthorizationHostname != null) {
                cmdParameters.add("-o");
                cmdParameters.add(sandboxAuthorizationHostname);
            }

            final ContainerConfig containerConfig = ContainerConfig.builder()
                    .hostConfig(hostConfig)
                    .image(IBANITY_SANDBOX_AUTHORIZATION_CLI_DOCKER_IMAGE)
                    .cmd(cmdParameters.toArray(new String[0]))
                    .build();

            final ContainerCreation creation = docker.createContainer(containerConfig);
            final String id = creation.id();

            while (true){
                docker.startContainer(id);

                ContainerInfo containerInfo = docker.inspectContainer(id);

                int loop = 100;
                while (containerInfo.state().running() && loop-- > 0) {
                    containerInfo = docker.inspectContainer(id);
                    Thread.sleep(500); //NOSONAR
                }

                String logs = docker.logs(id, DockerClient.LogsParam.stdout(), DockerClient.LogsParam.stderr()).readFully();
                if (!logs.isEmpty()) {
                    if (StringUtils.contains(logs, "SSL")) {
                        LOGGER.warn("Seems like an SSL error... Retrying. Logs is " + logs);
                        continue;
                    }
                    if (!StringUtils.contains(logs, DOCKER_SANDBOX_AUTHORIZATION_CLI_POSITIVE_ANSWER)) {
                        throw new IbanityException("Failed to authorize accounts through docker image:" + logs + ":");
                    }
                    LOGGER.info("Account authorization success");
                }

                if (containerInfo.state().running()) {
                    docker.stopContainer(id,4);
                }

                break;
            }
            docker.removeContainer(id);
            docker.close();

            Configurator.setLevel(APACHE_WIRE_LOGGING_PACKAGE, currentLogLevel);
        } catch (IbanityException ibanityException) {
            LOGGER.error("IbanityException",ibanityException);
            throw ibanityException;
        } catch (Exception e) {
            LOGGER.error("Exception",e);
            throw new IbanityException("Error during account authorization process",e);
        }
    }

    protected void assertResourceNotFoundException(ApiErrorsException apiErrorsException, String resourceType) {
        assertEquals(HttpStatus.SC_NOT_FOUND, apiErrorsException.getHttpStatus());
        assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getCode().equals(ERROR_DATA_CODE_RESOURCE_NOT_FOUND)).count());
        assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getDetail().equals(ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND)).count());
        // TODO: verify the resourceType
//        assertEquals(1, apiErrorsException.getErrorDatas().stream().filter(errorData -> errorData.getMeta().get(ERROR_DATA_META_RESOURCE_KEY).equals(FinancialInstitution.RESOURCE_TYPE)).count());
    }
}
