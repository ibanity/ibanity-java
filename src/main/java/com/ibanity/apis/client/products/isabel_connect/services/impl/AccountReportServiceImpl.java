package com.ibanity.apis.client.products.isabel_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.isabel_connect.CollectionApiModel;
import com.ibanity.apis.client.jsonapi.isabel_connect.DataApiModel;
import com.ibanity.apis.client.mappers.IsabelModelMapper;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.models.IsabelCollection;
import com.ibanity.apis.client.products.isabel_connect.models.AccountReport;
import com.ibanity.apis.client.products.isabel_connect.models.read.AccountReportsReadQuery;
import com.ibanity.apis.client.products.isabel_connect.models.read.IsabelPagingSpec;
import com.ibanity.apis.client.products.isabel_connect.services.AccountReportService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import com.ibanity.apis.client.utils.IbanityUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.stream.Collectors;

import static com.ibanity.apis.client.utils.URIHelper.buildUri;

public class AccountReportServiceImpl implements AccountReportService {
    private final ApiUrlProvider apiUrlProvider;

    public AccountReportServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    private final IbanityHttpClient ibanityHttpClient;

    @Override
    public IsabelCollection<AccountReport> list(AccountReportsReadQuery query) {
        IsabelPagingSpec pagingSpec = query.getPagingSpec();

        if (pagingSpec == null) {
            pagingSpec = IsabelPagingSpec.DEFAULT_PAGING_SPEC;
        }

        HttpResponse response = ibanityHttpClient.get(
                buildUri(getUrl(), pagingSpec),
                query.getAdditionalHeaders(),
                query.getAccessToken());

        return mapCollection(response);
    }

    private String getUrl() {
        return getUrl("");
    }

    private String getUrl(String accountReportId) {
        String url = apiUrlProvider
                .find(IbanityProduct.IsabelConnect, "account-reports")
                .replace(AccountReport.API_URL_TAG_ID, accountReportId);

        return StringUtils.removeEnd(url, "/");
    }

    private IsabelCollection<AccountReport> mapCollection(HttpResponse httpResponse) {
        try {
            String jsonPayload = IsabelModelMapper.readResponseContent(httpResponse.getEntity());
            CollectionApiModel collectionApiModel = IbanityUtils.objectMapper().readValue(jsonPayload, CollectionApiModel.class);
            String requestId = IsabelModelMapper.getRequestId(httpResponse);
            return IsabelCollection.<AccountReport>builder()
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

    private AccountReport mapItem(DataApiModel data) {
        return IbanityUtils.objectMapper().convertValue(data.getAttributes(), AccountReport.class);
    }
}
