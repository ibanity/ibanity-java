package com.ibanity.apis.client.models.links;

import io.crnk.core.resource.meta.MetaInformation;
import lombok.Data;

@Data
public class PaymentAccessLinks implements MetaInformation {

    private String redirect;

}
