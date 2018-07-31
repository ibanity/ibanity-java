package com.ibanity.apis.client.services;

import com.ibanity.apis.client.models.Account;
import com.ibanity.apis.client.paging.IbanityPagingSpec;

import java.util.List;
import java.util.UUID;

public interface AccountsService {

    Account find(String customerAccessToken, UUID accountId, UUID financialInstitutionId);

    List<Account> list(String customerAccessToken);

    List<Account> list(String customerAccessToken, IbanityPagingSpec pagingSpec);

    List<Account> list(String customerAccessToken, UUID financialInstitutionId);

    List<Account> list(String customerAccessToken, UUID financialInstitutionId, IbanityPagingSpec pagingSpec);
}
