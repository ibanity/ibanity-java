package com.ibanity.apis.client.products.xs2a.models.create;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class MetaRequestCreationQuery {

    private AuthorizationPortalCreationQuery authorizationPortalCreationQuery;
    private BigDecimal requestedPastTransactionDays;
}
