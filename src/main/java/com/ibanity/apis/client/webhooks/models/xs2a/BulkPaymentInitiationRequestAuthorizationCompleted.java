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
public class BulkPaymentInitiationRequestAuthorizationCompleted implements IbanityWebhookEvent {

    public final static String TYPE = "xs2a.bulkPaymentInitiationRequest.authorizationCompleted";

    private UUID id;
    private String type;
    private UUID bulkPaymentInitiationRequestId;
    private int deletedBefore;
    private Instant createdAt;

    public static Function<DataApiModel, BulkPaymentInitiationRequestAuthorizationCompleted> mappingFunction() {
        return dataApiModel -> {
            BulkPaymentInitiationRequestAuthorizationCompleted bulkPaymentInitiationRequestAuthorizationCompleted = toIbanityWebhooks(dataApiModel, BulkPaymentInitiationRequestAuthorizationCompleted.class);

            RelationshipsApiModel bulkPaymentInitiationRequestRelationship = dataApiModel.getRelationships().get("bulkPaymentInitiationRequest");
            if (bulkPaymentInitiationRequestRelationship != null) {
                bulkPaymentInitiationRequestAuthorizationCompleted.setBulkPaymentInitiationRequestId(fromString(bulkPaymentInitiationRequestRelationship.getData().getId()));
            }

            return bulkPaymentInitiationRequestAuthorizationCompleted;
        };
    }
}
