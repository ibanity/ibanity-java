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
public class PeriodicPaymentInitiationRequestStatusUpdated implements IbanityWebhookEvent {

    public final static String TYPE = "xs2a.periodicPaymentInitiationRequest.statusUpdated";

    private UUID id;
    private String type;
    private UUID periodicPaymentInitiationRequestId;
    private int deletedBefore;
    private Instant createdAt;

    public static Function<DataApiModel, PeriodicPaymentInitiationRequestStatusUpdated> mappingFunction() {
        return dataApiModel -> {
            PeriodicPaymentInitiationRequestStatusUpdated periodicPaymentInitiationRequestStatusUpdated = toIbanityWebhooks(dataApiModel, PeriodicPaymentInitiationRequestStatusUpdated.class);

            RelationshipsApiModel periodicPaymentInitiationRequestRelationship = dataApiModel.getRelationships().get("periodicPaymentInitiationRequest");
            if (periodicPaymentInitiationRequestRelationship != null) {
                periodicPaymentInitiationRequestStatusUpdated.setPeriodicPaymentInitiationRequestId(fromString(periodicPaymentInitiationRequestRelationship.getData().getId()));
            }

            return periodicPaymentInitiationRequestStatusUpdated;
        };
    }
}
