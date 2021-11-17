package com.ibanity.apis.client.services;

import com.ibanity.apis.client.models.IbanityProduct;

public interface ApiUrlProvider {

    String find(IbanityProduct ibanityProduct, String... paths);

    String find(String rootPath, String... paths);

    void loadApiSchema(IbanityProduct ibanityProduct);
}
