package com.ibanity.apis.client.configuration;

import com.ibanity.apis.client.services.impl.ApiServiceImpl;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.ClasspathLocationStrategy;
import org.apache.commons.configuration2.io.CombinedLocationStrategy;
import org.apache.commons.configuration2.io.FileLocationStrategy;
import org.apache.commons.configuration2.io.FileSystemLocationStrategy;
import org.apache.commons.configuration2.io.ProvidedURLLocationStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

public final class IbanityConfiguration {

    public static final String FORWARD_SLASH                        = "/";
    public static final String SANBOX_PREFIX_PATH                   = FORWARD_SLASH + "sandbox";
    public static final String CUSTOMER_PREFIX_PATH                 = FORWARD_SLASH + "customer";
    public static final String IBANITY_PROPERTIES_PREFIX            = "ibanity.";
    public static final String IBANITY_API_ENDPOINT_PROPERTY_KEY    = IBANITY_PROPERTIES_PREFIX + "api.endpoint";
    public static final String URL_PARAMETER_ID_POSTFIX             = "Id";

    private static final String PROPERTIES_FILE                     = "ibanity.properties";
    private static Configuration configuration                      = null;

    private static final Logger LOGGER = LogManager.getLogger(IbanityConfiguration.class);

    private static ApiIUrls apiIUrls = null;

    private IbanityConfiguration() {
    }

    public static Configuration getConfiguration() {
        if (configuration == null) {
            try {
                configuration = loadProperties();
            } catch (ConfigurationException e) {
                LOGGER.error(e);
            }
        }
        return configuration;
    }

    public static ApiIUrls getApiIUrls(){
        if (apiIUrls == null){
            apiIUrls = loadApiUrls();
        }
        return apiIUrls;
    }

    private static ApiIUrls loadApiUrls() {
        return new ApiServiceImpl().getApiUrls();
    }

    private static Configuration loadProperties() throws ConfigurationException {
        List<FileLocationStrategy> subs = Arrays.asList(
                new ProvidedURLLocationStrategy(),
                new FileSystemLocationStrategy(),
                new ClasspathLocationStrategy());
        FileLocationStrategy strategy = new CombinedLocationStrategy(subs);

        FileBasedConfigurationBuilder<PropertiesConfiguration> builder = new FileBasedConfigurationBuilder<PropertiesConfiguration>(
                PropertiesConfiguration.class).configure(
                new Parameters().fileBased().setLocationStrategy(strategy).setFileName(PROPERTIES_FILE));

        try {
            return builder.getConfiguration();
        } catch (ConfigurationException configurationException) {
            LOGGER.fatal(configurationException);
            throw configurationException;
        }
    }
}
