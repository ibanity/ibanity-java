package com.ibanity.apis.client.products.ponto_connect.services;

import com.ibanity.apis.client.products.ponto_connect.models.OnboardingDetails;
import com.ibanity.apis.client.products.ponto_connect.models.create.OnboardingDetailsCreateQuery;

public interface OnboardingDetailsService {

    OnboardingDetails create(OnboardingDetailsCreateQuery onboardingDetailsCreateQuery);
}
