package com.ibanity.apis.client.helpers;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.FileBasedBuilderParameters;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.ClasspathLocationStrategy;
import org.apache.commons.configuration2.io.CombinedLocationStrategy;
import org.apache.commons.configuration2.io.FileLocationStrategy;
import org.apache.commons.configuration2.io.FileSystemLocationStrategy;
import org.apache.commons.configuration2.io.ProvidedURLLocationStrategy;

import java.util.Arrays;
import java.util.List;

public final class IbanityConfiguration {

    public static final String IBANITY_PROPERTIES_PREFIX = "ibanity.";
    public static final String IBANITY_API_ENDPOINT_PROPERTY_KEY = "api.endpoint";

    private static final String PROPERTIES_FILE = "ibanity.properties";

    private static Configuration configuration = null;

    private IbanityConfiguration() {
    }

    public static String getConfiguration(final String configurationKey) {
        return getConfiguration(configurationKey, null);
    }

    public static String getConfiguration(final String configurationKey, String defaultValue) {
        return getConfigurationInstance().getString(IBANITY_PROPERTIES_PREFIX + configurationKey, defaultValue);
    }

    private static synchronized Configuration getConfigurationInstance() {
        if (configuration == null) {
            try {
                configuration = loadProperties();
            } catch (ConfigurationException configurationException) {
                throw new IllegalStateException(PROPERTIES_FILE + " couldn't be found", configurationException);
            }
        }
        return configuration;
    }


    private static Configuration loadProperties() throws ConfigurationException {
        List<FileLocationStrategy> subs = Arrays.asList(
                new ProvidedURLLocationStrategy(),
                new FileSystemLocationStrategy(),
                new ClasspathLocationStrategy());

        FileLocationStrategy strategy = new CombinedLocationStrategy(subs);

        FileBasedBuilderParameters builderParameters = new Parameters()
                .fileBased()
                .setLocationStrategy(strategy)
                .setFileName(PROPERTIES_FILE);

        return new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                .configure(builderParameters)
                .getConfiguration();
    }
}
