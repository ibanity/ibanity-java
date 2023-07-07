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
public class AccountTransactionsDeleted implements IbanityWebhookEvent {

    public final static String TYPE = "xs2a.account.transactionsDeleted";

    private UUID id;
    private String type;
    private UUID accountId;
    private int count;
    private Instant createdAt;

    public static Function<DataApiModel, AccountTransactionsDeleted> mappingFunction() {
        return dataApiModel -> {
            AccountTransactionsDeleted accountTransactionsDeleted = toIbanityWebhooks(dataApiModel, AccountTransactionsDeleted.class);

            RelationshipsApiModel accountRelationship = dataApiModel.getRelationships().get("account");
            if (accountRelationship != null) {
                accountTransactionsDeleted.setAccountId(fromString(accountRelationship.getData().getId()));
            }

            return accountTransactionsDeleted;
        };
    }
}
