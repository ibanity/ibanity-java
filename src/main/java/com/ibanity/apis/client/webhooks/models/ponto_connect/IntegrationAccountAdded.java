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
public class IntegrationAccountAdded implements IbanityWebhookEvent {

    public final static String TYPE = "pontoConnect.integration.accountAdded";

    private UUID id;
    private String type;
    private UUID account;
    private UUID organization;
    private Instant createdAt;

    public static Function<DataApiModel, IntegrationAccountAdded> mappingFunction() {
        return dataApiModel -> {
            IntegrationAccountAdded integrationAccountAdded = toIbanityWebhooks(dataApiModel, IntegrationAccountAdded.class);

            RelationshipsApiModel organizationRelationship = dataApiModel.getRelationships().get("organization");
            if (organizationRelationship != null) {
                integrationAccountAdded.setOrganization(fromString(organizationRelationship.getData().getId()));
            }


            RelationshipsApiModel accountRelationship = dataApiModel.getRelationships().get("account");
            if (accountRelationship != null) {
                integrationAccountAdded.setAccount(fromString(accountRelationship.getData().getId()));
            }

            return integrationAccountAdded;
        };
    }

}
