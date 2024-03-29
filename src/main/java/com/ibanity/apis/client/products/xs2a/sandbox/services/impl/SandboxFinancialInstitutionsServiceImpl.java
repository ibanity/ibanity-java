package com.ibanity.apis.client.products.xs2a.sandbox.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.create.FinancialInstitutionCreationQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.delete.FinancialInstitutionDeleteQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.update.FinancialInstitutionUpdateQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.services.SandboxFinancialInstitutionsService;
import com.ibanity.apis.client.products.xs2a.services.impl.FinancialInstitutionsServiceImpl;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.http.HttpResponse;

import java.util.List;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.mappers.ModelMapperHelper.buildRequest;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static org.apache.commons.lang3.StringUtils.removeEnd;

public class SandboxFinancialInstitutionsServiceImpl extends FinancialInstitutionsServiceImpl implements SandboxFinancialInstitutionsService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public SandboxFinancialInstitutionsServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        super(apiUrlProvider, ibanityHttpClient);
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public FinancialInstitution create(FinancialInstitutionCreationQuery financialInstitutionCreationQuery) {
        FinancialInstitution financialInstitution = FinancialInstitution.builder()
                .sandbox(Boolean.TRUE)
                .name(financialInstitutionCreationQuery.getName())
                .country(financialInstitutionCreationQuery.getCountry())
                .bic(financialInstitutionCreationQuery.getBic())
                .logoUrl(financialInstitutionCreationQuery.getLogoUrl())
                .authorizationModels(getAuthorizationModels(financialInstitutionCreationQuery))
                .sharedBrandReference(financialInstitutionCreationQuery.getSharedBrandReference())
                .sharedBrandName(financialInstitutionCreationQuery.getSharedBrandName())
                .financialInstitutionCustomerReferenceRequired(financialInstitutionCreationQuery.isFinancialInstitutionCustomerReferenceRequired())
                .timeZone(financialInstitutionCreationQuery.getTimeZone())
                .build();
        RequestApiModel request = buildRequest(FinancialInstitution.RESOURCE_TYPE, financialInstitution);

        String url = getSandboxUrl("");

        HttpResponse response = ibanityHttpClient.post(buildUri(url), request);
        return mapResource(response, FinancialInstitution.class);
    }

    @Override
    public FinancialInstitution update(FinancialInstitutionUpdateQuery financialInstitutionUpdateQuery) {
        FinancialInstitution financialInstitution = FinancialInstitution.builder()
                .name(financialInstitutionUpdateQuery.getName())
                .country(financialInstitutionUpdateQuery.getCountry())
                .bic(financialInstitutionUpdateQuery.getBic())
                .logoUrl(financialInstitutionUpdateQuery.getLogoUrl())
                .authorizationModels(null)
                .sandbox(true)
                .sharedBrandReference(financialInstitutionUpdateQuery.getSharedBrandReference())
                .sharedBrandName(financialInstitutionUpdateQuery.getSharedBrandName())
                .financialInstitutionCustomerReferenceRequired(financialInstitutionUpdateQuery.isFinancialInstitutionCustomerReferenceRequired())
                .timeZone(financialInstitutionUpdateQuery.getTimeZone())
                .build();
        RequestApiModel request = buildRequest(FinancialInstitution.RESOURCE_TYPE, financialInstitution);

        String url = getSandboxUrl(financialInstitutionUpdateQuery.getFinancialInstitutionId().toString());
        HttpResponse response = ibanityHttpClient.patch(buildUri(url), request);
        return mapResource(response, FinancialInstitution.class);
    }

    @Override
    public FinancialInstitution delete(final FinancialInstitutionDeleteQuery financialInstitutionDeleteQuery) {
        String url = getSandboxUrl(financialInstitutionDeleteQuery.getFinancialInstitutionId().toString());
        HttpResponse response = ibanityHttpClient.delete(buildUri(url));
        return mapResource(response, FinancialInstitution.class);
    }

    private List<String> getAuthorizationModels(FinancialInstitutionCreationQuery financialInstitutionCreationQuery) {
        return financialInstitutionCreationQuery.getAuthorizationModels().isEmpty() ? null : financialInstitutionCreationQuery.getAuthorizationModels();
    }

    private String getSandboxUrl(String financialInstitutionId) {
        return removeEnd(apiUrlProvider.find(IbanityProduct.Xs2a, "sandbox", "financialInstitutions")
                        .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId),
                "/");
    }
}
