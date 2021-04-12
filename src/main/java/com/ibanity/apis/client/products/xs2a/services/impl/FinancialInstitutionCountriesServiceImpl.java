package com.ibanity.apis.client.products.xs2a.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.CollectionApiModel;
import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.models.Collection;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitutionCountry;
import com.ibanity.apis.client.products.xs2a.models.read.FinancialInstitutionCountriesReadQuery;
import com.ibanity.apis.client.products.xs2a.services.FinancialInstitutionCountriesService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import com.ibanity.apis.client.utils.IbanityUtils;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.net.URI;
import java.util.stream.Collectors;

import static com.ibanity.apis.client.mappers.IbanityModelMapper.getRequestId;
import static com.ibanity.apis.client.mappers.IbanityModelMapper.readResponseContent;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static org.apache.commons.lang3.StringUtils.removeEnd;
import static org.apache.http.util.EntityUtils.consumeQuietly;

public class FinancialInstitutionCountriesServiceImpl implements FinancialInstitutionCountriesService {

    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public FinancialInstitutionCountriesServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public Collection<FinancialInstitutionCountry> list(FinancialInstitutionCountriesReadQuery financialInstitutionCountriesReadQuery) {
        IbanityPagingSpec pagingSpec =
                financialInstitutionCountriesReadQuery.getPagingSpec() == null
                        ? IbanityPagingSpec.DEFAULT_PAGING_SPEC : financialInstitutionCountriesReadQuery.getPagingSpec();

        URI uri = buildUri(getUrl(), pagingSpec);
        HttpResponse response = ibanityHttpClient.get(uri, financialInstitutionCountriesReadQuery.getAdditionalHeaders(), null);
        return mapCollection(response);
    }

    private String getUrl() {
        return removeEnd(apiUrlProvider.find(IbanityProduct.Xs2a, "financialInstitutionCountries"), "/");
    }

    private Collection<FinancialInstitutionCountry> mapCollection(HttpResponse httpResponse) {
        try {
            String jsonPayload = readResponseContent(httpResponse.getEntity());
            CollectionApiModel collectionApiModel = IbanityUtils.objectMapper().readValue(jsonPayload, CollectionApiModel.class);
            String requestId = getRequestId(httpResponse);
            return Collection.<FinancialInstitutionCountry>builder()
                    .requestId(requestId)
                    .pageLimit(collectionApiModel.getMeta().getPaging().getLimit())
                    .afterCursor(collectionApiModel.getMeta().getPaging().getAfter())
                    .beforeCursor(collectionApiModel.getMeta().getPaging().getBefore())
                    .firstLink(collectionApiModel.getLinks().getFirst())
                    .previousLink(collectionApiModel.getLinks().getPrev())
                    .nextLink(collectionApiModel.getLinks().getNext())
                    .items(
                            collectionApiModel.getData().stream()
                                    .map(this::mapFinancialInstitutionCountry)
                                    .peek(value -> {
                                        value.setRequestId(requestId);
                                    })
                                    .collect(Collectors.toList())
                    )
                    .build();
        } catch (IOException exception) {
            throw new IllegalArgumentException("Response cannot be parsed", exception);
        } finally {
            consumeQuietly(httpResponse.getEntity());
        }
    }

    private FinancialInstitutionCountry mapFinancialInstitutionCountry(DataApiModel dataApiModel) {
        return FinancialInstitutionCountry.builder()
                .id(dataApiModel.getId())
                .build();
    }
}
