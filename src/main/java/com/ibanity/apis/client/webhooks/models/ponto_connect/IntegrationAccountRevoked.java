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
public class IntegrationAccountRevoked implements IbanityWebhookEvent {

    public final static String TYPE = "pontoConnect.integration.accountRevoked";

    private UUID id;
    private String type;
    private UUID account;
    private UUID organization;
    private Instant createdAt;

    public static Function<DataApiModel, IntegrationAccountRevoked> mappingFunction() {
        return dataApiModel -> {
            IntegrationAccountRevoked integrationAccountRevoked = toIbanityWebhooks(dataApiModel, IntegrationAccountRevoked.class);

            RelationshipsApiModel organizationRelationship = dataApiModel.getRelationships().get("organization");
            if (organizationRelationship != null) {
                integrationAccountRevoked.setOrganization(fromString(organizationRelationship.getData().getId()));
            }

            RelationshipsApiModel accountRelationship = dataApiModel.getRelationships().get("account");
            if (accountRelationship != null) {
                integrationAccountRevoked.setAccount(fromString(accountRelationship.getData().getId()));
            }

            return integrationAccountRevoked;
        };
    }

}
