package com.ibanity.apis.client.builders;

import java.security.cert.X509Certificate;

public interface ApplicationCertificateBuilder {

    OptionalPropertiesBuilder applicationCertificate(X509Certificate certificate);
}
