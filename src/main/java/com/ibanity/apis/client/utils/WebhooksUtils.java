package com.ibanity.apis.client.utils;

import com.ibanity.apis.client.exceptions.IbanityRuntimeException;
import com.ibanity.apis.client.models.IbanityWebhookEvent;
import com.ibanity.apis.client.webhooks.models.xs2a.*;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static com.ibanity.apis.client.mappers.IbanityWebhookEventMapper.mapWebhookResource;
import static java.lang.String.format;

public class WebhooksUtils {

    private static final String DIGEST_ALGORITHM = MessageDigestAlgorithms.SHA_512;
    private static final int BUFFER_SIZE = 32_768;

    private static MessageDigest getMessageDigest() {
        try {
            return MessageDigest.getInstance(DIGEST_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Unsupported digest algorithm:" + DIGEST_ALGORITHM);
        }
    }

    public static String getDigest(String payload) {
        MessageDigest md = getMessageDigest();
        try (BufferedInputStream stream = new BufferedInputStream(new ByteArrayInputStream(payload.getBytes()))) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            while ((length = stream.read(buffer)) != -1) {
                md.update(buffer, 0, length);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not read payload.");
        }

        return Base64.getEncoder().encodeToString(md.digest());
    }

    public static IbanityWebhookEvent webhookEventParser(String payload, String type) {
        if (type.contains("xs2a.")) {
            return parseXs2aEvent(payload, type);
        } else {
            return parsePontoConnectEvent(payload, type);
        }
    }

    private static IbanityWebhookEvent parseXs2aEvent(String payload, String type) {
        switch (type) {
            case AccountDetailsUpdated.TYPE:
                return mapWebhookResource(payload, AccountDetailsUpdated.mappingFunction());
            case AccountTransactionsCreated.TYPE:
                return mapWebhookResource(payload, AccountTransactionsCreated.mappingFunction());
            case AccountTransactionsUpdated.TYPE:
                return mapWebhookResource(payload, AccountTransactionsUpdated.mappingFunction());
            case AccountTransactionsDeleted.TYPE:
                return mapWebhookResource(payload, AccountTransactionsDeleted.mappingFunction());
            case AccountPendingTransactionsCreated.TYPE:
                return mapWebhookResource(payload, AccountPendingTransactionsCreated.mappingFunction());
            case AccountPendingTransactionsUpdated.TYPE:
                return mapWebhookResource(payload, AccountPendingTransactionsUpdated.mappingFunction());
            case SynchronizationFailed.TYPE:
                return mapWebhookResource(payload, SynchronizationFailed.mappingFunction());
            case SynchronizationSucceededWithoutChange.TYPE:
                return mapWebhookResource(payload, SynchronizationSucceededWithoutChange.mappingFunction());
            case BulkPaymentInitiationRequestAuthorizationCompleted.TYPE:
                return mapWebhookResource(payload, BulkPaymentInitiationRequestAuthorizationCompleted.mappingFunction());
            case BulkPaymentInitiationRequestStatusUpdated.TYPE:
                return mapWebhookResource(payload, BulkPaymentInitiationRequestStatusUpdated.mappingFunction());
            case PaymentInitiationRequestAuthorizationCompleted.TYPE:
                return mapWebhookResource(payload, PaymentInitiationRequestAuthorizationCompleted.mappingFunction());
            case PaymentInitiationRequestStatusUpdated.TYPE:
                return mapWebhookResource(payload, PaymentInitiationRequestStatusUpdated.mappingFunction());
            case PeriodicPaymentInitiationRequestAuthorizationCompleted.TYPE:
                return mapWebhookResource(payload, PeriodicPaymentInitiationRequestAuthorizationCompleted.mappingFunction());
            case PeriodicPaymentInitiationRequestStatusUpdated.TYPE:
                return mapWebhookResource(payload, PeriodicPaymentInitiationRequestStatusUpdated.mappingFunction());
        }

        throw new IbanityRuntimeException(format("Event Type not handled by the java library \"%s\".", type));
    }

    private static IbanityWebhookEvent parsePontoConnectEvent(String payload, String type) {
        switch (type) {
            case com.ibanity.apis.client.webhooks.models.ponto_connect.AccountDetailsUpdated.TYPE:
                return mapWebhookResource(payload, com.ibanity.apis.client.webhooks.models.ponto_connect.AccountDetailsUpdated.mappingFunction());
            case com.ibanity.apis.client.webhooks.models.ponto_connect.AccountTransactionsCreated.TYPE:
                return mapWebhookResource(payload, com.ibanity.apis.client.webhooks.models.ponto_connect.AccountTransactionsCreated.mappingFunction());
            case com.ibanity.apis.client.webhooks.models.ponto_connect.AccountTransactionsUpdated.TYPE:
                return mapWebhookResource(payload, com.ibanity.apis.client.webhooks.models.ponto_connect.AccountTransactionsUpdated.mappingFunction());
            case com.ibanity.apis.client.webhooks.models.ponto_connect.SynchronizationFailed.TYPE:
                return mapWebhookResource(payload, com.ibanity.apis.client.webhooks.models.ponto_connect.SynchronizationFailed.mappingFunction());
            case com.ibanity.apis.client.webhooks.models.ponto_connect.SynchronizationSucceededWithoutChange.TYPE:
                return mapWebhookResource(payload, com.ibanity.apis.client.webhooks.models.ponto_connect.SynchronizationSucceededWithoutChange.mappingFunction());
            case com.ibanity.apis.client.webhooks.models.ponto_connect.IntegrationAccountAdded.TYPE:
                return mapWebhookResource(payload, com.ibanity.apis.client.webhooks.models.ponto_connect.IntegrationAccountAdded.mappingFunction());
            case com.ibanity.apis.client.webhooks.models.ponto_connect.IntegrationAccountRevoked.TYPE:
                return mapWebhookResource(payload, com.ibanity.apis.client.webhooks.models.ponto_connect.IntegrationAccountRevoked.mappingFunction());
            case com.ibanity.apis.client.webhooks.models.ponto_connect.IntegrationCreated.TYPE:
                return mapWebhookResource(payload, com.ibanity.apis.client.webhooks.models.ponto_connect.IntegrationCreated.mappingFunction());
            case com.ibanity.apis.client.webhooks.models.ponto_connect.IntegrationRevoked.TYPE:
                return mapWebhookResource(payload, com.ibanity.apis.client.webhooks.models.ponto_connect.IntegrationRevoked.mappingFunction());
            case com.ibanity.apis.client.webhooks.models.ponto_connect.OrganizationBlocked.TYPE:
                return mapWebhookResource(payload, com.ibanity.apis.client.webhooks.models.ponto_connect.OrganizationBlocked.mappingFunction());
            case com.ibanity.apis.client.webhooks.models.ponto_connect.OrganizationUnblocked.TYPE:
                return mapWebhookResource(payload, com.ibanity.apis.client.webhooks.models.ponto_connect.OrganizationUnblocked.mappingFunction());
            case com.ibanity.apis.client.webhooks.models.ponto_connect.AccountReauthorized.TYPE:
                return mapWebhookResource(payload, com.ibanity.apis.client.webhooks.models.ponto_connect.AccountReauthorized.mappingFunction());
        }

        throw new IbanityRuntimeException(format("Event Type not handled by the java library \"%s\".", type));
    }
}
