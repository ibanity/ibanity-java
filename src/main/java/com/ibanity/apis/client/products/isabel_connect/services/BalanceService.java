package com.ibanity.apis.client.products.isabel_connect.services;

import com.ibanity.apis.client.models.IsabelCollection;
import com.ibanity.apis.client.products.isabel_connect.models.Balance;
import com.ibanity.apis.client.products.isabel_connect.models.read.BalanceReadQuery;

public interface BalanceService {
    IsabelCollection<Balance> list(BalanceReadQuery query);
}
