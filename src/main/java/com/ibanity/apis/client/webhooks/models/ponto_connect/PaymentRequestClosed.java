package com.ibanity.apis.client.webhooks.models.ponto_connect;

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
public class PaymentRequestClosed implements IbanityWebhookEvent {

    public final static String TYPE = "pontoConnect.paymentRequest.closed";

    private UUID id;
    private String type;
    private UUID accountId;
    private UUID paymentRequestId;
    private UUID organizationId;
    private Instant createdAt;

    public static Function<DataApiModel, PaymentRequestClosed> mappingFunction() {
        return dataApiModel -> {
            PaymentRequestClosed paymentRequestClosed = toIbanityWebhooks(dataApiModel, PaymentRequestClosed.class);

            RelationshipsApiModel accountRelationship = dataApiModel.getRelationships().get("account");
            if (accountRelationship != null) {
                paymentRequestClosed.setAccountId(fromString(accountRelationship.getData().getId()));
            }

            RelationshipsApiModel organizationRelationship = dataApiModel.getRelationships().get("organization");
            if (organizationRelationship != null) {
                paymentRequestClosed.setOrganizationId(fromString(organizationRelationship.getData().getId()));
            }

            RelationshipsApiModel paymentRequestRelationship = dataApiModel.getRelationships().get("paymentRequest");
            if (paymentRequestRelationship != null) {
                paymentRequestClosed.setPaymentRequestId(fromString(paymentRequestRelationship.getData().getId()));
            }

            return paymentRequestClosed;
        };
    }

}
