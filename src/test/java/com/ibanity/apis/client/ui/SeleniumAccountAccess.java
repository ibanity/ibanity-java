package com.ibanity.apis.client.ui;

import com.ibanity.apis.client.configuration.SeleniumConfig;

public class SeleniumAccountAccess {
    private SeleniumConfig config;
    private String accountInformationAuthorizationUrl;

    public SeleniumAccountAccess(SeleniumConfig config) {
        this.config = new SeleniumConfig();
    }

    public void closeWindow() {
        this.config.getDriver().close();
    }

    public String getAccountInformationAuthorizationUrl() {
        return accountInformationAuthorizationUrl;
    }

    public void setAccountInformationAuthorizationUrl(String accountInformationAuthorizationUrl) {
        this.accountInformationAuthorizationUrl = accountInformationAuthorizationUrl;
    }
}
