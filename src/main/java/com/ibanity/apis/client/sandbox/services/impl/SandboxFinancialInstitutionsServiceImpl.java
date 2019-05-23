package com.ibanity.apis.client.sandbox.services.impl;

import com.ibanity.apis.client.models.FinancialInstitution;
import com.ibanity.apis.client.network.http.client.IbanityHttpClient;
import com.ibanity.apis.client.sandbox.models.factory.create.FinancialInstitutionCreationQuery;
import com.ibanity.apis.client.sandbox.models.factory.delete.FinancialInstitutionDeleteQuery;
import com.ibanity.apis.client.sandbox.models.factory.update.FinancialInstitutionUpdateQuery;
import com.ibanity.apis.client.sandbox.services.SandboxFinancialInstitutionsService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import com.ibanity.apis.client.services.impl.FinancialInstitutionsServiceImpl;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;

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
        FinancialInstitution financialInstitution = new FinancialInstitution();
        financialInstitution.setSandbox(Boolean.TRUE);
        financialInstitution.setName(financialInstitutionCreationQuery.getName());

        String url = getUrl("");
        String response = ibanityHttpClient.post(buildUri(url), financialInstitution);
        return mapResource(response, FinancialInstitution.class);
    }

    @Override
    public FinancialInstitution update(FinancialInstitutionUpdateQuery financialInstitutionUpdateQuery) {
        FinancialInstitution financialInstitution = new FinancialInstitution();
        financialInstitution.setId(financialInstitutionUpdateQuery.getFinancialInstitutionId());
        financialInstitution.setName(financialInstitutionUpdateQuery.getName());

        String url = getUrl(financialInstitutionUpdateQuery.getFinancialInstitutionId().toString());
        String response = ibanityHttpClient.post(buildUri(url), financialInstitution);
        return mapResource(response, FinancialInstitution.class);
    }

    @Override
    public FinancialInstitution delete(final FinancialInstitutionDeleteQuery financialInstitutionDeleteQuery) {
        String url = getUrl(financialInstitutionDeleteQuery.getFinancialInstitutionId().toString());
        String response = ibanityHttpClient.delete(buildUri(url));
        return mapResource(response, FinancialInstitution.class);
    }

    private String getUrl(String financialInstitutionId) {
        return apiUrlProvider.find("sandbox", "financialInstitutions")
                .replace(FinancialInstitution.API_URL_TAG_ID, financialInstitutionId);
    }
}
