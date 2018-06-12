package com.ibanity.apis.client.services.impl;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ibanity.apis.client.exceptions.IbanityException;
import com.ibanity.apis.client.models.AbstractModel;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.network.http.client.IbanityAccessTokenAdapterListener;
import com.ibanity.apis.client.services.configuration.IbanityConfiguration;
import io.crnk.client.CrnkClient;
import io.crnk.client.http.apache.HttpClientAdapter;
import io.crnk.core.boot.CrnkProperties;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.UUID;

public abstract class AbstractServiceImpl {
    private static final Logger LOGGER = LogManager.getLogger(AbstractServiceImpl.class);

    private static final String IBANITY_API_ENDPOINT = IbanityConfiguration.getConfiguration().getString(IbanityConfiguration.IBANITY_PROPERTIES_PREFIX + "api.endpoint");

    private static HashMap<String,CrnkClient> apiClients = new HashMap<>();

    protected static final String FINANCIAL_INSTITUTIONS_PATH               = "financial-institutions";
    protected static final String FINANCIAL_INSTITUTION_ID_TAG              = "<FI_ID>";
    protected static final String ACCOUNT_ID_TAG                            = "<ACCOUNT_ID>";
    protected static final String USER_ID_TAG                               = "<USER_ID>";
    protected static final String ACCOUNT_INFORMATION_ACCESS_REQUEST_ID_TAG = "<ACCOUNT_INFORMATION_ACCESS_REQUEST_ID>";


    public AbstractServiceImpl() {
    }

    protected synchronized CrnkClient getApiClient(String path){
        CrnkClient apiClient = null;
        if (apiClients.containsKey(path)) {
            apiClient = apiClients.get(path);
        }
        else {
            apiClient = getApiClient(path, null);
            apiClients.put(path, apiClient);
        }
        return apiClient;
    }

    protected <T extends AbstractModel> ResourceList<T> findAll(QuerySpec querySpec, ResourceRepositoryV2<T, UUID> repository) {
        try {
            return repository.findAll(querySpec);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new IbanityException(e.getMessage(), e);
        }
    }

    protected CrnkClient getApiClient(String path, CustomerAccessToken customerAccessToken){
        System.setProperty(CrnkProperties.RESOURCE_SEARCH_PACKAGE, "com.ibanity.apis");
        CrnkClient apiClient = new CrnkClient(IBANITY_API_ENDPOINT + path, CrnkClient.ClientType.OBJECT_LINKS);
        apiClient.getObjectMapper().registerModule(new Jdk8Module());
        apiClient.getObjectMapper().registerModule(new JavaTimeModule());
        apiClient.getObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        HttpClientAdapter adapter = (HttpClientAdapter) apiClient.getHttpAdapter();
        if (customerAccessToken == null){
            adapter.addListener(new IbanityAccessTokenAdapterListener());
        }
        else {
            adapter.addListener(new IbanityAccessTokenAdapterListener(customerAccessToken));
        }
        return apiClient;
    }
}
