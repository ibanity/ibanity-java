package com.ibanity.apis.client.services.impl;

import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.network.http.client.IbanityHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiUrlProviderImplTest {

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @InjectMocks
    private ApiUrlProviderImpl apiUrlProvider;

    @BeforeEach
    void setUp() {
        when(ibanityHttpClient.get(any(), eq(null))).thenReturn(getSchema());
    }

    @Test
    void find() {
        String actual = apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financialInstitution", "accountInformationAccessRequest", "accounts");
        assertThat(actual).isEqualTo("https://api.ibanity.localhost/xs2a/customer/financial-institutions/{financialInstitutionId}/account-information-access-requests/{accountInformationAccessRequestId}/accounts");
    }

    @Test
    void find_whenPathNotFound_throwIllegalArgumentException() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financiulInstitution", "accountInformationAccessRequest", "accounts");
        });
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