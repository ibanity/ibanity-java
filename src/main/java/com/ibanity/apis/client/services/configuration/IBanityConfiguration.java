package com.ibanity.apis.client.services.configuration;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

public class IBanityConfiguration {

    public static final String IBANITY_PROPERTIES_PREFIX = "ibanity.";

    private static final String PROPERTIES_FILE = "ibanity.properties";
    private static Configuration CONFIGURATION = null;

    private static final Logger LOGGER = LogManager.getLogger(IBanityConfiguration.class);

    public static Configuration getConfiguration() {
        if (CONFIGURATION == null) {
            CONFIGURATION = loadProperties();
        }
        return CONFIGURATION;
    }

    public static Configuration loadProperties(){
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
        }
        catch(ConfigurationException configurationException) {
            LOGGER.fatal(configurationException);
            throw new RuntimeException("Unable to load IBANITY config", configurationException);
        }
    }
}
