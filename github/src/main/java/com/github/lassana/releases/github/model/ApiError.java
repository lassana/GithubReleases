package com.github.lassana.releases.github.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author lassana
 * @since 1/13/14
 */
public class ApiError {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("documentation_url")
    private String mDocumentationUrl;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getDocumentationUrl() {
        return mDocumentationUrl;
    }

    public void setDocumentationUrl(String documentationUrl) {
        mDocumentationUrl = documentationUrl;
    }
}
