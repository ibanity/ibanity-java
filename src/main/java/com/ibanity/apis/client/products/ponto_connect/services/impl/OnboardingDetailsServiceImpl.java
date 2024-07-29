package com.ibanity.apis.client.products.ponto_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.ponto_connect.models.OnboardingDetails;
import com.ibanity.apis.client.products.ponto_connect.models.create.OnboardingDetailsCreateQuery;
import com.ibanity.apis.client.products.ponto_connect.services.OnboardingDetailsService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.http.HttpResponse;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.mappers.ModelMapperHelper.buildRequest;
import static com.ibanity.apis.client.products.ponto_connect.models.OnboardingDetails.RESOURCE_TYPE;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;

public class OnboardingDetailsServiceImpl implements OnboardingDetailsService {

    private ApiUrlProvider apiUrlProvider;
    private IbanityHttpClient ibanityHttpClient;

    public OnboardingDetailsServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public OnboardingDetails create(OnboardingDetailsCreateQuery onboardingDetailsCreateQuery) {

        RequestApiModel request = buildRequest(OnboardingDetails.RESOURCE_TYPE, toRequest(onboardingDetailsCreateQuery));
        HttpResponse response = ibanityHttpClient.post(buildUri(getUrl()), request, onboardingDetailsCreateQuery.getAdditionalHeaders(), onboardingDetailsCreateQuery.getAccessToken());

        return mapResource(response, OnboardingDetails.class);
    }

    private OnboardingDetails toRequest(OnboardingDetailsCreateQuery onboardingDetailsCreateQuery) {
        return OnboardingDetails.builder()
                .addressCity(onboardingDetailsCreateQuery.getAddressCity())
                .addressCountry(onboardingDetailsCreateQuery.getAddressCountry())
                .addressPostalCode(onboardingDetailsCreateQuery.getAddressPostalCode())
                .addressStreetAddress(onboardingDetailsCreateQuery.getAddressStreetAddress())
                .email(onboardingDetailsCreateQuery.getEmail())
                .enterpriseNumber(onboardingDetailsCreateQuery.getEnterpriseNumber())
                .firstName(onboardingDetailsCreateQuery.getFirstName())
                .lastName(onboardingDetailsCreateQuery.getLastName())
                .organizationName(onboardingDetailsCreateQuery.getOrganizationName())
                .phoneNumber(onboardingDetailsCreateQuery.getPhoneNumber())
                .vatNumber(onboardingDetailsCreateQuery.getVatNumber())
                .initialFinancialInstitutionId(onboardingDetailsCreateQuery.getInitialFinancialInstitutionId())
                .organizationType(onboardingDetailsCreateQuery.getOrganizationType())
                .partnerReference(onboardingDetailsCreateQuery.getPartnerReference())
                .automaticSubmissionOnCompletedForms(onboardingDetailsCreateQuery.getAutomaticSubmissionOnCompletedForms())
                .preferredOtpMethod(onboardingDetailsCreateQuery.getPreferredOtpMethod())
                .requestedOrganisationId(onboardingDetailsCreateQuery.getRequestedOrganisationId())
                .build();
    }

    private String getUrl() {
        return apiUrlProvider.find(IbanityProduct.PontoConnect, RESOURCE_TYPE);
    }
}
