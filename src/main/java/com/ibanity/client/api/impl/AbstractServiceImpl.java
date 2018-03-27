package com.ibanity.client.api.impl;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ibanity.client.api.configuration.IBanityConfiguration;
import com.ibanity.client.exceptions.IBanityException;
import com.ibanity.client.models.AbstractModel;
import com.ibanity.client.models.CustomerAccessToken;
import com.ibanity.client.network.http.client.IBanityAccessTokenAdapterListener;
import com.ibanity.client.paging.PagingBehavior;
import io.crnk.client.CrnkClient;
import io.crnk.client.http.apache.HttpClientAdapter;
import io.crnk.core.engine.internal.information.resource.DefaultResourceFieldInformationProvider;
import io.crnk.core.engine.internal.information.resource.DefaultResourceInformationProvider;
import io.crnk.core.engine.internal.jackson.JacksonResourceFieldInformationProvider;
import io.crnk.core.engine.properties.NullPropertiesProvider;
import io.crnk.core.module.Module;
import io.crnk.core.module.SimpleModule;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.UUID;

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

    protected <T extends AbstractModel> ResourceList<T> findAll(QuerySpec querySpec, ResourceRepositoryV2<T, UUID> repository) {
        try {
            return repository.findAll(querySpec);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new IBanityException(e.getMessage(), e);
        }
    }

    protected CrnkClient getApiClient(String path, CustomerAccessToken customerAccessToken){
        CrnkClient apiClient = new CrnkClient(IBANITY_API_ENDPOINT + path, CrnkClient.ClientType.OBJECT_LINKS);
        apiClient.getObjectMapper().registerModule(new Jdk8Module());
        apiClient.getObjectMapper().registerModule(new JavaTimeModule());
        apiClient.addModule(getIBanityModule(apiClient.getModuleRegistry().getContext()));
        HttpClientAdapter adapter = (HttpClientAdapter) apiClient.getHttpAdapter();
        if (customerAccessToken == null){
            adapter.addListener(new IBanityAccessTokenAdapterListener());
        }
        else {
            adapter.addListener(new IBanityAccessTokenAdapterListener(customerAccessToken));
        }
        return apiClient;
    }

    private Module getIBanityModule(Module.ModuleContext context){
        SimpleModule iBanityModule = new SimpleModule("iBanity");
        iBanityModule.addResourceInformationProvider(new DefaultResourceInformationProvider(
                new NullPropertiesProvider()
                , new PagingBehavior()
                , new DefaultResourceFieldInformationProvider()
                , new JacksonResourceFieldInformationProvider()
        ));

        iBanityModule.setupModule(context);
        return iBanityModule;
    }
}
