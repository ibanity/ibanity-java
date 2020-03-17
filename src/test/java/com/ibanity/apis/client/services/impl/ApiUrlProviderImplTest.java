package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.UnsupportedEncodingException;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.HTTP;
import static java.net.URI.create;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;

@ExtendWith({MockitoExtension.class})
class ApiUrlProviderImplTest {

    public static final String API_ENDPOINT = "https://api.ibanity.localhost";
    public static final String PROXY_ENDPOINT = "http://myproxy.com";

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    private ApiUrlProviderImpl apiUrlProvider;


    @BeforeEach
    void setUp() throws UnsupportedEncodingException {
        lenient().when(ibanityHttpClient.get(eq(create(PROXY_ENDPOINT + "/xs2a")), eq(null))).thenReturn(getHttpResponse());
        lenient().when(ibanityHttpClient.get(eq(create(API_ENDPOINT + "/xs2a")), eq(null))).thenReturn(getHttpResponse());
    }

    private HttpResponse getHttpResponse() throws UnsupportedEncodingException {
        HttpResponse httpResponse = new BasicHttpResponse(HTTP, 200, null);
        httpResponse.setEntity(new StringEntity(getSchema()));
        return httpResponse;
    }

    @Test
    void find_whenProxyIsSetup() {
        apiUrlProvider = new ApiUrlProviderImpl(ibanityHttpClient, API_ENDPOINT, PROXY_ENDPOINT);
        String actual = apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financialInstitution", "accountInformationAccessRequest", "accounts");
        assertThat(actual).isEqualTo(PROXY_ENDPOINT + "/xs2a/customer/financial-institutions/{financialInstitutionId}/account-information-access-requests/{accountInformationAccessRequestId}/accounts");
    }

    @Test
    void find() {
        apiUrlProvider = new ApiUrlProviderImpl(ibanityHttpClient, API_ENDPOINT);
        String actual = apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financialInstitution", "accountInformationAccessRequest", "accounts");
        assertThat(actual).isEqualTo(API_ENDPOINT + "/xs2a/customer/financial-institutions/{financialInstitutionId}/account-information-access-requests/{accountInformationAccessRequestId}/accounts");
    }

    @Test
    void find_whenPathNotFound_throwIllegalArgumentException() {
        apiUrlProvider = new ApiUrlProviderImpl(ibanityHttpClient, API_ENDPOINT, PROXY_ENDPOINT);
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financiulInstitution", "accountInformationAccessRequest", "accounts"));
    }

    private String getSchema() {
        //language=JSON
        return "{\n" +
                "  \"links\": {\n" +
                "    \"customer\": {\n" +
                "      \"accounts\": \"https://api.ibanity.localhost/xs2a/customer/accounts\",\n" +
                "      \"financialInstitution\": {\n" +
                "        \"accountInformationAccessRequest\": {\n" +
                "          \"accounts\": \"https://api.ibanity.localhost/xs2a/customer/financial-institutions/{financialInstitutionId}/account-information-access-requests/{accountInformationAccessRequestId}/accounts\"\n" +
                "        },\n" +
                "        \"accountInformationAccessRequests\": \"https://api.ibanity.localhost/xs2a/customer/financial-institutions/{financialInstitutionId}/account-information-access-requests/{accountInformationAccessRequestId}\",\n" +
                "        \"accounts\": \"https://api.ibanity.localhost/xs2a/customer/financial-institutions/{financialInstitutionId}/accounts/{accountId}\",\n" +
                "        \"paymentInitiationRequests\": \"https://api.ibanity.localhost/xs2a/customer/financial-institutions/{financialInstitutionId}/payment-initiation-requests/{paymentInitiationRequestId}\",\n" +
                "        \"transactions\": \"https://api.ibanity.localhost/xs2a/customer/financial-institutions/{financialInstitutionId}/accounts/{accountId}/transactions/{transactionId}\"\n" +
                "      },\n" +
                "      \"financialInstitutions\": \"https://api.ibanity.localhost/xs2a/customer/financial-institutions\",\n" +
                "      \"self\": \"https://api.ibanity.localhost/xs2a/customer\",\n" +
                "      \"synchronizations\": \"https://api.ibanity.localhost/xs2a/customer/synchronizations/{synchronizationId}\"\n" +
                "    },\n" +
                "    \"customerAccessTokens\": \"https://api.ibanity.localhost/xs2a/customer-access-tokens\",\n" +
                "    \"financialInstitutions\": \"https://api.ibanity.localhost/xs2a/financial-institutions/{financialInstitutionId}\",\n" +
                "    \"sandbox\": {\n" +
                "      \"financialInstitution\": {\n" +
                "        \"financialInstitutionAccount\": {\n" +
                "          \"financialInstitutionTransactions\": \"https://api.ibanity.localhost/sandbox/financial-institutions/{financialInstitutionId}/financial-institution-users/{financialInstitutionUserId}/financial-institution-accounts/{financialInstitutionAccountId}/financial-institution-transactions/{financialInstitutionTransactionId}\"\n" +
                "        },\n" +
                "        \"financialInstitutionAccounts\": \"https://api.ibanity.localhost/sandbox/financial-institutions/{financialInstitutionId}/financial-institution-users/{financialInstitutionUserId}/financial-institution-accounts/{financialInstitutionAccountId}\"\n" +
                "      },\n" +
                "      \"financialInstitutionUsers\": \"https://api.ibanity.localhost/sandbox/financial-institution-users/{financialInstitutionUserId}\",\n" +
                "      \"financialInstitutions\": \"https://api.ibanity.localhost/sandbox/financial-institutions/{financialInstitutionId}\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }
}
