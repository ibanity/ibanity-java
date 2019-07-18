package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.models.PaymentInitiationRequest;
import com.ibanity.apis.client.products.xs2a.models.create.PaymentInitiationRequestCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.links.FinancialInstitutionLinks;
import com.ibanity.apis.client.products.xs2a.models.links.PaymentInitiationAuthorizationLinks;
import com.ibanity.apis.client.products.xs2a.models.read.PaymentInitiationRequestReadQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadFile;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentInitiationRequestServiceImplTest {

    private static final UUID FINANCIAL_INSTITUTION_ID = UUID.fromString("9c6dad12-4927-4a40-af87-20f8ff4a4433");
    private static final UUID PAYMENT_INITIATION_REQUEST_ID = UUID.fromString("7fbdf174-325a-4c1a-808e-4eec951c1b4d");
    private static final String PIR_ENDPOINT = "https://api.ibanity.com/xs2a/customer/financial-institutions/{financialInstitutionId}/payment-initiation-requests/{paymentInitiationRequestId}";
    private static final String PIR_ENDPOINT_FOR_CREATE = "https://api.ibanity.com/xs2a/customer/financial-institutions/9c6dad12-4927-4a40-af87-20f8ff4a4433/payment-initiation-requests";
    private static final String REDIRECT_LINK = "https://callback.ibanity.com/sandbox/fi/pir/i?state=dmF1bHQ6djE6OWNXTHNwRkxoSk9vUjRoVk0rN1k2Z1N0SXBqNjNPNXdkSDZBTHpXaDVMZW9DSFEwTTBweVdpdFNXUlp2UC8wc0dId1NvVmVsT2lPaGxWK2J5SUlOb1JhbjlNUjBLei81UER2ZzNpcVF4TDRWUE5rUVMvRnhpUTFMZkhsT3ZHeDB2Uy9nLzhwRGd3VG1XTzkxR3pPSEUwQkxQR3YxS3AweDBpRzFaS0tiMVhlL3g5L1UzcEZkV1doMllVRWZ5bHZhMWJiUzAvTllXZUV0ZVhMYTJsT01YMFJ6UURTM3VOMDNvWE9nWlAzcmZrYUtiZENQMzFld1VVVDdHVjBhNWpQci9ZTmswUVVoWTBYVXZVY0VMVVRSdU9rM2psTnU0STQ2UzN5TlMxeCtybFFuVDNtQjN1OTk0aDdxbEk2OXcxbEdsbGlvUjZtR3pyZU0xR2psQlVzdG8zcmRNazlkK3lPK0hwS0tlbitRSkFEa2loS0kweklwWnBvK0dCdEFwSWNMakhUTEtCQ2pBaFgwQlRiSkhXZmFYVlR3a2NveUV1M05WSEZ6Qjg0cTNoQ1ZtN28xdlNaU0Mvd21TV2RRMWJGK0lTRWFXY2ZQQ1d2SGx1SjFmOVRJWGt3dzhFeGVROXRESWlMVUF5L0l3TTBWb2F4VWYyR0MrT1hMWkpzZkR0NGJ1YzdSdmRiVUFKaGhpSkhlZG0wVTMxN2JGTmRObXNLc3NLUVRSY2Rwb2NXaWh4YWxJS3hjTDVjNWpNNTRiaGVYNzVpTStGekZFTDk1MnQ2SVp5L2tITWJoUDNzVkN4d0M1SElpT29PUGp1a3BQbm1xVDRqWC8zQlNwVS8rbUhOOTBsSmVXK2c2Q3F0bWJ1MVNUWGsrQmxhNGZOZW9ETHFhbjM1Yk5TUEh2Y2xPdXRBWExKVjBSMXV6U3phMThXV3MrYU9vMUh5NE9OMEQxeklhK0tnS2Z1VU1JMXVLdXFBSGJHQ0hzOXZVcjRWRjQ0bEJ4WEZzTDlaM21KRDlXMkdzTEtJZzc2R2RubnFVTjUvOVhkZ1NZWXdnYU01b2pjanAyR0NseFozRjEyT0ZFTlN1c25ZUldtb3J1S1cxOUhVMENHYXlNeS80MDQ3b05qdUZnM0NlKy9QUk5ZNktqRVdUOXA0Q3d2b2Y5UGtXbnJYU2lLYkc1dGt6d3BMdzBSTWdSWnlIM2F4NHRxSHN3UDF2Vnh0SlVzMWhNMXpzS2VTMTRXeWRNc3QrU3c9PQ==";
    private static final String CUSTOMER_TOKEN_REFERENCE = "itsme";
    private static final String FINANCIAL_INSTITUTION_RELATED_LINK = "https://api.ibanity.com/xs2a/financial-institutions/9c6dad12-4927-4a40-af87-20f8ff4a4433";
    private static final String PIR_ENDPOINT_WITH_ID = "https://api.ibanity.com/xs2a/customer/financial-institutions/9c6dad12-4927-4a40-af87-20f8ff4a4433/payment-initiation-requests/7fbdf174-325a-4c1a-808e-4eec951c1b4d";

    @InjectMocks
    private PaymentInitiationRequestServiceImpl paymentInitiationRequestService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.Xs2a, "customer", "financialInstitution", "paymentInitiationRequests"))
                .thenReturn(PIR_ENDPOINT);
    }

    @Test
    void create() throws Exception {
        PaymentInitiationRequestCreationQuery requestCreationQuery =
                PaymentInitiationRequestCreationQuery.builder()
                        .customerAccessToken(CUSTOMER_TOKEN_REFERENCE)
                        .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                        .build();

        when(ibanityHttpClient.post(buildUri(PIR_ENDPOINT_FOR_CREATE), mapRequest(requestCreationQuery), emptyMap(), CUSTOMER_TOKEN_REFERENCE))
                .thenReturn(loadFile("json/createPir.json"));

        PaymentInitiationRequest actual = paymentInitiationRequestService.create(requestCreationQuery);
        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpectedForCreate());
    }

    @Test
    void delete() throws IOException {
        PaymentInitiationRequestReadQuery requestReadQuery =
                PaymentInitiationRequestReadQuery.builder()
                        .customerAccessToken(CUSTOMER_TOKEN_REFERENCE)
                        .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                        .paymentInitiationRequestId(PAYMENT_INITIATION_REQUEST_ID)
                        .build();

        when(ibanityHttpClient.delete(buildUri(PIR_ENDPOINT_WITH_ID), emptyMap(), CUSTOMER_TOKEN_REFERENCE))
                .thenReturn(loadFile("json/deletePir.json"));

        PaymentInitiationRequest actual = paymentInitiationRequestService.delete(requestReadQuery);
        assertThat(actual).isEqualToComparingFieldByField(createExpectedForDelete());
    }

    @Test
    void find() throws Exception {
        PaymentInitiationRequestReadQuery readQuery =
                PaymentInitiationRequestReadQuery.builder()
                        .paymentInitiationRequestId(PAYMENT_INITIATION_REQUEST_ID)
                        .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                        .customerAccessToken(CUSTOMER_TOKEN_REFERENCE)
                        .build();

        when(ibanityHttpClient.get(buildUri(PIR_ENDPOINT_WITH_ID), emptyMap(), CUSTOMER_TOKEN_REFERENCE))
                .thenReturn(loadFile("json/pir.json"));

        PaymentInitiationRequest actual = paymentInitiationRequestService.find(readQuery);

        assertThat(actual).isEqualToComparingFieldByField(createExpectedForFind());
    }

    private PaymentInitiationRequest createExpectedForDelete() {
        return PaymentInitiationRequest.builder()
                .id(PAYMENT_INITIATION_REQUEST_ID)
                .build();
    }

    private RequestApiModel mapRequest(PaymentInitiationRequestCreationQuery query) {
        PaymentInitiationRequest paymentInitiationRequest = PaymentInitiationRequest.builder()
                .financialInstitutionId(query.getFinancialInstitutionId())
                .amount(query.getAmount())
                .consentReference(query.getConsentReference())
                .creditorAccountReference(query.getCreditorAccountReference())
                .creditorAccountReferenceType(query.getCreditorAccountReferenceType())
                .creditorName(query.getCreditorName())
                .currency(query.getCurrency())
                .endToEndId(query.getEndToEndId())
                .productType(query.getProductType())
                .redirectUri(query.getRedirectUri())
                .remittanceInformation(query.getRemittanceInformation())
                .remittanceInformationType(query.getRemittanceInformationType())
                .creditorAgent(query.getCreditorAgent())
                .creditorAgentType(query.getCreditorAgentType())
                .debtorAccountReference(query.getDebtorAccountReference())
                .debtorAccountReferenceType(query.getDebtorAccountReferenceType())
                .debtorName(query.getDebtorName())
                .build();
        return RequestApiModel.builder()
                .data(
                        RequestApiModel.RequestDataApiModel.builder()
                                .attributes(paymentInitiationRequest)
                                .type(PaymentInitiationRequest.RESOURCE_TYPE)
                                .build()
                )
                .build();
    }

    private PaymentInitiationRequest createExpectedForFind() {
        return PaymentInitiationRequest.builder()
                .id(PAYMENT_INITIATION_REQUEST_ID)
                .consentReference("ad8f28f9-7ba1-4caf-9e7d-df2c04f88dd8")
                .productType("sepa-credit-transfer")
                .remittanceInformation("payment")
                .remittanceInformationType("unstructured")
                .requestedExecutionDate(LocalDate.parse("2020-02-05"))
                .currency("EUR")
                .amount(new BigDecimal("59"))
                .debtorName("Delmer Hermann")
                .debtorAccountReference("BE5283671456644082")
                .debtorAccountReferenceType("IBAN")
                .creditorName("Alex Creditor")
                .creditorAccountReference("BE4215561520006651")
                .creditorAccountReferenceType("IBAN")
                .creditorAgent("NBBEBEBB203")
                .creditorAgentType("BIC")
                .endToEndId("f4fdab3742af4a1386df4ca82c05ced6")
                .financialInstitutionLink(FinancialInstitutionLinks.builder().related(FINANCIAL_INSTITUTION_RELATED_LINK).build())
                .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                .build();
    }

    private PaymentInitiationRequest createExpectedForCreate() {
        return PaymentInitiationRequest.builder()
                .id(PAYMENT_INITIATION_REQUEST_ID)
                .consentReference("ad8f28f9-7ba1-4caf-9e7d-df2c04f88dd8")
                .productType("sepa-credit-transfer")
                .remittanceInformation("payment")
                .remittanceInformationType("unstructured")
                .requestedExecutionDate(LocalDate.parse("2020-02-05"))
                .currency("EUR")
                .amount(new BigDecimal("59"))
                .debtorName("Delmer Hermann")
                .debtorAccountReference("BE5283671456644082")
                .debtorAccountReferenceType("IBAN")
                .creditorName("Alex Creditor")
                .creditorAccountReference("BE4215561520006651")
                .creditorAccountReferenceType("IBAN")
                .creditorAgent("NBBEBEBB203")
                .creditorAgentType("BIC")
                .endToEndId("f4fdab3742af4a1386df4ca82c05ced6")
                .links(PaymentInitiationAuthorizationLinks.builder().redirect(REDIRECT_LINK).build())
                .financialInstitutionLink(FinancialInstitutionLinks.builder().related(FINANCIAL_INSTITUTION_RELATED_LINK).build())
                .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                .build();
    }
}