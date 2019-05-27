package com.ibanity.apis.client.builders;

import java.security.cert.X509Certificate;

public interface RequestSignatureCertificateBuilder {

    RequestSignatureCertificateIdBuilder requestSignatureCertificate(X509Certificate certificate);
}
