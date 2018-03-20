package com.ibanity.api.impl;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ibanity.api.configuration.IBanityConfiguration;
import com.ibanity.models.CustomerAccessToken;
import com.ibanity.network.http.client.IBanityAccessTokenAdapterListener;
import io.crnk.client.CrnkClient;
import io.crnk.client.http.apache.HttpClientAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public abstract class AbstractServiceImpl {
    private static final Logger LOGGER = LogManager.getLogger(AbstractServiceImpl.class);

    private static final String IBANITY_API_ENDPOINT = IBanityConfiguration.getConfiguration().getString(IBanityConfiguration.IBANITY_PROPERTIES_PREFIX + "api.endpoint");

    private static HashMap<String,CrnkClient> apiClients = new HashMap<>();

    protected static final String FINANCIAL_INSTITUTION_ID_TAG = "<FI_ID>";
    protected static final String ACCOUNT_ID_TAG = "<ACCOUNT_ID>";
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

    protected CrnkClient getApiClient(String path, CustomerAccessToken customerAccessToken){
        CrnkClient apiClient = new CrnkClient(IBANITY_API_ENDPOINT + path);
        apiClient.getObjectMapper().registerModule(new Jdk8Module());
        apiClient.getObjectMapper().registerModule(new JavaTimeModule());
        HttpClientAdapter adapter = (HttpClientAdapter) apiClient.getHttpAdapter();
        if (customerAccessToken == null){
            adapter.addListener(new IBanityAccessTokenAdapterListener());
        }
        adapter.addListener(new IBanityAccessTokenAdapterListener(customerAccessToken));
        return apiClient;
    }
}
