package com.ibanity.apis.client.models.links;

import io.crnk.core.resource.meta.MetaInformation;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class PaymentAccessLinks implements MetaInformation {

    private String redirect;

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("redirect", redirect)
                .toString();
    }
}
