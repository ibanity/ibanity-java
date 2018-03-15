package com.ibanity.api.impl;

import com.ibanity.api.configuration.IbanityConfiguration;
import com.ibanity.models.CustomerAccessToken;
import com.ibanity.network.http.client.HttpClientIbanityIntegration;
import io.crnk.client.CrnkClient;
import io.crnk.client.http.apache.HttpClientAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public abstract class AbstractServiceImpl {
    private static final Logger LOGGER = LogManager.getLogger(AbstractServiceImpl.class);

    private static final String IBANITY_API_ENDPOINT = IbanityConfiguration.getConfiguration().getString(IbanityConfiguration.IBANITY_PROPERTIES_PREFIX + "api.endpoint");

    private static HashMap<String,CrnkClient> apiClients = new HashMap<>();

    public AbstractServiceImpl() {
    }

    protected synchronized CrnkClient getApiClient(String path){
        CrnkClient apiClient = null;
        if (apiClients.containsKey(path)) {
            apiClient = apiClients.get(path);
        }
        else {
            apiClient = new CrnkClient(IBANITY_API_ENDPOINT + path);
            HttpClientAdapter adapter = (HttpClientAdapter) apiClient.getHttpAdapter();
            adapter.addListener(new HttpClientIbanityIntegration());
            apiClients.put(path, apiClient);
        }
        return apiClient;
    }

    protected CrnkClient getApiClient(String path, CustomerAccessToken customerAccessToken){
        CrnkClient apiClient = new CrnkClient(IBANITY_API_ENDPOINT + path);
        HttpClientAdapter adapter = (HttpClientAdapter) apiClient.getHttpAdapter();
        adapter.addListener(new HttpClientIbanityIntegration(customerAccessToken));
        return apiClient;
    }
}
