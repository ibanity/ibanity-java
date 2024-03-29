package com.ibanity.apis.client.webhooks.models.xs2a;

import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.RelationshipsApiModel;
import com.ibanity.apis.client.models.IbanityWebhookEvent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Function;

import static com.ibanity.apis.client.mappers.IbanityWebhookEventMapper.toIbanityWebhooks;
import static java.util.UUID.fromString;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentInitiationRequestAuthorizationCompleted implements IbanityWebhookEvent {

    public final static String TYPE = "xs2a.paymentInitiationRequest.authorizationCompleted";

    private UUID id;
    private String type;
    private UUID paymentInitiationRequestId;
    private String status;
    private Instant createdAt;

    public static Function<DataApiModel, PaymentInitiationRequestAuthorizationCompleted> mappingFunction() {
        return dataApiModel -> {
            PaymentInitiationRequestAuthorizationCompleted paymentInitiationRequestAuthorizationCompleted = toIbanityWebhooks(dataApiModel, PaymentInitiationRequestAuthorizationCompleted.class);

            RelationshipsApiModel paymentInitiationRequestRelationship = dataApiModel.getRelationships().get("paymentInitiationRequest");
            if (paymentInitiationRequestRelationship != null) {
                paymentInitiationRequestAuthorizationCompleted.setPaymentInitiationRequestId(fromString(paymentInitiationRequestRelationship.getData().getId()));
            }

            return paymentInitiationRequestAuthorizationCompleted;
        };
    }
}
