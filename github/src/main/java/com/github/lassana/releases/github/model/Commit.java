package com.github.lassana.releases.github.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author lassana
 * @since 1/10/14
 */
public class Commit {

    @SerializedName("sha")
    private final String mSha;
    @SerializedName("url")
    private final String mUrl;

    public Commit(String sha, String url) {
        mSha = sha;
        mUrl = url;
    }

    public String getSha() {
        return mSha;
    }

    public String getUrl() {
        return mUrl;
    }
}
