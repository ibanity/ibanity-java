package com.ibanity.apis.client.products.isabel_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.isabel_connect.CollectionApiModel;
import com.ibanity.apis.client.jsonapi.isabel_connect.DataApiModel;
import com.ibanity.apis.client.mappers.IsabelModelMapper;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.models.IsabelCollection;
import com.ibanity.apis.client.products.isabel_connect.models.Account;
import com.ibanity.apis.client.products.isabel_connect.models.Balance;
import com.ibanity.apis.client.products.isabel_connect.models.read.BalanceReadQuery;
import com.ibanity.apis.client.products.isabel_connect.models.read.IsabelPagingSpec;
import com.ibanity.apis.client.products.isabel_connect.services.BalanceService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import com.ibanity.apis.client.utils.IbanityUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.net.URI;
import java.util.stream.Collectors;

import static com.ibanity.apis.client.utils.URIHelper.buildUri;

public class BalanceServiceImpl implements BalanceService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public BalanceServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public IsabelCollection<Balance> list(BalanceReadQuery query) {
        IsabelPagingSpec pagingSpec = query.getPagingSpec();

        if (pagingSpec == null) {
            pagingSpec = IsabelPagingSpec.DEFAULT_PAGING_SPEC;
        }

        URI uri = buildUri(getUrl(query.getAccountId()), pagingSpec);
        HttpResponse response = ibanityHttpClient.get(uri, query.getAdditionalHeaders(), query.getAccessToken());
        return mapCollection(response);
    }

    private String getUrl(String accountId) {
        String url = apiUrlProvider
                .find(IbanityProduct.IsabelConnect, "accounts", "balances")
                .replace(Account.API_URL_TAG_ID, accountId);

        return StringUtils.removeEnd(url, "/");
    }

    private IsabelCollection<Balance> mapCollection(HttpResponse httpResponse) {
        try {
            String jsonPayload = IsabelModelMapper.readResponseContent(httpResponse.getEntity());
            CollectionApiModel collectionApiModel = IbanityUtils.objectMapper().readValue(jsonPayload, CollectionApiModel.class);
            String requestId = IsabelModelMapper.getRequestId(httpResponse);
            return IsabelCollection.<Balance>builder()
                    .requestId(requestId)
                    .pagingOffset(collectionApiModel.getPaginationOffset())
                    .pagingTotal(collectionApiModel.getPaginationTotal())
                    .items(
                            collectionApiModel
                                    .getData()
                                    .stream()
                                    .map(dataApiModel -> mapItem(dataApiModel))
                                    .collect(Collectors.toList())
                    )
                    .build();
        } catch (IOException exception) {
            throw new IllegalArgumentException("Response cannot be parsed", exception);
        }
    }

    private Balance mapItem(DataApiModel data) {
        return IbanityUtils.objectMapper().convertValue(data.getAttributes(), Balance.class);
    }
}
