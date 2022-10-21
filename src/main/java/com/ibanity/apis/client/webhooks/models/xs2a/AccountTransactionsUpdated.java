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
public class AccountTransactionsUpdated implements IbanityWebhookEvent {

    public final static String TYPE = "xs2a.account.transactionsUpdated";

    private UUID id;
    private String type;
    private UUID accountId;
    private UUID synchronizationId;
    private UUID batchSynchronizationId;
    private int count;
    private Instant createdAt;

    public static Function<DataApiModel, AccountTransactionsUpdated> mappingFunction() {
        return dataApiModel -> {
            AccountTransactionsUpdated accountTransactionsCreated = toIbanityWebhooks(dataApiModel, AccountTransactionsUpdated.class);

            RelationshipsApiModel accountRelationship = dataApiModel.getRelationships().get("account");
            if (accountRelationship != null) {
                accountTransactionsCreated.setAccountId(fromString(accountRelationship.getData().getId()));
            }

            RelationshipsApiModel synchronizationRelationship = dataApiModel.getRelationships().get("synchronization");
            if (synchronizationRelationship != null) {
                accountTransactionsCreated.setSynchronizationId(fromString(synchronizationRelationship.getData().getId()));
            }

            RelationshipsApiModel batchSynchronizationRelationship = dataApiModel.getRelationships().get("batchSynchronization");
            if (batchSynchronizationRelationship != null) {
                accountTransactionsCreated.setBatchSynchronizationId(fromString(batchSynchronizationRelationship.getData().getId()));
            }

            return accountTransactionsCreated;
        };
    }
}
