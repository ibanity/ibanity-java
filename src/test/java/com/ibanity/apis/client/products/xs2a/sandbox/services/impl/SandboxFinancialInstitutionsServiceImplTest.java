package com.ibanity.apis.client.products.xs2a.sandbox.services.impl;

import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.network.http.client.IbanityHttpClient;
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
import java.util.UUID;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadFile;
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
                .thenReturn(loadFile("json/sandbox/financial_institution.json"));

        FinancialInstitution actual = sandboxFinancialInstitutionsService.create(query);

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpected());
    }

    @Test
    void update() throws Exception {
        FinancialInstitutionUpdateQuery query = FinancialInstitutionUpdateQuery.builder()
                .name("aName")
                .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                .build();

        when(ibanityHttpClient.post(new URI(API_ENDPOINT_WITH_ID), createRequest()))
                .thenReturn(loadFile("json/sandbox/financial_institution.json"));

        FinancialInstitution actual = sandboxFinancialInstitutionsService.update(query);

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpected());
    }

    @Test
    void delete() throws Exception {
        FinancialInstitutionDeleteQuery query = FinancialInstitutionDeleteQuery.builder()
                .financialInstitutionId(FINANCIAL_INSTITUTION_ID)
                .build();

        when(ibanityHttpClient.delete(new URI(API_ENDPOINT_WITH_ID)))
                .thenReturn(loadFile("json/sandbox/financial_institution.json"));

        FinancialInstitution actual = sandboxFinancialInstitutionsService.delete(query);

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpected());
    }

    private FinancialInstitution createExpected() {
        return FinancialInstitution.builder()
                .name("MetaBank")
                .sandbox(true)
                .id(FINANCIAL_INSTITUTION_ID)
                .build();
    }

    private RequestApiModel createRequest() {
        return RequestApiModel.builder()
                .data(RequestApiModel.RequestDataApiModel.builder()
                        .type(FinancialInstitution.RESOURCE_TYPE)
                        .attributes(FinancialInstitution.builder()
                                .name("aName")
                                .sandbox(true)
                                .build())
                        .build())
                .build();
    }
}