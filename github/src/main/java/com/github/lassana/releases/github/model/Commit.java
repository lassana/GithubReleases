package com.github.lassana.releases.github.model;

/**
 * @author lassana
 * @since 1/10/14
 */
public class Commit {

    private final String mSha;
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
