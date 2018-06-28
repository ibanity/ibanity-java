package com.ibanity.apis.client.services.impl;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ibanity.apis.client.mappers.IbanityExceptionMapper;
import com.ibanity.apis.client.models.AbstractModel;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.network.http.client.IbanityHttpAdapterListener;
import io.crnk.client.CrnkClient;
import io.crnk.client.http.HttpAdapter;
import io.crnk.client.http.apache.HttpClientAdapter;
import io.crnk.core.boot.CrnkProperties;
import io.crnk.core.module.SimpleModule;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;

import java.util.UUID;

public abstract class AbstractServiceImpl {
    protected static final String FINANCIAL_INSTITUTIONS_PATH               = "financial-institutions";
    protected static final String FINANCIAL_INSTITUTION_ID_TAG              = "<FI_ID>";
    protected static final String ACCOUNT_ID_TAG                            = "<ACCOUNT_ID>";
    protected static final String USER_ID_TAG                               = "<USER_ID>";
    protected static final String ACCOUNT_INFORMATION_ACCESS_REQUEST_ID_TAG = "<ACCOUNT_INFORMATION_ACCESS_REQUEST_ID>";


    public AbstractServiceImpl() {
    }

    protected synchronized CrnkClient getApiClient(final String path) {
        return getApiClient(path, null, null);
    }

    protected <T extends AbstractModel> ResourceList<T> findAll(final QuerySpec querySpec, final ResourceRepositoryV2<T, UUID> repository) {
        return repository.findAll(querySpec);
    }

    protected CrnkClient getApiClient(final String path, final CustomerAccessToken customerAccessToken) {
        return getApiClient(path, customerAccessToken, null);
    }

    protected CrnkClient getApiClient(final String path, final CustomerAccessToken customerAccessToken, final UUID idempotency) {
        System.setProperty(CrnkProperties.RESOURCE_SEARCH_PACKAGE, "com.ibanity.apis");
        CrnkClient apiClient = new CrnkClient(path, CrnkClient.ClientType.OBJECT_LINKS);
        apiClient.getObjectMapper().registerModule(new Jdk8Module());
        apiClient.getObjectMapper().registerModule(new JavaTimeModule());
        apiClient.getObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        SimpleModule simpleModule = new SimpleModule("ErrorResponse");
        simpleModule.addExceptionMapper(new IbanityExceptionMapper());
        apiClient.addModule(simpleModule);

        HttpAdapter httpAdapter = apiClient.getHttpAdapter();
        if (httpAdapter instanceof HttpClientAdapter) {
            HttpClientAdapter adapter = (HttpClientAdapter) httpAdapter;
            adapter.addListener(new IbanityHttpAdapterListener(customerAccessToken, idempotency));
        }
        return apiClient;
    }
}
