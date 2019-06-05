package com.ibanity.apis.client.builders;

import java.security.cert.X509Certificate;

public interface TlsCertificateBuilder {

    OptionalPropertiesBuilder tlsCertificate(X509Certificate certificate);
}
