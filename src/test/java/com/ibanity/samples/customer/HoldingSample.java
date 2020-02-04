package com.ibanity.samples.customer;

import com.ibanity.apis.client.products.xs2a.models.Account;
import com.ibanity.apis.client.products.xs2a.models.CustomerAccessToken;
import com.ibanity.apis.client.products.xs2a.models.FinancialInstitution;
import com.ibanity.apis.client.products.xs2a.models.Holding;
import com.ibanity.apis.client.products.xs2a.models.read.HoldingReadQuery;
import com.ibanity.apis.client.products.xs2a.models.read.HoldingsReadQuery;
import com.ibanity.apis.client.products.xs2a.services.HoldingsService;
import com.ibanity.apis.client.services.IbanityService;

import java.util.List;
import java.util.UUID;

public class HoldingSample {

    private final HoldingsService holdingsService;

    public HoldingSample(IbanityService ibanityService) {
        holdingsService = ibanityService.xs2aService().holdingsService();
    }

    public List<Holding> list(CustomerAccessToken customerAccessToken, FinancialInstitution financialInstitution, Account account) {
        HoldingsReadQuery holdingsReadQuery = HoldingsReadQuery.builder()
                .customerAccessToken(customerAccessToken.getToken())
                .financialInstitutionId(financialInstitution.getId())
                .accountId(account.getId())
                .build();
        return holdingsService.list(holdingsReadQuery).getItems();
    }

    public Holding get(CustomerAccessToken customerAccessToken, FinancialInstitution financialInstitution, Account account, UUID holdingId) {
        HoldingReadQuery readQuery = HoldingReadQuery.builder()
                .customerAccessToken(customerAccessToken.getToken())
                .financialInstitutionId(financialInstitution.getId())
                .accountId(account.getId())
                .holdingId(holdingId)
                .build();
        return holdingsService.find(readQuery);
    }
}
