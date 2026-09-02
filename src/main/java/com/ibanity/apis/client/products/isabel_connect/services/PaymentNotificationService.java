package com.ibanity.apis.client.products.isabel_connect.services;

import com.ibanity.apis.client.models.IsabelCollection;
import com.ibanity.apis.client.products.isabel_connect.models.PaymentNotification;
import com.ibanity.apis.client.products.isabel_connect.models.read.PaymentNotificationsReadQuery;

public interface PaymentNotificationService {
    IsabelCollection<PaymentNotification> list(PaymentNotificationsReadQuery query);
}
