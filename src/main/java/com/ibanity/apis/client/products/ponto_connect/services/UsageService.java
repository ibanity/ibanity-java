package com.ibanity.apis.client.products.ponto_connect.services;

import com.ibanity.apis.client.products.ponto_connect.models.OrganizationUsage;
import com.ibanity.apis.client.products.ponto_connect.models.read.OrganizationUsageReadQuery;

public interface UsageService {

    OrganizationUsage getOrganizationUsage(OrganizationUsageReadQuery readQuery);
}
