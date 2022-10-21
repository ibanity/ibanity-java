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
public class SynchronizationSucceededWithoutChange implements IbanityWebhookEvent {

    public final static String TYPE = "pontoConnect.synchronization.succeededWithoutChange";

    private UUID id;
    private String type;
    private UUID accountId;
    private UUID synchronizationId;
    private UUID organizationId;
    private String synchronizationSubtype;
    private Instant createdAt;

    public static Function<DataApiModel, SynchronizationSucceededWithoutChange> mappingFunction() {
        return dataApiModel -> {
            SynchronizationSucceededWithoutChange accountTransactionsCreated = toIbanityWebhooks(dataApiModel, SynchronizationSucceededWithoutChange.class);

            RelationshipsApiModel accountRelationship = dataApiModel.getRelationships().get("account");
            if (accountRelationship != null) {
                accountTransactionsCreated.setAccountId(fromString(accountRelationship.getData().getId()));
            }

            RelationshipsApiModel synchronizationRelationship = dataApiModel.getRelationships().get("synchronization");
            if (synchronizationRelationship != null) {
                accountTransactionsCreated.setSynchronizationId(fromString(synchronizationRelationship.getData().getId()));
            }

            RelationshipsApiModel organizationRelationship = dataApiModel.getRelationships().get("organization");
            if (organizationRelationship != null) {
                accountTransactionsCreated.setOrganizationId(fromString(organizationRelationship.getData().getId()));
            }

            return accountTransactionsCreated;
        };
    }
}
