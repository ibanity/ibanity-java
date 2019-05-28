package com.ibanity.apis.client.products.xs2a.sandbox.services.impl;

import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.network.http.client.IbanityHttpClient;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.delete.FinancialInstitutionUserDeleteQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.read.FinancialInstitutionUserReadQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.read.FinancialInstitutionUsersReadQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.update.FinancialInstitutionUserUpdateQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.services.FinancialInstitutionUsersService;
import com.ibanity.apis.client.services.ApiUrlProvider;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.buildRequest;
import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapCollection;
import static com.ibanity.apis.client.mappers.IbanityModelMapper.mapResource;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;

public class FinancialInstitutionUsersServiceImpl implements FinancialInstitutionUsersService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public FinancialInstitutionUsersServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public IbanityCollection<FinancialInstitutionUser> list(FinancialInstitutionUsersReadQuery usersReadQuery) {
        String url = getUrl("");
        String response = ibanityHttpClient.get(buildUri(url, usersReadQuery.getPagingSpec()));
        return mapCollection(response, FinancialInstitutionUser.class);

    }

    @Override
    public FinancialInstitutionUser find(FinancialInstitutionUserReadQuery userReadQuery) {
        String url = getUrl(userReadQuery.getFinancialInstitutionUserId().toString());
        String response = ibanityHttpClient.get(buildUri(url));
        return mapResource(response, FinancialInstitutionUser.class);

    }

    @Override
    public FinancialInstitutionUser delete(FinancialInstitutionUserDeleteQuery userDeleteQuery) {
        String url = getUrl(userDeleteQuery.getFinancialInstitutionUserId().toString());
        String response = ibanityHttpClient.delete(buildUri(url));
        return mapResource(response, FinancialInstitutionUser.class);
    }

    @Override
    public FinancialInstitutionUser create(FinancialInstitutionUserUpdateQuery userCreationQuery) {
        FinancialInstitutionUser financialInstitutionUser = mapRequest(userCreationQuery);
        String url = getUrl("");
        RequestApiModel request = buildRequest(FinancialInstitutionUser.RESOURCE_TYPE, financialInstitutionUser);
        String response = ibanityHttpClient.post(buildUri(url), request);
        return mapResource(response, FinancialInstitutionUser.class);
    }

    @Override
    public FinancialInstitutionUser update(FinancialInstitutionUserUpdateQuery userUpdateQuery) {
        FinancialInstitutionUser financialInstitutionUser = mapRequest(userUpdateQuery);
        String url = getUrl(userUpdateQuery.getFinancialInstitutionUserId().toString());
        RequestApiModel request = buildRequest(FinancialInstitutionUser.RESOURCE_TYPE, financialInstitutionUser);
        String response = ibanityHttpClient.post(buildUri(url), request);
        return mapResource(response, FinancialInstitutionUser.class);
    }

    private FinancialInstitutionUser mapRequest(FinancialInstitutionUserUpdateQuery userUpdateQuery) {
        FinancialInstitutionUser financialInstitutionUser = new FinancialInstitutionUser();
        financialInstitutionUser.setId(userUpdateQuery.getFinancialInstitutionUserId());
        financialInstitutionUser.setLogin(userUpdateQuery.getLogin());
        financialInstitutionUser.setPassword(userUpdateQuery.getPassword());
        financialInstitutionUser.setFirstName(userUpdateQuery.getFirstName());
        financialInstitutionUser.setLastName(userUpdateQuery.getLastName());
        return financialInstitutionUser;
    }

    private String getUrl(String financialInstitutionUserId) {
        return apiUrlProvider.find(IbanityProduct.Xs2a, "sandbox", "financialInstitutionUsers")
                .replace(FinancialInstitutionUser.API_URL_TAG_ID, financialInstitutionUserId);
    }
}
