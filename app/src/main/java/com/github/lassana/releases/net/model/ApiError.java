package com.github.lassana.releases.net.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author lassana
 * @since 1/13/14
 */
public class ApiError {

    @SerializedName("message")
    private final String mMessage;
    @SerializedName("documentation_url")
    private final String mDocumentationUrl;

    public ApiError(String message, String documentationUrl) {
        mMessage = message;
        mDocumentationUrl = documentationUrl;
    }

    public String getMessage() {
        return mMessage;
    }

    public String getDocumentationUrl() {
        return mDocumentationUrl;
    }

}
