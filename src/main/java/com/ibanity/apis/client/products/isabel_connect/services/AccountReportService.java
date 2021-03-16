package com.ibanity.apis.client.products.isabel_connect.services;

import com.ibanity.apis.client.models.IsabelCollection;
import com.ibanity.apis.client.products.isabel_connect.models.AccountReport;
import com.ibanity.apis.client.products.isabel_connect.models.read.AccountReportsReadQuery;

public interface AccountReportService {
    IsabelCollection<AccountReport> list(AccountReportsReadQuery query);
}
