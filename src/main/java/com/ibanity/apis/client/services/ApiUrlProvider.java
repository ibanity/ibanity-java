package com.ibanity.apis.client.services;

public interface ApiUrlProvider {

    String find(String... paths);

    void loadApiSchema();
}
