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
public class IntegrationCreated implements IbanityWebhookEvent {

    public final static String TYPE = "pontoConnect.integration.created";

    private UUID id;
    private String type;
    private UUID organization;
    private Instant createdAt;

    public static Function<DataApiModel, IntegrationCreated> mappingFunction() {
        return dataApiModel -> {
            IntegrationCreated integrationCreated = toIbanityWebhooks(dataApiModel, IntegrationCreated.class);

            RelationshipsApiModel organizationRelationship = dataApiModel.getRelationships().get("organization");
            if (organizationRelationship != null) {
                integrationCreated.setOrganization(fromString(organizationRelationship.getData().getId()));
            }

            return integrationCreated;
        };
    }

}
