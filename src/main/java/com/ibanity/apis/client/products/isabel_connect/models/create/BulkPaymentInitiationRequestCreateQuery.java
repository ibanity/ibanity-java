package com.ibanity.apis.client.products.isabel_connect.models.create;

import lombok.Data;

import java.io.File;
import java.util.Map;

import static java.util.Collections.emptyMap;

@Data
public class BulkPaymentInitiationRequestCreateQuery {
    private String accessToken;
    private Boolean shared;
    private Boolean hideDetails;
    private String content;
    private File file;
    private String filename;

    private Map<String, String> additionalHeaders = emptyMap();

    private BulkPaymentInitiationRequestCreateQuery(String accessToken,
                                                    Boolean shared,
                                                    Boolean hideDetails,
                                                    File file,
                                                    String filename,
                                                    Map<String, String> additionalHeaders) {
        this.accessToken = accessToken;
        this.shared = shared;
        this.hideDetails = hideDetails;
        this.file = file;
        this.filename = filename;
        this.additionalHeaders = additionalHeaders;
    }

    private BulkPaymentInitiationRequestCreateQuery(String accessToken,
                                                    Boolean shared,
                                                    Boolean hideDetails,
                                                    String content,
                                                    String filename,
                                                    Map<String, String> additionalHeaders) {
        this.accessToken = accessToken;
        this.shared = shared;
        this.hideDetails = hideDetails;
        this.content = content;
        this.filename = filename;
        this.additionalHeaders = additionalHeaders;
    }

    private static Map<String, String> defaultAdditionalHeaders() {
        return emptyMap();
    }

    public static BulkPaymentInitiationRequestCreateQueryBuilder builder() {
        return new BulkPaymentInitiationRequestCreateQueryBuilder();
    }

    public static class BulkPaymentInitiationRequestCreateQueryBuilder {
        private String accessToken;
        private Boolean shared;
        private Boolean hideDetails;
        private File file;
        private String content;
        private String filename;
        private Map<String, String> additionalHeaders;
        private boolean hasAdditionalHeaders;

        BulkPaymentInitiationRequestCreateQueryBuilder() {
        }

        public BulkPaymentInitiationRequestCreateQueryBuilder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public BulkPaymentInitiationRequestCreateQueryBuilder shared(Boolean shared) {
            this.shared = shared;
            return this;
        }

        public BulkPaymentInitiationRequestCreateQueryBuilder hideDetails(Boolean hideDetails) {
            this.hideDetails = hideDetails;
            return this;
        }

        public BulkPaymentInitiationRequestCreateQueryBuilder file(File file) {
            this.file = file;
            return this;
        }

        public BulkPaymentInitiationRequestCreateQueryBuilder content(String content) {
            this.content = content;
            return this;
        }

        public BulkPaymentInitiationRequestCreateQueryBuilder filename(String filename) {
            this.filename = filename;
            return this;
        }

        public BulkPaymentInitiationRequestCreateQueryBuilder additionalHeaders(Map<String, String> additionalHeaders) {
            this.additionalHeaders = additionalHeaders;
            this.hasAdditionalHeaders = true;
            return this;
        }

        public BulkPaymentInitiationRequestCreateQuery build() {
            Map<String, String> additionalHeaders = this.additionalHeaders;
            if (!this.hasAdditionalHeaders) {
                additionalHeaders = BulkPaymentInitiationRequestCreateQuery.defaultAdditionalHeaders();
            }

            if (this.content != null && this.file != null)
                throw new IllegalStateException("At most one of 'content' or 'file' must be present but both are provided");

            if (this.content != null) {
                return new BulkPaymentInitiationRequestCreateQuery(accessToken, shared, hideDetails, content, filename, additionalHeaders);
            }

            if (this.file != null) {
                return new BulkPaymentInitiationRequestCreateQuery(accessToken, shared, hideDetails, file, filename, additionalHeaders);
            }

            throw new IllegalStateException("At least one of 'content' or 'file' should be provided");
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("BulkPaymentInitiationRequestCreateQueryBuilder{");
            sb.append("accessToken='").append(accessToken).append('\'');
            sb.append(", shared=").append(shared);
            sb.append(", hideDetails=").append(hideDetails);
            if (file != null) sb.append(", file=").append(file);
            if (content != null) sb.append(", content=").append(content);
            sb.append(", filename='").append(filename).append('\'');
            sb.append(", additionalHeaders=").append(additionalHeaders);
            sb.append(", hasAdditionalHeaders=").append(hasAdditionalHeaders);
            sb.append('}');
            return sb.toString();
        }
    }
}
