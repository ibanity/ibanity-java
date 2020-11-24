package com.ibanity.apis.client.products.ponto_connect.services;

import com.ibanity.apis.client.products.ponto_connect.models.Integration;
import com.ibanity.apis.client.products.ponto_connect.models.delete.OrganizationIntegrationDeleteQuery;

public interface IntegrationService {

    Integration delete(OrganizationIntegrationDeleteQuery organizationIntegrationDeleteQuery);
}
