package com.ibanity.apis.client.utils;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerRequestException;
import com.spotify.docker.client.exceptions.ImagePullFailedException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class DockerHelper {
    private static final Logger LOGGER = LogManager.getLogger(DockerHelper.class);
    private static final String APACHE_WIRE_LOGGING_PACKAGE = "org.apache.http.wire";
    private static final String IBANITY_SANDBOX_AUTHORIZATION_CLI_DOCKER_IMAGE      = "ibanity/sandbox-authorization-cli:latest";

    public static void pullImage() throws Exception{
        // Disable Apache Http Client Wire logging during Docker pull to prevent tonnes of logs
        Logger apacheWireLogger = LogManager.getLogger(APACHE_WIRE_LOGGING_PACKAGE);
        Level currentLogLevel = apacheWireLogger.getLevel();
        Configurator.setLevel(APACHE_WIRE_LOGGING_PACKAGE, Level.ERROR);

        try {
            // Create a client based on DOCKER_HOST and DOCKER_CERT_PATH env vars
            final DockerClient docker = DefaultDockerClient.fromEnv().build();

            int maxDockerPullRetry = 10;
            while (maxDockerPullRetry-- > 0) {
                try {
                    LOGGER.info("Pulling docker image " + IBANITY_SANDBOX_AUTHORIZATION_CLI_DOCKER_IMAGE + " for the test...");
                    docker.pull(IBANITY_SANDBOX_AUTHORIZATION_CLI_DOCKER_IMAGE);
                    LOGGER.info("Pull done");
                    break;
                } catch (DockerRequestException dockerRequestException) {
                    LOGGER.warn("Failed to pull docker image " + dockerRequestException.getResponseBody() + ". Retrying...");
                }
            }
            if (maxDockerPullRetry == 0) {
                throw new ImagePullFailedException(IBANITY_SANDBOX_AUTHORIZATION_CLI_DOCKER_IMAGE,
                        "Failed to pull docker image after 10 retries");
            }
        } finally {
            Configurator.setLevel(APACHE_WIRE_LOGGING_PACKAGE, currentLogLevel);
        }
    }
}
