package com.ibanity.apis.client.products.xs2a.models.links;

import lombok.*;

@Getter
@Builder
@EqualsAndHashCode
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class PaymentInitiationRequestAuthorizationLinks {

    private String nextRedirect;

}
