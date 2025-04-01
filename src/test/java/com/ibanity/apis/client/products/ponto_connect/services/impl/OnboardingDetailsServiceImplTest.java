package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.ponto_connect.models.OnboardingDetails;
import com.ibanity.apis.client.products.ponto_connect.models.create.OnboardingDetailsCreateQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadHttpResponse;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OnboardingDetailsServiceImplTest {

    private static final String ONBOARDING_DETAILS_ENDPOINT = "https://api.ibanity.com/ponto-connect/onboarding-details";
    private static final String ACCESS_TOKEN = "myToken";

    @InjectMocks
    private OnboardingDetailsServiceImpl onboardingDetailsService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(IbanityProduct.PontoConnect, "onboardingDetails"))
                .thenReturn(ONBOARDING_DETAILS_ENDPOINT);
    }

    @Test
    void create() throws URISyntaxException, IOException {
        OnboardingDetailsCreateQuery request = OnboardingDetailsCreateQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .addressCity("Brussels")
                .addressCountry("BE")
                .addressPostalCode("1000")
                .addressStreetAddress("123 Main St")
                .email("jsmith@example.com")
                .enterpriseNumber("0999999999")
                .firstName("Jo")
                .lastName("Smith")
                .organizationName("Smith Ltd")
                .phoneNumber("+32484000000")
                .vatNumber("BE0999999999")
                .initialFinancialInstitutionId(UUID.fromString("8ca9b992-f415-40d3-9624-dc459619b675"))
                .partnerReference("partner-reference")
                .automaticSubmissionOnCompletedForms(true)
                .preferredOtpMethod("sms")
                .requestedOrganisationId(UUID.fromString("9ca9b992-f415-40d3-9624-dc459619b675"))
                .organizationType("business")
                .skipFinancialInstitutionSelection(false)
                .build();

        when(ibanityHttpClient.post(eq(new URI(ONBOARDING_DETAILS_ENDPOINT)), any(),eq(emptyMap()), eq(ACCESS_TOKEN)))
                .thenReturn(loadHttpResponse("json/ponto-connect/create_onboarding_details.json"));

        OnboardingDetails actual = onboardingDetailsService.create(request);
        assertThat(actual).isEqualToComparingFieldByField(createOnboardingDetails());
    }

    private OnboardingDetails createOnboardingDetails() {
        return OnboardingDetails.builder()
                .id(UUID.fromString("df44642f-c38e-4e8c-9373-7f8e0b9f502f"))
                .addressCity("Brussels")
                .addressCountry("BE")
                .addressPostalCode("1000")
                .addressStreetAddress("123 Main St")
                .email("jsmith@example.com")
                .enterpriseNumber("0999999999")
                .firstName("Jo")
                .lastName("Smith")
                .organizationName("Smith Ltd")
                .phoneNumber("+32484000000")
                .vatNumber("BE0999999999")
                .initialFinancialInstitutionId(UUID.fromString("8ca9b992-f415-40d3-9624-dc459619b675"))
                .partnerReference("partner-reference")
                .automaticSubmissionOnCompletedForms(true)
                .preferredOtpMethod("sms")
                .requestedOrganisationId(UUID.fromString("9ca9b992-f415-40d3-9624-dc459619b675"))
                .organizationType("business")
                .skipFinancialInstitutionSelection(false)
                .build();
    }
}
