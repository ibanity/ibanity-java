package com.ibanity.apis.client.webhooks.models.isabel_connect;

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

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentStatusUpdated implements IbanityWebhookEvent {

    public final static String TYPE = "isabelConnect.payment.status.updated";

    private UUID id;
    private String type;
    private String notificationType;
    private Instant createdAt;
    private String paymentId;

    public static Function<DataApiModel, PaymentStatusUpdated> mappingFunction() {
        return dataApiModel -> {
            PaymentStatusUpdated paymentStatusUpdated = toIbanityWebhooks(dataApiModel, PaymentStatusUpdated.class);

            RelationshipsApiModel paymentRelationship = dataApiModel.getRelationships().get("payment");
            if (paymentRelationship != null) {
                paymentStatusUpdated.setPaymentId(paymentRelationship.getData().getId());
            }

            return paymentStatusUpdated;
        };
    }
}
