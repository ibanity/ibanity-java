package com.ibanity.samples;

import com.ibanity.apis.client.builders.IbanityServiceBuilder;
import com.ibanity.apis.client.builders.OptionalPropertiesBuilder;
import com.ibanity.apis.client.models.IbanityWebhookEvent;
import com.ibanity.apis.client.webhooks.models.xs2a.*;
import com.ibanity.apis.client.webhooks.services.WebhooksService;

import java.io.IOException;
import java.security.cert.CertificateException;

import static com.ibanity.apis.client.helpers.IbanityClientSecurityAuthenticationPropertiesKeys.*;
import static com.ibanity.apis.client.helpers.IbanityConfiguration.*;
import static com.ibanity.samples.helper.SampleHelper.loadCertificate;
import static com.ibanity.samples.helper.SampleHelper.loadPrivateKey;

public class WebhooksSample {
    private static final String JWT = "eyJhbGciOiJSUzUxMiIsImtpZCI6ImR1bW15XzEifQ.eyJhdWQiOiI1ODZjYzI1YS05NDc4LTRjOGEtOWIzYy0wMzQ3ZWRlZDIzOTAiLCJkaWdlc3QiOiJVaXhtaTRrUGtjUk5IMUpYUlg2eUVnWXZEakZ5ejAyUUdoVStNY2lrWjQ2SlZleE9sNTF4RnoyMFpvcXQ3NjNIeVQwVHJHTHFSSFoxcjVYYkp1UE5lQT09IiwiZXhwIjoxNjM3MDY4NDI3LCJpYXQiOjE2MzcwNjgzNjcsImlzcyI6Imh0dHBzOi8vYXBpLmRldmVsb3BtZW50LmliYW5pdHkubmV0IiwianRpIjoiYWI2MDg1YTQtM2ZjMi00NWUxLThjOTktMTY1MmYzYTk5ODI0In0.SFJLT5ql18qNYBQHrc-ne3cu3tnOfGNg3CkINcUUidb61-hJMaxYwEnOVGj-yzRDl0d8MksoZt8PavXcw2toW4uPESG8Q6r5mOcLMIUiKacdGbEDbcx01ocAPIJ1fPgND_XTTIt7PFkpcxLjbDk_W_DCFs7L0OHn6u9ltth75wePUw--PdnZLUnUpu6Vj2mKbIWBWH3foaGo0Z0uflvgCQmVnr8Csa5Fe0DrfNUgkI8b-abmz4cIDE76zb1VZDu0hecgR7p_zqjEZeOkIitwdNTJkrVlvWFaag8_lvuxAaWGy3CisGCs5-HCTqwbOglQRTU8kOb7oQFsxazGWfVofizXU7GEX8sbxqsIBlL1TR1MLB3UG0KB8OEYrW7kcL4XkQsMxguEi3oR6I1PsVd8rd-uJ6Z01iB-fiLtZXpQx1ojYGnzuuPDNCCwBLVpSela3eGgB90If4u_mohpaPdff4sfRQBW-Kmvk5All2R_AF3dBUeeRqWH7NklU6DoqdPw5s32tc8mMhHeo3r0LYy-K2v3e-1Gp9Qd8bUNNKaBTKzKJ7oEy5qhoDDxJMtre_iFWOkmKtEch4xr7nr0krrN7p5KCdIYpYdhZZ9y_06vlTuk0gMlT6Njbb181UqE-H0RxXRerG23KUCro0XFNpe0BX_g2GOOSgvQOGT3cwACjfk";
    private static final String PAYLOAD = "{\"data\":{\"attributes\":{\"createdAt\":\"2021-11-16T13:11:15.136Z\",\"synchronizationSubtype\":\"accountTransactions\"},\"id\":\"344f89b0-6c3c-420e-92ba-d03490c2adb0\",\"relationships\":{\"account\":{\"data\":{\"id\":\"3e3e7824-ebbf-4801-90eb-0cf5ef1edace\",\"type\":\"account\"}},\"synchronization\":{\"data\":{\"id\":\"92b12aac-039c-4eba-a9e9-a14acf83de36\",\"type\":\"synchronization\"}}},\"type\":\"xs2a.synchronization.succeededWithoutChange\"}}";

    public static void main(String[] args) throws CertificateException, IOException {
        String passphrase = getConfiguration(IBANITY_CLIENT_TLS_PRIVATE_KEY_PASSPHRASE_PROPERTY_KEY);
        OptionalPropertiesBuilder ibanityServiceBuilder = IbanityServiceBuilder.builder()
                .ibanityApiEndpoint(getConfiguration(IBANITY_API_ENDPOINT_PROPERTY_KEY))
                .tlsPrivateKey(loadPrivateKey(getConfiguration(IBANITY_CLIENT_TLS_PRIVATE_KEY_PATH_PROPERTY_KEY), passphrase))
                .passphrase(passphrase)
                .tlsCertificate(loadCertificate(getConfiguration(IBANITY_CLIENT_TLS_CERTIFICATE_PATH_PROPERTY_KEY)))
                .webhooksJwksCacheTTLMillis(30_000)
                .webhooksJwtClockSkewSeconds(3600000)
                .applicationId(getConfiguration(IBANITY_APPLICATION_ID))
                ;

        WebhooksService webhooksService = ibanityServiceBuilder.build().webhooksService();
        System.out.println(webhooksService.keys());
        IbanityWebhookEvent ibanityWebhookEvent = webhooksService.verifyAndParseEvent(PAYLOAD, JWT);
        switch (ibanityWebhookEvent.getType()) {
            case AccountTransactionsCreated.TYPE:
                System.out.println("AccountTransactionsCreated received");
                break;
            case AccountDetailsUpdated.TYPE:
                System.out.println("AccountDetailsUpdated received");
                break;
            case AccountTransactionsUpdated.TYPE:
                System.out.println("AccountTransactionsUpdated received");
                break;
            case SynchronizationFailed.TYPE:
                System.out.println("SynchronizationFailed received");
                break;
            case SynchronizationSucceededWithoutChange.TYPE:
                System.out.println("SynchronizationSucceededWithoutChange received");
                break;
        }


    }
}
