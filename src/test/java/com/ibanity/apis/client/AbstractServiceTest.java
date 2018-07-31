package com.ibanity.apis.client;

import com.ibanity.apis.client.exceptions.ApiErrorsException;
import com.ibanity.apis.client.exceptions.IbanityException;
import com.ibanity.apis.client.models.AbstractAccount;
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
import com.spotify.docker.client.exceptions.DockerRequestException;
import com.spotify.docker.client.exceptions.ImagePullFailedException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ContainerInfo;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.PortBinding;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.iban4j.CountryCode;
import org.iban4j.Iban;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ibanity.apis.client.configuration.IbanityConfiguration.getConfiguration;

public abstract class AbstractServiceTest {
    private static final String APACHE_WIRE_LOGGING_PACKAGE = "org.apache.http.wire";

    private static final String IBANITY_CLIENT_SSL_CA_CERTIFICATES_FOLDER_PROPERTY_KEY = "client.ssl.ca.certificates.folder";
    private static final String DOCKER_SANDBOX_AUTHORIZATION_CLI_POSITIVE_ANSWER    = "Your authorization has been submitted";

    private static final Logger LOGGER = LogManager.getLogger(AbstractServiceTest.class);

    private static final String IBANITY_CLIENT_DOCKER_EXTRAHOST_CALLBACK_NAME_PROPERTY_KEY                  = "client.docker.extrahost.callback.name";
    private static final String IBANITY_CLIENT_DOCKER_EXTRAHOST_CALLBACK_IP_PROPERTY_KEY                    = "client.docker.extrahost.callback.ip";
    private static final String IBANITY_CLIENT_DOCKER_EXTRAHOST_SANDBOX_AUTHORIZATION_NAME_PROPERTY_KEY     = "client.docker.extrahost.sandbox.authorization.name";
    private static final String IBANITY_CLIENT_DOCKER_EXTRAHOST_SANDBOX_AUTHORIZATION_IP_PROPERTY_KEY       = "client.docker.extrahost.sandbox.authorization.ip";

    private static final String IBANITY_SANDBOX_AUTHORIZATION_CLI_DOCKER_IMAGE      = "ibanity/sandbox-authorization-cli:latest";
    private static final String IBANITY_SANDBOX_AUTHORIZATION_CLI_DOCKER_VOLUME     = "/usr/local/share/ca-certificates";

    private static final String TEST_CASE                                           = AbstractServiceTest.class.getSimpleName();

    private static boolean DOCKER_IMAGE_PULL_SUCCESS = false;

    protected static final String FAKE_TPP_ACCOUNT_INFORMATION_ACCESS_REDIRECT_URL      = getConfiguration("tpp.accounts.information.access.result.redirect.url");
    protected static final String FAKE_TPP_PAYMENT_INITIATION_REDIRECT_URL              = getConfiguration("tpp.payments.initiation.result.redirect.url");

    protected static final String ERROR_DATA_CODE_RESOURCE_NOT_FOUND                = "resourceNotFound";
    protected static final String ERROR_DATA_DETAIL_RESOURCE_NOT_FOUND              = "The requested resource was not found.";
    protected static final String ERROR_DATA_META_RESOURCE_KEY                      = "resource";

    protected Instant now;

    protected String name;

    protected final AccountInformationAccessRequestsService accountInformationAccessRequestsService   = new AccountInformationAccessRequestsServiceImpl();
    protected final AccountsService accountsService                                                   = new AccountsServiceImpl();
    protected final FinancialInstitutionAccountsService financialInstitutionAccountsService           = new FinancialInstitutionAccountsServiceImpl();
    protected final FinancialInstitutionUsersService financialInstitutionUsersService                 = new FinancialInstitutionUsersServiceImpl();
    protected final SandboxFinancialInstitutionsService sandboxFinancialInstitutionsService           = new SandboxFinancialInstitutionsServiceImpl();
    protected final CustomerAccessTokensService customerAccessTokensService                           = new CustomerAccessTokensServiceImpl();

    protected CustomerAccessToken generatedCustomerAccessToken;

    protected FinancialInstitution financialInstitution;
    protected FinancialInstitutionUser financialInstitutionUser;
    protected final List<FinancialInstitutionAccount> financialInstitutionAccounts = new ArrayList<>();

    protected void initPublicAPIEnvironment() {
        generatedCustomerAccessToken = getCustomerAccessToken(UUID.randomUUID().toString());
        financialInstitution = createFinancialInstitution(null);
        financialInstitutionUser = createFinancialInstitutionUser(null);
        for (int index = 0; index < 5; index++) {
            financialInstitutionAccounts.add(createFinancialInstitutionAccount(financialInstitution, financialInstitutionUser.getId(), null));
        }
    }

    protected void cleanPublicAPIEnvironment() {
        for (FinancialInstitutionAccount financialInstitutionAccount : financialInstitutionAccounts) {
            deleteFinancialInstitutionAccount(financialInstitution.getId(), financialInstitutionUser.getId(),financialInstitutionAccount.getId());
        }
        deleteFinancialInstitution(financialInstitution.getId());
        deleteFinancialInstitutionUser(financialInstitutionUser.getId());
    }

    protected FinancialInstitution createFinancialInstitution(final UUID idempotencyKey) {
        now = Instant.now();
        name = TEST_CASE + "-" + now.toString();
        if (idempotencyKey != null) {
            return sandboxFinancialInstitutionsService.create(name, idempotencyKey);
        } else {
            return sandboxFinancialInstitutionsService.create(name);
        }
    }

    protected FinancialInstitutionUser createFinancialInstitutionUser(final UUID idempotencyKey) {
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


    protected void exitPublicApiEnvironment() {
        for (FinancialInstitutionAccount financialInstitutionAccount : financialInstitutionAccounts) {
            deleteFinancialInstitutionAccount(financialInstitution.getId(), financialInstitutionUser.getId(), financialInstitutionAccount.getId());
        }
        deleteFinancialInstitutionUser(financialInstitutionUser.getId());
        deleteFinancialInstitution(financialInstitution.getId());
    }

    protected void deleteFinancialInstitutionUser(final UUID financialInstitutionUserID) throws ApiErrorsException {
        financialInstitutionUsersService.delete(financialInstitutionUserID);
    }


    protected FinancialInstitutionAccount createFinancialInstitutionAccount(final FinancialInstitution financialInstitution, final UUID financialInstitutionUser, final UUID idempotencyKey) throws ApiErrorsException {
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

    protected void deleteFinancialInstitutionAccount(final UUID financialInstitutionId, final UUID financialInstitutionUserId, final UUID financialInstitutionAccountId) throws ApiErrorsException {
        financialInstitutionAccountsService.delete(financialInstitutionId, financialInstitutionUserId, financialInstitutionAccountId);
    }

    protected AccountInformationAccessRequest getAccountInformationAccessRequest() {
        return accountInformationAccessRequestsService.create(generatedCustomerAccessToken.getToken(), financialInstitution.getId(), FAKE_TPP_ACCOUNT_INFORMATION_ACCESS_REDIRECT_URL, UUID.randomUUID().toString());
    }

    protected void deleteFinancialInstitution(final UUID financialInstitutionId) throws ApiErrorsException {
        sandboxFinancialInstitutionsService.delete(financialInstitutionId);
    }

    protected CustomerAccessToken getCustomerAccessToken(final String applicationCustomerReference){
        return customerAccessTokensService.create(applicationCustomerReference);
    }

    protected void authorizeAccounts(final String redirectUrl) {

        String iBanList = financialInstitutionAccounts.stream().map(AbstractAccount::getReference).collect(Collectors.joining(","));
        String sslCAFilesPath = null;
        String sandboxAuthorizationHostname = null;

        if (getConfiguration(IBANITY_CLIENT_SSL_CA_CERTIFICATES_FOLDER_PROPERTY_KEY) != null) {
            FileUtils filesUtils = new FileUtils();
            sslCAFilesPath = filesUtils.getFile(getConfiguration(IBANITY_CLIENT_SSL_CA_CERTIFICATES_FOLDER_PROPERTY_KEY)).getPath();
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

            if (!DOCKER_IMAGE_PULL_SUCCESS) {
                int maxDockerPullRetry = 10;
                while (maxDockerPullRetry-- > 0) {
                    try {
                        LOGGER.info("Pulling docker image " + IBANITY_SANDBOX_AUTHORIZATION_CLI_DOCKER_IMAGE + " for the test...");
                        docker.pull(IBANITY_SANDBOX_AUTHORIZATION_CLI_DOCKER_IMAGE);
                        DOCKER_IMAGE_PULL_SUCCESS = true;
                    } catch (DockerRequestException dockerRequestException) {
                        LOGGER.warn("Failed to pull docker image " + dockerRequestException.getResponseBody() + ". Retrying...");
                    }
                }
                if (maxDockerPullRetry == 0) {
                    throw new ImagePullFailedException(IBANITY_SANDBOX_AUTHORIZATION_CLI_DOCKER_IMAGE,
                            "Failed to pull docker image after 10 retries");
                }
            }

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

            docker.startContainer(id);

            ContainerInfo containerInfo = docker.inspectContainer(id);

            int loop = 10000;
            while (containerInfo.state().running() && loop > 0){
                containerInfo = docker.inspectContainer(id);
                loop --;
            }

            String logs = docker.logs(id, DockerClient.LogsParam.stdout(), DockerClient.LogsParam.stderr()).readFully();
            if (!logs.isEmpty() && !StringUtils.contains(logs, DOCKER_SANDBOX_AUTHORIZATION_CLI_POSITIVE_ANSWER)) {
                throw new IbanityException("Failed to authorize accounts through docker image:" + logs + ":");
            }

            if (containerInfo.state().running()) {
                docker.stopContainer(id,4);
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
}
