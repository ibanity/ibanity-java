package com.ibanity.apis.client.products.xs2a.sandbox.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.delete.FinancialInstitutionUserDeleteQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.read.FinancialInstitutionUserReadQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.read.FinancialInstitutionUsersReadQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.update.FinancialInstitutionUserUpdateQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.services.FinancialInstitutionUsersService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.http.HttpResponse;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.*;
import static com.ibanity.apis.client.mappers.ModelMapperHelper.buildRequest;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static org.apache.commons.lang3.StringUtils.removeEnd;

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
        HttpResponse response = ibanityHttpClient.get(buildUri(url, usersReadQuery.getPagingSpec()));
        return mapCollection(response, FinancialInstitutionUser.class);

    }

    @Override
    public FinancialInstitutionUser find(FinancialInstitutionUserReadQuery userReadQuery) {
        String url = getUrl(userReadQuery.getFinancialInstitutionUserId().toString());
        HttpResponse response = ibanityHttpClient.get(buildUri(url));
        return mapResource(response, FinancialInstitutionUser.class);

    }

    @Override
    public FinancialInstitutionUser delete(FinancialInstitutionUserDeleteQuery userDeleteQuery) {
        String url = getUrl(userDeleteQuery.getFinancialInstitutionUserId().toString());
        HttpResponse response = ibanityHttpClient.delete(buildUri(url));
        return mapResource(response, FinancialInstitutionUser.class);
    }

    @Override
    public FinancialInstitutionUser create(FinancialInstitutionUserUpdateQuery userCreationQuery) {
        FinancialInstitutionUser financialInstitutionUser = mapRequest(userCreationQuery);
        String url = getUrl("");
        RequestApiModel request = buildRequest(FinancialInstitutionUser.RESOURCE_TYPE, financialInstitutionUser);
        HttpResponse response = ibanityHttpClient.post(buildUri(url), request);
        return mapResource(response, FinancialInstitutionUser.class);
    }

    @Override
    public FinancialInstitutionUser update(FinancialInstitutionUserUpdateQuery userUpdateQuery) {
        FinancialInstitutionUser financialInstitutionUser = mapRequest(userUpdateQuery);
        String url = getUrl(userUpdateQuery.getFinancialInstitutionUserId().toString());
        RequestApiModel request = buildRequest(FinancialInstitutionUser.RESOURCE_TYPE, financialInstitutionUser);
        HttpResponse response = ibanityHttpClient.patch(buildUri(url), request);
        return mapResource(response, FinancialInstitutionUser.class);
    }

    private FinancialInstitutionUser mapRequest(FinancialInstitutionUserUpdateQuery userUpdateQuery) {
        return FinancialInstitutionUser.builder()
                .login(userUpdateQuery.getLogin())
                .password(userUpdateQuery.getPassword())
                .firstName(userUpdateQuery.getFirstName())
                .lastName(userUpdateQuery.getLastName())
                .build();
    }

    private String getUrl(String financialInstitutionUserId) {
        return removeEnd(apiUrlProvider.find(IbanityProduct.Xs2a, "sandbox", "financialInstitutionUsers")
                        .replace(FinancialInstitutionUser.API_URL_TAG_ID, financialInstitutionUserId),
                "/");

    }
}
