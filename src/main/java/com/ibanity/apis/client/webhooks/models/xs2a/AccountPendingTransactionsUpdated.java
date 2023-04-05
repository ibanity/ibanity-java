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
public class AccountPendingTransactionsUpdated implements IbanityWebhookEvent {

    public final static String TYPE = "xs2a.account.pendingTransactionsUpdated";

    private UUID id;
    private String type;
    private UUID accountId;
    private UUID synchronizationId;
    private UUID batchSynchronizationId;
    private int count;
    private Instant createdAt;

    public static Function<DataApiModel, AccountPendingTransactionsUpdated> mappingFunction() {
        return dataApiModel -> {
            AccountPendingTransactionsUpdated accountPendingTransactionsCreated = toIbanityWebhooks(dataApiModel, AccountPendingTransactionsUpdated.class);

            RelationshipsApiModel accountRelationship = dataApiModel.getRelationships().get("account");
            if (accountRelationship != null) {
                accountPendingTransactionsCreated.setAccountId(fromString(accountRelationship.getData().getId()));
            }

            RelationshipsApiModel synchronizationRelationship = dataApiModel.getRelationships().get("synchronization");
            if (synchronizationRelationship != null) {
                accountPendingTransactionsCreated.setSynchronizationId(fromString(synchronizationRelationship.getData().getId()));
            }

            RelationshipsApiModel batchSynchronizationRelationship = dataApiModel.getRelationships().get("batchSynchronization");
            if (batchSynchronizationRelationship != null) {
                accountPendingTransactionsCreated.setBatchSynchronizationId(fromString(batchSynchronizationRelationship.getData().getId()));
            }

            return accountPendingTransactionsCreated;
        };
    }
}
