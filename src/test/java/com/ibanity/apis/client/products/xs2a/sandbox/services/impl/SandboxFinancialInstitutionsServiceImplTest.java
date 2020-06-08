package com.ibanity.apis.client.products.xs2a.sandbox.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.create.FinancialInstitutionCreationQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.delete.FinancialInstitutionDeleteQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.update.FinancialInstitutionUpdateQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadHttpResponse;
import static com.ibanity.apis.client.models.IbanityProduct.Xs2a;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SandboxFinancialInstitutionsServiceImplTest {

    private static final UUID FINANCIAL_INSTITUTION_ID = fromString("1c57b438-6a38-437f-8130-a186f547c6b7");

    private static final String API_ENDPOINT = "https://api.ibanity.com/sandbox/financial-institutions";
    private static final String API_SCHEMA_ENDPOINT = "https://api.ibanity.com/sandbox/financial-institutions/{financialInstitutionId}";
    private static final String API_ENDPOINT_WITH_ID = API_ENDPOINT + "/" + FINANCIAL_INSTITUTION_ID;
    private static final List<String> AUTHORIZATION_MODELS = newArrayList("detailed", "financialInstitutionOffered");

    @InjectMocks
    private SandboxFinancialInstitutionsServiceImpl sandboxFinancialInstitutionsService;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(Xs2a, "sandbox", "financialInstitutions")).thenReturn(API_SCHEMA_ENDPOINT);
    }

    @Test
    void create() throws Exception {
        FinancialInstitutionCreationQuery query = FinancialInstitutionCreationQuery.builder()
                .name("aName")
                .build();

        when(ibanityHttpClient.post(new URI(API_ENDPOINT), createRequest()))
                .thenReturn(loadHttpResponse("json/sandbox/financial_institution.json"));

        FinancialInstitution actual = sandboxFinancialInstitutionsService.create(query);

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpected());
    }

    @Test
    void createWithAuthorizationModels() throws Exception {
        FinancialInstitutionCreationQuery query = FinancialInstitutionCreationQuery.builder()
                .authorizationModels(AUTHORIZATION_MODELS)
                .name("aName")
                .build();

        when(ibanityHttpClient.post(new URI(API_ENDPOINT), createRequest(AUTHORIZATION_MODELS)))
                .thenReturn(loadHttpResponse("json/sandbox/financial_institution.json"));

        FinancialInstitution actual = sandboxFinancialInstitutionsService.create(query);

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpected());
    }

    @Test
    void update() throws Exception {
        FinancialInstitutionUpdateQuery query = FinancialInstitutionUpdateQuery.builder()
                .name("aName")
                .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                .build();

        when(ibanityHttpClient.patch(new URI(API_ENDPOINT_WITH_ID), createRequest()))
                .thenReturn(loadHttpResponse("json/sandbox/financial_institution.json"));

        FinancialInstitution actual = sandboxFinancialInstitutionsService.update(query);

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpected());
    }

    @Test
    void delete() throws Exception {
        FinancialInstitutionDeleteQuery query = FinancialInstitutionDeleteQuery.builder()
                .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                .build();

        when(ibanityHttpClient.delete(new URI(API_ENDPOINT_WITH_ID)))
                .thenReturn(loadHttpResponse("json/sandbox/financial_institution.json"));

        FinancialInstitution actual = sandboxFinancialInstitutionsService.delete(query);

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpected());
    }

    private FinancialInstitution createExpected() {
        return FinancialInstitution.builder()
                .name("MetaBank")
                .authorizationModels(AUTHORIZATION_MODELS)
                .sandbox(true)
                .id(FINANCIAL_INSTITUTION_ID)
                .build();
    }

    private RequestApiModel createRequest() {
        return createRequest(null);
    }

    private RequestApiModel createRequest(List<String> authorizationModels) {
        return RequestApiModel.builder()
                .data(RequestApiModel.RequestDataApiModel.builder()
                        .type(FinancialInstitution.RESOURCE_TYPE)
                        .attributes(FinancialInstitution.builder()
                                .authorizationModels(authorizationModels)
                                .name("aName")
                                .sandbox(true)
                                .build())
                        .build())
                .build();
    }
}
