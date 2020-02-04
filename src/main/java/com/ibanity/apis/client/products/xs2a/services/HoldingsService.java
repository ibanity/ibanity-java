package com.ibanity.apis.client.products.xs2a.services;

import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.products.xs2a.models.Holding;
import com.ibanity.apis.client.products.xs2a.models.read.HoldingReadQuery;
import com.ibanity.apis.client.products.xs2a.models.read.HoldingsReadQuery;

public interface HoldingsService {

    IbanityCollection<Holding> list(HoldingsReadQuery holdingsReadQuery);

    Holding find(HoldingReadQuery holdingReadQuery);
}
